package com.nevermore.muzhitui.module.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Simone on 2017/1/9.
 */

public class MyFriends implements Serializable {
    /**
     * loginList : [{"id":100004,"user_name":"大海","mp_name":"大海","headimg":"http://www.muzhitui.cn/song/upload/2016-05-24/146407190508061.jpg","wechatimg":null,"wechatname":null,"agent":"年费会员"},{"id":100001,"user_name":"James-Soong","mp_name":"宋广才","headimg":"http://www.muzhitui.cn/song/upload/2016-08-25/147212248525715.jpg","wechatimg":null,"wechatname":null,"agent":"年费会员"},{"id":100002,"user_name":"Peter","mp_name":"张洪桥","headimg":"http://www.muzhitui.cn/song/upload/2016-05-24/146409275742779.jpg","wechatimg":null,"wechatname":null,"agent":"年费会员"},{"id":100005,"user_name":"倾世灬苏尘","mp_name":null,"headimg":"http://wx.qlogo.cn/mmopen/PiajxSqBRaELLysuzDTCVWSTzia8zh0VoyJibSvYhhsDcnFxac4OQ6MznvqsNdeaoN9ZfFFudJtqZicLqWnAnnpUR34EGxemibHbkpoDobTgZInM/0","wechatimg":null,"wechatname":null,"agent":"未加入会员"},{"id":100003,"user_name":"小眉毛","mp_name":null,"headimg":"http://wx.qlogo.cn/mmopen/FoD5H2I9q2FQibRzTSOaziaTMAKKugcU0ialSXIrvkp4S8j4VaDRnzujW0mAlHhRbl9a53OiclDcnF4ibfbYb1htJV3KfIbwESYqa/0","wechatimg":null,"wechatname":null,"agent":"未加入会员"}]
     * allPages : 1
     * pageCurrent : 1
     * state : 1
     */

    private int allPages;
    private int pageCurrent;
    private String state;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private List<LoginListBean> loginList;

    public int getAllPages() {
        return allPages;
    }

    public void setAllPages(int allPages) {
        this.allPages = allPages;
    }

    public int getPageCurrent() {
        return pageCurrent;
    }

    public void setPageCurrent(int pageCurrent) {
        this.pageCurrent = pageCurrent;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<LoginListBean> getLoginList() {
        return loginList;
    }

    public void setLoginList(List<LoginListBean> loginList) {
        this.loginList = loginList;
    }

    public static class LoginListBean {
        /**
         * id : 100004
         * user_name : 大海
         * mp_name : 大海
         * headimg : http://www.muzhitui.cn/song/upload/2016-05-24/146407190508061.jpg
         * wechatimg : null
         * wechatname : null
         * agent : 年费会员
         */

        private int id;
        private String user_name;
        private String mp_name;
        private String headimg;
        private Object wechatimg;
        private Object wechatname;
        private String agent;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getMp_name() {
            return mp_name;
        }

        public void setMp_name(String mp_name) {
            this.mp_name = mp_name;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public Object getWechatimg() {
            return wechatimg;
        }

        public void setWechatimg(Object wechatimg) {
            this.wechatimg = wechatimg;
        }

        public Object getWechatname() {
            return wechatname;
        }

        public void setWechatname(Object wechatname) {
            this.wechatname = wechatname;
        }

        public String getAgent() {
            return agent;
        }

        public void setAgent(String agent) {
            this.agent = agent;
        }

        public LoginListBean(int id, String user_name, String headimg, String agent) {
            this.id = id;
            this.user_name = user_name;
            this.headimg = headimg;
            this.agent = agent;
        }
    }
}
