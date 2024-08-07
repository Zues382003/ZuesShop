package com.zuesshopbackend.category.Controller;

import com.zuesshop.entity.Category;
import com.zuesshop.exception.CategoryNotFoundException;
import com.zuesshopbackend.FileUploadUtil;
import com.zuesshopbackend.category.CategoryPageInfo;
import com.zuesshopbackend.category.Export.CategoryExcelExporter;
import com.zuesshopbackend.category.Service.CategoryService;
import com.zuesshopbackend.category.Export.CategoryCsvExporter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CategoryController {
    @Autowired
    private CategoryService service;

    @GetMapping("/categories")//Nếu trên trong giá trị Param không giống với tên String như dưới sẽ lỗi cmn.
    public String listFirstPage(@Param(value = "sortDirection") String sortDirection, Model model) throws CategoryNotFoundException {
        return listByPage(1,"name","asc", model,null);
    }

    @GetMapping("/categories/page/{pageNumber}")
    public String listByPage(@PathVariable("pageNumber")int numberPage,
                             @Param("sortField")String sortField,
                             @Param("sortDirection")String sortDirection, Model model,
                             @Param("keyword")String keyword) throws CategoryNotFoundException {
        CategoryPageInfo categoryPageInfo = new CategoryPageInfo();

        List<Category> listCategory = service.listByPage(numberPage,categoryPageInfo, sortField, sortDirection, keyword);

        String reverseSortDirection = sortDirection.equals("asc") ? "desc" : "asc";


        if(keyword != null){
            model.addAttribute("totalItems", categoryPageInfo.getTotalElements());
        }else {
            model.addAttribute("totalItems", service.totalCategories());
        }
        model.addAttribute("sortField",sortField);
        model.addAttribute("sortDirection",sortDirection);
        model.addAttribute("totalPages", categoryPageInfo.getTotalPages());
        model.addAttribute("currentPage", numberPage);
        model.addAttribute("listCategory",listCategory);
        model.addAttribute("reverseSortDirection",reverseSortDirection);
        model.addAttribute("keyword",keyword);


        return "/categories/categories";
    }

    @GetMapping("/categories/new")
    public String newCategory(Model model){
        List<Category> listCategory = service.listCategoryUsedInForm();

        model.addAttribute("listCategory",listCategory);
        model.addAttribute("category",new Category());
        model.addAttribute("pageTitle","Create New Category");
        return "/categories/category_form";
    }

    @PostMapping("/categories/save")
    public String saveCategory(Category category,
                               @RequestParam("FileImage") MultipartFile multipartFile, // nếu tên input file ở các form giống nhau sẽ bị lỗi
                                RedirectAttributes redirectAttributes) throws IOException, CategoryNotFoundException {
        if(!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            category.setImage(fileName);

            Category saveCategory = service.save(category);
            String uploadDir = "../category-images/" + saveCategory.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);

        }else{
            if(category.getImage().isEmpty()){
                category.setImage("default_category.jpg");
            }
            service.save(category);
        }
        redirectAttributes.addFlashAttribute("messageSuccess","The category has been saved successfully!");

        return "redirect:/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable(name="id")Integer id, Model model,
                               RedirectAttributes redirectAttributes){
        try{
            Category category = service.getByID(id);
            List<Category> listCategoryUsedInForm = service.listCategoryUsedInForm();

            model.addAttribute("category", category);
            model.addAttribute("listCategory",listCategoryUsedInForm);
            model.addAttribute("pageTitle", "Edit Category (ID:"+ id +")");

            return "/categories/category_form";

        }catch(CategoryNotFoundException ex){
            redirectAttributes.addFlashAttribute("messageError",ex.getMessage());
            return "redirect:/categories";
        }
    }

    @GetMapping("/categories/{id}/enabled/{status}")
    public String updateCategoryEnabledStatus(@PathVariable("id")Integer id,
            @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) throws CategoryNotFoundException {
        service.updateCategoryEnableStatus(id,enabled);
        Category category = service.getByID(id);

        String status = enabled ? "enabled" : "disable";
        String message = "The category ID: " + id + " has been " +status;
        redirectAttributes.addFlashAttribute("messageSuccess",message);

        return "redirect:/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id")Integer id, RedirectAttributes redirectAttributes){
        try{
            service.delete(id);
            String categoryDir = "./category-images/" + id;
            FileUploadUtil.removeDir(categoryDir);

            redirectAttributes.addFlashAttribute("messageSuccess", "The category with ID: " + id + " has been deleted successfully!");
        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("messageError", e.getMessage());
        }

        return "redirect:/categories";
    }

    @GetMapping("/categories/export/csv")
    public void exportToCsv(HttpServletResponse response) throws IOException {
        List<Category> listCategory = service.listCategoryUsedInForm();
        CategoryCsvExporter exporter = new CategoryCsvExporter();
        exporter.export(listCategory, response);
    }

    @GetMapping("/categories/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<Category> listCategory = service.listCategoryUsedInForm();
        CategoryExcelExporter exporter = new CategoryExcelExporter();
        exporter.export(listCategory, response);
    }

}
