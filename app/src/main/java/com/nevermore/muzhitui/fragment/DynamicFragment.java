package com.nevermore.muzhitui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nevermore.muzhitui.MainActivity;
import com.nevermore.muzhitui.R;

import java.util.ArrayList;
import java.util.List;

import base.BaseFragment;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/2.
 */

public class DynamicFragment extends BaseFragment {

    @BindView(R.id.tv_dynamic)
    TextView mDynamic;
    @BindView(R.id.tv_original)
    TextView mOriginal;
    @BindView(R.id.tv_make)
    TextView mMake;
    @BindView(R.id.frame_content)
    ViewPager frame_content;
    @BindView(R.id.tv_title)
    TextView tv_title;

    private List<TextView> titles = new ArrayList<>();
    private List<BaseFragment> frags = new ArrayList<>();
    @Override
    public int createSuccessView() {
        return R.layout.fragment_dynamic;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DynamicPage page = new DynamicPage();
        Bundle bundle = new Bundle();
        bundle.putInt(DynamicPage.DYNAMIC_PAGE_STATUS,0);
        page.setArguments(bundle);
        frags.add(page);
        frags.add(MaterialFragment.newInstance(2, 0));
        frags.add(MaterialFragment.newInstance(0, 0));
        titles.add(mDynamic);
        titles.add(mOriginal);
        titles.add(mMake);
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getFragmentManager()) {
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
        setTitle(mDynamic);
    }

    @OnClick({R.id.tv_dynamic,R.id.tv_original,R.id.tv_make})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_dynamic:  //动态
                frame_content.setCurrentItem(0);
                setTitle(view);
                tv_title.setText("动态");
                break;
            case R.id.tv_original: //原创
                frame_content.setCurrentItem(1);
                setTitle(view);
                tv_title.setText("原创");
                break;
            case R.id.tv_make: //秒变
                frame_content.setCurrentItem(2);
                setTitle(view);
                tv_title.setText("秒变");
                break;
        }
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
}
