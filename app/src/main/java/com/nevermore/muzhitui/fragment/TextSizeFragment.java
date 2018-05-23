package com.nevermore.muzhitui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nevermore.muzhitui.FontStyleInterface;
import com.nevermore.muzhitui.R;

import base.BaseFragment;
import butterknife.BindView;

public class TextSizeFragment extends BaseFragment {


    @BindView(R.id.sb)
    SeekBar mSb;
    private FontStyleInterface mAdMakeActivity;
    public static final String KEY_FONTSIZE = "FONTSIZE";
    private float mFontSize;
    private boolean mIsPagerEdit;
    @BindView(R.id.tvSmall)
    TextView mTvSmall;
    @BindView(R.id.tvBig)
    TextView mTvBig;

    public void setmIsPagerEdit(boolean mIsPagerEdit) {
        this.mIsPagerEdit = mIsPagerEdit;
    }

    @Override
    public int createSuccessView() {
        return R.layout.fragment_textsize;
    }

    @Override
    public void getFragmentArguments(Bundle bundle) {
        super.getFragmentArguments(bundle);
        mFontSize = bundle.getFloat(KEY_FONTSIZE);
    }

    public static TextSizeFragment newInstance(float fontSize) {
        TextSizeFragment myFontSizeFragment = new TextSizeFragment();
        Bundle bundle = new Bundle();
        bundle.putFloat(KEY_FONTSIZE, fontSize);
        myFontSizeFragment.setArguments(bundle);
        return myFontSizeFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdMakeActivity = (FontStyleInterface) getActivity();
        mSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAdMakeActivity.setFontSize(progress / 100f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        if (mFontSize != 0) {
            mSb.setProgress((int) (mFontSize * 100));
        }

        if (mIsPagerEdit) {
            mTvSmall.setTextColor(Color.parseColor("#ffffff"));
            mTvBig.setTextColor(Color.parseColor("#ffffff"));
        }
    }

}
