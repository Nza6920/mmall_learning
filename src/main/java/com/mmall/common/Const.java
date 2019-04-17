package com.mmall.common;

public class Const {

    // 登陆用户存入 session 的键值
    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface Role{
        int ROLE_CUSTOMER = 0;   // 普通用户
        int ROLE_ADMIN = 1;      // 管理员
    }
}
