package com.zuesshopbackend.user.Service;

import com.zuesshop.entity.Role;
import com.zuesshop.entity.User;
import com.zuesshopbackend.user.UserNotFoundException;
import com.zuesshopbackend.user.Repository.RoleRepository;
import com.zuesshopbackend.user.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {

    public static final int USER_PER_PAGE = 3;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getListUser(){
        return (List<User>) userRepository.findAll();
    }

    public User getByEmail(String email){
        return userRepository.getUserByEmail(email);
    }

    public Page<User> getListByPage(int pageNumber, String sortField, String sortDirection, String keyword, Integer roleId) throws UserNotFoundException {
        Sort sort = Sort.by(sortField);

        sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNumber - 1,USER_PER_PAGE, sort);

        if(keyword != null && !keyword.isEmpty()){
            if(roleId != null && roleId > 0){
                return userRepository.searchUserInRoles(keyword, roleId, pageable);
            }
            return userRepository.findAll(keyword,pageable);
        }

        if(roleId != null && roleId >0){
            return userRepository.listSearchUserByRoles(roleId, pageable);
        }

        return userRepository.findAll( pageable);
    }

    public List<Role> getListRole(){
        return (List<Role>) repository.findAll();
    }

    public void encodePassword(User user){
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
    }

    public User save(User user){
        boolean isUpdatingUser = (user.getId() != null);

        if(isUpdatingUser){
            User existingUser = userRepository.findById(user.getId()).get();

            if(user.getPassword().isEmpty()){
                user.setPassword(existingUser.getPassword());
            }else{
                encodePassword(user);
            }
        }else {
            encodePassword(user);
        }

        return userRepository.save(user);
    }

    public User updateAccount(User userInForm){
        User userInUserRepository = userRepository.findById(userInForm.getId()).get();

        if(!userInForm.getPassword().isEmpty()){
            userInUserRepository.setPassword(userInForm.getPassword());
            encodePassword(userInUserRepository);
        }

        if(userInForm.getPhotos() != null){
            userInUserRepository.setPhotos(userInForm.getPhotos());
        }

        userInUserRepository.setFirstName(userInForm.getFirstName());
        userInUserRepository.setLastName(userInForm.getLastName());

        return userRepository.save(userInUserRepository);
    }

    public boolean isEmailUnique(Integer id,String email){
        User userByEmail = userRepository.getUserByEmail(email);

        if(userByEmail == null){
            return true;
        }else{
            boolean isCreatingnew = (id ==null);
            if(isCreatingnew){
                return false;
            }
            else if(userByEmail.getId() != id)
            {
                return false;
            }
        }
        return true;
    }

    public User getUserById(Integer id) throws UserNotFoundException {
        try {
            return userRepository.findById(id).get();
        }catch (NoSuchElementException ex ){
            throw new UserNotFoundException("Could not find any user with ID: "+ id );
        }
    }

    public void delete(Integer id) throws UserNotFoundException {
        Long countById = userRepository.countById(id);

        if(countById == null || countById ==0){
            throw new UserNotFoundException("Could not find any user with ID: "+ id );
        }

        userRepository.deleteById(id);
    }

    public void updateUserEnabledStatus(Integer id, boolean enabled){
        userRepository.updateEnabledStatus(id,enabled);
    }
}
