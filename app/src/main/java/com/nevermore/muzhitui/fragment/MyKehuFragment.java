package com.nevermore.muzhitui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.LevelMember;
import com.nevermore.muzhitui.module.network.WorkService;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.BaseFragment;
import base.SPUtils;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
import base.recycler.recyclerview.DividerItemDecoration;
import base.recycler.recyclerview.OnItemClickListener;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;


public class MyKehuFragment extends BaseFragment {
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


    private CommonAdapter mCommonAdapter;
    private CommonAdapter mCommonAdapterPager;
    private List<LevelMember.LoginListBean> mLt = new ArrayList<>();
    private List<String> mLtPager = new ArrayList<>();
    public static final String KEY_POSITION = "POSITION";
    private int mPosition;
    private TextView mSelectedView;
    private int mPageCurrent = 1;
    private int mAllPage;
    private LoadingAlertDialog mLoadingAlertDialog;
    private boolean mIsFirst = true;
    private String mLoginId;
    private int mLastPage = -1;
    private int mLastPosition;

    @Override
    public int createSuccessView() {
        return R.layout.fragment_my_kehu;
    }

    public static MyKehuFragment newInstance(int position) {
        MyKehuFragment myKehuFragment = new MyKehuFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_POSITION, position);
        myKehuFragment.setArguments(bundle);
        return myKehuFragment;
    }

    @Override
    public void getFragmentArguments(Bundle bundle) {
        super.getFragmentArguments(bundle);
        mPosition = bundle.getInt(KEY_POSITION);
        mLastPosition = mPosition;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoadingAlertDialog = new LoadingAlertDialog(getActivity());
        mCommonAdapter = new CommonAdapter<LevelMember.LoginListBean>(getActivity(), R.layout.rvitem_kehu, mLt) {
            @Override
            public void convert(ViewHolder holder, LevelMember.LoginListBean bean) {
                holder.setImageURL(R.id.ivHead, bean.getHeadimg(), false);
                holder.setText(R.id.tvName, bean.getWechatname());
                holder.setText(R.id.tvMemberType, bean.getAgent());
                holder.setText(R.id.tvInvited, "已邀约" + bean.getNums() + "人");
            }
        };
        mCommonAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                Logger.i(" mLoginId = " + mLoginId);
                if (mLoginId == null) {
                    LevelMember.LoginListBean bean = (LevelMember.LoginListBean) o;
                    mLoginId = String.valueOf(bean.getId());
                    mPageCurrent = 1;
                    mPosition = 0;
                    loadData(mPageCurrent, mLoginId);
                    mIsFirst = true;
                }

            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                return false;
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvPages.setLayoutManager(linearLayoutManager);
        mCommonAdapterPager = new CommonAdapter<String>(getActivity(), R.layout.rvitem_pages, mLtPager) {
            @Override
            public void convert(ViewHolder holder, String o) {
                if (holder.getMyPosition() == (mPageCurrent - 1)) {
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
                if (mLastPage != -1) {
                    optPage(viewHolder);
                    mPageCurrent = position + 1;
                    loadPage(mPageCurrent);
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                return false;
            }
        });
        mRvKehu.setAdapter(mCommonAdapter);
        mRvPages.setAdapter(mCommonAdapterPager);
        mRvPages.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL_LIST));
        showLoadingView();
        loadData(mPageCurrent, mLoginId);
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


    private void loadData(int pageCurrent, String loginId) {

        if (loginId == null) {
            mLastPage = pageCurrent;
        }
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().levelMember((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),mPosition + 1, loginId, pageCurrent)).subscribe(new Subscriber<LevelMember>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
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
            public void onNext(LevelMember levelMember) {
                if ("1".equals(levelMember.getState())) {
                    if (mIsFirst) {
                        mIsFirst = false;
                        mLtPager.clear();
                        mAllPage = levelMember.getAllPages();
                        int size = mAllPage + 1;
                        for (int i = 1; i < size; i++) {
                            mLtPager.add(String.valueOf(i));
                        }
                        mCommonAdapterPager.notifyDataSetChanged();
                    }
                    mLt.clear();
                    mLt.addAll(levelMember.getLoginList());
                    mCommonAdapter.notifyDataSetChanged();
                } else {
                    showErrorView();
                    showTest(mServerEror);
                }
            }
        });
        ((BaseActivityTwoV) getActivity()).addSubscription(sbMyAccount);
    }

    public boolean backUpLevel() {
        if (mLoginId == null) {
            return false;
        } else {
            mIsFirst = true;
            mPageCurrent = mLastPage;
            mLoginId = null;
            mPosition = mLastPosition;
            loadData(mPageCurrent, mLoginId);
            return true;
        }
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

    private void loadPage(int pageCurrent) {
        mLoadingAlertDialog.show();
        loadData(pageCurrent, mLoginId);
    }
}
