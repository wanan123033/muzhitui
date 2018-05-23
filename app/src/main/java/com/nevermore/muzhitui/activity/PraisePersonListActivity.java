package com.nevermore.muzhitui.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chanven.lib.cptr.PtrClassicDefaultHeader;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.FanBean;
import com.nevermore.muzhitui.module.bean.GFBean;
import com.nevermore.muzhitui.module.bean.PraiseBean;
import com.nevermore.muzhitui.module.bean.WatchBean;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import rx.Subscriber;



/**
 * Created by Administrator on 2017/10/12.
 */

public class PraisePersonListActivity extends BaseActivityTwoV{
    public static final String DYNAMIC_ID = "dynamic_id";

    @BindView(R.id.rv_gf)
    RecyclerView rv_gf;
    @BindView(R.id.pcFlyt)
    PtrClassicFrameLayout mPcFlyt;
    @BindView(R.id.rel_tool)
    RelativeLayout rel_tool;

    public CommonAdapter mAdapter;
    private int dynamic_id;
    private LoadingAlertDialog mLoadingAlertDialog;
    private List<PraiseBean.Praise> praises = new ArrayList<>();
    private int mCurrenPager = 1;

    @Override
    public void init() {
        showBack();
        setMyTitle("点赞列表");
        rel_tool.setVisibility(View.GONE);
        dynamic_id = getIntent().getIntExtra(DYNAMIC_ID,0);
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        mAdapter = new CommonAdapter<PraiseBean.Praise>(this,R.layout.item_praise_list,praises) {
            @Override
            public void convert(ViewHolder holder, PraiseBean.Praise o) {
                ImageLoader.getInstance().displayImage(o.headimg, (ImageView) holder.getView(R.id.civ_topic));
                holder.setText(R.id.tv_name,o.user_name);
            }

        };
        RecyclerAdapterWithHF recyclerAdapterWithHF = new RecyclerAdapterWithHF(mAdapter);
        recyclerAdapterWithHF.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {
                Intent intent = new Intent(getApplicationContext(),DynamicPersonActivity.class);
                intent.putExtra(DynamicPersonActivity.USERID,praises.get(position).loginid+"");
                startActivity(intent);
            }
        });
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
                praises.clear();
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
    }

    private void loadData(int pageCurrent) {
        mLoadingAlertDialog.show();
        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().listPraise((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),dynamic_id,pageCurrent)).subscribe(new Subscriber<PraiseBean>() {
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
            public void onNext(PraiseBean praiseBean) {
                if("1".equals(praiseBean.state)){
                    mAdapter.addDate(praiseBean.praiseList);
                }else {
                    showTest(praiseBean.msg);
                }
            }
        }));
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_gf;
    }
}
