package com.nevermore.muzhitui.activity;


import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;

import android.widget.CompoundButton;
import android.widget.FrameLayout;

import android.widget.ToggleButton;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.rongyun.MztRongContext;
import com.nevermore.muzhitui.activity.rongyun.OperationRong;


import base.BaseActivityTwoV;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.eventbus.EventBus;
import io.rong.imkit.RongIM;

import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.utilities.PromptPopupDialog;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;


/**
 * Created by Simone on 2017/1/19.
 */

public class ChatInfoActivity extends BaseActivityTwoV implements View.OnClickListener , CompoundButton.OnCheckedChangeListener {


    @BindView(R.id.tbTopChatInfo)
    ToggleButton mTbTopChatInfo;
    @BindView(R.id.tbNewInfoNotice)
    ToggleButton mTbNewInfoNotice;
    @BindView(R.id.tvChatUserInfo)
    FrameLayout mTvChatUserInfo;
    @BindView(R.id.flEmptyInfo)
    FrameLayout mFlEmptyInfo;
    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;

    private UserInfo mUserInfo;
    @Override
    public void init() {
        showBack();
        setMyTitle("聊天详情");
        mConversationType = Conversation.ConversationType.PRIVATE;
        if (!TextUtils.isEmpty(getIntent().getStringExtra("id"))) {
            mUserInfo = RongUserInfoManager.getInstance().getUserInfo(getIntent().getStringExtra("id"));
            updateUI();
        }
        EventBus.getDefault().register(this);
        MztRongContext.getInstance().pushActivity(this);

        mTbTopChatInfo.setOnCheckedChangeListener(this);
        mTbNewInfoNotice.setOnCheckedChangeListener(this);



    }
    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        MztRongContext.getInstance().popActivity(this);
        super.onDestroy();
    }
    private void updateUI() {
        if (mUserInfo != null) {

            getState(mUserInfo.getUserId());
        }
    }
    private void getState(String targetId) {
        if (targetId != null) {//群组列表 page 进入
            if (RongIM.getInstance() != null) {
                RongIM.getInstance().getConversation(Conversation.ConversationType.PRIVATE, targetId, new RongIMClient.ResultCallback<Conversation>() {
                    @Override
                    public void onSuccess(Conversation conversation) {
                        if (conversation == null) {
                            return;
                        }

                        if (conversation.isTop()) {
                            mTbTopChatInfo.setChecked(true);
                        } else {
                            mTbTopChatInfo.setChecked(false);
                        }

                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });

                RongIM.getInstance().getConversationNotificationStatus(Conversation.ConversationType.PRIVATE, targetId, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                    @Override
                    public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {

                        if (conversationNotificationStatus == Conversation.ConversationNotificationStatus.DO_NOT_DISTURB) {
                            mTbNewInfoNotice.setChecked(true);
                        } else {
                            mTbNewInfoNotice.setChecked(false);
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
            }
        }
    }

    public void onEventMainThread(UserInfo userInfo) {
        if (userInfo != null && userInfo.getUserId().equals(getIntent().getStringExtra("id"))) {
            mUserInfo = userInfo;
            updateUI();
        }
    }
    @Override
    public int createSuccessView() {
        return R.layout.activity_chat_info;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tvChatUserInfo, R.id.flEmptyInfo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvChatUserInfo:
                Intent intent = new Intent(ChatInfoActivity.this, SeePersonalInfoIsFriendActivity.class);


                intent.putExtra("id", getIntent().getStringExtra("id"));

                startActivity(intent);

                break;
            case R.id.flEmptyInfo:

                PromptPopupDialog.newInstance(this,
                        getString(R.string.clean_private_chat_history)).setLayoutRes(io.rong.imkit.R.layout.rc_dialog_popup_prompt_warning)
                        .setPromptButtonClickedListener(new PromptPopupDialog.OnPromptButtonClickedListener() {
                            @Override
                            public void onPositiveButtonClicked() {
                                if (RongIM.getInstance() != null) {
                                    if (mUserInfo != null) {
                                        RongIM.getInstance().clearMessages(Conversation.ConversationType.PRIVATE, mUserInfo.getUserId(), new RongIMClient.ResultCallback<Boolean>() {
                                            @Override
                                            public void onSuccess(Boolean aBoolean) {
                                           showTest("清除成功"); }

                                            @Override
                                            public void onError(RongIMClient.ErrorCode errorCode) {
                                                showTest("清除失败"); }
                                        });
                                    }
                                }
                            }
                        }).show();

        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.tbTopChatInfo:
                if (isChecked) {
                    if (mUserInfo != null) {
                        OperationRong.setConversationTop(ChatInfoActivity.this, Conversation.ConversationType.PRIVATE, mUserInfo.getUserId(), true);
                    }
                } else {
                    if (mUserInfo != null) {
                        OperationRong.setConversationTop(ChatInfoActivity.this, Conversation.ConversationType.PRIVATE, mUserInfo.getUserId(), false);
                    }
                }
                break;
            case R.id.tbNewInfoNotice:
                if (isChecked) {
                    if (mUserInfo != null) {
                        OperationRong.setConverstionNotif(ChatInfoActivity.this, Conversation.ConversationType.PRIVATE, mUserInfo.getUserId(), true);
                    }
                } else {
                    if (mUserInfo != null) {
                        OperationRong.setConverstionNotif(ChatInfoActivity.this, Conversation.ConversationType.PRIVATE, mUserInfo.getUserId(), false);
                    }
                }
                break;
        }
    }
}
