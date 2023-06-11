package com.atguigu.security.custom;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * ClassName: UserDetailsService
 * Package: com.atguigu.security.custom
 *
 * @author yovinchen
 * @Create 2023/6/10 23:28
 */
public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService {

    /**
     * 根据用户名获取用户对象（获取不到直接抛异常）
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
