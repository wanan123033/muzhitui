package com.nevermore.muzhitui.event;

import com.nevermore.muzhitui.module.bean.MusicDataBean;
import com.nevermore.muzhitui.module.bean.SongInfo;

/**
 * Created by Simone on 2017/1/3.
 */

public class EditInfoEvent {
    private int flag;
    private String info;
    private String state;
    private String text;
    private SongInfo music;

    private int textType;

    public int getTextType() {
        return textType;
    }

    public void setTextType(int textType) {
        this.textType = textType;
    }

    public EditInfoEvent(SongInfo music, int flag) {
        this.music = music;
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public SongInfo getMusic() {
        return music;
    }

    public void setMusic(SongInfo music) {
        this.music = music;
    }

    public EditInfoEvent(int flag, String info, String state) {
        this.flag = flag;
        this.info = info;
        this.state=state;

    }

    public EditInfoEvent(int flag, String info, String state, String txttext) {
        this.flag = flag;
        this.info = info;
        this.state = state;
        this.text = txttext;
    }

    @Override
    public String toString() {
        return "EditInfoEvent{" +
                "flag=" + flag +
                ", info='" + info + '\'' +
                ", state='" + state + '\'' +
                ", text='" + text + '\'' +
                ", music=" + music +
                '}';
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
