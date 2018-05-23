package com.nevermore.muzhitui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nevermore.muzhitui.PageLookActivity;
import com.nevermore.muzhitui.R;

import base.BaseFragment;
import base.LogUtil;
import butterknife.BindView;

/**
 * Created by hehe on 2016/5/5.
 */
public class TabFireWorkFragment extends BaseFragment {
    @BindView(R.id.wv)
    WebView mWv;

    @Override
    public int createSuccessView() {
        return R.layout.fragment_firework;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                Intent intent = new Intent(getActivity(),PageLookActivity.class);
                intent.putExtra(PageLookActivity.KEY_URL, url);
                getActivity().startActivity(intent);
                return true;
            }
        });
    }
}
