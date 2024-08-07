package com.zuesshopbackend.setting.Repository;

import com.zuesshop.entity.Country;
import com.zuesshop.entity.State;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends CrudRepository<State, Integer> {
    public List<State> findByCountryOrderByNameAsc(Country country);
}
