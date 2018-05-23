package com.nevermore.muzhitui.module.bean;



/**
 * Created by hehe on 2016/6/4.
 */
public class LoginInfo {
    /**
     * state : 1 1是表示请求成功
     * login : {"phone":"15607464884","joindate":null,"phonetell":"15112673242","is_vip":0,"unionid":null,"mp_link":null,"wx_province":"???","openid-app":null,"tjloginid":152285,"password":"e10adc3949ba59abbe56e057f20f883e","tjdistributor_id":null,"wallet":0,"logins":null,"id":153532,"card_count":0,"wx_sex":2,"subscribe":0,"mp_name":"酒店","pathimg":"upload/2017-03-13/153532.png","try_count":5,"openid":null,"agtype":3,"wechatname":"666","is_card":0,"followdate":null,"profit":0,"headimg":"upload/2017-03-13/148939693160482.png","distributor_id":null,"is_expire":0,"user_name":"测试一下","is_show":1,"flag":0,"update_time":"2017-01-04 16:41:11","mp_code":"upload/2017-02-28/148825353347571.jpg","source":null,"user_phone":"15607464884","mp_desc":"噜啦啦就觉得看看吧","nums":1,"mp_company":"aaa","shipagid":4,"wx_city":"???","wechatimg":null,"wechat":"哦看来你巴0","wx_country":"??","newwallet":0,"tjloginmoney":null,"rongyun_token":"5udMxWi8EBq4uv/2cqSnH/oQkAkYhI/Zk/fqwmlhrTvMdOuSwhV0U+fcFTGBHOhKsM2pqeFSiPgFyG19qmpO4g=="}
     * msg : 登录成功
     * "msg": "登录成功",
     "password": "e10adc3949ba59abbe56e057f20f883e",
     "phone": "15607464884",
     "state": "1"
     "hasWeixin": "0",
     */

    private String state;
    private LoginBean login;
    private String msg;
    private String hasWeixin;
    private String password;
    private String phone;
    //1 存在微信 已绑定，0不存在


    @Override
    public String toString() {
        return "LoginInfo{" +
                "state='" + state + '\'' +
                ", login=" + login +
                ", msg='" + msg + '\'' +
                ", hasWeixin='" + hasWeixin + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

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

    public String getHasWeixin() {
        return hasWeixin;
    }

    public void setHasWeixin(String hasWeixin) {
        this.hasWeixin = hasWeixin;
    }

    public static class LoginBean {
        /**
         * phone : 15607464884
         * joindate : null
         * phonetell : 15112673242
         * is_vip : 0
         * unionid : null
         * mp_link : null
         * wx_province : ???
         * openid-app : null
         * tjloginid : 152285
         * password : e10adc3949ba59abbe56e057f20f883e
         * tjdistributor_id : null
         * wallet : 0.0
         * logins : null
         * id : 153532
         * card_count : 0
         * wx_sex : 2
         * subscribe : 0
         * mp_name : 酒店
         * pathimg : upload/2017-03-13/153532.png
         * try_count : 5
         * openid : null
         * agtype : 3
         * wechatname : 666
         * is_card : 0
         * followdate : null
         * profit : 0.0
         * headimg : upload/2017-03-13/148939693160482.png
         * distributor_id : null
         * is_expire : 0
         * user_name : 测试一下
         * is_show : 1
         * flag : 0
         * update_time : 2017-01-04 16:41:11
         * mp_code : upload/2017-02-28/148825353347571.jpg
         * source : null
         * user_phone : 15607464884
         * mp_desc : 噜啦啦就觉得看看吧
         * nums : 1
         * mp_company : aaa
         * shipagid : 4
         * wx_city : ???
         * wechatimg : null
         * wechat : 哦看来你巴0
         * wx_country : ??
         * newwallet : 0.0
         * tjloginmoney : null
         * rongyun_token : 5udMxWi8EBq4uv/2cqSnH/oQkAkYhI/Zk/fqwmlhrTvMdOuSwhV0U+fcFTGBHOhKsM2pqeFSiPgFyG19qmpO4g==
         */

        private String phone;
        private Object joindate;
        private String phonetell;
        private int is_vip;
        private Object unionid;
        private Object mp_link;
        private String wx_province;

        private int tjloginid;
        private String password;
        private Object tjdistributor_id;
        private double wallet;
        private Object logins;
        private int id;
        private int card_count;
        private int wx_sex;
        private int subscribe;
        private String mp_name;
        private String pathimg;
        private int try_count;
        private Object openid;
        private int agtype;
        private String wechatname;
        private int is_card;
        private Object followdate;
        private double profit;
        private String headimg;
        private Object distributor_id;
        private int is_expire;
        private String user_name;
        private int is_show;
        private int flag;
        private String update_time;
        private String mp_code;
        private Object source;
        private String user_phone;
        private String mp_desc;
        private int nums;
        private String mp_company;
        private int shipagid;
        private String wx_city;
        private Object wechatimg;
        private String wechat;
        private String wx_country;
        private double newwallet;
        private Object tjloginmoney;
        private String rongyun_token;

        @Override
        public String toString() {
            return "LoginBean{" +
                    "phone='" + phone + '\'' +
                    ", joindate=" + joindate +
                    ", phonetell='" + phonetell + '\'' +
                    ", is_vip=" + is_vip +
                    ", unionid=" + unionid +
                    ", mp_link=" + mp_link +
                    ", wx_province='" + wx_province + '\'' +
                    ", tjloginid=" + tjloginid +
                    ", password='" + password + '\'' +
                    ", tjdistributor_id=" + tjdistributor_id +
                    ", wallet=" + wallet +
                    ", logins=" + logins +
                    ", id=" + id +
                    ", card_count=" + card_count +
                    ", wx_sex=" + wx_sex +
                    ", subscribe=" + subscribe +
                    ", mp_name='" + mp_name + '\'' +
                    ", pathimg='" + pathimg + '\'' +
                    ", try_count=" + try_count +
                    ", openid=" + openid +
                    ", agtype=" + agtype +
                    ", wechatname='" + wechatname + '\'' +
                    ", is_card=" + is_card +
                    ", followdate=" + followdate +
                    ", profit=" + profit +
                    ", headimg='" + headimg + '\'' +
                    ", distributor_id=" + distributor_id +
                    ", is_expire=" + is_expire +
                    ", user_name='" + user_name + '\'' +
                    ", is_show=" + is_show +
                    ", flag=" + flag +
                    ", update_time='" + update_time + '\'' +
                    ", mp_code='" + mp_code + '\'' +
                    ", source=" + source +
                    ", user_phone='" + user_phone + '\'' +
                    ", mp_desc='" + mp_desc + '\'' +
                    ", nums=" + nums +
                    ", mp_company='" + mp_company + '\'' +
                    ", shipagid=" + shipagid +
                    ", wx_city='" + wx_city + '\'' +
                    ", wechatimg=" + wechatimg +
                    ", wechat='" + wechat + '\'' +
                    ", wx_country='" + wx_country + '\'' +
                    ", newwallet=" + newwallet +
                    ", tjloginmoney=" + tjloginmoney +
                    ", rongyun_token='" + rongyun_token + '\'' +
                    '}';
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Object getJoindate() {
            return joindate;
        }

        public void setJoindate(Object joindate) {
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

        public Object getUnionid() {
            return unionid;
        }

        public void setUnionid(Object unionid) {
            this.unionid = unionid;
        }

        public Object getMp_link() {
            return mp_link;
        }

        public void setMp_link(Object mp_link) {
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

        public Object getTjdistributor_id() {
            return tjdistributor_id;
        }

        public void setTjdistributor_id(Object tjdistributor_id) {
            this.tjdistributor_id = tjdistributor_id;
        }

        public double getWallet() {
            return wallet;
        }

        public void setWallet(double wallet) {
            this.wallet = wallet;
        }

        public Object getLogins() {
            return logins;
        }

        public void setLogins(Object logins) {
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

        public Object getOpenid() {
            return openid;
        }

        public void setOpenid(Object openid) {
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

        public Object getFollowdate() {
            return followdate;
        }

        public void setFollowdate(Object followdate) {
            this.followdate = followdate;
        }

        public double getProfit() {
            return profit;
        }

        public void setProfit(double profit) {
            this.profit = profit;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public Object getDistributor_id() {
            return distributor_id;
        }

        public void setDistributor_id(Object distributor_id) {
            this.distributor_id = distributor_id;
        }

        public int getIs_expire() {
            return is_expire;
        }

        public void setIs_expire(int is_expire) {
            this.is_expire = is_expire;
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

        public Object getSource() {
            return source;
        }

        public void setSource(Object source) {
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

        public Object getWechatimg() {
            return wechatimg;
        }

        public void setWechatimg(Object wechatimg) {
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

        public Object getTjloginmoney() {
            return tjloginmoney;
        }

        public void setTjloginmoney(Object tjloginmoney) {
            this.tjloginmoney = tjloginmoney;
        }

        public String getRongyun_token() {
            return rongyun_token;
        }

        public void setRongyun_token(String rongyun_token) {
            this.rongyun_token = rongyun_token;
        }
    }
}
