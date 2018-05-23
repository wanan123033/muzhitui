package com.nevermore.muzhitui.module.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Simone on 2017/1/6.
 */

public class MyLever implements Serializable{
    /**
     * loginList : [{"id":100020,"user_name":"@illen","mp_name":"谭浩洋","headimg":"http://www.muzhitui.cn/song/upload/2016-12-02/148061547726395.jpg","wechatimg":null,"wechatname":null,"agent":"年费会员"},{"id":100905,"user_name":"萍  格","mp_name":"李萍格","headimg":"http://wx.qlogo.cn/mmopen/FiaIW3HsXzg5QfV0reIbFtZLWo8HkHmTte6JePyicMVOVicvFT1uJ4oPlr7zZZEZibNJhicibxCpQT54In0BBeyM7nXLmtiaFFGMZLX/0","wechatimg":null,"wechatname":null,"agent":"年费会员"},{"id":126657,"user_name":"孙大胜~心悦","mp_name":"孙姐","headimg":"http://wx.qlogo.cn/mmopen/giaQibFlLQKPziaLmfd8UeicNBJmTIve89PlZm8yL4TprqOyE4R9cZMt7nV7W02RZU4tlKyWUyPOgDo2T84IuwEsaEEp90qdgS0U/0","wechatimg":null,"wechatname":null,"agent":"年费会员"},{"id":100178,"user_name":"深圳声飞扬录音棚-刘畅","mp_name":null,"headimg":"http://wx.qlogo.cn/mmopen/ePUrbOuFBgrcAG5xCnlSCUoZiaHVr4l4WIrnb0OsicUZUtGY4DkaeHdiao59h3lz9PL0zX5vGmuiboqbmYqUTwbpEytiaOgg2yFyx/0","wechatimg":null,"wechatname":null,"agent":"年费会员"},{"id":151232,"user_name":"拇指推客服-瑞琪","mp_name":"瑞琪","headimg":"http://wx.qlogo.cn/mmopen/FiaIW3HsXzg4LRHvxCG7jbFSredzH2ObkBeyfChkic0fjBBfvETNPFicXBEk5OoW8FOJl4R4Kc3thgunvoHrSeWvw/0","wechatimg":null,"wechatname":null,"agent":"年费会员"},{"id":119897,"user_name":"心道","mp_name":null,"headimg":"http://wx.qlogo.cn/mmopen/PiajxSqBRaEJUUvMN8cSyibicnxsibxRyRwsu4hm5zibicWFCvHxrvJcJEzv8cw8guZ9A3by462Z9q3Oq5nfcUrsc3ibg/0","wechatimg":null,"wechatname":null,"agent":"年费会员"},{"id":103832,"user_name":"wayne ","mp_name":null,"headimg":"http://wx.qlogo.cn/mmopen/giaQibFlLQKPwemVQr73ESJAfQcYKsDewyWfREBDcYddQJgbE810M7HBibI1K5OEv0dztAVh0e3Z47Sqf2W6rqXF4upDNAWPpvP/0","wechatimg":null,"wechatname":null,"agent":"年费会员"},{"id":112963,"user_name":"明酱","mp_name":"陈先生","headimg":"http://www.muzhitui.cn/song/upload/2016-08-03/147021651830447.jpg","wechatimg":null,"wechatname":null,"agent":"年费会员"},{"id":113081,"user_name":"Anna","mp_name":null,"headimg":"http://wx.qlogo.cn/mmopen/giaQibFlLQKPwemVQr73ESJMibM22PMib2wZoDs8uWVTNmbdVEdnoLRwticGkuQhkben23cAlRLBA6DMbC6QZYwKB0dMiaH7SvtUsp/0","wechatimg":null,"wechatname":null,"agent":"年费会员"},{"id":101786,"user_name":"Shiny","mp_name":"陆发光","headimg":"http://wx.qlogo.cn/mmopen/giaQibFlLQKPwQBSMzebO617OBsOMhuiateWsg66Y85BDsI4f63sGqcqeT6b2h2uX010VbrZM2uHjlEoibVnWdia54ib9vBOYH57icI/0","wechatimg":null,"wechatname":null,"agent":"年费会员"},{"id":124657,"user_name":" 蔡瑷瞳","mp_name":"蔡瑷瞳","headimg":"http://wx.qlogo.cn/mmopen/A48OAoYEtrVuNheOMFasdYI6Gm34N2jJ2Ovu6pWAFy0Pe0J6ncNGaFZmSI4Vj7qzdOQUklRxls1XAJNpxbcPwiaaBnMYiaGPGO/0","wechatimg":null,"wechatname":null,"agent":"年费会员"},{"id":109376,"user_name":"盛世互联陈聪✅","mp_name":"陈聪","headimg":"http://wx.qlogo.cn/mmopen/FoD5H2I9q2EOphLt9mjNCeZvmMV4ibO42PWC3bsA3LqYA6XvgE1eOw5Fj3AaCm9S5bwYK6ibqTeOk24Eh9VHJ3sBiapt2bF7wHo/0","wechatimg":null,"wechatname":null,"agent":"年费会员"},{"id":100015,"user_name":"kevin","mp_name":"大钰儿","headimg":"http://www.muzhitui.cn/song/upload/2017-01-05/148359790114399.jpg","wechatimg":null,"wechatname":null,"agent":"年费会员"},{"id":100871,"user_name":"悠悠","mp_name":null,"headimg":"http://wx.qlogo.cn/mmopen/giaQibFlLQKPziaLmfd8UeicNDib7iaheIclFZ8aicElslCxzHQvG31v7xOl72eBFXic8MufMgtYnrP4NliczllQ3QWW2cKP6rbKh61mL/0","wechatimg":null,"wechatname":null,"agent":"年费会员"},{"id":100050,"user_name":"周建云 ","mp_name":"周建云","headimg":"http://wx.qlogo.cn/mmopen/FoD5H2I9q2GZlcY1k1g11yumibvZFkrVNic13oa7foHlreM2L8Ceb9ibzY0TpgxicFxFiaQAJr01aLVM32d0dvEZibNUUfSRVtl1bA/0","wechatimg":null,"wechatname":null,"agent":"年费会员"},{"id":101190,"user_name":"Owen柯天雄","mp_name":"柯天雄","headimg":"http://wx.qlogo.cn/mmopen/giaQibFlLQKPyFmiahM7ILSCYFkSOfTr8zTRcL5icNhQ9V7REfMEIgxzIPqEcnQBzYoa1dawqgNQOk0xROKBejQq2lq7LUjBYbpg/0","wechatimg":null,"wechatname":null,"agent":"年费会员"},{"id":136685,"user_name":"笑面人生罗锦浦","mp_name":"罗锦浦","headimg":"http://wx.qlogo.cn/mmopen/zXhk4QWoegPFCxZx4swARXS31HicOdGcMJF11TcHtfRSpBgibj4Vibo0sHOByxiabpDX6U076wwHhWJdDOpaPl7MSadL6xP10Fdib/0","wechatimg":null,"wechatname":null,"agent":"年费会员"},{"id":101390,"user_name":"☻\u2026蓬 ° ","mp_name":null,"headimg":"http://wx.qlogo.cn/mmopen/FiaIW3HsXzg4KtH5LiaHT6OMSbekgOceyeUaWqXDQ8oXW6ewibKCib4sxJ5ialTPDZXhB9ZTsgp0MshpB08iaHyfRYeA/0","wechatimg":null,"wechatname":null,"agent":"年费会员"},{"id":122804,"user_name":"自家生產冬棗，零售，批發","mp_name":"吕延会","headimg":"http://wx.qlogo.cn/mmopen/MQJUYNPWjanbzicHfM2674BZ9OpTohOxib6IM7aG8K0tYia9Z83WdR9ECDnQbvrP0Liau3KlJ3pgNuH6yaJatJted6gicbibibQRfsk/0","wechatimg":null,"wechatname":null,"agent":"年费会员"},{"id":108836,"user_name":"中国O2O联盟-Michael 麦客 ❷","mp_name":null,"headimg":"http://wx.qlogo.cn/mmopen/giaQibFlLQKPzBV4Wag5HYAIbBK3o5reLFfHQgDcVvfsBiaA4ibIENU5Yjr9dCrZdlNKeMzForOdJiahoC1HvDUSg3lvxSRWSbecJ/0","wechatimg":null,"wechatname":null,"agent":"年费会员"}]
     * allPages : 79
     * pageCurrent : 1
     * state : 1
     * msg
     */

    private int allPages;
    private int pageCurrent;
    private String state;
    private List<LoginListBean> loginList;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getAllPages() {
        return allPages;
    }

    public void setAllPages(int allPages) {
        this.allPages = allPages;
    }

    public int getPageCurrent() {
        return pageCurrent;
    }

    public void setPageCurrent(int pageCurrent) {
        this.pageCurrent = pageCurrent;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<LoginListBean> getLoginList() {
        return loginList;
    }

    public void setLoginList(List<LoginListBean> loginList) {
        this.loginList = loginList;
    }

    public static class LoginListBean {
        /**
         * id : 100020
         * user_name : @illen
         * mp_name : 谭浩洋
         * headimg : http://www.muzhitui.cn/song/upload/2016-12-02/148061547726395.jpg
         * wechatimg : null
         * wechatname : null
         * agent : 年费会员
         */

        private int id;
        private String user_name;
        private String mp_name;
        private String headimg;
        private Object wechatimg;
        private Object wechatname;
        private String agent;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getMp_name() {
            return mp_name;
        }

        public void setMp_name(String mp_name) {
            this.mp_name = mp_name;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public Object getWechatimg() {
            return wechatimg;
        }

        public void setWechatimg(Object wechatimg) {
            this.wechatimg = wechatimg;
        }

        public Object getWechatname() {
            return wechatname;
        }

        public void setWechatname(Object wechatname) {
            this.wechatname = wechatname;
        }

        public String getAgent() {
            return agent;
        }

        public void setAgent(String agent) {
            this.agent = agent;
        }
    }
}
