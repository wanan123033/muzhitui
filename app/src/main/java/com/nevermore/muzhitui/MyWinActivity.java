package com.nevermore.muzhitui;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nevermore.muzhitui.module.bean.MyProfit;
import com.nevermore.muzhitui.module.network.WorkService;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
import base.recycler.recyclerview.DividerItemDecoration;
import base.recycler.recyclerview.OnItemClickListener;
import base.recycler.recyclerview.support.SectionAdapter;
import base.recycler.recyclerview.support.SectionSupport;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;

/**
 * 我的收益Activity
 */
public class MyWinActivity extends BaseActivityTwoV {


    @BindView(R.id.rvKehu)
    RecyclerView mRvKehu;
    @BindView(R.id.ivPre)
    ImageView mIvPre;
    @BindView(R.id.rvPages)
    RecyclerView mRvPages;
    @BindView(R.id.ivNext)
    ImageView mIvNext;
    @BindView(R.id.cvPages)
    CardView mCvPages;

    private SectionAdapter<MyProfit.ProMapBean.LoginArrayBean> mSectionAdapter;
    private List<MyProfit.ProMapBean.LoginArrayBean> mLt = new ArrayList<>();
    private List<String> mLtPager = new ArrayList<>();
    private CommonAdapter<String> mCommonAdapterPager;
    private LoadingAlertDialog mLoadingAlertDialog;
    private TextView mSelectedView;
    private boolean mIsFirst = true;
    private int mPageCurrent = 1;
    private int mAllPage;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    public void init() {
        setMyTitle("我的收益");
        showBack();
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        mSectionAdapter = new SectionAdapter<MyProfit.ProMapBean.LoginArrayBean>(this, R.layout.rvitem_mywin_des, mLt, new SectionSupport<MyProfit.ProMapBean.LoginArrayBean>() {
            @Override
            public int sectionHeaderLayoutId() {
                return R.layout.rvitem_mywin_head;
            }

            @Override
            public int sectionTitleTextViewId() {
                return R.id.tvTime;
            }

            @Override
            public String getTitle(MyProfit.ProMapBean.LoginArrayBean o) {
                return o.getTrade_date().substring(0, 10);
            }
        }) {
            @Override
            public void convert(ViewHolder holder, MyProfit.ProMapBean.LoginArrayBean o) {
                holder.setText(R.id.tvTime, o.getTrade_date().substring(10));
                holder.setText(R.id.tvDes, o.getDesc());
            }

        };
        mRvKehu.setAdapter(mSectionAdapter);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvPages.setLayoutManager(mLinearLayoutManager);
        mCommonAdapterPager = new CommonAdapter<String>(this, R.layout.rvitem_pages, mLtPager) {
            @Override
            public void convert(ViewHolder holder, String o) {
                if (holder.getMyPosition() == (mPageCurrent-1)) {
                    mSelectedView = holder.getView(R.id.tvPage);
                    holder.getView(R.id.tvPage).setSelected(true);
                } else {
                    holder.getView(R.id.tvPage).setSelected(false);
                }
                holder.setText(R.id.tvPage, o);

            }

        };
        mCommonAdapterPager.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                optPage(viewHolder);
                mPageCurrent = position + 1;
                loadPage(mPageCurrent);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                return false;
            }
        });
        mRvPages.setAdapter(mCommonAdapterPager);
        mRvPages.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL_LIST));
        loadData(mPageCurrent);
        mLoadingAlertDialog.show();
    }

    private void loadData(int pageCurrent) {
        Logger.i("pageCurrent = " + pageCurrent);
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().myProfit((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),pageCurrent)).subscribe(new Subscriber<MyProfit>() {
            @Override
            public void onCompleted() {
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                showErrorView();
                showTest(mNetWorkError);
                e.printStackTrace();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onNext(MyProfit myProfit) {
                if ("1".equals(myProfit.getState())) {
                    if (mIsFirst) {
                        mIsFirst = false;
                        mAllPage = myProfit.getAllPages();
                        int size = mAllPage + 1;
                        Logger.i("okokokok");
                        for (int i = 1; i < size; i++) {
                            mLtPager.add(String.valueOf(i));
                        }
                        mCommonAdapterPager.notifyDataSetChanged();
                    }
                    mLt.clear();
                    List<MyProfit.ProMapBean> ltTemp = myProfit.getProMap();
                    for (MyProfit.ProMapBean proMapBean : ltTemp) {
                        mLt.addAll(proMapBean.getLoginArray());
                    }
                    mSectionAdapter.notifyDataSetChanged();
                } else {
                    showErrorView();
                    showTest(mServerEror);
                }
            }
        });
        addSubscription(sbMyAccount);
    }


    @Override
    public int createSuccessView() {
        return R.layout.activity_my_win;
    }

    @OnClick({R.id.ivPre, R.id.ivNext})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivPre:
                if (mPageCurrent > 1) {
                    loadPage(--mPageCurrent);
                    mCommonAdapterPager.notifyDataSetChanged();
                }
                break;
            case R.id.ivNext:
                if (mPageCurrent < mAllPage) {
                    loadPage(++mPageCurrent);

                    mCommonAdapterPager.notifyDataSetChanged();

                }
                break;
        }
    }

    private void optPage(ViewHolder viewHolder) {
        if (mSelectedView == null) {
            mSelectedView = viewHolder.getView(R.id.tvPage);
        } else {
            mSelectedView.setSelected(false);
            mSelectedView = viewHolder.getView(R.id.tvPage);
        }
        mSelectedView.setSelected(true);
    }

    private void loadPage(int pageCurrent) {
        mLoadingAlertDialog.show();
        loadData(pageCurrent);
    }
}
