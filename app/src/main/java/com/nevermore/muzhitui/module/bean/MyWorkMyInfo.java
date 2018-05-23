package com.nevermore.muzhitui.module.bean;

/**
 * Created by hehe on 2016/6/3.
 */
public class MyWorkMyInfo {


    /**
     * id : 100001
     * readCount : 578
     * headimg : http://wx.qlogo.cn/mmopen/V4vwVHiarrnibbOlxpRe9bv0hgibdQp9EuFmTIrnox58YtLOyoZWfIlUb8piauAxQ7g5eP1Z6n4H4b1Jsgyib2sZrSyvRQxADmoPU/0
     * state : 1
     * wechatname : James-Soong
     */

    private int id;
    private int readCount;
    private String headimg;
    private String state;
    private String wechatname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getWechatname() {
        return wechatname;
    }

    public void setWechatname(String wechatname) {
        this.wechatname = wechatname;
    }
}
