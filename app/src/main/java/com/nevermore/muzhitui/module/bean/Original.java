package com.nevermore.muzhitui.module.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by simone on 2016/12/14.
 */

public class Original implements Serializable{


    /**
     * id : 88
     * title : 测试音乐
     * music_name : 瓦妮莎的微笑
     * adv_pic:底部广告图
     * adv_url:url
     * pageMeDetails : [{"id":304,"page_content":"第一课","page_type":2,"page_sort":1,"pageme_id":88,"page_picpath":null},{"id":303,"page_content":"第二个","page_type":2,"page_sort":2,"pageme_id":88,"page_picpath":"upload/2017-03-01/148836340168441.jpg"}]
     * state : 1
     * title_pic : upload/2017-03-01/148836340184488.jpg
     * music_url : http://music.ivwen.com/music/WaNiShaDeWeiXiao.m4a
     * msg : 原创文章单条信息获取成功
     */

    private int id;
    private String title;
    private String music_name;
    private String state;
    private String adv_pic;
    private String adv_url;
    private String title_pic;
    private String music_url;
    private String msg;
    private int info_show;
    private String adv_adtext;
    private int adv_id;
    private String singer_name;
    private String adv_adcolor;
    private String adv_font;
    private float adv_size;
    private int adv_animate;

    public String getAdv_adtext() {
        return adv_adtext;
    }

    public void setAdv_adtext(String adv_adtext) {
        this.adv_adtext = adv_adtext;
    }

    public int getAdv_id() {
        return adv_id;
    }

    public void setAdv_id(int adv_id) {
        this.adv_id = adv_id;
    }

    public String getSinger_name() {
        return singer_name;
    }

    public void setSinger_name(String singer_name) {
        this.singer_name = singer_name;
    }

    public String getAdv_adcolor() {
        return adv_adcolor;
    }

    public void setAdv_adcolor(String adv_adcolor) {
        this.adv_adcolor = adv_adcolor;
    }

    public String getAdv_font() {
        return adv_font;
    }

    public void setAdv_font(String adv_font) {
        this.adv_font = adv_font;
    }

    public float getAdv_size() {
        return adv_size;
    }

    public void setAdv_size(float adv_size) {
        this.adv_size = adv_size;
    }

    public int getAdv_animate() {
        return adv_animate;
    }

    public void setAdv_animate(int adv_animate) {
        this.adv_animate = adv_animate;
    }

    public int getInfo_show() {
        return info_show;
    }

    public void setInfo_show(int info_show) {
        this.info_show = info_show;
    }
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

    public String getMusic_name() {
        return music_name;
    }

    public void setMusic_name(String music_name) {
        this.music_name = music_name;
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

    public String getMusic_url() {
        return music_url;
    }

    public void setMusic_url(String music_url) {
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

    public String getAdv_pic() {
        return adv_pic;
    }

    public void setAdv_pic(String adv_pic) {
        this.adv_pic = adv_pic;
    }

    public String getAdv_url() {
        return adv_url;
    }

    public void setAdv_url(String adv_url) {
        this.adv_url = adv_url;
    }

    public static class PageMeDetailsBean {
        /**
         * id : 304
         * page_content : 第一课
         * page_type : 2
         * page_sort : 1
         * pageme_id : 88
         * page_picpath : null
         */

        private int id;
        private String page_content;
        private int page_type;
        private int page_sort;
        private int pageme_id;
        private String page_picpath;
        private String page_url;
        private String adv_font;
        private float adv_size;
        private int adv_id;
        private String adv_adcolor;
        private String adv_adtext;
        private int adv_animate;

        public int getAdv_animate() {
            return adv_animate;
        }

        public void setAdv_animate(int adv_animate) {
            this.adv_animate = adv_animate;
        }

        public String getAdv_adtext() {
            return adv_adtext;
        }

        public void setAdv_adtext(String adv_adtext) {
            this.adv_adtext = adv_adtext;
        }

        public String getAdv_font() {
            return adv_font;
        }

        public void setAdv_font(String adv_font) {
            this.adv_font = adv_font;
        }

        public float getAdv_size() {
            return adv_size;
        }

        public void setAdv_size(float adv_size) {
            this.adv_size = adv_size;
        }

        public int getAdv_id() {
            return adv_id;
        }

        public void setAdv_id(int adv_id) {
            this.adv_id = adv_id;
        }

        public String getAdv_adcolor() {
            return adv_adcolor;
        }

        public void setAdv_adcolor(String adv_adcolor) {
            this.adv_adcolor = adv_adcolor;
        }

        public String getPage_url() {
            return page_url;
        }

        public void setPage_url(String page_url) {
            this.page_url = page_url;
        }
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

        @Override
        public String toString() {
            return "PageMeDetailsBean{" +
                    "id=" + id +
                    ", page_content='" + page_content + '\'' +
                    ", page_type=" + page_type +
                    ", page_sort=" + page_sort +
                    ", pageme_id=" + pageme_id +
                    ", page_picpath='" + page_picpath + '\'' +
                    ", page_url='" + page_url + '\'' +
                    ", adv_font='" + adv_font + '\'' +
                    ", adv_size=" + adv_size +
                    ", adv_id=" + adv_id +
                    ", adv_adcolor='" + adv_adcolor + '\'' +
                    ", adv_adtext='" + adv_adtext + '\'' +
                    ", adv_animate=" + adv_animate +
                    '}';
        }
    }
}
