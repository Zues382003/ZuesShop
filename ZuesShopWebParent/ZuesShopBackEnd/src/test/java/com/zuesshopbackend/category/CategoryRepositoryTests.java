package com.zuesshopbackend.category;

import com.zuesshop.entity.Category;
import com.zuesshopbackend.category.Repository.CategoryReposity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoryRepositoryTests {
    @Autowired
    private CategoryReposity repo;

    @Test
    public void testCreateRootCategory(){
        Category category = new Category("Computers");
        Category saveCategory = repo.save(category);

        assertThat(saveCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateSubCategory(){
        Category parent = new Category(7);
        Category subCategory1 = new Category("Iphone",parent);

        repo.save(subCategory1);

    }

    @Test
    public void testGetCategory(){
        Category category = repo.findById(1).get();
        System.out.println(category.getName());

        Set<Category> children = category.getChildren();

        for(Category subCategory: children){
            System.out.println(subCategory.getName());
        }

        assertThat(children.size()).isGreaterThan(0);
    }

    @Test
    public void testPrintHierarchicalCategories(){
        Iterable<Category> categories = repo.findAll();

        for(Category category : categories){
            if(category.getParent() == null){
                System.out.println(category.getName());

                printChildren(category,0);
            }
        }
    }

    private void printChildren(Category parent, int subLevel){
        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();

        for(Category subCategory : children){
            for(int i = 0; i< newSubLevel; i++)
            {
                System.out.print("-");
            }

            System.out.println(subCategory.getName());
            printChildren(subCategory,newSubLevel);
        }
    }
}
