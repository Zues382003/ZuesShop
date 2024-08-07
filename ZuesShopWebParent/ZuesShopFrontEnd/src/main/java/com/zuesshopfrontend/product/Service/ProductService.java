package com.zuesshopfrontend.product.Service;

import com.zuesshop.entity.Product;
import com.zuesshop.exception.ProductNotFoundException;
import com.zuesshopfrontend.product.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    public final static int PRODUCTS_PER_PAGE = 10;
    public final static int SEARCH_PRODUCTS_PER_PAGE = 10;
    @Autowired
    private ProductRepository repo;

    public Page<Product> listByCategory(int pageNum , Integer categoryId){
        String categoryIdmatch = "-" + String.valueOf(categoryId) + "-";

        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);
        return repo.getListByCategory(categoryId, categoryIdmatch,pageable);
    }

    public Product getProductByAlias(String alias) throws ProductNotFoundException {
        Product product = repo.findByAlias(alias);
        if(product == null){
            throw new ProductNotFoundException("Could not find product with alias " + alias);
        }
        return product;
    }

    public Page<Product> search(String keyword, int pageNum){
        Pageable pageable = PageRequest.of(pageNum - 1, SEARCH_PRODUCTS_PER_PAGE);

        return repo.search(keyword, pageable);
    }
}
