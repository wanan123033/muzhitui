package com.nevermore.muzhitui.module.bean;

import java.util.List;

/**
 * Created by Simone on 2017/3/1.
 */

public class EditOrifinal {

    /**
     * id : 87
     * title : 底色啦
     * pageMeDetails : [{"id":302,"page_content":"卷子","page_type":2,"page_sort":1,"pageme_id":87,"page_picpath":null},{"id":301,"page_content":null,"page_type":2,"page_sort":2,"pageme_id":87,"page_picpath":"upload/2017-03-01/148836131487432.jpg"}]
     * state : 1
     * title_pic : upload/2017-03-01/14883613600900.jpg
     * music_url : null
     * msg : 原创文章单条信息获取成功
     */

    private int id;
    private String title;
    private String state;
    private String title_pic;
    private Object music_url;
    private String msg;
    private List<PageMeDetailsBean> pageMeDetails;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTitle_pic() {
        return title_pic;
    }

    public void setTitle_pic(String title_pic) {
        this.title_pic = title_pic;
    }

    public Object getMusic_url() {
        return music_url;
    }

    public void setMusic_url(Object music_url) {
        this.music_url = music_url;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<PageMeDetailsBean> getPageMeDetails() {
        return pageMeDetails;
    }

    public void setPageMeDetails(List<PageMeDetailsBean> pageMeDetails) {
        this.pageMeDetails = pageMeDetails;
    }

    public static class PageMeDetailsBean {
        /**
         * id : 302
         * page_content : 卷子
         * page_type : 2
         * page_sort : 1
         * pageme_id : 87
         * page_picpath : null
         */

        private int id;
        private String page_content;
        private int page_type;
        private int page_sort;
        private int pageme_id;
        private String page_picpath;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPage_content() {
            return page_content;
        }

        public void setPage_content(String page_content) {
            this.page_content = page_content;
        }

        public int getPage_type() {
            return page_type;
        }

        public void setPage_type(int page_type) {
            this.page_type = page_type;
        }

        public int getPage_sort() {
            return page_sort;
        }

        public void setPage_sort(int page_sort) {
            this.page_sort = page_sort;
        }

        public int getPageme_id() {
            return pageme_id;
        }

        public void setPageme_id(int pageme_id) {
            this.pageme_id = pageme_id;
        }

        public String getPage_picpath() {
            return page_picpath;
        }

        public void setPage_picpath(String page_picpath) {
            this.page_picpath = page_picpath;
        }
    }
}
