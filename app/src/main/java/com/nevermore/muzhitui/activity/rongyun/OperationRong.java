package com.nevermore.muzhitui.activity.rongyun;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by AMing on 16/4/11.
 * Company RongCloud
 */
public class OperationRong {

    public static void setConversationTop(final Context context, Conversation.ConversationType conversationType, String targetId, boolean state) {
        if (!TextUtils.isEmpty(targetId) && RongIM.getInstance() != null) {
            RongIM.getInstance().setConversationToTop(conversationType, targetId, state, new RongIMClient.ResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    Toast.makeText(context,"设置成功",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Toast.makeText(context,"设置失败",Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    public static void setConverstionNotif(final Context context, Conversation.ConversationType conversationType, String targetId, boolean state) {
        Conversation.ConversationNotificationStatus cns;
        if (state) {
            cns = Conversation.ConversationNotificationStatus.DO_NOT_DISTURB;
        } else {
            cns = Conversation.ConversationNotificationStatus.NOTIFY;
        }
        RongIM.getInstance().setConversationNotificationStatus(conversationType, targetId, cns, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
            @Override
            public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                if (conversationNotificationStatus == Conversation.ConversationNotificationStatus.DO_NOT_DISTURB) {

                    Toast.makeText(context,"设置免打扰成功",Toast.LENGTH_SHORT).show();
                } else if (conversationNotificationStatus == Conversation.ConversationNotificationStatus.NOTIFY) {
                    Toast.makeText(context,"取消免打扰成功",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Toast.makeText(context,"设置失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
