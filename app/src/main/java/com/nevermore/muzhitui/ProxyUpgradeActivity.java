package com.nevermore.muzhitui;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nevermore.muzhitui.module.bean.BuyProxyResult;
import com.nevermore.muzhitui.module.bean.UpgradeProxy;
import com.nevermore.muzhitui.module.network.WorkService;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import rx.Subscriber;
import rx.Subscription;

public class ProxyUpgradeActivity extends BaseActivityTwoV {

    private LoadingAlertDialog mLoadingAlertDialog;
    @BindView(R.id.tvPay)
    TextView mTvPay;

    @BindView(R.id.cbAgress)
    CheckBox mCbAgress;
    @BindView(R.id.rbA)
    RadioButton mCbA;
    @BindView(R.id.rbB)
    RadioButton mCbB;
    @BindView(R.id.rbC)
    RadioButton mCbC;
    @BindView(R.id.rgProxy)
    RadioGroup mRgProxy;

    @BindView(R.id.tvTip)
    TextView mTvTip;

    @BindView(R.id.tvSupplyMoney)
    TextView mTvSupplyMoney;

    private UpgradeProxy mUpgradeProxy;
    private UpgradeProxy.TypeListBean mTypeListBean;

    @Override
    public void init() {
        setMyTitle("代理升级");
        showBack();
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        upgradeProxy();
        mTvPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCbAgress.isChecked()) {
                    showTest("请阅读并同意《拇指推代理协议》");
                    return;
                }
                if(mTypeListBean != null)
                    upBuy(mTypeListBean.getId());
            }
        });
        mRgProxy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbA:
                        if (mUpgradeProxy != null) {
                            mTvTip.setText(String.format("您将多获得%d个库存,升级代理只需补齐差价", mUpgradeProxy.getTypeList().get(0).getCount()));
                            mTvSupplyMoney.setText("应付金额：" + mUpgradeProxy.getTypeList().get(0).getAmount() + "元");
                            mTypeListBean = mUpgradeProxy.getTypeList().get(0);
                        }
                        break;
                    case R.id.rbB:
                        if (mUpgradeProxy != null) {
                            mTvTip.setText(String.format("您将多获得%d个库存,升级代理只需补齐差价", mUpgradeProxy.getTypeList().get(1).getCount()));
                            mTvSupplyMoney.setText("应付金额：" + mUpgradeProxy.getTypeList().get(1).getAmount() + "元");
                            mTypeListBean = mUpgradeProxy.getTypeList().get(1);
                        }
                        break;
                    case R.id.rbC:
                        if (mUpgradeProxy != null) {
                            mTvTip.setText(String.format("您将多获得%d个库存,升级代理只需补齐差价", mUpgradeProxy.getTypeList().get(2).getCount()));
                            mTvSupplyMoney.setText("应付金额：" + mUpgradeProxy.getTypeList().get(2).getAmount() + "元");
                            mTypeListBean = mUpgradeProxy.getTypeList().get(2);
                        }
                        break;
                }
            }
        });
    }


    private void upBuy(int id) {
        mLoadingAlertDialog.show();
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().upBuy((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),id)).subscribe(new Subscriber<BuyProxyResult>() {
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
            public void onNext(BuyProxyResult buyProxyResult) {
                if ("1".equals(buyProxyResult.getState())) {
                    Intent intent = new Intent(ProxyUpgradeActivity.this, PayProxyActivity.class);
                    intent.putExtra(PayProxyActivity.PAYINFO, mTypeListBean);
                    intent.putExtra(PayProxyActivity.ORDERNUM, buyProxyResult.getOrderNo());
                    startActivity(intent);
                    finish();
                } else {
                    showTest(mServerEror);
                }

            }
        });
        addSubscription(sbMyAccount);
    }

    private void upgradeProxy() {
        mLoadingAlertDialog.show();
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().upApply((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))).subscribe(new Subscriber<UpgradeProxy>() {
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
            public void onNext(UpgradeProxy upgradeProxy) {
                if ("1".equals(upgradeProxy.getState())) {
                    mUpgradeProxy = upgradeProxy;
                    mTypeListBean = mUpgradeProxy.getTypeList().get(0);
                    mTvTip.setText(String.format("您将多获得%d个库存,升级代理只需补齐差价", mUpgradeProxy.getTypeList().get(0).getCount()));
                    mTvSupplyMoney.append(mUpgradeProxy.getTypeList().get(0).getAmount() + "元");
                    mCbA.setText(String.format("A级代理：%d张*%d元/张=%d元，10%%无限提成", upgradeProxy.getTypeList().get(0).getCount(), upgradeProxy.getTypeList().get(0).getPrice(), upgradeProxy.getTypeList().get(0).getAmount()));
                    mCbB.setText(String.format("B级代理：%d张*%d元/张=%d元，10%%无限提成", upgradeProxy.getTypeList().get(1).getCount(), upgradeProxy.getTypeList().get(1).getPrice(), upgradeProxy.getTypeList().get(1).getAmount()));
                    mCbC.setText(String.format("C级代理：%d张*%d元/张=%d元，10%%无限提成", upgradeProxy.getTypeList().get(2).getCount(), upgradeProxy.getTypeList().get(2).getPrice(), upgradeProxy.getTypeList().get(2).getAmount()));
                } else {
                    showTest(mServerEror);
                }

            }
        });
        addSubscription(sbMyAccount);
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_proxyupgrade;
    }
}
