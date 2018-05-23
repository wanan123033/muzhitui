package com.nevermore.muzhitui.event;

/**
 * Created by hehe on 2016/5/21.
 */
public class AuthorizeEvent {
    private String state;//state==login 纯微信登录 register 注册页面绑定微信的微信登录标志 registerAndPhone 注册页面成功后返回到登录页面

    public AuthorizeEvent(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
