package com.nevermore.muzhitui.activity.rongyun;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.nevermore.muzhitui.EventBusContanct;
import com.nevermore.muzhitui.LoginActivity;
import com.nevermore.muzhitui.MainActivity;
import com.nevermore.muzhitui.activity.NewFriendsActivity;
import com.nevermore.muzhitui.activity.rongyun.broadcast.BroadcastManager;
import com.nevermore.muzhitui.activity.rongyun.json.DynamicMsg;
import com.nevermore.muzhitui.activity.rongyun.message.module.MyExtensionModule;
import com.nevermore.muzhitui.activity.SeePersonalInfoIsFriendActivity;
import com.nevermore.muzhitui.event.EventRedDot;
import com.nevermore.muzhitui.event.refrush;
import com.nevermore.muzhitui.module.sqllite.DBManager;
import com.nevermore.muzhitui.module.sqllite.UserInfoRong;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import base.MyLogger;
import base.SPUtils;
import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.CommandMessage;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.ImageMessage;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.LocationMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * Created by Bob on 15/8/21.
 */
public class MztRongContext implements RongIMClient.OnReceiveMessageListener, RongIM.OnSendMessageListener,
        RongIM.UserInfoProvider, RongIM.ConversationBehaviorListener,
        RongIMClient.ConnectionStatusListener, RongIM.ConversationListBehaviorListener,
        Handler.Callback {
/**
 * Created by zhjchen on 1/29/15.
 */

    /**
     * 融云SDK事件监听处理。
     * 把事件统一处理，开发者可直接复制到自己的项目中去使用。
     * <p/>
     * 该类包含的监听事件有：
     * 1、消息接收器：OnReceiveMessageListener。
     * 2、发出消息接收器：OnSendMessageListener。
     * 3、用户信息提供者：GetUserInfoProvider。
     * 4、好友信息提供者：GetFriendsProvider。
     * 5、群组信息提供者：GetGroupInfoProvider。
     * 7、连接状态监听器，以获取连接相关状态：ConnectionStatusListener。
     * 8、地理位置提供者：LocationProvider。
     * 9、自定义 push 通知： OnReceivePushMessageListener。
     * 10、会话列表界面操作的监听器：ConversationListBehaviorListener。
     */
    private static MztRongContext mMztRongContext;

    private SharedPreferences mPreferences;
    private static ArrayList<Activity> mActivities;
    private static final String TAG = MztRongContext.class.getSimpleName();
    private Context mContext;
    private DBManager mgr;
    public static final String UPDATE_RED_DOT = "update_red_dot";
    public static final String EXIT = "EXIT";


    public static MztRongContext getInstance() {
        if (mMztRongContext == null) {
            mMztRongContext = new MztRongContext();
        }
        return mMztRongContext;
    }

    private MztRongContext() {
    }

    /**
     * 初始化 RongCloud.
     *
     * @param context 上下文。
     */
    public static void init(Context context) {
        if (mMztRongContext == null) {
            synchronized (MztRongContext.class) {
                if (mMztRongContext == null) {
                    mMztRongContext = new MztRongContext(context);
                }
            }
        }

    }

    private MztRongContext(Context context) {
        mContext = context;

        // mHandler = new Handler(this);
        //初始化DBManager
        mgr = new DBManager(context);
        mMztRongContext = this;
        mActivities = new ArrayList<>();
        //http初始化 用于登录、注册使用
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        initDefaultListener();
    }


    public void pushActivity(Activity activity) {
        mActivities.add(activity);
    }

    public void popActivity(Activity activity) {
        if (mActivities.contains(activity)) {
            activity.finish();
            mActivities.remove(activity);
        }
    }

    public void popAllActivity(int position) {
        try {
            if (MainActivity.viewPager != null) {
                MainActivity.viewPager.setCurrentItem(position);
            }
            for (Activity activity : mActivities) {
                if (activity != null) {
                    activity.finish();
                }
            }
            mActivities.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public SharedPreferences getSharedPreferences() {
        return mPreferences;
    }
    public RongIMClient.ConnectCallback getConnectCallback() {
        RongIMClient.ConnectCallback connectCallback = new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
            }
            @Override
            public void onSuccess(String s) {
            }
            @Override
            public void onError(final RongIMClient.ErrorCode e) {
            }
        };
        return connectCallback;
    }


    /**
     * RongIM.init(this) 后直接可注册的Listener。
     */
    private void initDefaultListener() {
        RongIM.setUserInfoProvider(this, true);//设置用户信息提供者。
        //   RongIM.setGroupInfoProvider(this, true);//设置群组信息提供者。
        RongIM.setConversationBehaviorListener(this);//设置会话界面操作的监听器。
        //  RongIM.setLocationProvider(this);//设置地理位置提供者,不用位置的同学可以注掉此行代码
        RongIM.setConversationListBehaviorListener(this);
        RongIM.getInstance().setMessageAttachedUserInfo(true);//用户发送信息时携带用户信息提供者 暂时关闭，为避免有的用户携带的信息不存在name ,和头像信息，导致头像及名称显示不全的问题
         setMyExtensionModule(); //设置快速回复添加的

        RongIM.registerMessageType(TextMessage.class);//发送文本注册自定义消息
        RongIM.getInstance().setOnReceiveMessageListener(this);//设置消息接收监听器。
//
        RongIM.getInstance().setConnectionStatusListener(this);//设置连接状态监听器。
        RongIM.getInstance().enableNewComingMessageIcon(true);
        RongIM.getInstance().enableUnreadMessageIcon(true);



        BroadcastManager.getInstance(mContext).addAction(MztRongContext.EXIT, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                quit(false);
            }
        });

    }

    private void quit(boolean isKicked) {
        Log.d(TAG, "quit isKicked " + isKicked);
        SharedPreferences.Editor editor = mContext.getSharedPreferences("config", Context.MODE_PRIVATE).edit();
        if (!isKicked) {
            editor.putBoolean("exit", true);
        }
        editor.putString("loginToken", "");

        editor.putInt("getAllUserInfoState", 0);
        editor.apply();
        /*//这些数据清除操作之前一直是在login界面,因为app的数据库改为按照userID存储,退出登录时先直接删除
        //这种方式是很不友好的方式,未来需要修改同app server的数据同步方式
        */

        RongIM.getInstance().logout();
        Intent loginActivityIntent = new Intent();
        loginActivityIntent.setClass(mContext, LoginActivity.class);
        loginActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isKicked) {
            loginActivityIntent.putExtra("kickedByOtherClient", true);
        }
        mContext.startActivity(loginActivityIntent);
    }

    public void setMyExtensionModule() {
        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultModule = null;
        if (moduleList != null) {
            for (IExtensionModule module : moduleList) {
                if (module instanceof DefaultExtensionModule) {
                    defaultModule = module;
                    break;
                }
            }
            if (defaultModule != null) {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
                RongExtensionManager.getInstance().registerExtensionModule(new MyExtensionModule());
            }
        }
    }


    /**
     * 接收消息的监听器：OnReceiveMessageListener 的回调方法，接收到消息后执行。
     *
     * @param message 接收到的消息的实体信息。
     * @param left    剩余未拉取消息数目。
     */
    @Override
    public boolean onReceived(Message message, int left) {
        MyLogger.kLog().e("++++++++++++++++++++"+message.getContent());

        //发送无序广播

        MessageContent messageContent = message.getContent();
        int userId = (Integer) SPUtils.get(SPUtils.KEY_GET_ID,0);
        if(userId == 1000){
            sendTextMessage("亲爱的用户你好，我是拇指推自动回复小机器人， 我目前无法跟您沟通交流，如果需要帮助请添加我们的客服微信：muzhituikefu",messageContent.getUserInfo().getUserId()+"");

        }
        if(message.getConversationType() == Conversation.ConversationType.PRIVATE){   //聊天信息

        }else if(message.getConversationType() == Conversation.ConversationType.SYSTEM){  //系统消息

        }
        if (messageContent instanceof CommandMessage){
            CommandMessage commandMessage = (CommandMessage) messageContent;
            try {
                JSONObject json = new JSONObject(commandMessage.getData());
                JSONObject data = new JSONObject(json.getString("data"));
                DynamicMsg praise = new DynamicMsg();
                if(data.has("msg_r"))
                    praise.msg = data.getString("msg_r");
                if (data.has("pagedt_id"))
                    praise.pageId = data.getInt("pagedt_id");
                if (data.has("name"))
                    praise.name = data.getString("name");
                if (data.has("type_r")) {
                    praise.type = data.getString("type_r");
                    if(praise.type.equals("7")){   //我的下级
                        SPUtils.put(SPUtils.XIAJI_NUM,(int)SPUtils.get(SPUtils.XIAJI_NUM,0) + 1);
                        EventBus.getDefault().post(EventBusContanct.getInstance(EventBusContanct.REFRESH_XIAJI));
                        return false;
                    }
                    if("6".equals(praise.type)){  //收益
                        EventBus.getDefault().post(EventBusContanct.getInstance(EventBusContanct.TABMY_STATE));
                        return false;
                    }
                }
                EventBus.getDefault().post(praise);

            }catch (Exception e){

            }


        }
        if(messageContent instanceof TextMessage && message.getConversationType().getValue() == Conversation.ConversationType.SYSTEM.getValue()){
            MyLogger.kLog().e("好友请求.....");
            EventBus.getDefault().post(EventBusContanct.getInstance(EventBusContanct.REFRSH_FRIENDS));
        }else if(message.getConversationType().getValue() == Conversation.ConversationType.PRIVATE.getValue()){
            MyLogger.kLog().e("聊天信息.....");
            EventBus.getDefault().post(EventBusContanct.getInstance(EventBusContanct.REFRSH_CHAT));
        }



        if (messageContent instanceof ContactNotificationMessage) {

            ContactNotificationMessage contactNotificationMessage = (ContactNotificationMessage) messageContent;
            Log.e(TAG, "onReceived-Request" + contactNotificationMessage.getOperation());
            if (contactNotificationMessage.getOperation().equals("好友添加申请同意")) {
                Log.e(TAG, "onReceived-AcceptResponse" + contactNotificationMessage.getExtra());
                //对方同意我的好友请求

            }
            Log.e(TAG, "onReceived-请求添加的id:" + contactNotificationMessage.getTargetUserId() + "\t需要添加好友的id:" + contactNotificationMessage.getSourceUserId());
            //避免出现系统消息删掉聊天列表信息
            RongIM.getInstance().removeConversation(Conversation.ConversationType.SYSTEM, String.valueOf(contactNotificationMessage.getSourceUserId()), new RongIMClient.ResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {

                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                }
            });
            //避免出现系统消息删掉聊天记录信息
            RongIM.getInstance().clearMessages(Conversation.ConversationType.SYSTEM, String.valueOf(contactNotificationMessage.getSourceUserId()), new RongIMClient.ResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                }
            });
        }
//        } else if (messageContent instanceof TextMessage  ) {//文本消息
//            TextMessage textMessage = (TextMessage) messageContent;
//            String extra = textMessage.getExtra();
//
//            if("21".equals(extra) && message.getConversationType() == Conversation.ConversationType.SYSTEM){ //好友请求
//                int num = 0;
//                num = (((int) SPUtils.get(SPUtils.NEWFRIEND_NUM, 0)) + 1);
//                //对方发来好友邀请
//                SPUtils.put(SPUtils.NEWFRIEND_NUM, num);
//                BroadcastManager.getInstance(mContext).sendBroadcast(UPDATE_RED_DOT);
//                EventBus.getDefault().post(new refrush(55));
//                return false;
//            }else if(message.getConversationType() == Conversation.ConversationType.PRIVATE){
//                SPUtils.put(SPUtils.CHAT_MESSAGE_NUM, (int) SPUtils.get(SPUtils.CHAT_MESSAGE_NUM, 0) + 1);
//                EventBus.getDefault().post(new refrush(55));
//                Log.e(TAG, "onReceived-textMessage:" + " \nextra:" + textMessage.getExtra());
//
//                //将后台传回的附加信息 用户信息添加到那个好友里面去里面去
//           /* if (textMessage.getJSONUserInfo()!=null&&!TextUtils.isEmpty(textMessage.getJSONUserInfo().toString())){
//
//                messageContent.setUserInfo(parseJsonToUserInfo(textMessage.getJSONUserInfo()));
//                Log.e(TAG, "onReceived-name:" + messageContent.getUserInfo().getName());
//                Log.e(TAG, "onReceived-getPortraitUri:" + messageContent.getUserInfo().getPortraitUri());
//            }else */
//                if (textMessage.getUserInfo() == null && !TextUtils.isEmpty(textMessage.getExtra())) {
//
//                    messageContent.setUserInfo(parseJsonToUserInfo1(textMessage.getExtra()));
//                    // Log.e(TAG, "onReceived-name:" + messageContent.getUserInfo().getName());
//
//                }
//            }
//        } else if (messageContent instanceof ImageMessage) {//图片消息
//            ImageMessage imageMessage = (ImageMessage) messageContent;
//            SPUtils.put(SPUtils.CHAT_MESSAGE_NUM,(int)SPUtils.get(SPUtils.CHAT_MESSAGE_NUM,0) + 1);
//            if (imageMessage.getUserInfo()==null&&!TextUtils.isEmpty(imageMessage.getExtra())){
//
//                messageContent.setUserInfo(parseJsonToUserInfo1(imageMessage.getExtra()));
//                // Log.e(TAG, "onReceived-name:" + messageContent.getUserInfo().getName());
//
//            }
//            Log.e(TAG, "onReceived-ImageMessage:" + imageMessage.getRemoteUri());
//        } else if (messageContent instanceof VoiceMessage) {//语音消息
//            SPUtils.put(SPUtils.CHAT_MESSAGE_NUM,(int)SPUtils.get(SPUtils.CHAT_MESSAGE_NUM,0) + 1);
//            VoiceMessage voiceMessage = (VoiceMessage) messageContent;
//            Log.e(TAG, "onReceived-voiceMessage:" + voiceMessage.getUri().toString());
//            if (voiceMessage.getUserInfo()==null&&!TextUtils.isEmpty(voiceMessage.getExtra())){
//
//                messageContent.setUserInfo(parseJsonToUserInfo1(voiceMessage.getExtra()));
//                // Log.e(TAG, "onReceived-name:" + messageContent.getUserInfo().getName());
//
//            }
//        } else if (messageContent instanceof RichContentMessage) {//图文消息
//            RichContentMessage richContentMessage = (RichContentMessage) messageContent;
//            SPUtils.put(SPUtils.CHAT_MESSAGE_NUM,(int)SPUtils.get(SPUtils.CHAT_MESSAGE_NUM,0) + 1);
//            if (richContentMessage.getUserInfo()==null&&!TextUtils.isEmpty(richContentMessage.getExtra())){
//
//                messageContent.setUserInfo(parseJsonToUserInfo1(richContentMessage.getExtra()));
//                // Log.e(TAG, "onReceived-name:" + messageContent.getUserInfo().getName());
//
//            }
//            Log.e(TAG, "onReceived-RichContentMessage:" + richContentMessage.getContent());
//        } else if (messageContent instanceof InformationNotificationMessage) {//小灰条消息
//            SPUtils.put(SPUtils.CHAT_MESSAGE_NUM,(int)SPUtils.get(SPUtils.CHAT_MESSAGE_NUM,0) + 1);
//            InformationNotificationMessage informationNotificationMessage = (InformationNotificationMessage) messageContent;
//            Log.e(TAG, "onReceived-informationNotificationMessage:" + informationNotificationMessage.getMessage());
//            if (informationNotificationMessage.getUserInfo()==null&&!TextUtils.isEmpty(informationNotificationMessage.getExtra())){
//
//                messageContent.setUserInfo(parseJsonToUserInfo1(informationNotificationMessage.getExtra()));
//                // Log.e(TAG, "onReceived-name:" + messageContent.getUserInfo().getName());
//
//            }
//        } else {
//            Log.e(TAG, "onReceived-其他消息，自己来判断处理");
//        }

        return false;

    }

    private void sendTextMessage(String message, String userId) {
        TextMessage txtMsg = TextMessage.obtain(message);
        RongIM.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, userId, txtMsg, message, new Date(System.currentTimeMillis()).toString(), new RongIMClient.SendMessageCallback() {
            @Override
            public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                  //发送失败
            }

            @Override
            public void onSuccess(Integer integer) {
                //发送成功
            }
        });

    }
    public UserInfo parseJsonToUserInfo1(String str) {


        UserInfo info = null;
        try{
            JSONObject jsonObj=new JSONObject(str);
            Log.e("eeeeeeeee",jsonObj.toString());

            String id = jsonObj.optString("id");
            String name = jsonObj.optString("name");
            String icon = jsonObj.optString("portrait");
            if(TextUtils.isEmpty(icon)) {
                icon = jsonObj.optString("icon");
            }

            if(!TextUtils.isEmpty(id) && !TextUtils.isEmpty(name)) {
                Uri portrait = icon != null?Uri.parse(icon):null;
                info = new UserInfo(id, name, portrait);
            }
            Log.e("eeeeeeeee",info.getName());
        }catch (Exception e){
            e.printStackTrace();
        }


        return info;
    }
    @Override
    public Message onSend(Message message) {
        return message;
    }

    /**
     * 消息在UI展示后执行/自己的消息发出后执行,无论成功或失败。
     *
     * @param message 消息。
     */
    @Override
    public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
        if (message.getSentStatus() == Message.SentStatus.FAILED) {
            if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_CHATROOM) {//不在聊天室
            } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_DISCUSSION) {//不在讨论组
            } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_GROUP) {//不在群组
            } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.REJECTED_BY_BLACKLIST) {//你在他的黑名单中
            }
        }
        MessageContent messageContent = message.getContent();

        if (messageContent instanceof TextMessage) {//文本消息
            TextMessage textMessage = (TextMessage) messageContent;
            Log.e(TAG, "onSent-TextMessage:" + textMessage.getContent()+messageContent.getJSONUserInfo());
        } else if (messageContent instanceof ImageMessage) {//图片消息
            ImageMessage imageMessage = (ImageMessage) messageContent;
            Log.d(TAG, "onSent-ImageMessage:" + imageMessage.getRemoteUri());
        } else if (messageContent instanceof VoiceMessage) {//语音消息
            VoiceMessage voiceMessage = (VoiceMessage) messageContent;
            Log.d(TAG, "onSent-voiceMessage:" + voiceMessage.getUri().toString());
        } else if (messageContent instanceof RichContentMessage) {//图文消息
            RichContentMessage richContentMessage = (RichContentMessage) messageContent;
            Log.d(TAG, "onSent-RichContentMessage:" + richContentMessage.getContent());
        } else {
            Log.d(TAG, "onSent-其他消息，自己来判断处理");
        }
        return false;
    }

    /**
     * 用户信息的提供者：GetUserInfoProvider 的回调方法，获取用户信息。
     *
     * @param userId 用户 Id。
     * @return 用户信息，（注：由开发者提供用户信息）。
     */
    @Override
    public UserInfo getUserInfo(String userId) {
        /**
        * demo 代码 开发者需替换成自己的代码。
        */

        List<UserInfoRong> userInfoRongs = mgr.query();
        Log.e(TAG, "用戶信息提供者" + userInfoRongs.size());
        for (UserInfoRong userInfoRong : userInfoRongs) {
            Log.e("用户信息",userInfoRong.id+" \t"+userInfoRong.user_name+" \t"+userInfoRong.headimg);
            if (String.valueOf(userInfoRong.id).equals(userId)) {
                Log.e("用户信息",userInfoRong.user_name+" \t"+userInfoRong.headimg);

                return new UserInfo(userInfoRong.id + "", userInfoRong.user_name,userInfoRong.headimg==null?null: Uri.parse(userInfoRong.headimg));
            }
        }
        return null;
    }


    /**
     * 会话界面操作的监听器：ConversationBehaviorListener 的回调方法，当点击用户头像后执行。
     *
     * @param context          应用当前上下文。
     * @param conversationType 会话类型。
     * @param user             被点击的用户的信息。
     * @return 返回True不执行后续SDK操作，返回False继续执行SDK操作。
     */
    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo user) {
        Log.e(TAG, "----onUserPortraitClick");

/**
 * demo 代码 开发者需替换成自己的代码。
 */
        if (user != null) {

            if (conversationType.equals(Conversation.ConversationType.PUBLIC_SERVICE) || conversationType.equals(Conversation.ConversationType.APP_PUBLIC_SERVICE)) {
                RongIM.getInstance().startPublicServiceProfile(mContext, conversationType, user.getUserId());
            } else {

                Intent intent = new Intent(context, SeePersonalInfoIsFriendActivity.class);


                intent.putExtra("id", user.getUserId());

                context.startActivity(intent);
            }
        }

        return false;
    }

    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        Log.e(TAG, "----onUserPortraitLongClick");

        return true;
    }

    /**
     * 会话界面操作的监听器：ConversationBehaviorListener 的回调方法，当点击消息时执行。
     *
     * @param context 应用当前上下文。
     * @param message 被点击的消息的实体信息。
     * @return 返回True不执行后续SDK操作，返回False继续执行SDK操作。
     */
    @Override
    public boolean onMessageClick(Context context, View view, Message message) {
        Log.e(TAG, "----onMessageClick");

/**
 * demo 代码 开发者需替换成自己的代码。
 */
        if (message.getContent() instanceof LocationMessage) {
            Log.d("Begavior", "extra:" + message.getContent());
        } else if (message.getContent() instanceof RichContentMessage) {
            RichContentMessage mRichContentMessage = (RichContentMessage) message.getContent();
            Log.d("Begavior", "extra:" + mRichContentMessage.getExtra());

        } else if (message.getContent() instanceof ImageMessage) {
            Log.d("Begavior", "extra:" + "图片");
        }

        Log.d("Begavior", message.getObjectName() + ":" + message.getMessageId());

        return false;
    }

    @Override
    public boolean onMessageLinkClick(Context context, String s) {
        return false;
    }


    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {

        Log.e(TAG, "----onMessageLongClick");
        return false;
    }

    /**
     * 连接状态监听器，以获取连接相关状态:ConnectionStatusListener 的回调方法，网络状态变化时执行。
     *
     * @param connectionStatus 网络状态。
     */
    @Override
    public void onChanged(ConnectionStatus connectionStatus) {
        Log.d(TAG, "onChanged:" + connectionStatus);

        switch (connectionStatus) {

            case CONNECTED://连接成功。
                // Toast.makeText(mContext,"融云连接成功",Toast.LENGTH_SHORT).show();
                break;
            case DISCONNECTED://断开连接。
                //  Toast.makeText(mContext,"断开连接",Toast.LENGTH_SHORT).show();
                break;
            case CONNECTING://连接中。
                //  Toast.makeText(mContext,"连接中",Toast.LENGTH_SHORT).show();
                break;
            case NETWORK_UNAVAILABLE://网络不可用。
                //   Toast.makeText(mContext,"网络不可用",Toast.LENGTH_SHORT).show();
                break;
            case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                //  Toast.makeText(mContext,"用户账户在其他设备登录，本机会被踢掉线",Toast.LENGTH_SHORT).show();
                break;
        }
    }


    /**
     * 点击会话列表 item 后执行。
     *
     * @param context      上下文。
     * @param view         触发点击的 View。
     * @param conversation 会话条目。
     * @return 返回 true 不再执行融云 SDK 逻辑，返回 false 先执行融云 SDK 逻辑再执行该方法。
     */
    @Override
    public boolean onConversationClick(Context context, View view, UIConversation conversation) {
        MessageContent messageContent = conversation.getMessageContent();
        if (messageContent instanceof ContactNotificationMessage) {

            ContactNotificationMessage contactNotificationMessage = (ContactNotificationMessage) messageContent;
            Log.d(TAG, "onConversationClick: "+contactNotificationMessage.getOperation());
            if (contactNotificationMessage.getOperation().equals("AcceptResponse")) {
                Log.d(TAG, "onConversationClick: 被加方同意请求后");
                // 被加方同意请求后

            } else {
                Log.d(TAG, "onConversationClick: NewFriendsActivity" + contactNotificationMessage.getExtra().toString());
                context.startActivity(new Intent(context, NewFriendsActivity.class));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String s) {
        return false;
    }

    @Override
    public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String s) {
        return false;
    }

    /**
     * 长按会话列表 item 后执行。
     *
     * @param context      上下文。
     * @param view         触发点击的 View。
     * @param conversation 长按会话条目。
     * @return 返回 true 不再执行融云 SDK 逻辑，返回 false 先执行融云 SDK 逻辑再执行该方法。
     */
    @Override
    public boolean onConversationLongClick(Context context, View view, UIConversation conversation) {
        Log.e("onConversationLongClick",conversation.getConversationTargetId()+"\t"+conversation.getConversationSenderId()+"\t"+conversation.getUIConversationTitle()+"\t"+conversation.getConversationType());
        String s="拇指推官方帐号";
     /* if ((conversation.getConversationType()== Conversation.ConversationType.SYSTEM)
                && (conversation.getUIConversationTitle().equals(s)||conversation.getUIConversationTitle().equals("系统消息"))) {
            return true;
        }else */if ((conversation.getConversationType()== Conversation.ConversationType.PRIVATE)
                && conversation.getUIConversationTitle().equals(s)&& conversation.getConversationTargetId().equals("1000")) {
            return true;
        } else{
            Log.e("onConversationLongClick","no system");
            return false;
        }

    }


    @Override
    public boolean handleMessage(android.os.Message message) {
        return false;
    }


}
