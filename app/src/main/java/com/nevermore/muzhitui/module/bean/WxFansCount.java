package com.nevermore.muzhitui.module.bean;

/**
 * Created by Administrator on 2017/7/14.
 */

public class WxFansCount {
    private int state;
    private int num;
    private String msg;

    @Override
    public String toString() {
        return "WxFansCount{" +
                "state=" + state +
                ", num=" + num +
                ", msg='" + msg + '\'' +
                '}';
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
