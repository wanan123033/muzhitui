package com.nevermore.muzhitui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.MyFriendsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by Simone on 2016/12/28.
 * 消息Fragment
 */

public class MessageFragment extends Fragment {
    @BindView(R.id.ivMessageAdd)
    ImageView mIvMessageAdd;
    View v;
    public MessageFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.conversationlist,null);
        ButterKnife.bind(this, v);
        //这段话不能少，静态集成，否则会话列表不会显示内容
        ConversationListFragment fragment = (ConversationListFragment) getChildFragmentManager()
                .findFragmentById(R.id.conversationlist);
        Uri uri;

            uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统 先去掉系统消息

                    .build();
        fragment.setUri(uri);
        return v;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.ivMessageAdd)
    public void onClick() {    //标题栏上面的加号
        Intent intent=new Intent(getActivity(),MyFriendsActivity.class);
        intent.putExtra("fragment","messagefragment");
        startActivity(intent);



    }



}
