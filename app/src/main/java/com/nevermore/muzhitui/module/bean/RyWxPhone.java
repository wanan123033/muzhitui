package com.nevermore.muzhitui.module.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
 */

public class RyWxPhone {
    private int allPages;
    private int pageCurrent;
    private int state;
    private List<WxPhone> phoneList;

    public int getPageCurrent() {
        return pageCurrent;
    }

    public void setPageCurrent(int pageCurrent) {
        this.pageCurrent = pageCurrent;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getAllPages() {
        return allPages;
    }

    public void setAllPages(int allPages) {
        this.allPages = allPages;
    }

    public List<WxPhone> getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(List<WxPhone> phoneList) {
        this.phoneList = phoneList;
    }

    public static class WxPhone{
        private int id;
        private String target_phone;
        private String province;
        private String city;
        private int flag = 1;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTarget_phone() {
            return target_phone;
        }

        public void setTarget_phone(String target_phone) {
            this.target_phone = target_phone;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }
    }
}
