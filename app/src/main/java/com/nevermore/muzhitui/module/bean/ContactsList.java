package com.nevermore.muzhitui.module.bean;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Simone on 2016/12/19.
 */

public class ContactsList implements Serializable {
    /**
     * msg : 通讯录处理成功
     * phoneList : [{"flag":0,"id":10,"target_name":"拉长","target_phone":"1563242516"},{"flag":0,"id":9,"target_name":"老","target_phone":"13667737282"},{"flag":0,"id":8,"target_name":"老板","target_phone":"13728775102"},{"flag":0,"id":7,"target_name":"集训","target_phone":"15607464884"}]
     * state : 1
     */

    private String msg;
    private String state;
    private List<PhoneListBean> phoneList;
    private int allPages;
    private int pageCurrent;
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<PhoneListBean> getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(List<PhoneListBean> phoneList) {
        this.phoneList = phoneList;
    }

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

    public static class PhoneListBean {
        public PhoneListBean() {

        }

        /**
         * flag : 0
         * id : 10
         * target_name : 拉长
         * target_phone : 1563242516
         */

        private int flag;//状态 0 还未邀请 1 已加入的状态  2 已发过邀请
        private int id=0;
        private String target_name;
        private String target_phone;
        private Bitmap photo;
        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTarget_name() {
            return target_name;
        }

        public void setTarget_name(String target_name) {
            this.target_name = target_name;
        }

        public String getTarget_phone() {
            return target_phone;
        }

        public void setTarget_phone(String target_phone) {
            this.target_phone = target_phone;
        }

//        public Bitmap getPhoto() {
//            return photo;
//        }
//
//        public void setPhoto(Bitmap photo) {
//            this.photo = photo;
//        }

        public PhoneListBean(int flag, int id, String target_name, String target_phone, Bitmap photo) {
            this.flag = flag;
            this.id = id;
            this.target_name = target_name;
            this.target_phone = target_phone;
            this.photo = photo;
        }

        public PhoneListBean(int flag, String target_name, String target_phone) {
            this.flag = flag;
            this.target_name = target_name;
            this.target_phone = target_phone;
        }
    }
}
