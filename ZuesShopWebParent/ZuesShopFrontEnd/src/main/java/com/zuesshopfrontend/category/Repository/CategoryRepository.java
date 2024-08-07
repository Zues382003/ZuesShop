package com.zuesshopfrontend.category.Repository;

import com.zuesshop.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer>{
    @Query("select c from Category c where c.enabled = true order by c.name asc ")
    public List<Category> findAllEnabled();

    @Query("select c from Category c where c.enabled = true and c.alias = ?1")
    public Category getCategory(String alias);
}
