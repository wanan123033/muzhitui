package com.nevermore.muzhitui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.Code;
import com.nevermore.muzhitui.module.bean.Register;
import com.nevermore.muzhitui.module.network.WorkService;

import base.BaseActivityTwoV;
import base.util.CountDownTimerUtils;
import base.util.VerificationUtil;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Simone on 2016/12/28.
 */

public class ResetPasswordActivity extends BaseActivityTwoV {

    @BindView(R.id.etResetPwdPhone)
    EditText mEtResetPwdPhone;
    @BindView(R.id.btnResetPwdCode)
    TextView mBtnResetPwdCode;
    @BindView(R.id.etResetPwdCode)
    EditText mEtResetPwdCode;
    @BindView(R.id.etResetPwdNew)
    EditText mEtResetPwdNew;
    @BindView(R.id.btnResetPwdEnsure)
    TextView mBtnResetPwdEnsure;
    private LoadingAlertDialog mLoadingAlertDialog;
    @Override
    public void init() {
        setMyTitle("重置密码");
        showBack();
        mLoadingAlertDialog = new LoadingAlertDialog(ResetPasswordActivity.this);

    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_reset_password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({ R.id.btnResetPwdCode, R.id.btnResetPwdEnsure})
    public void onClick(View view) {
        String phone=mEtResetPwdPhone.getText().toString().trim();
        String code = mEtResetPwdCode.getText().toString().trim();
        String password = mEtResetPwdNew.getText().toString().trim();
        switch (view.getId()) {


            case R.id.btnResetPwdCode:
                if (VerificationUtil.isMobile(phone)) {

                hintKbTwo();//关闭软键盘
                getPhoneCode(phone);
            }
                break;
            case R.id.btnResetPwdEnsure:
                if (!VerificationUtil.isMobile(phone)) {
                    showTest("请输入正确的手机号");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    showTest("请输入密码");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    showTest("请输入验证码");
                    return;
                }
                hintKbTwo();//关闭软键盘
                ResetPhonePassword(phone, password, code);

                break;
        }
    }
    private void getPhoneCode(String phone) {
        mLoadingAlertDialog.show();
        Subscription sbGetPhoneCode = wrapObserverWithHttp(WorkService.getWorkService().getPhoneCode(2, phone)).subscribe(new Subscriber<Code>() {
            @Override
            public void onCompleted() {

                mLoadingAlertDialog.dismiss();

            }

            @Override
            public void onError(Throwable e) {
                mLoadingAlertDialog.dismiss();
                showTest("服务器连接失败");
                e.printStackTrace();
            }

            @Override
            public void onNext(Code code) {
                if (code.getState().equals("1")) {
                    showTest("验证码发送成功");
                    CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(mBtnResetPwdCode, 60000, 1000);
                    mCountDownTimerUtils.start();
                }else{
                    showTest(code.getMsg()+"  状态码："+code.getState());
                }


            }
        });
        addSubscription(sbGetPhoneCode);

    }
    private void ResetPhonePassword(final String phone, String password, String code) {
        mLoadingAlertDialog.show();
        Subscription sbGetPhoneCode = wrapObserverWithHttp(WorkService.getWorkService().ResetPhonePassword(phone, password, code)).subscribe(new Subscriber<Register>() {
            @Override
            public void onCompleted() {

                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                mLoadingAlertDialog.dismiss();
                showTest("服务器连接失败");
                e.printStackTrace();
            }

            @Override
            public void onNext(Register register) {
                if (register != null) {
                    if (register.getState().equals("1")) {

                        showTest("重置成功");

                        finish();
                    }
                    showTest(register.getMsg());
                }


            }
        });
        addSubscription(sbGetPhoneCode);

    }
}
