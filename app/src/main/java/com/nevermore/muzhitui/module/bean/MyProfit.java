package com.nevermore.muzhitui.module.bean;

import java.util.List;

/**
 * Created by hehe on 2016/6/2.
 */
public class MyProfit {




    private String state;
    /**
     * date : 2016/03/28
     * loginArray : [{"login_id":100001,"id":4,"out_login_id":100050,"desc":"周建云 购买了年费会员,您获得收益36.0元","trade_date":"2016-03-28 20:43:56","money":36,"type":1},{"login_id":100001,"id":5,"out_login_id":100050,"desc":"周建云 购买了年费会员,您获得收益36.0元","trade_date":"2016-03-28 20:44:25","money":36,"type":1},{"login_id":100001,"id":6,"out_login_id":100050,"desc":"周建云 赞赏了\"\u201c爸爸，你说我长大后会有出息吗？\u201d（火爆全国了)\"文章,您获得收益9.9元","trade_date":"2016-03-28 23:31:49","money":9.9,"type":2}]
     */

    private List<ProMapBean> proMap;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<ProMapBean> getProMap() {
        return proMap;
    }

    public void setProMap(List<ProMapBean> proMap) {
        this.proMap = proMap;
    }

    private int allPages;

    public int getAllPages() {
        return allPages;
    }

    public void setAllPages(int allPages) {
        this.allPages = allPages;
    }

    public static class ProMapBean {
        private String date;
        /**
         * login_id : 100001
         * id : 4
         * out_login_id : 100050
         * desc : 周建云 购买了年费会员,您获得收益36.0元
         * trade_date : 2016-03-28 20:43:56
         * money : 36
         * type : 1
         */

        private List<LoginArrayBean> loginArray;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<LoginArrayBean> getLoginArray() {
            return loginArray;
        }

        public void setLoginArray(List<LoginArrayBean> loginArray) {
            this.loginArray = loginArray;
        }

        public static class LoginArrayBean {
            private int login_id;
            private int id;
            private int out_login_id;
            private String desc;
            private String trade_date;
            private float money;
            private int type;

            public int getLogin_id() {
                return login_id;
            }

            public void setLogin_id(int login_id) {
                this.login_id = login_id;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getOut_login_id() {
                return out_login_id;
            }

            public void setOut_login_id(int out_login_id) {
                this.out_login_id = out_login_id;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getTrade_date() {
                return trade_date;
            }

            public void setTrade_date(String trade_date) {
                this.trade_date = trade_date;
            }


            public float getMoney() {
                return money;
            }

            public void setMoney(float money) {
                this.money = money;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
