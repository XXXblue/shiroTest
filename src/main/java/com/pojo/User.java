package com.pojo;

import java.io.Serializable;

/**
 * @Author: XBlue
 * @Date: Create in 2018/5/615:30
 * @Description:
 * @Modified By:
 */
public class User implements Serializable{
    private String userName;
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
