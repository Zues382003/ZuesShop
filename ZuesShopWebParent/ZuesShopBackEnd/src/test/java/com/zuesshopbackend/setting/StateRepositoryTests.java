package com.zuesshopbackend.setting;

import com.zuesshop.entity.Country;
import com.zuesshop.entity.State;
import com.zuesshopbackend.setting.Repository.StateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class StateRepositoryTests {

    @Autowired
    private StateRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateStateIndia(){
        Integer countryId = 2;
        Country country = entityManager.find(Country.class,countryId);
//        State state = repo.save(new State("Karnataka",country));
//        State state = repo.save(new State("Punjab",country));
//        State state = repo.save(new State("Uttar Pradesh",country));
        State state = repo.save(new State("West Bengal",country));

        assertThat(state).isNotNull();
        assertThat(state.getId()).isGreaterThan(0);
    }
    @Test
    public void testCreateStateInUS(){
        Integer countryId = 3;
        Country country = entityManager.find(Country.class,countryId);
//        State state = repo.save(new State("California",country));
//        State state = repo.save(new State("Texas",country));
//        State state = repo.save(new State("New York",country));
        State state = repo.save(new State("Washington",country));

        assertThat(state).isNotNull();
        assertThat(state.getId()).isGreaterThan(0);
    }

    @Test
    public void testListStateByCountry(){
        Integer countryId = 2;
        Country country = entityManager.find(Country.class, countryId);
        List<State> listStates = repo.findByCountryOrderByNameAsc(country);

        listStates.forEach(System.out::println);

        assertThat(listStates.size()).isGreaterThan(0);
    }
}
