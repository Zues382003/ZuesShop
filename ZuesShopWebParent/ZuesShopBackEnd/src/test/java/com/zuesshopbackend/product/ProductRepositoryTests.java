package com.zuesshopbackend.product;

import com.zuesshop.entity.Brand;
import com.zuesshop.entity.Category;
import com.zuesshop.entity.Product;
import com.zuesshopbackend.product.Repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Date;
import java.util.Optional;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void createProduct1(){
        Brand brand = entityManager.find(Brand.class,10);
        Category category = entityManager.find(Category.class,15);

        Product product = new Product();
        product.setName("Samsung Galaxy A31");
        product.setAlias("samsung_galaxy_a31");
        product.setShortDescription("short description samsung");
        product.setFullDescription("full description samsung");

        product.setBrand(brand);
        product.setCategory(category);

        product.setPrice(456);
        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        Product saveProduct = repo.save(product);

        assertThat(saveProduct).isNotNull();
        assertThat(saveProduct.getId()).isGreaterThan(0);


    }

    @Test
    public void createProduct2(){
        Brand brand = entityManager.find(Brand.class,10);
        Category category = entityManager.find(Category.class,15);

        Product product = new Product();
        product.setName("Samsung Galaxy A33");
        product.setAlias("samsung_galaxy_a33");
        product.setShortDescription("short description samsung");
        product.setFullDescription("full description samsung");
        product.setEnabled(true);
        product.setInStock(true);

        product.setBrand(brand);
        product.setCategory(category);

        product.setPrice(1024);
        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        Product saveProduct = repo.save(product);

        assertThat(saveProduct).isNotNull();
        assertThat(saveProduct.getId()).isGreaterThan(0);

    }

    @Test
    public void testListAllProduct(){
        Iterable<Product> listProducts = repo.findAll();

        listProducts.forEach(System.out::println);
    }

    @Test
    public void testGetProduct(){
        Integer id = 2;
        Optional<Product> product = repo.findById(id);
        System.out.println(product);
        assertThat(product).isNotNull();
    }

    @Test
    public void testUpdateProduct(){
        Product product = repo.findById(2).get();
        product.setName("Samsung A99");

        Product saveProduct = repo.save(product);
        System.out.println(saveProduct);
    }

    @Test
    public void testDeleteProduct(){
        repo.deleteById(3);
        Optional<Product> product = repo.findById(3);

        assertThat(!product.isPresent());
    }
}
