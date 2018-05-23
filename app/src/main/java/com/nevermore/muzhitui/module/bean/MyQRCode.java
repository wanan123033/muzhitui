package com.nevermore.muzhitui.module.bean;

/**
 * Created by hehe on 2016/5/31.
 */
public class MyQRCode {

    /**
     * id : 100004
     * pathimg : upload/100004.png
     * headimg : http://www.muzhitui.cn/song/upload/2016-05-24/14640711433367.jpg
     * state : 1
     * wechatname : he zhao 赵鹤
     */

    private int id;
    private String pathimg;
    private String headimg;
    private String state;
    private String wechatname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPathimg() {
        return pathimg;
    }

    public void setPathimg(String pathimg) {
        this.pathimg = pathimg;
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
