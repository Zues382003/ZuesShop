package com.zuesshopbackend.brand.Repository;

import com.zuesshop.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends PagingAndSortingRepository<Brand,Integer>, CrudRepository<Brand, Integer> {
    public Long countById(Integer id);
    public Brand findByName(String name);
    @Query("select b from Brand b where b.name like %?1%")
    public Page<Brand> searchAndSort(String keyword, Pageable pageable);

    @Query("select new Brand(b.id, b.name) from Brand b order by b.name asc")
    public List<Brand> findAll();// chỉ lấy id và name cho lẹ
}
