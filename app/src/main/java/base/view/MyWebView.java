package base.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.nevermore.muzhitui.R;

/**
 * Created by hehe on 2016/6/13.
 */
public class MyWebView extends WebView {
    private ProgressBar progressbar;
    public MyWebView(Context context) {
        super(context);
        init(context);

    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void init(Context context) {
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
    //    settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// webview缓存的用法
     //   settings.setAppCacheEnabled();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setDefaultTextEncodingName("UTF-8") ;
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);

    //    settings.setLoadWithOverviewMode(true);

        progressbar = new ProgressBar(context, null,
                android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                6, 0, 0));
        progressbar.setProgressDrawable(this.getResources().getDrawable(
                R.drawable.btn_progress_webview));
        addView(progressbar);
        setWebChromeClient(new WebChromeClient());
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
      /*  LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);*/
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
