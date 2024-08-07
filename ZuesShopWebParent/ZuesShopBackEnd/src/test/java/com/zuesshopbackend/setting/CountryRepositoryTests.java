package com.zuesshopbackend.setting;

import com.zuesshop.entity.Country;
import com.zuesshopbackend.setting.Repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CountryRepositoryTests {
    @Autowired
    private CountryRepository repo;

    @Test
    public void testCreateCountry(){
        Country country =  repo.save(new Country("VietNam", "VN"));
        assertThat(country).isNotNull();
        assertThat(country.getId()).isGreaterThan(0);
    }

    @Test
    public void testListCountry(){
        List<Country> listCountries = repo.findAllByOrderByNameAsc();
        listCountries.forEach(System.out::println);

        assertThat(listCountries.size()).isGreaterThan(0);

    }
}
