package com.nevermore.muzhitui;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nevermore.muzhitui.activity.RegisterActivity;
import com.nevermore.muzhitui.activity.ResetPasswordActivity;
import com.nevermore.muzhitui.event.AuthorizeEvent;
import com.nevermore.muzhitui.event.DataRefreshEvent;
import com.nevermore.muzhitui.module.bean.GetToken;
import com.nevermore.muzhitui.module.bean.LoginInfo;
import com.nevermore.muzhitui.module.bean.MyFriends;
import com.nevermore.muzhitui.module.bean.MyLever;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nevermore.muzhitui.module.sqllite.DBManager;
import com.nevermore.muzhitui.module.sqllite.UserInfoRong;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.UIUtils;
import base.network.RetrofitUtil;
import base.util.LoginUtil;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;
import rx.Subscriber;
import rx.Subscription;

public class LoginActivity extends BaseActivityTwoV implements TextWatcher {

    @BindView(R.id.et_login_phone)
    EditText etLoginPhone;
    @BindView(R.id.et_login_password)
    EditText etLoginPassword;
    @BindView(R.id.tv_login_register)
    TextView tvLoginRegister;
    @BindView(R.id.tv_login_forgot_password)
    TextView tvLoginForgotPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.iv_login_wechat)
    Button ivLoginWechat;


    private LoadingAlertDialog mLoadingAlertDialog;
    private android.app.AlertDialog alertDialog;

    private boolean mIsflag;
    private DBManager mgr;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    public void init() {
        hideActionBar();
        mIsflag = false;
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        //初始化DBManager
        mgr = new DBManager(this);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        editor = sp.edit();
        etLoginPassword.addTextChangedListener(this);
        etLoginPhone.addTextChangedListener(this);
        btnLogin.setEnabled(false);
        btnLogin.setClickable(false);
    }

    @Override
    public int createSuccessView() {

        return R.layout.activity_login;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        //应用的最后一个Activity关闭时应释放DB
        mgr.closeDB();
        if (!mIsflag) {
            System.exit(0);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        String phone_number = (String) SPUtils.get(SPUtils.KEY_PHONE_NUMBER, "");
        etLoginPhone.setText(phone_number);
    }

    /**
     * @param view 登录
     */
    @OnClick({R.id.iv_login_wechat, R.id.btn_login, R.id.tv_login_register, R.id.tv_login_forgot_password})
    public void onClick(View view) {
        String phone = etLoginPhone.getText().toString().trim();
        String password = etLoginPassword.getText().toString().trim();
        switch (view.getId()) {
            case R.id.iv_login_wechat:
                mLoadingAlertDialog.show();
                LoginUtil.getInstance().loginWX("login");
                break;
            case R.id.btn_login:

                LoginPhone(phone, password);

                break;
            case R.id.tv_login_register:


                baseStartActivity(RegisterActivity.class);

                break;
            case R.id.tv_login_forgot_password:

                baseStartActivity(ResetPasswordActivity.class);

                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventEMMessage(AuthorizeEvent loginEvent) {
        if (loginEvent.getState().equals("login")) {
            mLoadingAlertDialog.dismiss();
            showTest("微信授权成功");
            login();
        } else if (loginEvent.getState().equals("loginAndPhone")) {
            mLoadingAlertDialog.dismiss();
            showTest("微信授权成功");
            appLoginAfterPhone();
        } else if (loginEvent.getState().equals("cancel")) {
            mLoadingAlertDialog.dismiss();
            showTest("取消微信授权登录");
        } else if (loginEvent.getState().equals("error")) {
            mLoadingAlertDialog.dismiss();
            showTest("微信授权登录失败");
        }

    }


    private void jiesu() {
        mIsflag = true;
        mLoadingAlertDialog.dismiss();//mainactivity死了这执行一行代码 为什么只执行到这里 主要是不在主线程
        MainActivity.mIsLogin = true;
        MobclickAgent.onProfileSignIn((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""));
        baseStartActivity(MainActivity.class);
        // EventBus.getDefault().post(new DataRefreshEvent());
        finish();
    }

    @Override
    public boolean isFullScreen() {
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
    }

    /**
     * 直接点击微信登录
     */
    private void login() {
        //country city privilege province language sex
        mLoadingAlertDialog.show();
        showTest("正在登录，请稍后...");
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().login((String) SPUtils.get(SPUtils.KEY_WXUNIONID, ""), (String) SPUtils.get(SPUtils.KEY_WXOPENID, ""), (String) SPUtils.get(SPUtils.KEY_WXNICKNAME, ""), (String) SPUtils.get(SPUtils.KEY_WXHEAD, ""), (String) SPUtils.get(SPUtils.KEY_COUNTRY, ""), (String) SPUtils.get(SPUtils.KEY_PROVINCE, ""), (String) SPUtils.get(SPUtils.KEY_CITY, ""), (String) SPUtils.get(SPUtils.KEY_SEX, ""), "1")).subscribe(new Subscriber<LoginInfo>() {
            @Override
            public void onCompleted() {
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mLoadingAlertDialog.dismiss();
                showTest("服务器连接失败");
            }

            @Override
            public void onNext(LoginInfo loginInfo) {
                if ("1".equals(loginInfo.getState())) {

                    showTest("微信登录成功");
                    setToken(loginInfo);
                } else {
                    showTest(loginInfo.getMsg());
                }

            }
        });
        addSubscription(sbMyAccount);
    }

    /**
     * 用户注册完成绑定完成后调用这个登录接口
     */
    private void appLoginAfterPhone() {//country city privilege province language sex
        showTest("正在登录，请稍后...");
        mLoadingAlertDialog.show();
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().appLoginAfterPhone((String) SPUtils.get(SPUtils.KEY_WXUNIONID, ""),
                (String) SPUtils.get(SPUtils.KEY_WXOPENID, ""), (String) SPUtils.get(SPUtils.KEY_WXNICKNAME, ""),
                (String) SPUtils.get(SPUtils.KEY_WXHEAD, ""), (String) SPUtils.get(SPUtils.KEY_COUNTRY, ""),
                (String) SPUtils.get(SPUtils.KEY_PROVINCE, ""), (String) SPUtils.get(SPUtils.KEY_CITY, ""),
                (String) SPUtils.get(SPUtils.KEY_SEX, ""), "1", (String) SPUtils.get(SPUtils.KEY_PHONE_NUMBER, ""), (String) SPUtils.get(SPUtils.KEY_PASSWORD, ""))).subscribe(new Subscriber<LoginInfo>() {
            @Override
            public void onCompleted() {
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mLoadingAlertDialog.dismiss();
                showTest(mNetWorkError);
            }

            @Override
            public void onNext(LoginInfo loginInfo) {
                if ("1".equals(loginInfo.getState())) {
                    Log.e("=================", loginInfo.getLogin().getHeadimg() + "\nid：" + loginInfo.getLogin().getId() + "\ntoken：" + loginInfo.getLogin().getRongyun_token());
                    showTest("手机号绑定微信后登录成功");
                    setToken(loginInfo);
                } else {
                    alertDialog = UIUtils.getAlertDialog(LoginActivity.this, "授权登录失败", "返回状态：" + loginInfo.getState() + " \t" + loginInfo.getMsg(), "取消", "微信登录", 0, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();


                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            mLoadingAlertDialog.show();
                            LoginUtil.getInstance().loginWX("login");

                        }
                    });
                    alertDialog.show();
                }

            }
        });
        addSubscription(sbMyAccount);
    }

    /**
     * 用户通过手机号密码登录
     *
     * @param phone
     * @param password
     */
    private void LoginPhone(final String phone, String password) {
        showTest("正在登录，请稍后...");
        mLoadingAlertDialog.show();
        Subscription sbGetPhoneCode = wrapObserverWithHttp(WorkService.getWorkService().LoginPhone(phone, password)).subscribe(new Subscriber<LoginInfo>() {
            @Override
            public void onCompleted() {


            }

            @Override
            public void onError(Throwable e) {
                mLoadingAlertDialog.dismiss();
                showTest("服务器连接失败");
                e.printStackTrace();
            }

            @Override
            public void onNext(final LoginInfo loginInfo) {
                mLoadingAlertDialog.dismiss();
                if (loginInfo != null) {
                    if (loginInfo.getState().equals("1")) {
                        Log.e("phone==", phone + "\n" + loginInfo.getPassword());
                        if (!TextUtils.isEmpty(loginInfo.getPhone())) {
                            SPUtils.put(SPUtils.KEY_PHONE_NUMBER, loginInfo.getPhone());
                        }
                        if (!TextUtils.isEmpty(loginInfo.getPassword())) {
                            SPUtils.put(SPUtils.KEY_PASSWORD, loginInfo.getPassword());
                        }
                        if(!TextUtils.isEmpty(loginInfo.getLogin().getPathimg()))
                            SPUtils.put(SPUtils.qr_code_img,loginInfo.getLogin().getPathimg());
                        if(!TextUtils.isEmpty(loginInfo.getLogin().getHeadimg()))
                            SPUtils.put(SPUtils.KEY_TOPIC,loginInfo.getLogin().getHeadimg());
                        if(!TextUtils.isEmpty(loginInfo.getLogin().getUser_name()))
                            SPUtils.put(SPUtils.KEY_USER_NAME,loginInfo.getLogin().getUser_name());
                        if (loginInfo.getHasWeixin().equals("0")) {
                            alertDialog = UIUtils.getAlertDialog(LoginActivity.this, null, "绑定微信才会有更多的功能哦", "稍后再说", "去绑定", 0, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();


                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                    mLoadingAlertDialog.show();
                                    LoginUtil.getInstance().loginWX("loginAndPhone");
                                }
                            });
                            alertDialog.show();
                        } else {
                            showTest("手机号登录成功");
                            setToken(loginInfo);
                        }


                    } else {
                        showTest(loginInfo.getMsg()+"\t状态码："+loginInfo.getState());
                    }

                }


            }
        });
        addSubscription(sbGetPhoneCode);
    }

    private void setToken(final LoginInfo loginInfo) {
        if(!TextUtils.isEmpty(loginInfo.getLogin().getPathimg()))
            SPUtils.put(SPUtils.qr_code_img,loginInfo.getLogin().getPathimg());
        if(!TextUtils.isEmpty(loginInfo.getLogin().getHeadimg()))
            SPUtils.put(SPUtils.KEY_TOPIC,loginInfo.getLogin().getHeadimg());
        if(!TextUtils.isEmpty(loginInfo.getLogin().getUser_name()))
            SPUtils.put(SPUtils.KEY_USER_NAME,loginInfo.getLogin().getUser_name());

        mLoadingAlertDialog.show();
        if (!TextUtils.isEmpty(loginInfo.getLogin().getRongyun_token())) {
            editor.putString("loginToken", loginInfo.getLogin().getRongyun_token());

            editor.apply();
            SPUtils.put(SPUtils.KEY_GET_TOKEN, loginInfo.getLogin().getRongyun_token());
            RongIM.connect(loginInfo.getLogin().getRongyun_token(), new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                    showTest("融云连接失败，重连中...");
                    mLoadingAlertDialog.dismiss();
                    getTokens(loginInfo);
                }

                @Override
                public void onSuccess(String s) {
                    showTest("连接融云成功，请稍等...");
                    mLoadingAlertDialog.dismiss();
                    if (loginInfo.getLogin().getPhone() != null) {
                        SPUtils.put(SPUtils.KEY_PHONE_NUMBER, loginInfo.getLogin().getPhone());
                    }

                    SPUtils.put(SPUtils.KEY_GET_JlOGIN_ID, loginInfo.getLogin().getTjloginid());

                    SPUtils.put(SPUtils.KEY_GET_ID, loginInfo.getLogin().getId());//用户id
                    SPUtils.put(SPUtils.GET_LOGIN_ID, loginInfo.getLogin().getId()+"");//用户id 用于解决session 问题

                    SPUtils.put(SPUtils.KEY_ISEXPIRE, loginInfo.getLogin().getIs_expire());
                    /***
                     * 设置当前用户信息
                     */
                    Log.e("login head;", RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL +loginInfo.getLogin().getHeadimg());
                    RongIM.getInstance().setCurrentUserInfo(new UserInfo(loginInfo.getLogin().getId() + "",
                            loginInfo.getLogin().getUser_name() == null ? null : loginInfo.getLogin().getUser_name(), loginInfo.getLogin().getHeadimg() == null ? null : Uri.parse(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL+loginInfo.getLogin().getHeadimg())));

                    loadData(1);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.e("connect", "onError errorcode:" + errorCode.getValue());
                    showTest("融云连接失败...");
                    mLoadingAlertDialog.dismiss();
                }
            });
        } else {
            getTokens(loginInfo);
        }
    }

    /**
     * 获取用户token信息
     *
     * @param loginInfo
     */
    private void getTokens(final LoginInfo loginInfo) {
        showTest("正在获取token,请稍后...");
      //  mLoadingAlertDialog.show();
        Subscription sbGetPhoneCode = wrapObserverWithHttp(WorkService.getWorkService().getToken((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),loginInfo.getLogin().getId())).subscribe(new Subscriber<GetToken>() {
            @Override
            public void onCompleted() {
               // mLoadingAlertDialog.dismiss();

            }

            @Override
            public void onError(Throwable e) {
              //  mLoadingAlertDialog.dismiss();
                showTest("服务器连接失败");
                e.printStackTrace();
            }

            @Override
            public void onNext(final GetToken getToken) {
                if (getToken != null) {
                    if (getToken.getState().equals("1")) {
                        showTest("token获取成功,连接融云...");
                        loginInfo.getLogin().setRongyun_token(getToken.getToken());
                        setToken(loginInfo);
                    } else {
                        showTest(getToken.getMsg());
                    }

                }


            }
        });
        addSubscription(sbGetPhoneCode);

    }

    /**
     * 获取我的好友信息
     *
     * @param currentPager
     */
    private void loadData(int currentPager) {
        mLoadingAlertDialog.show();

        Subscription sbGetCode = wrapObserverWithHttp(WorkService.getWorkService().myFrind((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),currentPager, 1)).subscribe(new Subscriber<MyFriends>() {
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
            public void onNext(MyFriends myFriends) {
                if ("1".equals(myFriends.getState())) {
                    showTest("正在加载好友信息，请稍等...");
                    loadData();//查找我的下级所有信息
                    ArrayList<UserInfoRong> userInfoAddRongs = new ArrayList<UserInfoRong>();

                    for (MyFriends.LoginListBean friend : myFriends.getLoginList()) {
                    Log.e("我的好友",friend.getId()+"\t"+friend.getUser_name()+"\t"+friend.getHeadimg());
                        userInfoAddRongs.add(new UserInfoRong(friend.getId(), friend.getUser_name(), friend.getHeadimg(), friend.getAgent()));

                    }

                    mgr.addFriend(userInfoAddRongs);//添加到我的好友表里
                    userInfoAddRongs.add(new UserInfoRong(1000, "拇指推官方账号", "http://www.muzhitui.cn/song/wx/img/logo.jpg", "2"));
                    mgr.add(userInfoAddRongs);//添加到所有好友的表里

                } else {
                    showTest(myFriends.getState());
                }

            }
        });
        addSubscription(sbGetCode);
    }

    /**
     * 获取我的下级信息
     *
     * @param
     */
    private void loadData() {

        mLoadingAlertDialog.show();

        int id = (int) SPUtils.get(SPUtils.KEY_GET_ID, 0);


        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().myLevelMember((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),0, id, 1, 0)).subscribe(new Subscriber<MyLever>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                showErrorView();
                showTest(mNetWorkError);
                e.printStackTrace();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onNext(MyLever levelMembers) {
                if ("1".equals(levelMembers.getState())) {
                    showTest("我的下级获取成功");
                    ArrayList<UserInfoRong> userInfoAddRongs = new ArrayList<UserInfoRong>();
                    for (MyLever.LoginListBean levelMember : levelMembers.getLoginList()) {

                        userInfoAddRongs.add(new UserInfoRong(levelMember.getId(), levelMember.getUser_name(), levelMember.getHeadimg(), levelMember.getAgent()));

                    }

                    mgr.addLevels(userInfoAddRongs);//我的下级添加到数据库表里
                    mgr.add(userInfoAddRongs);//我的下级添加到数据库表所有好友表里


                    jiesu();
                } else {
                    showErrorView();
                    showTest(mServerEror);
                }
            }
        });
        addSubscription(sbMyAccount);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (etLoginPhone.getText().length() > 0 && etLoginPassword.getText().length() > 0) {
            btnLogin.setEnabled(true);
            btnLogin.setClickable(true);
            btnLogin.setBackgroundResource(R.drawable.selector_login_btn);
            // btnLogin.setTextColor(getResources().getColor(R.color.green));
            // btnLogin.setBackgroundResource(R.drawable.shape_btn_green_bg1);
            btnLogin.setTextColor(getResources().getColor(R.color.white));
        } else {
            btnLogin.setEnabled(false);
            btnLogin.setClickable(false);
            btnLogin.setBackgroundResource(R.drawable.shape_et_white_bg);
            btnLogin.setTextColor(getResources().getColor(R.color.gray));
        }
    }
}
