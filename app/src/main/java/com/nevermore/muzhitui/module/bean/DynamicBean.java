package com.nevermore.muzhitui.module.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/9/11.
 */

public class DynamicBean {
    private int allPages;
    private List<Dynamic> dtList;
    private int pageCurrent;
    private String state;
    private String msg;

    public int getAllPages() {
        return allPages;
    }

    public void setAllPages(int allPages) {
        this.allPages = allPages;
    }

    public List<Dynamic> getDtList() {
        return dtList;
    }

    public void setDtList(List<Dynamic> dtList) {
        this.dtList = dtList;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "DynamicBean{" +
                "allPages=" + allPages +
                ", dtList=" + dtList +
                ", pageCurrent=" + pageCurrent +
                ", state='" + state + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

    public static class Dynamic implements Serializable {
        private String content;
        private Integer id;
        private String user_name;
        private String time;
        private List<Pic> pics;
        private int reply_count;
        private int fans_type;
        private String headimg;
        private int praise_type;
        private int loginid;
        private int praise_count;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Dynamic dynamic = (Dynamic) o;

            return id == dynamic.id;
        }

        @Override
        public int hashCode() {
            return id;
        }

        @Override
        public String toString() {
            return "Dynamic{" +
                    "content='" + content + '\'' +
                    ", id=" + id +
                    ", user_name='" + user_name + '\'' +
                    ", time='" + time + '\'' +
                    ", pics=" + pics +
                    ", reply_count=" + reply_count +
                    ", fans_type=" + fans_type +
                    ", headimg='" + headimg + '\'' +
                    ", praise_type=" + praise_type +
                    ", loginid=" + loginid +
                    ", praise_count=" + praise_count +
                    '}';
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Integer getId() {
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

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public List<Pic> getPics() {
            return pics;
        }

        public void setPics(List<Pic> pics) {
            this.pics = pics;
        }

        public int getReply_count() {
            return reply_count;
        }

        public void setReply_count(int reply_count) {
            this.reply_count = reply_count;
        }

        public int getFans_type() {
            return fans_type;
        }

        public void setFans_type(int fans_type) {
            this.fans_type = fans_type;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public int getPraise_type() {
            return praise_type;
        }

        public void setPraise_type(int praise_type) {
            this.praise_type = praise_type;
        }

        public int getLoginid() {
            return loginid;
        }

        public void setLoginid(int loginid) {
            this.loginid = loginid;
        }

        public int getPraise_count() {
            return praise_count;
        }

        public void setPraise_count(int praise_count) {
            this.praise_count = praise_count;
        }
    }
    public static class Pic implements Serializable{
        private int page_sort;
        private String page_picpath;

        @Override
        public String toString() {
            return "Pic{" +
                    "page_sort=" + page_sort +
                    ", page_picpath='" + page_picpath + '\'' +
                    '}';
        }

        public int getPage_sort() {
            return page_sort;
        }

        public void setPage_sort(int page_sort) {
            this.page_sort = page_sort;
        }

        public String getPage_picpath() {
            return page_picpath;
        }

        public void setPage_picpath(String page_picpath) {
            this.page_picpath = page_picpath;
        }
    }
}
