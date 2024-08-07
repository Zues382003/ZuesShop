package com.zuesshopbackend.brand.Service;

import com.zuesshop.entity.Brand;
import com.zuesshop.exception.BrandNotFoundException;
import com.zuesshopbackend.brand.Repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BrandService {
    @Autowired
    private BrandRepository repo;
    public static final int BRAND_PER_PAGE = 6;

    public List<Brand> listBrands(){
        return (List<Brand>) repo.findAll();
    }

    public Brand save(Brand brand) {
        return repo.save(brand);
    }

    public Page<Brand> listByPage(int pageNumber, String sortField ,String sortDirection, String keyword) {
        Sort sort = Sort.by(sortField);

        sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber-1, BRAND_PER_PAGE, sort);
        if(keyword != null){
            return repo.searchAndSort(keyword, pageable);
        }else{
            return  repo.findAll(pageable);
        }
    }


    public Brand getById(Integer id) throws BrandNotFoundException {
        try{
            return repo.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new BrandNotFoundException("Could not find brand with ID: " + id);
        }
    }

    public void delete(Integer id) throws BrandNotFoundException {
        Long countById = repo.countById(id);
        if(countById == 0){
            throw new BrandNotFoundException("Could not find any brand with ID: " + id);
        }
        repo.deleteById(id);
    }

    public String checkUniqueBrand(Integer id , String name){
        boolean isCreatingNew = (id == null || id ==0);
        Brand brand = repo.findByName(name);

        if(isCreatingNew){
            if(brand != null){
                return "Duplicate";
            }
        }else{
            if(brand != null && brand.getId() != id){
                return "Duplicate";
            }
        }

        return "OK";
    }
}
