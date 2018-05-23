package com.nevermore.muzhitui;

import android.support.v7.widget.RecyclerView;

import com.nevermore.muzhitui.module.bean.DashDetail;
import com.nevermore.muzhitui.module.network.WorkService;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import rx.Subscriber;
import rx.Subscription;

public class DashDetailActivity extends BaseActivityTwoV {

    @BindView(R.id.list)
    RecyclerView mList;
    private CommonAdapter<DashDetail.ListBean> mAdapter;
    List<DashDetail.ListBean> mLtObject = new ArrayList<>();
    private LoadingAlertDialog mLoadingAlertDialog;

    private void loadData() {
        mLoadingAlertDialog.show();
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().listBill((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))).subscribe(new Subscriber<DashDetail>() {
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
            public void onNext(DashDetail dashDetail) {
                if ("1".equals(dashDetail.getState())) {
                    mLtObject.addAll(dashDetail.getList());
                    mAdapter.notifyDataSetChanged();

                } else {
                    showTest(mServerEror);
                }

            }
        });
        addSubscription(sbMyAccount);
    }

    @Override
    public void init() {
        setMyTitle("提现记录");
        showBack();
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        mAdapter = new CommonAdapter<DashDetail.ListBean>(this, R.layout.rvitemt_dashdetail, mLtObject) {
            @Override
            public void convert(ViewHolder holder, DashDetail.ListBean o) {
                holder.setText(R.id.tvMoney, "财富-转出" + o.getMoney() + "元到微信钱包");
                holder.setText(R.id.tvTime, o.getCreatetime());
                if (o.getState() == 1) {
                    holder.setText(R.id.tvState, "提现成功");
                    //holder.setTextColor(R.id.tvState,R.color.green_new);
                } else {
                    holder.setText(R.id.tvState, "提现审核中");
                    holder.setTextColor(R.id.tvState,R.color.red);
                }

            }
        };
        mList.setAdapter(mAdapter);
        loadData();
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_dash_detail;
    }
}
