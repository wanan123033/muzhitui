package com.nevermore.muzhitui.module.bean;

/**
 * Created by hehe on 2016/6/14.
 */
public class MyStock {


    /**
     * id : 100001
     * typeName : A级代理
     * cardCount : 100
     * state : 1
     */

    private int id;
    private String typeName;
    private int cardCount;
    private String state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
