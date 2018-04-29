package com.example.community.domain;

import java.io.Serializable;

/**
 * 用户个人登录注册信息类
 */
public class User implements Serializable{
    private String account;
    private String password;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
