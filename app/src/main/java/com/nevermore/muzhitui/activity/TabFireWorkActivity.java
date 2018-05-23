package com.nevermore.muzhitui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nevermore.muzhitui.PageLookActivity;
import com.nevermore.muzhitui.R;

import base.BaseActivityTwoV;
import base.LogUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by simone on 2016/12/14.
 */

public class TabFireWorkActivity extends BaseActivityTwoV {

    @BindView(R.id.wv)
    WebView mWv;

    @Override
    public void init() {
        //hideActionBar();
        showBack();
        setMyTitle("公共文章");
        String mH5url = "http://weixin.sogou.com/";
        //mH5url = "http://baidu.com";
        mWv.loadUrl(mH5url);
        LogUtil.i("ShowH5Activity = " + mH5url);
        WebSettings settings = mWv.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// webview缓存的用法
        settings.setDomStorageEnabled(true);
        mWv.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
        mWv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent intent = new Intent(TabFireWorkActivity.this, PageLookActivity.class);
                intent.putExtra(PageLookActivity.KEY_URL, url);

              startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public int createSuccessView() {

        return R.layout.fragment_firework;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
