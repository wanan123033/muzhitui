package com.nevermore.muzhitui.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nevermore.muzhitui.EventBusContanct;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.BaseBean;
import com.nevermore.muzhitui.module.bean.DicussBean;
import com.nevermore.muzhitui.module.bean.ExceInfo;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nevermore.muzhitui.module.service.NetWorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;

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
 * Created by Administrator on 2017/9/13.
 */

public class DynamicMsgActivity extends BaseActivityTwoV {
    private LoadingAlertDialog mLoadingAlertDialog;

    private CommonAdapter mAdapter;
    private List<DicussBean.Reply> replies = new ArrayList<>();

    @BindView(R.id.rv_dynamic_msg)
    RecyclerView rv_dynamic_msg;
    @Override
    public void init() {
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        setMyTitle("动态消息");
        showBack();

        mAdapter = new CommonAdapter<DicussBean.Reply>(this,R.layout.item_dynamic_msg,replies) {
            @Override
            public void convert(ViewHolder holder, final DicussBean.Reply o) {
                holder.setText(R.id.tv_name,o.user_name);
                holder.setText(R.id.tv_date,o.time);
                if (o.reply_content.equals("点赞")){
                    holder.setText(R.id.tv_content,"给了您一个");
                    holder.getView(R.id.iv_praise).setVisibility(View.VISIBLE);
                }else {
                    holder.setText(R.id.tv_content, o.reply_content);
                    holder.getView(R.id.iv_praise).setVisibility(View.GONE);
                }
                ImageLoader.getInstance().displayImage(o.headimg, (ImageView) holder.getView(R.id.civ_topic));
                ImageLoader.getInstance().displayImage(o.page_picpath, (ImageView) holder.getView(R.id.iv_content));
                holder.getView(R.id.civ_topic).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),DynamicPersonActivity.class);
                        intent.putExtra(DynamicPersonActivity.USERID,String.valueOf(o.reply_loginid));
                        startActivity(intent);
                    }
                });
            }

        };
        rv_dynamic_msg.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                DicussBean.Reply reply = (DicussBean.Reply) o;
                Intent intent = new Intent(getApplicationContext(),DynamicDatailActivity.class);
                intent.putExtra(DynamicDatailActivity.DYNAMIC_ID,reply.pagedt_id);
                startActivity(intent);

            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                return false;
            }
        });

        loadData(1);

        showRight("清空", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearMsg();
            }
        });

        clearDtUnread();

    }
    private void clearDtUnread() {
        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().clearDtCount((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))).subscribe(new Subscriber<com.nevermore.muzhitui.module.BaseBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(com.nevermore.muzhitui.module.BaseBean baseBean) {
                if("1".equals(baseBean.state)){
                    EventBus.getDefault().post(EventBusContanct.getInstance(EventBusContanct.MYDYNAMICACTIVITY_DYNAMIC_MSG_STATE));
                }
            }
        }));
    }
    private void clearMsg() {
        mLoadingAlertDialog.show();
        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().clearOneDtMsg((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))).subscribe(new Subscriber<BaseBean>() {
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
                    mAdapter.removeAllDate();
                }else {
                    showTest(baseBean.msg);
                }
            }
        }));
    }

    private void loadData(int pageCurrent) {
        mLoadingAlertDialog.show();
        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().listOneDtMsg((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),pageCurrent)).subscribe(new Subscriber<DicussBean>() {
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
            public void onNext(DicussBean dicussBean) {
                if("1".equals(dicussBean.state)){
                    mAdapter.addDate(dicussBean.replyList);
                }else {
                    showTest(dicussBean.msg);
                }
            }
        }));
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_dynamic_msg;
    }

    @OnClick(R.id.tv_gengzao)
    public void onClick(View view){
        showTest("没有更早的消息了哦");
    }
}
