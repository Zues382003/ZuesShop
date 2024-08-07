package com.zuesshopbackend.category.Repository;

import com.zuesshop.entity.Category;
import com.zuesshop.entity.User;
import org.hibernate.mapping.Selectable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CategoryReposity extends PagingAndSortingRepository<Category,Integer> , CrudRepository<Category,Integer> {
    @Query("select c from Category c where c.parent.id is null")
    public List<Category> listRootCategory(Sort sort);

    @Query("select c from Category c where c.parent.id is null")
    public Page<Category> listRootCategory(Pageable pageable);

    @Query("select c from Category c where c.name like %?1%"
            + "or c.id like %?1%")
    public Page<Category> search(String keyword,Pageable pageable);
    public Long countById(Integer id);
    public Category findByName(String name);
    public Category findByAlias(String alias);

    @Query("UPDATE Category c set c.enabled = ?2 where c.id = ?1")
    @Modifying
    public void updateEnabledStatus(Integer id, boolean enabled);
}
