package com.nevermore.muzhitui.module.bean;

import com.nevermore.muzhitui.module.BasePageBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/9/18.
 */

public class QunWantBean extends BasePageBean{
    public List<QunWant> qunList;
    public static class QunWant implements Serializable{
        public int id;
        public String user_name;
        public String update_time;
        public String headimg;
        public int read_num;
        public String wx_city;
        public String wx_province;
        public int offer;
        public int loginid;
        public String wx_qun_name;
    }
}
