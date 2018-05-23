package com.nevermore.muzhitui.module.bean;

import java.util.List;

/**
 * Created by hehe on 2016/6/2.
 */
public class LevelMember {


    /**
     * loginList : [{"joindate":"2015-12-24 00:00:00","phonetell":null,"is_vip":0,"mp_link":null,"tjloginid":100004,"logins":null,"wallet":0,"id":100007,"card_count":0,"subscribe":null,"mp_name":null,"pathimg":"upload/100007.png","try_count":10,"agtype":1,"openid":"oowmqwcvzk7s2bF_v7PPQZwh3wAg","wechatname":"谢其原","followdate":null,"headimg":"","distributor_id":3,"agent":"年费会员","sem_page":null,"is_show":0,"flag":1,"mp_code":null,"source":"薇天使","mp_desc":null,"nums":1,"mp_company":null,"shipagid":4,"wechatimg":"http://wx.qlogo.cn/mmopen/VAiahRr71CDfkoWrIb1iaFTicg7cATMOpaQ2j6LAQLKZibsUjibib7bXLVPx34Gd28OwcH2PvZhOKGrIlX15cwfewfkvG7XQgEc14u/0","wechat":null,"newwallet":0,"tjloginmoney":null},{"joindate":"2015-12-24 00:00:00","phonetell":null,"is_vip":1,"mp_link":null,"tjloginid":100004,"logins":10,"wallet":0,"id":100005,"card_count":0,"subscribe":null,"mp_name":null,"pathimg":"upload/100005.png","try_count":10,"agtype":3,"openid":"oowmqwdAoV9WDkHF8c3YpRMKjZGE","wechatname":"谢常锐","followdate":null,"headimg":null,"distributor_id":null,"agent":"未加入会员","sem_page":null,"is_show":0,"flag":1,"mp_code":null,"source":"薇天使","mp_desc":null,"nums":1,"mp_company":null,"shipagid":4,"wechatimg":"http://wx.qlogo.cn/mmopen/PiajxSqBRaEK4qJX24qkvOQVno5OzZz0EzXVaic6Kkialq8ODicwpibjiacuicySwAv1nrucVgNmjfSsXO99BGgylbc5A/0","wechat":null,"newwallet":0,"tjloginmoney":null},{"joindate":"2016-05-25 16:14:23","phonetell":"13249431031","is_vip":0,"mp_link":null,"tjloginid":100004,"logins":null,"wallet":0,"id":100009,"card_count":0,"subscribe":null,"mp_name":"大海","pathimg":"upload/100009.png","try_count":10,"agtype":3,"openid":"oowmqwQ0wmCFquiColUbl5wURWTw","wechatname":"看，上帝！","followdate":null,"headimg":null,"distributor_id":null,"agent":"未加入会员","sem_page":null,"is_show":0,"flag":1,"mp_code":"upload/2016-05-23/146398867501015.jpg","source":"薇天使","mp_desc":null,"nums":1,"mp_company":"深圳市人人秀网络科技有限公司","shipagid":4,"wechatimg":"http://wx.qlogo.cn/mmopen/kTQR5br8Lia4BAVx0GGpZBkpggRBYOMsjBrHxCALfq3G5PlXo2BD3eE8arUvIkXPsBdoepjgbHgnLhNyeRyEoFiaPHjNVnPI8f/0","wechat":null,"newwallet":0,"tjloginmoney":null}]
     * allPages : 1
     * pageCurrent : 1
     * state : 1
     */

    private int allPages;
    private int pageCurrent;
    private String state;
    /**
     * joindate : 2015-12-24 00:00:00
     * phonetell : null
     * is_vip : 0
     * mp_link : null
     * tjloginid : 100004
     * logins : null
     * wallet : 0
     * id : 100007
     * card_count : 0
     * subscribe : null
     * mp_name : null
     * pathimg : upload/100007.png
     * try_count : 10
     * agtype : 1
     * openid : oowmqwcvzk7s2bF_v7PPQZwh3wAg
     * wechatname : 谢其原
     * followdate : null
     * headimg :
     * distributor_id : 3
     * agent : 年费会员
     * sem_page : null
     * is_show : 0
     * flag : 1
     * mp_code : null
     * source : 薇天使
     * mp_desc : null
     * nums : 1
     * mp_company : null
     * shipagid : 4
     * wechatimg : http://wx.qlogo.cn/mmopen/VAiahRr71CDfkoWrIb1iaFTicg7cATMOpaQ2j6LAQLKZibsUjibib7bXLVPx34Gd28OwcH2PvZhOKGrIlX15cwfewfkvG7XQgEc14u/0
     * wechat : null
     * newwallet : 0
     * tjloginmoney : null
     */

    private List<LoginListBean> loginList;

    public int getAllPages() {
        return allPages;
    }

    public void setAllPages(int allPages) {
        this.allPages = allPages;
    }

    public int getPageCurrent() {
        return pageCurrent;
    }

    public void setPageCurrent(int pageCurrent) {
        this.pageCurrent = pageCurrent;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<LoginListBean> getLoginList() {
        return loginList;
    }

    public void setLoginList(List<LoginListBean> loginList) {
        this.loginList = loginList;
    }

    public static class LoginListBean {
        private String joindate;
        private Object phonetell;
        private int is_vip;
        private Object mp_link;
        private int tjloginid;
        private Object logins;
        private float wallet;
        private int id;
        private int card_count;
        private Object subscribe;
        private Object mp_name;
        private String pathimg;
        private int try_count;
        private int agtype;
        private String openid;
        private String wechatname;
        private Object followdate;
        private String headimg;
        private int distributor_id;
        private String agent;
        private Object sem_page;
        private int is_show;
        private int flag;
        private Object mp_code;
        private String source;
        private Object mp_desc;
        private int nums;
        private Object mp_company;
        private int shipagid;
        private String wechatimg;
        private Object wechat;
        private float newwallet;
        private Object tjloginmoney;

        public String getJoindate() {
            return joindate;
        }

        public void setJoindate(String joindate) {
            this.joindate = joindate;
        }

        public Object getPhonetell() {
            return phonetell;
        }

        public void setPhonetell(Object phonetell) {
            this.phonetell = phonetell;
        }

        public int getIs_vip() {
            return is_vip;
        }

        public void setIs_vip(int is_vip) {
            this.is_vip = is_vip;
        }

        public Object getMp_link() {
            return mp_link;
        }

        public void setMp_link(Object mp_link) {
            this.mp_link = mp_link;
        }

        public int getTjloginid() {
            return tjloginid;
        }

        public void setTjloginid(int tjloginid) {
            this.tjloginid = tjloginid;
        }

        public Object getLogins() {
            return logins;
        }

        public void setLogins(Object logins) {
            this.logins = logins;
        }

        public float getWallet() {
            return wallet;
        }

        public void setWallet(float wallet) {
            this.wallet = wallet;
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

        public Object getSubscribe() {
            return subscribe;
        }

        public void setSubscribe(Object subscribe) {
            this.subscribe = subscribe;
        }

        public Object getMp_name() {
            return mp_name;
        }

        public void setMp_name(Object mp_name) {
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

        public int getAgtype() {
            return agtype;
        }

        public void setAgtype(int agtype) {
            this.agtype = agtype;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getWechatname() {
            return wechatname;
        }

        public void setWechatname(String wechatname) {
            this.wechatname = wechatname;
        }

        public Object getFollowdate() {
            return followdate;
        }

        public void setFollowdate(Object followdate) {
            this.followdate = followdate;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public int getDistributor_id() {
            return distributor_id;
        }

        public void setDistributor_id(int distributor_id) {
            this.distributor_id = distributor_id;
        }

        public String getAgent() {
            return agent;
        }

        public void setAgent(String agent) {
            this.agent = agent;
        }

        public Object getSem_page() {
            return sem_page;
        }

        public void setSem_page(Object sem_page) {
            this.sem_page = sem_page;
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

        public Object getMp_code() {
            return mp_code;
        }

        public void setMp_code(Object mp_code) {
            this.mp_code = mp_code;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public Object getMp_desc() {
            return mp_desc;
        }

        public void setMp_desc(Object mp_desc) {
            this.mp_desc = mp_desc;
        }

        public int getNums() {
            return nums;
        }

        public void setNums(int nums) {
            this.nums = nums;
        }

        public Object getMp_company() {
            return mp_company;
        }

        public void setMp_company(Object mp_company) {
            this.mp_company = mp_company;
        }

        public int getShipagid() {
            return shipagid;
        }

        public void setShipagid(int shipagid) {
            this.shipagid = shipagid;
        }

        public String getWechatimg() {
            return wechatimg;
        }

        public void setWechatimg(String wechatimg) {
            this.wechatimg = wechatimg;
        }

        public Object getWechat() {
            return wechat;
        }

        public void setWechat(Object wechat) {
            this.wechat = wechat;
        }

        public float getNewwallet() {
            return newwallet;
        }

        public void setNewwallet(float newwallet) {
            this.newwallet = newwallet;
        }

        public Object getTjloginmoney() {
            return tjloginmoney;
        }

        public void setTjloginmoney(Object tjloginmoney) {
            this.tjloginmoney = tjloginmoney;
        }
    }
}
