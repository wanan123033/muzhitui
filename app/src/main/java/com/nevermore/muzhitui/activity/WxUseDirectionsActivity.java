package com.nevermore.muzhitui.activity;

import com.nevermore.muzhitui.R;

import base.BaseActivityTwoV;

/**
 * Created by Administrator on 2017/11/9.
 */

public class WxUseDirectionsActivity extends BaseActivityTwoV{
    @Override
    public void init() {
        showBack();
        setMyTitle("微信加粉说明");
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_wx_use_directions;
    }
}
