package com.nevermore.muzhitui.module.bean;

import java.util.List;

/**
 * Created by hehe on 2016/6/3.
 */
public class MyWork {


    private int allPages;
    private int pageCurrent;
    private String state;

    private List<PageListBean> pageList;

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

    public List<PageListBean> getPageList() {
        return pageList;
    }

    public void setPageList(List<PageListBean> pageList) {
        this.pageList = pageList;
    }

    public static class PageListBean {
        private int id;
        private String title;
        private String website;
        private int forward;
        private String image;
        private int read;
        private int praise;
        private String wechatname;
        private Object loginid;
        private String pagedate;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public int getForward() {
            return forward;
        }

        public void setForward(int forward) {
            this.forward = forward;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getRead() {
            return read;
        }

        public void setRead(int read) {
            this.read = read;
        }

        public int getPraise() {
            return praise;
        }

        public void setPraise(int praise) {
            this.praise = praise;
        }

        public String getWechatname() {
            return wechatname;
        }

        public void setWechatname(String wechatname) {
            this.wechatname = wechatname;
        }

        public Object getLoginid() {
            return loginid;
        }

        public void setLoginid(Object loginid) {
            this.loginid = loginid;
        }

        public String getPagedate() {
            return pagedate;
        }

        public void setPagedate(String pagedate) {
            this.pagedate = pagedate;
        }
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
