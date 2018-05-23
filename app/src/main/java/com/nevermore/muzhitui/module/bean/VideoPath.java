package com.nevermore.muzhitui.module.bean;


import java.util.List;

/**
 * Created by Administrator on 2017/12/7.
 */

public class VideoPath extends com.nevermore.muzhitui.module.BaseBean{
    public List<VideoUrl> arrayList;
    public static class VideoUrl{
        public int show_number;
        public String title;
        public String jump_url;

        @Override
        public String toString() {
            return "VideoUrl{" +
                    "show_number=" + show_number +
                    ", title='" + title + '\'' +
                    ", jump_url='" + jump_url + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "VideoPath{" +
                "arrayList=" + arrayList +
                '}';
    }
}
