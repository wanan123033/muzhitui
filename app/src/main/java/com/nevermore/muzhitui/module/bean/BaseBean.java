package com.nevermore.muzhitui.module.bean;

/**
 * Created by hehe on 2016/6/2.
 */
public class BaseBean {
    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "state=" + state +
                '}';
    }
}
