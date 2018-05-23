package com.nevermore.muzhitui.module.service;

import com.nevermore.muzhitui.fragment.CardKey;
import com.nevermore.muzhitui.module.bean.AppVersion;
import com.nevermore.muzhitui.module.bean.BaseBean;
import com.nevermore.muzhitui.module.bean.BitchSell;
import com.nevermore.muzhitui.module.bean.BuyProxyResult;
import com.nevermore.muzhitui.module.bean.Code;
import com.nevermore.muzhitui.module.bean.Contacts;
import com.nevermore.muzhitui.module.bean.ContactsList;
import com.nevermore.muzhitui.module.bean.DashDetail;
import com.nevermore.muzhitui.module.bean.DicussBean;
import com.nevermore.muzhitui.module.bean.DynamicBean;
import com.nevermore.muzhitui.module.bean.Count;
import com.nevermore.muzhitui.module.bean.DynamicDel;
import com.nevermore.muzhitui.module.bean.DynamicInfo;
import com.nevermore.muzhitui.module.bean.ExceInfo;
import com.nevermore.muzhitui.module.bean.FanBean;
import com.nevermore.muzhitui.module.bean.FindUserInfoById;
import com.nevermore.muzhitui.module.bean.FriendBean;
import com.nevermore.muzhitui.module.bean.GetToken;
import com.nevermore.muzhitui.module.bean.ImageUpload;
import com.nevermore.muzhitui.module.bean.IsCanEdit;
import com.nevermore.muzhitui.module.bean.LevelMember;
import com.nevermore.muzhitui.module.bean.LoginInfo;
import com.nevermore.muzhitui.module.bean.Music;
import com.nevermore.muzhitui.module.bean.MusicDataBean;
import com.nevermore.muzhitui.module.bean.My;
import com.nevermore.muzhitui.module.bean.MyAccount;
import com.nevermore.muzhitui.module.bean.MyFriends;
import com.nevermore.muzhitui.module.bean.MyInfo;
import com.nevermore.muzhitui.module.bean.MyLevel;
import com.nevermore.muzhitui.module.bean.MyLever;
import com.nevermore.muzhitui.module.bean.MyMode;
import com.nevermore.muzhitui.module.bean.MyOrder;
import com.nevermore.muzhitui.module.bean.MyProfit;
import com.nevermore.muzhitui.module.bean.MyProxy;
import com.nevermore.muzhitui.module.bean.MyQRCode;
import com.nevermore.muzhitui.module.bean.MyStock;
import com.nevermore.muzhitui.module.bean.MyStyle;
import com.nevermore.muzhitui.module.bean.MyWork;
import com.nevermore.muzhitui.module.bean.MyWorkMyInfo;
import com.nevermore.muzhitui.module.bean.NewFriend;
import com.nevermore.muzhitui.module.bean.NewFriendNums;
import com.nevermore.muzhitui.module.bean.Order;
import com.nevermore.muzhitui.module.bean.Original;
import com.nevermore.muzhitui.module.bean.PageGPSBean;
import com.nevermore.muzhitui.module.bean.PageOverRequest;
import com.nevermore.muzhitui.module.bean.PageUpdate;
import com.nevermore.muzhitui.module.bean.PraiseBean;
import com.nevermore.muzhitui.module.bean.ProxyType;
import com.nevermore.muzhitui.module.bean.PublishQunFans;
import com.nevermore.muzhitui.module.bean.Published;
import com.nevermore.muzhitui.module.bean.QunChangeBean;
import com.nevermore.muzhitui.module.bean.QunChangeInfo;
import com.nevermore.muzhitui.module.bean.QunFanOne;
import com.nevermore.muzhitui.module.bean.QunFans;
import com.nevermore.muzhitui.module.bean.QunFansAcc;
import com.nevermore.muzhitui.module.bean.QunMeBean;
import com.nevermore.muzhitui.module.bean.QunMeInfo;
import com.nevermore.muzhitui.module.bean.QunWantBean;
import com.nevermore.muzhitui.module.bean.QunWantInfo;
import com.nevermore.muzhitui.module.bean.Register;
import com.nevermore.muzhitui.module.bean.RyWxPhone;
import com.nevermore.muzhitui.module.bean.SavePagerResult;
import com.nevermore.muzhitui.module.bean.SecondPage;
import com.nevermore.muzhitui.module.bean.SystemMsg;
import com.nevermore.muzhitui.module.bean.TopMaterial;
import com.nevermore.muzhitui.module.bean.UpgradeProxy;
import com.nevermore.muzhitui.module.bean.VedioText;
import com.nevermore.muzhitui.module.bean.Video;
import com.nevermore.muzhitui.module.bean.VideoPath;
import com.nevermore.muzhitui.module.bean.WatchBean;
import com.nevermore.muzhitui.module.bean.WorkBody;
import com.nevermore.muzhitui.module.bean.Works;
import com.nevermore.muzhitui.module.bean.WxFans;
import com.nevermore.muzhitui.module.bean.WxFansCount;
import com.nevermore.muzhitui.module.bean.WxPay;
import com.nevermore.muzhitui.module.bean.url;

import org.json.JSONArray;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by hehe on 2016/4/20.
 */
public interface NetWorkService {
    /**
     * 20170426
     * simone
     * 为解决session 问题 统一将接口中添加loginId_mzt （用户登录的拇指推id）放入接口请求中
     */
    public interface Work {
        @POST("song/newPageController/pageList?state=1&pageCurrent=1")
        Observable<Works> getWorks(@Body WorkBody workBody);

        @POST("song/appPageApi/pages")
        Observable<Works> getWorks(@Query("loginId_mzt") String loginId_mzt, @Query("orderBy") int orderBy, @Query("state") int state, @Query("pageCurrent") int pageCurrent);
     /*   @POST("user/batchQueryByMobile.j")
        Observable<CallBackUserInfos> QueryUserInfoByMobiles(@Body String[] strs);*/

        @POST("song/appLoginApi/myAccount")
        Observable<MyAccount> myAccount(@Query("loginId_mzt") String loginId_mzt);

        @POST("song/appLoginApi/appLogin")
        Observable<LoginInfo> login(@Query("unionid") String unionid, @Query("openid") String openid, @Query("nickname") String nickname, @Query("headimgurl") String headimgurl, @Query("country") String country, @Query("province") String province, @Query("city") String city, @Query("sex") String sex, @Query("subscribe") String subscribe); //country city privilege province language sex

        @POST("song/appLoginApi/bill")
        Observable<BaseBean> bill(@Query("loginId_mzt") String loginId_mzt, @Query("money") float money, @Query("telphone") String telphone, @Query("weixin") String weixin);

        @POST("song/appLoginApi/myLevel")
        Observable<MyLevel> myLevel(@Query("loginId_mzt") String loginId_mzt);

        @POST("song/appLoginApi/levelMember")
        Observable<LevelMember> levelMember(@Query("loginId_mzt") String loginId_mzt, @Query("level") int level, @Query("loginId") String loginId, @Query("pageCurrent") int pageCurrent);

        @POST("song/appLoginApi/myProfit")
        Observable<MyProfit> myProfit(@Query("loginId_mzt") String loginId_mzt, @Query("pageCurrent") int pageCurrent);

        @POST("song/appLoginApi/mySet")
        Observable<My> mySet(@Query("loginId_mzt") String loginId_mzt);

        @POST("song/appLoginApi/updateWechatname")
        Observable<My> updateWechatname(@Query("loginId_mzt") String loginId_mzt, @Query("wechatname") String wechatname);

        @Multipart
        @POST("song/appLoginApi/updateHeadimg")
        Observable<ImageUpload> uploadImg(@Query("loginId_mzt") String loginId_mzt, @Part("description") RequestBody description, @Part("file") MultipartBody.Part file);

        @Multipart
        @POST("song/appLoginApi/updateHeadimg")
        Observable<ImageUpload> uploadImg(@Query("loginId_mzt") String loginId_mzt, @Part("image\"; filename=\"test.png") RequestBody file);

        @Multipart
        @POST("song/appLoginApi/updateHeadimg")
        Observable<ImageUpload> uploadImg(@Query("loginId_mzt") String loginId_mzt, @PartMap Map<String, RequestBody> params);

        @POST("song/appPageApi/toPages")
        Observable<TopMaterial> toPages(@Query("loginId_mzt") String loginId_mzt);

        //==================================我的作品================================================
        @POST("song/appLoginApi/myInfo")
        Observable<MyWorkMyInfo> myWorkMyInfo(@Query("loginId_mzt") String loginId_mzt);

        @POST("song/appPageApi/pages")
        Observable<MyWork> myWorks(@Query("loginId_mzt") String loginId_mzt, @Query("orderBy") int orderBy, @Query("state") int state, @Query("isMem") int isMem, @Query("pageCurrent") int pageCurrent,@Query("loginId")String loginId);

        @POST("song/appAdvApi/getAllAdv")
        Observable<MyMode> myMode(@Query("loginId_mzt") String loginId_mzt);

        @POST("song/appAdvApi/delAdv")
        Observable<BaseBean> deleteAdv(@Query("loginId_mzt") String loginId_mzt, @Query("id") int id, @Query("table") String table);

        @POST("song/appPageApi/delete")
        Observable<BaseBean> deleteMyWork(@Query("loginId_mzt") String loginId_mzt, @Query("id") int id);

        //===========================================================
        @POST("song/appAdvApi/saveTop")
        Observable<BaseBean> saveTop(@Query("loginId_mzt") String loginId_mzt, @Query("publicNo") String publicNo, @Query("linkUrl") String linkUrl, @Query("topDate") String topDate, @Query("img") String img);

        @POST("song/appAdvApi/saveTop")
        Observable<BaseBean> updateTop(@Query("loginId_mzt") String loginId_mzt, @Query("id") int id, @Query("publicNo") String publicNo, @Query("linkUrl") String linkUrl, @Query("topDate") String topDate, @Query("img") String img);

        @POST("song/appAdvApi/saveBot")
        Observable<BaseBean> saveBot(@Query("loginId_mzt") String loginId_mzt, @Query("title1") String title1, @Query("title2") String title2, @Query("img") String img);

        @POST("song/appAdvApi/saveBot")
        Observable<BaseBean> updateBot(@Query("loginId_mzt") String loginId_mzt, @Query("id") int id, @Query("title1") String title1, @Query("title2") String title2, @Query("img") String img);

        @POST("song/appAdvApi/saveAdv")
        Observable<BaseBean> saveAdv(@Query("loginId_mzt") String loginId_mzt, @Query("adimage") String adimage, @Query("adtext") String adtext, @Query("adurl") String adurl, @Query("adtitle") String adtitle, @Query("font") String font, @Query("adcolor") String adcolor, @Query("size") float size, @Query("animate") int animate);

        @POST("song/appAdvApi/saveAdv")
        Observable<BaseBean> uploadAdv(@Query("loginId_mzt") String loginId_mzt, @Query("id") int id, @Query("adimage") String adimage, @Query("adtext") String adtext, @Query("adurl") String adurl, @Query("adtitle") String adtitle, @Query("font") String font, @Query("adcolor") String adcolor, @Query("size") float size, @Query("animate") int animate);

        @POST("song/appAdvApi/findSty")
        Observable<MyStyle> findSty(@Query("loginId_mzt") String loginId_mzt);

        @Multipart
        @POST("song/appAdvApi/addSty")
        Observable<MyStyle.StyListBean> addSty(@Query("loginId_mzt") String loginId_mzt, @PartMap Map<String, RequestBody> params);


        @POST("song/appAdvApi/delSty")
        Observable<BaseBean> deleSty(@Query("loginId_mzt") String loginId_mzt, @Query("id") int id);

        @POST("song/appPageApi/doSave")
        Observable<SavePagerResult> doSave(@Body PageOverRequest pageOverRequest);

        @FormUrlEncoded
        @POST("song/appPageApi/doSave")
        Observable<SavePagerResult> doSave(@Query("loginId_mzt") String loginId_mzt, @Field("pagehtml") String pagehtml, @Field("title") String title, @Field("website") String website, @Field("image") String image, @Field("state") int state, @Field("id") String id, @Field("infoShow") int infoShow);

        @Multipart
        @POST("song/fileUploadApi/compressUpload")
        Observable<ImageUpload> otherImagUpload(@Query("loginId_mzt") String loginId_mzt, @PartMap Map<String, RequestBody> params);

        //========================================================================
        @POST("song/appLoginApi/myInfo")
        Observable<MyQRCode> myInfo();

        @POST("song/appOrderApi/addOrder")
        Observable<MyOrder> addOrder(@Query("loginId_mzt") String loginId_mzt, @Query("agentid") int id, @Query("card") String card);

        @POST("song/appOrderApi/toAndroidPay")
        Observable<WxPay> toAndroidPay(@Query("loginId_mzt") String loginId_mzt, @Query("orderNo") String orderNo);

        @POST("song/appApplyApi/buyApply")
        Observable<BuyProxyResult> buyApply(@Query("loginId_mzt") String loginId_mzt, @Query("typeId") int typeId, @Query("tjDistributoId") String tjDistributoId, @Query("name") String name, @Query("mobile") String mobile, @Query("province") String province, @Query("city") String city);

        @POST("song/appApplyApi/buyApply")
        Observable<BuyProxyResult> buyApply(@Query("loginId_mzt") String loginId_mzt, @Query("typeId") int typeId, @Query("name") String name, @Query("mobile") String mobile, @Query("province") String province, @Query("city") String city);


        @POST("song/testPay/toAndroidPay")
        Observable<WxPay> testtoAndroidPay(@Query("orderNo") String orderNo);


        @POST("song/appApplyApi/myApply")
        Observable<MyProxy> myApply(@Query("loginId_mzt") String loginId_mzt);

        @POST("song/appApplyApi/myCards")
        Observable<MyStock> myCards(@Query("loginId_mzt") String loginId_mzt);

        @POST("song/appApplyApi/apply")
        Observable<ProxyType> apply(@Query("loginId_mzt") String loginId_mzt);

        @POST("song/appApplyApi/upApply")
        Observable<UpgradeProxy> upApply(@Query("loginId_mzt") String loginId_mzt);

        @POST("song/appApplyApi/upBuy")
        Observable<BuyProxyResult> upBuy(@Query("loginId_mzt") String loginId_mzt, @Query("typeId") int typeId);

        @POST("song/appCardApi/makeCard")
        Observable<CardKey> makeCard(@Query("loginId_mzt") String loginId_mzt, @Query("price") float price);

        @POST("song/appCardApi/toMoveCards")
        Observable<BitchSell> toMoveCards();

        @POST("song/appCardApi/doMoveCards")
        Observable<BaseBean> doMoveCards(@Query("loginId_mzt") String loginId_mzt, @Query("takeLoginId") int takeLoginId, @Query("count") int count);

        @POST("song/appLoginApi/musicList")
        Observable<Music> musicList();

        @POST("song/appLoginApi/listBill")
        Observable<DashDetail> listBill(@Query("loginId_mzt") String loginId_mzt);


        @POST("song/appPageApi/isPower")
        Observable<IsCanEdit> isCanEdit(@Query("loginId_mzt") String loginId_mzt);

        @POST("song/appPageApi/index")
        Observable<SecondPage> pageEdit(@Query("loginId_mzt") String loginId_mzt, @Query("website") String website);

        @POST("song/appPageApi/toPageUpdate")
        Observable<PageUpdate> toPageUpdate(@Query("loginId_mzt") String loginId_mzt, @Query("id") String id);


        @POST("song/appApplyApi/toAndroidPay")
        Observable<WxPay> toAndroidPayProxy(@Query("loginId_mzt") String loginId_mzt, @Query("orderNo") String orderNo);

        @POST("song/appApplyApi/toUpAndroidPay")
        Observable<WxPay> toAndroidUploadProxy(@Query("loginId_mzt") String loginId_mzt, @Query("orderNo") String orderNo);

        /***==================================simone=================================================**/
        //APP版本信息，後台顯示最新版本
        @POST("song/appLoginApi/getAppVersion")
        Observable<AppVersion> getAppVersion(@Query("loginId_mzt") String loginId_mzt);

        //获取验证码验证码flag， 必填写   1 表示注册时验证码  2 表示重置密码时验证码
        @POST("song/appLoginApi/getPhoneCode")
        Observable<Code> getPhoneCode(@Query("flag") int flag, @Query("phone") String phone);

        //注册
        @POST("song/appLoginApi/registerPhone")
        Observable<Register> RegisterPhone(@Query("phone") String phone, @Query("password") String password, @Query("phone_code") String phone_code);

        //登陆
        @POST("song/appLoginApi/loginPhone")
        Observable<LoginInfo> LoginPhone(@Query("phone") String phone, @Query("password") String password);

        //重置
        @POST("song/appLoginApi/resetPhonePassword")
        Observable<Register> ResetPhonePassword(@Query("phone") String phone, @Query("password") String password, @Query("phone_code") String phone_code);

        //是否绑定过微信 state=2，手机未注册过； state=3，未绑定过微信； state=4，绑定过微信；
        @POST("song/appLoginApi/isPhoneBindingWenxin")
        Observable<AppVersion> IsPhoneBindingWeixin(@Query("loginId_mzt") String loginId_mzt, @Query("phone") String phone);

        @POST("song/appLoginApi/isExistWenxin")
        Observable<AppVersion> isExistWeixin(@Query("loginId_mzt") String loginId_mzt, @Query("unionid") String unionid);

        //63、查询新手教程（显示图片）
        @POST("song/appVideoApi/getVideoCourse")
        Observable<Video> getVideoCourse(@Query("loginId_mzt") String loginId_mzt, @Query("flag") int flag);

        @POST("song/appQuestionApi/getQuestionAll")
            //63、查询新手教程（显示图片）
        Observable<VedioText> getQuestionAll(@Query("loginId_mzt") String loginId_mzt, @Query("pageCurrent") int pageCurrent);

        //融云获取Token
        @POST("song/appRyApi/getToken")
        Observable<GetToken> getToken(@Query("loginId_mzt") String loginId_mzt, @Query("loginId") int loginId);

        //融云---我的下级
        @POST("song/appRyApi/myLevelMember")
        Observable<MyLever> myLevelMember(@Query("loginId_mzt") String loginId_mzt, @Query("level") int level, @Query("loginId") int loginId, @Query("pageCurrent") int pageCurrent, @Query("isPage") int isPage);

        //融云---发送好友请求
        @POST("song/appRyApi/requestFriend")
        //回应好友请求
        Observable<Code> requestFriend(@Query("loginId_mzt") String loginId_mzt, @Query("targetUserId") int targetUserId, @Query("message") String message, @Query("extra") String extra);

        @POST("song/appRyApi/responsesFriend")
        Observable<Code> responsesFriend(@Query("loginId_mzt") String loginId_mzt, @Query("status") int status, @Query("targetUserId") int targetUserId, @Query("message") String message, @Query("extra") String extra);

        //融云---新的好友
        @POST("song/appRyApi/myFrindNew")
        Observable<NewFriend> myFrindNew(@Query("loginId_mzt") String loginId_mzt, @Query("pageCurrent") int pageCurrent);

        //融云---我的好友
        @POST("song/appRyApi/myFrind")
        Observable<MyFriends> myFrind(@Query("loginId_mzt") String loginId_mzt, @Query("pageCurrent") int pageCurrent, @Query("isPage") int isPage);

        //融云---所有人脉
        @POST("song/appRyApi/allConnection")
        Observable<Contacts> allConnection(@Query("loginId_mzt") String loginId_mzt, @Query("pageCurrent") int pageCurrent, @Query("wx_city") String wx_city);

        //融云---根据用户id查询好友信息 个人信息
        @POST("song/appRyApi/getUserById")
        Observable<FindUserInfoById> getUserById(@Query("loginId_mzt") String loginId_mzt, @Query("id") String id);

        //融云---根据用户id或手机号查询好友信息
        @POST("song/appRyApi/findFriend")
        Observable<FindUserInfoById> findFriend(@Query("loginId_mzt") String loginId_mzt, @Query("phoneOrId") String phoneOrId);

        // 融云---我的好友数量显示
        @POST("song/appRyApi/myFrindCount")
        Observable<NewFriendNums> myFrindCount();

        // 融云---融云---我的好友数量清零
        @POST("song/appRyApi/myFrindCountClear")
        Observable<NewFriendNums> myFrindCountClear();

        // 修改個人信息
        @POST("song/appRyApi/updateUserInfo")
        Observable<Code> updateUserInfo(@Query("loginId_mzt") String loginId_mzt, @Query("flag") int flag,
                                        @Query("user_name") String user_name, @Query("wechat") String wechat,
                                        @Query("wx_sex") String wx_sex,
                                        @Query("user_phone") String user_phone, @Query("mp_desc") String mp_desc);

        // 修改個人信息
        @POST("song/appRyApi/updateUserInfo")
        Observable<Code> updateUserInfo(@Query("loginId_mzt") String loginId_mzt, @Query("flag") int flag, @Query("wx_country") String wx_country,
                                        @Query("wx_province") String wx_province, @Query("wx_city") String wx_city);

        @Multipart
        @POST("song/appRyApi/updateUserInfo")
        Observable<Code> updateUserInfo(@Query("loginId_mzt") String loginId_mzt, @Query("flag") int flag, @PartMap Map<String, RequestBody> params);

        //将图片以文件形式上传到服务器 后台返回url
        @Multipart
        @POST("song/fileUploadApi/compressUpload")
        Observable<url> compressUpload(@Query("loginId_mzt") String loginId_mzt, @PartMap Map<String, RequestBody> params);

        @Multipart
        @POST("song/appPageDtApi/uploadDTImg")
        Observable<url> uploadDTImg(@Query("loginId_mzt") String loginId_mzt, @PartMap Map<String, RequestBody> params);
        //http://www.muzhitui.cn/song/appPageMeApi/doUpdateIos
        //原创上传图片及信息
        @Multipart
        @POST("song/appPageMeApi/save")
        Observable<Published> saveImgActicle(@Query("loginId_mzt") String loginId_mzt, @Query("title") String title,
                                             @Query("adv_pic") String adv_pic, @Query("adv_url") String adv_url,
                                             @Query("music_url") String music_url, @Query("music_name") String music_name,
                                             @PartMap Map<String, RequestBody> title_picParams, @Query("content_text") String text
        );
        //原创上传图片及信息
        @Multipart
        @POST("song/appPageMeApi/save")
        Observable<Published> saveImgActicle2(@Query("loginId_mzt") String loginId_mzt, @Query("title") String title,
                                              @Query("adv_pic") String adv_pic, @Query("adv_url") String adv_url,
                                              @Query("music_url") String music_url, @Query("music_name") String music_name,
                                              @PartMap Map<String, RequestBody> title_picParams, @Part("content_text") JSONArray text
        );

        //修改原创上传图片及信息
        @Multipart
        @POST("song/appPageMeApi/doUpdate")
        Observable<Published> doUpdate(@Query("loginId_mzt") String loginId_mzt, @Query("id") int id, @Query("title") String title,
                                       @Query("adv_pic") String adv_pic, @Query("adv_url") String adv_url,
                                       @Query("music_url") String music_url, @Query("music_name") String music_name,
                                       @PartMap Map<String, RequestBody> title_picParams, @Query("content_text") String text
        );

        //修改原创上传图片及信息 没有图片信息的情况
        @POST("song/appPageMeApi/doUpdate")
        Observable<Published> doUpdate(@Query("loginId_mzt") String loginId_mzt, @Query("id") int id, @Query("title") String title,
                                       @Query("adv_pic") String adv_pic, @Query("adv_url") String adv_url,
                                       @Query("music_url") String music_url, @Query("music_name") String music_name, @Query("content_text") String text
        );

        //原创素材
        @POST("song/appPageMeApi/pagesMeAll")
        Observable<Works> pagesMeAll(@Query("loginId_mzt") String loginId_mzt, @Query("isMem") int id, @Query("pageCurrent") int pageCurrent,@Query("loginId")String loginId);

        //编辑页面
        @POST("song/appPageMeApi/toPageUpdateMe")
        Observable<Original> toPageUpdateMe(@Query("loginId_mzt") String loginId_mzt, @Query("id") int id);

        //删除自己的原创
        @POST("song/appPageMeApi/del")
        Observable<Code> del(@Query("loginId_mzt") String loginId_mzt, @Query("id") int id);

        //删除新的好友列表
        @POST("song/appRyApi/deleteFriendList")
        Observable<Code> deleteFriendList(@Query("loginId_mzt") String loginId_mzt, @Query("targetUserId") int targetUserId);

        //删除我的好友
        @POST("song/appRyApi/deleteFriend")
        Observable<Code> deleteFriend(@Query("loginId_mzt") String loginId_mzt, @Query("targetUserId") int targetUserId);

        //我的好友搜索
        @POST("song/appRyApi/myFrindName")
        Observable<MyFriends> myFrindName(@Query("loginId_mzt") String loginId_mzt, @Query("pageCurrent") int pageCurrent, @Query("isPage") int isPage, @Query("name") String name);

        //我的下级搜索
        @POST("song/appRyApi/myLevelMemberName")
        Observable<MyFriends> myLevelMemberName(@Query("loginId_mzt") String loginId_mzt, @Query("pageCurrent") int pageCurrent, @Query("isPage") int isPage, @Query("name") String name);

        //原创上传图片及信息编辑
        @Multipart
        @POST("song/appRyApi/reportUser")
        Observable<Code> reportUser(@Query("loginId_mzt") String loginId_mzt, @PartMap Map<String, RequestBody> content, @Query("target_id") int target_id, @Query("report_id") int report_id);
        //融云用户投诉

        @POST("song/appRyApi/reportUser")
        Observable<Code> reportUser(@Query("loginId_mzt") String loginId_mzt, @Query("target_id") int target_id, @Query("report_id") int report_id);

        //获取音乐信息
        @POST("song/appCYMusicApi/getYCMusics")
        Observable<MusicDataBean> getYCMusics();

        //手机号授权微信登录后 调用的登录接口
        @POST("song/appLoginApi/appLoginAfterPhone")
        Observable<LoginInfo> appLoginAfterPhone(@Query("unionid") String unionid, @Query("openid") String openid, @Query("nickname") String nickname,
                                                 @Query("headimgurl") String headimgurl, @Query("country") String country, @Query("province") String province,
                                                 @Query("city") String city, @Query("sex") String sex, @Query("subscribe") String subscribe,
                                                 @Query("phone") String phone, @Query("password") String password); //country city privilege province language sex

        //得到融云通讯录中的信息
        @POST("song/appRyApi/getRyPhoneList")
        Observable<ContactsList> getRyPhoneList(@Query("loginId_mzt" ) String loginId_mzt, @Query("ry_phones") String ry_phones);

        //得到融云通讯录中的信息点击邀请发送短信请求至服务器
        @POST("song/appRyApi/sendRyInvitation")
        Observable<Code> sendRyInvitation(@Query("loginId_mzt") String loginId_mzt, @Query("id") int id);

        //判断会员是否有使用原创的权限，及判断非会员使用次数权限
        //  state=2，可使用原创
       // state=2，非会员没有试用次数了，不可原创；
        //state=3，会员到期不可原创
        @POST("song/appPageMeApi/isPowerMe")
        Observable<Code> isPowerMe(@Query("loginId_mzt") String loginId_mzt);
       // 首页精品原创
       @POST("song/appPageMeApi/pagesMeAllGood")
       Observable<Works> pagesMeAllGood(@Query("loginId_mzt") String loginId_mzt, @Query("pageCurrent") int pageCurrent);

        // 首页精品原创
        @POST("song/appLoginApi/isPublic")
        Observable<Code> isPublic(@Query("loginId_mzt") String loginId_mzt);

        //微信加粉的列表获取
        @POST("song/appRyApi/getRyWxPhoneList")
        Observable<RyWxPhone> getRyWxPhoneList(@Query("loginId_mzt") String loginId_mzt,
                                               @Query("pageCurrent") int pageCurrent,
                                               @Query("pageSize") int pageSize,
                                               @Query("province") String province,
                                               @Query("city") String city);

        //获取当天已加粉的数量
        @POST("song/appRyApi/getWxFansCount")
        Observable<WxFansCount> getWxFansCount(@Query("loginId_mzt") String loginId_mzt);

        //上传已加粉数量
        @POST("song/appRyApi/addWxFans")
        Observable<WxFans> addWxFans(@Query("loginId_mzt") String loginId_mzt, @Query("num") int num);

        //上报异常信息
        @POST("song/appBugMessageApi/collectBugMessage")
        Observable<ExceInfo> collectBugMessage(@Query("loginId_mzt") String loginId_mzt,
                                               @Query("app_version") String versionName,
                                               @Query("source") int source,
                                               @Query("name") String name,
                                               @Query("phone_system") String phoneSystem,
                                               @Query("bug_msg") String bugMsg);

        //获取系统消息
        @POST("song/appSysNoticeApi/getSysNotice")
        Observable<SystemMsg> getSystemMsg(@Query("loginId_mzt") String loginId_mzt);

        //删除系统消息
        @POST("song/appSysNoticeApi/delSysNotice")
        Observable<Code> delSysNotice(@Query("loginId_mzt") String loginId_mzt, @Query("id") int id);

        //获取动态列表
        @POST("song/appPageDtApi/listDtAll")
        Observable<DynamicBean> listDtAll(@Query("loginId_mzt") String loginId_mzt, @Query("pageCurrent") int pageCurrent);

        //按时间获取动态列表
        @POST("song/appPageDtApi/listDtAll")
        Observable<DynamicBean> listDtAll(@Query("loginId_mzt") String loginId_mzt,@Query("begin_time")String begin_time,@Query("end_time")String end_time, @Query("user_id") String user_id);

        //获取某人的动态列表（包括自己，用user_id作为唯一区分）
        @POST("song/appPageDtApi/listDtAll")
        Observable<DynamicBean> listDtAll(@Query("loginId_mzt") String loginId_mzt, @Query("pageCurrent") int pageCurrent, @Query("user_id") String user_id);

        //动态点赞或取消点赞   type = 0 :取消点赞  type = 1 : 确认点赞
        @POST("song/appPageDtApi/praise")
        Observable<ExceInfo> praise(@Query("loginId_mzt") String loginId_mzt, @Query("pagedt_id") int pagedt_id, @Query("type") int type,@Query("dt_loginid") int dt_loginid);

        //获取点赞用户列表
        @POST("song/appPageDtApi/listPraise")
        Observable<PraiseBean> listPraise(@Query("loginId_mzt") String loginId_mzt, @Query("pagedt_id") int pagedt_id, @Query("pageCurrent") int pageCurrent);

        //获取某人的关注列表(自己的)
        @POST("song/appPageDtApi/listWatchs")
        Observable<WatchBean> listWatchs(@Query("loginId_mzt") String loginId_mzt,@Query("loginId") String loginId, @Query("pageCurrent") int pageCurrent);
        //获取他人的关注列表
        @POST("song/appPageDtApi/listWatchsTa ")
        Observable<WatchBean> listWatchsTa(@Query("loginId_mzt") String loginId_mzt,@Query("loginId") String loginId, @Query("pageCurrent") int pageCurrent);

        //获取某人的粉丝列表
        @POST("song/appPageDtApi/listFans")
        Observable<FanBean> listFans(@Query("loginId_mzt") String loginId_mzt,@Query("loginId") String loginId, @Query("pageCurrent") int pageCurrent);

        /**
         * 点击关注中的数字，清零
         * @param loginId_mzt
         * @param loginid_target 清除关注的对象
         * @return
         */
        @POST("song/appPageDtApi/clearCount")
        Observable<com.nevermore.muzhitui.module.BaseBean> clearCount(@Query("loginId_mzt") String loginId_mzt, @Query("loginid_target") String loginid_target);

        /**
         * 回复某人在动态上的评论
         * @param loginId_mzt
         * @param pagedt_id  动态ID
         * @param reply  回复内容
         * @param type  回复类型  1： 评论  2：回复
         * @param reply_loginid_parent  回复的对象
         * @return
         */
        @POST("song/appPageDtApi/reply")
        Observable<com.nevermore.muzhitui.module.BaseBean> reply(@Query("loginId_mzt") String loginId_mzt, @Query("pagedt_id") int pagedt_id, @Query("reply_content") String reply, @Query("type") int type, @Query("reply_loginid_parent") int reply_loginid_parent);

        /**
         * 动态回复列表清空
         * @param loginId_mzt
         * @return
         */
        @POST("song/appPageDtApi/clearOneDtMesg")
        Observable<com.nevermore.muzhitui.module.BaseBean> clearOneDtMsg(@Query("loginId_mzt") String loginId_mzt);

        /**
         * 获取动态的回复列表
         * @param loginId_mzt
         * @param pageCurrent
         * @return
         */
        @POST("song/appPageDtApi/listOneDtMesg")
        Observable<DicussBean> listOneDtMsg(@Query("loginId_mzt") String loginId_mzt, @Query("pageCurrent") int pageCurrent);

        /**
         * 获取个人动态评论消息的未读数量
         * @param loginId_mzt
         * @return
         */
        @POST("song/appPageDtApi/getCountOneDtMesg")
        Observable<Count> getCountOneDtMesg(@Query("loginId_mzt") String loginId_mzt);

        /**
         * 动态删除
         * @param loginId_mzt
         * @param pagedt_id 动态ID
         * @return
         */
        @POST("song/appPageDtApi/delPageDt")
        Observable<com.nevermore.muzhitui.module.BaseBean> delPageDt(@Query("loginId_mzt") String loginId_mzt, @Query("pagedt_id") int pagedt_id);

        /**
         * 删除某条动态的某条评论
         * @param loginId_mzt
         * @param pagedt_id 动态ID
         * @param reply_id  评论ID
         * @return
         */
        @POST("song/appPageDtApi/delReply")
        Observable<com.nevermore.muzhitui.module.BaseBean> delReply(@Query("loginId_mzt") String loginId_mzt, @Query("pagedt_id") int pagedt_id, @Query("reply_id") int reply_id);

        /**
         * 查询动态的详细内容
         * @param loginId_mzt
         * @param pagedt_id
         * @return
         */
        @POST("song/appPageDtApi/getOneDt")
        Observable<DynamicInfo> getOneDt(@Query("loginId_mzt") String loginId_mzt, @Query("pagedt_id") int pagedt_id);

        /**
         * 关注或取消关注
         * @param loginId_mzt
         * @param loginid_target 关注的对象或取消关注的对象
         * @param status 1 关注 0取消关注
         * @return
         */
        @POST("song/appPageDtApi/fans")
        Observable<com.nevermore.muzhitui.module.BaseBean> fans(@Query("loginId_mzt") String loginId_mzt, @Query("loginid_target") String loginid_target, @Query("status") int status);

        /**
         * 我有群删除
         * @param qun_id 群ID
         * @return
         */
        @POST("song/appWxQunApi/deleteQunMe")
        Observable<com.nevermore.muzhitui.module.BaseBean> deleteQunMe(@Query("loginId_mzt") String loginId_mzt, @Query("qun_id") int qun_id);

        /**
         * 搜索我有群信息
         * @param loginId_mzt
         * @param qun_name  搜索内容
         * @param offer_sort 价格顺序  0升序 1降序
         * @param pageCurrent
         * @return
         */
        @POST("song/appWxQunApi/listQunMe")
        Observable<QunMeBean> listQunMe(@Query("loginId_mzt") String loginId_mzt, @Query("wx_qun_name") String qun_name, @Query("offer_sort") Integer offer_sort, @Query("pageCurrent") int pageCurrent);

        /**
         * 查询我发布的我有群信息列表
         * @param loginId_mzt
         * @param pageCurrent
         * @return
         */
        @POST("song/appWxQunApi/listQunMeMyself")
        Observable<QunMeBean> listQunMeMyself(@Query("loginId_mzt") String loginId_mzt, @Query("pageCurrent") int pageCurrent);

        /**
         * 我有群的详细信息查询
         * @param loginId_mzt
         * @param qun_id
         * @return
         */
        @POST("song/appWxQunApi/getQunMeOne")
        Observable<QunMeInfo> getQunMeOne(@Query("loginId_mzt") String loginId_mzt, @Query("qun_id") int qun_id);

        /**
         * 我有群增加一个阅读量
         * @param loginId_mzt
         * @param qun_id
         * @return
         */
        @POST("song/appWxQunApi/addQunMeRead")
        Observable<com.nevermore.muzhitui.module.BaseBean> addQunMeRead(@Query("loginId_mzt") String loginId_mzt, @Query("qun_id") int qun_id);

        /**
         * 发布一个我要群信息
         * @param loginId_mzt
         * @param wx_qun_name 群名称
         * @param wx_country 国家
         * @param wx_province 省份
         * @param wx_city 城市
         * @param offer 悬赏金额
         * @param phone 电话号码
         * @param wx_no 微信号
         * @param wx_qun_num 微信群内人数
         * @param is_owner 是否是群主  1是  0不是
         * @param remark 备注
         * @return
         */
        @POST("song/appWxQunApi/saveQunWant")
        Observable<com.nevermore.muzhitui.module.BaseBean> saveQunWant(@Query("loginId_mzt") String loginId_mzt, @Query("wx_qun_name") String wx_qun_name, @Query("wx_country") String wx_country, @Query("wx_province") String wx_province, @Query("wx_city") String wx_city, @Query("offer") int offer, @Query("phone") String phone, @Query("wx_no") String wx_no, @Query("wx_qun_num") int wx_qun_num, @Query("is_owner") int is_owner, @Query("remark") String remark);

        /**
         * 修改一个我要群信息
         * @param loginId_mzt
         * @param wx_qun_name
         * @param wx_country
         * @param wx_province
         * @param wx_city
         * @param offer
         * @param phone
         * @param wx_no
         * @param wx_qun_num
         * @param is_owner
         * @param remark
         * @param qun_id
         * @return
         */
        @POST("song/appWxQunApi/updateQunWant")
        Observable<com.nevermore.muzhitui.module.BaseBean> updateQunWant(@Query("loginId_mzt") String loginId_mzt, @Query("wx_qun_name") String wx_qun_name, @Query("wx_country") String wx_country, @Query("wx_province") String wx_province, @Query("wx_city") String wx_city, @Query("offer") int offer, @Query("phone") String phone, @Query("wx_no") String wx_no, @Query("wx_qun_num") int wx_qun_num, @Query("is_owner") int is_owner, @Query("remark") String remark, @Query("qun_id") int qun_id);

        /**
         * 删除一个我要群
         * @param loginId_mzt
         * @param qun_id
         * @return
         */
        @POST("song/appWxQunApi/deleteQunWant")
        Observable<com.nevermore.muzhitui.module.BaseBean> deleteQunWant(@Query("loginId_mzt") String loginId_mzt, @Query("qun_id") int qun_id);

        /**
         * 我要群搜索
         * @param loginId_mzt
         * @param qun_name 搜索关键字
         * @param offer_sort 价格顺序  0升序 1降序
         * @param pageCurrent
         * @return
         */
        @POST("song/appWxQunApi/listQunWant")
        Observable<QunWantBean> listQunWant(@Query("loginId_mzt") String loginId_mzt, @Query("wx_qun_name") String qun_name, @Query("offer_sort") Integer offer_sort, @Query("pageCurrent") int pageCurrent);

        /**
         * 查询我发布的我要群信息列表
         * @param loginId_mzt
         * @param pageCurrent
         * @return
         */
        @POST("song/appWxQunApi/listQunWantMyself")
        Observable<QunWantBean> listQunWantMyself(@Query("loginId_mzt") String loginId_mzt, @Query("pageCurrent") int pageCurrent);

        /**
         * 查询我要群的详细信息
         * @param loginId_mzt
         * @param qun_id 群ID
         * @return
         */
        @POST("song/appWxQunApi/getQunWantOne")
        Observable<QunWantInfo> getQunWantOne(@Query("loginId_mzt") String loginId_mzt, @Query("qun_id") int qun_id);

        /**
         * 增加一个我要群的阅读量
         * @param loginId_mzt
         * @param qun_id
         * @return
         */
        @POST("song/appWxQunApi/addQunWantRead")
        Observable<com.nevermore.muzhitui.module.BaseBean> addQunWantRead(@Query("loginId_mzt") String loginId_mzt, @Query("qun_id") int qun_id);

        /**
         * 群互换删除
         * @param loginId_mzt
         * @param qun_id
         * @return
         */
        @POST("song/appWxQunApi/deleteQunChange")
        Observable<com.nevermore.muzhitui.module.BaseBean> deleteQunChange(@Query("loginId_mzt") String loginId_mzt, @Query("qun_id") int qun_id);

        /**
         * 群互换列表查询
         * @param loginId_mzt
         * @param pageCurrent
         * @return
         */
        @POST("song/appWxQunApi/listQunChange")
        Observable<QunChangeBean> listQunChange(@Query("loginId_mzt") String loginId_mzt, @Query("pageCurrent") int pageCurrent);

        /**
         * 我发布的群互换列表
         * @param loginId_mzt
         * @param pageCurrent
         * @return
         */
        @POST("song/appWxQunApi/listQunChangeMyself")
        Observable<QunChangeBean> listQunChangeMyself(@Query("loginId_mzt") String loginId_mzt, @Query("pageCurrent") int pageCurrent);

        /**
         * 群互换详细信息查询
         * @param loginId_mzt
         * @param qun_id
         * @return
         */
        @POST("song/appWxQunApi/getQunChangeOne")
        Observable<QunChangeInfo> getQunChangeOne(@Query("loginId_mzt") String loginId_mzt, @Query("qun_id") int qun_id);

        /**
         * 群互换增加一个阅读数
         * @param loginId_mzt
         * @param qun_id
         * @return
         */
        @POST("song/appWxQunApi/addQunChangeRead")
        Observable<com.nevermore.muzhitui.module.BaseBean> addQunChangeRead(@Query("loginId_mzt") String loginId_mzt, @Query("qun_id") int qun_id);

        /**
         * 个人动态个人信息查询
         * @param loginId_mzt
         * @param loginId
         * @return
         */
        @POST("song/appPageDtApi/getMyInfo")
        Observable<MyInfo> getMyInfo(@Query("loginId_mzt") String loginId_mzt, @Query("loginId") String loginId);

        /**
         * 清空关注数量 （关注对象新增动态数量）的数字，清零
         * @param loginId_mzt
         * @return
         */
        @POST("song/appPageDtApi/clearCountAtt")
        Observable<com.nevermore.muzhitui.module.BaseBean> clearAttCount(@Query("loginId_mzt") String loginId_mzt);

        /**
         * 清空粉丝未读数量
         * @param loginId_mzt
         * @return
         */
        @POST("song/appPageDtApi/clearCountFan")
        Observable<com.nevermore.muzhitui.module.BaseBean> clearCountFan(@Query("loginId_mzt") String loginId_mzt);

        /**
         * 清空动态未读消息数量
         * @param loginId_mzt
         * @return
         */
        @POST("song/appPageDtApi/clearCountDt")
        Observable<com.nevermore.muzhitui.module.BaseBean> clearDtCount(@Query("loginId_mzt") String loginId_mzt);

        /**
         * 屏蔽某人的动态消息提醒
         * @param loginId_mzt
         * @param type   1屏蔽  0不屏蔽
         * @param loginid_target
         * @return
         */
        @POST("song/appPageDtApi/shield")
        Observable<com.nevermore.muzhitui.module.BaseBean> shield(@Query("loginId_mzt") String loginId_mzt,@Query("type")int type,@Query("loginid_target")String loginid_target);

        @POST("song/appLoginApi/getOrderList")
        Observable<Order> getOrderList(@Query("loginId_mzt") String loginId_mzt);

        @POST("song/appLoginApi/clearCountProfit")
        Observable<Order> clearCountProfit(@Query("loginId_mzt") String loginId_mzt);

        @POST("song/appPublishApi/getPublish")
        Observable<VideoPath> getPopVedio();

        @POST("song/appPageDtApi/isDelPageDt")
        Observable<DynamicDel> isDelPageDt(@Query("loginId_mzt") String loginId_mzt,@Query("pagedt_id")int pagedtId);

        @POST("song/appOprationGPSApi/getOprationGPSByNumber")
        Observable<PageGPSBean> getOprationGPSByNumber(@Query("loginId_mzt") String loginId_mzt, @Query("show_number")int showNum);

        @GET
        Observable<ResponseBody> download(@Url String url);

        @Streaming
        @GET
        Observable<ResponseBody> downloadBigFile(@Url String url);

        /**
         * 互粉删除
         * @param loginId_mzt
         * @param id
         * @return
         */
        @POST("song/appWxQunFansApi/deleteQunFans")
        Observable<com.nevermore.muzhitui.module.BaseBean> deleteQunFans(@Query("loginId_mzt") String loginId_mzt,@Query("id")String id);

        /**
         * 互粉查询
         * @param loginId_mzt
         * @param pageCurrent
         * @return
         */
        @POST("song/appWxQunFansApi/listQunFans")
        Observable<QunFans> listQunFans(@Query("loginId_mzt") String loginId_mzt,@Query("industry_type")String industry_type,@Query("province") String provice,@Query("city")String city,@Query("sex")Integer sex, @Query("pageCurrent") int pageCurrent);

        /**
         * 互粉单条查询
         * @param loginId_mzt
         * @return
         */
        @POST("song/appWxQunFansApi/getQunFansOne")
        Observable<QunFanOne> getQunFansOne(@Query("loginId_mzt") String loginId_mzt, @Query("id") int id);

        /**
         * 互粉是否发布过名片
         * @param loginId_mzt
         * @return
         */
        @POST("song/appWxQunFansApi/isPublishQunFans")
        Observable<PublishQunFans> isPublishQunFans(@Query("loginId_mzt") String loginId_mzt);

        /**
         * 互粉置顶
         * @param loginId_mzt
         * @return
         */
        @POST("song/appWxQunFansApi/topQunFans")
        Observable<com.nevermore.muzhitui.module.BaseBean> topQunFans(@Query("loginId_mzt") String loginId_mzt, @Query("id") int id);

        /**
         * 精准加粉列表
         * @param login_mzt
         * @param sex
         * @param in_type
         * @param privder
         * @param city
         * @param num
         * @return
         */
        @POST("song/appWxQunFansApi/listQunFansAcc")
        Observable<FriendBean> listQunFansAcc(@Query("loginId_mzt") String login_mzt, @Query("sex") Integer sex, @Query("industry_type") String in_type, @Query("province") String privder, @Query("city") String city, @Query("pageSize") Integer num);

        /**
         * 是否显示精准加粉和批量加粉
         * @return
         */
        @POST("song/appWxQunFansApi/isQunFansAcc")
        Observable<QunFansAcc> isQunFansAcc();

        /**
         * 功能反馈
         * @param login_mzt
         * @param content
         * @return
         */
        @POST("song/appFeedbackApi/collectFeedback")
        Observable<com.nevermore.muzhitui.module.BaseBean> collectFeedback(@Query("loginId_mzt") String login_mzt,@Query("content")String content);
    }

}
