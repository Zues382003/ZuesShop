package com.zuesshopbackend.brand;
import com.zuesshop.entity.Brand;
import com.zuesshop.entity.Category;
import com.zuesshopbackend.brand.Repository.BrandRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class BrandRepositoryTests {
    @Autowired
    private BrandRepository repo;

    @Test
    public void testCreateBrand1(){
        Brand acer = new Brand("Acer");
        Category laptops = new Category(6);

        acer.getCategories().add(laptops);
        Brand savedAcer = repo.save(acer);

        assertThat(savedAcer).isNotNull();
        assertThat(savedAcer.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateBrand2(){
        Brand apple = new Brand("Apple");
        Category tablet = new Category(7);
        Category cellphones = new Category(4);

        apple.getCategories().add(tablet);
        apple.getCategories().add(cellphones);

        Brand saveApple = repo.save(apple);

        assertThat(saveApple).isNotNull();
        assertThat(saveApple.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateBrand3(){
        Brand samsung = new Brand("Samsung");

        samsung.getCategories().add(new Category(29));// category memory
        samsung.getCategories().add(new Category(24));// category internal hard drive

        Brand saveSamsung = repo.save(samsung);
        assertThat(saveSamsung).isNotNull();
        assertThat(saveSamsung.getId()).isGreaterThan(0);
    }

    @Test
    public void testAllBrand(){
        List<Brand> listBrand = (List<Brand>) repo.findAll();
        listBrand.forEach(System.out::println);

        assertThat(listBrand).isNotEmpty();
    }

    @Test
    public void testGetById(){
        Optional<Brand> brand = repo.findById(1);
        System.out.println(brand);

        assertThat(brand).isNotEmpty();
    }

    @Test
    public void testUpdateName(){
        String newName = "Samsung Electronics";
        Brand samsung = repo.findById(3).get();
        samsung.setName(newName);

        Brand saveBrand = repo.save(samsung);
        assertThat(saveBrand.getName()).isEqualTo(newName);
    }

    @Test
    public void testDelete(){
        Integer id = 2;
        repo.deleteById(2);

        Optional<Brand> result = repo.findById(2);
        assertThat(result.isEmpty());
    }
}
