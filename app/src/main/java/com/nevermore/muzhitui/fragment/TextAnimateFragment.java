package com.nevermore.muzhitui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nevermore.muzhitui.FontStyleInterface;
import com.nevermore.muzhitui.R;

import java.util.ArrayList;
import java.util.List;

import base.BaseFragment;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
import base.recycler.recyclerview.OnItemClickListener;
import butterknife.BindView;

public class TextAnimateFragment extends BaseFragment {


    List<Integer> mLtObject = new ArrayList<>();
    @BindView(R.id.list)
    RecyclerView mList;
    private CommonAdapter mAdapter;
    private ImageView mSelectedView;
    private FontStyleInterface mAdMakeAcitivity;
    public static final String KEY_FONTANIMATION = "FONTSIZE";
    private int mIndex;

    @Override
    public int createSuccessView() {
        return R.layout.fragment_color_list;
    }

    @Override
    public void getFragmentArguments(Bundle bundle) {
        super.getFragmentArguments(bundle);
        mIndex = bundle.getInt(KEY_FONTANIMATION);
    }

    public static TextAnimateFragment newInstance(int index) {
        TextAnimateFragment myFontAnimationFragment = new TextAnimateFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_FONTANIMATION, index);
        myFontAnimationFragment.setArguments(bundle);
        return myFontAnimationFragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdMakeAcitivity = (FontStyleInterface) getActivity();
        mLtObject.add(R.drawable.animation_enlarge);
        mLtObject.add(R.drawable.animation_fade);
        mLtObject.add(R.drawable.animation_narrow);
        mLtObject.add(R.drawable.animation_z_y_jitter);
        mLtObject.add(R.drawable.animation_u_d_jitter);
        mLtObject.add(R.drawable.ic_animation_none);

        mAdapter = new CommonAdapter<Integer>(getActivity(), R.layout.rvitem_textanimate, mLtObject) {
            @Override
            public void convert(ViewHolder holder, Integer o) {
                holder.setImageResource(R.id.ivAnimate, o);
                if (holder.getMyPosition() == mIndex) {
                    mSelectedView = holder.getView(R.id.ivAnimateSelect);
                    holder.setVisible(R.id.ivAnimateSelect, true);
                }
            }

        };
        mAdapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                if (mSelectedView == null) {
                    mSelectedView = viewHolder.getView(R.id.ivAnimateSelect);
                } else {
                    mSelectedView.setVisibility(View.GONE);
                    mSelectedView = viewHolder.getView(R.id.ivAnimateSelect);
                }
                mAdMakeAcitivity.setFontAnimation(position);
                viewHolder.setVisible(R.id.ivAnimateSelect, true);
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
