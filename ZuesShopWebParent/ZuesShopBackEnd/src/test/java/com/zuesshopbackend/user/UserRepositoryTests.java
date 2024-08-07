package com.zuesshopbackend.user;

import com.zuesshop.entity.Role;
import com.zuesshop.entity.User;
import com.zuesshopbackend.user.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {
    @Autowired
    private UserRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateNewUserWithOneRole() {
        Role roleAdmin = entityManager.find(Role.class, 1);
        User userChanhNguyen = new User("21521883@gm.uit.edu.vn", "chanh382003", "Nguyen", "Minh Chanh");
        userChanhNguyen.addRole(roleAdmin);

        User saveUser = repository.save(userChanhNguyen);
        assertThat(saveUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateNewUserWithTwoRole() {
        User UserZues = new User("nguyenminhchanh3842@gmail.com", "chanh38", "William", "Zues");
        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);

        UserZues.addRole(roleEditor);
        UserZues.addRole(roleAssistant);

        User saveUser = repository.save(UserZues);

        assertThat(saveUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUser() {
        Iterable<User> listUsers = repository.findAll();
        listUsers.forEach(user -> {
            System.out.println(user);
        });
    }

    @Test
    public void testGetUserById() {
        User userChanh = repository.findById(1).get();
        System.out.println(userChanh);
        assertThat(userChanh).isNotNull();
    }

    @Test
    public void testUpdateUserDetails() {
        User userChanh = repository.findById(1).get();
        userChanh.setEnabled(true);
        userChanh.setPassword("@Chanh38");

        repository.save(userChanh);
    }

    @Test
    public void testUpdateUserRoles() {
        User userChanh = repository.findById(2).get();
        Role roleEditor = new Role(3);
        Role roleSalesperson = new Role(2);

        userChanh.getRoles().remove(roleEditor);
        userChanh.addRole(roleSalesperson);

        repository.save(userChanh);
    }

    @Test
    public void testDeleteUser() {
        Integer userId = 2;
        repository.deleteById(userId);
    }

    @Test
    public void testGetUserByEmail() {
        String email = "21521883@gm.uit.edu.vn";
        User user = repository.getUserByEmail(email);

        assertThat(user).isNotNull();
    }

}
