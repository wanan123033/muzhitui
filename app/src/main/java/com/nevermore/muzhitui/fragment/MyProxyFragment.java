package com.nevermore.muzhitui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.TextView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.MyProxy;
import com.nevermore.muzhitui.module.network.WorkService;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.BaseFragment;
import base.SPUtils;
import base.UIUtils;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by hehe on 2016/6/14.
 */

public class MyProxyFragment extends BaseFragment {
    @BindView(R.id.tvHigher)
    TextView mTvHigher;

    @BindView(R.id.tvMyWin)
    TextView mTvMyWin;


    @BindView(R.id.list)
    RecyclerView mList;
    private CommonAdapter mAdapter;
    List<Object> mLtObject = new ArrayList<>();
    private LoadingAlertDialog mLoadingAlertDialog;

    @Override
    public int createSuccessView() {
        return R.layout.fragment_myproxy;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoadingAlertDialog = new LoadingAlertDialog(getActivity());
        mAdapter = new CommonAdapter<Object>(getActivity(), R.layout.rvitem_myproxy, mLtObject) {
            @Override
            public void convert(ViewHolder holder, Object o) {
                if (o instanceof MyProxy.OneDistributorsBean) {
                    MyProxy.OneDistributorsBean bean = (MyProxy.OneDistributorsBean) o;
                    holder.setImageURL(R.id.ivHead, bean.getWechatimg(), true);
                    holder.setText(R.id.tvName, bean.getWechatname());
                    holder.setText(R.id.tvProxyLevel, bean.getDistributorLevel());
                    holder.setText(R.id.tvMember, String.valueOf(bean.getNums()));
                    holder.setText(R.id.tvMoney, String.valueOf(bean.getNums()));
                } else if (o instanceof MyProxy.TwoDistributorsBean) {
                    MyProxy.TwoDistributorsBean bean = (MyProxy.TwoDistributorsBean) o;
                    holder.setImageURL(R.id.ivHead, bean.getWechatimg(), true);
                    holder.setText(R.id.tvName, bean.getWechatname());
                    holder.setText(R.id.tvProxyLevel, bean.getDistributorLevel());
                    holder.setText(R.id.tvMember, String.valueOf(bean.getNums()));
                    holder.setText(R.id.tvMoney, String.valueOf(bean.getNums()));
                }
            }
        };
        mList.setAdapter(mAdapter);
        loadData();
    }

    private void loadData() {
        mLoadingAlertDialog.show();
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().myApply((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))).subscribe(new Subscriber<MyProxy>() {
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
            public void onNext(MyProxy myProxy) {
                if ("1".equals(myProxy.getState())) {
                    mLtObject.addAll(myProxy.getOneDistributors());
                    mLtObject.addAll(myProxy.getTwoDistributors());
                    mAdapter.notifyDataSetChanged();

                    if (myProxy.getHasTjdistributor() == 0) {
                        mTvHigher.setVisibility(View.GONE);
                    } else {
                        mTvHigher.setVisibility(View.VISIBLE);
                        mTvHigher.append(":" + myProxy.getTjdistributorName());
                    }
                    String win = myProxy.getProfit() + "元";
                    SpannableStringBuilder spanBuilder = new SpannableStringBuilder(win);
                    spanBuilder.setSpan(new TextAppearanceSpan(null, 0, UIUtils.dip2px(24), null, null), 0, String.valueOf(myProxy.getProfit()).length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    mTvMyWin.setText(spanBuilder);
                } else {
                    showTest(mServerEror);
                }

            }
        });
        ((BaseActivityTwoV) getActivity()).addSubscription(sbMyAccount);
    }
}
