package com.nevermore.muzhitui;

import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import base.BaseActivityTwoV;
import butterknife.BindView;

public class TipActivity extends BaseActivityTwoV {

    @BindView(R.id.wvTip)
    WebView mWvMyTip;

    @Override
    public void init() {
        setMyTitle("标题模板制作");
        showBack();
        String htmlUrl = "file:///android_asset/h5/src/titleModeMake.html";
        mWvMyTip.loadUrl(htmlUrl);//
        WebSettings settings = mWvMyTip.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// webview缓存的用法
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        mWvMyTip.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
        mWvMyTip.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_tip;
    }


    @Override
    protected void onDestroy() {
        if (mWvMyTip != null) {
            mWvMyTip.destroy();
        }
        super.onDestroy();
    }
}
