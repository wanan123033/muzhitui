package com.nevermore.muzhitui.module.bean;

/**
 * Created by Administrator on 2018/1/12.
 */

public class PublishQunFans extends com.nevermore.muzhitui.module.BaseBean{
    public int is_publish;
    public int is_able_top;
    public int id;

    @Override
    public String toString() {
        return "PublishQunFans{" +
                "state='" + state + '\'' +
                ", msg='" + msg + '\'' +
                ", is_publish=" + is_publish +
                ", is_able_top=" + is_able_top +
                ", id=" + id +
                '}';
    }
}
