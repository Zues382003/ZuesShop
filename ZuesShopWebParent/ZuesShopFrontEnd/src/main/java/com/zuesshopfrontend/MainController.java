package com.zuesshopfrontend;

import com.zuesshop.entity.Category;
import com.zuesshopfrontend.category.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public String viewHomePage(Model model){
        List<Category> listCategories = categoryService.listNoChildrenCategories();

        model.addAttribute("listCategories", listCategories);
        return "index";
    }


}
