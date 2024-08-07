package com.zuesshopbackend.product.Service;

import com.zuesshop.entity.Product;
import com.zuesshop.exception.ProductNotFoundException;
import com.zuesshopbackend.product.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ProductService {
    public static final int PRODUCT_PER_PAGE = 5;
    @Autowired
    private ProductRepository repo;
    public List<Product> listAll(){
        return (List<Product>) repo.findAll();
    }

    public Page<Product> listByPage(int pageNum, String sortField, String sortDirection, String keyword, Integer categoryId){
        Sort sort = Sort.by(sortField);

        sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1,PRODUCT_PER_PAGE,sort);

        if(keyword != null && !keyword.isEmpty()){
            if(categoryId !=null && categoryId > 0){
                String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
                return repo.searchInCategory(categoryId,categoryIdMatch,keyword,pageable);
            }
            return repo.findAll(keyword,pageable);
        }

        if(categoryId !=null && categoryId > 0){
            String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
            return repo.findAllInCategory(categoryId,categoryIdMatch,pageable);
        }
        return repo.findAll(pageable);
    }

    public Product save(Product product){
        if(product.getId() == null){
            product.setCreatedTime(new Date());
        }else{
            product.setUpdatedTime(new Date());
        }

        if(product.getAlias() == null || product.getAlias().isEmpty()){
            String defaultAlias = product.getName().replaceAll(" ","-");
            product.setAlias(defaultAlias);
        }else{
            product.setAlias(product.getAlias().replaceAll(" ", "-"));
        }
        return repo.save(product);
    }

    public void saveProductPrice(Product productInForm){
        Product productInDB = repo.findById(productInForm.getId()).get();
        productInDB.setCost(productInForm.getCost());
        productInDB.setPrice(productInForm.getPrice());
        productInDB.setDiscountPercent(productInForm.getDiscountPercent());

        repo.save(productInDB);
    }

    public String checkUnique(Integer id, String name){
        boolean isCreatingNew = (id == null || id ==0);
        Product product = repo.findByName(name);

        if(isCreatingNew){
            if(product != null){
                return "Duplicate";
            }
        }else{
            if(product != null && product.getId() != id){
                return "Duplicate";
            }
        }

        return "OK";
    }
    public void updateStatus(Integer id, boolean enabled){
        repo.updateEnabledStatus(id, enabled);
    }
    public void deleteProduct(Integer id) throws ProductNotFoundException {
        Long countById = repo.countById(id);
        if(countById == null || countById ==0){
            throw new ProductNotFoundException("Could find find any product with ID: " + id);
        }
        repo.deleteById(id);
    }

    public Product getById(Integer id) throws ProductNotFoundException {
        try {
            return repo.findById(id).get();// Khi không tìm thấy id này trong cơ sở dữ liệu sẽ ném ra NoSuchElementException
        } catch (NoSuchElementException ex) {
            throw new ProductNotFoundException("Could not find any product with ID " + id);
        }
    }
}
