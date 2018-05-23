package com.nevermore.muzhitui.module.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/28.
 */

public class MusicDataBean {
    /**
     * msg : 查询所有背景音乐信息成功
     * musicArray : [{"category":"默认","list":[{"name":"无背景音乐","url":""}]},{"category":"优美","list":[{"name":"月亮代表我的心","url":"http://static2.ivwen.com/music/YueLiangDaiBiaoWoDeXin.m4a"},{"name":"爱的罗曼史","url":"http://static2.ivwen.com/music/AiDeLuoManShi.m4a"}]},{"category":"节日","list":[{"name":"神秘园","url":"http://static2.ivwen.com/music/ShenMiYuan.m4a"},{"name":"清晨","url":"http://static2.ivwen.com/music/QingChen.m4a"},{"name":"雪之梦","url":"http://static2.ivwen.com/music/XueZhiMeng.m4a"},{"name":"River Flows In You","url":"http://static2.ivwen.com/music/RiverFlowsInYou.m4a"},{"name":"Kiss The Rain","url":"http://static2.ivwen.com/music/KissTheRain.m4a"}]},{"category":"浪漫","list":[{"name":"梁祝","url":"http://static2.ivwen.com/music/LiangZhu.m4a"},{"name":"战马奔腾","url":"http://static2.ivwen.com/music/ZhanMaBenTeng.m4a"}]}]
     * state : 1
     */

    private String msg;
    private String state;
    private List<MusicArrayBean> musicArray;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<MusicArrayBean> getMusicArray() {
        return musicArray;
    }

    public void setMusicArray(List<MusicArrayBean> musicArray) {
        this.musicArray = musicArray;
    }

    public static class MusicArrayBean {
        /**
         * category : 默认
         * list : [{"name":"无背景音乐","url":""}]
         */

        private String category;
        private List<ListBean> list;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }


    }
    public static class ListBean {
        /**
         * name : 无背景音乐
         * url :
         */

        private String name;
        private String url;
        boolean isCheck=false;
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "name='" + name + '\'' +
                    ", url='" + url + '\'' +
                    ", isCheck=" + isCheck +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "MusicDataBean{" +
                "msg='" + msg + '\'' +
                ", state='" + state + '\'' +
                ", musicArray=" + musicArray +
                '}';
    }
}
