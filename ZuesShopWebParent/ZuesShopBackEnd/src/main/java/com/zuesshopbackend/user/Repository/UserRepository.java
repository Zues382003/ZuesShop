package com.zuesshopbackend.user.Repository;

import com.zuesshop.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User,Integer>, CrudRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    public User getUserByEmail(@Param("email") String email);

    public Long countById(Integer id);// declare do spring jpa

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.id = ?1")
    public Page<User> listSearchUserByRoles(Integer roleId, Pageable pageable);

    @Query("select u from User u where concat(u.id, ' ', u.email, ' ', u.firstName, ' ', u.lastName) like %?1% ")
    public Page<User> findAll(String keyword, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE (r.id = ?2) and"
            + " concat(u.id, ' ', u.email, ' ', u.firstName, ' ', u.lastName) like %?1% ")
    public Page<User> searchUserInRoles(String keyword, Integer roleId, Pageable pageable);

    @Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
    @Modifying
    public void updateEnabledStatus(Integer id, boolean enabled);
}
