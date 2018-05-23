package com.nevermore.muzhitui.module.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/8.
 */

public class MusicList {
    private int status;
    private String error;
    private String tab;
    private List<MusicData> info;

    public static class MusicData{
        private int filesize;
        private String singername;
        private String topic;
        private List<MusicInfo> group;

        public int getFilesize() {
            return filesize;
        }

        public void setFilesize(int filesize) {
            this.filesize = filesize;
        }

        public String getSingername() {
            return singername;
        }

        public void setSingername(String singername) {
            this.singername = singername;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public List<MusicInfo> getGroup() {
            return group;
        }

        public void setGroup(List<MusicInfo> group) {
            this.group = group;
        }

        @Override
        public String toString() {
            return "MusicData{" +
                    "filesize=" + filesize +
                    ", singername='" + singername + '\'' +
                    ", topic='" + topic + '\'' +
                    ", group=" + group.toString() +
                    '}';
        }
    }
    public static class MusicInfo{
        private String hash;
        private double price;

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        @Override
        public String toString() {
            return "MusicInfo{" +
                    "hash='" + hash + '\'' +
                    ", price=" + price +
                    '}';
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public List<MusicData> getInfo() {
        return info;
    }

    public void setInfo(List<MusicData> info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "MusicList{" +
                "status=" + status +
                ", error='" + error + '\'' +
                ", tab='" + tab + '\'' +
                ", info=" + info.toString() +
                '}';
    }
}
