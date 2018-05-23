package com.nevermore.muzhitui.module.bean;

/**
 * Created by Administrator on 2018/1/3.
 *
 * state  1 成功，0失败 2 无此序号
 */

public class PageGPSBean extends com.nevermore.muzhitui.module.BaseBean{


    public String img_url;
    public int show_number;
    public String title;
    public int is_release;
    public String jump_url;

    @Override
    public String toString() {
        return "PageGPSBean{" +
                "img_url='" + img_url + '\'' +
                ", show_number=" + show_number +
                ", title='" + title + '\'' +
                ", is_release=" + is_release +
                ", jump_url='" + jump_url + '\'' +
                '}';
    }
}
