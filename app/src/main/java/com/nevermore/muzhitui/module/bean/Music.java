package com.nevermore.muzhitui.module.bean;

import java.util.List;

/**
 * Created by hehe on 2016/6/15.
 */
public class Music {


    /**
     * state : 1
     * musicList : [{"name":"刀郎-手心里的温柔.mp3","src":"upload/14614374529377.mp3"},{"name":"陈瑞-相思的债.mp3","src":"upload/146143741935519.mp3"},{"name":"陈瑞-五百年前的情缘.mp3","src":"upload/146143739991075.mp3"},{"name":"陈瑞-藕断丝连.mp3","src":"upload/146143738173179.mp3"},{"name":"陈瑞-梦醉西楼.mp3","src":"upload/146143735880388.mp3"},{"name":"布仁巴雅尔-鸿雁(蒙).mp3","src":"upload/146143732190341.mp3"},{"name":"爱之战-吴秀波","src":"upload/146143728486531.mp3"},{"name":"感恩的心.mp3","src":"upload/146143704178463.mp3"},{"name":"Korean.mp3","src":"upload/146143695036010.mp3"},{"name":"In_My_Secret_Life.mp3","src":"upload/146143679689120.mp3"},{"name":"神秘花园 钢琴名曲.mp3","src":"upload/14614368455964.mp3"},{"name":"爱的眼泪","src":"upload/14614366668678.mp3"},{"name":"日光海岸","src":"upload/14614364953771.mp3"},{"name":"刀郎-西海情歌.mp3","src":"upload/146143747732320.mp3"},{"name":"刀郎-谢谢你.mp3","src":"upload/146143749776847.mp3"},{"name":"龚玥-高原蓝.mp3","src":"upload/14614375274194.mp3"},{"name":"龚玥-红豆红.mp3","src":"upload/146143755377382.mp3"},{"name":"降央卓玛-呼伦贝尔大草原.mp3","src":"upload/146143758519666.mp3"},{"name":"降央卓玛-卓玛.mp3","src":"upload/146143760495729.mp3"},{"name":"侃侃-穿过生命散发的芬芳.mp3","src":"upload/146143763093010.mp3"},{"name":"权振东-亲爱的小孩(Live).mp3","src":"upload/146143765721411.mp3"},{"name":"石头&李玉刚-雨花石.mp3","src":"upload/146143769057039.mp3"},{"name":"吴秀波-保留纯真.mp3","src":"upload/146143773014034.mp3"},{"name":"云朵-云朵.mp3","src":"upload/146143776424560.mp3"},{"name":"汪峰-光明.mp3","src":"upload/146143782134269.mp3"},{"name":"汪峰-加德满都的风铃.mp3","src":"upload/146143786409622.mp3"},{"name":"汪峰-生来彷徨.mp3","src":"upload/146143789316326.mp3"},{"name":"汪峰-时光倒流.mp3","src":"upload/146143791611771.mp3"},{"name":"汪峰-像梦一样自由.mp3","src":"upload/146143794249997.mp3"},{"name":"汪峰-河流.mp3","src":"upload/146143804190424.mp3"},{"name":"汪峰-硬币(现场版).mp3","src":"upload/14614382883039.mp3"},{"name":"马頔-南山南.mp3","src":"upload/146143858234387.mp3"},{"name":"王菲-天空.mp3","src":"upload/146143871460662.mp3"},{"name":"李健-贝加尔湖畔.mp3","src":"upload/146143877411740.mp3"},{"name":"BECAUSE I LOVE YOU.MP3","src":"upload/146143884054781.MP3"},{"name":"Carpenter - Yesterday Once More.mp3","src":"upload/14614388764645.mp3"},{"name":"CASABLANCA.MP3","src":"upload/146143891329417.MP3"},{"name":"Country road.mp3","src":"upload/146143893895590.mp3"},{"name":"HOTEL CALL FORMIA.MP3","src":"upload/146143898915156.MP3"},{"name":"KISS ME SOFTLY FUGEES.MP3","src":"upload/146143902137533.MP3"},{"name":"RIGHT HERE WAITING.MP3","src":"upload/14614390892918.MP3"},{"name":"SAY YOU SAY ME.MP3","src":"upload/146143911995775.MP3"},{"name":"THE SOUND OF SILENCE.MP3","src":"upload/146143917227518.MP3"},{"name":"WHEN A MAN LOVE A WOMAN .MP3","src":"upload/146143921507057.MP3"},{"name":"WHEN I FALL IN LOVE.MP3","src":"upload/14614392746691.MP3"},{"name":"优美的配乐","src":"upload/146143932707975.MP3"},{"name":"吕方朋友别哭.MP3","src":"upload/146143938343198.MP3"},{"name":"牵手.mp3","src":"upload/146143946029739.mp3"},{"name":"take me to your heart.mp3","src":"upload/146143950462738.mp3"},{"name":"明天会更好.mp3","src":"upload/146143956058344.mp3"},{"name":"念亲恩.mp3","src":"upload/146143960216839.mp3"},{"name":"if a song could get me you","src":"upload/146163454121826.mp3"},{"name":"be what you wanna be","src":"upload/146163456800241.mp3"},{"name":"佛教音乐 - 绿度母心咒(桑吉平措)","src":"upload/146163510488862.mp3"},{"name":"冷漠、路勇、秋裤大叔 - 红包","src":"upload/14616351359107.mp3"},{"name":"路勇 - 放手也是一种幸福","src":"upload/146163516749791.mp3"},{"name":"路勇 - 我佛慈悲","src":"upload/146163519067752.mp3"},{"name":"路勇 - 愿做菩萨那朵莲","src":"upload/146163521175824.mp3"},{"name":"伦巴佛说(桑吉平措)","src":"upload/146163523931040.mp3"},{"name":"桑吉平措 - 白度母心咒","src":"upload/146163525557091.mp3"},{"name":"Adele-Rolling In The Deep","src":"upload/146294034308599.wma"},{"name":"Darin-Be What You Wanna Be","src":"upload/146294040137486.wma"},{"name":"Lenka-The Show","src":"upload/146294043758284.wma"},{"name":"Various Artists-Big Big World","src":"upload/146294046159799.wma"},{"name":"杨培安-我相信(DJ版)","src":"upload/146294054630727.wma"}]
     */

    private String state;
    /**
     * name : 刀郎-手心里的温柔.mp3
     * src : upload/14614374529377.mp3
     */

    private List<MusicListBean> musicList;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<MusicListBean> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<MusicListBean> musicList) {
        this.musicList = musicList;
    }

    public static class MusicListBean {
        private String name;
        private String src;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        @Override
        public String toString() {
            return "MusicListBean{" +
                    "name='" + name + '\'' +
                    ", src='" + src + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Music{" +
                "state='" + state + '\'' +
                ", musicList=" + musicList +
                '}';
    }
}
