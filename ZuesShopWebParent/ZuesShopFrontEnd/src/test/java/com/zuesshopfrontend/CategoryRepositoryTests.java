package com.zuesshopfrontend;

import com.zuesshop.entity.Category;
import com.zuesshopfrontend.category.Repository.CategoryRepository;
import com.zuesshopfrontend.category.Service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTests {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testListEnabledCategories(){
        List<Category> listEnabledCategories = categoryRepository.findAllEnabled();
        for (Category category : listEnabledCategories){
            System.out.print(category);
        }
    }
}
