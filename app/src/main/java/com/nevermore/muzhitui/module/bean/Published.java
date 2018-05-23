package com.nevermore.muzhitui.module.bean;

import java.io.Serializable;

/**
 * Created by Simone on 2016/12/28.
 */

public class Published implements Serializable {


    /**
     * state : 2
     * msg : 手机号码或密码错误
     */

    private String state;
    private String msg;
    private int pageme_id;
    private String title_pic;

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

    public int getPageme_id() {
        return pageme_id;
    }

    public String getTitle_pic() {
        return title_pic;
    }

    public void setTitle_pic(String title_pic) {
        this.title_pic = title_pic;
    }

    public void setPageme_id(int pageme_id) {
        this.pageme_id = pageme_id;
    }
}
