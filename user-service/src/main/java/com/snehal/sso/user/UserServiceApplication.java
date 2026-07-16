package com.snehal.sso.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UserServiceApplication {
    public static void main(String[] a) {
        SpringApplication.run(UserServiceApplication.class, a);
    }
}