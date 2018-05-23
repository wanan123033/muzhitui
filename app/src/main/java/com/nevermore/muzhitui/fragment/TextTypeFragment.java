package com.nevermore.muzhitui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nevermore.muzhitui.FontStyleInterface;
import com.nevermore.muzhitui.R;

import base.BaseFragment;
import base.MyRadioGroup;
import butterknife.BindView;

public class TextTypeFragment extends BaseFragment {


    @BindView(R.id.myG)
    MyRadioGroup mMyG;
    @BindView(R.id.rb1)
    RadioButton rb1;
    @BindView(R.id.rb2)
    RadioButton rb2;
    @BindView(R.id.rb3)
    RadioButton rb3;
    @BindView(R.id.rb4)
    RadioButton rb4;
    @BindView(R.id.rb5)
    RadioButton rb5;

    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;

    @BindView(R.id.tv4)
    TextView tv4;

    @BindView(R.id.tv5)
    TextView tv5;

    private FontStyleInterface mAdMakeActivity;
    public static final String KEY_FONTFAMILY = "FONTFAMILY";
    private String mFontFamily;
    private boolean mIsPagerEdit;

    @Override
    public int createSuccessView() {
        return R.layout.fragment_texttype;
    }


    public void setmIsPagerEdit(boolean mIsPagerEdit) {
        this.mIsPagerEdit = mIsPagerEdit;
    }

    @Override
    public void getFragmentArguments(Bundle bundle) {
        super.getFragmentArguments(bundle);
        mFontFamily = bundle.getString(KEY_FONTFAMILY);
    }

    public static TextTypeFragment newInstance(String fontFamily) {
        TextTypeFragment myFontFamilyFragment = new TextTypeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_FONTFAMILY, fontFamily);
        myFontFamilyFragment.setArguments(bundle);
        return myFontFamilyFragment;
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
                        mAdMakeActivity.setFontFamily("宋体");
                        break;
                    case R.id.rb2:
                        mAdMakeActivity.setFontFamily("仿宋");
                        break;
                    case R.id.rb3:
                        mAdMakeActivity.setFontFamily("黑体");
                        break;
                    case R.id.rb4:
                        mAdMakeActivity.setFontFamily("棣书");
                        break;
                    case R.id.rb5:
                        mAdMakeActivity.setFontFamily("粗黑");
                        break;
                }

            }
        });
        if (mIsPagerEdit) {
            tv1.setTextColor(Color.parseColor("#ffffff"));
            tv2.setTextColor(Color.parseColor("#ffffff"));
            tv3.setTextColor(Color.parseColor("#ffffff"));
            tv4.setTextColor(Color.parseColor("#ffffff"));
            tv5.setTextColor(Color.parseColor("#ffffff"));
        }
        if (!TextUtils.isEmpty(mFontFamily)) {
            switch (mFontFamily) {
                case "宋体":
                    rb1.setChecked(true);
                    break;
                case "仿宋":
                    rb2.setChecked(true);
                    break;
                case "黑体":
                    rb3.setChecked(true);
                    break;
                case "棣书":
                    rb4.setChecked(true);
                    break;
                case "粗黑":
                    rb5.setChecked(true);
                    break;

            }
        }
    }

}
