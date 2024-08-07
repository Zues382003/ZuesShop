package com.zuesshopfrontend.customer;

import com.zuesshop.entity.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    @Query("Select c from Customer c where c.email = ?1")
    public Customer findByEmail(String email);

    @Query("select c from Customer c where c.verificationCode = ?1")
    public Customer findByVerificationCode(String code);

    @Query("update Customer c set c.enabled = true, c.verificationCode = null where c.id = ?1")
    @Modifying
    public void enable(Integer id);
}

