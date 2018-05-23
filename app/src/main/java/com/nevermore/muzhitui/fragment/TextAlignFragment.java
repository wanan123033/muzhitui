package com.nevermore.muzhitui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RadioButton;

import com.nevermore.muzhitui.FontStyleInterface;
import com.nevermore.muzhitui.R;

import base.BaseFragment;
import base.MyRadioGroup;
import butterknife.BindView;

public class TextAlignFragment extends BaseFragment {


    @BindView(R.id.myG)
    MyRadioGroup mMyG;
    @BindView(R.id.rb1)
    RadioButton rb1;
    @BindView(R.id.rb2)
    RadioButton rb2;
    @BindView(R.id.rb3)
    RadioButton rb3;

    private FontStyleInterface mAdMakeActivity;
    public static final String KEY_FONTFAMILY = "FONTFAMILY";
    private String mFontFamily;

    @Override
    public int createSuccessView() {
        return R.layout.fragment_textalign;
    }


    @Override
    public void getFragmentArguments(Bundle bundle) {
        super.getFragmentArguments(bundle);
    }

    public static TextAlignFragment newInstance(String fontFamily) {
        TextAlignFragment textAlignFragment = new TextAlignFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_FONTFAMILY, fontFamily);
        textAlignFragment.setArguments(bundle);
        return textAlignFragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdMakeActivity = (FontStyleInterface) getActivity();
        mMyG.setOnCheckedChangeListener(new MyRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MyRadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb1:
                        mAdMakeActivity.setAlignStyle(0);
                        break;
                    case R.id.rb2:
                        mAdMakeActivity.setAlignStyle(1);
                        break;
                    case R.id.rb3:
                        mAdMakeActivity.setAlignStyle(2);
                        break;
                }

            }
        });
    }

}
