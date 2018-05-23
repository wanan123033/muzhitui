package com.nevermore.muzhitui;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nevermore.muzhitui.event.DataRefreshEvent;
import com.nevermore.muzhitui.event.EventRedDot;
import com.nevermore.muzhitui.fragment.DynamicFragment;
import com.nevermore.muzhitui.fragment.FriendsFragment;
import com.nevermore.muzhitui.fragment.HomeFragment;
import com.nevermore.muzhitui.fragment.PeopleFragment;
import com.nevermore.muzhitui.fragment.TabMyFragment;
import com.nevermore.muzhitui.module.MessageNum;
import com.nevermore.muzhitui.module.bean.AppVersion;
import com.nevermore.muzhitui.module.bean.Code;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nevermore.muzhitui.module.sqllite.DBManager;
import com.nevermore.muzhitui.module.sqllite.UserInfoRong;
import com.nevermore.muzhitui.receiver.ExampleUtil;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.MyLogger;
import base.SPUtils;
import base.UIUtils;
import base.service.HomeWatcherReceiver;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;
import me.leolin.shortcutbadger.ShortcutBadger;
import rx.Subscriber;
import rx.Subscription;

public class MainActivity extends BaseActivityTwoV implements
        ViewPager.OnPageChangeListener,
        View.OnClickListener,
        IUnReadMessageObserver, RongIM.UserInfoProvider {




    @BindView(R.id.iv_main_add)
    RelativeLayout ivMainAdd;
    @BindView(R.id.main_viewpager)
    ViewPager mViewPager;
    public static ViewPager viewPager;
    @BindView(R.id.tab_img_chats)
    ImageView mTabImgChats;
    @BindView(R.id.tab_text_chats)
    TextView mTabTextChats;
    @BindView(R.id.seal_chat)
    RelativeLayout mSealChat;
    @BindView(R.id.tab_img_contact)
    ImageView mTabImgContact;
    @BindView(R.id.tab_text_contact)
    TextView mTabTextContact;
    @BindView(R.id.tab_img_find)
    ImageView mTabImgFind;
    @BindView(R.id.tab_text_find)
    TextView mTabTextFind;
    @BindView(R.id.seal_find)
    RelativeLayout mSealFind;
    @BindView(R.id.tab_img_me)
    ImageView mTabImgMe;
    @BindView(R.id.tab_text_me)
    TextView mTabTextMe;
    @BindView(R.id.seal_me)
    RelativeLayout mSealMe;
    @BindView(R.id.main_bottom)
    LinearLayout mMainBottom;
    @BindView(R.id.mine_red)
    TextView mMineRed;
    @BindView(R.id.main_show)
    RelativeLayout mMainShow;
    @BindView(R.id.me_red)
    ImageView me_red;


    private LoadingAlertDialog mLoadingAlertDialog;


    public static boolean mIsLogin = false;
    private List<Fragment> mFragment = new ArrayList<>();

    private Conversation.ConversationType[] mConversationsTypes = null;
    public static int loginId = 0;
    public static int tjLoginId = 0;
    private android.app.AlertDialog alertDialogAPP;

    public static final String MESSAGE_RECEIVED_ACTION = "com.nevermore.muzhitui.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static boolean isForeground;

    @Override
    public void init() {

        hideActionBar();
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        loginId = (int) SPUtils.get(SPUtils.KEY_GET_ID, 0);
        tjLoginId = (int) SPUtils.get(SPUtils.KEY_GET_JlOGIN_ID, 0);
        viewPager = mViewPager;
        //版本更新的窗口
        // initMenuPpwWindowDash();

        mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP,
                Conversation.ConversationType.PUBLIC_SERVICE,
                Conversation.ConversationType.APP_PUBLIC_SERVICE,
                Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.DISCUSSION};
        //初始化DBManager
        mgr = new DBManager(MainActivity.this);
        RongIM.setUserInfoProvider(MainActivity.this, true);//设置用户信息提供者。

      //设置官方账号置顶
        RongIM.getInstance().setConversationToTop(Conversation.ConversationType.PRIVATE, "1000", true, new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
        //弹出版本更新对话框
        alertDialogAPP = UIUtils.getAlertDialog(MainActivity.this,  "温馨提示", "有新版本，更新才能有更多的体验哟！", null, "立即更新",0,  null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳转到应用市场选择
                intentAppMarket();
            }
        });
        loadDataP();
//        String s = null;
//        s.length();
    }
    private void loadDataP() {

        Subscription sbGetCode = wrapObserverWithHttp(WorkService.getWorkService().isPublic((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))).subscribe(new Subscriber<Code>() {
            @Override
            public void onCompleted() {
                Log.e("string","onCompleted");

            }

            @Override
            public void onError(Throwable e) {
                Log.e("string","onError");
            }

            @Override
            public void onNext(Code string) {
                Logger.e("string:",string.getIs_public());
                Log.e("string",string.getIs_public());
                SPUtils.put(SPUtils.IS_PUBLIC,string.getIs_public());

            }
        });
        addSubscription(sbGetCode);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventEMMessage(EventRedDot eventRedDot) {
        getUnreadCount();


    }

    private void getUnreadCount() {
        int num1 = RongIMClient.getInstance().getUnreadCount(Conversation.ConversationType.SYSTEM,Conversation.ConversationType.PRIVATE);
        int num2 = (int) SPUtils.get(SPUtils.XIAJI_NUM,0);
        if (num1 + num2> 0) {
            mMineRed.setVisibility(View.VISIBLE);
            mMineRed.setText(String.valueOf(num1 + num2));
        } else {
            mMineRed.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventEMMessage(EventBusContanct contanct){
        if(contanct.getFlag() == EventBusContanct.TABMY_STATE){
            me_red.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        getUnreadCount();
        getNewAppVersion();

        WindowManager wm1 = this.getWindowManager();
        int width1 = wm1.getDefaultDisplay().getWidth();
        int height1 = wm1.getDefaultDisplay().getHeight();

        MyLogger.kLog().e("width = "+width1 +" , height = "+ height1);
    }

    private void initMainViewPager() {

        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);


        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }
        };

        mViewPager.setAdapter(fragmentPagerAdapter);
        mFragment.add(new HomeFragment());
//        mFragment.add(new MessageFragment());
        mFragment.add(new DynamicFragment());
        mFragment.add(new FriendsFragment());
        mFragment.add(new TabMyFragment());
        fragmentPagerAdapter.notifyDataSetChanged();
        mViewPager.setOffscreenPageLimit(4);
        /**设置页面滑动监听**/
        mViewPager.setOnPageChangeListener(this);

        RongIM.getInstance().addUnReadMessageCountChangedObserver(this, mConversationsTypes);
        getConversationPush();// 获取 push 的 id 和 target
        getPushMessage();

    }


    @Override
    public int createSuccessView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
        MobclickAgent.onResume(this);
        EventBus.getDefault().post(EventBusContanct.getInstance(EventBusContanct.REFRESH_DYNAMIC));
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeground = false;
        MobclickAgent.onPause(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        changeTextViewColor();
        changeSelectedTabState(0);

        initMainViewPager();
        registerHomeKeyReceiver(this);

        JPushInterface.init(getApplicationContext());
    }

    private void changeTextViewColor() {
        mTabImgChats.setImageResource(R.mipmap.ic_index_on);
        mTabImgContact.setImageResource(R.mipmap.iv_material_select);
        mTabImgFind.setImageResource(R.mipmap.iv_contact_select);
        mTabImgMe.setImageResource(R.mipmap.iv_my_select);
        mTabTextChats.setTextColor(Color.parseColor("#abadbb"));
        mTabTextContact.setTextColor(Color.parseColor("#abadbb"));
        mTabTextFind.setTextColor(Color.parseColor("#abadbb"));
        mTabTextMe.setTextColor(Color.parseColor("#abadbb"));
    }

    private void changeSelectedTabState(int position) {
        switch (position) {
            case 0:
                mTabTextChats.setTextColor(Color.parseColor("#222222"));
                mTabImgChats.setImageResource(R.mipmap.ic_index_off);
                break;
            case 1:
                mTabTextContact.setTextColor(Color.parseColor("#222222"));
                mTabImgContact.setImageResource(R.mipmap.iv_material_selected);
                break;
            case 2:
                mTabTextFind.setTextColor(Color.parseColor("#222222"));
                mTabImgFind.setImageResource(R.mipmap.iv_contact_selected);
                break;
            case 3:
                mTabTextMe.setTextColor(Color.parseColor("#222222"));
                mTabImgMe.setImageResource(R.mipmap.iv_my_selected);
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra("systemconversation", false)) {
            mViewPager.setCurrentItem(1, false);
        }
    }


    private void getConversationPush() {
        if (getIntent() != null && getIntent().hasExtra("PUSH_CONVERSATIONTYPE") && getIntent().hasExtra("PUSH_TARGETID")) {

            final String conversationType = getIntent().getStringExtra("PUSH_CONVERSATIONTYPE");
            final String targetId = getIntent().getStringExtra("PUSH_TARGETID");


            RongIM.getInstance().getConversation(Conversation.ConversationType.valueOf(conversationType), targetId, new RongIMClient.ResultCallback<Conversation>() {
                @Override
                public void onSuccess(Conversation conversation) {

                    if (conversation != null) {

                        if (conversation.getLatestMessage() instanceof ContactNotificationMessage) { //好友消息的push
                            //startActivity(new Intent(MainActivity.this, NewFriendsActivity.class));
                            //MztRongContext.getInstance().popAllActivity(2);
                        } else {
                            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon().appendPath("conversation")
                                    .appendPath(conversationType).appendQueryParameter("targetId", targetId).build();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode e) {

                }
            });
        }
    }

    /**
     * 得到不落地 push 消息
     */
    private void getPushMessage() {
        Intent intent = getIntent();

        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {

            //该条推送消息的内容。
            String content = intent.getData().getQueryParameter("pushContent");
            //标识该推送消息的唯一 Id。
            String id = intent.getData().getQueryParameter("pushId");
            //用户自定义参数 json 格式，解析后用户可根据自己定义的 Key 、Value 值进行业务处理。
            String extra = intent.getData().getQueryParameter("extra");

            Log.d("TestPushActivity", "--content:" + content + "--id:" + id + "---extra:" + extra);



            String path = intent.getData().getPath();
            if (path.contains("push_message")) {
                SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
                String cacheToken = sharedPreferences.getString(SPUtils.KEY_GET_TOKEN, "");
                if (TextUtils.isEmpty(cacheToken)) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                } else {
                    if (!RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
                        mLoadingAlertDialog.show();

                        RongIM.connect(cacheToken, new RongIMClient.ConnectCallback() {
                            @Override
                            public void onTokenIncorrect() {
                                mLoadingAlertDialog.dismiss();
                            }

                            @Override
                            public void onSuccess(String s) {
                                mLoadingAlertDialog.dismiss();
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode e) {
                                mLoadingAlertDialog.dismiss();
                            }
                        });
                    }
                }
            }
        }
    }

    @Override
    public void onCountChanged(int count) {
        if (count == 0) {
            /**android 角标显示 及数量显示 清空*/
            ShortcutBadger.removeCount(MainActivity.this);
            mMineRed.setVisibility(View.GONE);
        } else if (count > 0 && count < 100) {
            /**android 角标显示 及数量显示*/
            ShortcutBadger.applyCount(MainActivity.this, count);

            mMineRed.setVisibility(View.VISIBLE);
        } else {
            mMineRed.setVisibility(View.VISIBLE);
        }
        if(count >= 0) {
            EventBus.getDefault().post(new MessageNum(count));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        EventBus.getDefault().unregister(this);
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(this);
        if (mHomeKeyReceiver != null)
            this.unregisterReceiver(mHomeKeyReceiver);
        if (RongIM.getInstance() != null ) {

            RongIMClient.getInstance().disconnect();
            RongIM.getInstance().disconnect();

        }
        //应用的最后一个Activity关闭时应释放DB
        mgr.closeDB();
      //  android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }


    private HomeWatcherReceiver mHomeKeyReceiver = null;

    private void registerHomeKeyReceiver(Context context) {
        if (mHomeKeyReceiver == null) {
            mHomeKeyReceiver = new HomeWatcherReceiver();
            final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            try {
                context.registerReceiver(mHomeKeyReceiver, homeFilter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changeTextViewColor();
        changeSelectedTabState(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }



    @OnClick({R.id.seal_chat, R.id.seal_dynamic, R.id.iv_main_add, R.id.seal_find, R.id.seal_me})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.seal_chat:
                mViewPager.setCurrentItem(0, false);
                break;
//            case R.id.seal_contact_list:
//
//                mViewPager.setCurrentItem(1, false);
//                break;
            case R.id.iv_main_add:

                new MainPopWindow(this).showAtLocation(ivMainAdd, Gravity.BOTTOM, 0, 0);
//                mAddPopWindow.addMo();
                break;
            case R.id.seal_find:
                EventBus.getDefault().post(EventBusContanct.getInstance(EventBusContanct.REFRSH_CHAT));
                EventBus.getDefault().post(EventBusContanct.getInstance(EventBusContanct.REFRESH_XIAJI));
                EventBus.getDefault().post(EventBusContanct.getInstance(EventBusContanct.REFRSH_FRIENDS));
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.seal_me:
                me_red.setVisibility(View.GONE);
                mViewPager.setCurrentItem(3, false);
                EventBus.getDefault().post(new DataRefreshEvent());
                break;
            case R.id.seal_dynamic:
                mViewPager.setCurrentItem(1, false);
                EventBus.getDefault().post(EventBusContanct.getInstance(EventBusContanct.REFRESH_DYNAMIC));
                break;
        }
    }

    private PopupWindow mPpwMenuNewVersion;//版本更新

    /**
     * 版本更新的提示
     */

    private void getNewAppVersion() {


        Subscription sbGetAppVersion = wrapObserverWithHttp(WorkService.getWorkService().getAppVersion((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""))).subscribe(new Subscriber<AppVersion>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

                e.printStackTrace();
            }

            @Override
            public void onNext(AppVersion appVersion) {

                if (!TextUtils.isEmpty(appVersion.getVersion())) {
                    Log.e("app 服务器返回的版本  111", appVersion.getVersion() + "");
                    if (Integer.parseInt(appVersion.getVersion()) > Integer.parseInt(getAppVersion())) {
                        Log.e("app 服务器返回的版本 222", appVersion.getVersion() + "");
                       // mPpwMenuNewVersion.showAtLocation(mViewPager, Gravity.BOTTOM, 0, 0);
                        alertDialogAPP.show();
                    }
                }
            }
        });
        addSubscription(sbGetAppVersion);
    }

    private void initMenuPpwWindowDash() {
        //设置contentView
        View contentView = UIUtils.inflate(MainActivity.this, R.layout.ppw_newapp_version);
        mPpwMenuNewVersion = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPpwMenuNewVersion.setContentView(contentView);
        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setColor(Color.parseColor("#80000000"));
        mPpwMenuNewVersion.setBackgroundDrawable(colorDrawable);
        mPpwMenuNewVersion.setOutsideTouchable(true);
        //设置各个控件的点击响应
        contentView.findViewById(R.id.iv_pnv_Close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPpwMenuNewVersion.dismiss();
            }
        });
        contentView.findViewById(R.id.tv_pnv_updata).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到应用市场选择

                intentAppMarket();
            }
        });


    }

    private boolean isPkgInstalled(String pkgName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    private void intentAppMarket() {
        if (isPkgInstalled("com.tencent.android.qqdownloader")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse("market://details?id=" + "com.nevermore.muzhitui");//app包名
            intent.setData(uri);
            intent.setPackage("com.tencent.android.qqdownloader");//360应用市场包名

            startActivity(intent);
        }
        //未安装，跳转至market下载该程序
        else {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri uri = Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=com.nevermore.muzhitui&from=message&isappinstalled=0#opened");
            intent.setData(uri);
            startActivity(intent);
            alertDialogAPP.dismiss();
        }

    }

    /**
     * 2  * 获取版本号
     * 3  * @return 当前应用的版本号
     * 4
     */
    public String getAppVersion() {
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            String version = info.versionCode + "";
            Log.e("app 当前手机app版本", version);
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("appversion", "沒有找到版本号");
            return "沒有找到版本号";
        }
    }

    private DBManager mgr;

    @Override
    public UserInfo getUserInfo(String userId) {
        List<UserInfoRong> userInfoRongs = mgr.query();
        Log.e("main", "用戶信息提供者" + userInfoRongs.size());
        for (UserInfoRong userInfoRong : userInfoRongs) {
            Log.e("用户信息", userInfoRong.id + " \t" + userInfoRong.user_name + " \t" + userInfoRong.headimg);
            if (String.valueOf(userInfoRong.id).equals(userId)) {
                Log.e("用户信息", userInfoRong.user_name + " \t" + userInfoRong.headimg);

                return new UserInfo(userInfoRong.id + "", userInfoRong.user_name, userInfoRong.headimg == null ? null : Uri.parse(userInfoRong.headimg));
            }
        }
        return null;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    setCostomMsg(showMsg.toString());
                }
            } catch (Exception e){
            }
        }
    };

    private void setCostomMsg(String msg){
        //TODO 系统消息图标+1
        Bundle bundle = new Bundle();
        bundle.putString("system_msg",msg);
        mFragment.get(0).setArguments(bundle);
    }


}
