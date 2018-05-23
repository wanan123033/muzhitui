package com.nevermore.muzhitui;

import android.view.View;
import android.widget.TextView;

import com.nevermore.muzhitui.event.PaySuccessEvent;
import com.nevermore.muzhitui.module.bean.ProxyType;
import com.nevermore.muzhitui.module.bean.UpgradeProxy;
import com.nevermore.muzhitui.module.bean.WxPay;
import com.nevermore.muzhitui.module.network.WorkService;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelpay.PayReq;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.UIUtils;
import base.helper.PayHelp;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

public class PayProxyActivity extends BaseActivityTwoV {


    @BindView(R.id.tvMoney)
    TextView mTvMoney;
    @BindView(R.id.tvDes)
    TextView mTvDes;
    @BindView(R.id.tvWxPay)
    TextView mTvWxPay;
    @BindView(R.id.tvMoneyPay)
    TextView mTvMoneyPay;
    public static final String PAYINFO = "PAYINFO";
    public static final String ORDERNUM = "ORDERNUM";
    private LoadingAlertDialog mLoadingAlertDialog;
    private String mOrderNum;

    private boolean mIsBuy;
    @Override
    public void init() {
        EventBus.getDefault().register(this);
        showBack();
        setMyTitle("支付");
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        Object object = getIntent().getSerializableExtra(PAYINFO);
        if (object != null) {
            if (object instanceof UpgradeProxy.TypeListBean) {
                UpgradeProxy.TypeListBean bean = (UpgradeProxy.TypeListBean) object;
                mTvMoney.setText("您申请的是" + bean.getName());
                mTvDes.setText("应付金额是" + bean.getAmount() + "元");
                mIsBuy = false;
            } else if (object instanceof ProxyType.TypeListBean) {
                ProxyType.TypeListBean bean = (ProxyType.TypeListBean) object;
                mTvMoney.setText("您申请的是" + bean.getName());
                mTvDes.setText("应付金额是" + bean.getAmount() + "元");
                mIsBuy = true;
            }
        }
        mOrderNum = getIntent().getStringExtra(ORDERNUM);

    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_pay_proxy;
    }


    @Subscribe
    public void onEventEMMessage(PaySuccessEvent paySuccessEvent) {
        //    Toast.makeText(UIUtils.getContext(), "支付成功", Toast.LENGTH_LONG).show();
        finish();
    }

    private void buy() {
        mLoadingAlertDialog.show();
        Observable observable = null;
        Logger.i("mOrderNum = " + mOrderNum);
        if (mIsBuy) {
            observable = wrapObserverWithHttp(WorkService.getWorkService().toAndroidPayProxy((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),mOrderNum));
        } else {
            observable = wrapObserverWithHttp(WorkService.getWorkService().toAndroidUploadProxy((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),mOrderNum));
        }


//        observable = wrapObserverWithHttp(WorkService.getWorkService().testtoAndroidPay("145860815040182"));
        Subscription sbMyAccount = observable.subscribe(new Subscriber<WxPay>() {
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
            public void onNext(WxPay baseBean) {
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
                        PayHelp.getInstance().wxPay(PayProxyActivity.this, payReq);
                    } else {
                        showTest("您未安装微信");
                    }
                } else {
                    showTest(mServerEror);
                }
            }
        });
        addSubscription(sbMyAccount);

    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    @OnClick({R.id.tvWxPay, R.id.tvMoneyPay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvWxPay:
                buy();
                break;
            case R.id.tvMoneyPay:
                break;
        }
    }
}
