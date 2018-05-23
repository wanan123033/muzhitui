package com.nevermore.muzhitui.module.bean;

/**
 * Created by Administrator on 2016/12/9.
 */

public class AppVersion {
    /**
     * 版本号
     * "version"："1"
     */
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "AppVersion{" +
                "version='" + version + '\'' +
                '}';
    }
}
