package com.nevermore.muzhitui.module.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/28.
 */

public class Register implements Serializable {


    /**
     * state : 2
     * msg : 手机号码或密码错误
     *  "password": "e10adc3949ba59abbe56e057f20f883e",
     "phone": "15607464884",
     */

    private String state;
    private String msg;
    private String password;
    private String phone;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
