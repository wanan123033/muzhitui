package com.nevermore.muzhitui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import base.BaseActivityTwoV;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by Administrator on 2017/8/2.
 */

public class MessageActivity extends BaseActivityTwoV {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void init() {
        //这段话不能少，静态集成，否则会话列表不会显示内容
        ConversationListFragment fragment = (ConversationListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.conversationlist);
        Uri uri;

        uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                .build();
        fragment.setUri(uri);
        showBack(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(EventBusContanct.getInstance(EventBusContanct.REFRSH_CHAT));
            }
        });
        setMyTitle("消息");
    }
    @Override
    public int createSuccessView() {
        return R.layout.activity_message;
    }

}
