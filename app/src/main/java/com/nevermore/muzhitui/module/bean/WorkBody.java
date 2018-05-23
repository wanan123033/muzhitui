package com.nevermore.muzhitui.module.bean;

/**
 * Created by hehe on 2016/5/26.
 */
public class WorkBody {
    private int state;
    private int pageCurrent;

    public WorkBody(int state, int pageCurrent) {
        this.state = state;
        this.pageCurrent = pageCurrent;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPageCurrent() {
        return pageCurrent;
    }

    public void setPageCurrent(int pageCurrent) {
        this.pageCurrent = pageCurrent;
    }
}
