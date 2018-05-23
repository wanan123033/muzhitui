package com.nevermore.muzhitui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicDefaultHeader;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.BaseBean;
import com.nevermore.muzhitui.module.bean.FanBean;
import com.nevermore.muzhitui.module.bean.GFBean;
import com.nevermore.muzhitui.module.bean.WatchBean;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.MyLogger;
import base.SPUtils;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/9/13.
 * 关注或粉丝列表
 */

public class GFActivity extends BaseActivityTwoV{

    @BindView(R.id.rv_gf)
    RecyclerView rv_gf;
    @BindView(R.id.pcFlyt)
    PtrClassicFrameLayout mPcFlyt;
    @BindView(R.id.tv_g)
    TextView tv_g;
    @BindView(R.id.tv_f)
    TextView tv_f;
    @BindView(R.id.iv_back)
    ImageView iv_back;

    public static final String MENU_STATE = "menu_state";
    public static final String USER_ID = "uesrId";

    private List<Object> gfs = new ArrayList<>();
    private CommonAdapter mAdapter;
    private RecyclerAdapterWithHF recyclerAdapterWithHF;

    private int menuStatus;   //0 关注列表  1 粉丝列表
    private String userId;
    private int mCurrenPager = 1;
    private List<TextView> titles = new ArrayList<>();

    private LoadingAlertDialog mLoadingAlertDialog;

    @Override
    public void init() {
        hideActionBar();
        showBack();
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        titles.clear();
        titles.add(tv_g);
        titles.add(tv_f);
        menuStatus = getIntent().getIntExtra(MENU_STATE,0);
        userId = getIntent().getStringExtra(USER_ID);
        mAdapter = new CommonAdapter<Object>(this,R.layout.item_gf,gfs) {
            @Override
            public void convert(ViewHolder holder, final Object gfBean) {

                if(gfBean instanceof WatchBean.Watch){  //关注
                    WatchBean.Watch watch = (WatchBean.Watch) gfBean;
                    ((TextView)holder.getView(R.id.tv_name)).setText(watch.user_name);
                    holder.getView(R.id.iv_gf_icon).setVisibility(View.GONE);
                    ImageLoader.getInstance().displayImage(watch.headimg, (ImageView) holder.getView(R.id.civ_topic));
                    if(watch.count != 0){
                        holder.getView(R.id.seal_num_message).setVisibility(View.VISIBLE);
                        holder.setText(R.id.seal_num_message,watch.count+"");
                    }else {
                        holder.getView(R.id.seal_num_message).setVisibility(View.GONE);
                    }
                    MyLogger.kLog().e(watch.user_name+"----"+watch.count);
                    if(!((String)SPUtils.get(SPUtils.GET_LOGIN_ID,"")).equals(userId)){
                        holder.getView(R.id.seal_num_message).setVisibility(View.GONE);
                    }else if(watch.count != 0){
                        holder.getView(R.id.seal_num_message).setVisibility(View.VISIBLE);
                    }
                }else if(gfBean instanceof FanBean.Fan){ //粉丝
                    FanBean.Fan fan = (FanBean.Fan) gfBean;
                    ((TextView)holder.getView(R.id.tv_name)).setText(fan.user_name);
                    holder.getView(R.id.iv_gf_icon).setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(fan.headimg, (ImageView) holder.getView(R.id.civ_topic));
                    holder.getView(R.id.seal_num_message).setVisibility(View.GONE);

                    if(fan.type == 1){
                        holder.getView(R.id.iv_gf_icon).setBackgroundResource(R.mipmap.attention_eash_secleted);
                    }else {
                        holder.getView(R.id.iv_gf_icon).setBackgroundResource(R.mipmap.attention_eash_nomal);
                    }
                    if(!((String)SPUtils.get(SPUtils.GET_LOGIN_ID,"")).equals(userId)){
                        holder.getView(R.id.iv_gf_icon).setVisibility(View.GONE);
                        holder.getView(R.id.seal_num_message).setVisibility(View.GONE);
                    }else {
                        holder.getView(R.id.iv_gf_icon).setVisibility(View.VISIBLE);
                    }
                }
                holder.setOnClickListener(R.id.iv_gf_icon, new View.OnClickListener() {    //关注或取消关注
                    @Override
                    public void onClick(View v) {
                        attention(gfBean);
                    }
                });
            }
        };
        recyclerAdapterWithHF = new RecyclerAdapterWithHF(mAdapter);
        //设置下拉刷新
        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(this);
        mPcFlyt.addPtrUIHandler(header);
        mPcFlyt.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                gfs.clear();
                mCurrenPager=1;
                loadData(mCurrenPager);
            }
        });
        mPcFlyt.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                loadData( ++mCurrenPager);
            }
        });
        rv_gf.setAdapter(recyclerAdapterWithHF);
        loadData(1);
        if(menuStatus == 0){
            setTitle(tv_g);
        }else if(menuStatus == 1){
            setTitle(tv_f);
        }
        recyclerAdapterWithHF.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {
                final GFBean gfBean = (GFBean) gfs.get(position);
                int loginId = 0;
                if(gfBean instanceof FanBean.Fan){
                    loginId = ((FanBean.Fan)gfBean).loginid;
                }else if(gfBean instanceof WatchBean.Watch){
                    loginId = ((WatchBean.Watch)gfBean).loginid;
                    if (((WatchBean.Watch)gfBean).count != 0 && ((String)SPUtils.get(SPUtils.GET_LOGIN_ID,"")).equals(userId)) {
                        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().clearCount(userId, loginId + "")).subscribe(new Subscriber<BaseBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                MyLogger.kLog().e(e);
                            }

                            @Override
                            public void onNext(BaseBean baseBean) {
                                if ("1".equals(baseBean.state)) {
                                    MyLogger.kLog().e("清除成功");
                                    ((WatchBean.Watch)gfBean).count = 0;
                                    recyclerAdapterWithHF.notifyDataSetChangedHF();
                                } else {
                                    MyLogger.kLog().e(baseBean.msg);
                                }
                            }
                        }));
                    }
                }
                Intent intent = new Intent(getApplicationContext(),DynamicPersonActivity.class);
                intent.putExtra(DynamicPersonActivity.USERID,loginId+"");
                startActivity(intent);
            }
        });
    }

    /**
     * 关注或取消关注
     * @param gfBean
     */
    private void attention(final Object gfBean) {
        int loginId = 0;
        int type = 0;
        if(gfBean instanceof FanBean.Fan){
            loginId =((FanBean.Fan)gfBean).loginid;
            type = ((FanBean.Fan)gfBean).type == 0 ? 1 : 0;
            mLoadingAlertDialog.show();
            addSubscription(wrapObserverWithHttp(WorkService.getWorkService().fans((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),String.valueOf(loginId),type)).subscribe(new Subscriber<BaseBean>() {
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
                public void onNext(BaseBean baseBean) {
                    if("1".equals(baseBean.state)){
                        if(((FanBean.Fan)gfBean).type == 0){
                            showTest("您已成功互粉");
                            ((FanBean.Fan)gfBean).type = 1;
                        }else {
                            showTest("您已取消互粉");
                            ((FanBean.Fan)gfBean).type = 0;
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }));
        }
    }

    private void loadData(final int pageCurrent) {
        if(menuStatus == 0){
            addSubscription(wrapObserverWithHttp(WorkService.getWorkService().listWatchs((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""), userId, pageCurrent)).subscribe(new Subscriber<WatchBean>() {
                @Override
                public void onCompleted() {
                    mPcFlyt.setLoadMoreEnable(true);
                    mPcFlyt.loadMoreComplete(true);
                    mPcFlyt.refreshComplete();
                }

                @Override
                public void onError(Throwable e) {
                    MyLogger.kLog().e(e);
                }

                @Override
                public void onNext(WatchBean watchBean) {
                    if ("1".equals(watchBean.state)) {
                        if (pageCurrent == 1) {
                            mAdapter.replaceAllDate(watchBean.watchList);
                        }else {
                            mAdapter.addDate(watchBean.watchList);
                        }
                    } else {
                        showTest(watchBean.msg);
                    }
                }
            }));
        }else if(menuStatus == 1){
            addSubscription(wrapObserverWithHttp(WorkService.getWorkService().listFans((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),userId,pageCurrent)).subscribe(new Subscriber<FanBean>() {
                @Override
                public void onCompleted() {
                    mPcFlyt.setLoadMoreEnable(true);
                    mPcFlyt.loadMoreComplete(true);
                    mPcFlyt.refreshComplete();
                }

                @Override
                public void onError(Throwable e) {
                    MyLogger.kLog().e(e);
                }

                @Override
                public void onNext(FanBean o) {
                    MyLogger.kLog().e(o.fansList.size());
                    if ("1".equals(o.state)){
                        if(pageCurrent == 1){
                            mAdapter.replaceAllDate(o.fansList);
                        }else {
                            mAdapter.addDate(o.fansList);
                        }
                    }else {
                        showTest(o.msg);
                    }
                }
            }));
        }

    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_gf;
    }

    @OnClick({R.id.tv_g,R.id.tv_f})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_g:
                menuStatus = 0;
                setTitle(view);
                break;
            case R.id.tv_f:
                menuStatus = 1;
                setTitle(view);
                break;
        }
        loadData(1);
    }
    public void setTitle(View item){
        for(TextView view : titles){
            if (item == view){
                view.setBackgroundColor(Color.WHITE);
                view.setTextColor(Color.BLACK);
            }else{
                view.setBackgroundColor(getResources().getColor(R.color.transparent));
                view.setTextColor(Color.WHITE);
            }
        }
    }
    @Override
    public void showBack() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                finish();
                onBackPressed();
            }
        });
    }
}
