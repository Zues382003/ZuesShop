package com.zuesshopbackend.product.Controller;

import com.zuesshop.entity.Brand;
import com.zuesshop.entity.Category;
import com.zuesshop.entity.Product;
import com.zuesshop.exception.CategoryNotFoundException;
import com.zuesshop.exception.ProductNotFoundException;
import com.zuesshopbackend.FileUploadUtil;
import com.zuesshopbackend.brand.Service.BrandService;
import com.zuesshopbackend.category.Service.CategoryService;
import com.zuesshopbackend.product.Export.ProductCsvExporter;
import com.zuesshopbackend.product.Export.ProductExcelExporter;
import com.zuesshopbackend.product.Service.ProductService;
import com.zuesshopbackend.security.ShopZuesUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
    public String listByFirst(Model model) {
        return listByPage(1, model, "name", "asc", null, 0);
    }

    @GetMapping("/products/page/{pageNum}")
    public String listByPage(
            @PathVariable(name = "pageNum") int pageNum, Model model,
            @Param("sortField") String sortField, @Param("sortDirection") String sortDirection,
            @Param("keyword") String keyword,
            @Param("categoryId") Integer categoryId) {
        Page<Product> page = productService.listByPage(pageNum, sortField, sortDirection, keyword, categoryId);
        List<Product> listProducts = page.getContent();
        List<Category> listCategories = categoryService.listCategoryUsedInForm();

        String reverseSortDirection = sortDirection.equals("asc") ? "desc" : "asc";
        if (categoryId != null) {
            model.addAttribute("categoryId", categoryId);
        }

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", reverseSortDirection);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("listCategories", listCategories);

        return "/products/products";
    }

    @GetMapping("/products/new")
    public String createProduct(Model model) {
        List<Brand> listBrands = brandService.listBrands();

        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);

        model.addAttribute("listBrands", listBrands);
        model.addAttribute("product", product);
        model.addAttribute("pageTitle", "Create New Product");
        model.addAttribute("numberOfExistingExtraImage", 0);


        return "/products/product_form";
    }

    @PostMapping("/products/save")
    public String saveProduct(Product product, @RequestParam(name = "FileImageProduct", required = false) MultipartFile mainMultipartFile,
                              @RequestParam(name = "extraImage", required = false) MultipartFile[] extraMultipartFile, RedirectAttributes redirectAttributes,
                              @RequestParam(name = "detailNames", required = false) String[] detailNames,
                              @RequestParam(name = "detailValues", required = false) String[] detailValue,
                              @RequestParam(name = "imageIDs", required = false/*can return null if empty*/) String[] imageIDs,
                              @RequestParam(name = "imageNames", required = false) String[] imageNames,
                              @RequestParam(name = "detailIDs", required = false) String[] detailIDs,
                              @AuthenticationPrincipal ShopZuesUserDetails loggedUser
                              ) throws IOException {
        if(loggedUser.hasRole("Salesperson")){
            productService.saveProductPrice(product);
            redirectAttributes.addFlashAttribute("messageSuccess", "The product has been saved successfully.");
            return "redirect:/products";
        }

        ProductSaveHelper.setMainImageName(mainMultipartFile, product);
        ProductSaveHelper.setExistingExtraImageNames(imageIDs, imageNames, product);
        ProductSaveHelper.setNewExtraImageNames(extraMultipartFile, product);
        ProductSaveHelper.setProductDetails(detailIDs, detailNames, detailValue, product);

        Product saveProduct = productService.save(product);

        ProductSaveHelper.saveUploadImages(mainMultipartFile, extraMultipartFile, saveProduct);

        ProductSaveHelper.deleteExtraImageWereRemoveOnForm(product);

        redirectAttributes.addFlashAttribute("messageSuccess", "The product has been saved successfully.");
        return "redirect:/products";
    }

    @GetMapping("/products/{id}/enabled/{status}")
    public String updateCategoryEnabledStatus(@PathVariable("id") Integer id,
                                              @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) throws CategoryNotFoundException, ProductNotFoundException {
        Product product = productService.getById(id);
        productService.updateStatus(id, enabled);
        String status = enabled ? "enabled" : "disable";
        String message = "The category ID: " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("messageSuccess", message);

        return "redirect:/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id, RedirectAttributes re) {
        try {
            productService.deleteProduct(id);
            String removeExtrasDir = "./product-images/" + id + "/extras";
            String removeImagesDir = "./product-images/" + id;
            FileUploadUtil.removeDir(removeExtrasDir);
            FileUploadUtil.removeDir(removeImagesDir);
            re.addFlashAttribute("messageSuccess", "The product ID: " + id + " has been deleted successfully.");

        } catch (ProductNotFoundException e) {
            re.addFlashAttribute("messageError", e.getMessage());
        }
        return "redirect:/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        try {
            Product product = productService.getById(id);
            List<Brand> listBrands = brandService.listBrands();

            Integer numberOfExistingExtraImage = product.getImages().size();

            model.addAttribute("product", product);
            model.addAttribute("numberOfExistingExtraImage", numberOfExistingExtraImage);
            model.addAttribute("listBrands", listBrands);
            model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");

            return "/products/product_form";
        } catch (ProductNotFoundException e) {
            ra.addFlashAttribute("messageError", e.getMessage());
            return "redirect:/products";
        }
    }

    @GetMapping("/products/detail/{id}")
    public String viewProductDetail(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        try {
            Product product = productService.getById(id);

            model.addAttribute("product", product);

            return "/products/product_detail_modal";
        } catch (ProductNotFoundException e) {
            ra.addFlashAttribute("messageError", e.getMessage());
            return "redirect:/products";
        }
    }

    @GetMapping("/products/export/csv")
    public void exportToCsv(HttpServletResponse response) throws IOException {
        List<Product> listProduct = productService.listAll();
        ProductCsvExporter exporter = new ProductCsvExporter();
        exporter.export(listProduct, response);
    }

    @GetMapping("/products/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<Product> listProduct = productService.listAll();
        ProductExcelExporter exporter = new ProductExcelExporter();
        exporter.export(listProduct, response);
    }

}
