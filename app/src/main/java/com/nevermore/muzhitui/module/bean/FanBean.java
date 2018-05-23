package com.nevermore.muzhitui.module.bean;

import com.nevermore.muzhitui.module.BasePageBean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/18.
 */

public class FanBean extends BasePageBean{
    public List<Fan> fansList;

    public static class Fan extends GFBean{
        public Fan(){
            gf_type = 1;
        }

        public int id;
        public String user_name;
        public int loginid;
        public String headimg;
        public int type;
    }
}
