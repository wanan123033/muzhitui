package com.nevermore.muzhitui.event;

/**
 * Created by hehe on 2016/8/26.
 */
public class PublishedModelEvent {
    private int state=1;//1表示添加模板 2表示添加广告模板
    private int position;


    public PublishedModelEvent(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public PublishedModelEvent(int state, int position) {
        this.state = state;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
