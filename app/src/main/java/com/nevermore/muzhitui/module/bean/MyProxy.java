package com.nevermore.muzhitui.module.bean;

import java.util.List;

/**
 * Created by hehe on 2016/6/9.
 */
public class MyProxy {


    /**
     * profit : 0.0
     * oneDistributors : [{"profit":0,"distributorLevel":"A-2","nums":168,"wechatimg":"http://wx.qlogo.cn/mmopen/zXhk4QWoegPFCxZx4swARWHeN8MNet1GTsxhJ8jVNiaTNSVK6qAWpZQxVY8CibcmpNqDwRJGpg6ZW1ictv13mWbAtNK6iacArSicia/0","wechatname":"Peter"}]
     * state : 1
     * twoDistributors : [{"profit":0,"distributorLevel":"B-2","nums":1,"wechatimg":"http://wx.qlogo.cn/mmopen/ickBD79gnSZNLlm13TvwMrUeXbfpTMny0ylQbQkI4n5K2GHMhbmqDqn0icfpEPibxtgOiaibpCKNbcGia4aJTeguQc1ajX0ThFm4D1/0","wechatname":"he zhao 赵鹤"}]
     * hasTjdistributor : 0
     */

    private double profit;
    private String state;
    private int hasTjdistributor;
    private String tjdistributorName;
    /**
     * profit : 0.0
     * distributorLevel : A-2
     * nums : 168
     * wechatimg : http://wx.qlogo.cn/mmopen/zXhk4QWoegPFCxZx4swARWHeN8MNet1GTsxhJ8jVNiaTNSVK6qAWpZQxVY8CibcmpNqDwRJGpg6ZW1ictv13mWbAtNK6iacArSicia/0
     * wechatname : Peter
     */

    private List<OneDistributorsBean> oneDistributors;
    /**
     * profit : 0.0
     * distributorLevel : B-2
     * nums : 1
     * wechatimg : http://wx.qlogo.cn/mmopen/ickBD79gnSZNLlm13TvwMrUeXbfpTMny0ylQbQkI4n5K2GHMhbmqDqn0icfpEPibxtgOiaibpCKNbcGia4aJTeguQc1ajX0ThFm4D1/0
     * wechatname : he zhao 赵鹤
     */

    private List<TwoDistributorsBean> twoDistributors;

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getHasTjdistributor() {
        return hasTjdistributor;
    }

    public void setHasTjdistributor(int hasTjdistributor) {
        this.hasTjdistributor = hasTjdistributor;
    }

    public List<OneDistributorsBean> getOneDistributors() {
        return oneDistributors;
    }

    public void setOneDistributors(List<OneDistributorsBean> oneDistributors) {
        this.oneDistributors = oneDistributors;
    }

    public List<TwoDistributorsBean> getTwoDistributors() {
        return twoDistributors;
    }

    public void setTwoDistributors(List<TwoDistributorsBean> twoDistributors) {
        this.twoDistributors = twoDistributors;
    }

    public static class OneDistributorsBean {
        private double profit;
        private String distributorLevel;
        private int nums;
        private String wechatimg;
        private String wechatname;

        public double getProfit() {
            return profit;
        }

        public void setProfit(double profit) {
            this.profit = profit;
        }

        public String getDistributorLevel() {
            return distributorLevel;
        }

        public void setDistributorLevel(String distributorLevel) {
            this.distributorLevel = distributorLevel;
        }

        public int getNums() {
            return nums;
        }

        public void setNums(int nums) {
            this.nums = nums;
        }

        public String getWechatimg() {
            return wechatimg;
        }

        public void setWechatimg(String wechatimg) {
            this.wechatimg = wechatimg;
        }

        public String getWechatname() {
            return wechatname;
        }

        public void setWechatname(String wechatname) {
            this.wechatname = wechatname;
        }
    }

    public String getTjdistributorName() {
        return tjdistributorName;
    }

    public void setTjdistributorName(String tjdistributorName) {
        this.tjdistributorName = tjdistributorName;
    }

    public static class TwoDistributorsBean {
        private double profit;
        private String distributorLevel;
        private int nums;
        private String wechatimg;
        private String wechatname;

        public double getProfit() {
            return profit;
        }

        public void setProfit(double profit) {
            this.profit = profit;
        }

        public String getDistributorLevel() {
            return distributorLevel;
        }

        public void setDistributorLevel(String distributorLevel) {
            this.distributorLevel = distributorLevel;
        }

        public int getNums() {
            return nums;
        }

        public void setNums(int nums) {
            this.nums = nums;
        }

        public String getWechatimg() {
            return wechatimg;
        }

        public void setWechatimg(String wechatimg) {
            this.wechatimg = wechatimg;
        }

        public String getWechatname() {
            return wechatname;
        }

        public void setWechatname(String wechatname) {
            this.wechatname = wechatname;
        }
    }
}
