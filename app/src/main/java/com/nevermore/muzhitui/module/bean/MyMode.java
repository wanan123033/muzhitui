package com.nevermore.muzhitui.module.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hehe on 2016/6/3.
 */
public class MyMode {


    private String state;
    /**
     * topId : 129
     * linkUrl : http://www.tpy10.net/ewm.php?name=muzhitui8
     * publicNo : 拇指推
     * topDate : 2016-04-26 00:00:00
     * img : wx/img/yuemei.png
     * table : top
     */

    private List<TopArrayBean> topArray;
    /**
     * botId : 20
     * title1 : 赶紧使用拇指推推广神器吧
     * title2 : 长按扫描公共号二维码
     * img : upload/146018889722215.jpg
     * table : bot
     */

    private List<BotArrayBean> botArray;
    /**
     * seltitle :
     * adimage : upload/145979728780975.jpg
     * adverthtml : null
     * adurl : Www.jd.com
     * advertid : 63
     * adtitle : 拇指推编辑器，好用
     * adcolor :
     * table : adv
     * adtext :
     */

    private List<AdvArrayBean> advArray;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<TopArrayBean> getTopArray() {
        return topArray;
    }

    public void setTopArray(List<TopArrayBean> topArray) {
        this.topArray = topArray;
    }

    public List<BotArrayBean> getBotArray() {
        return botArray;
    }

    public void setBotArray(List<BotArrayBean> botArray) {
        this.botArray = botArray;
    }

    public List<AdvArrayBean> getAdvArray() {
        return advArray;
    }

    public void setAdvArray(List<AdvArrayBean> advArray) {
        this.advArray = advArray;
    }

    public static class TopArrayBean extends BaseMyMode implements Serializable {
        private int topId;
        private String linkUrl;
        private String publicNo;
        private String topDate;
        private String img;
        private String table;

        public int getTopId() {
            return topId;
        }

        public void setTopId(int topId) {
            setModeId(topId);
            this.topId = topId;
        }

        public String getLinkUrl() {
            return linkUrl;
        }

        public void setLinkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
        }

        public String getPublicNo() {
            return publicNo;
        }

        public void setPublicNo(String publicNo) {
            this.publicNo = publicNo;
        }

        public String getTopDate() {
            return topDate;
        }

        public void setTopDate(String topDate) {
            this.topDate = topDate;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getTable() {
            return table;
        }

        public void setTable(String table) {
            setModeTable(table);
            this.table = table;
        }


    }

    public static class BotArrayBean extends BaseMyMode implements Serializable {
        private int botId;
        private String title1;
        private String title2;
        private String img;
        private String table;

        public int getBotId() {
            return botId;
        }

        public void setBotId(int botId) {
            setModeId(botId);
            this.botId = botId;
        }

        public String getTitle1() {
            return title1;
        }

        public void setTitle1(String title1) {
            this.title1 = title1;
        }

        public String getTitle2() {
            return title2;
        }

        public void setTitle2(String title2) {
            this.title2 = title2;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getTable() {
            return table;
        }

        public void setTable(String table) {
            setModeTable(table);
            this.table = table;
        }
    }

    public static class AdvArrayBean extends BaseMyMode implements Serializable {
        private String seltitle;
        private String adimage;
        private Object adverthtml;
        private String adurl;
        private int advertid;
        private String adtitle;
        private String adcolor;
        private float size;
        private int animate;
        private String font;
        private String table;
        private String adtext;

        @Override
        public String toString() {
            return "AdvArrayBean{" +
                    "seltitle='" + seltitle + '\'' +
                    ", adimage='" + adimage + '\'' +
                    ", adverthtml=" + adverthtml +
                    ", adurl='" + adurl + '\'' +
                    ", advertid=" + advertid +
                    ", adtitle='" + adtitle + '\'' +
                    ", adcolor='" + adcolor + '\'' +
                    ", size=" + size +
                    ", animate=" + animate +
                    ", font='" + font + '\'' +
                    ", table='" + table + '\'' +
                    ", adtext='" + adtext + '\'' +
                    '}';
        }

        public String getSeltitle() {
            return seltitle;
        }

        public void setSeltitle(String seltitle) {
            this.seltitle = seltitle;
        }

        public String getAdimage() {
            return adimage;
        }

        public void setAdimage(String adimage) {
            this.adimage = adimage;
        }

        public Object getAdverthtml() {
            return adverthtml;
        }

        public void setAdverthtml(Object adverthtml) {
            this.adverthtml = adverthtml;
        }

        public String getAdurl() {
            return adurl;
        }

        public void setAdurl(String adurl) {
            this.adurl = adurl;
        }

        public int getAdvertid() {
            return advertid;
        }

        public void setAdvertid(int advertid) {
            setModeId(advertid);
            this.advertid = advertid;
        }

        public String getAdtitle() {
            return adtitle;
        }

        public void setAdtitle(String adtitle) {
            this.adtitle = adtitle;
        }

        public String getAdcolor() {
            return adcolor;
        }

        public void setAdcolor(String adcolor) {
            this.adcolor = adcolor;
        }

        public String getTable() {
            return table;
        }

        public void setTable(String table) {
            setModeTable(table);
            this.table = table;
        }

        public float getSize() {
            return size;
        }

        public void setSize(float size) {
            this.size = size;
        }

        public int getAnimate() {
            return animate;
        }

        public void setAnimate(int animate) {
            this.animate = animate;
        }

        public String getFont() {
            return font;
        }

        public void setFont(String font) {
            this.font = font;
        }

        public String getAdtext() {
            return adtext;
        }

        public void setAdtext(String adtext) {
            this.adtext = adtext;
        }
    }
}
