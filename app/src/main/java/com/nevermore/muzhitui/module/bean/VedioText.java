package com.nevermore.muzhitui.module.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Simone on 2017/3/6.
 */

public class VedioText implements Serializable {
    /**
     * allPages : 1
     * pageList : [{"show_number":1,"id":14,"title":"标题 1","update_time":"2017-03-03","jump_url":"https://www.meipian.cn/dbj6xot?from=timeline"},{"show_number":2,"id":15,"title":"标题2","update_time":"2017-03-03","jump_url":"https://www.meipian.cn/dbj6xot?from=timeline"},{"show_number":3,"id":16,"title":"问题3","update_time":"2017-03-03","jump_url":"https://www.meipian.cn/dbj6xot?from=timeline"},{"show_number":4,"id":17,"title":"问题4","update_time":"2017-03-03","jump_url":"https://www.meipian.cn/dbj6xot?from=timeline"}]
     * pageCurrent : 1
     * state : 1
     */

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<PageListBean> getPageList() {
        return pageList;
    }

    public void setPageList(List<PageListBean> pageList) {
        this.pageList = pageList;
    }

    public static class PageListBean {
        /**
         * show_number : 1
         * id : 14
         * title : 标题 1
         * update_time : 2017-03-03
         * jump_url : https://www.meipian.cn/dbj6xot?from=timeline
         */

        private int show_number;
        private int id;
        private String title;
        private String update_time;
        private String jump_url;

        public int getShow_number() {
            return show_number;
        }

        public void setShow_number(int show_number) {
            this.show_number = show_number;
        }

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

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getJump_url() {
            return jump_url;
        }

        public void setJump_url(String jump_url) {
            this.jump_url = jump_url;
        }
        boolean isCheck=false;


        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }
    }
}
