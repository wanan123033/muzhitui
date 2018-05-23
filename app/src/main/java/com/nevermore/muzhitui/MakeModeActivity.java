package com.nevermore.muzhitui;

import android.view.View;
import android.widget.FrameLayout;

import base.BaseActivityTwoV;
import base.util.CacheUtil;
import butterknife.BindView;
import butterknife.OnClick;

public class MakeModeActivity extends BaseActivityTwoV {


    @BindView(R.id.flytYemei)
    FrameLayout mFlytYemei;
    @BindView(R.id.flytYeJiao)
    FrameLayout mFlytYeJiao;
    @BindView(R.id.flytAd)
    FrameLayout mFlytAd;

    @Override
    public void init() {
        showBack();
        setMyTitle("选择类型");
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_make_mode;
    }

    @OnClick({R.id.flytYemei, R.id.flytYeJiao, R.id.flytAd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.flytYemei:    //页眉模板制作

                baseStartActivity(StartMakeActivity.class);
                break;
            case R.id.flytYeJiao:   //添加二维码
                baseStartActivity(QRMakeActivity.class);
                break;
            case R.id.flytAd:    //添加广告
                baseStartActivity(AdMakeActivity.class);
                break;
        }
    }
}
