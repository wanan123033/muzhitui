package com.nevermore.muzhitui.fragment;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicDefaultHeader;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.nevermore.muzhitui.EventBusContanct;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.DynamicDatailActivity;
import com.nevermore.muzhitui.activity.DynamicPersonActivity;
import com.nevermore.muzhitui.activity.DynamicPostedActivity;
import com.nevermore.muzhitui.activity.PhotoDetailActivity;
import com.nevermore.muzhitui.activity.SnapshotActivity;
import com.nevermore.muzhitui.activity.editPhoto.ImageItem;
import com.nevermore.muzhitui.activity.rongyun.json.DynamicMsg;
import com.nevermore.muzhitui.module.BaseBean;
import com.nevermore.muzhitui.module.bean.Bimp;
import com.nevermore.muzhitui.module.bean.DynamicBean;
import com.nevermore.muzhitui.module.bean.DynamicDel;
import com.nevermore.muzhitui.module.bean.ExceInfo;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.BaseFragment;
import base.ImageUtil;
import base.MyLogger;
import base.SPUtils;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
import base.thread.BaseRunnable;
import base.thread.ThreadManager;
import base.util.ShareUtil;
import base.view.AllTextView;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/9/8
 * 动态首页、我的动态  显示某人的所有动态   默认是动态首页
 */
//TODO   动态退出一个小时后刷一次  一小时之内下拉会刷
public class DynamicPage extends BaseFragment{

    public static final String DYNAMIC_PAGE_STATUS = "dynamic_page_status";
    public static final String DYNAMIC_USER_ID = "dynamic_uesrId";
    private String userId;
    private int type = 0;   //0  表示动态首页  1显示我的动态  2显示别人的动态
    @BindView(R.id.rv_dynamic)
    RecyclerView rv_dynamic;
    @BindView(R.id.pcFlyt)
    PtrClassicFrameLayout pcFlyt;
    @BindView(R.id.civ_posted)
    CircleImageView civ_posted;

    private int currentPage = 1;

    List<DynamicBean.Dynamic> dynamicBeanList = new ArrayList<>();

    private LoadingAlertDialog mLoadingAlertDialog;

    private CommonAdapter mAdapter;

    private RecyclerAdapterWithHF recyclerAdapterWithHF;

    @Override
    public int createSuccessView() {
        EventBus.getDefault().register(this);
        return R.layout.page_dynamic;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        type = args.getInt(DYNAMIC_PAGE_STATUS);
        userId = args.getString(DYNAMIC_USER_ID);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoadingAlertDialog = new LoadingAlertDialog(getActivity());
        final Drawable praise_nomal = getResources().getDrawable(R.mipmap.praise_nomal);
        final Drawable praise_finish = getResources().getDrawable(R.mipmap.praise_finish);
        praise_nomal.setBounds(0,0,32,32);
        praise_finish.setBounds(0,0,32,32);
        if(type == 0){
            civ_posted.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(0,0,0,40);
            pcFlyt.setLayoutParams(params);
        }else {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(0,0,0,0);
            pcFlyt.setLayoutParams(params);
            civ_posted.setVisibility(View.GONE);
        }
        mAdapter = new CommonAdapter<DynamicBean.Dynamic>(getActivity(),R.layout.item_dynamic,dynamicBeanList) {

            @Override
            public void convert(final ViewHolder holder, final DynamicBean.Dynamic o) {
                if(type == 0){  //表示是首页
                    holder.getView(R.id.tv_attention).setVisibility(View.VISIBLE);
                    holder.getView(R.id.tv_delete).setVisibility(View.GONE);
                    if(o.getLoginid() == Integer.parseInt((String) SPUtils.get(SPUtils.GET_LOGIN_ID,"0"))){
                        holder.getView(R.id.tv_delete).setVisibility(View.VISIBLE);
                        holder.getView(R.id.tv_attention).setVisibility(View.GONE);
                    }

                }else if(type == 1){  //表示是我的动态
                    holder.getView(R.id.tv_delete).setVisibility(View.VISIBLE);
                    holder.getView(R.id.tv_attention).setVisibility(View.GONE);
                }else if(type == 2){  //表示是别人的动态

                    holder.getView(R.id.tv_delete).setVisibility(View.GONE);
                    holder.getView(R.id.tv_attention).setVisibility(View.GONE);
                }
                holder.setText(R.id.tv_name, o.getUser_name());
                holder.setText(R.id.tv_date, o.getTime());
//                holder.setText(R.id.tv_dynamic_content, o.getContent());
                ((AllTextView)holder.getView(R.id.all_dynamic_content)).setText(o.getContent());
                ImageLoader.getInstance().displayImage(o.getHeadimg(), (ImageView) holder.getView(R.id.civ_topic));
                RecyclerView gridView = holder.getView(R.id.gv_photo_list);


                initLinearLayout(gridView,o);
                ((TextView)holder.getView(R.id.tv_praise)).setCompoundDrawables(o.getPraise_type() == 0 ? praise_nomal : praise_finish,null,null,null);
                ((TextView)holder.getView(R.id.tv_praise)).setText(" "+o.getPraise_count());
                holder.setOnClickListener(R.id.tv_praise, new View.OnClickListener() {   //点赞
                    @Override
                    public void onClick(View v) {
                        if(o.getPraise_type() == 0){
                            o.setPraise_type(1);
                            o.setPraise_count(o.getPraise_count() + 1);
                        }else{
                            o.setPraise_type(0);
                            o.setPraise_count(o.getPraise_count() - 1);
                        }
                        praise(o,o.getPraise_type());
                        ((TextView)holder.getView(R.id.tv_praise)).setCompoundDrawables(o.getPraise_type() == 0 ? praise_nomal : praise_finish,null,null,null);
                        ((TextView)holder.getView(R.id.tv_praise)).setText(o.getPraise_count()+"");
                    }
                });
                ((TextView)holder.getView(R.id.tv_discuss)).setText(" "+o.getReply_count());
                holder.setOnClickListener(R.id.tv_discuss, new View.OnClickListener() {  //评论
                    @Override
                    public void onClick(View v) {
                        //TODO 跳转到动态详情，并开始评论
                        gotoDetail(o,0);
                    }
                });

                holder.setOnClickListener(R.id.tv_snapshot, new View.OnClickListener() {  //快照
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),SnapshotActivity.class);
                        intent.putExtra(SnapshotActivity.DYNAMIC_CONTENT,o);
                        startActivity(intent);

                    }
                });
                holder.setOnClickListener(R.id.all_dynamic_content, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoDetail(o,1);
                    }
                });

                holder.setOnClickListener(R.id.tv_wxzf, new View.OnClickListener() {     //微信转发
                    @Override
                    public void onClick(View v) {
                        mLoadingAlertDialog.show();
                        ThreadManager.getInstance().run(new BaseRunnable() {
                            @Override
                            public void run() {
                                final List<DynamicBean.Pic> pics = o.getPics();
                                final List<File> files = new ArrayList<>();
                                if(pics == null || pics.isEmpty()){
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showTest("没有图片不能一键转发");
                                        }});
                                    return;
                                }
                                for(int i = 0 ; i < pics.size() ; i++){
                                    final String url = pics.get(i).getPage_picpath().replace("_s","");
                                    File file = new File((String) SPUtils.get(url,""));
                                    if(file.exists()){
                                        files.add(file);
                                        if(files.size() == pics.size()){
                                            startActivity(ShareUtil.wxzf(o.getContent(),files));
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mLoadingAlertDialog.dismiss();
                                                }
                                            });

                                        }
                                        continue;
                                    }
                                    ((BaseActivityTwoV)getActivity()).addSubscription(wrapObserverWithHttp(WorkService.getWorkService().download(url)).subscribe(new Subscriber<ResponseBody>() {
                                        @Override
                                        public void onCompleted() {
                                        }
                                        @Override
                                        public void onError(Throwable e) {
                                        }
                                        @Override
                                        public void onNext(ResponseBody body) {
                                            File file = new File(saveFile(body));
                                            files.add(file);
                                            //标识该图片已经下载
                                            SPUtils.put(url,file.getAbsolutePath());
                                            if(files.size() == pics.size()){
                                                startActivity(ShareUtil.wxzf(o.getContent(),files));
                                                mLoadingAlertDialog.dismiss();
                                            }
                                        }
                                    }));
                                }

                            }
                        });
                    }
                });
                holder.setOnClickListener(R.id.civ_topic, new View.OnClickListener() {   //动态个人资料
                    @Override
                    public void onClick(View v) {
                        if(type == 0) {
                            Intent intent = new Intent(getActivity(), DynamicPersonActivity.class);
                            intent.putExtra(DynamicPersonActivity.USERID, o.getLoginid() + "");
                            startActivity(intent);
                        }
                    }
                });
                //TODO 是否已关注逻辑判断
                if(o.getFans_type() == 0){  //显示 “+关注”
                    holder.setBackgroundRes(R.id.tv_attention,R.mipmap.attention);
                }else if(o.getFans_type() == 1){  //显示 “+已关注”
                    holder.setBackgroundRes(R.id.tv_attention,R.mipmap.attentioned);
                }else{  //是自己则不显示
                    holder.getView(R.id.tv_attention).setVisibility(View.GONE);
                }
                holder.setOnClickListener(R.id.tv_attention, new View.OnClickListener() {  //关注
                    @Override
                    public void onClick(View v) {
                        if(o.getFans_type() == 0){
                            o.setFans_type(1);
                            holder.setBackgroundRes(R.id.tv_attention,R.mipmap.attentioned);
                        }else if(o.getFans_type() == 1){
                            holder.setBackgroundRes(R.id.tv_attention,R.mipmap.attention);
                            o.setFans_type(0);
                        }
                        attention(o,o.getFans_type());
                    }
                });

                holder.setOnClickListener(R.id.tv_delete, new View.OnClickListener() {   //删除动态
                    @Override
                    public void onClick(View v) {
                        deleteDynamic(o);
                    }
                });


            }
        };
        recyclerAdapterWithHF = new RecyclerAdapterWithHF(mAdapter);
        rv_dynamic.setAdapter(recyclerAdapterWithHF);
        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(getActivity());
        pcFlyt.addPtrUIHandler(header);
        pcFlyt.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                dynamicBeanList.clear();
                currentPage = 1;
                loadData(currentPage);
            }

        });
        currentPage = 1;
        loadData(currentPage);

        pcFlyt.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                currentPage++;
                loadData(currentPage);
            }
        });
    }




//    /**
//     * 按时间查询动态列表
//     */
//    private void loadData() {
//        mLoadingAlertDialog.show();
//        ((BaseActivityTwoV)getActivity()).addSubscription(wrapObserverWithHttp(WorkService.getWorkService().listDtAll((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),begin_time,end_time,userId)).subscribe(new Subscriber<DynamicBean>() {
//            @Override
//            public void onCompleted() {
//                removeLoadingView();
//                pcFlyt.setLoadMoreEnable(true);
//                pcFlyt.loadMoreComplete(true);
//                pcFlyt.refreshComplete();
//                mLoadingAlertDialog.dismiss();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                removeLoadingView();
//                mLoadingAlertDialog.dismiss();
//            }
//
//            @Override
//            public void onNext(DynamicBean dynamicBean) {
//                if("1".equals(dynamicBean.getState())){
//                    begin_time = end_time;
//                    mAdapter.addFirstData(dynamicBean.getDtList());
//                }else {
//                    showTest(dynamicBean.getMsg());
//                }
//            }
//        }));
//    }


    private void initLinearLayout(RecyclerView gridView, DynamicBean.Dynamic pics) {
        if(pics.getPics() == null || pics.getPics().isEmpty()){
            gridView.setVisibility(View.GONE);
            return;
        }
        gridView.setVisibility(View.VISIBLE);
        MyGridAdapter adapter = new MyGridAdapter();
        adapter.setData(pics);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL);
        gridView.setLayoutManager(manager);
        gridView.setAdapter(adapter);
    }

    /**
     * 保存快照
     * @param dynamic
     */
    private void saveSnapshot(DynamicBean.Dynamic dynamic) {
        Intent intent = new Intent(getActivity(),SnapshotActivity.class);
        intent.putExtra(SnapshotActivity.DYNAMIC_CONTENT,dynamic);
        intent.putExtra(SnapshotActivity.RIGHT_STATE,1);
        startActivity(intent);
    }

    /**
     * 删除动态
     * @param dynamic
     */
    private void deleteDynamic(final DynamicBean.Dynamic dynamic) {

        mLoadingAlertDialog.show();
        ((BaseActivityTwoV)getActivity()).addSubscription(wrapObserverWithHttp(WorkService.getWorkService().delPageDt((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),dynamic.getId())).subscribe(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                removeLoadingView();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onNext(BaseBean baseBean) {
                if("1".equals(baseBean.state)){
                    showTest(baseBean.msg);
                    mAdapter.removeDate(dynamic);
                }else {
                    showTest(baseBean.msg);
                }


            }
        }));
    }

    /**
     * 关注动态
     * @param dynamic 动态信息
     * @param status 1 关注 0取消关注
     */
    private void attention(DynamicBean.Dynamic dynamic, final int status) {
        mLoadingAlertDialog.show();
        ((BaseActivityTwoV)getActivity()).addSubscription(wrapObserverWithHttp(WorkService.getWorkService().fans((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),dynamic.getLoginid()+"",status)).subscribe(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                removeLoadingView();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onNext(BaseBean baseBean) {
                if("1".equals(baseBean.state)){
                    if(status == 1){
                        showTest("您已成功关注");
                        EventBus.getDefault().post(EventBusContanct.getInstance(EventBusContanct.TABMYFRAGMENT_REFRSH_FAN_JIA1));
                    }else if(status == 0){
                        showTest("您已取消关注");
                        EventBus.getDefault().post(EventBusContanct.getInstance(EventBusContanct.TABMYFRAGMENT_REFRSH_FAN_JIAN1));
                    }
                    loadData(currentPage);
                }
            }
        }));
    }

    @Override
    public void onResume() {
        super.onResume();
        Bimp.bmp.clear();
    }


    /**
     * 点赞动态
     * @param o 动态信息
     * @param type 1 确认点赞 0 取消点赞
     */
    private void praise(DynamicBean.Dynamic o, final int type) {
        mLoadingAlertDialog.show();
        ((BaseActivityTwoV) getActivity()).addSubscription(wrapObserverWithHttp(WorkService.getWorkService().praise((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),o.getId(),type,o.getLoginid())).subscribe(new Subscriber<ExceInfo>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                removeLoadingView();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onNext(ExceInfo exceInfo) {
                MyLogger.kLog().e(exceInfo.toString());
                if(exceInfo.getState() == 1){
                    if(type == 1){
                        showTest("您已成功点赞！");
                    }else{
                        showTest("您已取消点赞！");
                    }
                }else{
                    showTest(exceInfo.getMsg());
                }
            }
        }));
    }

    /**
     * 分页加载动态列表
     * @param currentPage 页码
     */
    private void loadData(final int currentPage) {
        mLoadingAlertDialog.show();
        if(type == 0) {                                 //首页
            ((BaseActivityTwoV) getActivity()).addSubscription(wrapObserverWithHttp(WorkService.getWorkService().listDtAll((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""), currentPage)).subscribe(new Subscriber<DynamicBean>() {
                @Override
                public void onCompleted() {
                    removeLoadingView();
                    pcFlyt.setLoadMoreEnable(true);
                    pcFlyt.loadMoreComplete(true);
                    pcFlyt.refreshComplete();
                    mLoadingAlertDialog.dismiss();
                }

                @Override
                public void onError(Throwable e) {
                    removeLoadingView();
                    mLoadingAlertDialog.dismiss();
                }

                @Override
                public void onNext(DynamicBean o) {
                    if ("1".equals(o.getState())) {
                        if(currentPage == 1){
                            mAdapter.replaceAllDate(o.getDtList());
                        }else {
                            mAdapter.addDate(o.getDtList());
                        }
                    }
                }
            }));
        }else if(type == 1 || type == 2){                        //加载某人的动态，包括自己
            ((BaseActivityTwoV)getActivity()).addSubscription(wrapObserverWithHttp(WorkService.getWorkService().listDtAll((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""), currentPage,userId)).subscribe(new Subscriber<DynamicBean>() {
                @Override
                public void onCompleted() {
                    removeLoadingView();
                    pcFlyt.setLoadMoreEnable(true);
                    pcFlyt.loadMoreComplete(true);
                    pcFlyt.refreshComplete();
                    mLoadingAlertDialog.dismiss();
                }

                @Override
                public void onError(Throwable e) {
                    removeLoadingView();
                    mLoadingAlertDialog.dismiss();
                }

                @Override
                public void onNext(DynamicBean dynamicBean) {
                    if ("1".equals(dynamicBean.getState())) {
                        if(currentPage == 1){
                            mAdapter.replaceAllDate(dynamicBean.getDtList());
                        }else {
                            mAdapter.addDate(dynamicBean.getDtList());
                        }
                    }
                }
            }));

        }
    }



    private class MyGridAdapter extends RecyclerView.Adapter {

        private DynamicBean.Dynamic dynamic;

        public void setData(DynamicBean.Dynamic dynamic){
            this.dynamic = dynamic;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder2 holder2 = new ViewHolder2(LayoutInflater.from(getActivity()).inflate(R.layout.item_img_content2,parent,false));
            return holder2;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
            ViewHolder2 holder = (ViewHolder2) h;
            ImageLoader.getInstance().displayImage(dynamic.getPics().get(position).getPage_picpath(),holder.img_photo);
            if(position == 3 && dynamic.getPics().size() > 4){
                holder.tv_seeAll.setVisibility(View.VISIBLE);
                holder.tv_seeAll.setText("共("+ dynamic.getPics().size() +")图\n查看更多");
                holder.iv_playbg.setVisibility(View.VISIBLE);
            }else{
                holder.tv_seeAll.setVisibility(View.GONE);
                holder.iv_playbg.setVisibility(View.GONE);
            }
            holder.tv_seeAll.setOnClickListener(new View.OnClickListener() {   //图片上的查看更多  动态详情
                @Override
                public void onClick(View v) {
                    gotoDetail(dynamic,1);
                }
            });
            holder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bimp.bmp.clear();
                    for(DynamicBean.Pic pic : dynamic.getPics()){
                        ImageItem item = new ImageItem();
                        item.imageLoadPath = pic.getPage_picpath();
                        Bimp.bmp.add(item);
                    }
                    Intent intent = new Intent(getActivity(), PhotoDetailActivity.class);
                    intent.putExtra(PhotoDetailActivity.POSTION,position);
                    startActivity(intent);
                }
            });
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            if(dynamic.getPics().size() < 4){
                return dynamic.getPics().size();
            }
            return 4;
        }
    }

    public static class ViewHolder2 extends RecyclerView.ViewHolder{
        public ImageView img_photo;
        public TextView tv_seeAll;
        public ImageView iv_playbg;

        public ViewHolder2(View itemView) {
            super(itemView);
            img_photo = itemView.findViewById(R.id.img_photo);
            tv_seeAll = itemView.findViewById(R.id.tv_seeAll);
            iv_playbg = itemView.findViewById(R.id.iv_playbg);
        }

        public void setOnItemClickListener(final AdapterView.OnItemClickListener onItemClickListener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(null,itemView,getAdapterPosition(),0);
                }
            });
        }
    }
    @OnClick(R.id.civ_posted)
    public void onClick(View view){ //发布动态
        Intent intent = new Intent(getActivity(),DynamicPostedActivity.class);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventEMMessage(String message) {
        MyLogger.kLog().e(message);
        if(DynamicPostedActivity.POSTED_STATE.equals(message)) {
            dynamicBeanList.clear();
            currentPage = 1;
            loadData(currentPage);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventEMMessage(DynamicMsg message) {
        if(message.type.equals("1")){  //点赞
            for(DynamicBean.Dynamic bean : dynamicBeanList){
                if(bean.getId() == message.pageId){
                    int count = bean.getPraise_count();
                    bean.setPraise_count(++count);
                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }else if(message.type.equals("3")){  //评论
            for(DynamicBean.Dynamic bean : dynamicBeanList){
                if(bean.getId() == message.pageId){
                    int count = bean.getReply_count();
                    bean.setReply_count(++count);
                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }else if(message.type.equals("2")){   //取消点赞
            for(DynamicBean.Dynamic bean : dynamicBeanList){
                if(bean.getId() == message.pageId){
                    int count = bean.getPraise_count();
                    bean.setPraise_count(--count);
                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }else if (message.type.equals("6")){ //关注不处理

        }
        EventBus.getDefault().post(EventBusContanct.getInstance(EventBusContanct.TABMY_STATE));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventEMMessage(EventBusContanct message) {
        if (message.getFlag() == EventBusContanct.REFRESH_DYNAMIC){
            long beginTime = (long) SPUtils.get(SPUtils.KEY_DYNAMIC_DATA,0l);
            long time = System.currentTimeMillis() - beginTime;
            if(time > 60l * 60l * 1000l){
                loadData(currentPage = 1);
                SPUtils.put(SPUtils.KEY_DYNAMIC_DATA,System.currentTimeMillis());
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    public String saveFile(ResponseBody body) {
        byte[] bufd = null;
        try {
            bufd = body.bytes();
            return ImageUtil.saveImageToGallery(BitmapFactory.decodeByteArray(bufd,0,bufd.length));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public void gotoDetail(final DynamicBean.Dynamic bean,final int type){
        mLoadingAlertDialog.show();
        ((BaseActivityTwoV)getActivity()).addSubscription(wrapObserverWithHttp(WorkService.getWorkService().isDelPageDt((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),bean.getId())).subscribe(new Subscriber<DynamicDel>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                removeLoadingView();
                removeErrorView();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onNext(DynamicDel dynamicDel) {
                if ("2".equals(dynamicDel.state) || "0".equals(dynamicDel.state)){
                    showTest("动态已删除！");
                    if("2".equals(dynamicDel.state)){
                        mAdapter.removeDate(bean);
                    }
                }else if("3".equals(dynamicDel.state)){
                    Intent intent = new Intent(getActivity(),DynamicDatailActivity.class);
                    intent.putExtra(DynamicDatailActivity.DYNAMIC_ID,bean.getId());
                    intent.putExtra(DynamicDatailActivity.DISCUSS_TYPE,type);
                    getActivity().startActivity(intent);
                }
            }
        }));
    }
}
