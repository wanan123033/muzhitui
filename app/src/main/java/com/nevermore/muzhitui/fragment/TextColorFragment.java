package com.nevermore.muzhitui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.nevermore.muzhitui.FontStyleInterface;
import com.nevermore.muzhitui.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import base.BaseFragment;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
import base.recycler.recyclerview.OnItemClickListener;
import butterknife.BindView;

public class TextColorFragment extends BaseFragment {


    List<String> mLtObject = new ArrayList<>();
    @BindView(R.id.list)
    RecyclerView mList;
    private CommonAdapter mAdapter;
    String[] strColor = {"#000000", "#ffffff", "#545454", "#a8a8a8", "#3fe3c5", "#00ac97", "#009a7b", "#0c9564", "#7dc143", "#b8dc88", "#f8e100", "#dbb40a", "#b18302", "#a2711f", "#cc7020", "#a3701f", "#cc6f21", "#f97f2c", "#fc552b", "#ee332a", "#f48ebd", "#f075ab", "#fd43a8", "#ca28af", "#9521c0", "#7748f6", "#6465fe", "#2f47b1", "#0161b8", "#017cbf", "#69adde"};
    private FontStyleInterface mAdMakeAcitivity;

    @Override
    public int createSuccessView() {
        return R.layout.fragment_color_list;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdMakeAcitivity = (FontStyleInterface) getActivity();
        mLtObject = Arrays.asList(strColor);
        mAdapter = new CommonAdapter<String>(getActivity(), R.layout.rvitem_textcolor, mLtObject) {
            @Override
            public void convert(ViewHolder holder, String o) {
                holder.setBackgroundColor(R.id.view, Color.parseColor(o));
            }

        };
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                mAdMakeAcitivity.setFontColor((String) o);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                return false;
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mList.setLayoutManager(linearLayoutManager);
        mList.setAdapter(mAdapter);
    }


}
