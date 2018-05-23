package com.nevermore.muzhitui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.nevermore.muzhitui.BatchSellActivity;
import com.nevermore.muzhitui.ProxyUpgradeActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.MyStock;
import com.nevermore.muzhitui.module.network.WorkService;

import base.BaseActivityTwoV;
import base.BaseFragment;
import base.SPUtils;
import base.UIUtils;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by hehe on 2016/6/7.
 */
public class MyRepertoryFragment extends BaseFragment {
    @BindView(R.id.tvOverflower)
    TextView mTvOverflower;
    @BindView(R.id.tvProxyLevel)
    TextView mTvProxyLevel;
    @BindView(R.id.tvBuyProxy)
    TextView mTvBuyProxy;
    @BindView(R.id.tvBatchSell)
    TextView mTvBatchSell;
    @BindView(R.id.tvCopyKey)
    TextView mTvCopyKey;
    MyStock mMyStock;
    @BindView(R.id.etPrice)
    EditText mEtPrice;
    private LoadingAlertDialog mLoadingAlertDialog;

    public void setmMyStock(MyStock mMyStock) {
        this.mMyStock = mMyStock;
        if (mTvOverflower != null) {
            setStock();
        }
    }

    @Override
    public int createSuccessView() {
        return R.layout.fragment_myrepertory;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoadingAlertDialog = new LoadingAlertDialog(getActivity());
       /* ColorStateList redColors = ColorStateList.valueOf(0xffff0000);
        SpannableStringBuilder spanBuilder = new SpannableStringBuilder("这是一个测试");*/
//style 为0 即是正常的，还有Typeface.BOLD(粗体) Typeface.ITALIC(斜体)等
//size  为0 即采用原始的正常的 size大小

//style 为0 即是正常的，还有Typeface.BOLD(粗体) Typeface.ITALIC(斜体)等
//size  为0 即采用原始的正常的 size大小

        if (mMyStock != null) {
            setStock();
        }
    }

    private void setStock() {
        SpannableStringBuilder spanBuilder = new SpannableStringBuilder("剩余库存" + mMyStock.getCardCount() + "张");
        spanBuilder.setSpan(new TextAppearanceSpan(null, 0, UIUtils.dip2px(33), null, null), 4, 4 + (String.valueOf(mMyStock.getCardCount()).length()), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        mTvOverflower.setText(spanBuilder);
        mTvProxyLevel.setText("您目前级别：" + mMyStock.getTypeName());
    }

    @OnClick({R.id.tvOverflower, R.id.tvProxyLevel, R.id.tvBuyProxy, R.id.tvBatchSell, R.id.tvCopyKey})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvOverflower:
                break;
            case R.id.tvProxyLevel:
                break;
            case R.id.tvBuyProxy:
                baseStartActivity(ProxyUpgradeActivity.class);
                getActivity().finish();
                break;
            case R.id.tvBatchSell:
                Intent intent = new Intent(getActivity(), BatchSellActivity.class);
                intent.putExtra(BatchSellActivity.MYSTOCK, mMyStock.getCardCount() + "");
                getActivity().startActivity(intent);
                getActivity().finish();
                break;
            case R.id.tvCopyKey:
                if (TextUtils.isEmpty(mEtPrice.getText().toString())) {
                    showTest("请输入单价");
                    return;
                }
                float price = Float.valueOf(mEtPrice.getText().toString());
                if (price >= 200 && price <= 298) {
                    getKey(price);
                } else {
                    showTest("单价不能低于200,大于298元");
                }
                break;
        }
    }

    private void getKey(float price) {
        mLoadingAlertDialog.show();
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().makeCard((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),price)).subscribe(new Subscriber<CardKey>() {
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
            public void onNext(CardKey cardKey) {
                if ("1".equals(cardKey.getState())) {
                    UIUtils.copy(cardKey.getCard());
                    showTest("复制成功");
                }

            }
        });
        ((BaseActivityTwoV) getActivity()).addSubscription(sbMyAccount);
    }


}
