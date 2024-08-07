package com.zuesshopbackend.brand.Controller;

import com.zuesshop.entity.Brand;
import com.zuesshop.entity.Category;
import com.zuesshop.exception.BrandNotFoundException;
import com.zuesshopbackend.FileUploadUtil;
import com.zuesshopbackend.brand.Export.BrandCsvExporter;
import com.zuesshopbackend.brand.Export.BrandExcelExporter;
import com.zuesshopbackend.brand.Service.BrandService;
import com.zuesshopbackend.category.Service.CategoryService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class BrandController {
    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/brands")
    public String listByFirst(Model model) throws BrandNotFoundException {
        return listByPage(1,"asc", "name",null, model);
    }

    @GetMapping("/brands/page/{pageNumber}")
    public String listByPage(@PathVariable("pageNumber")int pageNumber,
                             @Param("sortDirection")String sortDirection,
                             @Param("sortField") String sortField,
                             @Param("keyword")String keyword ,Model model) throws BrandNotFoundException {
        if(sortDirection == null || sortDirection.isEmpty()){
            sortDirection = "asc";
        }
        Page<Brand> pageBrands = brandService.listByPage(pageNumber,sortField,sortDirection,keyword);

        List<Brand> listBrands = pageBrands.getContent();

        String reverseSortDirection = sortDirection.equals("asc") ? "desc" : "asc";

        model.addAttribute("sortField",sortField);
        model.addAttribute("sortDirection",sortDirection);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("listBrands",listBrands);
        model.addAttribute("reverseSortDirection",reverseSortDirection);
        model.addAttribute("keyword",keyword);
        model.addAttribute("totalItems",pageBrands.getTotalElements());
        model.addAttribute("totalPages", pageBrands.getTotalPages());

        return "/brands/brands";
    }

    @GetMapping("/brands/new")
    public String newBrand(Model model){
        List<Category> listCategories = categoryService.listCategoryUsedInForm();

        model.addAttribute("brand", new Brand());
        model.addAttribute("listCategories",listCategories);
        model.addAttribute("pageTitle","Create New Brand");

        return "/brands/brand_form";
    }

    @PostMapping("/brands/save")
    public String savebrand(Brand brand, @RequestParam("FileImageBrand")MultipartFile multipartFile, RedirectAttributes redirectAttributes) throws IOException {
        if(!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            brand.setLogo(fileName);

            Brand saveBrand = brandService.save(brand);
            String uploadDir = "../brand-logos/" + saveBrand.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }else{
            if(brand.getLogo().isEmpty()){
                brand.setLogo("default_logo.jpg");
            }
            brandService.save(brand);
        }
        redirectAttributes.addFlashAttribute("messageSuccess", "The brand has been save successfully!");

        return "redirect:/brands";
    }

    @GetMapping("/brands/edit/{id}")
    public String editBrand(@PathVariable("id")Integer id, Model model, RedirectAttributes redirectAttributes){
        try{
            Brand brand = brandService.getById(id);
            List<Category> listCategories = categoryService.listCategoryUsedInForm();


            model.addAttribute("brand", brand);
            model.addAttribute("listCategories",listCategories);
            model.addAttribute("pageTitle","Edit Brand (ID:"+ id +")");

            return "/brands/brand_form";
        }catch (BrandNotFoundException ex){
            redirectAttributes.addFlashAttribute("messageError",ex.getMessage());
            return "redirect:/brands";
        }
    }

    @GetMapping("/brands/delete/{id}")
    public String deleteBrand(@PathVariable("id")Integer id, RedirectAttributes redirectAttributes){
        try{
            brandService.delete(id);
            String brandDir = "../brand-logos/" + id;

            FileUploadUtil.removeDir(brandDir);
            redirectAttributes.addFlashAttribute("messageSuccess", "The brand with ID: " + id + " has been deleted successfully!");
        }catch (BrandNotFoundException ex){
            redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
        }
        return "redirect:/brands";
    }

    @GetMapping("/brands/export/csv")
    public void exportToCsv(HttpServletResponse response) throws IOException {
        List<Brand> listBrands = brandService.listBrands();
        BrandCsvExporter exporter = new BrandCsvExporter();
        exporter.export(listBrands, response);
    }

    @GetMapping("/brands/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<Brand> listBrands = brandService.listBrands();
        BrandExcelExporter exporter = new BrandExcelExporter();
        exporter.export(listBrands, response);
    }
}
