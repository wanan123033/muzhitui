package com.nevermore.muzhitui.module.bean;

/**
 * Created by hehe on 2016/6/22.
 */
public class PageUpdate {
    
    private int id;
    private String website;
    private String state;
    private String pageContent;
    private String image;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPageContent() {
        return pageContent;
    }

    public void setPageContent(String pageContent) {
        this.pageContent = pageContent;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "PageUpdate{" +
                "id=" + id +
                ", website='" + website + '\'' +
                ", state='" + state + '\'' +
                ", pageContent='" + pageContent + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
