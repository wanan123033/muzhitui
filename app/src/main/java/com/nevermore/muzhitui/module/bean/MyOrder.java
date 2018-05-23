package com.nevermore.muzhitui.module.bean;

/**
 * Created by hehe on 2016/6/7.
 */
public class MyOrder extends BaseBean{
    private String agordernum;

    public String getAgordernum() {
        return agordernum;
    }

    @Override
    public String toString() {
        return "MyOrder{" +
                "agordernum='" + agordernum + '\'' +
                "state=" + getState()+
                '}';
    }

    public void setAgordernum(String agordernum) {
        this.agordernum = agordernum;
    }
}
