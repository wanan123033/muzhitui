package com.nevermore.muzhitui.module.sqllite;

/**
 * Created by Simone on 2017/3/13.
 */

public class UserInfoRong {
    /**
     * id : 100004
     * user_name : 大海
     * <p>
     * headimg : http://www.muzhitui.cn/song/upload/2016-05-24/146407190508061.jpg
     * <p>
     * agent : 年费会员
     */

    public int id;
    public String user_name;

    public String headimg;

    public String agent;
    public UserInfoRong() {

    }
    public UserInfoRong(int id, String user_name, String headimg, String agent) {
        this.id = id;
        this.user_name = user_name;
        this.headimg = headimg;
        this.agent = agent;
    }
}
