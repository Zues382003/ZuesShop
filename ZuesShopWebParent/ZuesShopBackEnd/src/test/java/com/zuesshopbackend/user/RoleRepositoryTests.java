package com.zuesshopbackend.user;

import com.zuesshop.entity.Role;
import com.zuesshopbackend.user.Repository.RoleRepository;
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
public class RoleRepositoryTests {
    @Autowired
    private RoleRepository repository;

    @Test
    public void testCreateFirstRole(){
        Role roleAdmin = new Role("Admin", "manage everything");
        Role saveRole = repository.save(roleAdmin);

        assertThat(saveRole.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateRestRoles(){
        Role roleSaleperson = new Role("Salesperson","manage product price, customers, shipping, orders and sales report");
        Role roleEdit = new Role("Editor", "manage categories, brands, products, articles and menus");
        Role roleShipper = new Role("Shipper", "view products, view orders");
        Role roleAssistant = new Role("Assistant", "manage questions and reviews");

        repository.saveAll(List.of(roleSaleperson,roleEdit,roleShipper,roleAssistant));

    }
}
