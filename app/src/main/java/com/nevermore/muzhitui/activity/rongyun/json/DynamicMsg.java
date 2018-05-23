package com.nevermore.muzhitui.activity.rongyun.json;

/**
 * Created by Administrator on 2017/11/23.
 */

public class DynamicMsg {
    public String name;
    public String msg;
    public int pageId;
    public String type;

    @Override
    public String toString() {
        return "DynamicMsg{" +
                "name='" + name + '\'' +
                ", msg='" + msg + '\'' +
                ", pageId=" + pageId +
                ", type='" + type + '\'' +
                '}';
    }
}
