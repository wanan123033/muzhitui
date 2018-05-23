package com.nevermore.muzhitui.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nevermore.muzhitui.MaterialActivity;
import com.nevermore.muzhitui.activity.MyModeTwoActivty;
import com.nevermore.muzhitui.PageLookActivity;
import com.nevermore.muzhitui.PagerEditActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.IsCanEdit;
import com.nevermore.muzhitui.module.network.WorkService;

import base.BaseActivityTwoV;
import base.BaseFragment;
import base.SPUtils;
import base.UIUtils;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by hehe on 2016/5/4.
 */
public class TabArticalEditFragment extends BaseFragment {
    @BindView(R.id.tvMatrial)
    TextView mTvMatrial;
    @BindView(R.id.tvMode)
    TextView mTvMode;
    @BindView(R.id.wvVedio)
    WebView mWvVedio;

    @BindView(R.id.tvMuzhiChange)
    TextView mTvMuzhiChange;

    @BindView(R.id.etUrl)
    EditText mEtUrl;

    @BindView(R.id.ivClear)
    ImageView mIvClear;

    @BindView(R.id.ll_taef)
    LinearLayout mll_taef;

    private LoadingAlertDialog mLoadingAlertDialog;
    private AlertDialog alertDialog;


    @Override
    public int createSuccessView() {
        return R.layout.fragment_articaledit;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String htmlUrl = "file:///android_asset/h5/src/vedio.html";
        mWvVedio.loadUrl(htmlUrl);
        WebSettings settings = mWvVedio.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// webview缓存的用法
        settings.setDomStorageEnabled(true);
        mWvVedio.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
        mWvVedio.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                l("url  = " + url);
                view.loadUrl(url);
                return true;
            }
        });

        alertDialog = UIUtils.getAlertDialog(getActivity(), "温馨提示", "您的试用次数已经使用完毕，请先购买会员", "确认使用", "购买会员", 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();


            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();


            }
        });


    }


    private void isCanEdit() {
        mLoadingAlertDialog = new LoadingAlertDialog(getActivity());
        mLoadingAlertDialog.show();
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().isCanEdit((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))).subscribe(new Subscriber<IsCanEdit>() {
            @Override
            public void onCompleted() {
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mLoadingAlertDialog.dismiss();
                showTest(mNetWorkError);
            }

            @Override
            public void onNext(IsCanEdit isCanEdit) {
                if (isCanEdit.getStatus() == 1||isCanEdit.getStatus() == 2) {/**去掉会员秒变次数用完的判断 20170517*/
                    Intent intent = new Intent(getActivity(), PagerEditActivity.class);
                    intent.putExtra(PagerEditActivity.KEY_URL, mEtUrl.getText().toString());
                    getActivity().startActivity(intent);
                } /*else if (isCanEdit.getStatus() == 2) {
                    alertDialog.show();
                    // showTest("您的秒变次数已用完");
                }*/ else {
                    showTest(mServerEror);
                }

            }
        });
        ((BaseActivityTwoV) getActivity()).addSubscription(sbMyAccount);
    }

    @OnClick({R.id.tvMatrial, R.id.tvMode, R.id.tvMuzhiChange, R.id.ivClear, R.id.flytBase, R.id.flytAd, R.id.flytCard, R.id.flytWin, R.id.flytCreator})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvMatrial:
                baseStartActivity(MaterialActivity.class);
                break;
            case R.id.tvMode:
                baseStartActivity(MyModeTwoActivty.class);
                break;
            case R.id.tvMuzhiChange:
                if (!TextUtils.isEmpty(mEtUrl.getText())) {
                    isCanEdit();
                } else {
                    showTest("链接不能为空");
                }
                break;
            case R.id.ivClear:
                mEtUrl.setText("");
                break;
            case R.id.flytBase:
                Intent intent = new Intent(getActivity(), PageLookActivity.class);
                intent.putExtra(PageLookActivity.KEY_URL, "http://www.muzhitui.cn/song/wx/base.html");
                intent.putExtra(PageLookActivity.KEY_TEXT, "这个软件可以把你的产品或项目推广到整个社交生态圈的推广神器！");

                getActivity().startActivity(intent);
                break;
            case R.id.flytAd:
                Intent intent1 = new Intent(getActivity(), PageLookActivity.class);
                intent1.putExtra(PageLookActivity.KEY_URL, "http://www.muzhitui.cn/song/wx/admode.html");
                intent1.putExtra(PageLookActivity.KEY_TEXT, "这个软件可以把你的产品或项目推广到整个社交生态圈的推广神器！");

                getActivity().startActivity(intent1);
                break;
            case R.id.flytCard:
                Intent intent2 = new Intent(getActivity(), PageLookActivity.class);
                intent2.putExtra(PageLookActivity.KEY_URL, "http://www.muzhitui.cn/song/wx/card.html");
                intent2.putExtra(PageLookActivity.KEY_TEXT, "这个软件可以把你的产品或项目推广到整个社交生态圈的推广神器！");

                getActivity().startActivity(intent2);
                break;
            case R.id.flytWin:
                Intent intent3 = new Intent(getActivity(), PageLookActivity.class);
                intent3.putExtra(PageLookActivity.KEY_URL, "http://mp.weixin.qq.com/s?__biz=MzIwMjIyNjg0Nw==&mid=2676236060&idx=1&sn=79abc385aaeedd15ced8df549773ae75&scene=0#wechat_redirect");
                intent3.putExtra(PageLookActivity.KEY_TEXT, "这个软件可以把你的产品或项目推广到整个社交生态圈的推广神器！");

                getActivity().startActivity(intent3);
                break;
            case R.id.flytCreator:
                Intent intent4 = new Intent(getActivity(), PageLookActivity.class);
                intent4.putExtra(PageLookActivity.KEY_URL, "http://www.muzhitui.cn/song/wx/creator.html");
                intent4.putExtra(PageLookActivity.KEY_TEXT, "这个软件可以把你的产品或项目推广到整个社交生态圈的推广神器！");

                getActivity().startActivity(intent4);
                break;
        }
    }

    @Override
    public void onDestroy() {
        if (mWvVedio != null) {
            mWvVedio.destroy();
        }
        super.onDestroy();
    }


}
