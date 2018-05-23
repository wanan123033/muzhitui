package com.nevermore.muzhitui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.nevermore.muzhitui.fragment.SplashFragment;
import com.orhanobut.logger.Logger;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.UIUtils;
import butterknife.BindView;

public class SplashActivity extends BaseActivityTwoV {

    @BindView(R.id.vp)
    ViewPager mVp;
    @BindView(R.id.ivSplash)
    ImageView mIvSplash;

    @Override
    public void init() {
        hideActionBar();
//        boolean isFirstEnter = true;
        boolean isFirstEnter = (boolean) SPUtils.get(SPUtils.KEY_ISFIRSTENTER, true);
        if (isFirstEnter) {
            Logger.i("init()init()init()init()");
            mVp.setVisibility(View.VISIBLE);
            mVp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    return SplashFragment.newInstance(position);
                }

                @Override
                public int getCount() {
                    return 4;
                }
            });
        } else {
            mIvSplash.setVisibility(View.VISIBLE);
            UIUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    baseStartActivity(MainActivity.class);
                    finish();
                }
            }, 3 * 1000);
        }



    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_splash;
    }

    @Override
    public boolean isFullScreen() {
        return true;
    }
}
