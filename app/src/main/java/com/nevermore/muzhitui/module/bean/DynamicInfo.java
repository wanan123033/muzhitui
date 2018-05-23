package com.nevermore.muzhitui.module.bean;

import com.nevermore.muzhitui.module.BasePageBean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/18.
 */

public class DynamicInfo extends BasePageBean{
    public Info oneDt;

    public static class Info{
        public List<DynamicBean.Pic> pics;
        public int id;
        public String content;
        public String user_name;
        public String time;
        public String reply_count;
        public int fans_type;
        public String headimg;
        public List<Reply> reply_infos;
        public int praise_type;
        public int loginid;
        public List<Pic> head_pics;
        public int praise_count;
    }
    public static class Reply{
        public String reply_headimg;
        public String reply_user_name_p;
        public String reply_content;
        public int is_del_reply;
        public String reply_time;
        public int reply_loginid;
        public int reply_type;
        public String reply_user_name;
        public int reply_id;
        public int reply_user_id_p;
    }
    public static class Pic{
        public String headimg;
        public int loginid;
    }
}
