package com.nevermore.muzhitui;

import android.view.Gravity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.PopupWindow;

import base.BaseActivityTwoV;
import base.network.RetrofitUtil;
import base.util.ShareUtil;
import base.view.LoadingAlertDialog;
import butterknife.BindView;

public class QrActivity extends BaseActivityTwoV {


    @BindView(R.id.wvMyQr)
    WebView mWvMyQr;

    LoadingAlertDialog mLoadingAlertDialog;

    @Override
    public void init() {
        setMyTitle("我的二维码");
        setTitleColor("#ffffff");
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        initMenuPpwWindow();
        showBack();
        setTitleBackGround(R.drawable.bg_mw);
        showRight(R.drawable.ic_spread, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPpwMenuShare.showAtLocation(mWvMyQr, Gravity.BOTTOM, 0, 0);
            }
        });


        String htmlUrl = RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + RetrofitUtil.MYQR + MainActivity.loginId;

        mWvMyQr.loadUrl(htmlUrl);//
        WebSettings settings = mWvMyQr.getSettings();
        settings.setJavaScriptEnabled(true);
  /*      settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// webview缓存的用法
        settings.setDomStorageEnabled(true);*/
        settings.setUseWideViewPort(true);
        mLoadingAlertDialog.show();
        mWvMyQr.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

            }
        });
        mWvMyQr.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private PopupWindow mPpwMenuShare;


    private void initMenuPpwWindow() {
        //设置contentView
        mPpwMenuShare = ShareUtil.getInstance().initMenuPpwWindow();
        ShareUtil.getInstance().setShareInfo(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + RetrofitUtil.SHAREQR + MainActivity.loginId, "拇指推—这是一个推广神器！", PagerEditActivity.defaultPath, "这个软件可以把你的产品或项目推广到整个社交生态圈的推广神器！");
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_qr;
    }


    @Override
    protected void onDestroy() {
        if (mWvMyQr != null) {
            mWvMyQr.destroy();
        }
        super.onDestroy();
    }
}
