package com.zuesshopbackend.brand.Controller;

import com.zuesshop.entity.Brand;
import com.zuesshop.entity.Category;
import com.zuesshop.exception.BrandNotFoundException;
import com.zuesshopbackend.brand.BrandNotFoundRestException;
import com.zuesshopbackend.brand.CategoryDTO;
import com.zuesshopbackend.brand.Service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class BrandRestController {
    @Autowired
    private BrandService brandService;

    @PostMapping("/brands/check_unique")
    public String checkUniqueBrand(@Param("id")Integer id, @Param("name")String name){
        return brandService.checkUniqueBrand(id,name.trim());
    }

    @GetMapping("/brands/{id}/categories")
    public List<CategoryDTO> listCategoriesByBrand(@PathVariable("id")Integer id) throws BrandNotFoundRestException {
        List<CategoryDTO> listCategories = new ArrayList<>();
        try{
            Brand brand = brandService.getById(id);
            Set<Category> categories = brand.getCategories();

            for(Category category : categories){
                CategoryDTO categoryDTO = new CategoryDTO(category.getId(),category.getName());
                listCategories.add(categoryDTO);
            }
            return listCategories;
        }catch (BrandNotFoundException ex){
            throw new BrandNotFoundRestException();
        }
    }
}
