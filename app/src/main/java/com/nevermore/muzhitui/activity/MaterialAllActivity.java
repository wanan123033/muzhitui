package com.nevermore.muzhitui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.fragment.MaterialFragment;

import base.BaseActivityTwoV;
import base.view.MyTabLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Simone on 2017/1/4.
 */

public class MaterialAllActivity extends BaseActivityTwoV {


    @BindView(R.id.vpAllMaterial)
    ViewPager mVpAllMaterial;
    @BindView(R.id.tblAllMaterial)
    MyTabLayout mTblAllMaterial;
    @BindView(R.id.materialBack)
    ImageView mMaterialBack;
    private String tabTiles[] = new String[]{"原创", "秒变", "最热"};

    @Override
    public int createSuccessView() {
        return R.layout.activity_all_material;
    }

    @Override
    public void init() {
        hideActionBar();
        mTblAllMaterial.setTabMode(TabLayout.MODE_FIXED);
        mVpAllMaterial.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Log.e("Position===:", position + "");
                switch (position) {
                    case 0:
                        return MaterialFragment.newInstance(2, 0);
                    case 1:
                        return MaterialFragment.newInstance(0, 0);
                    case 2:
                        return MaterialFragment.newInstance(1, 0);
                    default:
                        return MaterialFragment.newInstance(2, 0);
                }

               /* if (position == 0) {
                    position = 1;
                } else {
                    position = 0;
                }
                return MaterialFragment.newInstance(position);*/
            }

            @Override
            public int getCount() {
                return tabTiles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabTiles[position];
            }
        });
        mTblAllMaterial.setupWithViewPager(mVpAllMaterial);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @OnClick(R.id.materialBack)
    public void onClick() {
        finish();
    }
}

