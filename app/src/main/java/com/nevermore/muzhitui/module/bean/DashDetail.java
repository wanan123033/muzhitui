package com.nevermore.muzhitui.module.bean;

import java.util.List;

/**
 * Created by hehe on 2016/7/14.
 */
public class DashDetail {



//      state : 1
//      list : [{"createtime":"2016-03-26 18:38:02","proposername":"James-Soong","id":1,"proposerid":100001,"weixin":"james-1999","updatetime":"2016-03-26 18:39:53","state":1,"money":"1","bank":null,"bill_no":"456456413","telphone":"13728775102"},{"createtime":"2016-04-16 20:21:17","proposername":"James-Soong","id":23,"proposerid":100001,"weixin":"james-1999","updatetime":"2016-04-16 20:21:50","state":1,"money":"100.0","bank":null,"bill_no":"146080927723655","telphone":"13728775102"},{"createtime":"2016-06-06 16:14:43","proposername":"James-Soong","id":204,"proposerid":100001,"weixin":"ss","updatetime":null,"state":0,"money":"445.0","bank":null,"bill_no":"14652008839593","telphone":"54455555"},{"createtime":"2016-06-06 16:17:06","proposername":"James-Soong","id":205,"proposerid":100001,"weixin":"ss","updatetime":null,"state":0,"money":"445.0","bank":null,"bill_no":"146520102678046","telphone":"54455555"},{"createtime":"2016-06-06 16:17:47","proposername":"James-Soong","id":206,"proposerid":100001,"weixin":"weixin","updatetime":null,"state":0,"money":"80.0","bank":null,"bill_no":"146520106769982","telphone":"18899000099"},{"createtime":"2016-06-06 16:18:57","proposername":"James-Soong","id":207,"proposerid":100001,"weixin":null,"updatetime":null,"state":0,"money":"80.0","bank":null,"bill_no":"146520113741068","telphone":"18899000099"},{"createtime":"2016-06-06 16:50:34","proposername":"James-Soong","id":208,"proposerid":100001,"weixin":null,"updatetime":null,"state":0,"money":"80.0","bank":null,"bill_no":"146520303478947","telphone":"18899000099"},{"createtime":"2016-06-06 16:50:52","proposername":"James-Soong","id":209,"proposerid":100001,"weixin":null,"updatetime":null,"state":0,"money":"80.0","bank":null,"bill_no":"146520305217311","telphone":"18899000099"},{"createtime":"2016-06-06 17:25:16","proposername":"James-Soong","id":210,"proposerid":100001,"weixin":null,"updatetime":null,"state":0,"money":"87.0","bank":null,"bill_no":"146520511630114","telphone":"1876739364"},{"createtime":"2016-06-06 17:25:33","proposername":"James-Soong","id":211,"proposerid":100001,"weixin":null,"updatetime":null,"state":0,"money":"87.0","bank":null,"bill_no":"146520513336538","telphone":"1876739364"},{"createtime":"2016-06-06 17:32:07","proposername":"James-Soong","id":212,"proposerid":100001,"weixin":null,"updatetime":null,"state":0,"money":"87.98","bank":null,"bill_no":"14652055279660","telphone":"18899000091"},{"createtime":"2016-06-06 17:32:53","proposername":"James-Soong","id":213,"proposerid":100001,"weixin":null,"updatetime":null,"state":0,"money":"89.0","bank":null,"bill_no":"146520557373435","telphone":"8899009"},{"createtime":"2016-06-06 17:32:58","proposername":"James-Soong","id":214,"proposerid":100001,"weixin":null,"updatetime":null,"state":0,"money":"89.0","bank":null,"bill_no":"146520557812881","telphone":"8899009"},{"createtime":"2016-06-06 17:33:02","proposername":"James-Soong","id":215,"proposerid":100001,"weixin":null,"updatetime":null,"state":0,"money":"89.0","bank":null,"bill_no":"146520558202663","telphone":"8899009"},{"createtime":"2016-06-06 17:33:03","proposername":"James-Soong","id":216,"proposerid":100001,"weixin":null,"updatetime":null,"state":0,"money":"89.0","bank":null,"bill_no":"146520558362157","telphone":"8899009"},{"createtime":"2016-06-06 17:33:07","proposername":"James-Soong","id":217,"proposerid":100001,"weixin":null,"updatetime":null,"state":0,"money":"89.0","bank":null,"bill_no":"146520558759919","telphone":"8899009"},{"createtime":"2016-06-06 17:33:10","proposername":"James-Soong","id":218,"proposerid":100001,"weixin":null,"updatetime":null,"state":0,"money":"89.0","bank":null,"bill_no":"146520559002230","telphone":"8899009"}]


    private String state;
    /**
     * createtime : 2016-03-26 18:38:02
     * proposername : James-Soong
     * id : 1
     * proposerid : 100001
     * weixin : james-1999
     * updatetime : 2016-03-26 18:39:53
     * state : 1
     * money : 1
     * bank : null
     * bill_no : 456456413
     * telphone : 13728775102
     */

    private List<ListBean> list;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String createtime;
        private String proposername;
        private int id;
        private int proposerid;
        private String weixin;
        private String updatetime;
        private int state;
        private String money;
        private Object bank;
        private String bill_no;
        private String telphone;

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getProposername() {
            return proposername;
        }

        public void setProposername(String proposername) {
            this.proposername = proposername;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getProposerid() {
            return proposerid;
        }

        public void setProposerid(int proposerid) {
            this.proposerid = proposerid;
        }

        public String getWeixin() {
            return weixin;
        }

        public void setWeixin(String weixin) {
            this.weixin = weixin;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public Object getBank() {
            return bank;
        }

        public void setBank(Object bank) {
            this.bank = bank;
        }

        public String getBill_no() {
            return bill_no;
        }

        public void setBill_no(String bill_no) {
            this.bill_no = bill_no;
        }

        public String getTelphone() {
            return telphone;
        }

        public void setTelphone(String telphone) {
            this.telphone = telphone;
        }
    }
}
