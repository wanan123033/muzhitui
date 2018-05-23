package com.nevermore.muzhitui.module.bean;

import java.io.Serializable;

/**
 * Created by Simone on 2017/1/16.
 */

public class FindUserInfoById implements Serializable{
    /**
     * state : 1
     * login : {"phone":"15112673244","joindate":null,"phonetell":null,"is_vip":0,"unionid":"orIR2v2ypM7j3oPdPtowBn6ha2JA","mp_link":null,"wx_province":"辽宁","tjloginid":100001,"password":null,"tjdistributor_id":null,"wallet":0,"logins":null,"id":100014,"card_count":0,"wx_sex":2,"subscribe":1,"mp_name":"秋","pathimg":"upload/100014.png","try_count":3,"openid":"onxrevtZCLBrrPPqjLLUJEvSQre8","agtype":3,"wechatname":null,"is_card":0,"followdate":"2016-03-26 20:38:08","profit":0,"status":null,"headimg":"http://wx.qlogo.cn/mmopen/giaQibFlLQKPwOyOzu5vZic9u9Qf1re7j2JRxF6HJKXhWmChvlJG2GNymK0ByPweSgcXPwXaicJZ0djDUs1jCiblqvqyWWicDpicOTJ/0","distributor_id":null,"agent":"未加入会员","user_name":null,"is_show":0,"flag":0,"update_time":null,"mp_code":null,"source":null,"user_phone":null,"mp_desc":null,"nums":0,"mp_company":null,"shipagid":4,"wx_city":"大连","wechatimg":null,"wechat":null,"wx_country":"中国","newwallet":0,"tjloginmoney":null,"rongyun_token":null}
     * msg : 由ID查找好友详细成功
     */

    private String state;
    private LoginBean login;
    private String msg;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LoginBean getLogin() {
        return login;
    }

    public void setLogin(LoginBean login) {
        this.login = login;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class LoginBean {
        /**
         * phone : 15112673244
         * joindate : null
         * phonetell : null
         * is_vip : 0
         * unionid : orIR2v2ypM7j3oPdPtowBn6ha2JA
         * mp_link : null
         * wx_province : 辽宁
         * tjloginid : 100001
         * password : null
         * tjdistributor_id : null
         * wallet : 0.0
         * logins : null
         * id : 100014
         * card_count : 0
         * wx_sex : 2
         * subscribe : 1
         * mp_name : 秋
         * pathimg : upload/100014.png
         * try_count : 3
         * openid : onxrevtZCLBrrPPqjLLUJEvSQre8
         * agtype : 3
         * wechatname : null
         * is_card : 0
         * followdate : 2016-03-26 20:38:08
         * profit : 0.0
         * status : null
         * headimg : http://wx.qlogo.cn/mmopen/giaQibFlLQKPwOyOzu5vZic9u9Qf1re7j2JRxF6HJKXhWmChvlJG2GNymK0ByPweSgcXPwXaicJZ0djDUs1jCiblqvqyWWicDpicOTJ/0
         * distributor_id : null
         * agent : 未加入会员
         * user_name : null
         * is_show : 0
         * flag : 0
         * update_time : null
         * mp_code : null
         * source : null
         * user_phone : null
         * mp_desc : null
         * nums : 0
         * mp_company : null
         * shipagid : 4
         * wx_city : 大连
         * wechatimg : null
         * wechat : null
         * wx_country : 中国
         * newwallet : 0.0
         * tjloginmoney : null
         * rongyun_token : null
         */

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
        private int wx_sex;
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
        private String status;
        private String headimg;
        private String distributor_id;
        private String agent;
        private String user_name;
        private int is_show;
        private int flag;
        private String update_time;
        private String mp_code;
        private String source;
        private String user_phone;
        private String mp_desc;
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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
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

        public int getIs_vip() {
            return is_vip;
        }

        public void setIs_vip(int is_vip) {
            this.is_vip = is_vip;
        }

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }

        public String getMp_link() {
            return mp_link;
        }

        public void setMp_link(String mp_link) {
            this.mp_link = mp_link;
        }

        public String getWx_province() {
            return wx_province;
        }

        public void setWx_province(String wx_province) {
            this.wx_province = wx_province;
        }

        public int getTjloginid() {
            return tjloginid;
        }

        public void setTjloginid(int tjloginid) {
            this.tjloginid = tjloginid;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getTjdistributor_id() {
            return tjdistributor_id;
        }

        public void setTjdistributor_id(String tjdistributor_id) {
            this.tjdistributor_id = tjdistributor_id;
        }

        public double getWallet() {
            return wallet;
        }

        public void setWallet(double wallet) {
            this.wallet = wallet;
        }

        public String getLogins() {
            return logins;
        }

        public void setLogins(String logins) {
            this.logins = logins;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCard_count() {
            return card_count;
        }

        public void setCard_count(int card_count) {
            this.card_count = card_count;
        }

        public int getWx_sex() {
            return wx_sex;
        }

        public void setWx_sex(int wx_sex) {
            this.wx_sex = wx_sex;
        }

        public int getSubscribe() {
            return subscribe;
        }

        public void setSubscribe(int subscribe) {
            this.subscribe = subscribe;
        }

        public String getMp_name() {
            return mp_name;
        }

        public void setMp_name(String mp_name) {
            this.mp_name = mp_name;
        }

        public String getPathimg() {
            return pathimg;
        }

        public void setPathimg(String pathimg) {
            this.pathimg = pathimg;
        }

        public int getTry_count() {
            return try_count;
        }

        public void setTry_count(int try_count) {
            this.try_count = try_count;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public int getAgtype() {
            return agtype;
        }

        public void setAgtype(int agtype) {
            this.agtype = agtype;
        }

        public String getWechatname() {
            return wechatname;
        }

        public void setWechatname(String wechatname) {
            this.wechatname = wechatname;
        }

        public int getIs_card() {
            return is_card;
        }

        public void setIs_card(int is_card) {
            this.is_card = is_card;
        }

        public String getFollowdate() {
            return followdate;
        }

        public void setFollowdate(String followdate) {
            this.followdate = followdate;
        }

        public double getProfit() {
            return profit;
        }

        public void setProfit(double profit) {
            this.profit = profit;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public String getDistributor_id() {
            return distributor_id;
        }

        public void setDistributor_id(String distributor_id) {
            this.distributor_id = distributor_id;
        }

        public String getAgent() {
            return agent;
        }

        public void setAgent(String agent) {
            this.agent = agent;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public int getIs_show() {
            return is_show;
        }

        public void setIs_show(int is_show) {
            this.is_show = is_show;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getMp_code() {
            return mp_code;
        }

        public void setMp_code(String mp_code) {
            this.mp_code = mp_code;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getUser_phone() {
            return user_phone;
        }

        public void setUser_phone(String user_phone) {
            this.user_phone = user_phone;
        }

        public String getMp_desc() {
            return mp_desc;
        }

        public void setMp_desc(String mp_desc) {
            this.mp_desc = mp_desc;
        }

        public int getNums() {
            return nums;
        }

        public void setNums(int nums) {
            this.nums = nums;
        }

        public String getMp_company() {
            return mp_company;
        }

        public void setMp_company(String mp_company) {
            this.mp_company = mp_company;
        }

        public int getShipagid() {
            return shipagid;
        }

        public void setShipagid(int shipagid) {
            this.shipagid = shipagid;
        }

        public String getWx_city() {
            return wx_city;
        }

        public void setWx_city(String wx_city) {
            this.wx_city = wx_city;
        }

        public String getWechatimg() {
            return wechatimg;
        }

        public void setWechatimg(String wechatimg) {
            this.wechatimg = wechatimg;
        }

        public String getWechat() {
            return wechat;
        }

        public void setWechat(String wechat) {
            this.wechat = wechat;
        }

        public String getWx_country() {
            return wx_country;
        }

        public void setWx_country(String wx_country) {
            this.wx_country = wx_country;
        }

        public double getNewwallet() {
            return newwallet;
        }

        public void setNewwallet(double newwallet) {
            this.newwallet = newwallet;
        }

        public String getTjloginmoney() {
            return tjloginmoney;
        }

        public void setTjloginmoney(String tjloginmoney) {
            this.tjloginmoney = tjloginmoney;
        }

        public String getRongyun_token() {
            return rongyun_token;
        }

        public void setRongyun_token(String rongyun_token) {
            this.rongyun_token = rongyun_token;
        }

        @Override
        public String toString() {
            return "LoginBean{" +
                    "phone='" + phone + '\'' +
                    ", joindate='" + joindate + '\'' +
                    ", phonetell='" + phonetell + '\'' +
                    ", is_vip=" + is_vip +
                    ", unionid='" + unionid + '\'' +
                    ", mp_link='" + mp_link + '\'' +
                    ", wx_province='" + wx_province + '\'' +
                    ", tjloginid=" + tjloginid +
                    ", password='" + password + '\'' +
                    ", tjdistributor_id='" + tjdistributor_id + '\'' +
                    ", wallet=" + wallet +
                    ", logins='" + logins + '\'' +
                    ", id=" + id +
                    ", card_count=" + card_count +
                    ", wx_sex=" + wx_sex +
                    ", subscribe=" + subscribe +
                    ", mp_name='" + mp_name + '\'' +
                    ", pathimg='" + pathimg + '\'' +
                    ", try_count=" + try_count +
                    ", openid='" + openid + '\'' +
                    ", agtype=" + agtype +
                    ", wechatname='" + wechatname + '\'' +
                    ", is_card=" + is_card +
                    ", followdate='" + followdate + '\'' +
                    ", profit=" + profit +
                    ", status='" + status + '\'' +
                    ", headimg='" + headimg + '\'' +
                    ", distributor_id='" + distributor_id + '\'' +
                    ", agent='" + agent + '\'' +
                    ", user_name='" + user_name + '\'' +
                    ", is_show=" + is_show +
                    ", flag=" + flag +
                    ", update_time='" + update_time + '\'' +
                    ", mp_code='" + mp_code + '\'' +
                    ", source='" + source + '\'' +
                    ", user_phone='" + user_phone + '\'' +
                    ", mp_desc='" + mp_desc + '\'' +
                    ", nums=" + nums +
                    ", mp_company='" + mp_company + '\'' +
                    ", shipagid=" + shipagid +
                    ", wx_city='" + wx_city + '\'' +
                    ", wechatimg='" + wechatimg + '\'' +
                    ", wechat='" + wechat + '\'' +
                    ", wx_country='" + wx_country + '\'' +
                    ", newwallet=" + newwallet +
                    ", tjloginmoney='" + tjloginmoney + '\'' +
                    ", rongyun_token='" + rongyun_token + '\'' +
                    '}';
        }
    }
}
