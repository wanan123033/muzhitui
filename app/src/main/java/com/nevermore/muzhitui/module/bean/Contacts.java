package com.nevermore.muzhitui.module.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Simone on 2016/12/19.
 */

public class Contacts implements Serializable {
    /**
     * loginList : [{"id":100000000,"user_name":"拇指推官方帐号","wx_sex":1,"status":null,"mp_name":"宋广才","headimg":"http://wx.qlogo.cn/mmopen/et9q9FvjBBrtgMOQcQnYouwYqhavRSMibNVHD9nHv7aShztrCbz8RDRl26jkRmsDIy3aLh1ugmrD2TLibZT9LIvw/0","wx_city":"深圳","wx_province":"广东","wechatimg":null,"wx_country":"中国","wechatname":null,"agent":"年费会员"},{"id":153873,"user_name":"153873的用户名","wx_sex":null,"status":1,"mp_name":"","headimg":"http://192.168.1.49:8099/song/upload/2017-01-19/148480754779315.png","wx_city":"","wx_province":"","wechatimg":null,"wx_country":"","wechatname":null,"agent":"未加入会员"},{"id":153873,"user_name":"153873的用户名","wx_sex":null,"status":1,"mp_name":"","headimg":"http://192.168.1.49:8099/song/upload/2017-01-19/148480754779315.png","wx_city":"","wx_province":"","wechatimg":null,"wx_country":"","wechatname":null,"agent":"未加入会员"},{"id":153873,"user_name":"153873的用户名","wx_sex":null,"status":1,"mp_name":"","headimg":"http://192.168.1.49:8099/song/upload/2017-01-19/148480754779315.png","wx_city":"","wx_province":"","wechatimg":null,"wx_country":"","wechatname":null,"agent":"未加入会员"},{"id":153872,"user_name":"小莫 3","wx_sex":0,"status":1,"mp_name":null,"headimg":"http://wx.qlogo.cn/mmopen/9Yae9bSziawTIbGW2yd5aaIGcMiaNjuhaPVU7MmEZ571rrxINmFyHuU5VziaU2auWl6fPKicXqDQiaic9HL7uTqRRuMTfJJcib4Cqq6/0","wx_city":"","wx_province":"","wechatimg":null,"wx_country":"","wechatname":null,"agent":"未加入会员"},{"id":153872,"user_name":"小莫 3","wx_sex":0,"status":1,"mp_name":null,"headimg":"http://wx.qlogo.cn/mmopen/9Yae9bSziawTIbGW2yd5aaIGcMiaNjuhaPVU7MmEZ571rrxINmFyHuU5VziaU2auWl6fPKicXqDQiaic9HL7uTqRRuMTfJJcib4Cqq6/0","wx_city":"","wx_province":"","wechatimg":null,"wx_country":"","wechatname":null,"agent":"未加入会员"},{"id":153872,"user_name":"小莫 3","wx_sex":0,"status":3,"mp_name":null,"headimg":"http://wx.qlogo.cn/mmopen/9Yae9bSziawTIbGW2yd5aaIGcMiaNjuhaPVU7MmEZ571rrxINmFyHuU5VziaU2auWl6fPKicXqDQiaic9HL7uTqRRuMTfJJcib4Cqq6/0","wx_city":"","wx_province":"","wechatimg":null,"wx_country":"","wechatname":null,"agent":"未加入会员"},{"id":153869,"user_name":"merlin （man） 3","wx_sex":1,"status":null,"mp_name":null,"headimg":"http://wx.qlogo.cn/mmopen/et9q9FvjBBrtgMOQcQnYouwYqhavRSMibNVHD9nHv7aShztrCbz8RDRl26jkRmsDIy3aLh1ugmrD2TLibZT9LIvw/0","wx_city":"深圳","wx_province":"广东","wechatimg":null,"wx_country":"中国","wechatname":null,"agent":"未加入会员"},{"id":153868,"user_name":"Simone 3","wx_sex":0,"status":3,"mp_name":null,"headimg":"http://wx.qlogo.cn/mmopen/qFaGu5D2HvY27tvVwSuDhhqvBxWicpaq2YF7J5E2Fwkt2mUnX7JpQ0xYIPJXMw2xNGeTab0mfcywWGWXGSLZeNia6ZEjyfzFEF/0","wx_city":"","wx_province":"","wechatimg":null,"wx_country":"","wechatname":null,"agent":"未加入会员"},{"id":153868,"user_name":"Simone 3","wx_sex":0,"status":3,"mp_name":null,"headimg":"http://wx.qlogo.cn/mmopen/qFaGu5D2HvY27tvVwSuDhhqvBxWicpaq2YF7J5E2Fwkt2mUnX7JpQ0xYIPJXMw2xNGeTab0mfcywWGWXGSLZeNia6ZEjyfzFEF/0","wx_city":"","wx_province":"","wechatimg":null,"wx_country":"","wechatname":null,"agent":"未加入会员"},{"id":153864,"user_name":"Simone 2","wx_sex":0,"status":null,"mp_name":null,"headimg":"http://wx.qlogo.cn/mmopen/oBU1CRkQW1ibxZ3doEATbziaSFmwib4FOBM4Kw44nHbDP53RtwZ8YrCMt8NyNJEM7ia10icLRC0EIriaZbTRU7JC8x6GA3MuibpVicJia/0","wx_city":"","wx_province":"","wechatimg":null,"wx_country":"","wechatname":null,"agent":"未加入会员"},{"id":153863,"user_name":"小莫 2","wx_sex":0,"status":1,"mp_name":null,"headimg":"http://wx.qlogo.cn/mmopen/NusbLlEXDlzbM4KVaNpJFOatwEuTBCNEFPMX1iaW2gLplibhUhF0kCl2Bp4thOlFuCia3yyj4xsglshjiaNznPdkbF6ZrmHMzFeZ/0","wx_city":"","wx_province":"","wechatimg":null,"wx_country":"","wechatname":null,"agent":"未加入会员"},{"id":153862,"user_name":"merlin （man） 2","wx_sex":1,"status":null,"mp_name":null,"headimg":"http://wx.qlogo.cn/mmopen/icFTnRoibgibpib831ls9fcUqvyc8dEQIwhkxxd9CU5PK9SI1hqJQRgXkArCWN6ToWbibwEVHBy5Uiaicobl2HkqHLCBQ/0","wx_city":"深圳","wx_province":"广东","wechatimg":null,"wx_country":"中国","wechatname":null,"agent":"未加入会员"},{"id":153850,"user_name":"聰","wx_sex":1,"status":1,"mp_name":null,"headimg":"http://wx.qlogo.cn/mmopen/tfTVtAY7xEqAkNB2Oo0mM3XBLzOvEA29IRurYGNicpsQKqYKht77VcLGRRPqcwSVNLG8Y8VicxDNUXibNgGyFzt0KQmkicVWhibNh/0","wx_city":"","wx_province":"","wechatimg":null,"wx_country":"CN","wechatname":null,"agent":"未加入会员"},{"id":153850,"user_name":"聰","wx_sex":1,"status":3,"mp_name":null,"headimg":"http://wx.qlogo.cn/mmopen/tfTVtAY7xEqAkNB2Oo0mM3XBLzOvEA29IRurYGNicpsQKqYKht77VcLGRRPqcwSVNLG8Y8VicxDNUXibNgGyFzt0KQmkicVWhibNh/0","wx_city":"","wx_province":"","wechatimg":null,"wx_country":"CN","wechatname":null,"agent":"未加入会员"},{"id":153850,"user_name":"聰","wx_sex":1,"status":1,"mp_name":null,"headimg":"http://wx.qlogo.cn/mmopen/tfTVtAY7xEqAkNB2Oo0mM3XBLzOvEA29IRurYGNicpsQKqYKht77VcLGRRPqcwSVNLG8Y8VicxDNUXibNgGyFzt0KQmkicVWhibNh/0","wx_city":"","wx_province":"","wechatimg":null,"wx_country":"CN","wechatname":null,"agent":"未加入会员"},{"id":153849,"user_name":"围裙妈妈","wx_sex":0,"status":1,"mp_name":null,"headimg":"http://wx.qlogo.cn/mmopen/giaQibFlLQKPziaLmfd8UeicNAYHtFJTp3eTEmVBSxicjDFx1PriawCV80UYYLyuiaauXaibpk2Nz1VXcWdeYicZDibhy1S3Y0kqMT1r4W/0","wx_city":"","wx_province":"","wechatimg":null,"wx_country":"中国","wechatname":null,"agent":"未加入会员"},{"id":153848,"user_name":"湫","wx_sex":1,"status":3,"mp_name":null,"headimg":"http://wx.qlogo.cn/mmopen/ickBD79gnSZNlicY0UFpibpTzXDibFvEYXV6JGfYFwibbkkCrsXJq2LwIKkCYaY3fib0X1ObT6lVbtzI64JibhHk73oLLf6uJiaAKaib1/0","wx_city":"","wx_province":"","wechatimg":null,"wx_country":"中国","wechatname":null,"agent":"未加入会员"},{"id":153847,"user_name":"卫哥⊙_⊙","wx_sex":1,"status":3,"mp_name":null,"headimg":"http://wx.qlogo.cn/mmopen/ickBD79gnSZO0AuaVl8TJ0l8BF9Ch9dfc5dVwJ3UjwteRfguuINhibRlMmCkYutfYQib844OcKialpiboOpVHnrSmaA1pdaVHe1az/0","wx_city":"广州","wx_province":"广东","wechatimg":null,"wx_country":"中国","wechatname":null,"agent":"未加入会员"},{"id":153847,"user_name":"卫哥⊙_⊙","wx_sex":1,"status":1,"mp_name":null,"headimg":"http://wx.qlogo.cn/mmopen/ickBD79gnSZO0AuaVl8TJ0l8BF9Ch9dfc5dVwJ3UjwteRfguuINhibRlMmCkYutfYQib844OcKialpiboOpVHnrSmaA1pdaVHe1az/0","wx_city":"广州","wx_province":"广东","wechatimg":null,"wx_country":"中国","wechatname":null,"agent":"未加入会员"}]
     * allPages : 2563
     * pageCurrent : 1
     * state : 1
     */

    private int allPages;
    private int pageCurrent;
    private String state;
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
         * id : 100000000
         * user_name : 拇指推官方帐号
         * wx_sex : 1
         * status : null
         * mp_name : 宋广才
         * headimg : http://wx.qlogo.cn/mmopen/et9q9FvjBBrtgMOQcQnYouwYqhavRSMibNVHD9nHv7aShztrCbz8RDRl26jkRmsDIy3aLh1ugmrD2TLibZT9LIvw/0
         * wx_city : 深圳
         * wx_province : 广东
         * wechatimg : null
         * wx_country : 中国
         * wechatname : null
         * agent : 年费会员
         */

        private int id;
        private String user_name;
        private int wx_sex;
        private int status;
        private String mp_name;
        private String headimg;
        private String wx_city;
        private String wx_province;
        private String wechatimg;
        private String wx_country;
        private String wechatname;
        private String agent;
        private int num;

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

        public int getWx_sex() {
            return wx_sex;
        }

        public void setWx_sex(int wx_sex) {
            this.wx_sex = wx_sex;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
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

        public String getWx_city() {
            return wx_city;
        }

        public void setWx_city(String wx_city) {
            this.wx_city = wx_city;
        }

        public String getWx_province() {
            return wx_province;
        }

        public void setWx_province(String wx_province) {
            this.wx_province = wx_province;
        }

        public String getWechatimg() {
            return wechatimg;
        }

        public void setWechatimg(String wechatimg) {
            this.wechatimg = wechatimg;
        }

        public String getWx_country() {
            return wx_country;
        }

        public void setWx_country(String wx_country) {
            this.wx_country = wx_country;
        }

        public String getWechatname() {
            return wechatname;
        }

        public void setWechatname(String wechatname) {
            this.wechatname = wechatname;
        }

        public String getAgent() {
            return agent;
        }

        public void setAgent(String agent) {
            this.agent = agent;
        }

        @Override
        public String toString() {
            return "LoginListBean{" +
                    "id=" + id +
                    ", user_name='" + user_name + '\'' +
                    ", wx_sex=" + wx_sex +
                    ", status=" + status +
                    ", mp_name='" + mp_name + '\'' +
                    ", headimg='" + headimg + '\'' +
                    ", wx_city='" + wx_city + '\'' +
                    ", wx_province='" + wx_province + '\'' +
                    ", wechatimg='" + wechatimg + '\'' +
                    ", wx_country='" + wx_country + '\'' +
                    ", wechatname='" + wechatname + '\'' +
                    ", agent='" + agent + '\'' +
                    '}';
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}
