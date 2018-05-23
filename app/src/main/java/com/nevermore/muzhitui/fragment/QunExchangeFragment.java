package com.nevermore.muzhitui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.chanven.lib.cptr.PtrClassicDefaultHeader;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.nevermore.muzhitui.EventBusContanct;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.QunExchangeDetailDialog;
import com.nevermore.muzhitui.activity.QunExchangePostedActivity;
import com.nevermore.muzhitui.module.bean.QunChangeBean;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/9/14.
 * 互换
 */

public class QunExchangeFragment extends base.BaseFragment {

    @BindView(R.id.rv_qun_exchange)
    RecyclerView rv_qun_exchange;
    @BindView(R.id.pcFlyt)
    PtrClassicFrameLayout mPcFlyt;

    private CommonAdapter mAdapter;
    private RecyclerAdapterWithHF recyclerAdapterWithHF;

    private LoadingAlertDialog mLoadingAlertDialog;

    private List<QunChangeBean.QunChange> exchanges = new ArrayList<>();
    private int mCurrenPager = 1;

    @Override
    public int createSuccessView() {
        EventBus.getDefault().register(this);
        return R.layout.fragment_qun_exchange;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoadingAlertDialog = new LoadingAlertDialog(getActivity());
        mAdapter = new CommonAdapter<QunChangeBean.QunChange>(getActivity(),R.layout.item_qun_exchange,exchanges) {
            @Override
            public void convert(ViewHolder holder, final QunChangeBean.QunChange qunChange) {
                ImageLoader.getInstance().displayImage(qunChange.pic1, (ImageView) holder.getView(R.id.iv_qun_qrcode));
                ImageLoader.getInstance().displayImage(qunChange.headimg, (ImageView) holder.getView(R.id.civ_qun_topic));
                holder.setText(R.id.tv_qun_exname, qunChange.wx_no);
                if (qunChange.wx_qun_name.length() > 7) {
                    holder.setText(R.id.tv_qun_name, qunChange.wx_qun_name.substring(0, 7) + "...");
                } else{
                    holder.setText(R.id.tv_qun_name, qunChange.wx_qun_name);
                }
                holder.setOnClickListener(R.id.ll_qun_exchange, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QunExchangeDetailDialog dialog = new QunExchangeDetailDialog(getActivity(),qunChange);
                        dialog.show();
                    }
                });
            }
        };
        StaggeredGridLayoutManager slm = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        rv_qun_exchange.setLayoutManager(slm);
        recyclerAdapterWithHF = new RecyclerAdapterWithHF(mAdapter);
        rv_qun_exchange.setAdapter(recyclerAdapterWithHF);
        loadData(1);

        //设置下拉刷新
        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(getActivity());
        mPcFlyt.addPtrUIHandler(header);
        mPcFlyt.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                exchanges.clear();
                mCurrenPager=1;
                loadData(mCurrenPager);
            }
        });

        mPcFlyt.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                loadData(++mCurrenPager);
            }
        });
        //mPcFlyt.findViewById(R.id.loadmore_default_footer_tv).setVisibility(View.GONE);
    }

    @OnClick(R.id.civ_posted_qun)
    public void onClick(View view){
        baseStartActivity(QunExchangePostedActivity.class);
    }

    private void loadData(final int pageCurrent){
        mLoadingAlertDialog.show();
        ((BaseActivityTwoV)getActivity()).addSubscription(wrapObserverWithHttp(WorkService.getWorkService().listQunChange((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),pageCurrent)).subscribe(new Subscriber<QunChangeBean>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                mPcFlyt.setLoadMoreEnable(true);
                mPcFlyt.loadMoreComplete(true);
                mPcFlyt.refreshComplete();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                removeLoadingView();
                removeErrorView();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onNext(QunChangeBean qunChangeBean) {
                if("1".equals(qunChangeBean.state)){
                    if (pageCurrent == 1) {
                        mAdapter.removeAllDate();
                    }
                    mAdapter.addDate(qunChangeBean.qunList);
                }else{
                    showTest(qunChangeBean.msg);
                }
            }
        }));
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventEMMessage(EventBusContanct contanct){
        if(contanct.getFlag() == EventBusContanct.REFRESH_QUN_EXCHANGE){
            mCurrenPager = 1;
            loadData(1);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
