package com.nevermore.muzhitui.activity.editPhoto;

import java.io.Serializable;

/**
 * 一个图片对象
 *
 * @author Administrator
 */
public class ImageItem implements Serializable {
    public String text = "";
    public String drr;//图片sd地址  上传服务器时把图片调用下面方法压缩后 保存到临时文件夹 图片压缩后小于100KB，失真度不明显
    public boolean isVisable;//是否显示上下箭头移动的。
    public String imageLoadPath;//从网上加载的图片路径 （包括编辑原创请求下来的图片路径，和模板图片请求的路径，视频模板的图片路径）
    public int imageType = 2;//1.纯文字 2.从手机上获取的图片 （包括相册获取，或者拍照） //3.视屏 （获取的本地图片） 4.模板 （从模板页面加载的图片，所以只传图片路径）
    public String url;// 从视频或者模板获取的url

    public String color;
    public int id;
    public int animate = -1;
    public String fontfamily;

    public String txtText;  //只用于显示的文本  不带有html代码的字段
    public float fontSize;  //文本字体大小
    public String pageContent;  //除纯文本,图文 外新增字段，编辑区对应的编辑内容跟视频，模板没有关系


    public ImageItem() {

    }

    public ImageItem(String text, String drr, String imageLoadPath, int imageType, String url,String color,int id,int animate) {
        this.text = text;
        this.imageLoadPath = imageLoadPath;
        this.imageType = imageType;
        this.drr = drr;
        this.url = url;
        this.color = color;
        this.id = id;
        this.animate = animate;
    }

    @Override
    public String toString() {
        return "ImageItem{" +
                "text='" + text + '\'' +
                ", drr='" + drr + '\'' +
                ", isVisable=" + isVisable +
                ", imageLoadPath='" + imageLoadPath + '\'' +
                ", imageType=" + imageType +
                ", url='" + url + '\'' +
                ", color='" + color + '\'' +
                ", id=" + id +
                ", animate=" + animate +
                ", fontfamily='" + fontfamily + '\'' +
                ", txtText='" + txtText + '\'' +
                ", fontSize=" + fontSize +
                ", pageContent='" + pageContent + '\'' +
                '}';
    }
}
