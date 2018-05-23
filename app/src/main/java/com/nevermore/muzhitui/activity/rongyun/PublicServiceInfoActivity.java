package com.nevermore.muzhitui.activity.rongyun;

import android.os.Bundle;
import android.util.Log;

import com.nevermore.muzhitui.R;

import base.BaseActivityTwoV;


public class PublicServiceInfoActivity extends BaseActivityTwoV {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("", "---------PublicServiceInfoActivity------------");


    }

    @Override
    public void init() {
        setMyTitle("公众号信息");

    }

    @Override
    public int createSuccessView() {
        return R.layout.pub_account_info;
    }

}
