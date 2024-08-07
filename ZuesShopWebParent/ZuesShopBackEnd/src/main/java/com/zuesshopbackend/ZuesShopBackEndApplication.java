package com.zuesshopbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.zuesshop.entity","com.zuesshopbackend.security"})
public class ZuesShopBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuesShopBackEndApplication.class, args);
    }

}
