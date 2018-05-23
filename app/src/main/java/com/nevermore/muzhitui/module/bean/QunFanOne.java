package com.nevermore.muzhitui.module.bean;

/**
 * Created by Administrator on 2018/1/10.
 */

public class QunFanOne extends com.nevermore.muzhitui.module.BaseBean{
    public String pic1;
    public int id;
    public int sex;
    public String phone;
    public String wx_name;
    public String province;
    public String introduce;
    public String industry_type;
    public String wx_no;
    public String city;
    public String user_name;
    public String headimg;

    @Override
    public String toString() {
        return "QunFanOne{" +
                "state='" + state + '\'' +
                ", msg='" + msg + '\'' +
                ", pic1='" + pic1 + '\'' +
                ", id=" + id +
                ", sex=" + sex +
                ", phone='" + phone + '\'' +
                ", wx_name='" + wx_name + '\'' +
                ", province='" + province + '\'' +
                ", introduce='" + introduce + '\'' +
                ", industry_type='" + industry_type + '\'' +
                ", wx_no='" + wx_no + '\'' +
                ", city='" + city + '\'' +
                ", user_name='" + user_name + '\'' +
                ", headimg='" + headimg + '\'' +
                '}';
    }
}
