package com.nevermore.muzhitui.module.bean;

import com.orhanobut.logger.Logger;

/**
 * Created by hehe on 2016/6/24.
 */
public class BaseMyMode {
    private int modeId;
    private String modeTable;


    public int getModeId() {
        return modeId;
    }

    public void setModeId(int modeId) {
        Logger.i("setModeId =" + modeId);
        this.modeId = modeId;
    }

    public String getModeTable() {
        return modeTable;
    }

    public void setModeTable(String modeTable) {
        this.modeTable = modeTable;
    }
}
