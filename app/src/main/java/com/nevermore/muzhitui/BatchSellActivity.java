package com.nevermore.muzhitui;

import android.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.nevermore.muzhitui.event.BatchSellEvent;
import com.nevermore.muzhitui.module.bean.BaseBean;
import com.nevermore.muzhitui.module.network.WorkService;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.UIUtils;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by hehe on 2016/6/18.
 */
public class BatchSellActivity extends BaseActivityTwoV {

    @BindView(R.id.tvOverflower)
    TextView mTvOverFlower;

    @BindView(R.id.etId)
    EditText mEtId;

    @BindView(R.id.etNum)
    EditText mEtNum;

    @BindView(R.id.tvConfirm)
    TextView mTvConfirm;

    public static final String MYSTOCK = "MYSTOCK";

    LoadingAlertDialog mLoadingAlertDialog;

    @Override
    public void init() {
        showBack();
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        String stock = getIntent().getStringExtra(MYSTOCK);

        SpannableStringBuilder spanBuilder = new SpannableStringBuilder("剩余库存" + stock + "张");
        spanBuilder.setSpan(new TextAppearanceSpan(null, 0, UIUtils.dip2px(33), null, null), 4, 4 + (stock.length()), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        mTvOverFlower.setText(spanBuilder);
        mTvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }
    AlertDialog alertDialog = null;
    private void loadData() {

        alertDialog = UIUtils.getAlertDialog(this, "提示信息", "您确认将卡密转卖吗，一旦转卖将不可回收。", "取消", "确定", 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buy();
            }
        });
        alertDialog.show();
    }
    AlertDialog dialog = null;
    private void buy(){
        final String sellId = mEtId.getText().toString();
        final String sellCount = mEtNum.getText().toString();
        if (TextUtils.isEmpty(sellId)) {
            dialog = UIUtils.getAlertDialog(this, "提示信息", "请输入对方ID", null, "确定", 0, null, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
//            showTest("请输入对方ID");
            return;
        }

        if (TextUtils.isEmpty(sellCount)) {
            dialog = UIUtils.getAlertDialog(this, "提示信息", "请输入转卖数量", null, "确定", 0, null, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
//            showTest("输入转卖数量");
            return;
        }
        if(Integer.parseInt(sellCount) < 2){
            dialog = UIUtils.getAlertDialog(this, "提示信息", "转卖数量不得低于2", null, "确定", 0, null, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
//            showTest("转卖数量不得低于2");
            return;
        }

        mLoadingAlertDialog.show();
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().doMoveCards((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),Integer.valueOf(sellId), Integer.valueOf(sellCount))).subscribe(new Subscriber<BaseBean>() {
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
            public void onNext(BaseBean baseBean) {
                final int state = baseBean.getState();
                Logger.i("state = " + state);
                switch (state) {
                    case 0:
                        showTest(mServerEror);
                        break;
                    case 1:
                        EventBus.getDefault().post(new BatchSellEvent());
                        finish();
                        break;
                    case 2:
                        showTest("该用户不存在");
                        break;
                    case 3:
                        showTest("卡密库存不足");
                        break;
                    case 4:
                        showTest("卡密不可转卖给代理商");
                        break;
                    case 5:
                        showTest("该用户正在帮其他代理转卖，不可同时给两家代理转卖");
                        break;
                }
            }
        });
        addSubscription(sbMyAccount);


    }

    @Override
    public int createSuccessView() {
        return R.layout.acitivty_batchsell;
    }
}
