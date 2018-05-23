package com.nevermore.muzhitui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nevermore.muzhitui.PagerEditActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.IsCanEdit;
import com.nevermore.muzhitui.module.network.WorkService;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.view.LoadingAlertDialog;
import base.view.WebViewJavaScriptFunction;
import base.view.X5WebView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by simone on 2016/12/15.
 * 拇指秒变Activity
 */

public class ThumbChangeActivity extends BaseActivityTwoV {

    @BindView(R.id.etUrl)
    EditText etUrl;
    @BindView(R.id.ivClear)
    ImageView ivClear;
    @BindView(R.id.tvMuzhiChange)
    TextView tvMuzhiChange;
    @BindView(R.id.tvOperationInfo)
    TextView tvOperationInfo;
    @BindView(R.id.webView)
    X5WebView webView;

    private LoadingAlertDialog mLoadingAlertDialog;
    @Override
    public void init() {

        setMyTitle("拇指秒变");
        showBack();
        playVedio();

    }

    private void playVedio() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        webView.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        webView.addJavascriptInterface(new WebViewJavaScriptFunction() {

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
        webView.loadUrl("http://www.muzhitui.cn/song/wx/base.html?from=message&isappinstalled=0");
    }
    @Override
    public int createSuccessView() {
        return R.layout.activity_thumbchange;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({ R.id.ivClear, R.id.tvMuzhiChange, R.id.tvOperationInfo})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ivClear:
                etUrl.setText("");
                break;
            case R.id.tvMuzhiChange:
                if (!TextUtils.isEmpty(etUrl.getText()) && etUrl.getText().toString().indexOf("http") == 0) {
                    Intent intent = new Intent(ThumbChangeActivity.this, PagerEditActivity.class);
                    intent.putExtra(PagerEditActivity.KEY_URL, etUrl.getText().toString());
                    startActivity(intent);
                } else {
                    showTest("链接不能为空");
                }
                break;
            case R.id.tvOperationInfo:
                baseStartActivity(VidioActivity.class);
                break;
        }
    }
//    private void isCanEdit() {
//        mLoadingAlertDialog = new LoadingAlertDialog(this);
//        mLoadingAlertDialog.show();
//        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().isCanEdit((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))).subscribe(new Subscriber<IsCanEdit>() {
//            @Override
//            public void onCompleted() {
//                mLoadingAlertDialog.dismiss();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//                mLoadingAlertDialog.dismiss();
////                showTest(mNetWorkError);
//            }
//
//            @Override
//            public void onNext(IsCanEdit isCanEdit) {
//                if (isCanEdit.getStatus() == 1||isCanEdit.getStatus() == 2||isCanEdit.getStatus() == 3) {/**去掉会员秒变次数用完的判断 20170517*/
//
//                }/* else if (isCanEdit.getStatus() == 2) {
//                    showDialog("您的试用次数已经使用完毕，请先购买会员");
//                    alertDialog.show();
//
//                }else if (isCanEdit.getStatus() == 3) {
//                    showDialog("您的会员已到期，无法编辑分享；请先续费");
//                    alertDialog.show();
//                } */else {
//                    showTest(mServerEror);
//                }
//
//            }
//        });
//        addSubscription(sbMyAccount);
//    }

    // /////////////////////////////////////////
    // 向webview发出信息
    private void enableX5FullscreenFunc() {

        if (webView.getX5WebViewExtension() != null) {
            Toast.makeText(this, "开启X5全屏播放模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            webView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void disableX5FullscreenFunc() {
        if (webView.getX5WebViewExtension() != null) {
            Toast.makeText(this, "恢复webkit初始状态", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", true);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            webView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void enableLiteWndFunc() {
        if (webView.getX5WebViewExtension() != null) {
            Toast.makeText(this, "开启小窗模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", true);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            webView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void enablePageVideoFunc() {
        if (webView.getX5WebViewExtension() != null) {
            Toast.makeText(this, "页面内全屏播放模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 1);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            webView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
}
