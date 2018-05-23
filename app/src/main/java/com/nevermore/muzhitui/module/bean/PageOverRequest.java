package com.nevermore.muzhitui.module.bean;

/**
 * Created by hehe on 2016/6/17.
 */
public class PageOverRequest {

    private String pagehtml;
    private String title;
    private String website;
    private String image;
    private int state;
    private String id;
    private int infoShow;

    public String getPagehtml() {
        return pagehtml;
    }

    public void setPagehtml(String pagehtml) {
        this.pagehtml = pagehtml;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getInfoShow() {
        return infoShow;
    }

    public void setInfoShow(int infoShow) {
        this.infoShow = infoShow;
    }
}
