package com.nevermore.muzhitui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nevermore.muzhitui.DashDetailActivity;
import com.nevermore.muzhitui.EventBusContanct;
import com.nevermore.muzhitui.MainActivity;
import com.nevermore.muzhitui.MyKehuActivity;
import com.nevermore.muzhitui.MyProxy;

import com.nevermore.muzhitui.MyWinActivity;
import com.nevermore.muzhitui.QrActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.SettingActivity;

import com.nevermore.muzhitui.activity.ConnectWeActivity;
import com.nevermore.muzhitui.activity.GFActivity;
import com.nevermore.muzhitui.activity.MyDynamicActivity;
import com.nevermore.muzhitui.activity.MyWorksActivity;
import com.nevermore.muzhitui.activity.PersonalInfoActivity;
import com.nevermore.muzhitui.activity.PopularizeActivity;
import com.nevermore.muzhitui.activity.QrHomeActivity;
import com.nevermore.muzhitui.event.DataRefreshEvent;
import com.nevermore.muzhitui.event.EditInfoEvent;
import com.nevermore.muzhitui.event.PaySuccessEvent;
import com.nevermore.muzhitui.module.bean.BaseBean;
import com.nevermore.muzhitui.module.bean.MyAccount;
import com.nevermore.muzhitui.module.bean.MyOrder;
import com.nevermore.muzhitui.module.bean.WxPay;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nevermore.muzhitui.module.service.NetWorkService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelpay.PayReq;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

import base.BaseActivityTwoV;
import base.BaseFragment;
import base.ImageUtil;
import base.MyLogger;
import base.SPUtils;
import base.UIUtils;
import base.helper.PayHelp;
import base.network.RetrofitUtil;
import base.view.DragPointView;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by hehe on 2016/5/1.
 *  我的模块的Fragment
 */
public class TabMyFragment extends BaseFragment {
    @BindView(R.id.ivHead)
    ImageView mIvHead;
    @BindView(R.id.tvName)
    TextView mTvName;
    @BindView(R.id.tvMoney)
    TextView mTvMoney;
    @BindView(R.id.tvMemberType)
    TextView mTvMemberType;
    @BindView(R.id.tvWinNum)
    TextView mTvWinNum;
    @BindView(R.id.ivHeadQr)
    ImageView mIvHeadQr;
    @BindView(R.id.llytMy)
    LinearLayout mLlytMy;
    @BindView(R.id.tvPrixyType)
    TextView tvPrixyType;
    @BindView(R.id.tvDynamicNum)
    TextView tvDynamicNum;
    @BindView(R.id.tv_guanzhu)
    TextView tv_guanzhu;
    @BindView(R.id.tv_fensi)
    TextView tv_fensi;
    @BindView(R.id.civ_guan)
    TextView civ_guan;
    @BindView(R.id.civ_fen)
    TextView civ_fen;
    @BindView(R.id.tv_prexy)
    TextView tv_prexy;

    private EditText mEtMoney;
    private EditText mEtTele;
    private EditText mEtCard;
    private TextView mTvBalance;
    private LoadingAlertDialog mLoadingAlertDialog;
    private CheckBox mCbAgree;
    private RadioGroup mRgBuy;
    private int mBuyType = 1;
    private String mCard;
    public static boolean mIsBuy;
    private MyAccount myAccount;

    @Override
    public int createSuccessView() {
        EventBus.getDefault().register(this);
        return R.layout.fragment_my;
    }


    private PopupWindow mPpwMenu;
    private PopupWindow mPpwMenuDash;
    private PopupWindow mPpwMenuDashInfo;

    private void initMenuPpwWindow() {
        final ClipboardManager myClipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        //设置contentView
        View contentView = UIUtils.inflate(getActivity(), R.layout.ppw_buy);
        mPpwMenu = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPpwMenu.setContentView(contentView);
        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setColor(Color.parseColor("#80000000"));
        mPpwMenu.setBackgroundDrawable(colorDrawable);
        mPpwMenu.setOutsideTouchable(true);
        //设置各个控件的点击响应
        contentView.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPpwMenu.dismiss();
            }
        });

        View view = contentView.findViewById(R.id.ll_kami);
        ClipData mClipData = myClipboard.getPrimaryClip();
        //获取到内容
        if(mClipData != null) {
            ClipData.Item item = mClipData.getItemAt(0);
            String text = item.getText().toString();
            if (!TextUtils.isEmpty(text)) {
                view.setVisibility(View.VISIBLE);
            }else {
                view.setVisibility(View.GONE);
            }
        }
        final TextView tvMemberPayMoney = (TextView) contentView.findViewById(R.id.tvMemberPayMoney);
        contentView.findViewById(R.id.tvBuy).setOnClickListener(new View.OnClickListener() {   //购买会员
            @Override
            public void onClick(View v) {
                buy();
            }
        });
        mCbAgree = (CheckBox) contentView.findViewById(R.id.cbAgree);
        mRgBuy = (RadioGroup) contentView.findViewById(R.id.rgBuy);
        mEtCard = (EditText) contentView.findViewById(R.id.etCard);
        TextView tvID = (TextView) contentView.findViewById(R.id.tvTuijianId);


        final TextView clip = (TextView) contentView.findViewById(R.id.clip);

        if (myClipboard != null) {

            clip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //GET贴板是否有内容
                    ClipData mClipData = myClipboard.getPrimaryClip();
                    //获取到内容
                    if(mClipData != null) {
                        ClipData.Item item = mClipData.getItemAt(0);
                        String text = item.getText().toString();
                        if (!TextUtils.isEmpty(text)) {
                            mEtCard.setText(text);
//                    mEtCard.setText(myClipboard.getText().toString());
                            mEtCard.setSelection(mEtCard.length());
                        }
                    }

                }
            });
        }
        tvID.setText("" + MainActivity.tjLoginId);
        if (mBuyType == 1) {
            setText("298", tvMemberPayMoney);
        }
        mRgBuy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbYear:
                        mBuyType = 1;
                        setText("298", tvMemberPayMoney);
                        break;
                    case R.id.rblife:
                        setText("894", tvMemberPayMoney);
                        mBuyType = 2;
                        break;
                }
            }
        });
    }

    private void setText(String text, TextView textView) {
        ColorStateList blueColors = ColorStateList.valueOf(getResources().getColor(R.color.orangered));
        SpannableStringBuilder spanBuilder1 = new SpannableStringBuilder(text + " 元");
        spanBuilder1.setSpan(new TextAppearanceSpan(null, 0, 32, blueColors, null), 0, (String.valueOf(text).length()), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanBuilder1.setSpan(new UnderlineSpan(), 0, (String.valueOf(text).length()), 0);
        textView.setText(spanBuilder1);
    }

    private void initMenuPpwWindowDash() {
        //设置contentView
        View contentView = UIUtils.inflate(getActivity(), R.layout.ppw_dash);
        mPpwMenuDash = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPpwMenuDash.setContentView(contentView);
        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setColor(Color.parseColor("#80000000"));
        mPpwMenuDash.setBackgroundDrawable(colorDrawable);
        mPpwMenuDash.setOutsideTouchable(true);
        //设置各个控件的点击响应
        contentView.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPpwMenuDash.dismiss();
            }
        });
        contentView.findViewById(R.id.tvDash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ensure();
            }
        });
        contentView.findViewById(R.id.tvDashDetail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseStartActivity(DashDetailActivity.class);
            }
        });
        mEtMoney = (EditText) contentView.findViewById(R.id.etMoney);
        mEtTele = (EditText) contentView.findViewById(R.id.etTele);
        mEtTele.setText((String) SPUtils.get(SPUtils.KEY_PHONE_NUMBER, ""));
        mTvBalance = (TextView) contentView.findViewById(R.id.tvBalance);

    }

    TextView tvDashInfoMoney;

    private void initMenuPpwWindowDashInfo() {
        //设置contentView
        View contentView = UIUtils.inflate(getActivity(), R.layout.ppw_dush_info);
        mPpwMenuDashInfo = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPpwMenuDashInfo.setContentView(contentView);
        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setColor(Color.parseColor("#80000000"));
        mPpwMenuDashInfo.setBackgroundDrawable(colorDrawable);
        mPpwMenuDashInfo.setOutsideTouchable(true);
        //设置各个控件的点击响应
        contentView.findViewById(R.id.ivDashInfoClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPpwMenuDashInfo.dismiss();
            }
        });
        tvDashInfoMoney = (TextView) contentView.findViewById(R.id.tvDashInfoMoney);


    }

    private android.app.AlertDialog alertDialog;

    private void ensure() {
        String money = mEtMoney.getText().toString();

        final String tele = mEtTele.getText().toString();
        if (money.isEmpty()) {
            showTest("请填写金额", mEtMoney);
            return;
        } else {
            if (money.indexOf(".") != -1) {
                if (money.length() - money.indexOf(".") > 3) {
                    showTest("小数点后不能超过两位", mEtMoney);
                    return;
                }
            }
        }
        if (tele.isEmpty()) {
            showTest("请填写手机号码", mEtTele);
            return;
        }
        final float dashMoney = Float.valueOf(money);

        if (dashMoney < 100) {
            showTest("大于100元才能提交", mEtTele);
            return;
        }
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEtTele.getWindowToken(), 0);
        alertDialog = UIUtils.getAlertDialog(getActivity(), "确定提现？", "提现金额： " + mEtMoney.getText().toString() + " 元" + "\n手机号码： " + mEtTele.getText().toString(), "取消", "确定", 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();


            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dash(dashMoney, tele);

            }
        });
        alertDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLoadingAlertDialog = new LoadingAlertDialog(getActivity());
        loadData();
        initMenuPpwWindow();
        initMenuPpwWindowDash();
        initMenuPpwWindowDashInfo();
        if (mIsBuy) {
            displayBuyDialog();
        }
    }

    //1.年费 2.无限 3.未加入
    private void loadData() {
        MyLogger.kLog().e("------------------");
        mLoadingAlertDialog.show();
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().myAccount((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""))).subscribe(new Subscriber<MyAccount>() {
            @Override
            public void onCompleted() {
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mLoadingAlertDialog.dismiss();
                Log.e("myAccount:", e.toString());
                showTest(mNetWorkError + "我的账户信息请求出错");
            }

            @Override
            public void onNext(MyAccount myAccount) {
                if ("1".equals(myAccount.getState())) {
                    initinfo(myAccount);
                } else {
                    showTest(mServerEror + "  用户信息查询失败，请重新登录");
                }
            }
        });
        ((BaseActivityTwoV) getActivity()).addSubscription(sbMyAccount);
    }

    private void initinfo(MyAccount myAccount) {
        this.myAccount = myAccount;
        ImageLoader.getInstance().displayImage(myAccount.getHeadimg(), mIvHead, ImageUtil.getInstance().getCircleDisplayOption());
        SPUtils.put(SPUtils.KEY_TOPIC,myAccount.getHeadimg().replace(RetrofitUtil.API_URL+RetrofitUtil.PROJECT_URL,""));
        if(myAccount.count_dt_unread > 0){
            tvDynamicNum.setVisibility(View.VISIBLE);
            tvDynamicNum.setText(""+myAccount.count_dt_unread);
        }else {
            tvDynamicNum.setVisibility(View.INVISIBLE);
        }

        if(myAccount.count_att_add != 0){
            civ_guan.setText("+"+myAccount.count_att_add);
            civ_guan.setVisibility(View.VISIBLE);
        }else {
            civ_guan.setVisibility(View.GONE);
        }

        if(myAccount.count_fans_add != 0){
            civ_fen.setText("+"+myAccount.count_fans_add);
            civ_fen.setVisibility(View.VISIBLE);
        }else {
            civ_fen.setVisibility(View.GONE);
        }
        if(myAccount.is_agent == 0){
            tv_prexy.setText("申请代理");
        }else {
            tv_prexy.setText("我的代理账户");
        }
        tv_fensi.setText("粉丝("+myAccount.count_fans+")");
        tv_guanzhu.setText("关注("+myAccount.count_attention+")");
        Log.e("myAccount head:", myAccount.getHeadimg() + "");
        if (myAccount.getUser_name() != null) {
            mTvName.setText(myAccount.getUser_name() + " (ID:" + myAccount.getId() + ")");
            SPUtils.put(SPUtils.KEY_USERNAME, myAccount.getUser_name());
            Log.e("myAccount username:", myAccount.getUser_name() + "");
        } else {
            mTvName.setText(myAccount.getWechatname() + " (ID:" + myAccount.getId() + ")");
            SPUtils.put(SPUtils.KEY_USERNAME, myAccount.getWechatname());
        }
        Log.e("myAccount wechatname:", myAccount.getWechatname() + "");
        mTvMoney.setText("财富数量:" + myAccount.getNewwallet() + "元");
        SPUtils.put(SPUtils.KEY_MEMBER_STATE, myAccount.getAgtype());//保存会员状态
        SPUtils.put(SPUtils.KEY_ISEXPIRE, myAccount.getIsExpire());
        Log.e("myAccount 会员状态:", myAccount.getAgtype()+ "\t是否过期："+myAccount.getIsExpire());
        RongIM.getInstance().setCurrentUserInfo(new UserInfo(myAccount.getId() + "",
                myAccount.getUser_name() == null ? null : myAccount.getUser_name(), myAccount.getHeadimg() == null ? null : Uri.parse(myAccount.getHeadimg())));
        //刷新当前用户的好友信息
        RongIM.getInstance().refreshUserInfoCache(new UserInfo(myAccount.getId() + "",
                myAccount.getUser_name() == null ? null : myAccount.getUser_name(), myAccount.getHeadimg() == null ? null : Uri.parse(myAccount.getHeadimg())));
        Log.e("myAccount.getAgtype():", myAccount.getAgtype() + "");
        switch (myAccount.getAgtype()) {
            case 1:
                Log.e("myAccount join date:", myAccount.getJoindate() + "");
                if (!TextUtils.isEmpty(myAccount.getJoindate())) {
                    if (myAccount.getIsExpire() == 0) {//0正常会员 1过期会员
                        String messge = "年费会员：有效期至 " + UIUtils.getSubOneYear(myAccount.getJoindate().substring(0, 10)) + " 日";
                        mTvMemberType.setText(Html.fromHtml(messge));
                    } else if (myAccount.getIsExpire() == 1) {
                        String messge = "年费会员<font color='red'>（已过期）</font> 有效期至 " + UIUtils.getSubOneYear(myAccount.getJoindate().substring(0, 10)) + " 日";
                        mTvMemberType.setText(Html.fromHtml(messge));
                    }
                } else {
                    mTvMemberType.setText("年费会员：有效期至 ");
                }
                break;
            case 2:
                mTvMemberType.setText("终生会员");
                break;
            case 3:
                mTvMemberType.setText("未加入会员");
                break;
        }
        MainActivity.loginId = myAccount.getId();
        mTvBalance.setText("您的余额：" + myAccount.getNewwallet());
        ColorStateList blueColors = ColorStateList.valueOf(0xff2192f2);//蓝色
        SpannableStringBuilder spanBuilder = new SpannableStringBuilder("您的余额:" + myAccount.getNewwallet() + "元");
        spanBuilder.setSpan(new TextAppearanceSpan(null, 0, 0, blueColors, null), 5, 5 + (String.valueOf(myAccount.getNewwallet()).length()), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        mTvBalance.setText(spanBuilder);
        if(myAccount.getProfitCount() > 0) {
            mTvWinNum.setVisibility(View.VISIBLE);
            mTvWinNum.setText(myAccount.getProfitCount() + "");
        }else {
            mTvWinNum.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(myAccount.getPathimg())) {
            String path = RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + myAccount.getPathimg();
            Log.e("myAccount path:", path);

            ImageLoader.getInstance().displayImage(path, mIvHeadQr, ImageUtil.getInstance().getBaseDisplayOption());
        }
        mEtMoney.setHint(myAccount.getNewwallet() + "");
        mEtMoney.requestFocus();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventEMMessage(DataRefreshEvent dataRefreshEvent) {
        loadData();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventEMMessage(EventBusContanct contanct) {
        if(contanct.getFlag() == EventBusContanct.MYDYNAMICACTIVITY_DYNAMIC_MSG_STATE){
            tvDynamicNum.setVisibility(View.GONE);
        }
    }



    android.app.AlertDialog dialog = null;

    private void buy() {
        if (!mCbAgree.isChecked()) {
            showTest("请同意《拇指推广告系统购买协议》");
            return;
        }
        mLoadingAlertDialog.show();
        Observable observable = null;
        mCard = mEtCard.getText().toString();
        Logger.i(mBuyType + "   " + mCard);
        if (TextUtils.isEmpty(mCard)) {
            mCard = "";
        }
        observable = wrapObserverWithHttp(WorkService.getWorkService().addOrder((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""), mBuyType, mCard).flatMap(new Func1<MyOrder, Observable<WxPay>>() {
            @Override
            public Observable<WxPay> call(MyOrder myOrder) {
                Log.i("TAG",myOrder.toString());
                switch (myOrder.getState()) {
                    case 0:
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog = UIUtils.getAlertDialog(getActivity(), "提示信息", "您不是卡密提供者的下级用户， 无法通过卡密购买会员", null, "确定", 0, null, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(dialog != null && dialog.isShowing())
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
//                                Toast.makeText(getContext(), "您不是卡密提供者的下级用户， 无法通过卡密购买会员", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case 1:
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "已成为当前会员或不能购买当前会员", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case 2:

                        return WorkService.getWorkService().toAndroidPay((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""), myOrder.getAgordernum());
                    case 3:
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "升级终生会员，你只需支付差价", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return WorkService.getWorkService().toAndroidPay((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""), myOrder.getAgordernum());
                    case 4:
                        return WorkService.getWorkService().toAndroidPay((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""), myOrder.getAgordernum());
                    case 5:
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "卡密不可用", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case 6:
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "卡密库存不足", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case 7:
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "复购会员不可使用卡密", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                }
                return null;

            }
        }));
        Subscription sbMyAccount = observable.subscribe(new Subscriber<WxPay>() {
            @Override
            public void onCompleted() {
                Log.i("TAG","++++++++++++++++++++");
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mLoadingAlertDialog.dismiss();
                showTest(mNetWorkError + "微信支付错误");
                Log.i("TAG","-------------");
            }

            @Override
            public void onNext(WxPay baseBean) {
                Log.i("TAG",baseBean.toString());
//                Toast.makeText(getActivity(),baseBean.toString(),Toast.LENGTH_LONG).show();
                if (baseBean.getState() == 1) {
                    if (UIUtils.isWXAppInstalledAndSupported()) {
                        PayReq payReq = new PayReq();
                        payReq.appId = baseBean.getSignParams().getAppid();
                        payReq.nonceStr = baseBean.getSignParams().getNoncestr();
                        payReq.packageValue = baseBean.getSignParams().getPackageX();
                        payReq.partnerId = baseBean.getSignParams().getPartnerid();
                        payReq.prepayId = baseBean.getSignParams().getPrepayid();
                        payReq.sign = baseBean.getSignParams().getSign();
                        payReq.timeStamp = baseBean.getSignParams().getTimestamp();
                        PayHelp.getInstance().wxPay(((MainActivity) getActivity()), payReq);
                    } else {
                        showTest("您未安装微信");
                    }
                } else {
                    showTest(mServerEror);
                }
            }
        });
        ((BaseActivityTwoV) getActivity()).addSubscription(sbMyAccount);
    }

    @Subscribe
    public void onEventEMMessage(PaySuccessEvent paySuccessEvent) {

        showTest("支付成功");
        mPpwMenu.dismiss();
        if (mBuyType == 1) {
            mTvMemberType.setText("年费会员：有效期至" + UIUtils.addOneYear(new Date()) + "日");
        } else if (mBuyType == 2) {
            mTvMemberType.setText("终生会员");
        }
       // loadData();//刷新用户信息，避免用户分享时出错 1 年费 2终身，3不是会员
        SPUtils.put(SPUtils.KEY_MEMBER_STATE, mBuyType);//保存会员状态
        SPUtils.put(SPUtils.KEY_ISEXPIRE, 0);//
    }

    private float money;

    private void dash(float dashMoney, String tele) {
        money = dashMoney;
        mLoadingAlertDialog.show();
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().bill((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""), dashMoney, tele, "")).subscribe(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {
                mLoadingAlertDialog.dismiss();
                alertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                mLoadingAlertDialog.dismiss();
                showTest(mNetWorkError + "提现错误", mEtMoney);
                e.printStackTrace();
            }

            @Override
            public void onNext(BaseBean state) {
                switch (state.getState()) {
                    case 2:
                        showTest("余额不足", mEtMoney);
                        break;
                    case 1:
                        mPpwMenuDash.dismiss();
                        Log.e("money", money + "");
                        tvDashInfoMoney.setText("提现金额：" + money + "元");
                        mPpwMenuDashInfo.showAtLocation(mLlytMy, Gravity.BOTTOM, 0, 0);
                        loadData();//提现成功刷新用户信息
                        break;
                    case 0:
                        showTest(mServerEror, mEtMoney);
                        break;
                    case 3:
                        showTest("非会员或过期会员无法提现。");
                        break;
                }

            }
        });
        ((BaseActivityTwoV) getActivity()).addSubscription(sbMyAccount);
    }

    @OnClick({R.id.ll_Dash, R.id.ll_buy, R.id.flytZuoPing,R.id.flytWin, R.id.flytQr, R.id.flytSetting, R.id.flytConnect, R.id.ivHead, R.id.ivHeadQr, R.id.flytProxy, R.id.flytDynamic,R.id.rel_guanzhu,R.id.ll_fensi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_Dash:     //提现
                mPpwMenuDash.showAtLocation(mLlytMy, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.ll_buy:        //购买会员
                displayBuyDialog();
                break;
            case R.id.flytZuoPing:   //我的作品

                baseStartActivity(MyWorksActivity.class);
                break;
            case R.id.flytWin:    //我的收益
                baseStartActivity(MyWinActivity.class);
            clearWin();
                break;

            case R.id.flytQr:     //推广中心
                baseStartActivity(PopularizeActivity.class);
                break;
            case R.id.flytSetting:   //设置
                baseStartActivity(SettingActivity.class);
                break;
            case R.id.flytConnect:   //联系我们
                baseStartActivity(ConnectWeActivity.class);
                break;
            case R.id.ivHead:        //用户信息  头像
                // baseStartActivity(MySettingActivity.class);
                baseStartActivity(PersonalInfoActivity.class);
                break;
            case R.id.ivHeadQr:    //联系我们下面的二维码图片
                baseStartActivity(PopularizeActivity.class);
                break;
            case R.id.flytProxy:   //我的代理
                baseStartActivity(MyProxy.class);
                break;
            case R.id.flytDynamic:   //我的动态

                baseStartActivity(MyDynamicActivity.class);
                break;
            case R.id.rel_guanzhu:     //关注  粉丝
            case R.id.ll_fensi:
                int menuStatus = 0;
                if(view.getId() == R.id.ll_fensi){
                    menuStatus = 1;
                    if (myAccount.count_fans_add > 0)
                        //清除粉丝近期数量
                        ((BaseActivityTwoV)getActivity()).addSubscription(wrapObserverWithHttp(WorkService.getWorkService().clearCountFan((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))).subscribe(new Subscriber<com.nevermore.muzhitui.module.BaseBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(com.nevermore.muzhitui.module.BaseBean baseBean) {
                                if("1".equals(baseBean.state)){
                                    civ_fen.setVisibility(View.GONE);
                                }
                            }
                        }));
                }else {
                    if (myAccount.count_att_add > 0)
                        //清除关注近期数量
                        ((BaseActivityTwoV)getActivity()).addSubscription(wrapObserverWithHttp(WorkService.getWorkService().clearAttCount((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))).subscribe(new Subscriber<com.nevermore.muzhitui.module.BaseBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(com.nevermore.muzhitui.module.BaseBean baseBean) {
                                if("1".equals(baseBean.state)){
                                    civ_guan.setVisibility(View.GONE);
                                }
                            }
                        }));
                }
                Intent intent = new Intent(getActivity(), GFActivity.class);
                intent.putExtra(GFActivity.MENU_STATE,menuStatus);
                intent.putExtra(GFActivity.USER_ID,(String)SPUtils.get(SPUtils.GET_LOGIN_ID,""));
                startActivity(intent);
                break;

        }
    }

    private void clearWin() {
        ((BaseActivityTwoV)getActivity()).addSubscription(wrapObserverWithHttp(WorkService.getWorkService().clearCountProfit((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))).subscribe(new Subscriber<com.nevermore.muzhitui.module.BaseBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(com.nevermore.muzhitui.module.BaseBean baseBean) {
               MyLogger.kLog().e(baseBean.toString());
            }
        }));
    }


    public void displayBuyDialog() {
        mPpwMenu.showAtLocation(mLlytMy, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
