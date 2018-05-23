package com.nevermore.muzhitui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.nevermore.muzhitui.event.MyModeRefreshEvent;
import com.nevermore.muzhitui.fragment.MyWorksModeFragment;
import com.nevermore.muzhitui.fragment.TextAnimateFragment;
import com.nevermore.muzhitui.fragment.TextColorFragment;
import com.nevermore.muzhitui.fragment.TextSizeFragment;
import com.nevermore.muzhitui.fragment.TextTypeFragment;
import com.nevermore.muzhitui.module.bean.BaseBean;
import com.nevermore.muzhitui.module.bean.ImageUpload;
import com.nevermore.muzhitui.module.bean.MyMode;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.xutils.x;

import java.io.File;
import java.util.List;

import base.BaseActivityTwoV;
import base.ImageUtil;
import base.SPUtils;
import base.UIUtils;
import base.helper.PhotoActionHelper;
import base.network.RetrofitUtil;
import base.util.CacheUtil;
import base.util.EmojiFilter;
import base.view.LoadingAlertDialog;
import base.view.MyViewPager;
import butterknife.BindView;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

/**
 * 添加广告模板
 */
public class AdMakeActivity extends BaseActivityTwoV implements FontStyleInterface {
    @BindView(R.id.tvUpload)
    TextView mTvUpload;
    @BindView(R.id.etTitle1)
    EditText mEtTitle1;
    @BindView(R.id.flytHref)
    FrameLayout mFlytHref;
    @BindView(R.id.etText)
    EditText mEtText;
    @BindView(R.id.ivConfirm)
    ImageView mIvConfirm;
    @BindView(R.id.tvWordNum)
    TextView mTvWordNum;
    @BindView(R.id.rlytText)
    RelativeLayout mRlytText;
    @BindView(R.id.tbl)
    TabLayout mTbl;
    @BindView(R.id.vp)
    MyViewPager mVp;
    @BindView(R.id.tvConfirm)
    TextView mTvConfirm;
    @BindView(R.id.bwv)
    BridgeWebView bwv;
    @BindView(R.id.ivChoicedImg)
    ImageView ivChoicedImg;

    private String mImg;
    private String tabTiles[] = new String[]{"字体", "颜色", "大小", "动画"};
    private LoadingAlertDialog mLoadingAlertDialog;
    private String mFontFamily = "宋体";
    private String mFontColor = "#fff";
    private float mFontSize = 0.5f;
    private int mFontAnimate = -1;
    private int mId;
    private MyMode.AdvArrayBean mAdArrayBean;
    private boolean mIsChoiceImg;

    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart;
        private int editEnd;

        @Override
        public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int arg1, int arg2,
                                  int arg3) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.e("4444","--------");
            if (temp.length() > 15) {
                editStart = mEtText.getSelectionStart();
                editEnd = mEtText.getSelectionEnd();
                showTest(mEtText, "你输入的字数已经超过了限制", Snackbar.LENGTH_SHORT, null, null);
                s.delete(editStart - 1, editEnd);
                int tempSelection = editStart;
                mEtText.setText(s);
                mEtText.setSelection(tempSelection - 1);
            }
            mTvWordNum.setText(s.length() + "/15");
            setText(mEtText.getText().toString());
        }
    };


    @Override
    public void init() {
        mAdArrayBean = (MyMode.AdvArrayBean) getIntent().getSerializableExtra(MyWorksModeFragment.AD);
        if (mAdArrayBean != null) {
            mFontColor = mAdArrayBean.getAdcolor();
            mFontFamily = mAdArrayBean.getFont();
            mFontSize = mAdArrayBean.getSize();
            mFontAnimate = mAdArrayBean.getAnimate();
            mId = mAdArrayBean.getAdvertid();
            setFontColor(mFontColor);
            setFontFamily(mFontFamily);
            setFontSize(mFontSize);
            setFontAnimation(mFontAnimate);
            mEtText.setText(mAdArrayBean.getAdtext());
            mEtTitle1.setText(mAdArrayBean.getAdurl());
            ImageLoader.getInstance().displayImage(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + mAdArrayBean.getAdimage(), ivChoicedImg, ImageUtil.getInstance().getBaseDisplayOption());
            bwv.setWebViewClient(new MyBridgeWebViewClient(bwv));

            mImg = mAdArrayBean.getAdimage();
        }
        bwv.setBackgroundColor(Color.TRANSPARENT);
        mEtText.addTextChangedListener(mTextWatcher);
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        setMyTitle("广告模板制作");
        showBack();
        String htmlUrl = "file:///android_asset/h5/src/adMake.html";
        bwv.getSettings().setDomStorageEnabled(true);
        bwv.getSettings().setUseWideViewPort(true);
        bwv.loadUrl(htmlUrl);
      /*  bwv.setDefaultHandler(new DefaultHandler() {
            @Override
            public void handler(String data, final CallBackFunction function) {

            }
        });*/
        mTbl.setTabMode(TabLayout.MODE_FIXED);
        mTbl.setTabGravity(TabLayout.GRAVITY_CENTER);
        mVp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return TextTypeFragment.newInstance(mFontFamily);     //字体
                    case 1:
                        return new TextColorFragment();  //颜色
                    case 2:
                        return TextSizeFragment.newInstance(mFontSize);  //大小
                    default:
                        return TextAnimateFragment.newInstance(mFontAnimate);   //动画
                }
            }

            @Override
            public int getCount() {
                return tabTiles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabTiles[position];
            }
        });
        mTbl.setupWithViewPager(mVp);
        mVp.setOffscreenPageLimit(4);

    }

    class MyBridgeWebViewClient extends BridgeWebViewClient {

        public MyBridgeWebViewClient(BridgeWebView webView) {
            super(webView);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Logger.i("mAdArrayBean.getAdtext() = " + mAdArrayBean.getAdtext());
//            if(!TextUtils.isEmpty(mAdArrayBean.getAdtext())) {
                setText(mAdArrayBean.getAdtext());
//            }


        }
    }

    private void uploadAD() {

        final String title1 = EmojiFilter.filterEmoji(mEtTitle1.getText().toString());
        final String title2 = EmojiFilter.filterEmoji(mEtText.getText().toString());
      /*  if (TextUtils.isEmpty(title1)) {
            showTest("请填写链接地址");
            return;
        }*/
        if (TextUtils.isEmpty(title2)) {
            showTest("请填写广告上的文字");
            return;
        }
        if (TextUtils.isEmpty(mImg)) {
            showTest("请添加广告图片");
            return;
        }
        mLoadingAlertDialog.show();
        Observable observable = null;
        if (mId == 0) {
            observable = wrapObserverWithHttp(WorkService.getWorkService().otherImagUpload((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),ImageUtil.getInstance().wrapUploadImgRequest(new File(mImg))).flatMap(new Func1<ImageUpload, Observable<BaseBean>>() {
                @Override
                public Observable<BaseBean> call(ImageUpload o) {
                    if (!TextUtils.isEmpty(o.getImgUrl())) {

                        return WorkService.getWorkService().saveAdv((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),o.getImgUrl(), title2, title1, null, mFontFamily, mFontColor, mFontSize, mFontAnimate);
                    } else {
                        return null;
                    }
                }
            }));
        } else {
            if (mIsChoiceImg) {
                observable = wrapObserverWithHttp(WorkService.getWorkService().otherImagUpload((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),ImageUtil.getInstance().wrapUploadImgRequest(new File(mImg))).flatMap(new Func1<ImageUpload, Observable<BaseBean>>() {
                    @Override
                    public Observable<BaseBean> call(ImageUpload o) {
                        if (!TextUtils.isEmpty(o.getImgUrl())) {
                            return WorkService.getWorkService().uploadAdv((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),mId, o.getImgUrl(), title2, title1, null, mFontFamily, mFontColor, mFontSize, mFontAnimate);
                        } else {
                            return null;
                        }
                    }
                }));
            } else {
                observable = wrapObserverWithHttp(WorkService.getWorkService().uploadAdv((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),mId, mImg, title2, title1, null, mFontFamily, mFontColor, mFontSize, mFontAnimate));
            }
        }
        Subscription sbMyAccount = observable.subscribe(new Subscriber<BaseBean>() {
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
            public void onNext(BaseBean baseBean) {
                if (baseBean.getState() == 1) {
                    EventBus.getDefault().post(new MyModeRefreshEvent());
                    finish();
                } else {
                    showTest(mServerEror);
                }

            }
        });
        addSubscription(sbMyAccount);
    }


    public void setFontFamily(String fontFamily) {
        Log.e("==mFontFamily",mFontFamily);
        mFontFamily = fontFamily;
        bwv.callHandler("setFontFamily", fontFamily, new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                l("data = " + data);
            }
        });
    }


    public void setFontColor(String fontColor) {
        Log.e("==mFontColor",mFontColor);
        mFontColor = fontColor;
        bwv.callHandler("setFontColor", fontColor, new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                l("data = " + data);
            }
        });
    }

    public void setFontSize(float size) {
        Log.e("==mFontSize",mFontSize+"");
        mFontSize = size;
        l("size = " + size);
        bwv.callHandler("setFontSize", String.valueOf(size), new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                l("data = " + data);
            }
        });
    }

    public void setFontAnimation(int index) {
        Log.e("==mFontAnimate",mFontAnimate+"");
        mFontAnimate = index;

        bwv.callHandler("setFontAnimation", String.valueOf(index), new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                l("data = " + data);
            }
        });

    }

    public void setText(String text) {
        bwv.callHandler("setText", text, new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                Logger.i("data = " + data);
            }
        });

    }

    @Override
    public void setAlignStyle(int index) {

    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_ad_make;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK
                && data != null
                && (requestCode == REQUEST_CLIP_IMAGE)) {
            String path = PhotoActionHelper.getOutputPath(data);
            if (path != null) {
               /* Bitmap bitmap = BitmapFactory.decodeFile(path);
                mIvHead.setImageBitmap(bitmap); // 这里注意 别内存溢出*/
                final Uri uri = Uri.parse("file:/" + path);
                mImg = path;
                mIsChoiceImg = true;
                ImageLoader.getInstance().displayImage(uri.toString(), ivChoicedImg);
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String mOutputPath;
    public static final int REQUEST_CLIP_IMAGE = 2028;

    @OnClick({R.id.tvUpload, R.id.ivConfirm, R.id.tvConfirm, R.id.ivChoicedImg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvUpload:

                break;
            case R.id.ivConfirm:
                Logger.i("onclick  onclick onclick ");
                setText(mEtText.getText().toString());
                break;
            case R.id.tvConfirm:     //完成提交
                uploadAD();
                break;
            case R.id.ivChoicedImg:
                CacheUtil.getInstance().add(CacheUtil.FORMAT_FRTE_TYPE,1);
                ImageUtil.getInstance().chooseImage("开始制作",new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        if (resultList != null && !resultList.isEmpty()) {
                            mImg = resultList.get(0).getPhotoPath();
                            mOutputPath = new File(UIUtils.getExternalCacheDir(), System.currentTimeMillis() + ".jpg").getPath();
                            PhotoActionHelper.clipImage(AdMakeActivity.this).input(mImg).output(mOutputPath).setExtraHeight(UIUtils.dip2px(165)).maxOutputWidth(640)
                                    .requestCode(REQUEST_CLIP_IMAGE).start();
                        }
                    }

                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {

                    }
                }, 1);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (bwv != null) {
        /*    bwv.clearHistory();
            bwv.clearCache(true);
            bwv.loadUrl("about:blank"); // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now
            bwv.freeMemory();
            bwv.pauseTimers();*/
            bwv.destroy();
            //bwv = null; // Note that mWebView.destroy() and mWebView = null do the exact same thing  为毛了
        }
        super.onDestroy();
    }

}
