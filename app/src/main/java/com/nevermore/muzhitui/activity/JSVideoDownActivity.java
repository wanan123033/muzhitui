package com.nevermore.muzhitui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.nevermore.muzhitui.PageLookActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.VideoPopWindow;
import com.nevermore.muzhitui.module.bean.VideoBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.MyLogger;
import base.UIUtils;
import base.util.CacheUtil;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/31.
 */

public class JSVideoDownActivity extends BaseActivityTwoV {
    public static final String PAGE_URL = "page_url";
    private LoadingAlertDialog mLoadingAlerDialog;
    private String mPageUrl;
    private VideoPopWindow videopop;

    @BindView(R.id.wv)
    WebView mMyMw;
    @BindView(R.id.progressBar1)
    ProgressBar progressBar1;

    @Override
    public void init() {
        mLoadingAlerDialog = new LoadingAlertDialog(this);
        showBack();
        setMyTitle("下载视频");

        mMyMw.getSettings().setPluginState(WebSettings.PluginState.ON);
        mMyMw.getSettings().setJavaScriptEnabled(true);
        mMyMw.getSettings().setAppCacheEnabled(false);
        mMyMw.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mMyMw.getSettings().setUseWideViewPort(true);
        mMyMw.getSettings().setLoadWithOverviewMode(true);
        mMyMw.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mPageUrl = getIntent().getStringExtra(PAGE_URL);
        mMyMw.loadUrl(mPageUrl);

        mMyMw.setWebChromeClient(new WebChromeClient(){
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
        });

        mMyMw.addJavascriptInterface(new Object(){
            @JavascriptInterface
            public void getResult(final String message){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (message.startsWith("{") && message.endsWith("}")){  //跳转到下载视频的界面
                            try {
                                JSONObject jsonObject = new JSONObject(message);
                                final VideoBean bean = new VideoBean();
                                if(jsonObject.has("video_url"))
                                    bean.videoUrl = jsonObject.getString("video_url");
                                if(jsonObject.has("image_url"))
                                    bean.imageUrl = jsonObject.getString("image_url");
                                if(jsonObject.has("videoTitle"))
                                    bean.videoTitle = jsonObject.getString("videoTitle");
                                mLoadingAlerDialog.dismiss();
                                videopop = new VideoPopWindow(JSVideoDownActivity.this, bean, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        List<VideoBean> beans = (List<VideoBean>) CacheUtil.getInstance().get(MyDownVideoActivity.DOWN_VIDEOS);
                                        if (beans == null){
                                            beans = new ArrayList<>();
                                        }
                                        MyLogger.kLog().e(bean.toString());
                                        beans.add(bean);
                                        CacheUtil.getInstance().add(MyDownVideoActivity.DOWN_VIDEOS,beans);
                                        videopop.dismiss();
                                        Intent intent = new Intent(getApplicationContext(), MyDownVideoActivity.class);
                                        startActivity(intent);
                                        mMyMw.destroy();
                                        finish();
                                    }
                                });
                                videopop.showAtLocation(mMyMw, Gravity.BOTTOM, 0, 0);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }
                });

            }
        },"Android");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMyMw.resumeTimers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMyMw.pauseTimers();
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_js_videodown;
    }

    @OnClick(R.id.tv_video)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_video:
                downVideo();
                break;
        }
    }

    private void downVideo() {
        mLoadingAlerDialog.show();
        if(mPageUrl.contains("huoshan.com")){ //下载火山视频
//            mMyMw.loadUrl("javascript:alert('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            mMyMw.loadUrl("javascript:"+
                    "var video = document.getElementsByClassName(\"player\")[0];" +
                    "var video_url = video.getAttribute(\"data-src\");\n" +
                    "var image_url = video.getAttribute(\"data-poster-src\");"+
                    "Android.getResult('{\"video_url\":\"'+video_url+'\",\"image_url\":\"'+image_url+'\",\"videoTitle\":\"'+document.title+'\"}');");
        }else if(mPageUrl.contains("gifshow.com") || mPageUrl.contains("xiaokaxiu.com") || mPageUrl.contains("inke.cn")){  //下载快手 映客  小咖秀视频
            //           mMyMw.loadUrl("javascript:alert('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
//            <video id="video-player" poster="https://tx2.a.yximgs.com/upic/2017/12/14/02/BMjAxNzEyMTQwMjE4MzJfMjc1MTc1NzhfNDIwNDMyMTExN18yXzM=_A378bd1d23d1df5130e173cef080e89b7.jpg" src="https://txmov2.a.yximgs.com/upic/2017/12/14/02/BMjAxNzEyMTQwMjE4MzJfMjc1MTc1NzhfNDIwNDMyMTExN18yXzM=_b_A6fbbe32eb634bf16635c78f585db1d72.mp4" alt="馃樄馃樄馃樄绗戞瀹濆疂浜? loop="" preload="auto" webkit-playsinline="" playsinline="" x5-video-player-fullscreen="true" x5-video-player-type="h5" class="video" x5nativepanel="freewififorvideo_fromvideoelment" style="max-height: 640px;"></video>
            mMyMw.loadUrl("var video = document.getElementsByTagName(\"video\")[0];" +
                    "var video_url = video.getAttribute(\"src\");" +
                    "var image_url = video.getAttribute(\"poster\");"+
                    "Android.getResult('{\"video_url\":\"'+video_url+'\",\"image_url\":\"'+image_url+'\",\"videoTitle\":\"'+document.title+'\"}');");
        }else if(mPageUrl.contains("douyin.com")){  //抖音视频
            mMyMw.loadUrl("javascript:var videodiv = document.getElementById(\"theVideo\");" +
                    "var video_url = videodiv.getAttribute('src');" +
                    "var imgDiv = document.getElementsByClassName('player')[0];" +
                    "var image_url = imgDiv.getAttribute('data-poster-src');" +
                    "Android.getResult('{\"video_url\":\"'+video_url+'\",\"image_url\":\"'+image_url+'\",\"videoTitle\":\"'+document.title+'\"}');");
        }else if(mPageUrl.contains("miaopai.com") || mPageUrl.contains("weibo.cn")){   //秒变 新浪微博
//            mMyMw.loadUrl("javascript:alert('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            mMyMw.loadUrl("javascript:var video = document.getElementsByTagName(\"video\")[0];" +
                    "var video_url = video.getAttribute(\"src\");" +
                    "var image_url = video.getAttribute(\"poster\");"+
                    "Android.getResult('{\"video_url\":\"'+video_url+'\",\"image_url\":\"'+image_url+'\",\"videoTitle\":\"'+document.title+'\"}');");
        }else if (mPageUrl.contains("pearvideo.com")){  //梨视频
//            mMyMw.loadUrl("javascript:alert('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            mMyMw.loadUrl("javascript:var video = document.getElementById(\"h5video\");" +
                    "var video_url = video.getAttribute(\"src\");" +
                    "Android.getResult('{\"video_url\":\"'+video_url+'\",\"videoTitle\":\"'+document.title+'\"}');");
        }else if(mPageUrl.contains("xigua")){  //西瓜
            mMyMw.loadUrl("javascript:var video = document.getElementById(\"vjs_video_3_html5_api\");" +
                    "var video_url = video.getAttribute(\"src\");" +
                    "var image_url = video.getAttribute('poster');"+
                    "Android.getResult('{\"video_url\":\"'+video_url+'\",\"image_url\":\"'+image_url+'\",\"videoTitle\":\"'+document.title+'\"}');");
////            mMyMw.loadUrl("javascript:alert('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
//            mMyMw.loadUrl("javascript:var video = document.getElementById(\"vjs_video_3_html5_api\");" +
//                    "var video_url = video.getAttribute(\"src\");" +
//                    "alert('{\"video_url\":\"'+video_url+'\",\"videoTitle\":\"'+document.title+'\"}');");
        }else if(mPageUrl.contains("neihanshequ.com")){ //内涵段子
            mMyMw.loadUrl("javascript:var video = document.getElementsByClassName('player-container')[0];" +
                    "var video_url = video.getAttribute('data-src');" +
                    "var image_url = video.getAttribute('data-poster-src');" +
                    "Android.getResult('{\"video_url\":\"'+video_url+'\",\"image_url\":\"'+image_url+'\",\"videoTitle\":\"'+document.title+'\"}');");
        }else if(mPageUrl.contains("musemuse.cn")){     //muse
            mMyMw.loadUrl("javascript:var video = document.getElementById('videoALL');" +
                    "var video_url = video.getAttribute('src');" +
                    "var image_url = video.getAttribute('poster');" +
                    "Android.getResult('{\"video_url\":\"'+video_url+'\",\"image_url\":\"'+image_url+'\",\"videoTitle\":\"'+document.title+'\"}');");
        }else if(mPageUrl.contains("baidu.com")){  //百度视频  链接有加密 需要解密 播放
//            mMyMw.loadUrl("javascript:alert('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
            mMyMw.loadUrl("javascript:var video = document.getElementsByTagName(\"video\")[0];" +
                    "var video_url = video.getAttribute(\"src\");" +
                    "Android.getResult('{\"video_url\":\"'+video_url+'\",\"videoTitle\":\"'+document.title+'\"}');");
        }else{
            mLoadingAlerDialog.dismiss();
            showTest("不支持该链接");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMyMw != null) {
            mMyMw.stopLoading();//再次打开页面时，若界面没有消亡，会导致进度条不显示并且界面崩溃
            mMyMw.onPause();
            mMyMw.clearCache(true);
            mMyMw.clearHistory();
            mMyMw.removeAllViews();
            mMyMw.destroyDrawingCache();
            ViewGroup parent = (ViewGroup) mMyMw.getParent();
            if (parent != null) {
                parent.removeView(mMyMw);
            }
            mMyMw.removeAllViews();
            mMyMw.destroy();//这句由于有些在其他线程还没有结束，会导致空指针异常导致没办法使用
            mMyMw = null;
        }
    }
}
