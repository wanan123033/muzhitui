package com.nevermore.muzhitui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nevermore.muzhitui.PageLookActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.Code;
import com.nevermore.muzhitui.module.bean.SystemMsg;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.UIUtils;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
import base.recycler.recyclerview.OnItemClickListener;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/8/15.
 */

public class SystemMsgActivity extends BaseActivityTwoV {
    @BindView(R.id.rv_systemMsg)
    RecyclerView rv_systemMsg;

    private List<SystemMsg.Notice> msgs = new ArrayList<>();

    private CommonAdapter mAdapter;
    //http://www.jb51.net/article/116569.htm

    private LoadingAlertDialog mLoadingAlertDialog;
    AlertDialog alertDialog;
    @Override
    public void init() {
        setMyTitle("系统消息");
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        mAdapter = new CommonAdapter<SystemMsg.Notice>(this,R.layout.rv_system_msg_item,msgs) {
            @Override
            public void convert(ViewHolder holder, final SystemMsg.Notice notice) {
                Log.i("TAG","convert-------");
                holder.setText(R.id.tv_msgTitle,notice.getTitle());
                holder.setText(R.id.tv_msgContent,notice.getContent_1());
                holder.setText(R.id.tv_date,notice.getUpdate_time());
                ImageLoader.getInstance().displayImage(notice.getImg_url(),(ImageView) holder.getView(R.id.iv_imgContent));

                holder.setOnClickListener(R.id.tvDelete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                holder.setOnLongClickListener(R.id.slyt, new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        alertDialog = UIUtils.getAlertDialog(SystemMsgActivity.this, null,"删除该消息?", "取消", "确定", 0, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();


                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                delete(notice);
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.show();
                        return true;
                    }
                });
            }
        };

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                Intent intent = new Intent(getApplicationContext(), PageLookActivity.class);
                intent.putExtra(PageLookActivity.KEY_IS_SHARE,false);
                intent.putExtra(PageLookActivity.KEY_URL,msgs.get(position).getUrl());
                startActivity(intent);

            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                return false;
            }
        });
        rv_systemMsg.setAdapter(mAdapter);
        loadData();
        showBack();

    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_system_msg;
    }
    private void loadData() {
        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().getSystemMsg((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))).subscribe(new Subscriber<SystemMsg>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(SystemMsg msg) {
                Log.i("TAG",msg.toString());
                msgs.addAll(msg.getNoticeArray());
                mAdapter.notifyDataSetChanged();
            }
        }));
    }

    public void delete(final SystemMsg.Notice notice){
        //TODO 删除系统消息
        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().delSysNotice((String)SPUtils.get(SPUtils.GET_LOGIN_ID,""),notice.getId())).subscribe(new Subscriber<Code>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Code o) {
                        if(o.getState().equals("1")){
                            showTest("删除成功！");
                            msgs.remove(notice);
                            mAdapter.notifyDataSetChanged();
                        }else{
                            showTest("删除失败！");
                        }
                    }
                })
        );
    }

}
