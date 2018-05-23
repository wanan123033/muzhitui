package com.nevermore.muzhitui.module.bean;

import com.nevermore.muzhitui.module.BasePageBean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/9.
 */

public class QunFans extends BasePageBean{
    public List<QunFan> list;

    @Override
    public String toString() {
        return "QunFans{" +
                "state='" + state + '\'' +
                ", allPages=" + allPages +
                ", msg='" + msg + '\'' +
                ", pageCurrent=" + pageCurrent +
                ", list=" + list +
                '}';
    }

    public static class QunFan{
        public int id;
        public String wx_name;
        public String headimg;
        public String introduce;
        public int is_top;

        @Override
        public String toString() {
            return "QunFan{" +
                    "id=" + id +
                    ", wx_name='" + wx_name + '\'' +
                    ", headimg='" + headimg + '\'' +
                    ", introduce='" + introduce + '\'' +
                    ", is_top=" + is_top +
                    '}';
        }
    }
}
