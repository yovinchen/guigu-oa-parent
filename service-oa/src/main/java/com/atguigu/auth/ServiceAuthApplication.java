package com.atguigu.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * ClassName: ServiceAuthApplication
 * Package: com.atguigu.auth
 *
 * @author yovinchen
 * @Create 2023/6/1 23:03
 */
@SpringBootApplication
@ComponentScan("com.atguigu")
public class ServiceAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceAuthApplication.class, args);
    }
}
