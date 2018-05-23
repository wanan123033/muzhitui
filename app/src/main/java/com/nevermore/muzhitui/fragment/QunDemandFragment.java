package com.nevermore.muzhitui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
import com.nevermore.muzhitui.activity.QunDetailActivity;
import com.nevermore.muzhitui.activity.QunHavePostedActivity;
import com.nevermore.muzhitui.module.bean.QunMeBean;
import com.nevermore.muzhitui.module.bean.QunWantBean;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
import base.recycler.recyclerview.OnItemClickListener;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/9/14.
 * 我要群
 */

public class QunDemandFragment extends base.BaseFragment {

    @BindView(R.id.rv_demand_qun)
    RecyclerView rv_demand_qun;
    @BindView(R.id.pcFlyt)
    PtrClassicFrameLayout pcFlyt;
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.iv_shang)
    ImageView iv_shang;
    @BindView(R.id.iv_xia)
    ImageView iv_xia;

    private CommonAdapter mAdapter;
    private String qun_name;
    private Integer offer_sort;

    private LoadingAlertDialog mLoadingAlertDialog;

    private RecyclerAdapterWithHF recyclerAdapterWithHF;
    private List<QunWantBean.QunWant> wants = new ArrayList<>();
    private int mCurrenPager = 1;

    @Override
    public int createSuccessView() {
        return R.layout.fragment_qun_demand;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoadingAlertDialog = new LoadingAlertDialog(getActivity());
        mAdapter = new CommonAdapter<QunWantBean.QunWant>(getActivity(),R.layout.item_qun_have,wants) {
            @Override
            public void convert(ViewHolder holder, QunWantBean.QunWant o) {
                holder.setText(R.id.tv_name,o.wx_qun_name);
                holder.setText(R.id.tv_weizhi,"需求者："+o.user_name);
                holder.setText(R.id.tv_renshu,"发布时间："+o.update_time);
                holder.setText(R.id.tv_baojia,"悬赏：￥"+o.offer);
                holder.setText(R.id.tv_readNum,o.read_num+"人阅读");
                ImageLoader.getInstance().displayImage(o.headimg, (ImageView) holder.getView(R.id.iv_topic));
            }
        };
        recyclerAdapterWithHF = new RecyclerAdapterWithHF(mAdapter);
        recyclerAdapterWithHF.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {
                Intent intent = new Intent(getActivity(), QunDetailActivity.class);
                QunWantBean.QunWant qun = wants.get(position);
                intent.putExtra(QunDetailActivity.QUN_ID,qun.id);
                intent.putExtra(QunDetailActivity.TYPE,1);
                startActivity(intent);
            }
        });
        rv_demand_qun.setAdapter(recyclerAdapterWithHF);
        offer_sort = null;
        loadData(1);
        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(getActivity());
        pcFlyt.addPtrUIHandler(header);
        pcFlyt.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                wants.clear();
                mCurrenPager=1;
                loadData(mCurrenPager);
            }
        });
        pcFlyt.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                loadData(++mCurrenPager);
            }
        });

        et_content.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE || (event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER) ){
                    loadData(1);
                    ((BaseActivityTwoV)getActivity()).hideSoftInput();
                    return true;
                }
                return false;
            }
        });
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                qun_name = et_content.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.civ_posted_qun,R.id.iv_search,R.id.ll_offert})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.civ_posted_qun:
                Intent intent = new Intent(getActivity(),QunHavePostedActivity.class);
                intent.putExtra(QunHavePostedActivity.TYPE,1);
                startActivity(intent);
                break;
            case R.id.iv_search:
                String content = et_content.getText().toString().trim();
                if(content.isEmpty()){
                    qun_name = null;
                    showTest("请输入搜索内容");
                    break;
                }
                qun_name = content;
                mAdapter.removeAllDate();
                loadData(1);
                break;
            case R.id.ll_offert:
                if(offer_sort == null){
                    offer_sort = 1;
                    iv_shang.setImageResource(R.mipmap.shang_icon_per);
                    iv_xia.setImageResource(R.mipmap.iv_xia_seled);
                }else if (offer_sort == 1){
                    offer_sort = 0;
                    iv_shang.setImageResource(R.mipmap.iv_shang_seled);
                    iv_xia.setImageResource(R.mipmap.xia_icon_per);
                }else {
                    offer_sort = 1;
                    iv_shang.setImageResource(R.mipmap.shang_icon_per);
                    iv_xia.setImageResource(R.mipmap.iv_xia_seled);
                }
                loadData(1);
                break;
        }
    }

    private void loadData(final int pageCurrent) {
        mLoadingAlertDialog.show();
        ((BaseActivityTwoV)getActivity()).addSubscription(wrapObserverWithHttp(WorkService.getWorkService().listQunWant((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),qun_name,offer_sort,pageCurrent)).subscribe(new Subscriber<QunWantBean>() {
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
                removeErrorView();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onNext(QunWantBean qunWantBean) {
                if("1".equals(qunWantBean.state)){
                    if(pageCurrent == 1){
                        mAdapter.replaceAllDate(qunWantBean.qunList);
                    }else {
                        mAdapter.addDate(qunWantBean.qunList);
                    }
                }else {
                    showTest(qunWantBean.msg);
                }
            }
        }));
    }
}
