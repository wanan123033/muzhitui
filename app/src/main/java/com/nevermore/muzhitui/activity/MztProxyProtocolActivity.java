package com.nevermore.muzhitui.activity;

import android.view.View;

import com.nevermore.muzhitui.BuyProxyActivity;
import com.nevermore.muzhitui.R;

import base.BaseActivityTwoV;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/2.
 */

public class MztProxyProtocolActivity extends BaseActivityTwoV{
    @Override
    public void init() {
        showBack();
        setMyTitle("代理收益制度");
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_mzt_proxy_protocol;
    }

    @OnClick(R.id.tvApply)
    public void onClick(View view){
        baseStartActivity(BuyProxyActivity.class);
    }
}