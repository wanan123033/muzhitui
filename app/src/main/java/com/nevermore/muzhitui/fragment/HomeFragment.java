package com.nevermore.muzhitui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.chanven.lib.cptr.PtrClassicDefaultHeader;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.nevermore.muzhitui.MainActivity;
import com.nevermore.muzhitui.MyCard;
import com.nevermore.muzhitui.PageLookActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.DynamicPostedActivity;
import com.nevermore.muzhitui.activity.FeedbackActivity;
import com.nevermore.muzhitui.activity.MyModeTwoActivty;
import com.nevermore.muzhitui.activity.MztProxyProtocolActivity;
import com.nevermore.muzhitui.activity.NetWorkSnapActivity;
import com.nevermore.muzhitui.activity.PopularizeActivity;
import com.nevermore.muzhitui.activity.QrHomeActivity;
import com.nevermore.muzhitui.activity.QunExchangeActivity;
import com.nevermore.muzhitui.activity.SnapshotActivity;
import com.nevermore.muzhitui.activity.SystemMsgActivity;
import com.nevermore.muzhitui.activity.TabFireWorkActivity;
import com.nevermore.muzhitui.activity.ThumbChangeActivity;
import com.nevermore.muzhitui.activity.VideoDownActivity;
import com.nevermore.muzhitui.activity.VidioActivity;
import com.nevermore.muzhitui.activity.WxAddFriendActivity;
import com.nevermore.muzhitui.activity.editPhoto.OriginalArticleActivity;
import com.nevermore.muzhitui.module.bean.TopMaterial;
import com.nevermore.muzhitui.module.bean.Video;
import com.nevermore.muzhitui.module.bean.Works;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.BaseFragment;
import base.ImageUtil;
import base.MyLogger;
import base.RoastAdapter;
import base.SPUtils;
import base.UIUtils;
import base.helper.PhotoActionHelper;
import base.network.RetrofitUtil;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
import base.recycler.recyclerview.OnItemClickListener;
import base.thread.Handler;
import base.view.DragPointView;
import base.view.LoadingAlertDialog;
import base.view.RoundImageView;
import base.view.ScrollView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Administrator on 2018/1/2.
 */

public class HomeFragment extends BaseFragment {

    @BindView(R.id.rv_yc)
    RecyclerView rv_yc;
    @BindView(R.id.pcFlyt)
    PtrClassicFrameLayout mPcFlyt;
    @BindView(R.id.iv_message)
    ImageView iv_message;
    @BindView(R.id.seal_num_message)
    DragPointView civ_msg;
    @BindView(R.id.rpv)
    ViewPager rpv;
    @BindView(R.id.scrollview)
    ScrollView scrollview;
    @BindView(R.id.rv_video)
    RecyclerView rv_video;

    private LoadingAlertDialog mLoadingAlertDialog;
    private CommonAdapter mYcAdapter,mVideoAdapter;
    private int mCurrenPager;
    private List<Works.PageListBean> mListObject = new ArrayList<Works.PageListBean>();
    private RoastAdapter mTopAdapter;


    final Runnable scrollRunnable = new Runnable() {
        @Override
        public void run() {
            if (rpv != null && mTopAdapter != null){
                int current = rpv.getCurrentItem();
                if(current == mTopAdapter.getCount() - 1){
                    rpv.setCurrentItem(0);
                }else {
                    current++;
                    rpv.setCurrentItem(current);
                }
            }
            Handler.getHandler().postDelayed(scrollRunnable,5000);
        }
    };
    @Override
    public int createSuccessView() {
        return R.layout.fragment_home;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //解决上下滑动卡顿的问题（主要原因：该RecycleView嵌套在ScrollView中事件冲突的问题）
        rv_yc.setHasFixedSize(true);
        rv_yc.setNestedScrollingEnabled(false);
        rv_video.setHasFixedSize(true);
        rv_video.setNestedScrollingEnabled(false);

        mLoadingAlertDialog = new LoadingAlertDialog(getActivity());

        mYcAdapter = new CommonAdapter<Works.PageListBean>(getActivity(), R.layout.item_index_fragment, mListObject) {
            @Override
            public void convert(ViewHolder holder, Works.PageListBean o) {
                holder.setImageURL(R.id.ivItemIndexfImage,  o.getTitle_pic(), false);
                Log.e("首页标题图片url","===="+o.getTitle_pic());
                holder.setText(R.id.ivItemIndexfTitle, o.getTitle());
                holder.setText(R.id.ivItemIndexfName, "发布者："+o.getUser_name());
                holder.setText(R.id.ivItemIndexfReadNum, o.getRead() + " 阅读");
                holder.setText(R.id.ivItemIndexfData, o.getUpdate_time());
            }
        };

        RecyclerAdapterWithHF recyclerAdapterWithHF = new RecyclerAdapterWithHF(mYcAdapter);
        recyclerAdapterWithHF.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {
                Works.PageListBean bean = mListObject.get(position);
                Intent intent = new Intent(getActivity(), PageLookActivity.class);
                intent.putExtra(PageLookActivity.KEY_URL, RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + RetrofitUtil.PAGE_LOOK_OWN + "?id=" + bean.getId() + "&share=1"+ "&curLoginId=" + MainActivity.loginId+"&isHomePage=1"+"&isHomePage=1");
                if (!TextUtils.isEmpty(bean.getTitle_pic())) {
                    String url = RetrofitUtil.API_URL_HOU + RetrofitUtil.PROJECT_URL;
                    int aa = bean.getTitle_pic().indexOf(url) + url.length();

                    String ml = bean.getTitle_pic().substring(aa);
                    Log.e("title pic:", aa + "\t" + ml);
                    intent.putExtra(PageLookActivity.KEY_IMG, ml);
                }

                intent.putExtra(PageLookActivity.KEY_ID, bean.getId() + "");

                intent.putExtra("isOriginal",true);//是否是原创，因为不需要在首页显示红笔，所以判断是否是原创也都不需要了，可以通过原创判断分享出去的是否显示底板
                intent.putExtra("isShowEdit", true);//不显示编辑红笔提示
                getActivity().startActivity(intent);
                bean.setRead(bean.getRead() + 1);
                mYcAdapter.notifyItemChanged(position);
            }
        });
        rv_yc.setAdapter(recyclerAdapterWithHF);
        //设置下拉刷新
        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(getActivity());
        mPcFlyt.addPtrUIHandler(header);
        mPcFlyt.setHeaderView(header);
        mPcFlyt.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mListObject.clear();
                mCurrenPager=1;
                loadDataOur(mCurrenPager);
            }
        });

        loadData();
        loadDataOur(mCurrenPager = 1);
        iv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 跳转到系统消息界面
                civ_msg.setText("0");
                civ_msg.setVisibility(View.GONE);
                baseStartActivity(SystemMsgActivity.class);
            }
        });

        mPcFlyt.setLastUpdateTimeRelateObject(this);
        scrollview.setOnScrollListener(new ScrollView.OnScrollListener() {
            @Override
            public void scrollTop() {
                mListObject.clear();
                loadData();
                mCurrenPager = 1;
                loadDataOur(mCurrenPager);
            }

            @Override
            public void scrollDown() {
                loadDataOur(++mCurrenPager);
            }
        });

        mVideoAdapter = new CommonAdapter<Video.VedioArrayBean>(getActivity(),R.layout.item_video_list,new ArrayList<Video.VedioArrayBean>()) {
            @Override
            public void convert(ViewHolder holder, final Video.VedioArrayBean video) {
                ImageLoader.getInstance().displayImage(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + video.getImg_url(),
                        (ImageView) holder.getView(R.id.iv_imgContent), ImageUtil.getInstance().getBaseDisplayOption());
                holder.setText(R.id.tv_name,video.getTitle());

                holder.setOnClickListener(R.id.iv_play, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        videoOnClick(video);
                    }
                });
                RoundImageView riv = holder.getView(R.id.iv_imgContent);
                riv.setRound(5);
            }
        };
        rv_video.setAdapter(mVideoAdapter);
        mVideoAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                videoOnClick((Video.VedioArrayBean) o);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                return false;
            }
        });
        getVideoCourse(3);
    }

    private void videoOnClick(Video.VedioArrayBean video) {
        boolean isShared = false;
        if (video.getIs_share() == 0) {
            isShared = true;
        }
        Log.e("share list:", video.getIs_share() + "\t是否分享：" + isShared + "\t需要复制的链接：" + video.getJump_url_c());
        Log.e("share item:", video.toString());
        Intent intent = new Intent(getActivity(), PageLookActivity.class);
        intent.putExtra(PageLookActivity.KEY_URL, video.getJump_url());
        intent.putExtra(PageLookActivity.KEY_IS_SHARE, isShared);//0能分享链接  1 不能分享链接
        intent.putExtra(PageLookActivity.KEY_IS_SHARE_LINK, video.getJump_url_c());//不能分享时需复制链接
        intent.putExtra(PageLookActivity.KEY_TEXT, "这个软件可以把你的产品或项目推广到整个社交生态圈的推广神器！");

        startActivity(intent);
    }

    private void getVideoCourse(final int flag) {
        mLoadingAlertDialog.show();
        MyLogger.kLog().e("getVideoCourse mLoadingAlertDialog 开启");
        Subscription sbGetVideoCourse = wrapObserverWithHttp(WorkService.getWorkService().getVideoCourse((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""), flag)).subscribe(new Subscriber<Video>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                mLoadingAlertDialog.dismiss();
                MyLogger.kLog().e("getVideoCourse mLoadingAlertDialog 关闭");
            }

            @Override
            public void onError(Throwable e) {
                removeLoadingView();
                removeErrorView();
                mLoadingAlertDialog.dismiss();
                showTest("服务器连接失败");
                e.printStackTrace();
                MyLogger.kLog().e("getVideoCourse mLoadingAlertDialog 关闭");
            }

            @Override
            public void onNext(final Video video) {
                if (video.getState().equals("1")) {
                    mVideoAdapter.addDate(video.getVedioArray());
                } else {
                    showTest("视频加载失败");
                }


            }
        });
        ((BaseActivityTwoV)getActivity()).addSubscription(sbGetVideoCourse);

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
                    if(mTopAdapter == null){
                        List<String> mData = new ArrayList<String>();
                        final List<TopMaterial.ScreensBean> screens = topMaterial.getScreens();
                        for(TopMaterial.ScreensBean bean : screens){
                            mData.add(bean.getImg_url());
                        }
                        mTopAdapter = new RoastAdapter(mData,getActivity());
                        mTopAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getActivity(), PageLookActivity.class);
                                intent.putExtra(PageLookActivity.KEY_URL,screens.get(position).getJump_url() );
                                intent.putExtra(PageLookActivity.KEY_IMG, screens.get(position).getImg_url());
                                intent.putExtra(PageLookActivity.KEY_ID, screens.get(position).getId() + "");

                                intent.putExtra("isShowEdit", true);//不显示编辑红笔提示
                                startActivity(intent);
                            }
                        });
                        rpv.setAdapter(mTopAdapter);
                        rpv.setCurrentItem(0);

                        //TODO 自动轮播
                        Handler.getHandler().postDelayed(scrollRunnable,5000);
                    }
                }
            }
        });
        ((BaseActivityTwoV) getActivity()).addSubscription(sbMyAccount);
    }

    private void loadDataOur(int currentPager) {
        mLoadingAlertDialog.show();
        MyLogger.kLog().e("loadDataOur mLoadingAlertDialog 开启");
        Subscription sbGetCode = wrapObserverWithHttp(WorkService.getWorkService().pagesMeAllGood((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""), currentPager)).subscribe(new Subscriber<Works>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
//                mPcFlyt.setLoadMoreEnable(true);
//                mPcFlyt.loadMoreComplete(true);
                mPcFlyt.refreshComplete();
                mLoadingAlertDialog.dismiss();
                MyLogger.kLog().e("loadDataOur mLoadingAlertDialog 关闭");

            }

            @Override
            public void onError(Throwable e) {
                removeLoadingView();
//                removeErrorView();
                e.printStackTrace();
                mLoadingAlertDialog.dismiss();
                showTest(mNetWorkError+"精品原创请求错误");
                MyLogger.kLog().e("loadDataOur mLoadingAlertDialog 关闭");
            }

            @Override
            public void onNext(Works works) {
                if ("1".equals(works.getState())) {
                    mYcAdapter.addDate(works.getPageList());
                } else {
                    showTest("出问题了  精品原创");
                }
                mLoadingAlertDialog.dismiss();
                MyLogger.kLog().e("loadDataOur mLoadingAlertDialog 关闭");
            }
        });
        ((BaseActivityTwoV) getActivity()).addSubscription(sbGetCode);
    }

    @OnClick({R.id.ll_miaobianwenzhang,R.id.ll_wenzhangzhizuo,R.id.ll_dynamic_posted,R.id.ll_pop,
    R.id.ll_wx_addfriend,R.id.ll_qun,R.id.ll_down_video,R.id.ll_qrcode,R.id.ll_pin_snap,R.id.ll_network,R.id.ll_wenzhangku,R.id.ll_gongnengxuqiu,
    R.id.ll_video_kili,R.id.ll_mpsz,R.id.ll_make,R.id.ll_sqdl})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_miaobianwenzhang:
                baseStartActivity(ThumbChangeActivity.class);
                break;
            case R.id.ll_wenzhangzhizuo:
                baseStartActivity(OriginalArticleActivity.class);
                break;
            case R.id.ll_dynamic_posted:
                baseStartActivity(DynamicPostedActivity.class);
                break;
            case R.id.ll_pop:
                baseStartActivity(PopularizeActivity.class);
                break;
            case R.id.ll_wx_addfriend:
                baseStartActivity(WxAddFriendActivity.class);
                break;
            case R.id.ll_qun:
                baseStartActivity(QunExchangeActivity.class);
                break;
            case R.id.ll_down_video:
                baseStartActivity(VideoDownActivity.class);
                break;
            case R.id.ll_qrcode:
                baseStartActivity(QrHomeActivity.class);
                break;
            case R.id.ll_pin_snap:
                pinSnap();
                break;
            case R.id.ll_network:
                baseStartActivity(NetWorkSnapActivity.class);
                break;
            case R.id.ll_wenzhangku:
                baseStartActivity(TabFireWorkActivity.class);
                break;
            case R.id.ll_gongnengxuqiu:
                baseStartActivity(FeedbackActivity.class);
                break;
            case R.id.ll_video_kili:
                baseStartActivity(VidioActivity.class);
                break;
            case R.id.ll_mpsz:
                baseStartActivity(MyCard.class);
                break;
            case R.id.ll_make:
                baseStartActivity(MyModeTwoActivty.class);
                break;
            case R.id.ll_sqdl:
                baseStartActivity(MztProxyProtocolActivity.class);
                break;
        }
    }

    private void pinSnap() {
//        ImageUtil.getInstance().chooseImage("开始制作",new GalleryFinal.OnHanlderResultCallback() {
//            @Override
//            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
//                if (reqeustCode == 133 && resultList != null && !resultList.isEmpty()) {
//                    ArrayList<String> paths = new ArrayList<>();
//                    for(int i = 0 ; i < resultList.size() ; i++){
//                        paths.add(resultList.get(i).getPhotoPath());
//                    }
//                    Intent intent = new Intent(getActivity(), SnapshotActivity.class);
//                    intent.putStringArrayListExtra(SnapshotActivity.IMAGES_PATH,paths);
//                    startActivity(intent);
//                }
//            }
//            @Override
//            public void onHanlderFailure(int requestCode, String errorMsg) {
//
//            }
//        }, 36);
        GalleryFinal.openGalleryMuti(133, 36, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (reqeustCode == 133 && resultList != null && !resultList.isEmpty()) {
                    ArrayList<String> paths = new ArrayList<>();
                    for(int i = 0 ; i < resultList.size() ; i++){
                        try {
                            paths.add(URLDecoder.decode(resultList.get(i).getPhotoPath(), "UTF-8"));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    Intent intent = new Intent(getActivity(), SnapshotActivity.class);
                    intent.putStringArrayListExtra(SnapshotActivity.IMAGES_PATH,paths);
                    intent.putExtra(SnapshotActivity.RIGHT_STATE,1);
                    startActivity(intent);
                }
            }
            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {

            }
        });
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
