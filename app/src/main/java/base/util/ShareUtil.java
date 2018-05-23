package base.util;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.event.CloseShareEvent;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import base.LogUtil;
import base.UIUtils;
import base.network.RetrofitUtil;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by gk on 2016/3/15.
 */
public class ShareUtil implements View.OnClickListener {
    private static ShareUtil mShareUtil;
    PopupWindow mPpwMenuShare;
    private String mWebSite;
    private String mImgPath;
    private String mTitle;
    private String mText = "www.muzhitui.cn";
    private View mContentView;

    private ShareUtil() {
    }

    public PopupWindow initMenuPpwWindow() {
        //设置contentView
        mContentView = UIUtils.inflate(UIUtils.getContext(), R.layout.ppw_share);
        mPpwMenuShare = new PopupWindow(mContentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPpwMenuShare.setContentView(mContentView);
        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setColor(Color.parseColor("#80000000"));
        mPpwMenuShare.setBackgroundDrawable(colorDrawable);
        mPpwMenuShare.setOutsideTouchable(true);
        //设置各个控件的点击响应
        mContentView.findViewById(R.id.ivWx).setOnClickListener(this);
        mContentView.findViewById(R.id.ivWxP).setOnClickListener(this);
        mContentView.findViewById(R.id.ivClose).setOnClickListener(this);
        mContentView.findViewById(R.id.ivSina).setOnClickListener(this);
        mContentView.findViewById(R.id.ivQq).setOnClickListener(this);
        mContentView.findViewById(R.id.ivQqZone).setOnClickListener(this);
        mContentView.findViewById(R.id.ivA).setOnClickListener(this);
        return mPpwMenuShare;
    }


    public void setShareInfo(String webSite, String title, String imgPath, String text) {
        Logger.e("shareInfo:"+webSite + " 标题" + title + "   图片地址：" + imgPath);
        mWebSite = webSite;
        mTitle = title;
        mImgPath = imgPath;

        if (!TextUtils.isEmpty(text)) {
            mText = text;
        }else {
            mText = "www.muzhitui.cn";
        }
    }

    public static ShareUtil getInstance() {
        if (mShareUtil == null) {
            synchronized (ShareUtil.class) {
                if (mShareUtil == null) {
                    mShareUtil = new ShareUtil();
                }
            }
        }
        return mShareUtil;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivWx:
                shareWX();
                break;
            case R.id.ivSina:

                shareSinaWeibo();
                break;
            case R.id.ivQq:
                shareQQ();
                break;
            case R.id.ivQqZone:
                shareQQzone();
                break;
            case R.id.ivWxP:
                shareWXMoment();
                break;
            case R.id.ivClose:
                mPpwMenuShare.dismiss();
                break;
            case R.id.ivA:
                UIUtils.copy(mWebSite);
                Toast.makeText(UIUtils.getContext(), "复制成功", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void shareWX() {
        ShareParams wechat = new ShareParams();
        wechat.setTitle(mTitle);
        //wechat.setText("拇指推，微信推广神器");
        wechat.setImageUrl(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + mImgPath);
        wechat.setUrl(mWebSite);
        Logger.e("mWebSite = " + mWebSite+"\t"+mImgPath+"\t"+mTitle);
        wechat.setText(mText);
        wechat.setShareType(Platform.SHARE_WEBPAGE);

        Platform weixin = ShareSDK.getPlatform(Wechat.NAME);

        weixin.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                LogUtil.i("onComplete");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                LogUtil.i("onError  =  " + i + "   " + throwable.toString());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                LogUtil.i("onCancel  =  " + i);
            }
        });
        weixin.share(wechat);
    }


    public void shareWXMoment() {
        ShareParams wechat = new ShareParams();
        wechat.setTitle(mTitle);
        //wechat.setText("拇指推，微信推广神器");
        wechat.setImageUrl(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + mImgPath);
        wechat.setUrl(mWebSite);
        wechat.setText(mText);
        wechat.setShareType(Platform.SHARE_WEBPAGE);

        Platform weixin = ShareSDK.getPlatform(WechatMoments.NAME);
        weixin.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                LogUtil.i("onComplete");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                LogUtil.i("onError  =  " + i + "   " + throwable.toString());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                LogUtil.i("onCancel  =  " + i);
            }
        });
        weixin.share(wechat);
    }

    public void shareSinaWeibo() {
        ShareParams wechat = new ShareParams();
        wechat.setTitle(mTitle);
        //wechat.setText("拇指推，微信推广神器");
       // wechat.setImageUrl(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + mImgPath);
       /* wechat.setUrl(mWebSite);
        wechat.setText(mText);*/

        wechat.setText("我在拇指推编写了一篇文章，很好玩哟，网址在这里"+mWebSite);
        wechat.setShareType(Platform.SHARE_WEBPAGE);

        Platform weixin = ShareSDK.getPlatform(SinaWeibo.NAME);
        weixin.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                LogUtil.i("onComplete");
                Log.e("shareSinaWeibo","onComplete");
                EventBus.getDefault().post(new CloseShareEvent(1,"分享成功"));

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.e("shareSinaWeibo","onError"+ "   "+throwable.toString());
                LogUtil.i("onError  =  " + i + "   " + throwable.toString());
                EventBus.getDefault().post(new CloseShareEvent(2,throwable.toString()));
            }

            @Override
            public void onCancel(Platform platform, int i) {
                LogUtil.i("onCancel  =  " + i);
                EventBus.getDefault().post(new CloseShareEvent(2,"取消分享"));
            }
        });
        weixin.share(wechat);
    }

    public void shareQQzone() {
        QZone.ShareParams sp = new QZone.ShareParams();
        sp.setTitle(mTitle);
        //wechat.setText("拇指推，微信推广神器");
        sp.setImageUrl(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + mImgPath);
        //sp.setUrl(mWebSite);
        sp.setTitleUrl(mWebSite);
        sp.setText(mText);
        sp.setShareType(Platform.SHARE_WEBPAGE);

        Platform qzone = ShareSDK.getPlatform(QZone.NAME);
        qzone.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        }); // 设置分享事件回调
// 执行图文分享
        qzone.share(sp);
    }


    public void shareQQ() {
        QQ.ShareParams sp = new QQ.ShareParams();
        sp.setTitle(mTitle);
        //wechat.setText("拇指推，微信推广神器");
        sp.setImageUrl(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + mImgPath);

        sp.setTitleUrl(mWebSite);
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setText(mText);
        Platform QQ = ShareSDK.getPlatform(cn.sharesdk.tencent.qq.QQ.NAME);
        QQ.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        }); // 设置分享事件回调
// 执行图文分享
        QQ.share(sp);
    }

    public static Intent wxzf(String content, List<File> files){
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm",
                "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
        intent.putExtra("Kdescription", content);
        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        for (File f : files) {
            if(f != null)
                imageUris.add(Uri.fromFile(f));
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        return intent;
    }


}
