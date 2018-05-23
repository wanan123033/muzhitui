package com.nevermore.muzhitui.fragment;

import android.content.Intent;
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
import com.nevermore.muzhitui.activity.SeePersonalInfoIsFriendActivity;
import com.nevermore.muzhitui.module.bean.LevelMember;
import com.nevermore.muzhitui.module.bean.MyLever;
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

public class MySubordinatesFragment extends BaseFragment {


    @BindView(R.id.rvMysub)
    RecyclerView mRvMysub;
    @BindView(R.id.ivPreMysub)
    ImageView mIvPreMysub;
    @BindView(R.id.rvPagesMysub)
    RecyclerView mRvPagesMysub;
    @BindView(R.id.ivNextMysub)
    ImageView mIvNextMysub;
    @BindView(R.id.cvPagesMySub)
    CardView mCvPagesMySub;



    private CommonAdapter mCommonAdapter;
    private CommonAdapter mCommonAdapterPager;
    private List<MyLever.LoginListBean> mLt = new ArrayList<>();
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
        return R.layout.fragment_mysubordinate;
    }

    public static MySubordinatesFragment newInstance(int position) {
        MySubordinatesFragment mySubordinatesFragment = new MySubordinatesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_POSITION, position);
        mySubordinatesFragment.setArguments(bundle);
        return mySubordinatesFragment;
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
        mCommonAdapter = new CommonAdapter<MyLever.LoginListBean>(getActivity(), R.layout.item_lv_myfriend, mLt) {
            @Override
            public void convert(ViewHolder holder, MyLever.LoginListBean o) {
                holder.setText(R.id.ivMyFriendName, o.getUser_name());
                if (!o.getAgent().equals("未加入会员")){
                    if (o.getAgent().equals("年费会员")){
                        holder.setVisible(R.id.ivMyFriendState1,true);
                        holder.setVisible(R.id.ivMyFriendState2,false);
                    }else{
                        holder.setVisible(R.id.ivMyFriendState1,false);
                        holder.setVisible(R.id.ivMyFriendState2,true);
                    }

                }else{
                    holder.setVisible(R.id.ivMyFriendState1, false);
                    holder.setVisible(R.id.ivMyFriendState2, false);
                }


                holder.setImageURL(R.id.ivMyFriendHead, o.getHeadimg(), false);
            }
        };
        mCommonAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                Logger.i(" mLoginId = " + mLoginId);
                if (mLoginId == null) {
                    Intent intent = new Intent(getActivity(), SeePersonalInfoIsFriendActivity.class);
                    intent.putExtra("id", mLt.get(position).getId()+"");

                    intent.putExtra("isShowDelete", false);

                    startActivity(intent);
                }

            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                return false;
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvPagesMysub.setLayoutManager(linearLayoutManager);
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
        mRvMysub.setAdapter(mCommonAdapter);
        mRvPagesMysub.setAdapter(mCommonAdapterPager);
        mRvPagesMysub.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL_LIST));
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
        int id= (int) SPUtils.get(SPUtils.KEY_GET_ID, 0);



        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().myLevelMember((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),mPosition,id, mPageCurrent,1)).subscribe(new Subscriber<MyLever>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                mLoadingAlertDialog.dismiss();
                removeErrorView();
            }

            @Override
            public void onError(Throwable e) {
                showErrorView();
                showTest(mNetWorkError+"我的下级请求出错");
                e.printStackTrace();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onNext(MyLever levelMember) {
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

    @OnClick({R.id.ivPreMysub, R.id.ivNextMysub})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivPreMysub:
                if (mPageCurrent > 1) {
                    loadPage(--mPageCurrent);
                    mCommonAdapterPager.notifyDataSetChanged();
                }
                break;
            case R.id.ivNextMysub:
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
