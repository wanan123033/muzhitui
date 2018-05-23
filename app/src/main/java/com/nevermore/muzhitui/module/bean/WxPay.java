package com.nevermore.muzhitui.module.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hehe on 2016/7/5.
 */
public class WxPay {


    /**
     * state : 1
     * signParams : {"appid":"wxd1d6178de8a22bf2","noncestr":"dffac38df13c3a801f1b8994f9303bcc","package":"Sign=WXPay","partnerid":"1349689101","prepayid":"wx20160705101656341282eb790505162805","sign":"E87B4F66FDBD40D7B2C14656BDEF98CE","timestamp":"1467685016"}
     */

    private int state;
    /**
     * appid : wxd1d6178de8a22bf2
     * noncestr : dffac38df13c3a801f1b8994f9303bcc
     * package : Sign=WXPay
     * partnerid : 1349689101
     * prepayid : wx20160705101656341282eb790505162805
     * sign : E87B4F66FDBD40D7B2C14656BDEF98CE
     * timestamp : 1467685016
     */

    private SignParamsBean signParams;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public SignParamsBean getSignParams() {
        return signParams;
    }

    public void setSignParams(SignParamsBean signParams) {
        this.signParams = signParams;
    }

    public static class SignParamsBean {
        private String appid;
        private String noncestr;
        @SerializedName("package")
        private String packageX;
        private String partnerid;
        private String prepayid;
        private String sign;
        private String timestamp;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "SignParamsBean{" +
                    "appid='" + appid + '\'' +
                    ", noncestr='" + noncestr + '\'' +
                    ", packageX='" + packageX + '\'' +
                    ", partnerid='" + partnerid + '\'' +
                    ", prepayid='" + prepayid + '\'' +
                    ", sign='" + sign + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "WxPay{" +
                "state=" + state +
                ", signParams=" + signParams.toString() +
                '}';
    }
}
