package com.nevermore.muzhitui.module.bean;

import com.nevermore.muzhitui.module.BasePageBean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/18.
 */

public class WatchBean extends BasePageBean{

    public List<Watch> watchList;
    public static class Watch extends GFBean{
        public Watch(){
            gf_type = 0;
        }
        public int id;
        public int count;
        public String user_name;
        public String headimg;
        public int loginid;

        @Override
        public String toString() {
            return "Watch{" +
                    "id=" + id +
                    ", count=" + count +
                    ", user_name='" + user_name + '\'' +
                    ", headimg='" + headimg + '\'' +
                    ", loginid=" + loginid +
                    '}';
        }
    }
}
