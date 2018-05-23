package com.nevermore.muzhitui.module.bean;

import java.util.List;

/**
 * Created by hehe on 2016/5/26.
 */
public class Works {


    /**
     * allPages : 2
     * pageList : [{"id":70,"title":"教育孩子\u201c不能吃亏\u201d这是错误的理念","website":"http://mp.weixin.qq.com/s?__biz=MjM5MzU4NjcyNA==&mid=402646628&idx=3&sn=8a7e9d65d7c02e5def490b138731b498&scene=7#wechat_redirect","forward":0,"image":"upload/146089192364065.jpg","read":5,"praise":0,"wechatname":"James-Soong","loginid":null,"pagedate":"2016-04-17"},{"id":106,"title":"教育孩子\u201c不能吃亏\u201d这是错误的理念","website":"http://mp.weixin.qq.com/s?__biz=MjM5MzU4NjcyNA==&mid=402646628&idx=3&sn=8a7e9d65d7c02e5def490b138731b498&scene=7#wechat_redirect","forward":0,"image":"upload/146089192364065.jpg","read":5,"praise":0,"wechatname":"James-Soong","loginid":null,"pagedate":"2016-04-17"},{"id":69,"title":"市场匍匐前行、重心不断上移、全球各大市场的联动性加强","website":"http://mp.weixin.qq.com/s?__biz=MzI5OTE2MjA0MA==&mid=2661076106&idx=2&sn=62d690b07ed4755bba27de2af713ed86&scene=0#wechat_redirect","forward":0,"image":"upload/146089046773646.jpg","read":1,"praise":0,"wechatname":"James-Soong","loginid":null,"pagedate":"2016-04-17"},{"id":99,"title":"市场匍匐前行、重心不断上移、全球各大市场的联动性加强","website":"http://mp.weixin.qq.com/s?__biz=MzI5OTE2MjA0MA==&mid=2661076106&idx=2&sn=62d690b07ed4755bba27de2af713ed86&scene=0#wechat_redirect","forward":0,"image":"upload/146089046773646.jpg","read":1,"praise":0,"wechatname":"James-Soong","loginid":null,"pagedate":"2016-04-17"},{"id":67,"title":"财务报表粉饰实务分析（附案例参考）","website":"http://mp.weixin.qq.com/s?__biz=MzI5OTE2MjA0MA==&mid=2661076106&idx=3&sn=99b5ce37ad5d5faf675e5e188c81c0fe&scene=0#wechat_redirect","forward":0,"image":"","read":1,"praise":0,"wechatname":"James-Soong","loginid":null,"pagedate":"2016-04-17"},{"id":68,"title":"财务报表粉饰实务分析（附案例参考）","website":"http://mp.weixin.qq.com/s?__biz=MzI5OTE2MjA0MA==&mid=2661076106&idx=3&sn=99b5ce37ad5d5faf675e5e188c81c0fe&scene=0#wechat_redirect","forward":0,"image":"","read":1,"praise":0,"wechatname":"James-Soong","loginid":null,"pagedate":"2016-04-17"},{"id":94,"title":"财务报表粉饰实务分析（附案例参考）","website":"http://mp.weixin.qq.com/s?__biz=MzI5OTE2MjA0MA==&mid=2661076106&idx=3&sn=99b5ce37ad5d5faf675e5e188c81c0fe&scene=0#wechat_redirect","forward":0,"image":"","read":1,"praise":0,"wechatname":"James-Soong","loginid":null,"pagedate":"2016-04-17"},{"id":95,"title":"财务报表粉饰实务分析（附案例参考）","website":"http://mp.weixin.qq.com/s?__biz=MzI5OTE2MjA0MA==&mid=2661076106&idx=3&sn=99b5ce37ad5d5faf675e5e188c81c0fe&scene=0#wechat_redirect","forward":0,"image":"","read":1,"praise":0,"wechatname":"James-Soong","loginid":null,"pagedate":"2016-04-17"},{"id":97,"title":"财务报表粉饰实务分析（附案例参考）","website":"http://mp.weixin.qq.com/s?__biz=MzI5OTE2MjA0MA==&mid=2661076106&idx=3&sn=99b5ce37ad5d5faf675e5e188c81c0fe&scene=0#wechat_redirect","forward":0,"image":"","read":1,"praise":0,"wechatname":"James-Soong","loginid":null,"pagedate":"2016-04-17"},{"id":103,"title":"财务报表粉饰实务分析（附案例参考）","website":"http://mp.weixin.qq.com/s?__biz=MzI5OTE2MjA0MA==&mid=2661076106&idx=3&sn=99b5ce37ad5d5faf675e5e188c81c0fe&scene=0#wechat_redirect","forward":0,"image":"","read":1,"praise":0,"wechatname":"James-Soong","loginid":null,"pagedate":"2016-04-17"}]
     * pageCurrent : 1
     * state : 1
     */

    private int allPages;
    private int pageCurrent;
    private String state;
    /**
     * id : 70
     * title : 教育孩子“不能吃亏”这是错误的理念
     * website : http://mp.weixin.qq.com/s?__biz=MjM5MzU4NjcyNA==&mid=402646628&idx=3&sn=8a7e9d65d7c02e5def490b138731b498&scene=7#wechat_redirect
     * forward : 0
     * image : upload/146089192364065.jpg
     * read : 5
     * praise : 0
     * wechatname : James-Soong
     * loginid : null
     * pagedate : 2016-04-17
     *  "update_time": "2017-02-20",
     "user_name": "宋广才"
     title_pic:""
     */

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
        private String user_name;
        private String update_time;
        private String title_pic;







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

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getTitle_pic() {
            return title_pic;
        }

        public void setTitle_pic(String title_pic) {
            this.title_pic = title_pic;
        }
    }
}
