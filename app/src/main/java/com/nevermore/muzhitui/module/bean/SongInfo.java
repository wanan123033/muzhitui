package com.nevermore.muzhitui.module.bean;

/**
 * Created by Administrator on 2017/8/8.
 */

public class SongInfo {
    private String imgUrl;  //图片地址
    private String url;     //播放地址
    private String songName; //歌曲名称
    private String singerName; //歌手
    private String extName;    //文件格式
    private int fileSize;      //文件大小

    @Override
    public String toString() {
        return "SongInfo{" +
                "imgUrl='" + imgUrl + '\'' +
                ", url='" + url + '\'' +
                ", songName='" + songName + '\'' +
                ", singerName='" + singerName + '\'' +
                ", extName='" + extName + '\'' +
                ", fileSize=" + fileSize +
                '}';
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getExtName() {
        return extName;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }
}
