package com.nevermore.muzhitui.module.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/5.
 */

public class GetToken implements Serializable {

    /**
     * token : DhfbTPYWsruojW6qDNdZ0foQkAkYhI/Zk/fqwmlhrTvMdOuSwhV0U6gYFj36ukhLH7MuFk4vMgolgy3+SrlQwYOIvxBEIicc
     * userId : 1000018
     * state : 1
     * msg : 获取token成功--(数据库)
     */

    private String token;
    private int userId;
    private String state;
    private String msg;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
