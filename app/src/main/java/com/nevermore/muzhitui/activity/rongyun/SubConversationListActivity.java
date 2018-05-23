package com.nevermore.muzhitui.activity.rongyun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.adapter.SubConversationListAdapterEx;

import base.BaseActivityTwoV;

import io.rong.imkit.RongContext;
import io.rong.imkit.fragment.SubConversationListFragment;

/**
 * Created by Bob on 15/11/3.
 * 聚合会话列表
 */
public class SubConversationListActivity extends BaseActivityTwoV {
    @Override
    public void init() {

        showBack();
        Intent intent = getIntent();
        if (intent.getData() == null) {
            return;
        }
        //聚合会话参数
        String type = intent.getData().getQueryParameter("type");

        if (type == null)
            return;

        if (type.equals("group")) {
            setMyTitle("" + R.string.de_actionbar_sub_group);
        } else if (type.equals("private")) {
            setMyTitle("" + R.string.de_actionbar_sub_private);
        } else if (type.equals("discussion")) {
            setMyTitle("" + R.string.de_actionbar_sub_discussion);
        } else if (type.equals("system")) {
           /* setMyTitle("444444" + R.string.rc_conversation_list_system_conversation);*/
            setMyTitle("系统消息" );
        } else {
            setMyTitle("" + R.string.de_actionbar_sub_defult);
        }
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_rong;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SubConversationListFragment fragment = new SubConversationListFragment();
        fragment.setAdapter(new SubConversationListAdapterEx(RongContext.getInstance()));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.rong_content, fragment);
        transaction.commit();

    }


}
