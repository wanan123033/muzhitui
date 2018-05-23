package com.nevermore.muzhitui.activity.rongyun;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.adapter.LvQuickReplyAdapter;
import com.nevermore.muzhitui.module.bean.VedioText;
import com.nevermore.muzhitui.module.json.QuickReplyBean;
import com.nevermore.muzhitui.module.json.QuickReplyJson;

import java.util.Date;
import java.util.List;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.UIUtils;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.TextMessage;

/**
 * Created by Simone on 2017/3/30.
 */

public class QuickReplyActivity extends BaseActivityTwoV {

    @BindView(R.id.gvQuickReply)
    ExpandableListView gvQuickReply;



    private AlertDialog alertDialog;
    String id = "100001";
    List<QuickReplyBean> mQuickReplyBean;
    @Override
    public void init() {
        showBack();
        setMyTitle("快速回复");
        if (!TextUtils.isEmpty((String) SPUtils.get(SPUtils.QUICK_REPLY_ID, ""))) {
            id = (String) SPUtils.get(SPUtils.QUICK_REPLY_ID, "");
        }
        mQuickReplyBean = QuickReplyJson.getSampleMusic();

        final    LvQuickReplyAdapter adapter = new LvQuickReplyAdapter(mQuickReplyBean, this, gvQuickReply);
        gvQuickReply.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        gvQuickReply.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,final int groupPosition,final int childPosition, long id) {
                alertDialog = UIUtils.getAlertDialogText(QuickReplyActivity.this,mQuickReplyBean.get(groupPosition).getList().get(childPosition).getName(), mQuickReplyBean.get(groupPosition).getList().get(childPosition).getUrl(),
                        "取消", "发送", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();


                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sendTextMessage(mQuickReplyBean.get(groupPosition).getList().get(childPosition).getUrl());
                                alertDialog.dismiss();
                                finish();

                            }
                        });
                alertDialog.show();
                return false;
            }
        });

    }

    // 发送文本消息。
    private void sendTextMessage(String message) {

        TextMessage txtMsg = TextMessage.obtain(message);


        RongIM.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, id, txtMsg, message, new Date(System.currentTimeMillis()).toString(), new RongIMClient.SendMessageCallback() {
            @Override
            public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                showTest("发送失败");
            }

            @Override
            public void onSuccess(Integer integer) {
                showTest("发送成功");
            }
        });
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_quick_reply;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


}
