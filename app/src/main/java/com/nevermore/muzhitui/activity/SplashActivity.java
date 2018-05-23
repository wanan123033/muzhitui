package com.nevermore.muzhitui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.nevermore.muzhitui.LoginActivity;
import com.nevermore.muzhitui.MainActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.rongyun.MztRongContext;

import base.BaseActivityTwoV;
import butterknife.BindView;
import io.rong.imkit.RongIM;

/**
 * Created by AMing on 16/8/5.
 * Company RongCloud
 */
public class SplashActivity extends BaseActivityTwoV {

    @BindView(R.id.ivSplash)
    ImageView mIvSplash;
    private Context context;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ButterKnife.bind(this);

        Intent intent1 = new Intent("com.muzhitui.logcat.recevier");
        sendBroadcast(intent1);

    }

    @Override
    public void init() {
        hideActionBar();
        context = this;
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        String cacheToken = sp.getString("loginToken", "");

        if (!TextUtils.isEmpty(cacheToken)) {
            RongIM.connect(cacheToken, MztRongContext.getInstance().getConnectCallback());
            goToMain();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            }, 800);
        } else {
            goToLogin();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            }, 800);
        }
        mIvSplash.setVisibility(View.VISIBLE);
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_splash;
    }


    private void goToMain() {
        startActivity(new Intent(context, MainActivity.class));
        finish();
    }

    private void goToLogin() {
        startActivity(new Intent(context, LoginActivity.class));
        finish();
    }



}
