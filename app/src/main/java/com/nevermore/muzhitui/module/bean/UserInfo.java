package com.nevermore.muzhitui.module.bean;

import java.io.Serializable;

/**
 * Created by Simone on 2017/1/11.
 */

public class UserInfo implements Serializable {
    /**
     * login : {"agent":"未加入会员","agtype":3,"card_count":0,"distributor_id":"","flag":0,"followdate":"2016-03-26 20:38:08","headimg":"http://wx.qlogo.cn/mmopen/giaQibFlLQKPwOyOzu5vZic9u9Qf1re7j2JRxF6HJKXhWmChvlJG2GNymK0ByPweSgcXPwXaicJZ0djDUs1jCiblqvqyWWicDpicOTJ/0","id":100014,"is_card":0,"is_show":0,"is_vip":0,"joindate":"","logins":"","mp_code":"","mp_company":"","mp_desc":"","mp_link":"","mp_name":"","newwallet":0,"nums":0,"openid":"onxrevtZCLBrrPPqjLLUJEvSQre8","openid-app":"","password":"","pathimg":"upload/100014.png","phone":"15112673244","phonetell":"","profit":0,"rongyun_token":"","shipagid":4,"source":"","status":"","subscribe":1,"tjdistributor_id":"","tjloginid":100001,"tjloginmoney":"","try_count":3,"unionid":"orIR2v2ypM7j3oPdPtowBn6ha2JA","update_time":"","wallet":0,"wechat":"","wechatimg":"http://wx.qlogo.cn/mmopen/giaQibFlLQKPwOyOzu5vZic9u9Qf1re7j2JRxF6HJKXhWmChvlJG2GNymK0ByPweSgcXPwXaicJZ0djDUs1jCiblqvqyWWicDpicOTJ/0","wechatname":"秋","wx_city":"大连","wx_country":"中国","wx_province":"辽宁","wx_sex":2}
     * msg : 查找好友成功
     * state : 1
     */
    private Login login;

    private String msg;

    private String state;

    public void setLogin(Login login){
        this.login = login;
    }
    public Login getLogin(){
        return this.login;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
    public void setState(String state){
        this.state = state;
    }
    public String getState(){
        return this.state;
    }
    public class Login {
        private String phone;

        private String joindate;

        private String phonetell;

        private int is_vip;

        private String unionid;

        private String mp_link;

        private String wx_province;



        private int tjloginid;

        private String password;

        private String tjdistributor_id;

        private double wallet;

        private String logins;

        private int id;

        private int card_count;

        private String wx_sex;

        private int subscribe;

        private String mp_name;

        private String pathimg;

        private int try_count;

        private String openid;

        private int agtype;

        private String wechatname;

        private int is_card;

        private String followdate;

        private double profit;

        private int status;

        private String headimg;

        private String distributor_id;

        private String agent;

        private int is_show;

        private int flag;

        private String update_time;

        private String mp_code;

        private String source;

        private Object mp_desc;

        private int nums;

        private String mp_company;

        private int shipagid;

        private String wx_city;

        private String wechatimg;

        private String wechat;

        private String wx_country;

        private double newwallet;

        private String tjloginmoney;

        private String rongyun_token;

        public void setPhone(String phone){
            this.phone = phone;
        }
        public String getPhone(){
            return this.phone;
        }
        public void setJoindate(String joindate){
            this.joindate = joindate;
        }
        public String getJoindate(){
            return this.joindate;
        }
        public void setPhonetell(String phonetell){
            this.phonetell = phonetell;
        }
        public String getPhonetell(){
            return this.phonetell;
        }
        public void setIs_vip(int is_vip){
            this.is_vip = is_vip;
        }
        public int getIs_vip(){
            return this.is_vip;
        }
        public void setUnionid(String unionid){
            this.unionid = unionid;
        }
        public String getUnionid(){
            return this.unionid;
        }
        public void setMp_link(String mp_link){
            this.mp_link = mp_link;
        }
        public String getMp_link(){
            return this.mp_link;
        }
        public void setWx_province(String wx_province){
            this.wx_province = wx_province;
        }
        public String getWx_province(){
            return this.wx_province;
        }

        public void setTjloginid(int tjloginid){
            this.tjloginid = tjloginid;
        }
        public int getTjloginid(){
            return this.tjloginid;
        }
        public void setPassword(String password){
            this.password = password;
        }
        public String getPassword(){
            return this.password;
        }
        public void setTjdistributor_id(String tjdistributor_id){
            this.tjdistributor_id = tjdistributor_id;
        }
        public String getTjdistributor_id(){
            return this.tjdistributor_id;
        }
        public void setWallet(double wallet){
            this.wallet = wallet;
        }
        public double getWallet(){
            return this.wallet;
        }
        public void setLogins(String logins){
            this.logins = logins;
        }
        public String getLogins(){
            return this.logins;
        }
        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setCard_count(int card_count){
            this.card_count = card_count;
        }
        public int getCard_count(){
            return this.card_count;
        }
        public void setWx_sex(String wx_sex){
            this.wx_sex = wx_sex;
        }
        public String getWx_sex(){
            return this.wx_sex;
        }
        public void setSubscribe(int subscribe){
            this.subscribe = subscribe;
        }
        public int getSubscribe(){
            return this.subscribe;
        }
        public void setMp_name(String mp_name){
            this.mp_name = mp_name;
        }
        public String getMp_name(){
            return this.mp_name;
        }
        public void setPathimg(String pathimg){
            this.pathimg = pathimg;
        }
        public String getPathimg(){
            return this.pathimg;
        }
        public void setTry_count(int try_count){
            this.try_count = try_count;
        }
        public int getTry_count(){
            return this.try_count;
        }
        public void setOpenid(String openid){
            this.openid = openid;
        }
        public String getOpenid(){
            return this.openid;
        }
        public void setAgtype(int agtype){
            this.agtype = agtype;
        }
        public int getAgtype(){
            return this.agtype;
        }
        public void setWechatname(String wechatname){
            this.wechatname = wechatname;
        }
        public String getWechatname(){
            return this.wechatname;
        }
        public void setIs_card(int is_card){
            this.is_card = is_card;
        }
        public int getIs_card(){
            return this.is_card;
        }
        public void setFollowdate(String followdate){
            this.followdate = followdate;
        }
        public String getFollowdate(){
            return this.followdate;
        }
        public void setProfit(double profit){
            this.profit = profit;
        }
        public double getProfit(){
            return this.profit;
        }
        public void setStatus(int status){
            this.status = status;
        }
        public int getStatus(){
            return this.status;
        }
        public void setHeadimg(String headimg){
            this.headimg = headimg;
        }
        public String getHeadimg(){
            return this.headimg;
        }
        public void setDistributor_id(String distributor_id){
            this.distributor_id = distributor_id;
        }
        public String getDistributor_id(){
            return this.distributor_id;
        }
        public void setAgent(String agent){
            this.agent = agent;
        }
        public String getAgent(){
            return this.agent;
        }
        public void setIs_show(int is_show){
            this.is_show = is_show;
        }
        public int getIs_show(){
            return this.is_show;
        }
        public void setFlag(int flag){
            this.flag = flag;
        }
        public int getFlag(){
            return this.flag;
        }
        public void setUpdate_time(String update_time){
            this.update_time = update_time;
        }
        public String getUpdate_time(){
            return this.update_time;
        }
        public void setMp_code(String mp_code){
            this.mp_code = mp_code;
        }
        public String getMp_code(){
            return this.mp_code;
        }
        public void setSource(String source){
            this.source = source;
        }
        public String getSource(){
            return this.source;
        }
        public void setMp_desc(Object mp_desc){
            this.mp_desc = mp_desc;
        }
        public Object getMp_desc(){
            return this.mp_desc;
        }
        public void setNums(int nums){
            this.nums = nums;
        }
        public int getNums(){
            return this.nums;
        }
        public void setMp_company(String mp_company){
            this.mp_company = mp_company;
        }
        public String getMp_company(){
            return this.mp_company;
        }
        public void setShipagid(int shipagid){
            this.shipagid = shipagid;
        }
        public int getShipagid(){
            return this.shipagid;
        }
        public void setWx_city(String wx_city){
            this.wx_city = wx_city;
        }
        public String getWx_city(){
            return this.wx_city;
        }
        public void setWechatimg(String wechatimg){
            this.wechatimg = wechatimg;
        }
        public String getWechatimg(){
            return this.wechatimg;
        }
        public void setWechat(String wechat){
            this.wechat = wechat;
        }
        public String getWechat(){
            return this.wechat;
        }
        public void setWx_country(String wx_country){
            this.wx_country = wx_country;
        }
        public String getWx_country(){
            return this.wx_country;
        }
        public void setNewwallet(double newwallet){
            this.newwallet = newwallet;
        }
        public double getNewwallet(){
            return this.newwallet;
        }
        public void setTjloginmoney(String tjloginmoney){
            this.tjloginmoney = tjloginmoney;
        }
        public String getTjloginmoney(){
            return this.tjloginmoney;
        }
        public void setRongyun_token(String rongyun_token){
            this.rongyun_token = rongyun_token;
        }
        public String getRongyun_token(){
            return this.rongyun_token;
        }
    }
}
