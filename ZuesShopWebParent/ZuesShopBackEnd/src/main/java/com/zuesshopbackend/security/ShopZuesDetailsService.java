package com.zuesshopbackend.security;

import com.zuesshop.entity.User;
import com.zuesshopbackend.user.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
public class ShopZuesDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(email);
        if(user != null){
            return new ShopZuesUserDetails(user);
        }
        throw new UsernameNotFoundException("Could not find user with email" + email);
    }
}
