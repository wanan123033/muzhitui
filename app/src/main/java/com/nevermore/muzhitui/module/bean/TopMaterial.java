package com.nevermore.muzhitui.module.bean;

import java.util.List;

/**
 * Created by hehe on 2016/6/14.
 */
public class TopMaterial {


    /**
     * state : 1
     * screens : [{"show_number":1,"img_url":"upload/145878819401957.jpg","id":12,"title":"从国务院对城市总规的批复，看中国城市的真实排名","flag":1,"is_release":1,"jump_url":"http://mp.weixin.qq.com/s?__biz=MjM5MjAxNDM4MA==&mid=417137743&idx=1&sn=cdd000365b692f3634193d441a53a518&3rd=MzA3MDU4NTYzMw==&scene=6#wechat_redirect","time_out":null,"resolution":null,"size":null},{"show_number":1,"img_url":"upload/146503100685013.jpg","id":16,"title":"代理招商","flag":2,"is_release":1,"jump_url":"http://d.eqxiu.com/s/rJPcRb2p?from=singlemessage&isappinstalled=0","time_out":null,"resolution":null,"size":null},{"show_number":2,"img_url":"upload/145878854941517.jpg","id":11,"title":"疫苗案震惊全国！李克强批示罕见发狠话","flag":1,"is_release":1,"jump_url":"http://toutiao.com/group/6265108470396092930/?iid=3554235807&app=news_article&wxshare_count=1&tt_from=weixin&utm_source=weixin&utm_medium=toutiao_android&utm_campaign=client_share","time_out":null,"resolution":null,"size":null},{"show_number":2,"img_url":"upload/146529611544934.png","id":17,"title":"个人用户二维码","flag":2,"is_release":1,"jump_url":"http://c.eqxiu.com/s/LuqZONPL","time_out":null,"resolution":null,"size":null},{"show_number":2,"img_url":"upload/146496745306615.png","id":19,"title":"底部广告拇指推","flag":2,"is_release":1,"jump_url":null,"time_out":null,"resolution":null,"size":null},{"show_number":3,"img_url":"upload/145878891748392.jpg","id":13,"title":"女友在嘿嘿时说过那些奇葩话？","flag":1,"is_release":1,"jump_url":"http://mp.weixin.qq.com/s?__biz=MjMyMzYyNzg2MA==&mid=402866579&idx=1&sn=7ada8d6fa41535b0c854dfd3e70d0a07&3rd=MzA3MDU4NTYzMw==&scene=6#wechat_redirect","time_out":null,"resolution":null,"size":null}]
     */

    private String state;
    /**
     * show_number : 1
     * img_url : upload/145878819401957.jpg
     * id : 12
     * title : 从国务院对城市总规的批复，看中国城市的真实排名
     * flag : 1
     * is_release : 1
     * jump_url : http://mp.weixin.qq.com/s?__biz=MjM5MjAxNDM4MA==&mid=417137743&idx=1&sn=cdd000365b692f3634193d441a53a518&3rd=MzA3MDU4NTYzMw==&scene=6#wechat_redirect
     * time_out : null
     * resolution : null
     * size : null
     */

    private List<ScreensBean> screens;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<ScreensBean> getScreens() {
        return screens;
    }

    public void setScreens(List<ScreensBean> screens) {
        this.screens = screens;
    }

    public static class ScreensBean {
        private int show_number;
        private String img_url;
        private int id;
        private String title;
        private int flag;
        private int is_release;
        private String jump_url;
        private Object time_out;
        private Object resolution;
        private Object size;

        public int getShow_number() {
            return show_number;
        }

        public void setShow_number(int show_number) {
            this.show_number = show_number;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
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

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public int getIs_release() {
            return is_release;
        }

        public void setIs_release(int is_release) {
            this.is_release = is_release;
        }

        public String getJump_url() {
            return jump_url;
        }

        public void setJump_url(String jump_url) {
            this.jump_url = jump_url;
        }

        public Object getTime_out() {
            return time_out;
        }

        public void setTime_out(Object time_out) {
            this.time_out = time_out;
        }

        public Object getResolution() {
            return resolution;
        }

        public void setResolution(Object resolution) {
            this.resolution = resolution;
        }

        public Object getSize() {
            return size;
        }

        public void setSize(Object size) {
            this.size = size;
        }
    }
}
