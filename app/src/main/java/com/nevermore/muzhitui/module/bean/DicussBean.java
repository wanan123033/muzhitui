package com.nevermore.muzhitui.module.bean;

import com.nevermore.muzhitui.module.BasePageBean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/18.
 */

public class DicussBean extends BasePageBean {
    public List<Reply> replyList;
    public static class Reply{
        public int id;
        public String user_name;
        public String time;
        public int pagedt_id;
        public String headimg;
        public String reply_content;
        public int reply_loginid;
        public String page_picpath;
        public int is_reply_praise;
    }
}
