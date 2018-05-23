package com.nevermore.muzhitui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nevermore.muzhitui.PageLookActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.fragment.QunDemandFragment;
import com.nevermore.muzhitui.fragment.QunExchangeFragment;
import com.nevermore.muzhitui.fragment.QunHaveFragment;
import com.nevermore.muzhitui.module.bean.PageGPSBean;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.BaseFragment;
import base.SPUtils;
import base.view.DragView;
import base.view.VideoGpsView;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/9/14.
 * 群互换
 */

public class QunExchangeActivity extends BaseActivityTwoV{
    @BindView(R.id.frame_content)
    ViewPager frame_content;
    @BindView(R.id.tv_exchange)
    TextView tv_exchange;
    @BindView(R.id.tv_have_qun)
    TextView tv_have_qun;
    @BindView(R.id.tv_demand_qun)
    TextView tv_demand_qun;

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title2)
    TextView tv_title;
    @BindView(R.id.tv_common_my)
    TextView tvRight;
    @BindView(R.id.dragview)
    DragView dragview;

    private List<BaseFragment> frags = new ArrayList<>();
    private List<TextView> titles = new ArrayList<>();
    private int type;

    @Override
    public void init() {
        hideActionBar();
        showBack();
        frags.add(new QunExchangeFragment());
        frags.add(new QunHaveFragment());
        frags.add(new QunDemandFragment());
        titles.add(tv_exchange);
        titles.add(tv_have_qun);
        titles.add(tv_demand_qun);
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return frags.get(position);
            }

            @Override
            public int getCount() {
                return frags.size();
            }
        };
        frame_content.setAdapter(adapter);
        frame_content.setCurrentItem(0);
        setTitle(tv_exchange);
        setMyTitle("群互换");

        setRight(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MySelfQunActivity.class);
                intent.putExtra(MySelfQunActivity.TYPE,type);
                startActivity(intent);
            }
        });

        final VideoGpsView videoGpsView = new VideoGpsView(this,4);
        dragview.addDragView(videoGpsView, 50, 850, true, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoGpsView.onClick(v);
            }
        });
    }
    @Override
    public int createSuccessView() {
        return R.layout.activity_qun_exchange;
    }

    @OnClick({R.id.tv_exchange,R.id.tv_have_qun,R.id.tv_demand_qun})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_exchange:
                frame_content.setCurrentItem(0);
                setTitle(tv_exchange);
                setMyTitle("群互换");
                type = 0;
                break;
            case R.id.tv_have_qun:
                frame_content.setCurrentItem(1);
                setTitle(tv_have_qun);
                setMyTitle("我有群");
                type = 1;
                break;
            case R.id.tv_demand_qun:
                frame_content.setCurrentItem(2);
                setTitle(tv_demand_qun);
                setMyTitle("我要群");
                type = 2;
                break;
        }
    }

    public void setTitle(TextView itemView){
        for(TextView view : titles){
            if (itemView == view){
                view.setBackgroundColor(Color.WHITE);
                view.setTextColor(Color.BLACK);
            }else{
                view.setBackgroundColor(getResources().getColor(R.color.transparent));
                view.setTextColor(Color.WHITE);
            }
        }
    }

    @Override
    public void showBack() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                finish();
                onBackPressed();
            }
        });
    }

    @Override
    public void setMyTitle(String title) {
        tv_title.setText(title);
    }

    public void setRight(View.OnClickListener onClickListener) {
        tvRight.setOnClickListener(onClickListener);
    }
}
