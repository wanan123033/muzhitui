package com.nevermore.muzhitui;

/**
 * Created by Administrator on 2017/10/23.
 */

public class EventBusContanct {
    public static final int WX_XIU = 333;  //修改名片  发布名片
    private static EventBusContanct instance;

    public static final int MYDYNAMICACTIVITY_DYNAMIC_MSG_STATE = 255;  //动态消息被清空
    public static final int REFRESH_QUN_EXCHANGE = 105;
    public static final int TABMY_STATE = 110;  //主页  我的上面加点
    public static final int REFRESH_DYNAMIC = 119;
    public static final int REFRESH_DYNAMIC_MSG = 169;
    public static final int REFRSH_CHAT = 698; //更新聊天信息
    public static final int REFRESH_XIAJI = 666; //刷新我的下级
    public static int REFRSH_FRIENDS = 296;  //刷新好友请求
    private int flag;
    public static final int TABMYFRAGMENT_REFRSH_FAN_JIA1 = 122;    //关注数加1
    public static final int TABMYFRAGMENT_REFRSH_FAN_JIAN1 = 233;   //关注数减1

    public EventBusContanct(int flag){
        this.flag = flag;
    }

    private EventBusContanct() {

    }

    public static synchronized EventBusContanct getInstance(int flag){
        if(instance == null){
            instance = new EventBusContanct();
        }
        instance.setFlag(flag);
        return instance;
    }
    public int getFlag(){
        return flag;
    }

    private void setFlag(int flag) {
        this.flag = flag;
    }
}
