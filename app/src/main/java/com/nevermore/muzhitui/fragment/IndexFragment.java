package com.nevermore.muzhitui.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chanven.lib.cptr.PtrClassicDefaultHeader;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.nevermore.muzhitui.MainActivity;
import com.nevermore.muzhitui.MyCard;
import com.nevermore.muzhitui.activity.MyModeTwoActivty;
import com.nevermore.muzhitui.PageLookActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.PopularizeActivity;
import com.nevermore.muzhitui.activity.SystemMsgActivity;
import com.nevermore.muzhitui.activity.TabFireWorkActivity;
import com.nevermore.muzhitui.activity.VidioActivity;
import com.nevermore.muzhitui.module.bean.TopMaterial;
import com.nevermore.muzhitui.module.bean.Works;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import base.BaseActivityTwoV;
import base.BaseFragment;
import base.ImageUtil;
import base.RoastAdapter;
import base.SPUtils;
import base.network.RetrofitUtil;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;

import base.view.DragPointView;
import base.view.LinearLayoutManagerWrapper;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.Subscription;


/**
 * Created by hehe on 2016/5/4.
 */
public class IndexFragment extends BaseFragment {


    private static final int UPTATE_VIEWPAGER = 1258;
    @BindView(R.id.ListViewForScrollView)
    RecyclerView mListViewForScrollView;
    @BindView(R.id.pcFlyt)
    PtrClassicFrameLayout mPcFlyt;
    @BindView(R.id.iv_message)
    ImageView iv_message;
    @BindView(R.id.seal_num_message)
    DragPointView civ_msg;


    private List<TopMaterial.ScreensBean> mList = new ArrayList<>();
    private TestLoopAdapter mLoopAdapter;
    private LoadingAlertDialog mLoadingAlertDialog;


    private CommonAdapter mAdapter;
    private RecyclerAdapterWithHF recyclerAdapterWithHF;

    private int mCurrenPager = 1;
    private RoastAdapter adapter;
    List<Works.PageListBean> mLtObject = new ArrayList<>();

    ViewPager mRpv;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPTATE_VIEWPAGER:
                    if(mRpv != null) {
                        int current = mRpv.getCurrentItem();
                        mRpv.setCurrentItem(++current);
                    }
                    break;
            }
        }
    };

    @Override
    public int createSuccessView() {
        return R.layout.fragment_index;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mLoadingAlertDialog = new LoadingAlertDialog(getActivity());


        showLoadingView();
        loadDataOur( mCurrenPager);//精品原创
        loadData();
        mListViewForScrollView.setLayoutManager(new LinearLayoutManagerWrapper(getActivity(), LinearLayoutManager.VERTICAL, false));//设置下拉刷新控件抛出异常崩溃捕捉。
        mAdapter = new CommonAdapter<Works.PageListBean>(getActivity(), R.layout.item_index_man_fragment, mLtObject) {
            @Override
            public void convert(ViewHolder holder, Works.PageListBean o) {

                if (o.equals(mLtObject.get(0))) {
                    holder.setVisible(R.id.llIndexMain, true);
                } else {
                    holder.setVisible(R.id.llIndexMain, false);
                }

                mRpv = holder.getView(R.id.rpv);
//                mLoopAdapter = new TestLoopAdapter(mRpv);
//                mRpv.setAdapter(mLoopAdapter);
//                mRpv.setHintView(new IconHintView(getActivity(), R.drawable.shape_point_focuse, R.drawable.shape_point_normal, UIUtils.dip2px(12)));

                List<String> urls = new ArrayList<>();
                for (int i = 0 ; i < mList.size(); i++){
                    urls.add(mList.get(i).getImg_url());
                }

                adapter = new RoastAdapter(urls,getActivity());
//                LinearLayout ll_top = holder.getView(R.id.ll_top);
//                ll_top.removeAllViews();
//                for (ImageView imageView : views)
//                    ll_top.addView(imageView);

                adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(), PageLookActivity.class);
                        intent.putExtra(PageLookActivity.KEY_URL,mList.get(position).getJump_url() );
                        intent.putExtra(PageLookActivity.KEY_IMG, mList.get(position).getImg_url());
                        intent.putExtra(PageLookActivity.KEY_ID, mList.get(position).getId() + "");

                        intent.putExtra("isShowEdit", true);//不显示编辑红笔提示
                        startActivity(intent);
                    }
                });
                mRpv.setAdapter(adapter);
                holder.setOnClickListener(R.id.ivIndexVidio, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        baseStartActivity(VidioActivity.class);//视频专区
                    }
                });
                holder.setOnClickListener(R.id.ivIndexCard, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        baseStartActivity(MyCard.class);//我的名片
                    }
                });
                holder.setOnClickListener(R.id.ivIndexModel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        baseStartActivity(MyModeTwoActivty.class);//我的模板
                    }
                });
                holder.setOnClickListener(R.id.ivIndexToPromote, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        baseStartActivity(PopularizeActivity.class);//去推广
                    }
                });
                if (SPUtils.get(SPUtils.IS_PUBLIC,"").equals("1")){
                    holder.setVisible(R.id.ivIndexPublicArticles,true);
                }else{
                    holder.setVisible(R.id.ivIndexPublicArticles,false);
                }
                holder.setVisible(R.id.ivIndexPublicArticles,true);
                holder.setOnClickListener(R.id.ivIndexPublicArticles, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        baseStartActivity(TabFireWorkActivity.class);//公共文章
                    }
                });

                holder.setImageURL(R.id.ivItemIndexfImage,  o.getTitle_pic(), false);
                Log.e("首页标题图片url","===="+o.getTitle_pic());
                holder.setText(R.id.ivItemIndexfTitle, o.getTitle());
                holder.setText(R.id.ivItemIndexfName, "发布者："+o.getUser_name());
                holder.setText(R.id.ivItemIndexfReadNum, o.getRead() + " 阅读");
                holder.setText(R.id.ivItemIndexfData, o.getUpdate_time());
            }
        };

        recyclerAdapterWithHF = new RecyclerAdapterWithHF(mAdapter);
        recyclerAdapterWithHF.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {
                Intent intent = new Intent(getActivity(), PageLookActivity.class);
                intent.putExtra(PageLookActivity.KEY_URL, RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + RetrofitUtil.PAGE_LOOK_OWN + "?id=" + mLtObject.get(position).getId() + "&share=1"+ "&curLoginId=" + MainActivity.loginId+"&isHomePage=1"+"&isHomePage=1");
                if (!TextUtils.isEmpty(mLtObject.get(position).getTitle_pic())) {
                    String url = RetrofitUtil.API_URL_HOU + RetrofitUtil.PROJECT_URL;
                    int aa = mLtObject.get(position).getTitle_pic().indexOf(url) + url.length();

                    String ml = mLtObject.get(position).getTitle_pic().substring(aa);
                    Log.e("title pic:", aa + "\t" + ml);
                    intent.putExtra(PageLookActivity.KEY_IMG, ml);
                }

                intent.putExtra(PageLookActivity.KEY_ID, mLtObject.get(position).getId() + "");

                intent.putExtra("isOriginal",true);//是否是原创，因为不需要在首页显示红笔，所以判断是否是原创也都不需要了，可以通过原创判断分享出去的是否显示底板
                intent.putExtra("isShowEdit", true);//不显示编辑红笔提示
                getActivity().startActivity(intent);
                mLtObject.get(position).setRead(mLtObject.get(position).getRead() + 1);
                recyclerAdapterWithHF.notifyItemChanged(position);
            }
        });
        mListViewForScrollView.setAdapter(recyclerAdapterWithHF);
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
                mLtObject.clear();
                mCurrenPager=1;
                loadData();
                loadDataOur(mCurrenPager);
            }
        });


        mPcFlyt.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                loadDataOur( ++mCurrenPager);
            }
        });

        iv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 跳转到系统消息界面
                civ_msg.setText("0");
                civ_msg.setVisibility(View.GONE);
                baseStartActivity(SystemMsgActivity.class);
            }
        });
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = UPTATE_VIEWPAGER;
                if(mRpv != null) {
                    int autoCurrIndex = mRpv.getCurrentItem();
                    int sum = mRpv.getAdapter().getCount();
                    if (autoCurrIndex == sum) {
                        autoCurrIndex = 0;
                    }else {
                        autoCurrIndex++;
                    }
                    message.arg1 = autoCurrIndex;
                    mHandler.sendMessage(message);
                }
            }
        }, 5000, 5000);
    }


    private void loadData() {
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().toPages((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))).subscribe(new Subscriber<TopMaterial>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                showTest(mNetWorkError+"广告轮播请求错误");
            }

            @Override
            public void onNext(TopMaterial topMaterial) {
                if ("1".equals(topMaterial.getState())) {
                    mList.clear();
                    List<TopMaterial.ScreensBean> screens = topMaterial.getScreens();
                    if(screens != null && !screens.isEmpty()) {
                        mList.addAll(screens);
//                    views.clear();
//                    for (int i = 0; i < mList.size(); i++) {
//                        ImageView imageView = new ImageView(getActivity());
//                        imageView.setBackgroundColor(Color.GRAY);
//                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
//                        params.setMargins(5, 0, 5, 0);
//                        imageView.setLayoutParams(params);
//                        views.add(imageView);
//
//                    }
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    showTest("广告轮播出错");
               }

            }
        });
        ((BaseActivityTwoV) getActivity()).addSubscription(sbMyAccount);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private class TestLoopAdapter extends LoopPagerAdapter {

        public TestLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, final int position) {
            ImageView view = new ImageView(container.getContext());
            final TopMaterial.ScreensBean screensBean = mList.get(position);

            ImageLoader.getInstance().displayImage(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + screensBean.getImg_url(), view, ImageUtil.getInstance().getBaseDisplayOption());
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (screensBean!=null&screensBean.getJump_url()!=null){
                            Intent intent = new Intent(getActivity(), PageLookActivity.class);
                            intent.putExtra(PageLookActivity.KEY_URL,screensBean.getJump_url() );
                            intent.putExtra(PageLookActivity.KEY_IMG, screensBean.getImg_url());
                            intent.putExtra(PageLookActivity.KEY_ID, screensBean.getId() + "");

                            intent.putExtra("isShowEdit", true);//不显示编辑红笔提示
                            startActivity(intent);
                            Log.e("onclicklistener:",screensBean.getJump_url());
                        }

                    }
                });


            return view;
        }

        @Override
        public int getRealCount() {
            return mList.size();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void loadDataOur( int currentPager) {
        mLoadingAlertDialog.show();
        Subscription sbGetCode = wrapObserverWithHttp(WorkService.getWorkService().pagesMeAllGood((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""), currentPager)).subscribe(new Subscriber<Works>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                removeErrorView();
                mPcFlyt.setLoadMoreEnable(true);
                mPcFlyt.loadMoreComplete(true);
                mPcFlyt.refreshComplete();
                mLoadingAlertDialog.dismiss();

            }

            @Override
            public void onError(Throwable e) {
                removeLoadingView();
//                removeErrorView();
                e.printStackTrace();
                mLoadingAlertDialog.dismiss();
                showTest(mNetWorkError+"精品原创请求错误");
            }

            @Override
            public void onNext(Works works) {
                if ("1".equals(works.getState())) {
                    mAdapter.addDate(works.getPageList());
                } else {
                    showTest("出问题了  精品原创");
                }
                mLoadingAlertDialog.dismiss();
            }
        });
        ((BaseActivityTwoV) getActivity()).addSubscription(sbGetCode);
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        String system_msg = args.getString("system_msg");
        int count = Integer.parseInt(civ_msg.getText().toString());
        if(count < 100){
            civ_msg.setText(++count);
        }else{
            civ_msg.setText("99");
        }
        civ_msg.setVisibility(View.VISIBLE);
    }
}
