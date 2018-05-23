package base.util;

import android.text.TextUtils;

import com.nevermore.muzhitui.event.AuthorizeEvent;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import base.SPUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by gk on 2016/3/15.
 */
public class LoginUtil {
    private static LoginUtil mLoginUtil;

    private LoginUtil() {
    }

    public static LoginUtil getInstance() {
        if (mLoginUtil == null) {
            synchronized (LoginUtil.class) {
                if (mLoginUtil == null) {
                    mLoginUtil = new LoginUtil();
                }
            }
        }
        return mLoginUtil;
    }

    public void loginWX(final String state) {
        Platform wx = ShareSDK.getPlatform(Wechat.NAME);
        wx.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                if (hashMap != null) {
                    SPUtils.put(SPUtils.KEY_WXHEAD, String.valueOf(hashMap.get("headimgurl")));
                    SPUtils.put(SPUtils.KEY_WXNICKNAME, String.valueOf(hashMap.get("nickname")));
                    SPUtils.put(SPUtils.KEY_WXUNIONID, String.valueOf(hashMap.get("unionid")));
                    SPUtils.put(SPUtils.KEY_WXOPENID, String.valueOf(hashMap.get("openid")));//country city privilege province language sex
                    SPUtils.put(SPUtils.KEY_COUNTRY, String.valueOf(hashMap.get("country")));
                    SPUtils.put(SPUtils.KEY_CITY, String.valueOf(hashMap.get("city")));
                    SPUtils.put(SPUtils.KEY_PRIVILEGE, String.valueOf(hashMap.get("privilege")));
                    SPUtils.put(SPUtils.KEY_PROVINCE, String.valueOf(hashMap.get("province")));
                    SPUtils.put(SPUtils.KEY_LANGUAGE, String.valueOf(hashMap.get("language")));
                    SPUtils.put(SPUtils.KEY_SEX, String.valueOf(hashMap.get("sex")));
                    Logger.e("country:"+hashMap.get("country")+"\tnickname:"+hashMap.get("nickname")+"\tcity:"+hashMap.get("city")+"\tprovince:"+hashMap.get("province")+"\tlanguage:"+hashMap.get("language"));
                    EventBus.getDefault().post(new AuthorizeEvent(state));
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Logger.i("onError");
                EventBus.getDefault().post(new AuthorizeEvent("error"));
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Logger.i("onCancel");
                EventBus.getDefault().post(new AuthorizeEvent("cancel"));
            }
        });
        wx.authorize();
        wx.showUser(null);
        //移除授权
        wx.removeAccount(true);
    }


}
