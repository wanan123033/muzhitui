package com.nevermore.muzhitui.module.bean;

import java.util.List;

/**
 * Created by Simone on 2017/1/3.
 */

public class Video {

    /**
     * state : 1
     * vedioArray : [{"img_url":"upload/2016-12-29/148299903589720.jpg","show_number":1,"title":"基本操作","jump_url":"http://www.muzhitui.cn/song/wx/base.html?from=message&isappinstalled=0"},{"img_url":"upload/2016-12-29/148299847880064.png","show_number":2,"title":"名片设置","jump_url":"http://www.muzhitui.cn/song/wx/card.html?from=message&isappinstalled=0"}]
     */

    private String state;
    private List<VedioArrayBean> vedioArray;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<VedioArrayBean> getVedioArray() {
        return vedioArray;
    }

    public void setVedioArray(List<VedioArrayBean> vedioArray) {
        this.vedioArray = vedioArray;
    }

    public static class VedioArrayBean {
        /**
         * img_url : upload/2016-12-29/148299903589720.jpg
         * show_number : 1
         * title : 基本操作
         * jump_url : http://www.muzhitui.cn/song/wx/base.html?from=message&isappinstalled=0
         * is_share  1 能分享链接  0 不能分享链接
         * jump_url_c：不能分享时需要复制的连接
         */

        private String img_url;
        private int show_number;
        private String title;
        private String jump_url;
        private int is_share;
        private String jump_url_c;//

        public String getJump_url_c() {
            return jump_url_c;
        }

        public void setJump_url_c(String jump_url_c) {
            this.jump_url_c = jump_url_c;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public int getShow_number() {
            return show_number;
        }

        public void setShow_number(int show_number) {
            this.show_number = show_number;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getJump_url() {
            return jump_url;
        }

        public void setJump_url(String jump_url) {
            this.jump_url = jump_url;
        }

        public int getIs_share() {
            return is_share;
        }

        public void setIs_share(int is_share) {
            this.is_share = is_share;
        }

        @Override
        public String toString() {
            return "VedioArrayBean{" +
                    "img_url='" + img_url + '\'' +
                    ", show_number=" + show_number +
                    ", title='" + title + '\'' +
                    ", jump_url='" + jump_url + '\'' +
                    ", is_share=" + is_share +
                    ", jump_url_c='" + jump_url_c + '\'' +
                    '}';
        }
    }
}
