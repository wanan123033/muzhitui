package com.nevermore.muzhitui.module.bean;

/**
 * Created by Administrator on 2017/8/7.
 */

public class ExceInfo {
    private int state;
    private String msg;

    public int getState() {
        return state;
    }

    public void setState(int state) {
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
        return "ExceInfo{" +
                "state=" + state +
                ", msg='" + msg + '\'' +
                '}';
    }
}
