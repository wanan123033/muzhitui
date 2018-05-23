package com.nevermore.muzhitui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aigestudio.wheelpicker.core.AbstractWheelPicker;
import com.aigestudio.wheelpicker.view.WheelCurvedPicker;
import com.nevermore.muzhitui.activity.MztProxyProtocolActivity;
import com.nevermore.muzhitui.module.bean.BuyProxyResult;
import com.nevermore.muzhitui.module.bean.ProxyType;
import com.nevermore.muzhitui.module.bean.UpgradeProxy;
import com.nevermore.muzhitui.module.network.WorkService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.UIUtils;
import base.view.LoadingAlertDialog;
import base.view.SingleChoicePopWindow;
import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.widget.SingleChoiceAdapter;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by hehe on 2016/6/7.
 */
public class BuyProxyActivity extends BaseActivityTwoV {
    @BindView(R.id.tvMoney)
    TextView mTvMoney;
    @BindView(R.id.tvDes)
    TextView mTvDes;
    @BindView(R.id.tvProxyLevel)
    TextView mTvProxyLevel;
    @BindView(R.id.etRecommend)
    EditText mEtRecommend;
    @BindView(R.id.etName)
    EditText mEtName;
    @BindView(R.id.flytName)
    FrameLayout mFlytName;
    @BindView(R.id.etTele)
    EditText mEtTele;
    @BindView(R.id.cbAgress)
    CheckBox mCbAgress;
    @BindView(R.id.tvNowPay)
    TextView mTvNowPay;
    @BindView(R.id.llyt)
    LinearLayout mLlyt;
    @BindView(R.id.etProvince)
    EditText mEtProvince;
    @BindView(R.id.etCity)
    EditText mEtCity;
    @BindView(R.id.rootView)
    ScrollView rootView;
    private LoadingAlertDialog mLoadingAlertDialog;
    private int mProxyLevel = 3;
    List<String> mLtStr = new ArrayList<>();
    final List<ProxyType.TypeListBean> mList = new ArrayList<>();
    private UpgradeProxy.TypeListBean mProxy;
    private int Currentindex;

    @Override
    public void init() {
        setMyTitle("购买代理");
        showBack();
        mCbAgress.setChecked(true);
        initPopWindow();
//        mWcP.setOnWheelChangeListener(new AbstractWheelPicker.SimpleWheelChangeListener() {
//            @Override
//            public void onWheelSelected(int index, String data) {
//                super.onWheelSelected(index, data);
//                mProxyLevel = mList.get(index).getId();
////                mTvProxyLevel.setText(mList.get(index).getName());
////                mTvMoney.setText(mList.get(index).getAmount() + "元");
////                mTvDes.setText(mList.get(index).getCount()+"个账号"+mList.get(index).getPrice()+"元/账号");
////                mTypeListBean = mList.get(index);
//
//            }
//        });
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        loadProxy();
        mTypeListBean = mList.get(0);
        SpannableStringBuilder builder = new SpannableStringBuilder("我已阅读《拇指推代理协议》");
        builder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                baseStartActivity(MztProxyProtocolActivity.class);
            }
        },5,12, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        mCbAgress.setText(builder);
        mCbAgress.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_buy_proxy;
    }


    @OnClick({R.id.tvProxyLevel, R.id.tvNowPay, R.id.llyt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvProxyLevel:
                initMenuPpwWindow(Currentindex);
                break;
            case R.id.tvNowPay:
                buyProxy();
                break;
            case R.id.llyt:
                break;
        }
    }
    Dialog dialog = null;
    private void buyProxy() {
        final String name = mEtName.getText().toString();
        final String tele = mEtTele.getText().toString();
        final String province = mEtProvince.getText().toString();
        final String city = mEtCity.getText().toString();
        final String recommendId = mEtRecommend.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(tele) || TextUtils.isEmpty(province) || TextUtils.isEmpty(city)) {
            showTest("请完善信息");
            return;
        }
        if (!mCbAgress.isChecked()) {
            showTest("请阅读并同意《拇指推代理协议》");
            return;
        }
        dialog = UIUtils.getAlertDialog(this, "提示信息", "您的申请已提交，工作人员会尽快联系您！", null, "确定", 0, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                load(recommendId,name,tele,province,city);
            }
        });
        dialog.show();

    }

    private ProxyType.TypeListBean mTypeListBean;

    private void loadProxy() {
        ProxyType.TypeListBean cbean = new ProxyType.TypeListBean();
        cbean.setName("C级代理");
        cbean.setCount(25);
        cbean.setAmount(2980);
        cbean.setPrice(120);
        cbean.setId(3);
        cbean.setRebate(745);
        mList.add(cbean);
        ProxyType.TypeListBean bbean = new ProxyType.TypeListBean();
        bbean.setName("B级代理");
        bbean.setCount(120);
        bbean.setAmount(12000);
        bbean.setPrice(100);
        bbean.setId(2);
        bbean.setRebate(3000);
        mList.add(bbean);
        ProxyType.TypeListBean abean = new ProxyType.TypeListBean();
        abean.setName("A级代理");
        abean.setCount(300);
        abean.setAmount(24000);
        abean.setPrice(80);
        abean.setId(1);
        abean.setRebate(6000);
        mList.add(abean);
    }

    public void load(String recommendId,String name,String tele,String province,String city){
        mLoadingAlertDialog.show();
        Subscription sbMyAccount = null;
        if (TextUtils.isEmpty(recommendId)) {
            sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().buyApply((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),mProxyLevel, name, tele, province, city)).subscribe(new Subscriber<BuyProxyResult>() {
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
                public void onNext(BuyProxyResult baseBean) {
                    if (baseBean.getState().equals("1")) {
                        Intent intent = new Intent(BuyProxyActivity.this, PayProxyActivity.class);
                        intent.putExtra(PayProxyActivity.PAYINFO, mTypeListBean);
                        intent.putExtra(PayProxyActivity.ORDERNUM, baseBean.getOrderNo());
                        startActivity(intent);
                        finish();
                    } else {
                        showTest(mNetWorkError);
                    }
                }
            });
        } else {
            sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().buyApply((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),mProxyLevel, recommendId, name, tele, province, city)).subscribe(new Subscriber<BuyProxyResult>() {
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
                public void onNext(BuyProxyResult baseBean) {
                    final String state = baseBean.getState();
                    switch (state) {
                        case "1":
                            Intent intent = new Intent(BuyProxyActivity.this, PayProxyActivity.class);
                            intent.putExtra(PayProxyActivity.PAYINFO, mTypeListBean);
                            intent.putExtra(PayProxyActivity.ORDERNUM, baseBean.getOrderNo());
                            startActivity(intent);
                            finish();
                            break;
                        case "2":
                            showTest("不存在该推荐人");
                            break;
                        case "3":
                            showTest("该推荐人不是代理");
                            break;
                        case "4":
                            showTest("不存在该代理类型");
                            break;
                    }

                }
            });
        }


        addSubscription(sbMyAccount);
    }
    private void initMenuPpwWindow(int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(new String[]{"C级代理", "B级代理", "A级代理"}, index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Currentindex = i;
                mProxyLevel = mList.get(i).getId();
                mTvProxyLevel.setText(mList.get(i).getName());
                mTvMoney.setText(mList.get(i).getAmount() + "元");
                mTvDes.setText(mList.get(i).getCount()+"个账号"+mList.get(i).getPrice()+"元/账号");
                mTypeListBean = mList.get(i);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
    private SingleChoicePopWindow mSingleChoicePopWindow;
    public void showSingleChoiceWindow() {
        mSingleChoicePopWindow.show(true);
    }
    public void initPopWindow() {
        List<String> mSingleDataList = new ArrayList<>();
        mSingleDataList.add("C级代理");
        mSingleDataList.add("B级代理");
        mSingleDataList.add("A级代理");
        mSingleChoicePopWindow = new SingleChoicePopWindow(this, rootView,
                mSingleDataList);

        mSingleChoicePopWindow.setTitle("genius single title");
        mSingleChoicePopWindow.setOnOKButtonListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Currentindex = mSingleChoicePopWindow.getSelectItem();
                mProxyLevel = mList.get(Currentindex).getId();
                mTvProxyLevel.setText(mList.get(Currentindex).getName());
                mTvMoney.setText(mList.get(Currentindex).getAmount() + "元");
                mTvDes.setText(mList.get(Currentindex).getCount()+"个账号"+mList.get(Currentindex).getPrice()+"元/账号");
                mTypeListBean = mList.get(Currentindex);

            }
        });
    }

}
