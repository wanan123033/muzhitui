package com.nevermore.muzhitui.event;

/**
 * Created by hehe on 2016/8/26.
 */
public class ObjectEvent {
    private Object oj;
    private int position;
    private int state;//1.添加模板 包括页眉，页脚，视频 2.只添加视频模板

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Object getOj() {
        return oj;
    }

    public void setOj(Object oj) {
        this.oj = oj;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ObjectEvent(int state, int position, Object oj) {
        this.state = state;
        this.position = position;
        this.oj = oj;
    }

    public ObjectEvent(int state, Object oj) {
        this.state = state;
        this.oj = oj;
    }
}
