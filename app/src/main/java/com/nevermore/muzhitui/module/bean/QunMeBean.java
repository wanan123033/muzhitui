package com.nevermore.muzhitui.module.bean;

import com.nevermore.muzhitui.module.BasePageBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/9/18.
 */

public class QunMeBean extends BasePageBean{
    public List<Qun> qunList;
    public static class Qun implements Serializable{
        public int id;
        public String headimg;
        public int read_num;
        public int wx_qun_num;
        public String wx_city;
        public String wx_province;
        public int offer;
        public String wx_qun_name;
    }
}
