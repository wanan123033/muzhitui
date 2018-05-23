package com.nevermore.muzhitui.module.bean;

import java.io.Serializable;

/**
 * Created by Simone on 2017/2/8.
 */

public class Friends implements Serializable {
    private int id;
    private String user_name;

    private String headimg;

    public Friends(int id, String user_name, String headimg) {
        this.id = id;
        this.user_name = user_name;
        this.headimg = headimg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }
}
