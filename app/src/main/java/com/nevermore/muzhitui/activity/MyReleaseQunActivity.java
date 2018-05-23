package com.nevermore.muzhitui.activity;

import android.support.v7.widget.RecyclerView;

import com.nevermore.muzhitui.R;

import base.BaseActivityTwoV;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;

/**
 * Created by Administrator on 2017/9/14.
 * 我发布的群列表  我的发布
 */

public class MyReleaseQunActivity extends BaseActivityTwoV {
    private CommonAdapter mAdapter;
    @Override
    public void init() {
        setMyTitle("我的发布");

        mAdapter = new CommonAdapter(this,R.layout.item_my_release,null) {
            @Override
            public void convert(ViewHolder holder, Object o) {

            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            }
        };
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_my_release;
    }
}
