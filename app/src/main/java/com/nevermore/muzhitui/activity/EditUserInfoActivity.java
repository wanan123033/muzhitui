package com.nevermore.muzhitui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.Code;
import com.nevermore.muzhitui.module.network.WorkService;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Simone on 2017/1/16.
 */

public class EditUserInfoActivity extends BaseActivityTwoV implements TextWatcher{
    @BindView(R.id.tvEditUICancel)
    TextView mTvEditUICancel;
    @BindView(R.id.tvEditUITitle)
    TextView mTvEditUITitle;
    @BindView(R.id.tvEditUIFinish)
    TextView mTvEditUIFinish;
    @BindView(R.id.tvEditUIName)
    EditText mTvEditUIName;
    @BindView(R.id.ivEditUIDelete)
    ImageView mIvEditUIDelete;
    @BindView(R.id.tvEditUIWXid)
    EditText mTvEditUIWXid;
    @BindView(R.id.ivEditUIWXidDelete)
    ImageView mIvEditUIWXidDelete;
    @BindView(R.id.tvEditUIPhone)
    EditText mTvEditUIPhone;
    @BindView(R.id.ivEditUIPhoneDelete)
    ImageView mIvEditUIPhoneDelete;

    @BindView(R.id.tvEditUIProduce)
    EditText mTvEditUIProduce;

    @BindView(R.id.rlEditUIName)
    RelativeLayout mRlEditUIName;
    @BindView(R.id.rlEditUIWXid)
    RelativeLayout mRlEditUIWXid;
    @BindView(R.id.rlEditUIPhone)
    RelativeLayout mRlEditUIPhone;
    @BindView(R.id.rlEditUIProduce)
    RelativeLayout mRlEditUIProduce;
    @BindView(R.id.tvEditUIFemale)
    TextView mTvEditUIFemale;
    @BindView(R.id.ivEditUIFemale)
    ImageView mIvEditUIFemale;
    @BindView(R.id.rlEditUIFemale)
    RelativeLayout mRlEditUIFemale;
    @BindView(R.id.tvEditUIMale)
    TextView mTvEditUIMale;
    @BindView(R.id.ivEditUIMale)
    ImageView mIvEditUIMale;
    @BindView(R.id.rlEditUIMale)
    RelativeLayout mRlEditUIMale;
    @BindView(R.id.llEditUISex)
    LinearLayout llEditUISex;
    @BindView(R.id.tvFontLength)
    TextView mTvFontLength;

    private LoadingAlertDialog mLoadingAlertDialog;
    private int flag;
    private String user_name, wechat,
            wx_sex,
            user_phone, mp_desc;
    private String isnot;

    @Override
    public void init() {
        hideActionBar();
        mLoadingAlertDialog = new LoadingAlertDialog(this);

        flag = getIntent().getIntExtra("flag", 0);
        Log.e("flag:", flag + "");
        if (flag == 2) {
            mTvEditUITitle.setText("名字");
            user_name = getIntent().getStringExtra("username");
            mRlEditUIName.setVisibility(View.VISIBLE);
            mTvEditUIName.setText(user_name);

        } else if (flag == 3) {
            mTvEditUITitle.setText("微信号");
            wechat = getIntent().getStringExtra("wxid");
            mRlEditUIWXid.setVisibility(View.VISIBLE);
            mTvEditUIWXid.setText(wechat);
        } else if (flag == 4) {
            mTvEditUITitle.setText("性別");
            wx_sex = getIntent().getStringExtra("sex");
            llEditUISex.setVisibility(View.VISIBLE);
            mTvEditUIFemale.setText("男");
            mTvEditUIMale.setText("女");
            if (wx_sex.equals("2")) {
                mIvEditUIFemale.setVisibility(View.GONE);
                mIvEditUIMale.setVisibility(View.VISIBLE);
            } else {
                mIvEditUIFemale.setVisibility(View.VISIBLE);
                mIvEditUIMale.setVisibility(View.GONE);
            }

        } else if (flag == 6) {
            mTvEditUITitle.setText("手机号");
            user_phone = getIntent().getStringExtra("phone");
            mRlEditUIPhone.setVisibility(View.VISIBLE);
            mTvEditUIPhone.setText(user_phone);
        } else if (flag == 7) {
            mTvEditUITitle.setText("我的介绍");
            mp_desc = getIntent().getStringExtra("mp_desc");
            mRlEditUIProduce.setVisibility(View.VISIBLE);
            mTvEditUIProduce.setText(mp_desc);
        }else if(flag == 10){
            mTvEditUITitle.setText("是否群主");
            isnot = getIntent().getStringExtra("isnot");
            llEditUISex.setVisibility(View.VISIBLE);
            mTvEditUIFemale.setText("否");
            mTvEditUIMale.setText("是");
            if (isnot.equals("1")) {
                mIvEditUIFemale.setVisibility(View.GONE);
                mIvEditUIMale.setVisibility(View.VISIBLE);
            } else {
                mIvEditUIFemale.setVisibility(View.VISIBLE);
                mIvEditUIMale.setVisibility(View.GONE);
            }
        }
        mTvEditUIProduce.addTextChangedListener(this);
        mTvEditUIProduce.setSelection(mTvEditUIProduce.getText().toString().trim().length());
        int length=   mTvEditUIProduce.getText().toString().length();
        String message="<font color='red'>"+(200-length)+"</font>"+"/200";
        mTvFontLength.setText(Html.fromHtml(message));
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_edit_userinfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tvEditUICancel, R.id.tvEditUIFinish, R.id.ivEditUIDelete, R.id.ivEditUIWXidDelete, R.id.ivEditUIPhoneDelete, R.id.rlEditUIFemale, R.id.rlEditUIMale})
    public void onClick(View view) {
        user_name = mTvEditUIName.getText().toString().trim();
        user_phone = mTvEditUIPhone.getText().toString().trim();
        wechat = mTvEditUIWXid.getText().toString().trim();
        mp_desc = mTvEditUIProduce.getText().toString().trim();
        switch (view.getId()) {

            case R.id.tvEditUICancel:
                finish();
                break;
            case R.id.tvEditUIFinish:
                if (flag == 10){
                    Intent intent = new Intent();
                    intent.putExtra("isnot",isnot);
                    setResult(268,intent);
                    finish();
                    break;
                }
                loadData();
                break;
            case R.id.ivEditUIDelete:
                mTvEditUIName.setText("");
                break;
            case R.id.ivEditUIWXidDelete:
                mTvEditUIWXid.setText("");
                break;
            case R.id.ivEditUIPhoneDelete:
                mTvEditUIPhone.setText("");
                break;
            case R.id.rlEditUIFemale:
                if(flag != 10) {
                    mIvEditUIFemale.setVisibility(View.VISIBLE);
                    mIvEditUIMale.setVisibility(View.GONE);
                    overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                    wx_sex = "1";
                }else if(flag == 10){
                    mIvEditUIFemale.setVisibility(View.VISIBLE);
                    mIvEditUIMale.setVisibility(View.GONE);
                    overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                    isnot = "0";
                }
                break;
            case R.id.rlEditUIMale:
                if(flag != 10) {
                    mIvEditUIFemale.setVisibility(View.GONE);
                    mIvEditUIMale.setVisibility(View.VISIBLE);
                    overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                    wx_sex = "2";
                }else if (flag == 10){
                    mIvEditUIFemale.setVisibility(View.GONE);
                    mIvEditUIMale.setVisibility(View.VISIBLE);
                    overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                    isnot = "1";
                }
                break;
        }
    }


    private void loadData() {

        mLoadingAlertDialog.show();


        Subscription sbGetCode = wrapObserverWithHttp(WorkService.getWorkService().updateUserInfo((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),flag, user_name, wechat, wx_sex, user_phone, mp_desc)).subscribe(new Subscriber<Code>() {
            @Override
            public void onCompleted() {
                mLoadingAlertDialog.dismiss();
                removeLoadingView();

            }

            @Override
            public void onError(Throwable e) {

                mLoadingAlertDialog.dismiss();
                removeLoadingView();
                showErrorView();
                e.printStackTrace();
            }

            @Override
            public void onNext(final Code code) {
                Log.e("TAG",code.toString());

                if (code.getState().equals("1")) {
                    showTest("修改成功");
                    if (flag == 2) {

                        Intent intent = new Intent();
                        intent.putExtra(SPUtils.KEY_WXNICKNAME, user_name);
                        setResult(Activity.RESULT_OK, intent);

                    } else if (flag == 3) {
                        Intent intent = new Intent();
                        intent.putExtra("wxid", wechat);
                        setResult(Activity.RESULT_OK, intent);
                    } else if (flag == 4) {
                        Intent intent = new Intent();
                        intent.putExtra("sex", wx_sex);
                        setResult(Activity.RESULT_OK, intent);
                    } else if (flag == 6) {
                        Intent intent = new Intent();
                        intent.putExtra("phone", user_phone);
                       // SPUtils.put(SPUtils.KEY_PHONE_NUMBER, user_phone);
                        setResult(Activity.RESULT_OK, intent);
                    } else if (flag == 7) {
                        Intent intent = new Intent();
                        intent.putExtra("mp_desc", mp_desc);
                        setResult(Activity.RESULT_OK, intent);
                    }
                    finish();
                }
                showTest(code.getMsg());


            }
        });
        addSubscription(sbGetCode);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

     int length=   mTvEditUIProduce.getText().toString().length();
        String message="<font color='red'>"+(200-length)+"</font>"+"/200";
        if (length<=200){
            mTvFontLength.setText(Html.fromHtml(message));
        }else{
            showTest("不能超过200个字");
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
