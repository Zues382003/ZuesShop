package com.zuesshopfrontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan("com.zuesshop.entity")
public class ZuesShopFrontEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuesShopFrontEndApplication.class, args);
    }

}
