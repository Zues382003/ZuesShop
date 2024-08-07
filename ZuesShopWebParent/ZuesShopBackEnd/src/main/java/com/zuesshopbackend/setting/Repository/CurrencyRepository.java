package com.zuesshopbackend.setting.Repository;

import com.zuesshop.entity.Currency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRepository extends CrudRepository<Currency, Integer> {
    public List<Currency> findAllByOrderByNameAsc();
}
