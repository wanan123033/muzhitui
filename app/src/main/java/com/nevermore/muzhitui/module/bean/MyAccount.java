package com.nevermore.muzhitui.module.bean;

/**
 * Created by hehe on 2016/5/31.
 */
public class MyAccount {

    /**
     * id : 100004
     * joindate : 2016-03-25 00:00:00
     * phonetell : 13249431031
     * pathimg : upload/100004.png
     * headimg : http://www.muzhitui.cn/song/upload/2016-05-24/14640711433367.jpg
     * state : 1
     * tryCount : 99
     * agtype : 1
     * wechat : 13802227557
     * wechatname : he zhao 赵鹤
     * newwallet : -49
     */

    private int id;
    private String joindate;
    private String phonetell;
    private String pathimg="";
    private String headimg;
    private String state;
    private int tryCount;
    private int agtype;
    private String wechat;
    private String wechatname;
    private float newwallet;
    private int profitCount;
    private int memLevels;
    private String user_name;
    private int isExpire;
    private int tryCountYc;
    public int count_attention;
    public int count_fans;
    public int count_dt_unread;
    public int count_fans_add;
    public int count_att_add;
    public int is_agent;
    public int count_profit;

    public int getTryCountYc() {
        return tryCountYc;
    }

    public void setTryCountYc(int tryCountYc) {
        this.tryCountYc = tryCountYc;
    }

    public int getIsExpire() {
        return isExpire;
    }

    public void setIsExpire(int isExpire) {
        this.isExpire = isExpire;
    }
    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJoindate() {
        return joindate;
    }

    public void setJoindate(String joindate) {
        this.joindate = joindate;
    }

    public String getPhonetell() {
        return phonetell;
    }

    public void setPhonetell(String phonetell) {
        this.phonetell = phonetell;
    }

    public String getPathimg() {
        return pathimg;
    }

    public void setPathimg(String pathimg) {
        this.pathimg = pathimg;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getTryCount() {
        return tryCount;
    }

    public void setTryCount(int tryCount) {
        this.tryCount = tryCount;
    }

    public int getAgtype() {
        return agtype;
    }

    public void setAgtype(int agtype) {
        this.agtype = agtype;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getWechatname() {
        return wechatname;
    }

    public void setWechatname(String wechatname) {
        this.wechatname = wechatname;
    }

    public float getNewwallet() {
        return newwallet;
    }

    public void setNewwallet(float newwallet) {
        this.newwallet = newwallet;
    }

    public int getProfitCount() {
        return count_profit;
    }

    public void setProfitCount(int profitCount) {
        this.profitCount = profitCount;
    }

    public int getMemLevels() {
        return memLevels;
    }

    public void setMemLevels(int memLevels) {
        this.memLevels = memLevels;
    }
}
