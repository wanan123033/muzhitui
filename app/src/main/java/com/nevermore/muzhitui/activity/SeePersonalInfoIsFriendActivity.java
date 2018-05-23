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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.nevermore.muzhitui.CallPhonePopintWindow;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.rongyun.MztRongContext;

import com.nevermore.muzhitui.module.bean.Code;
import com.nevermore.muzhitui.module.bean.FindUserInfoById;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nevermore.muzhitui.module.sqllite.DBManager;
import com.nevermore.muzhitui.module.sqllite.UserInfoRong;
import com.nostra13.universalimageloader.core.ImageLoader;

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
import rx.Subscriber;
import rx.Subscription;


/**
 * Created by Simone on 2016/12/21.
 */

public class SeePersonalInfoIsFriendActivity extends BaseActivityTwoV implements View.OnClickListener {


    @BindView(R.id.rlSeeFBack)
    RelativeLayout mIvSeeFBack;
    @BindView(R.id.ivSeeFHead)
    ImageView mIvSeeFHead;
    @BindView(R.id.tvSeeFAddName)
    TextView mTvSeeFAddName;
    @BindView(R.id.llSeeFSendMessage)
    LinearLayout mLlSeeSendMessage;
    @BindView(R.id.tvSeeFInfoName)
    TextView mTvSeeFInfoName;
    @BindView(R.id.tvSeeFId)
    TextView mTvSeeFId;
    @BindView(R.id.tvSeeFwxid)
    TextView mTvSeeFwxid;
    @BindView(R.id.tvSeeFPhone)
    TextView mTvSeeFPhone;
    @BindView(R.id.tvSeeFProduce)
    TextView mTvSeeFProduce;
    @BindView(R.id.flSeeFReport)
    FrameLayout mFlSeeFReport;
    @BindView(R.id.tbSeeFPullBlack)
    ToggleButton mTbSeeFPullBlack;
    @BindView(R.id.btnSeeFDeleteF)
    Button mBtnSeeFDeleteF;

    @BindView(R.id.llSeeOther)
    LinearLayout mLlSeeOther;
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
    private DBManager mgr;
    private FindUserInfoById.LoginBean mUserInfo;
    String id = null;
private boolean isShowDelete=true;
    @Override
    public void init() {
        hideActionBar();
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        //初始化DBManager
        mgr = new DBManager(this);
        id = getIntent().getStringExtra("id");
        isShowDelete=  getIntent().getBooleanExtra("isShowDelete",true);

        int userid = (int) SPUtils.get(SPUtils.KEY_GET_ID, 0);
        if (id.equals(String.valueOf(userid))) {
            mLlSeeOther.setVisibility(View.GONE);
        } else {
            mLlSeeOther.setVisibility(View.VISIBLE);
        }


        RongIM.getInstance().getBlacklistStatus(String.valueOf(id), new RongIMClient.ResultCallback<RongIMClient.BlacklistStatus>() {

            @Override
            public void onSuccess(RongIMClient.BlacklistStatus blacklistStatus) {
                if (blacklistStatus != null) {
                    /**
                     * IN_BLACK_LIST 在黑名单中
                     *
                     */

                    if (blacklistStatus == RongIMClient.BlacklistStatus.IN_BLACK_LIST) {
                        mTbSeeFPullBlack.setChecked(true);
                    } else if (blacklistStatus == RongIMClient.BlacklistStatus.NOT_IN_BLACK_LIST) {
                        mTbSeeFPullBlack.setChecked(false);
                    }
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
        mTbSeeFPullBlack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    RongIM.getInstance().addToBlacklist(id, new RongIMClient.OperationCallback() {
                        @Override
                        public void onSuccess() {
                            showTest("黑名单加入成功,你将不再收到对方的消息");
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            showTest("黑名单加入失败");
                        }
                    });
                } else {
                    RongIM.getInstance().removeFromBlacklist(id, new RongIMClient.OperationCallback() {
                        @Override
                        public void onSuccess() {
                            showTest("移除黑名单成功");
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            showTest("移除黑名单失败");
                        }
                    });
                }


            }
        });

        mTvSeeFwxid.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String text = mTvSeeFwxid.getText().toString().trim();
                if(!TextUtils.isEmpty(text)){
                    ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    cmb.setPrimaryClip(ClipData.newPlainText(null, text));
                    showTest("复制成功");
                    return true;
                }
                return false;
            }
        });

        loadData(id);

        if (isShowDelete){

            mBtnSeeFDeleteF.setBackgroundResource(R.drawable.selector_delete_btn);
            }else{

            mBtnSeeFDeleteF.setBackgroundResource(R.drawable.shape_btn_gray_delete_bg);
            mBtnSeeFDeleteF.setEnabled(false);
            mBtnSeeFDeleteF.setClickable(false);
            }
             }

    @Override
    public int createSuccessView() {
        return R.layout.activity_see_personal_info_isfriend;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //应用的最后一个Activity关闭时应释放DB
        mgr.closeDB();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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
                    setText();

                } else {
                    showTest(userInfo.getMsg());
                }

            }
        });
        addSubscription(sbGetCode);
    }

    private void setText() {
        Log.e("head:",mUserInfo.getHeadimg()+"");
        if (mUserInfo.getHeadimg() != null) {
            ImageLoader.getInstance().displayImage(mUserInfo.getHeadimg(), mIvSeeFHead, ImageUtil.getInstance().getCircleDisplayOption());

        }

        mTvSeeFAddName.setText(mUserInfo.getUser_name() + "");
        mTvSeeFInfoName.setText(mUserInfo.getUser_name() + "");
        mTvSeeFId.setText(mUserInfo.getId() + "");
        if (!TextUtils.isEmpty(mUserInfo.getWechat())) {
            mTvSeeFwxid.setText(mUserInfo.getWechat() + "");
        }
        if (!TextUtils.isEmpty(mUserInfo.getPhone())) {
            mTvSeeFPhone.setText(mUserInfo.getPhone() + "");
        }
        if (!TextUtils.isEmpty(mUserInfo.getMp_desc())) {
            mTvSeeFProduce.setText(mUserInfo.getMp_desc());
        }else{
            mTvSeeFProduce.setText("这个家伙太懒了，什么都没留下！");
        }
        if (TextUtils.isEmpty(mUserInfo.getWx_province())) {
            mUserInfo.setWx_province("未知");
        }
        if (TextUtils.isEmpty(mUserInfo.getWx_city())) {
            mUserInfo.setWx_city("未知");
        }

        mTvcity.setText(mUserInfo.getWx_province() + " • " + mUserInfo.getWx_city() + " • ");

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
    }

    private android.app.AlertDialog alertDialog;

    @OnClick({R.id.rlSeeFBack, R.id.ivSeeFHead, R.id.llSeeFSendMessage, R.id.flSeeFReport, R.id.btnSeeFDeleteF, R.id.ivQrcode,R.id.ll_callphone,R.id.ll_dynamic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlSeeFBack:
                finish();
                break;
            case R.id.ivSeeFHead:
                initPpwWindow1();
                break;
            case R.id.llSeeFSendMessage:
                if (RongIM.getInstance() != null)
                    RongIM.getInstance().startPrivateChat(this, mUserInfo.getId() + "", mUserInfo.getUser_name() + "");
                break;
            case R.id.flSeeFReport:
                Intent i = new Intent(SeePersonalInfoIsFriendActivity.this, ReportActivity.class);
                i.putExtra("id", id);
                startActivity(i);

                break;
            case R.id.btnSeeFDeleteF:

                alertDialog = UIUtils.getAlertDialog(this, null, "确定删除好友吗？", "取消", "确定", 0, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();


                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        delete(Integer.parseInt(id));
                    }
                });
                alertDialog.show();


                break;
            case R.id.ivQrcode:


                Intent intentid = new Intent(SeePersonalInfoIsFriendActivity.this, UserInfoQRCode.class);
                intentid.putExtra("id", Integer.parseInt(id));

                startActivity(intentid);


                break;

            case R.id.ll_callphone:
                if(!TextUtils.isEmpty(mUserInfo.getPhone())) {
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

    private void initPpwindow2() {
        View view = LayoutInflater.from(this).inflate(R.layout.pw_callphone,null);
        final PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,true);
        view.findViewById(R.id.btnCallphone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + mUserInfo.getPhone());
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

    private void delete(final int targetId) {

        mLoadingAlertDialog.show();
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().deleteFriend((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),targetId)).subscribe(new Subscriber<Code>() {
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
            public void onNext(Code code) {
                if (code.getState().equals("1")) {//
                    showTest("删除成功");
                    UserInfoRong userInfoRong = new UserInfoRong();
                    userInfoRong.id = targetId;


                    mgr.deleteOldUserInfo(userInfoRong);

                    RongIM.getInstance().removeConversation(Conversation.ConversationType.PRIVATE, String.valueOf(targetId), new RongIMClient.ResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {

                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
                    RongIM.getInstance().clearMessages(Conversation.ConversationType.PRIVATE, String.valueOf(targetId), new RongIMClient.ResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                        }
                    });
                    MztRongContext.getInstance().popAllActivity(1);
                    finish();
                } else {
                    showTest(code.getMsg());
                }
            }
        });
        addSubscription(sbMyAccount);
    }


    private PopupWindow mOptImgPopWindow1;

    private void initPpwWindow1() {
        //设置contentView
        View contentView = UIUtils.inflate(SeePersonalInfoIsFriendActivity.this, R.layout.ppw_toast);
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

        mOptImgPopWindow1.showAtLocation(mIvSeeFHead, Gravity.BOTTOM, 0, 0);
    }

}
