package com.zuesshopbackend.user.Repository;

import com.zuesshop.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends CrudRepository<Role,Integer> {
}
