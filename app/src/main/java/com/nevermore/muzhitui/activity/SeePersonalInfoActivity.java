package com.nevermore.muzhitui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.event.refrush;
import com.nevermore.muzhitui.module.bean.Code;
import com.nevermore.muzhitui.module.bean.FindUserInfoById;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nevermore.muzhitui.module.sqllite.DBManager;
import com.nevermore.muzhitui.module.sqllite.UserInfoRong;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;

import base.BaseActivityTwoV;
import base.ImageUtil;
import base.SPUtils;
import base.UIUtils;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;
import rx.Subscriber;
import rx.Subscription;


/**
 * Created by Simone on 2016/12/21.
 *
 * 查看好友信息页面
 */

public class SeePersonalInfoActivity extends BaseActivityTwoV {


    @BindView(R.id.ll_seeBack)
    RelativeLayout mLlSeeBack;
    @BindView(R.id.llSeePersonalInfoAddHead)
    ImageView mLlSeePersonalInfoAddHead;
    @BindView(R.id.llSeePersonalInfoAddName)
    TextView mLlSeePersonalInfoAddName;
    @BindView(R.id.tvSeeAddOrSend)
    TextView mTvSeeAddOrSend;
    @BindView(R.id.llSeePersonalInfoAdd)
    LinearLayout mLlSeePersonalInfoAdd;
    @BindView(R.id.tvSeePersonalInfoName)
    TextView mTvSeePersonalInfoName;
    @BindView(R.id.tvSeePersonalInfoId)
    TextView mTvSeePersonalInfoId;
    @BindView(R.id.tvSeePersonalInfowxid)
    TextView mTvSeePersonalInfowxid;
    @BindView(R.id.tvSeePersonalInfoPhone)
    TextView mTvSeePersonalInfoPhone;
    @BindView(R.id.tvSeePersonalInfoProduce)
    TextView mTvSeePersonalInfoProduce;
    @BindView(R.id.ivSex)
    ImageView mIvSex;
    @BindView(R.id.tvcity)
    TextView mTvcity;
    @BindView(R.id.ivMemState)
    ImageView mIvMemState;
    @BindView(R.id.tvMemState)
    TextView mTvMemState;
    @BindView(R.id.ivQrcode)
    ImageView mIvQrcode;

    private LoadingAlertDialog mLoadingAlertDialog;
    private String friend_state;
    private FindUserInfoById.LoginBean mUserInfo;
    String id = null;
    private DBManager mgr;
    @Override
    public void init() {
        hideActionBar();
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        //初始化DBManager
        mgr = new DBManager(this);

        id = getIntent().getStringExtra("id");
        friend_state = getIntent().getStringExtra("friend_state");

        Log.v("好友状态", friend_state + "");
        loadData(id);
        //1 好友, 2 请求添加, 3 请求被添加, 4 请求被拒绝, 5 我被对方删除
        if (Integer.parseInt(friend_state) == 2) {
            mTvSeeAddOrSend.setText("再次请求");


        } else if (Integer.parseInt(friend_state) == 3) {
            mTvSeeAddOrSend.setText("通过验证");


        } else if (Integer.parseInt(friend_state) == 4) {
            mTvSeeAddOrSend.setText("请求被拒绝");


        } else if (Integer.parseInt(friend_state) == 5) {
            mTvSeeAddOrSend.setText("请求被删除");


        } else if (Integer.parseInt(friend_state) == 11) {

            mTvSeeAddOrSend.setText("加为好友");

        } else if (Integer.parseInt(friend_state) == 12) {

            mTvSeeAddOrSend.setText("等待验证");

        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventEMMessage(refrush refrush) {


        if (refrush.getState() == 1) {
            mTvSeeAddOrSend.setText("等待验证");
            loadData(id);

        }


    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_see_personal_info;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    private void setText(FindUserInfoById.LoginBean info) {
        Log.e("head:",info.getHeadimg()+"");
        if (info.getHeadimg() != null) {
            ImageLoader.getInstance().displayImage(info.getHeadimg(), mLlSeePersonalInfoAddHead, ImageUtil.getInstance().getCircleDisplayOption());

        }

        mLlSeePersonalInfoAddName.setText(info.getUser_name() + "");
        mTvSeePersonalInfoName.setText(info.getUser_name() + "");
        mTvSeePersonalInfoId.setText(info.getId() + "");
        if (!TextUtils.isEmpty(info.getWechat())){
            mTvSeePersonalInfowxid.setText(info.getWechat() + "");
        }
        if (!TextUtils.isEmpty(info.getUser_phone())) {
            mTvSeePersonalInfoPhone.setText(info.getUser_phone() + "");
        }
        if (!TextUtils.isEmpty(info.getMp_desc())) {
            mTvSeePersonalInfoProduce.setText(info.getMp_desc());
        }else{
            mTvSeePersonalInfoProduce.setText("这个家伙太懒了，什么都没留下！");
        }
        if (TextUtils.isEmpty(mUserInfo.getWx_province())) {
            mUserInfo.setWx_province("未知");
        }
        if (TextUtils.isEmpty(mUserInfo.getWx_city())) {
            mUserInfo.setWx_city("未知");
        }

        mTvcity.setText(mUserInfo.getWx_province() + " • " + mUserInfo.getWx_city()+ " • ");
        Logger.e("性别：",mUserInfo.getWx_sex()+"");
        if (mUserInfo.getWx_sex() == 0) {
            mIvSex.setImageResource(R.mipmap.ic_nv);
        } else {
            mIvSex.setImageResource(R.mipmap.ic_nan);
        }
        if (!mUserInfo.getAgent().equals("未加入会员")) {
            if (mUserInfo.getAgent().equals("年费会员")) {
                mIvMemState.setImageResource(R.mipmap.ic_nainfei);

            } else {
                mIvMemState.setImageResource(R.mipmap.ic_zhongshen);
            }
            mTvMemState.setVisibility(View.GONE);
            mIvMemState.setVisibility(View.VISIBLE);
        } else {
            mTvMemState.setVisibility(View.VISIBLE);
            mIvMemState.setVisibility(View.GONE);
        }

        mTvSeePersonalInfowxid.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String text = mTvSeePersonalInfowxid.getText().toString().trim();
                if(!TextUtils.isEmpty(text)){
                    ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    cmb.setPrimaryClip(ClipData.newPlainText(null, text));
                    showTest("复制成功");
                    return true;
                }
                return false;
            }
        });
    }

    private void loadDataresponse(int state,final int targetUserId) {

        mLoadingAlertDialog.show();

        Subscription sbGetCode = wrapObserverWithHttp(WorkService.getWorkService().responsesFriend((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),state, targetUserId, null, null)).subscribe(new Subscriber<Code>() {
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
            public void onNext(Code code) {
                if ("1".equals(code.getState())) {
                    showTest("添加好友成功，你们现在可以聊天啦");

                    ArrayList<UserInfoRong> userInfoAddRongs = new ArrayList<UserInfoRong>();
                    userInfoAddRongs.add(new UserInfoRong(mUserInfo.getId(), mUserInfo.getUser_name(), mUserInfo.getHeadimg(), mUserInfo.getAgent()));
                    mgr.add(userInfoAddRongs);
                    mgr.addFriend(userInfoAddRongs);

                    //刷新刚添加的好友信息
                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(mUserInfo.getId() + "",
                            mUserInfo.getUser_name() == null ? null :mUserInfo.getUser_name(), mUserInfo.getHeadimg() == null ? null : Uri.parse(mUserInfo.getHeadimg())));
                    EventBus.getDefault().post(new refrush(2));
                    sendTextMessage("我们已经是好友啦,可以开始聊天了~",targetUserId+"");
                    Intent intent = new Intent(SeePersonalInfoActivity.this, SeePersonalInfoIsFriendActivity.class);
                    intent.putExtra("id", getIntent().getStringExtra("id"));
                    startActivity(intent);
                    finish();

                } else {
                    showTest(code.getMsg());

                }

            }
        });
        addSubscription(sbGetCode);
    }

    // 发送文本消息。
    private void sendTextMessage(String message,String id) {

        TextMessage txtMsg = TextMessage.obtain(message);


        RongIM.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, id, txtMsg, message, new Date(System.currentTimeMillis()).toString(), new RongIMClient.SendMessageCallback() {
            @Override
            public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
              //  showTest("发送失败");
            }

            @Override
            public void onSuccess(Integer integer) {

                //showTest("发送成功");
            }
        });

    }
    private void loadData(String id) {

        mLoadingAlertDialog.show();

        Subscription sbGetCode = wrapObserverWithHttp(WorkService.getWorkService().getUserById((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),id)).subscribe(new Subscriber<FindUserInfoById>() {
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
            public void onNext(FindUserInfoById userInfo) {

                if ("1".equals(userInfo.getState())) {
                    mUserInfo = userInfo.getLogin();

                    Log.e("TAG",mUserInfo.toString());
                    setText(userInfo.getLogin());

                } else {
                    showTest(userInfo.getMsg());

                }

            }
        });
        addSubscription(sbGetCode);
    }

    @OnClick({R.id.ll_seeBack, R.id.llSeePersonalInfoAddHead, R.id.llSeePersonalInfoAdd, R.id.ivQrcode,R.id.ll_callphone,R.id.ll_dynamic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_seeBack:
                finish();
                break;
            case R.id.llSeePersonalInfoAddHead:
                initPpwWindow1();
                break;
            case R.id.llSeePersonalInfoAdd:
                if (Integer.parseInt(friend_state) == 3) {

                    loadDataresponse(1, Integer.parseInt(id));


                } else if (Integer.parseInt(friend_state) == 11 || Integer.parseInt(friend_state) == 2) {


                    Intent intent = new Intent(SeePersonalInfoActivity.this, FriendVerifcationActivity.class);
                    intent.putExtra("id", Integer.parseInt(id));


                    startActivity(intent);

                }
                break;
            case R.id.ivQrcode:
                Intent intentid = new Intent(SeePersonalInfoActivity.this, UserInfoQRCode.class);
                intentid.putExtra("id", Integer.parseInt(id));

                startActivity(intentid);
                break;

            case R.id.ll_callphone:
                if(!TextUtils.isEmpty(mUserInfo.getUser_phone())) {
                    initPpwindow2();
                }
                break;
            case R.id.ll_dynamic:
                Intent intent = new Intent(getApplicationContext(),DynamicPersonActivity.class);
                intent.putExtra(DynamicPersonActivity.USERID,String.valueOf(id));
                startActivity(intent);
                break;


        }
    }

    private PopupWindow mOptImgPopWindow1;

    private void initPpwWindow1() {
        //设置contentView
        View contentView = UIUtils.inflate(SeePersonalInfoActivity.this, R.layout.ppw_toast);
        mOptImgPopWindow1 = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mOptImgPopWindow1.setContentView(contentView);
        mOptImgPopWindow1.setBackgroundDrawable(new BitmapDrawable());
        mOptImgPopWindow1.setOutsideTouchable(true);
        //设置各个控件的点击响应
        RelativeLayout rlToast = (RelativeLayout) contentView.findViewById(R.id.rlToast);
        ImageView icToast = (ImageView) contentView.findViewById(R.id.icToast);
        icToast.setVisibility(View.GONE);
        RelativeLayout rlImage = (RelativeLayout) contentView.findViewById(R.id.rlImage);
        ImageView ivBigHead = (ImageView) contentView.findViewById(R.id.ivBigHead);
        rlImage.setVisibility(View.VISIBLE);
        if (mUserInfo.getHeadimg() != null) {
            ImageLoader.getInstance().displayImage(mUserInfo.getHeadimg(), ivBigHead, ImageUtil.getInstance().getBaseDisplayOption());
        }

        rlToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptImgPopWindow1.dismiss();
            }
        });

        mOptImgPopWindow1.showAtLocation(mLlSeePersonalInfoAddHead, Gravity.BOTTOM, 0, 0);
    }

    private void initPpwindow2() {
        View view = LayoutInflater.from(this).inflate(R.layout.pw_callphone,null);
        final PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,true);
        view.findViewById(R.id.btnCallphone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + mUserInfo.getUser_phone());
                intent.setData(data);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.btndimiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(findViewById(R.id.ll_callphone),Gravity.BOTTOM,0,0);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        //应用的最后一个Activity关闭时应释放DB
        mgr.closeDB();
    }
}
