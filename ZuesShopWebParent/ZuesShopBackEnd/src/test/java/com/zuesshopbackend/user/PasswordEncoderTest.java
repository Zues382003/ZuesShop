package com.zuesshopbackend.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;


public class PasswordEncoderTest {
    @Test
    public void testEncodePassword(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "chanh382003";
        String encodePassword = passwordEncoder.encode(rawPassword);

        System.out.println(encodePassword);

        boolean matches = passwordEncoder.matches(rawPassword,encodePassword);//kiem tra mat sau thô có giống sau khi mã hóa không
        assertThat(matches).isTrue();
    }
}
