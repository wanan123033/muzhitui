package com.nevermore.muzhitui.module.bean;

/**
 * Created by Administrator on 2017/12/15.
 */

public class VideoBean {
    public String videoUrl;  //视频链接
    public String imageUrl;  //图片链接
    public String videoTitle;  //视频标题

    public int max; //视频大小
    public int progress;  //当前下载的大小
    public String path;  //视频存放路径
    public long duration; //视频时长
    public String videoImgPath;


    @Override
    public String toString() {
        return "VideoBean{" +
                "videoUrl='" + videoUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", videoTitle='" + videoTitle + '\'' +
                ", max=" + max +
                ", progress=" + progress +
                ", path='" + path + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VideoBean)) return false;

        VideoBean videoBean = (VideoBean) o;

        return videoUrl.equals(videoBean.videoUrl);
    }

    @Override
    public int hashCode() {
        return videoUrl.hashCode();
    }
}
