package com.nevermore.muzhitui.event;

/**
 * Created by Simone on 2017/3/20.
 */

public class refrush {
    private int state;//state 为1时 ，刷新人脉信息
    //state 为2时 ，刷新新的好友信息

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public refrush(int state) {
        this.state = state;
    }
}
