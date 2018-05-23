package com.nevermore.muzhitui.module.bean;

import java.io.Serializable;

/**
 * Created by Simone on 2017/1/11.
 */

public class NewFriendNums implements Serializable {
    /**
     * state : 0
     * msg : 用户为空
     * num : 3
     */

    private String state;
    private String msg;
    private int num;

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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
