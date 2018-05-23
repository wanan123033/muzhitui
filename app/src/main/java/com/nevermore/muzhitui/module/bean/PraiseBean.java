package com.nevermore.muzhitui.module.bean;

import com.nevermore.muzhitui.module.BasePageBean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/18.
 */

public class PraiseBean extends BasePageBean{

    public List<Praise> praiseList;

    public static class Praise{
        public int id;
        public String user_name;
        public String headimg;
        public int loginid;

    }
}
