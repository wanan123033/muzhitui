package com.nevermore.muzhitui.module.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/31.
 */

public class Order extends com.nevermore.muzhitui.module.BaseBean{
    public List<OrderPar> orderList;

    public static class OrderPar{
        public int loginId;
        public String headimg;
        public String username_out;
        public String user_name;
        public double price;
        public int friend_status;
        public int loginId_out;
    }
}
