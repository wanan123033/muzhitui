package com.nevermore.muzhitui.module.bean;

import java.io.Serializable;

/**
 * Created by Simone on 2016/12/28.
 */

public class Code implements Serializable {


    /**
     * state : 2
     * msg : 手机号码或密码错误
     */

    private String state;
    private String msg;
    private String is_public="1";

    public String getIs_public() {
        return is_public;
    }

    public void setIs_public(String is_public) {
        this.is_public = is_public;
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

    @Override
    public String toString() {
        return "Code{" +
                "state='" + state + '\'' +
                ", msg='" + msg + '\'' +
                ", is_public='" + is_public + '\'' +
                '}';
    }
}
