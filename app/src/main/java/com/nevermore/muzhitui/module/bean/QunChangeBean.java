package com.nevermore.muzhitui.module.bean;

import com.nevermore.muzhitui.module.BasePageBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/9/18.
 */

public class QunChangeBean extends BasePageBean{
    public List<QunChange> qunList;
    public static class QunChange implements Serializable{
        public String pic1;
        public int id;
        public String headimg;
        public int expiration_time;
        public String wx_qun_name;
        public String wx_no;
    }
}
