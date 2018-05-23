package com.nevermore.muzhitui.module.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hehe on 2016/6/14.
 */
public class UpgradeProxy {


    /**
     * typeName : A级代理
     * amount : 24000.0
     * id : 100001
     * count : 300
     * state : 1
     * distributorId : 1
     * typeList : [{"amount":24000,"id":1,"price":80,"count":300,"name":"A级代理","rebate":6000},{"amount":20000,"id":2,"price":100,"count":200,"name":"B级代理","rebate":5000},{"amount":12000,"id":3,"price":120,"count":100,"name":"C级代理","rebate":3000}]
     */

    private String typeName;
    private int amount;
    private int id;
    private int count;
    private String state;
    private int distributorId;
    /**
     * amount : 24000.0
     * id : 1
     * price : 80.0
     * count : 300
     * name : A级代理
     * rebate : 6000.0
     */

    private List<TypeListBean> typeList;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }


    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(int distributorId) {
        this.distributorId = distributorId;
    }

    public List<TypeListBean> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<TypeListBean> typeList) {
        this.typeList = typeList;
    }

    public static class TypeListBean implements Serializable {
        private int amount;
        private int id;
        private int price;
        private int count;
        private String name;
        private int rebate;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public int getRebate() {
            return rebate;
        }

        public void setRebate(int rebate) {
            this.rebate = rebate;
        }
    }
}
