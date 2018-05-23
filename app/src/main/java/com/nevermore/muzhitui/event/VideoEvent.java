package com.nevermore.muzhitui.event;

import android.graphics.Bitmap;

/**
 * Created by hehe on 2016/8/26.
 */
public class VideoEvent {

private int position;
  private String path;
    private Bitmap mBitmap;
    private String text;
    private String url;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public VideoEvent(int position, String path, Bitmap bitmap, String text, String url) {
        this.position = position;
        this.path = path;
        mBitmap = bitmap;
        this.text = text;
        this.url = url;
    }

    @Override
    public String toString() {
        return "VideoEvent{" +
                "position=" + position +
                ", path='" + path + '\'' +
                ", mBitmap=" + mBitmap +
                ", text='" + text + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
