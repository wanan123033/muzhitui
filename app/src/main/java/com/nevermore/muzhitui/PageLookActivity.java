package com.nevermore.muzhitui;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nevermore.muzhitui.activity.MyDownVideoActivity;
import com.nevermore.muzhitui.activity.MyWorksActivity;
import com.nevermore.muzhitui.activity.SnapshotActivity;
import com.nevermore.muzhitui.activity.editPhoto.OriginalArticleEditActivity;
import com.nevermore.muzhitui.activity.rongyun.MztRongContext;
import com.nevermore.muzhitui.event.CloseShareEvent;
import com.nevermore.muzhitui.event.ReadCountEvent;
import com.nevermore.muzhitui.fragment.TabMyFragment;
import com.nevermore.muzhitui.module.bean.VideoBean;
import com.orhanobut.logger.Logger;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.MyLogger;
import base.SPUtils;
import base.UIUtils;
import base.network.RetrofitUtil;
import base.util.CacheUtil;
import base.util.ShareUtil;
import base.view.LoadingAlertDialog;
import base.view.WebViewJavaScriptFunction;
import base.view.X5WebView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 文章详情界面
 */
public class PageLookActivity extends BaseActivityTwoV {
    public static final String KEY_URL = "PAGERURL";
    public static final String KEY_IMG = "IMG";
    public static final String KEY_TEXT = "TEXT";
    public static final String KEY_IS_SHARE = "IS_SHARE";  //1 能分享链接  0 不能分享链接
    public static final String KEY_IS_SHARE_LINK = "IS_SHARE_LINK";  //不能分享时需复制链接
    public static final String KEY_WORK = "WORK";  //不能分享时需复制链接
    public static final String KEY_TITLE = "KEY_TITLE";  //文章标题
    public static final String IS_SHOWEDIT = "isShowEdit";  //文章标题
    public static final String IS_SNAP = "isSnap";  //是否可以截图  默认不能截图
    public static final String IS_DOWN_VIDEO = "isDownVideo";   //是否显示下载视频按钮 默认不显示
    @BindView(R.id.mywv)
    X5WebView mMyMw;
    private PopupWindow mPpwMenuShare;
    private String mPageUrl;
    public String mTitle;
    public String mText;
    public String mImgPath;
    @BindView(R.id.ivEdit)
    ImageView mIvEdit;
    @BindView(R.id.progressBar1)
    ProgressBar progressBar1;
    @BindView(R.id.tv_net_snap)
    TextView tv_net_snap;
    @BindView(R.id.tv_down_video)
    TextView tv_down_video;

    private String mPageId = "";
    public static final String KEY_ID = "ID";
    public ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> mUploadMessageForAndroid5;
    public final static int FILECHOOSER_RESULTCODE = 1;
    public final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;
    private android.app.AlertDialog alertDialog;
    private boolean isOriginal = false;
    boolean isShowEdit = false;
    private LoadingAlertDialog mLoadingAlerDialog;
    String isShareLink = "";
    boolean isWork = false;
    boolean isDownVideo = false;

    private String title;
    private boolean isSnap;
    private AlertDialog alertDialog3;


    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().post(new ReadCountEvent());
        mMyMw.resumeTimers();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void init() {
        mLoadingAlerDialog = new LoadingAlertDialog(this);
//        mLoadingAlerDialog.show();
        setMyTitle("文章详情");
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        mMyMw.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        mPageId = getIntent().getStringExtra(KEY_ID);
        mText = getIntent().getStringExtra(KEY_TEXT);
        title = getIntent().getStringExtra(KEY_TITLE);
        mTitle = title;
        isOriginal = getIntent().getBooleanExtra("isOriginal", false);//是否是原创
        isShowEdit = getIntent().getBooleanExtra(IS_SHOWEDIT, false);//是否显示编辑的图标
        isWork = getIntent().getBooleanExtra(PageLookActivity.KEY_WORK, false);//判断是否能分享

        isSnap = getIntent().getBooleanExtra(IS_SNAP,false);
        if(isSnap){
            tv_net_snap.setVisibility(View.VISIBLE);
        }else {
            tv_net_snap.setVisibility(View.GONE);
        }

        isDownVideo = getIntent().getBooleanExtra(IS_DOWN_VIDEO,false);
        mPageUrl = getIntent().getStringExtra(KEY_URL);

        Log.e("isShowEdit:", isShowEdit + "");
        if (mPageId != null) {
            if (isShowEdit) {
                mIvEdit.setVisibility(View.GONE);
            } else {
                mIvEdit.setVisibility(View.VISIBLE);
            }

            mIvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isOriginal) {

                        Intent intentOriginal = new Intent(PageLookActivity.this, OriginalArticleEditActivity.class);
                        intentOriginal.putExtra(PagerEditActivity.KEY_ID, mPageId);
                        startActivity(intentOriginal);

                    } else {
                        Intent intent1 = new Intent(PageLookActivity.this, PagerEditActivity.class);
                        intent1.putExtra(PagerEditActivity.KEY_ID, mPageId);
                        Log.i("TAG---","PageLookActivity title="+title);
                        intent1.putExtra(PagerEditActivity.KEY_TITLE,title);
                        startActivity(intent1);
                    }
                    finish();
                }
            });
        }

        showBack(true);

        mImgPath = getIntent().getStringExtra(KEY_IMG);

        final int memberstate = (int) SPUtils.get(SPUtils.KEY_MEMBER_STATE, 0);//会员状态  1是年费2是终身3不是会员
        final int IsExpire = (int) SPUtils.get(SPUtils.KEY_ISEXPIRE, 0);//是否已过期  1是会员已过期 0是正常
        final boolean isShare = getIntent().getBooleanExtra(KEY_IS_SHARE, true);// 视频页面跳转 1 能分享链接  0 不能分享链接 默认是所有页面可分享，视频页面需要特殊处理

        isShareLink = getIntent().getStringExtra(KEY_IS_SHARE_LINK);//不能分享时需复制链接

        Log.e("isShare:", isShare + "");
        if (!TextUtils.isEmpty(mPageUrl)) {
            showRight(R.drawable.ic_myqr_menue, new View.OnClickListener() {           //右上角三个点的按钮点击
                @Override
                public void onClick(View v) {
                    mPpwMenuShare.showAtLocation(mMyMw, Gravity.BOTTOM, 0, 0);
                    //加载的webview是否可分享
//                    if (isShare == false) {
//                        alertDialog = UIUtils.getAlertDialog(PageLookActivity.this, "提示信息", "对不起，该视频暂不支持分享，如需分享请点击确定复制连接！", "取消", "确定复制", 0, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                alertDialog.dismiss();
//                                mPpwMenuShare.dismiss();
//                                finish();
//
//                            }
//                        }, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                alertDialog.dismiss();
//                                mPpwMenuShare.dismiss();
//                                UIUtils.copy(isShareLink);
//                                baseStartActivity(MainActivity.class);
//
//                                MztRongContext.getInstance().popAllActivity(0);
//                            }
//                        });
//                        alertDialog.show();
//                    } else {
//
//                        //判断会员是否可分享
//                        String message = "您目前还不是会员，无法分享文章；请购买会员后即可分享";
//                        String clickmessage = "购买会员";
//                        if (IsExpire == 1) {
//                            message = "您的会员已到期，无法分享文章；请续费后即可恢复正常使用";
//                            clickmessage = "续费";
//                        }
//
//                        if ((memberstate == 3) || (IsExpire == 1)) {
//                            alertDialog = UIUtils.getAlertDialog(PageLookActivity.this, "提示信息", message, "取消", clickmessage, 0, new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    alertDialog.dismiss();
//                                    mPpwMenuShare.dismiss();
//                                    finish();
//
//                                }
//                            }, new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    alertDialog.dismiss();
//                                    mPpwMenuShare.dismiss();
//                                    //finish();
//                                    baseStartActivity(MainActivity.class);
//                                    TabMyFragment.mIsBuy = true;
//                                    MztRongContext.getInstance().popAllActivity(3);
//                                }
//                            });
//                            alertDialog.show();
//                        }
//                    }


                }
            });
            mMyMw.getSettings().setPluginState(WebSettings.PluginState.ON);
            mMyMw.getSettings().setJavaScriptEnabled(true);
            mMyMw.getSettings().setAppCacheEnabled(false);
            mMyMw.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            mMyMw.getSettings().setUseWideViewPort(true);
            mMyMw.getSettings().setLoadWithOverviewMode(true);
            mMyMw.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

            if(!isOriginal && !mPageUrl.contains("&isHomePage=1")){
                mPageUrl += "&isHomePage=1";
            }
            MyLogger.kLog().e("url=" + mPageUrl);
            mMyMw.loadUrl(mPageUrl);//+"&zanshang=0"

//            mMyMw.loadUrl("https://v.qq.com/iframe/player.html?vid=i0507nkrhkj&tiny=0&auto=0");
            mMyMw.setWebViewClient(new WebViewClient() {
                // autoplay when finished loading via javascript injection
                public void onPageFinished(WebView view, String url) {
                  //  mMyMw.getSettings().setLoadsImagesAutomatically(true);
//                    mMyMw.loadUrl("javascript:(function() { var videos = document.getElementsByTagName('audio'); for(var i=0;i<videos.length;i++){videos[i].play();}})()");
//                    mMyMw.loadUrl("javascript:(function() { var videos = document.getElementsByTagName('audio'); for(var i=0;i<videos.length;i++){videos[i].pause();}})()");
                    if(url.contains("douyin.com")){
//                        mMyMw.loadUrl("javascript:alert('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                    }
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    super.shouldOverrideUrlLoading(view, url);
                    Log.e("click url:", url);
                    if (url.startsWith("tel:")) {
                        Log.e("tel url:", url);
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(url));
                        startActivity(intent);

                    } else if ((url.startsWith("http:") || url.startsWith("https:")) && !isSnap ) {
                        //加载网页点击的时候 ，判断为原创进来的时候，就不显示红笔
                        if (isOriginal) {
                            mIvEdit.setVisibility(View.GONE);
                        } else {
                            mIvEdit.setVisibility(View.VISIBLE);
                        }
                        if (url.contains(RetrofitUtil.PAGE_LOOK_OWN) || url.contains(RetrofitUtil.PAGE_LOOK)) {//判断是不是拇指推文章加载的 ，如果是拇指推文章就判断是否是原创，是否需要添加isHomePage字段，判断是否需要显示底板
                            if (url.contains("&curLoginId=")) {
                            } else {
                                url = url + "&curLoginId=" + MainActivity.loginId;
                            }
                        }
                        view.loadUrl(url);
                        //   view.loadUrl(url); //将原来的在本页面加载网页的改成再承载一个activity 加载页面

//                        Intent intent = new Intent(PageLookActivity.this, PageLookActivity.class);
//                        if (url.contains(RetrofitUtil.PAGE_LOOK_OWN) || url.contains(RetrofitUtil.PAGE_LOOK)) {//判断是不是拇指推文章加载的 ，如果是拇指推文章就判断是否是原创，是否需要添加isHomePage字段，判断是否需要显示底板
//                            if (url.contains("&curLoginId=")) {
//                                intent.putExtra(PageLookActivity.KEY_URL, url);
//                            } else {
//                                intent.putExtra(PageLookActivity.KEY_URL, url + "&curLoginId=" + MainActivity.loginId);
//                            }
//
//
//                        } else {
//                            intent.putExtra(PageLookActivity.KEY_URL, url);
//
//                        }
//                        intent.putExtra(PageLookActivity.KEY_IMG, mImgPath);//首页面的图片路径
//                        intent.putExtra(PageLookActivity.KEY_ID, mPageId);//文字id
//                        intent.putExtra("isOriginal", isOriginal);//是否是原创，因为不需要在首页显示红笔，所以判断是否是原创也都不需要了，可以通过原创判断分享出去的是否显示底板
//                        intent.putExtra("isShowEdit", isShowEdit);//不显示编辑红笔提示
//                        intent.putExtra(IS_DOWN_VIDEO,isDownVideo);
//                        intent.putExtra(IS_SNAP,isSnap);
//                        startActivity(intent);

                    }
                    Log.e("====url:", url);
                    return true;

                }
            });
            mMyMw.setWebChromeClient(
                    new WebChromeClient() {
                        @Override
                        public void onProgressChanged(WebView webView, int i) {
                            super.onProgressChanged(webView, i);
                            progressBar1.setProgress(i);
                            if(i >= 100){
                                progressBar1.setVisibility(View.GONE);
                            }else {
                                progressBar1.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onReceivedTitle(WebView view, String title) {
                            super.onReceivedTitle(view, title);
                            MyLogger.kLog().e(title);
                            if (mPageUrl.startsWith(RetrofitUtil.API_URL)) {
                                mPageUrl = mPageUrl.replace(RetrofitUtil.API_URL, RetrofitUtil.API_URL_RANDOM);
                            }
                            //判断是否是原创，在pageurl添加isHomePage 分享出去不显示底板
                            if (isOriginal & !mPageUrl.contains("&isHomePage=1")) {
                                mPageUrl = mPageUrl + "&isHomePage=1";

                            }
                            ShareUtil.getInstance().setShareInfo(mPageUrl.replace("&share=0","&share=1"), mTitle, TextUtils.isEmpty(mImgPath) ? PagerEditActivity.defaultPath : mImgPath, mText);


                        }

                        //扩展支持alert事件
                        @Override
                        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                            Log.e("onJsAlert:", message.toString());
                            try {
                                FileOutputStream fos = new FileOutputStream("/sdcard/tt.html");
                                byte[] buf = message.getBytes("utf-8");
                                fos.write(buf,0,buf.length);
                                fos.flush();
                                fos.close();
                            }catch (Exception e){
                                MyLogger.kLog().e(e);
                            }
                            //  showTest(message);
                            alertDialog = UIUtils.getAlertDialog(PageLookActivity.this, "提示信息", message, null, "确定", 0, null, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
//                                    finish();
                                }
                            });
                            alertDialog.show();
                            result.confirm();
                            return true;
                        }

                        //扩展浏览器上传文件
                        //3.0++版本
                        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                            openFileChooserImpl(uploadMsg);
                        }

                        //3.0--版本
                        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                            openFileChooserImpl(uploadMsg);
                        }

                        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                            openFileChooserImpl(uploadMsg);
                        }

                        // For Android > 5.0
                        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, WebChromeClient.FileChooserParams fileChooserParams) {
                            openFileChooserImplForAndroid5(uploadMsg);
                            return true;
                        }


                    }
            );
            mMyMw.addJavascriptInterface(new WebViewJavaScriptFunction() {

                @Override
                public void onJsFunctionCalled(String tag) {
                    // TODO Auto-generated method stub

                }

                @JavascriptInterface
                public void onX5ButtonClicked() {
                    enableX5FullscreenFunc();
                }

                @JavascriptInterface
                public void onCustomButtonClicked() {
                    disableX5FullscreenFunc();
                }

                @JavascriptInterface
                public void onLiteWndButtonClicked() {
                    enableLiteWndFunc();
                }

                @JavascriptInterface
                public void onPageVideoClicked() {
                    enablePageVideoFunc();
                }
            }, "Android");
            initMenuPpwWindow();
        }



        if(isDownVideo){
            tv_down_video.setVisibility(View.VISIBLE);
        }else {
            tv_down_video.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mMyMw.pauseTimers();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (null == mUploadMessageForAndroid5)
                return;
            Uri result = (intent == null || resultCode != RESULT_OK) ? null : intent.getData();
            if (result != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            } else {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            }
            mUploadMessageForAndroid5 = null;
        }
    }

    private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
    }

    private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
        mUploadMessageForAndroid5 = uploadMsg;
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");
        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");

        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
    }

    private void initMenuPpwWindow() {
        mPpwMenuShare = ShareUtil.getInstance().initMenuPpwWindow();

        if (mPageUrl.startsWith(RetrofitUtil.API_URL)) {
            mPageUrl = mPageUrl.replace(RetrofitUtil.API_URL, RetrofitUtil.API_URL_RANDOM);
        }

        //判断是否是原创，在pageurl添加isHomePage 分享出去不显示底板
        if (isOriginal && !mPageUrl.contains("&isHomePage=1")) {
            mPageUrl = mPageUrl + "&isHomePage=1";
        }
        MyLogger.kLog().e("fenxiang mTitle=" + mTitle);
        ShareUtil.getInstance().setShareInfo(mPageUrl, mTitle, TextUtils.isEmpty(mImgPath) ? PagerEditActivity.defaultPath : mImgPath, mText);
    }

    @Override
    public int createSuccessView() {

        return R.layout.activity_page_look;
    }

    @OnClick({R.id.tv_net_snap,R.id.tv_down_video})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_net_snap:
                netSnap();
                break;
            case R.id.tv_down_video:
                break;
        }
    }


    private void netSnap(){
        //传递链接到截图页面保存截图
        Intent intent = new Intent(getApplicationContext(),SnapshotActivity.class);
        intent.putExtra(SnapshotActivity.WEB_URL,mPageUrl);
        intent.putExtra(SnapshotActivity.RIGHT_STATE,5);
        startActivity(intent);
    }



    @Override
    protected void onDestroy() {
        if (mMyMw != null) {
            Logger.i("close");
            mMyMw.destroy();
        }
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventEMMessage(CloseShareEvent closeShareEvent) {

        if (closeShareEvent.getState() == 1) {
            showTest("分享成功");
            mPpwMenuShare.dismiss();
            //finish();
        } else if (closeShareEvent.getState() == 2) {
            showTest("分享失败" + closeShareEvent.getMessage());
            mPpwMenuShare.dismiss();

        } else if (closeShareEvent.getState() == 3) {
            showTest("取消分享");
            mPpwMenuShare.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isWork){
            baseStartActivity(MyWorksActivity.class);
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        try {
            super.onConfigurationChanged(newConfig);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // /////////////////////////////////////////
    // 向webview发出信息
    private void enableX5FullscreenFunc() {

        if (mMyMw.getX5WebViewExtension() != null) {
            Toast.makeText(this, "开启X5全屏播放模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            mMyMw.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void disableX5FullscreenFunc() {
        if (mMyMw.getX5WebViewExtension() != null) {
            Toast.makeText(this, "恢复webkit初始状态", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", true);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            mMyMw.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void enableLiteWndFunc() {
        if (mMyMw.getX5WebViewExtension() != null) {
            Toast.makeText(this, "开启小窗模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", true);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            mMyMw.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void enablePageVideoFunc() {
        if (mMyMw.getX5WebViewExtension() != null) {
            Toast.makeText(this, "页面内全屏播放模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 1);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            mMyMw.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    @Override
    protected boolean webViewShowBack() {
        if(mMyMw.canGoBack()){
            mMyMw.goBack();
            return false;
        }
        return super.webViewShowBack();
    }
}
