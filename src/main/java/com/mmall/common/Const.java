package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

public class Const {

    // 登陆用户存入 session 的键值
    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    // 产品排序
    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");
    }

    // 购物车选中状态
    public interface Cart {
        int CHECKED = 1;    // 选中
        int UNCHECKED = 0;  // 未选中

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";

    }
    // 角色
    public interface Role{
        int ROLE_CUSTOMER = 0;   // 普通用户
        int ROLE_ADMIN = 1;      // 管理员
    }

    public enum ProductStatusEnum {
        ON_SALE(1,"在线");

        private String value;
        private int code;

        ProductStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }
}
