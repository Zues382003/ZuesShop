package com.zuesshopbackend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig   {

    @Bean
    public UserDetailsService userDetailsService(){
        return new ShopZuesDetailsService();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests()
                .requestMatchers("/users/**", "/settings/**", "/countries/**", "/states/**").hasAuthority("Admin")
                .requestMatchers("/categories/**").hasAnyAuthority("Admin","Editor")
                .requestMatchers("/brands/**").hasAnyAuthority("Admin","Editor")
                .requestMatchers("/products/edit/**", "/products/save", "/products/check_unique")
                    .hasAnyAuthority("Admin", "Editor", "Salesperson")
                .requestMatchers("/products/new", "/products/delete/**")
                    .hasAnyAuthority("Admin", "Editor")
                .requestMatchers("/products", "/products/page/**", "/products/detail/**")
                    .hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
                .requestMatchers("/products/**").hasAnyAuthority("Admin","Editor")
                .requestMatchers("/customers/**").hasAnyAuthority("Admin","Salesperson")
                .requestMatchers("/shipping/**").hasAnyAuthority("Admin","Salesperson")
                .requestMatchers("/orders/**").hasAnyAuthority("Admin","Salesperson","Shipper")
                .requestMatchers("/reports/**").hasAnyAuthority("Admin","Salesperson")
                .requestMatchers("/articles/**").hasAnyAuthority("Admin","Editor")
                .requestMatchers("/menu/**").hasAnyAuthority("Admin","Editor")
                .requestMatchers("/questions/**").hasAnyAuthority("Admin","Assistant")
                .requestMatchers("/reviews/**").hasAnyAuthority("Admin","Assistant")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .usernameParameter("email")
                    .permitAll()
                .and()
                .logout().permitAll()
                .and()
                        .rememberMe().key("zues382003");
        http.authenticationProvider(authenticationProvider());
        return http.build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() throws Exception {
        return (web -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**", "/css/**","/fonts/**","/fontawesome/**", "/richtext/**","/webfonts/**"));
    }


}
