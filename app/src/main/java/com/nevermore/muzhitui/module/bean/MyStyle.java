package com.nevermore.muzhitui.module.bean;

import java.util.List;

/**
 * Created by hehe on 2016/6/3.
 */
public class MyStyle {

    /**
     * state : 1
     * styList : [{"id":1,"type":0,"obj_id":100001,"url":"123"},{"id":2,"type":0,"obj_id":100001,"url":"upload/2016-06-03/389498324.jpg"}]
     */

    private String state;
    /**
     * id : 1
     * type : 0
     * obj_id : 100001
     * url : 123
     */

    private List<StyListBean> styList;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<StyListBean> getStyList() {
        return styList;
    }

    public void setStyList(List<StyListBean> styList) {
        this.styList = styList;
    }

    public static class StyListBean {
        private String state;
        private int id;
        private int type;
        private int obj_id;
        private String url;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getObj_id() {
            return obj_id;
        }

        public void setObj_id(int obj_id) {
            this.obj_id = obj_id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
