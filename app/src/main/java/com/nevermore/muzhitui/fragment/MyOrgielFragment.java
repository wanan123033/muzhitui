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

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.nevermore.muzhitui.MainActivity;
import com.nevermore.muzhitui.PageLookActivity;
import com.nevermore.muzhitui.PagerEditActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.editPhoto.OriginalArticleEditActivity;
import com.nevermore.muzhitui.activity.NewFriendsActivity;
import com.nevermore.muzhitui.module.bean.Code;
import com.nevermore.muzhitui.module.bean.Works;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.BaseFragment;
import base.ImageUtil;
import base.MyLogger;
import base.RecyclerBaseAdapter;
import base.SPUtils;
import base.UIUtils;
import base.network.RetrofitUtil;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import rx.Subscriber;
import rx.Subscription;

public class MyOrgielFragment extends BaseFragment {


    private static final String USERID = "userId";
    @BindView(R.id.list)
    RecyclerView mList;
    List<Works.PageListBean> mLtObject = new ArrayList<>();
    @BindView(R.id.pcFlyt)
    PtrClassicFrameLayout pcFlyt;
    private RecyclerBaseAdapter mAdapter;
    private int mCurrenPager = 1;
    public static final String KEY_POSITION = "POSITION";

    private int mAllPages;
    private RecyclerAdapterWithHF recyclerAdapterWithHF;
    private LoadingAlertDialog mLoadingAlertDialog;
    private android.app.AlertDialog alertDialog;
    private String userId;

    @Override
    public int createSuccessView() {

        return R.layout.fragment_mywork_list;
    }

    @Override
    public void getFragmentArguments(Bundle bundle) {
        super.getFragmentArguments(bundle);
        userId = bundle.getString(USERID, (String) SPUtils.get(SPUtils.GET_LOGIN_ID,""));


    }

    public static MyOrgielFragment newInstance(int position,String userId) {
        MyOrgielFragment myWorksWorksFragment = new MyOrgielFragment();
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
        userId = bundle.getString(USERID, (String) SPUtils.get(SPUtils.GET_LOGIN_ID,""));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLtObject.clear();
        mLoadingAlertDialog = new LoadingAlertDialog(getActivity());
        mAdapter = new RecyclerBaseAdapter<Works.PageListBean>(getActivity(), mLtObject, R.layout.item_index_me_fragment, new RecyclerBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.tvEdit:
                        Intent intentOriginal = new Intent(getActivity(), OriginalArticleEditActivity.class);
                        intentOriginal.putExtra(PagerEditActivity.KEY_ID, mLtObject.get(position).getId() + "");
                        startActivity(intentOriginal);
                        break;
                    case R.id.tvDelete:
                        if(SPUtils.get(SPUtils.GET_LOGIN_ID,"").equals(userId)) {
                            delete(mLtObject.get(position));
                        }else {
                            showTest("删除失败！");
                        }
                        break;
                    case R.id.rlyt:
                        Intent intent = new Intent(getActivity(), PageLookActivity.class);
                        intent.putExtra(PageLookActivity.KEY_URL, RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + RetrofitUtil.PAGE_LOOK_OWN + "?id=" + mLtObject.get(position).getId() + "&share=1"+ "&curLoginId=" + MainActivity.loginId);
                        if (!TextUtils.isEmpty(mLtObject.get(position).getTitle_pic())) {
                            String url = RetrofitUtil.API_URL_HOU + RetrofitUtil.PROJECT_URL;
                            int aa = mLtObject.get(position).getTitle_pic().indexOf(url) + url.length();

                            String ml = mLtObject.get(position).getTitle_pic().substring(aa);
                            Log.e("title pic:", aa + "\t" + ml);
                            intent.putExtra(PageLookActivity.KEY_IMG, ml);
                        }
                        intent.putExtra(PageLookActivity.KEY_ID, mLtObject.get(position).getId() + "");

                        intent.putExtra("isOriginal",true);
                        if(!SPUtils.get(SPUtils.GET_LOGIN_ID,"").equals(userId)) {
                            intent.putExtra(PageLookActivity.IS_SHOWEDIT,true);
                            intent.putExtra("isOriginal",false);
                        }
                        getActivity().startActivity(intent);
                        mLtObject.get(position).setRead(mLtObject.get(position).getRead() + 1);
                        mAdapter.notifyItemChanged(position);
                        break;
                }
            }
        }) {
            @Override
            public void fillData(ViewHolder viewHolder, Works.PageListBean data,final int position) {
               ImageLoader.getInstance().displayImage( data.getTitle_pic(), (ImageView) viewHolder.getView(R.id.ivItemIndexfImage), ImageUtil.getInstance().getBaseDisplayOption());

                viewHolder.setText(R.id.ivItemIndexfTitle, data.getTitle());
                viewHolder.setText(R.id.ivItemIndexfReadNum, data.getRead()+" 阅读"  );
                viewHolder.setText(R.id.ivItemIndexfData, data.getUpdate_time());
                viewHolder.setText(R.id.ivItemIndexfName, "发布者："+data.getUser_name());
                if(SPUtils.get(SPUtils.GET_LOGIN_ID,"").equals(userId)) {
                    viewHolder.getView(R.id.tvEdit).setVisibility(View.VISIBLE);
                    viewHolder.getView(R.id.tvDelete).setVisibility(View.VISIBLE);
                }else {
                    viewHolder.getView(R.id.tvEdit).setVisibility(View.GONE);
                    viewHolder.getView(R.id.tvDelete).setVisibility(View.GONE);
                }
                LinearLayout rlyt=viewHolder.getView(R.id.rlyt);
                if(SPUtils.get(SPUtils.GET_LOGIN_ID,"").equals(userId)) {
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





    private void delete(final Works.PageListBean bean) {
        mLoadingAlertDialog.show();
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().del((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),bean.getId())).subscribe(new Subscriber<Code>() {
            @Override
            public void onCompleted() {

                mLoadingAlertDialog.dismiss();
               // showTest("删除成功");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mLoadingAlertDialog.dismiss();
                showTest(mNetWorkError);
            }

            @Override
            public void onNext(Code code) {
                if (code.getState().equals("1")) {//
                    showTest("删除成功");
                    mLtObject.remove(bean);
                    mAdapter.notifyDataSetChanged();
                } else {
                    showTest(code.getMsg());
                }
            }
        });
        ((BaseActivityTwoV) getActivity()).addSubscription(sbMyAccount);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
    private void loadData( int currentPager) {
        MyLogger.kLog().e("userId="+userId);
        Subscription sbGetCode = wrapObserverWithHttp(
                WorkService.getWorkService().pagesMeAll((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),2, currentPager,userId)
        ).subscribe(new Subscriber<Works>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                removeErrorView();
                pcFlyt.setLoadMoreEnable(true);
                pcFlyt.loadMoreComplete(true);
                if (mCurrenPager == mAllPages) {
                    pcFlyt.setLoadMoreEnable(false);
                }
            }

            @Override
            public void onError(Throwable e) {  //请求失败
                removeLoadingView();
                showErrorView();
                e.printStackTrace();
            }

            @Override
            public void onNext(Works works) {    //请求成功
                if ("1".equals(works.getState())) {//

                    mLtObject.addAll(works.getPageList());
                    mAdapter.notifyDataSetChanged();
                    mAllPages = works.getAllPages();
                } else {
                    showTest(mServerEror);
                }

            }
        });
        ((BaseActivityTwoV) getActivity()).addSubscription(sbGetCode);
    }

}
