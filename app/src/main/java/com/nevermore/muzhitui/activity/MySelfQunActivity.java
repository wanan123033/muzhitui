package com.nevermore.muzhitui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
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
import com.nevermore.muzhitui.module.bean.QunChangeBean;
import com.nevermore.muzhitui.module.bean.QunMeBean;
import com.nevermore.muzhitui.module.bean.QunWantBean;
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
 * Created by Administrator on 2017/9/27.
 */

public class MySelfQunActivity extends BaseActivityTwoV{
    @BindView(R.id.rv_my_release)
    RecyclerView rv_my_release;
    @BindView(R.id.pcFlyt)
    PtrClassicFrameLayout mPcFlyt;
    @BindView(R.id.tv_exchange)
    TextView tv_exchange;
    @BindView(R.id.tv_have_qun)
    TextView tv_have_qun;
    @BindView(R.id.tv_demand_qun)
    TextView tv_demand_qun;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title2)
    TextView tv_title;


    public static final String TYPE = "type";

    private CommonAdapter mAdapter;
    private int type;  //0群互换  1我有群   2我要群

    private LoadingAlertDialog mLoadingAlertDialog;
    private RecyclerAdapterWithHF recyclerAdapterWithHF;
    private List quns = new ArrayList();
    private int mCurrenPager = 1;
    private List<TextView> titles = new ArrayList<>();

    @Override
    public void init() {
        hideActionBar();
        setMyTitle("我的发布");
        showBack();
        titles.add(tv_exchange);
        titles.add(tv_have_qun);
        titles.add(tv_demand_qun);
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        type = getIntent().getIntExtra(TYPE,0);
        setTitle(titles.get(type));
        mAdapter = new CommonAdapter<Object>(this,R.layout.item_my_release,quns) {
            @Override
            public void convert(ViewHolder holder, final Object o) {
                if(o instanceof QunWantBean.QunWant){
                    QunWantBean.QunWant want = (QunWantBean.QunWant) o;
                    holder.setText(R.id.tv_name,want.wx_qun_name);
                    holder.getView(R.id.tv_user).setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_user,want.user_name);
                    holder.setText(R.id.tv_date,want.update_time);
                    ImageLoader.getInstance().displayImage(want.headimg, (ImageView) holder.getView(R.id.iv_topic));
                }else if(o instanceof QunMeBean.Qun){
                    QunMeBean.Qun qun = (QunMeBean.Qun) o;
                    holder.setText(R.id.tv_user,qun.wx_city);
                    holder.getView(R.id.tv_user).setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_name,qun.wx_qun_name);
                    holder.setText(R.id.tv_date,"人数："+qun.wx_qun_num);
                    ImageLoader.getInstance().displayImage(qun.headimg, (ImageView) holder.getView(R.id.iv_topic));
                }else if(o instanceof QunChangeBean.QunChange){
                    QunChangeBean.QunChange change = (QunChangeBean.QunChange) o;
                    holder.setText(R.id.tv_name,"微信号："+change.wx_no);
                    holder.getView(R.id.tv_user).setVisibility(View.INVISIBLE);
                    holder.setText(R.id.tv_date,"群名字："+change.wx_qun_name);
                    ImageLoader.getInstance().displayImage(change.pic1, (ImageView) holder.getView(R.id.iv_topic));
                }
                holder.setOnClickListener(R.id.tv_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteQun(o);
                    }
                });

                holder.setOnClickListener(R.id.tv_xiugai, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateQun(o);
                    }
                });
            }
        };
        recyclerAdapterWithHF = new RecyclerAdapterWithHF(mAdapter);

        recyclerAdapterWithHF.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {
                if(type == 0){
                    QunChangeBean.QunChange change = (QunChangeBean.QunChange) quns.get(position);
                    QunExchangeDetailDialog dialog = new QunExchangeDetailDialog(MySelfQunActivity.this,change)
                            ;
                    dialog.show();
                }else if(type == 1){
                    QunMeBean.Qun qun = (QunMeBean.Qun) quns.get(position);
                    Intent intent = new Intent(getApplicationContext(),QunDetailActivity.class);
                    intent.putExtra(QunDetailActivity.TYPE,0);
                    intent.putExtra(QunDetailActivity.QUN_ID,qun.id);
                    startActivity(intent);
                }else if(type == 2){
                    QunWantBean.QunWant want = (QunWantBean.QunWant) quns.get(position);
                    Intent intent = new Intent(getApplicationContext(),QunDetailActivity.class);
                    intent.putExtra(QunDetailActivity.TYPE,1);
                    intent.putExtra(QunDetailActivity.QUN_ID,want.id);
                    startActivity(intent);
                }
            }
        });
        rv_my_release.setAdapter(recyclerAdapterWithHF);
        loadData(1);

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
                quns.clear();
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
    }

    private void updateQun(Object o) {
        if(o instanceof QunWantBean.QunWant){
            Intent intent = new Intent(this,QunHavePostedActivity.class);
            intent.putExtra(QunHavePostedActivity.TYPE,1);
            intent.putExtra(QunHavePostedActivity.DATA,(QunWantBean.QunWant)o);
            startActivity(intent);
        }else if(o instanceof QunMeBean.Qun){
            Intent intent = new Intent(this,QunHavePostedActivity.class);
            intent.putExtra(QunHavePostedActivity.TYPE,0);
            intent.putExtra(QunHavePostedActivity.DATA,(QunMeBean.Qun)o);
            startActivity(intent);
        }else if(o instanceof QunChangeBean.QunChange){
            Intent intent = new Intent(this,QunExchangePostedActivity.class);
            intent.putExtra(QunExchangePostedActivity.DATA,(QunChangeBean.QunChange)o);
            startActivity(intent);
        }
    }

    private void deleteQun(Object o) {
        mLoadingAlertDialog.show();
        if(o instanceof QunWantBean.QunWant){
            addSubscription(wrapObserverWithHttp(WorkService.getWorkService().deleteQunWant((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),((QunWantBean.QunWant)o).id)).subscribe(new Subscriber<BaseBean>() {
                @Override
                public void onCompleted() {
                    removeLoadingView();
                    mLoadingAlertDialog.dismiss();
                }

                @Override
                public void onError(Throwable e) {
                    removeErrorView();
                    removeLoadingView();
                    mLoadingAlertDialog.dismiss();
                }

                @Override
                public void onNext(BaseBean baseBean) {
                    if("1".equals(baseBean.state)){
                        showTest("删除成功");
                        loadData(mCurrenPager);
                    }else{
                        showTest(baseBean.msg);
                    }
                }
            }));
        }else if(o instanceof QunMeBean.Qun){
            addSubscription(wrapObserverWithHttp(WorkService.getWorkService().deleteQunMe((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),((QunMeBean.Qun)o).id)).subscribe(new Subscriber<BaseBean>() {
                @Override
                public void onCompleted() {
                    removeLoadingView();
                    mLoadingAlertDialog.dismiss();
                }

                @Override
                public void onError(Throwable e) {
                    removeErrorView();
                    removeLoadingView();
                    mLoadingAlertDialog.dismiss();
                }

                @Override
                public void onNext(BaseBean baseBean) {
                    if("1".equals(baseBean.state)){
                        showTest("删除成功");
                        loadData(mCurrenPager);
                    }else{
                        showTest(baseBean.msg);
                    }
                }
            }));
        }else if(o instanceof QunChangeBean.QunChange){
            addSubscription(wrapObserverWithHttp(WorkService.getWorkService().deleteQunChange((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),((QunChangeBean.QunChange)o).id)).subscribe(new Subscriber<BaseBean>() {
                @Override
                public void onCompleted() {
                    removeLoadingView();
                    mLoadingAlertDialog.dismiss();
                }

                @Override
                public void onError(Throwable e) {
                    removeErrorView();
                    removeLoadingView();
                    mLoadingAlertDialog.dismiss();
                }

                @Override
                public void onNext(BaseBean baseBean) {
                    if("1".equals(baseBean.state)){
                        showTest("删除成功");
                        loadData(mCurrenPager);
                    }else{
                        showTest(baseBean.msg);
                    }
                }
            }));
        }

    }

    private void loadData(int pageCurrent) {
        mLoadingAlertDialog.show();
        if(type == 0){
            addSubscription(wrapObserverWithHttp(WorkService.getWorkService().listQunChangeMyself((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),pageCurrent)).subscribe(new Subscriber<QunChangeBean>() {
                @Override
                public void onCompleted() {
                    removeLoadingView();
                    mPcFlyt.setLoadMoreEnable(true);
                    mPcFlyt.loadMoreComplete(true);
                    mPcFlyt.refreshComplete();
                    mLoadingAlertDialog.dismiss();
                }

                @Override
                public void onError(Throwable e) {
                    removeErrorView();
                    removeLoadingView();
                    mLoadingAlertDialog.dismiss();
                }

                @Override
                public void onNext(QunChangeBean qunChangeBean) {
                    if("1".equals(qunChangeBean.state)){
                        if(qunChangeBean.pageCurrent == 1){
                            mAdapter.replaceAllDate(qunChangeBean.qunList);
                        }else {
                            mAdapter.addDate(qunChangeBean.qunList);
                        }
                    }else{
                        showTest(qunChangeBean.msg);
                    }

                }
            }));
        }else if (type == 1){
            addSubscription(wrapObserverWithHttp(WorkService.getWorkService().listQunMeMyself((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),pageCurrent)).subscribe(new Subscriber<QunMeBean>() {
                @Override
                public void onCompleted() {
                    removeLoadingView();
                    mPcFlyt.setLoadMoreEnable(true);
                    mPcFlyt.loadMoreComplete(true);
                    mPcFlyt.refreshComplete();
                    mLoadingAlertDialog.dismiss();
                }

                @Override
                public void onError(Throwable e) {
                    removeErrorView();
                    removeLoadingView();
                    mLoadingAlertDialog.dismiss();
                }

                @Override
                public void onNext(QunMeBean qunMeBean) {
                    if("1".equals(qunMeBean.state)){
                        if(qunMeBean.pageCurrent == 1){
                            mAdapter.replaceAllDate(qunMeBean.qunList);
                        }else {
                            mAdapter.addDate(qunMeBean.qunList);
                        }
                    }else{
                        showTest(qunMeBean.msg);
                    }
                }
            }));
        }else if(type == 2){
            addSubscription(wrapObserverWithHttp(WorkService.getWorkService().listQunWantMyself((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),pageCurrent)).subscribe(new Subscriber<QunWantBean>() {
                @Override
                public void onCompleted() {
                    removeLoadingView();
                    mPcFlyt.setLoadMoreEnable(true);
                    mPcFlyt.loadMoreComplete(true);
                    mPcFlyt.refreshComplete();
                    mLoadingAlertDialog.dismiss();
                }

                @Override
                public void onError(Throwable e) {
                    removeErrorView();
                    removeLoadingView();
                    mLoadingAlertDialog.dismiss();
                }

                @Override
                public void onNext(QunWantBean qunWantBean) {
                    if("1".equals(qunWantBean.state)){
                        if(qunWantBean.pageCurrent == 1){
                            mAdapter.replaceAllDate(qunWantBean.qunList);
                        }else {
                            mAdapter.addDate(qunWantBean.qunList);
                        }
                    }else{
                        showTest(qunWantBean.msg);
                    }
                }
            }));
        }
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_myself_qun;
    }

    @OnClick({R.id.tv_exchange,R.id.tv_have_qun,R.id.tv_demand_qun})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_exchange:
                type = 0;
                setTitle((TextView) view);
                mCurrenPager = 1;
                loadData(mCurrenPager);
                break;
            case R.id.tv_have_qun:
                type = 1;
                setTitle((TextView) view);
                mCurrenPager = 1;
                loadData(mCurrenPager);
                break;
            case R.id.tv_demand_qun:
                type = 2;
                setTitle((TextView) view);
                mCurrenPager = 1;
                loadData(mCurrenPager);
                break;
        }

    }
    public void setTitle(TextView itemView){
        for(TextView view : titles){
            if (itemView == view){
                view.setBackgroundColor(getResources().getColor(R.color.red));
                view.setTextColor(Color.WHITE);
            }else{
                view.setBackgroundColor(getResources().getColor(R.color.transparent));
                view.setTextColor(Color.BLACK);
            }
        }
    }

    @Override
    public void setMyTitle(String title) {
        tv_title.setText(title);
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
