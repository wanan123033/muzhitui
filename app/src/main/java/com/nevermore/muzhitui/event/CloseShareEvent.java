package com.nevermore.muzhitui.event;

/**
 * Created by Simone on 2017/4/13.
 */

public class CloseShareEvent {
    private int state;//1成功，2.失败3.取消分享
    private String message;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CloseShareEvent(int state, String message) {
        this.state = state;
        this.message = message;
    }
}
