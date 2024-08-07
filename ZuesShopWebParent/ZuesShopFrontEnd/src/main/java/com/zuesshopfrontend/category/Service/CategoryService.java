package com.zuesshopfrontend.category.Service;

import com.zuesshop.entity.Category;
import com.zuesshop.exception.CategoryNotFoundException;
import com.zuesshopfrontend.category.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repo;

    public List<Category> listNoChildrenCategories(){
        List<Category> listNoChildrenCategories = new ArrayList<>();

        List<Category> listEnabledCategories = repo.findAllEnabled();

        listEnabledCategories.forEach(category -> {
            Set<Category> children = category.getChildren();
            if(children == null || children.size() == 0){
                listNoChildrenCategories.add(category);
            }
        });

        return listNoChildrenCategories;
    }

    public Category getCategory(String alias) throws CategoryNotFoundException {
        Category category = repo.getCategory(alias);
        if(category == null){
            throw new CategoryNotFoundException("Could not find any category with alias " + alias);
        }
        return category;
    }

    public List<Category> getCategoryParents(Category category){
        List<Category> listParents = new ArrayList<>();

        listParents.add(category);

        while(category.getParent() != null){
            listParents.add(0,category.getParent());
            category = category.getParent();
        }

        return listParents;
    }
}
