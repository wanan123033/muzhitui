package com.nevermore.muzhitui.activity;

import android.graphics.Color;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.VideoFilter;
import com.nevermore.muzhitui.fragment.DownVideoIngFragment;
import com.nevermore.muzhitui.fragment.DownVideoedFragment;
import com.nevermore.muzhitui.module.bean.VideoBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.BaseFragment;
import base.util.CacheUtil;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/13.
 */

public class MyDownVideoActivity extends BaseActivityTwoV{
    public static final String DOWN_VIDEOS = "videos";
    @BindView(R.id.tv_down)
    TextView tv_down;
    @BindView(R.id.tv_video)
    TextView tv_video;
    DownVideoedFragment dvedfragment;
    DownVideoIngFragment dvifargment;
    private List<TextView> titles = new ArrayList<>();
    @Override
    public void init() {
        showBack();
        setMyTitle("我的下载");
        EventBus.getDefault().register(this);
        titles.add(tv_down);
        titles.add(tv_video);
        List<VideoBean> beans = (List<VideoBean>) CacheUtil.getInstance().get(DOWN_VIDEOS);
        dvifargment = new DownVideoIngFragment();
        dvifargment.setDownVideos(beans);

        dvedfragment = new DownVideoedFragment();
        setTitle(tv_down);
        jumpFragment(dvifargment);

        getNum();
    }

    private void jumpFragment(BaseFragment dvifargment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_content,dvifargment);
        transaction.commit();
    }

    @OnClick({R.id.tv_down,R.id.tv_video})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_down:
                setTitle(view);
                jumpFragment(dvifargment);
                break;
            case R.id.tv_video:
                setTitle(view);
                jumpFragment(dvedfragment);
                break;
        }
    }





    @Override
    public int createSuccessView() {
        return R.layout.activity_my_down_video;
    }
    public void setTitle(View item){
        for(TextView view : titles){
            if (item == view){
                view.setBackgroundColor(Color.WHITE);
                view.setTextColor(Color.BLACK);
            }else{
                view.setBackgroundColor(getResources().getColor(R.color.transparent));
                view.setTextColor(Color.WHITE);
            }
        }
    }

    public void getNum(){
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        String[] files = appDir.list(new VideoFilter());
        if (files != null)
            tv_video.setText("已下载("+files.length+")");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventEMMessage(VideoBean bean){
        getNum();
    }
}
