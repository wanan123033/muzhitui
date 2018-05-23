package com.nevermore.muzhitui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.nevermore.muzhitui.R;

import base.BaseActivityTwoV;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/7.
 */

public class ConnectWeActivity extends BaseActivityTwoV {
    @BindView(R.id.tvMyProxyPhone)
    TextView mTvMyProxyPhone;

    @Override
    public void init() {
        showBack();
        setMyTitle("联系我们");
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_connectwe;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tvMyProxyPhone)
    public void onClick() {
        Intent in2 = new Intent();
        in2.setAction(Intent.ACTION_CALL);
        in2.setData(Uri.parse("tel:"+mTvMyProxyPhone.getText().toString()));
        startActivity(in2);
    }
}
