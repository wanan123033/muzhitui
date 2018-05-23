package com.nevermore.muzhitui.activity;

import com.nevermore.muzhitui.R;

import base.BaseActivityTwoV;
import base.view.X5WebView;
import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/19.
 */

public class DownActivity extends BaseActivityTwoV{
    @BindView(R.id.web)
    X5WebView web;
    @Override
    public int createSuccessView() {
        return R.layout.down_yinyongbao;
    }

    @Override
    public void init() {

        web.loadUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.nevermore.muzhitui&from=message&isappinstalled=0#opened");
    }
}
