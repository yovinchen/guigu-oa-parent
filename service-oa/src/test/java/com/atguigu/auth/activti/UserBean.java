package com.atguigu.auth.activti;

import org.springframework.stereotype.Component;

/**
 * ClassName: UserBean
 * Package: com.atguigu.auth.activti
 *
 * @author yovinchen
 * @Create 2023/6/12 14:52
 */

@Component
public class UserBean {

    public String getUsername(int id) {
        if (id == 1) {
            return "Mac";
        }
        if (id == 2) {
            return "Lila";
        }
        return "admin";
    }
}
