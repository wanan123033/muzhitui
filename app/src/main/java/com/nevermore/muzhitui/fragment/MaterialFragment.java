package com.nevermore.muzhitui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.nevermore.muzhitui.MainActivity;
import com.nevermore.muzhitui.PageLookActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.Works;
import com.nevermore.muzhitui.module.network.WorkService;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.BaseFragment;
import base.MyLogger;
import base.SPUtils;
import base.network.RetrofitUtil;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.Subscription;

/**
 * 素材Fragment
 */
public class MaterialFragment extends BaseFragment {


    @BindView(R.id.list)
    RecyclerView mList;
    @BindView(R.id.pcFlyt)
    PtrClassicFrameLayout mPcFlyt;
    private CommonAdapter mAdapter;
    private RecyclerAdapterWithHF recyclerAdapterWithHF;
    public static final String KEY_POSITION = "POSITION";
    private int mPosition, isMe = 0;
    private int mCurrenPager;
    List<Works.PageListBean> mLtObject = new ArrayList<>();
    private LoadingAlertDialog mLoadingAlerDialog;

    @Override
    public int createSuccessView() {
        return R.layout.fragment_matrail;
    }

    @Override
    public void getFragmentArguments(Bundle bundle) {
        super.getFragmentArguments(bundle);
        mPosition = bundle.getInt(KEY_POSITION);
        isMe = bundle.getInt("isMe");
      /*  if (mPosition == 0) {
            mPosition = 1;
        } else {
            mPosition = 0;
        }*/


        mCurrenPager = 1;
    }

    /**
     * @param position
     * @param isMe     是否是自己的原创作品，1为是的。0为别人的原创
     * @return
     */

    public static MaterialFragment newInstance(int position, int isMe) {
        MaterialFragment materialFragment = new MaterialFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_POSITION, position);
        bundle.putInt("isMe", isMe);
        materialFragment.setArguments(bundle);

        return materialFragment;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showLoadingView();
        mLoadingAlerDialog = new LoadingAlertDialog(getActivity());
        if (mPosition == 2) {
            loadDataOur(isMe, mCurrenPager);
            mAdapter = new CommonAdapter<Works.PageListBean>(getActivity(), R.layout.item_index_fragment, mLtObject) {
                @Override
                public void convert(ViewHolder holder, Works.PageListBean o) {
                    holder.setText(R.id.ivItemIndexfTitle, o.getTitle());
                    holder.setText(R.id.ivItemIndexfName, "发布者：" + o.getUser_name());
                    holder.setText(R.id.ivItemIndexfReadNum, o.getRead() + " 阅读");
                    holder.setText(R.id.ivItemIndexfData, o.getUpdate_time());
                    holder.setImageURL(R.id.ivItemIndexfImage, o.getTitle_pic(), false);
                    Log.e("素材title:", o.getTitle_pic() + "");

                }
            };
            recyclerAdapterWithHF = new RecyclerAdapterWithHF(mAdapter);
            recyclerAdapterWithHF.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {      //原创单个文章点击详情
                @Override
                public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {
                    Intent intent = new Intent(getActivity(), PageLookActivity.class);
                    intent.putExtra(PageLookActivity.KEY_URL, RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + RetrofitUtil.PAGE_LOOK_OWN + "?id=" + mLtObject.get(position).getId() + "&share=1" + "&curLoginId=" + MainActivity.loginId + "&isHomePage=1");

                    Log.i("MYTAG",RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + RetrofitUtil.PAGE_LOOK_OWN + "?id=" + mLtObject.get(position).getId() + "&share=1" + "&curLoginId=" + MainActivity.loginId + "&isHomePage=1");
                    if (!TextUtils.isEmpty(mLtObject.get(position).getTitle_pic())) {
                        String url = RetrofitUtil.API_URL_HOU + RetrofitUtil.PROJECT_URL;
                        int aa = mLtObject.get(position).getTitle_pic().indexOf(url) + url.length();

                        String ml = mLtObject.get(position).getTitle_pic().substring(aa);
                        Log.e("title pic:", aa + "\t" + ml);
                        intent.putExtra(PageLookActivity.KEY_IMG, ml);
                    }
                    intent.putExtra(PageLookActivity.KEY_ID, mLtObject.get(position).getId() + "");
                    MyLogger.kLog().e(mLtObject.get(position).getTitle());
                    intent.putExtra(PageLookActivity.KEY_TITLE,mLtObject.get(position).getTitle());
                    intent.putExtra("isOriginal", true);//是否是原创，因为不需要在首页显示红笔，所以判断是否是原创也都不需要了，可以通过原创判断分享出去的是否显示底板
                    intent.putExtra("isShowEdit", true);//不显示编辑红笔提示//不显示编辑红笔提示以及底板修改
                    getActivity().startActivity(intent);
                    mLtObject.get(position).setRead(mLtObject.get(position).getRead() + 1);
                    recyclerAdapterWithHF.notifyItemChanged(position);
                }
            });

        } else {
            Log.e("position", mPosition + "");
            loadData(1, 1);
            mAdapter = new CommonAdapter<Works.PageListBean>(getActivity(), R.layout.rvitem_material, mLtObject) {
                @Override
                public void convert(ViewHolder holder, Works.PageListBean o) {
                    holder.setText(R.id.tvMain, o.getTitle());
                    holder.setText(R.id.tvSub, o.getWechatname());
                    holder.setText(R.id.tvReadNum, o.getRead() + " 阅读");
                    holder.setText(R.id.tvTime, o.getPagedate());
                    holder.setImageURL(R.id.ivHead, RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + o.getImage(), false);
                }
            };
            recyclerAdapterWithHF = new RecyclerAdapterWithHF(mAdapter);
            recyclerAdapterWithHF.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {   //秒变，最热单个文章点击详情
                @Override
                public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {
                    Intent intent = new Intent(getActivity(), PageLookActivity.class);
                    intent.putExtra(PageLookActivity.KEY_URL, RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + RetrofitUtil.PAGE_LOOK + "?id=" + mLtObject.get(position).getId() + "&share=1" + "&curLoginId=" + MainActivity.loginId);
                    intent.putExtra(PageLookActivity.KEY_IMG, mLtObject.get(position).getImage());
                    intent.putExtra(PageLookActivity.KEY_ID, mLtObject.get(position).getId() + "");
                    intent.putExtra(PageLookActivity.KEY_TITLE,mLtObject.get(position).getTitle());
                    Log.e("素材地址", mLtObject.get(position).getImage() + "");
                    getActivity().startActivity(intent);
                    mLtObject.get(position).setRead(mLtObject.get(position).getRead() + 1);
                    recyclerAdapterWithHF.notifyItemChanged(position);
                }
            });
        }

        mList.setAdapter(recyclerAdapterWithHF);
        mPcFlyt.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mLtObject.clear();
                mCurrenPager = 1;

                if (mPosition == 2) {
                    loadDataOur(isMe, mCurrenPager);
                } else {
                    loadData(1, mCurrenPager);
                }
            }
        });


        mPcFlyt.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                if (mPosition == 2) {
                    loadDataOur(isMe, ++mCurrenPager);
                } else {
                    loadData(1, ++mCurrenPager);
                }
            }
        });
    }


    private void loadData(int state, int currentPager) {
        mLoadingAlerDialog.show();
        Subscription sbGetCode = wrapObserverWithHttp(WorkService.getWorkService().getWorks((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""), mPosition, state, currentPager)).subscribe(new Subscriber<Works>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                removeErrorView();
                mPcFlyt.setLoadMoreEnable(true);
                mPcFlyt.loadMoreComplete(true);
                mPcFlyt.refreshComplete();
                mLoadingAlerDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                removeLoadingView();
                showErrorView();
                e.printStackTrace();
                mLoadingAlerDialog.dismiss();
            }

            @Override
            public void onNext(Works works) {
                if ("1".equals(works.getState())) {
                    mAdapter.addDate(works.getPageList());
                } else {
                    showTest("网络异常");
                }

            }
        });
        ((BaseActivityTwoV) getActivity()).addSubscription(sbGetCode);
    }

    private void loadDataOur(int isMem, int currentPager) {
        mLoadingAlerDialog.show();
        Subscription sbGetCode = wrapObserverWithHttp(WorkService.getWorkService().pagesMeAll((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""), isMem, currentPager,"")).subscribe(new Subscriber<Works>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                removeErrorView();
                mPcFlyt.setLoadMoreEnable(true);
                mPcFlyt.loadMoreComplete(true);
                mPcFlyt.refreshComplete();
                mLoadingAlerDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                removeLoadingView();
                showErrorView();
                e.printStackTrace();
                mLoadingAlerDialog.dismiss();
            }

            @Override
            public void onNext(Works works) {
                if ("1".equals(works.getState())) {
                    mAdapter.addDate(works.getPageList());
                } else {
                    showTest("网络异常");
                }

            }
        });
        ((BaseActivityTwoV) getActivity()).addSubscription(sbGetCode);
    }

}
