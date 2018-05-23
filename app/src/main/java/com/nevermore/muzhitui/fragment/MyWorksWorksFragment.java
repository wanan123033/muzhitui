package com.nevermore.muzhitui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.nevermore.muzhitui.MainActivity;
import com.nevermore.muzhitui.PageLookActivity;
import com.nevermore.muzhitui.PagerEditActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.event.DraftEvent;
import com.nevermore.muzhitui.event.ReadCountEvent;
import com.nevermore.muzhitui.event.WorkEvent;
import com.nevermore.muzhitui.module.bean.BaseBean;
import com.nevermore.muzhitui.module.bean.MyWork;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.BaseFragment;
import base.ImageUtil;
import base.RecyclerBaseAdapter;
import base.SPUtils;
import base.UIUtils;
import base.network.RetrofitUtil;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import rx.Subscriber;
import rx.Subscription;

public class MyWorksWorksFragment extends BaseFragment {


    @BindView(R.id.list)
    RecyclerView mList;
    List<MyWork.PageListBean> mLtObject = new ArrayList<>();
    @BindView(R.id.pcFlyt)
    PtrClassicFrameLayout pcFlyt;
    private RecyclerBaseAdapter mAdapter;
    private int mCurrenPager = 1;
    public static final String KEY_POSITION = "POSITION";
    public static final String USERID = "USERID";
    private int mPosition;
    private int mAllPages;
    private RecyclerAdapterWithHF recyclerAdapterWithHF;
    private LoadingAlertDialog mLoadingAlertDialog;
    private boolean mIsReFresh;
    private android.app.AlertDialog alertDialog;
    private String userId;
    @Override
    public int createSuccessView() {
       if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return R.layout.fragment_mywork_list;
    }

    @Override
    public void getFragmentArguments(Bundle bundle) {
        super.getFragmentArguments(bundle);

    }

    public static MyWorksWorksFragment newInstance(int position,String userId) {
        MyWorksWorksFragment myWorksWorksFragment = new MyWorksWorksFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_POSITION, position);
        if (!TextUtils.isEmpty(userId))
            bundle.putString(USERID,userId);
        myWorksWorksFragment.setArguments(bundle);
        return myWorksWorksFragment;
    }

    @Override
    public void setArguments(Bundle bundle) {
        super.setArguments(bundle);
        mPosition = bundle.getInt(KEY_POSITION);
        userId = bundle.getString(USERID, (String) SPUtils.get(SPUtils.GET_LOGIN_ID,""));
    }

    @Subscribe
    public void onEventModeRefresh(WorkEvent baseMyMode) {
        if (mPosition == 1) {
            mIsReFresh = true;
            loadData(1);
        }
    }

    @Subscribe
    public void onEventModeRefresh(DraftEvent draftEvent) {
        if (mPosition == 0) {
            mIsReFresh = true;
            loadData(1);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLtObject.clear();
        mLoadingAlertDialog = new LoadingAlertDialog(getActivity());
        mAdapter = new RecyclerBaseAdapter<MyWork.PageListBean>(getActivity(), mLtObject, R.layout.fragment_mywork, new RecyclerBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.tvEdit:
                        Intent intent1 = new Intent(getActivity(), PagerEditActivity.class);
                        intent1.putExtra(PagerEditActivity.KEY_ID, mLtObject.get(position).getId() + "");
                        getActivity().startActivity(intent1);
                        break;
                    case R.id.tvDelete:
                        delete(mLtObject.get(position));
                        break;
                    case R.id.rlyt:
                        Intent intent = new Intent(getActivity(), PageLookActivity.class);
                        intent.putExtra(PageLookActivity.KEY_URL, RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + RetrofitUtil.PAGE_LOOK + "?id=" + mLtObject.get(position).getId() + "&share=0"+ "&curLoginId=" + MainActivity.loginId);
                        intent.putExtra(PageLookActivity.KEY_IMG, mLtObject.get(position).getImage());
                        intent.putExtra(PageLookActivity.KEY_ID, mLtObject.get(position).getId() + "");
                        intent.putExtra(PageLookActivity.KEY_TITLE, mLtObject.get(position).getTitle());

                        getActivity().startActivity(intent);
                        mLtObject.get(position).setRead(mLtObject.get(position).getRead() + 1);
                        mAdapter.notifyItemChanged(position);
                        break;
                }
            }
        }) {
            @Override
            public void fillData(ViewHolder viewHolder, MyWork.PageListBean data,final int position) {
                ImageLoader.getInstance().displayImage(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + data.getImage(), (ImageView) viewHolder.getView(R.id.ivHead), ImageUtil.getInstance().getBaseDisplayOption());
                viewHolder.setText(R.id.tvMain, data.getTitle());
                viewHolder.setText(R.id.tvSub, "阅读：" + data.getRead());
                viewHolder.setText(R.id.tvTime, data.getPagedate());
                if(userId.equals(SPUtils.get(SPUtils.GET_LOGIN_ID,""))) {
                    viewHolder.getView(R.id.tvDelete).setVisibility(View.VISIBLE);
                }else {
                    viewHolder.getView(R.id.tvDelete).setVisibility(View.GONE);
                }
                RelativeLayout rlyt=viewHolder.getView(R.id.rlyt);
                if(userId.equals(SPUtils.get(SPUtils.GET_LOGIN_ID,""))) {
                    rlyt.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            alertDialog = UIUtils.getAlertDialog(getActivity(), null, "删除该文章?", "取消", "确定", 0, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();


                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    delete(mLtObject.get(position));
                                    alertDialog.dismiss();
                                }
                            });
                            alertDialog.show();
                            return true;
                        }
                    });
                }
            }

            @Override
            public boolean onItemMove(int fromPosition, int toPosition) {
                return false;
            }
        };
        mAdapter.setIsCela(true);
        recyclerAdapterWithHF = new RecyclerAdapterWithHF(mAdapter);
        mList.setAdapter(recyclerAdapterWithHF);
        pcFlyt.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return false;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

            }
        });
        pcFlyt.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                loadData(++mCurrenPager);
            }
        });
        loadData(mCurrenPager);
        showLoadingView();


    }


    public void refreshData() {
        mIsReFresh = true;
        mCurrenPager = 1;
        loadData(mCurrenPager);
        showLoadingView();
    }


    private void delete(final MyWork.PageListBean bean) {
        mLoadingAlertDialog.show();
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().deleteMyWork((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),bean.getId())).subscribe(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {
                EventBus.getDefault().post(new ReadCountEvent());
                mLoadingAlertDialog.dismiss();
                showTest("删除成功");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mLoadingAlertDialog.dismiss();
                showTest(mNetWorkError);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                if (1 == baseBean.getState()) {//
                    mLtObject.remove(bean);
                    mAdapter.notifyDataSetChanged();
                } else {
                    showTest(mServerEror);
                }
            }
        });
        ((BaseActivityTwoV) getActivity()).addSubscription(sbMyAccount);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //   EventBus.getDefault().unregister(this);
    }

    private void loadData(int currenPager) {

        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().myWorks((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),2, mPosition, 2, currenPager,userId)).subscribe(new Subscriber<MyWork>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                pcFlyt.setLoadMoreEnable(true);
                pcFlyt.loadMoreComplete(true);
                if (mCurrenPager == mAllPages) {
                    pcFlyt.setLoadMoreEnable(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                showErrorView();
                showTest(mNetWorkError);
            }

            @Override
            public void onNext(MyWork myWork) {
                if ("1".equals(myWork.getState())) {//
                    if (mIsReFresh) {
                        mIsReFresh = false;
                        mLtObject.clear();
                    }
                    mLtObject.addAll(myWork.getPageList());
                    mAdapter.notifyDataSetChanged();
                    mAllPages = myWork.getAllPages();
                } else {
                    showTest(mServerEror);
                }
            }
        });
        ((BaseActivityTwoV) getActivity()).addSubscription(sbMyAccount);
    }
}
