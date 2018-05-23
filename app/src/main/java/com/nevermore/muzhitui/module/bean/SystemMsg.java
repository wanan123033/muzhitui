package com.nevermore.muzhitui.module.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */

public class SystemMsg {
    private List<Notice> noticeArray;

    public List<Notice> getNoticeArray() {
        return noticeArray;
    }

    public void setNoticeArray(List<Notice> noticeArray) {
        this.noticeArray = noticeArray;
    }

    @Override
    public String toString() {
        return "SystemMsg{" +
                "noticeArray=" + noticeArray +
                '}';
    }

    public static class Notice{
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Notice notice = (Notice) o;

            return id == notice.id;

        }

        @Override
        public int hashCode() {
            return id;
        }

        private String img_url;
        private int id;
        private String title;
        private String update_time;
        private String content_1;
        private String url;

        @Override
        public String toString() {
            return "Notice{" +
                    "img_url='" + img_url + '\'' +
                    ", id=" + id +
                    ", title='" + title + '\'' +
                    ", update_time='" + update_time + '\'' +
                    ", content_1='" + content_1 + '\'' +
                    ", url='" + url + '\'' +
                    '}';
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

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getContent_1() {
            return content_1;
        }

        public void setContent_1(String content_1) {
            this.content_1 = content_1;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
