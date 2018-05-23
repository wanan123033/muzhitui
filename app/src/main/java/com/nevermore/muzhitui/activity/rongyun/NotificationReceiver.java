package com.nevermore.muzhitui.activity.rongyun;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import io.rong.imlib.model.Conversation;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * Created by Simone on 2017/2/21.
 */

public class NotificationReceiver extends PushMessageReceiver {
    /* push 通知到达事件*/
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage message) {

        return false; // 返回 false, 会弹出融云 SDK 默认通知; 返回 true, 融云 SDK 不会弹通知, 通知需要由您自定义。
    }

    /* push 通知点击事件 */
    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage message) {
        return false; // 返回 false, 会走融云 SDK 默认处理逻辑, 即点击该通知会打开会话列表或会话界面; 返回 true, 则由您自定义处理逻辑。
    }
}
