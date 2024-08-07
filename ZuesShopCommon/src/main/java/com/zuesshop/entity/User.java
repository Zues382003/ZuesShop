package com.zuesshop.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, nullable = false, unique = true)
    private String email;

    @Column(length = 64, nullable = false)
    private String password;

    @Column(name = "first_name",length = 45, nullable = false)
    private String firstName;

    @Column(name="last_name", length = 45, nullable = false)
    private  String lastName;

    @Column(length = 128)
    private String photos;

    private boolean enabled;

    public User(){
    }

    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    //Khi sử dụng FetchType.EAGER, dữ liệu sẽ được truy xuất ngay lập tức khi thực hiện truy vấn.
    // Điều này có nghĩa là các đối tượng liên quan sẽ được tải từ cơ sở dữ liệu cùng với đối tượng gốc ngay khi truy vấn được thực thi.
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),//mặc định là id của class
            inverseJoinColumns = @JoinColumn(name = "role_id")// mặc định là id xét theo lớp entity hàng dưới
    )
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role){
        this.roles.add(role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", roles=" + roles +
                '}';
    }

    @Transient
    public String getPhotosImagePath(){
        if(id == null || photos == null) return "/images/default_user.jpg";

        return "/user-photos/" + this.id + "/" + this.photos;
    }

    @Transient
    public String getFullName(){
        return firstName + " " + lastName;
    }

    public boolean hasRole(String roleName){
        Iterator<Role> iterator = roles.iterator();

        while(iterator.hasNext()){
            Role role = iterator.next();
            if(role.getName().equals(roleName)){
                return true;
            }
        }

        return false;
    }
}
