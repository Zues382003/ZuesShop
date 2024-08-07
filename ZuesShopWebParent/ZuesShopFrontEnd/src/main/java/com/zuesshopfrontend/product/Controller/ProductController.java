package com.zuesshopfrontend.product.Controller;

import com.zuesshop.entity.Category;
import com.zuesshop.entity.Product;
import com.zuesshop.exception.CategoryNotFoundException;
import com.zuesshop.exception.ProductNotFoundException;
import com.zuesshopfrontend.category.Service.CategoryService;
import com.zuesshopfrontend.product.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/c/{category_alias}")
    public String viewCategoryFirstPage(@PathVariable("category_alias") String alias, Model model) throws CategoryNotFoundException {
        return viewCategoryByPage(alias, 1, model);
    }

    @GetMapping("/c/{category_alias}/page/{pageNum}")
    public String viewCategoryByPage(@PathVariable("category_alias") String alias, @PathVariable("pageNum") int pageNum, Model model) throws CategoryNotFoundException {
        try {
            Category category = categoryService.getCategory(alias);
            if (category == null) {
                return "error/404";
            }

            List<Category> listCategoryParents = categoryService.getCategoryParents(category);

            Page<Product> page = productService.listByCategory(pageNum, category.getId());
            List<Product> listProducts = page.getContent();

            model.addAttribute("currentPage", pageNum);
            model.addAttribute("totalItems", page.getTotalElements());
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("listProducts", listProducts);

            model.addAttribute("pageTitle", category.getName());
            model.addAttribute("category", category);
            model.addAttribute("listCategoryParents", listCategoryParents);

            return "product/product_by_category";
        } catch (CategoryNotFoundException e) {
            return "error/404";
        }
    }
    @GetMapping("/p/{product_alias}")
    public String viewProductDetail(@PathVariable("product_alias") String alias, Model model){
        try{
            Product product = productService.getProductByAlias(alias);
            List<Category> listCategoryParents = categoryService.getCategoryParents(product.getCategory());

            model.addAttribute("listCategoryParents", listCategoryParents);
            model.addAttribute("product",product);
            model.addAttribute("pageTitle", product.getShortName());

            return "product/product_detail";
        } catch (ProductNotFoundException e) {
            return "error/404";
        }
    }

    @GetMapping("/search")
    public String searchFirstPage(@Param("keyword") String keyword, Model model){

        return searchByPage(keyword,1,model);
    }

    @GetMapping("/search/page/{pageNum}")
    public String searchByPage(@Param("keyword") String keyword,
                               @PathVariable("pageNum") int pageNum, Model model){
        Page<Product> page = productService.search(keyword,pageNum);
        List<Product> listProducts = page.getContent();

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("listProducts", listProducts);

        model.addAttribute("pageTitle", keyword + " - Search Result");
        model.addAttribute("keyword", keyword);
        return "product/search_result";
    }
}
