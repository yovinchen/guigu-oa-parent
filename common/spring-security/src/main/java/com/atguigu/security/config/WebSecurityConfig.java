package com.atguigu.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * ClassName: WebSecurityConfig
 * Package: com.atguigu.security.config
 *
 * @author yovinchen
 * @Create 2023/6/10 22:47
 */
@Configuration
//@EnableWebSecurity是开启SpringSecurity的默认行为
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

}
