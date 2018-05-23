package com.nevermore.muzhitui.module.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hehe on 2016/6/14.
 */
public class ProxyType {

    /**
     * phonetell : 8899009
     * name : 宋广才
     * state : 1
     * province : 广东
     * typeList : [{"amount":24000,"id":1,"price":80,"count":300,"name":"A级代理","rebate":6000},{"amount":20000,"id":2,"price":100,"count":200,"name":"B级代理","rebate":5000},{"amount":12000,"id":3,"price":120,"count":100,"name":"C级代理","rebate":3000}]
     * city : 深圳
     */

    private String phonetell;
    private String name;
    private String state;
    private String province;
    private String city;
    /**
     * amount : 24000.0
     * id : 1
     * price : 80.0
     * count : 300
     * name : A级代理
     * rebate : 6000.0
     */

    private List<TypeListBean> typeList;

    public String getPhonetell() {
        return phonetell;
    }

    public void setPhonetell(String phonetell) {
        this.phonetell = phonetell;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public List<TypeListBean> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<TypeListBean> typeList) {
        this.typeList = typeList;
    }

    public static class TypeListBean implements Serializable {
        private double amount;
        private int id;
        private double price;
        private int count;
        private String name;
        private double rebate;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getRebate() {
            return rebate;
        }

        public void setRebate(double rebate) {
            this.rebate = rebate;
        }
    }
}
