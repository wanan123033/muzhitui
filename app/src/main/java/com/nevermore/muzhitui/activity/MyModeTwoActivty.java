package com.nevermore.muzhitui.activity;


import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nevermore.muzhitui.PageLookActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.fragment.MyWorksModeFragment;
import com.nevermore.muzhitui.module.bean.PageGPSBean;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.view.DragView;
import base.view.VideoGpsView;
import butterknife.BindView;
import rx.Subscriber;

public class MyModeTwoActivty extends BaseActivityTwoV {

    @BindView(R.id.flytMyMode)
    FrameLayout mFlytMyMode;
    @BindView(R.id.dragview)
    DragView dragview;


    @Override
    public void init() {
        showBack();
        setMyTitle("我的模板");
        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        MyWorksModeFragment myWorksModeFragment = new MyWorksModeFragment();
        fragmentTransaction.add(R.id.flytMyMode,myWorksModeFragment);

        fragmentTransaction.commit();

        final VideoGpsView videoGpsView = new VideoGpsView(this,9);
        dragview.addDragView(videoGpsView, 550, 850, true, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoGpsView.onClick(v);
            }
        });
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_my_mode_two_activty;
    }


}
