package com.nevermore.muzhitui.module.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/15.
 */

public class FriendBean extends com.nevermore.muzhitui.module.BasePageBean{
    public List<Friend> list;
    public static class Friend {
        public int id;
        public String phone;
        public String wx_name;
        public String headimg;
        public int loginid;

        @Override
        public String toString() {
            return "Friend{" +
                    "id=" + id +
                    ", phone='" + phone + '\'' +
                    ", wx_name='" + wx_name + '\'' +
                    ", headimg='" + headimg + '\'' +
                    ", loginid=" + loginid +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "FriendBean{" +
                "state='" + state + '\'' +
                ", allPages=" + allPages +
                ", msg='" + msg + '\'' +
                ", pageCurrent=" + pageCurrent +
                ", list=" + list +
                '}';
    }
}
