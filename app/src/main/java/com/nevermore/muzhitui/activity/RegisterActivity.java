package com.nevermore.muzhitui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.event.AuthorizeEvent;
import com.nevermore.muzhitui.module.bean.Code;
import com.nevermore.muzhitui.module.bean.Register;
import com.nevermore.muzhitui.module.network.WorkService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.UIUtils;
import base.util.CountDownTimerUtils;
import base.util.LoginUtil;
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

public class RegisterActivity extends BaseActivityTwoV {

    @BindView(R.id.etRegisterPhone)
    EditText mEtRegisterPhone;
    @BindView(R.id.btnRegisterCode)
    TextView mBtnRegisterCode;
    @BindView(R.id.etRegisterCode)
    EditText mEtRegisterCode;
    @BindView(R.id.etRegisterPassword)
    EditText mEtRegisterPassword;
    @BindView(R.id.btnRegister)
    TextView mBtnRegister;
    private LoadingAlertDialog mLoadingAlertDialog;
    private android.app.AlertDialog alertDialog;
    @Override
    public void init() {
        setMyTitle("注册");
        showBack();
        mLoadingAlertDialog = new LoadingAlertDialog(RegisterActivity.this);

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventEMMessage(AuthorizeEvent loginEvent) {
        if (loginEvent.getState().equals("register")){
            showTest("微信授权成功");
            mLoadingAlertDialog.dismiss();
            EventBus.getDefault().post(new AuthorizeEvent("loginAndPhone"));
            finish();
        } else if (loginEvent.getState().equals("cancel")) {
            mLoadingAlertDialog.dismiss();
            showTest("取消微信授权");
        }else if (loginEvent.getState().equals("error")) {
            mLoadingAlertDialog.dismiss();
            showTest("微信授权失败");
        }

    }
    @Override
    public int createSuccessView() {
        return R.layout.activity_register;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);

    }

    @OnClick({ R.id.btnRegisterCode, R.id.btnRegister})
    public void onClick(View view) {
        String phone = mEtRegisterPhone.getText().toString().trim();
        String password = mEtRegisterPassword.getText().toString().trim();
        String code = mEtRegisterCode.getText().toString().trim();
        switch (view.getId()) {

            case R.id.btnRegisterCode:

                if (VerificationUtil.isMobile(phone)) {
                    //mBtnRegisterCode.setEnabled(false);

                    hintKbTwo();//关闭软键盘
                    getPhoneCode(phone);

                } else {
                    showTest("请输入正确的手机号");
                }

                break;
            case R.id.btnRegister:
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
                RegisterPhone(phone, password, code);

                break;
        }
    }

    private void getPhoneCode(String phone) {
        mLoadingAlertDialog.show();
        Subscription sbGetPhoneCode = wrapObserverWithHttp(WorkService.getWorkService().getPhoneCode(1, phone)).subscribe(new Subscriber<Code>() {
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
                    showTest(code.getMsg());
                    CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(mBtnRegisterCode, 60000, 1000);
                    mCountDownTimerUtils.start();
                }else{
                    showTest(code.getMsg()+"  状态码："+code.getState());
                }


            }
        });
        addSubscription(sbGetPhoneCode);

    }

    private void RegisterPhone(final String phone, final String password, String code) {
        mLoadingAlertDialog.show();
        Subscription sbGetPhoneCode = wrapObserverWithHttp(WorkService.getWorkService().RegisterPhone(phone, password, code)).subscribe(new Subscriber<Register>() {
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
                        Log.e("phone",phone+"\n"+register.getPassword());
                        if (!TextUtils.isEmpty(register.getPhone())) {
                            SPUtils.put(SPUtils.KEY_PHONE_NUMBER, register.getPhone());
                        }
                        if (!TextUtils.isEmpty(register.getPassword())){
                            SPUtils.put(SPUtils.KEY_PASSWORD, register.getPassword());
                        }

                        showTest("注册成功");

                        alertDialog = UIUtils.getAlertDialog(RegisterActivity.this, null, "绑定微信才会有更多的功能哦", "稍后再说", "去绑定", 0, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                alertDialog.dismiss();
                                finish();


                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                                mLoadingAlertDialog.show();
                                LoginUtil.getInstance().loginWX("register");
                            }
                        });
                        alertDialog.show();

                    }
                    showTest(register.getMsg());
                }


            }
        });
        addSubscription(sbGetPhoneCode);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);}
}
