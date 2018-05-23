package com.nevermore.muzhitui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nevermore.muzhitui.event.DraftEvent;
import com.nevermore.muzhitui.fragment.TextAlignFragment;
import com.nevermore.muzhitui.fragment.TextAnimateFragment;
import com.nevermore.muzhitui.fragment.TextColorFragment;
import com.nevermore.muzhitui.fragment.TextSizeFragment;
import com.nevermore.muzhitui.fragment.TextTypeFragment;
import com.nevermore.muzhitui.module.bean.BaseMyMode;
import com.nevermore.muzhitui.module.bean.ImageUpload;
import com.nevermore.muzhitui.module.bean.MyMode;
import com.nevermore.muzhitui.module.bean.PageUpdate;
import com.nevermore.muzhitui.module.bean.SavePagerResult;
import com.nevermore.muzhitui.module.bean.SecondPage;
import com.nevermore.muzhitui.module.bean.SongInfo;
import com.nevermore.muzhitui.module.network.WorkService;
import com.orhanobut.logger.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import base.BaseActivityTwoV;
import base.CrashHandler;
import base.ImageUtil;
import base.MyLogger;
import base.SPUtils;
import base.UIUtils;
import base.helper.PhotoActionHelper;
import base.network.RetrofitUtil;
import base.util.CacheUtil;
import base.view.LoadingAlertDialog;
import base.view.MyViewPager;
import base.view.bridgewebview.BridgeHandler;
import base.view.bridgewebview.BridgeWebView;
import base.view.bridgewebview.BridgeWebViewClient;
import base.view.bridgewebview.CallBackFunction;
import base.view.bridgewebview.DefaultHandler;
import butterknife.BindView;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * 秒变文章编辑
 */
public class PagerEditActivity extends BaseActivityTwoV implements View.OnClickListener, FontStyleInterface {

    @BindView(R.id.mywv)
    BridgeWebView mMyMw;
    public static final String KEY_URL = "PAGERURL";
    public static final String KEY_ID = "ID";
    public static final String KEY_TITLE = "TITLE";

    private LoadingAlertDialog mLoadingAlertDialog;
    private PopupWindow mOptImgPopWindow;
    private String mOutputPath;
    public static final int REQUEST_CLIP_IMAGE = 2028;
    @BindView(R.id.tbl)
    TabLayout mTbl;
    @BindView(R.id.vp)
    MyViewPager mVp;
    final static String YY_OVERRIDE_SCHEMA = "yy://";
    final static String YY_SCHEMA = "wvjbscheme://";
    final static String YY_RETURN_DATA = YY_OVERRIDE_SCHEMA + "return/";//格式为   yy://return/{function}/returncontent
    final static String YY_FETCH_QUEUE = YY_RETURN_DATA + "_fetchQueue/";
    @BindView(R.id.flytFont)
    FrameLayout mflytFont;

    @BindView(R.id.etContent)
    EditText mEtContent;

    @BindView(R.id.ivConfirm)
    ImageView mIvConfirm;

    @BindView(R.id.ivCancle)
    ImageView mIvCancle;

    @BindView(R.id.tvClear)
    TextView mTvClear;

    @BindView(R.id.llytFont)
    LinearLayout mLlytFont;

    @BindView(R.id.llytStyle)
    LinearLayout mLlytStyle;

    @BindView(R.id.llytEdit)
    LinearLayout mLlytEdit;

    @BindView(R.id.cbStyle)
    CheckBox mCbStyle;

    public String SERVERSTYLE = "<link rel=\"stylesheet\" type=\"text/css\" href=\"/" + RetrofitUtil.PROJECT_URL + "css/app/style.css\"/>";

    public String SERVERMUSIC = "/" + RetrofitUtil.PROJECT_URL + "img/app/music.png";
    private String HEADTRAP = "<!--headTrap<body></body><head></head><html></html>-->";
    private String TAILTRAP = "<!--tailTrap<body></body><head></head><html></html>-->";

    private String mPageId;

    public static final String defaultPath = "wx/img/logo.jpg";
    private String mImgPath = defaultPath;

    private String tabTiles[] = new String[]{"对齐", "字体", "颜色", "大小", "动画"};
    private String mFontFamily = "宋体";
    private String mFontColor = "#fff";
    private float mFontSize = 0.5f;
    private int mFontAnimate = -1;
    private String mWebSite;
    private String mTitle;
    private String mPath;


    private GalleryFinal.OnHanlderResultCallback mOnhanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null && !resultList.isEmpty()) {
                final Uri uri = Uri.parse("file:/" + resultList.get(0).getPhotoPath());
                mOptImgPopWindow.dismiss();
                mOutputPath = new File(UIUtils.getExternalCacheDir(), System.currentTimeMillis() + ".jpg").getPath();
                Logger.e("path:"+resultList.get(0).getPhotoPath());
                PhotoActionHelper.clipImage(PagerEditActivity.this).input(resultList.get(0).getPhotoPath()).output(mOutputPath).maxOutputWidth(640)
                        .requestCode(REQUEST_CLIP_IMAGE).start();
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {

        }
    };
    private float mX;
    private float mY;
    private StringBuffer mStrAdd;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);

    }

    @Override
    public void init() {
        setMyTitle("文章编辑");
        showBack();
        mWebSite = getIntent().getStringExtra(KEY_URL);

        mPageId = getIntent().getStringExtra(KEY_ID);
        mTitle = getIntent().getStringExtra(KEY_TITLE);
        mPageId = mPageId == null ? "" : mPageId;
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        if (!TextUtils.isEmpty(mWebSite) || !TextUtils.isEmpty(mPageId)) {
            mIvConfirm.setOnClickListener(this);
            mIvCancle.setOnClickListener(this);
            mflytFont.setOnClickListener(this);
            mTvClear.setOnClickListener(this);
            mCbStyle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mLlytStyle.setVisibility(View.VISIBLE);
                    } else {
                        mLlytStyle.setVisibility(View.INVISIBLE);
                    }

                }
            });
            showRight("完成", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setEnabled(false);
                    mMyMw.callHandler("getHtmlContent", "", new CallBackFunction() {
                        @Override
                        public void onCallBack(String data) {
                            Log.i("onCallBack",data+"----------");
                            data = data.replace(mStrAdd.toString(), "");
                            data = wrapContent(data);
                            Intent intent = new Intent(PagerEditActivity.this, PageEditOverActivity.class);
                            Log.i("TAG---","PagerEditActivity mTitle = " + mTitle);
                            intent.putExtra(PageEditOverActivity.KEY_TITLE, mTitle);
                            try {
                                FileOutputStream fos = new FileOutputStream("/storage/sdcard0/test.html");
                                byte[] buf = data.getBytes("utf-8");
                                fos.write(buf,0,buf.length);
                                fos.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            CacheUtil.getInstance().add(PageEditOverActivity.KEY_CONTENT,data);   //防止data数据过大导致TransactionTooLargeException异常
                            intent.putExtra(PageEditOverActivity.KEY_WEBSITE, mWebSite);
                            intent.putExtra(PageEditOverActivity.KEY_IMAGE, mImgPath);
                            intent.putExtra(PageEditOverActivity.KEY_PAGEID, mPageId);
                            startActivity(intent);
                            finish();
                        }
                    });

                }
            });
            mLoadingAlertDialog.show();
//            getWindow().setFormat(PixelFormat.TRANSLUCENT);
//
//            mMyMw.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);
//            mMyMw.addJavascriptInterface(new WebViewJavaScriptFunction() {
//
//                @Override
//                public void onJsFunctionCalled(String tag) {
//                    // TODO Auto-generated method stub
//
//                }
//
//                @JavascriptInterface
//                public void onX5ButtonClicked() {
//                    enableX5FullscreenFunc();
//                }
//
//                @JavascriptInterface
//                public void onCustomButtonClicked() {
//                    disableX5FullscreenFunc();
//                }
//
//                @JavascriptInterface
//                public void onLiteWndButtonClicked() {
//                    enableLiteWndFunc();
//                }
//
//                @JavascriptInterface
//                public void onPageVideoClicked() {
//                    enablePageVideoFunc();
//                }
//            }, "Android");
            mMyMw.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mX = event.getX();
                    mY = event.getY();
                    return false;
                }
            });
            initJavaOpt();
            mMyMw.setDefaultHandler(new DefaultHandler() {
                @Override
                public void handler(String data, final CallBackFunction function) {
                    Logger.i("data =" + data);
                }
            });
            if (TextUtils.isEmpty(mPageId)) {
                pageEdit(mWebSite);
            } else {
                pageUpdate(mPageId);
            }

           /* wrapObserverWithHttp(Observable.create(new Observable.OnSubscribe<String>() {  //observe是不是页面退出也要取消
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(pageUrl)
                            .build();
                    try {
                        Response response = client.newCall(request).execute(); //取消请求
                        String content = response.body().string();
                        Logger.i(content);
                        subscriber.onNext(wrapHtml(content));
                        subscriber.onCompleted();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            })).subscribe(new Subscriber<String>() {
                @Override
                public void onCompleted() {
                    mLoadingAlertDialog.dismiss();
                }

                @Override
                public void onError(Throwable e) {
                    showTest(mNetWorkError);
                }

                @Override
                public void onNext(String s) {
                    mMyMw.loadDataWithBaseURL("file:///android_asset/pageedt/", s, "text/html", "UTF-8", "file:///android_asset/pageedt/");
                }
            });*/
            mMyMw.getSettings().setUseWideViewPort(true);
            mMyMw.setWebChromeClient(new WebChromeClient() {


                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
//                    mTitle = title;
                    if(TextUtils.isEmpty(mTitle)){
                        mTitle = title;
                    }
                }

                public boolean onJsAlert(WebView view, String url, String message,
                                         JsResult result) {
                    Toast.makeText(PagerEditActivity.this, message, Toast.LENGTH_LONG).show();
                    result.confirm();
                    return true;
                }


            });
            mMyMw.setWebViewClient(new MyBridgeWebViewClient(mMyMw));
            initPpwWindow();
            initFont();
        }
    }

    private void pageEdit(String webSite) {
        Subscription sbSecondeEdit = wrapObserverWithHttp(WorkService.getWorkService().pageEdit((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),webSite)
                .doOnNext(new Action1<SecondPage>() {
            @Override
            public void call(SecondPage secondPage) {
                if (secondPage.getPage() != null) {
                    secondPage.getPage().setPagecontent(wrapHtml(secondPage.getPage().getPagecontent()));
                }
            }
        })).subscribe(new Subscriber<SecondPage>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                CrashHandler.getInstance().saveCrashInfo2File(e,"3.8.1");
                removeLoadingView();
                removeErrorView();
                mLoadingAlertDialog.dismiss();
                showTest(mNetWorkError);
            }

            @Override
            public void onNext(SecondPage secondPage) {
                try {
                    FileOutputStream fis = new FileOutputStream("/storage/sdcard0/logcat.log");
                    byte[] length = secondPage.toString().getBytes("utf-8");
                    fis.write(length,0,length.length);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (secondPage.getStatus().equals("1")||secondPage.getStatus().equals("2")||secondPage.getStatus().equals("3")) {/**去掉会员秒变次数用完的判断 20170517*/
                    SecondPage.PageBean page = secondPage.getPage();
                    if (page != null) {
                        mPageId = page.getId();
                        if (mPageId != null) {
                            mImgPath = page.getImage();
                        }
                        Logger.e("mImgPath =" + mImgPath + "   " + mPageId);
                        mMyMw.loadDataWithBaseURL("file:///android_asset/pageedt/", page.getPagecontent(), "text/html", "UTF-8", "file:///android_asset/pageedt/");
                    }else {
                        showTest("请求出现错误！");
                    }
                }/*else if(secondPage.getStatus().equals("2")){

                   // Logger.e("mImgPath =" + secondPage.getPage().getImage() + "   " + secondPage.getPage().getId());
                    showTest("您的试用次数已经使用完毕，请先购买会员");
                }else if(secondPage.getStatus().equals("3")){
                  //  Logger.e("mImgPath =" + secondPage.getPage().getImage() + "   " + secondPage.getPage().getId());
                    showTest("您的会员已到期，请先续费");
                } */else {
                    // finish();
                    showTest("暂时不支持该链接  状态码："+secondPage.getStatus());
                }

            }
        });
        addSubscription(sbSecondeEdit);
    }


    private void pageUpdate(String id) {
        Subscription sbSecondeEdit = wrapObserverWithHttp(WorkService.getWorkService().toPageUpdate((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),id).doOnNext(new Action1<PageUpdate>() {
            @Override
            public void call(PageUpdate pageUpdate) {
                if (pageUpdate.getPageContent() != null) {
                    pageUpdate.setPageContent(wrapHtml(pageUpdate.getPageContent()));
                }
            }
        })).subscribe(new Subscriber<PageUpdate>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                removeLoadingView();
                mLoadingAlertDialog.dismiss();
                showTest(mNetWorkError);
            }

            @Override
            public void onNext(PageUpdate secondPage) {
                MyLogger.kLog().e(secondPage.toString());
                try {
                    FileOutputStream fos = new FileOutputStream("/storage/sdcard0/test.html");
                    byte[] buf = secondPage.getPageContent().getBytes("utf-8");
                    fos.write(buf,0,buf.length);
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }



                if (secondPage.getState().equals("1")/*||secondPage.getState().equals("2")||secondPage.getState().equals("3")*/) {/**去掉会员秒变次数用完的判断 20170517*/
                    mImgPath = secondPage.getImage();
                    mWebSite = secondPage.getWebsite();
                    mMyMw.loadDataWithBaseURL("file:///android_asset/pageedt/", secondPage.getPageContent(), "text/html", "UTF-8", "file:///android_asset/pageedt/");
                }else if(secondPage.getState().equals("2")){
                    showTest("您的试用次数已经使用完毕，请先购买会员");
                }else if(secondPage.getState().equals("3")){
                    showTest("您的会员已到期，请先续费");
                }  else {
                    // finish();
                    showTest("暂时不支持该链接");
                }

            }
        });

        addSubscription(sbSecondeEdit);
    }


    private void initFont() {
        mTbl.setTabMode(TabLayout.MODE_FIXED);
        mTbl.setTabGravity(TabLayout.GRAVITY_CENTER);
        mVp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return TextAlignFragment.newInstance("");
                    case 1:
                        TextTypeFragment textTypeFragment = TextTypeFragment.newInstance(mFontFamily);
                        textTypeFragment.setmIsPagerEdit(true);
                        return textTypeFragment;
                    case 2:
                        return new TextColorFragment();
                    case 3:
                        TextSizeFragment textSizeFragment = TextSizeFragment.newInstance(mFontSize);
                        textSizeFragment.setmIsPagerEdit(true);
                        return textSizeFragment;
                    default:
                        return TextAnimateFragment.newInstance(mFontAnimate);
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


    private CallBackFunction mCallBackFunction;

    @Subscribe
    public void onEventModeRefresh(BaseMyMode baseMyMode) {
        if (mCallBackFunction != null) {
            Gson gson = new GsonBuilder().create();
            if (baseMyMode instanceof MyMode.TopArrayBean) {
                MyMode.TopArrayBean topArrayBean = (MyMode.TopArrayBean) baseMyMode;
                Logger.i("topArrayBean = " + topArrayBean.getImg());
                topArrayBean.setTopDate(UIUtils.getFormatDate("yyyy-MM-dd", System.currentTimeMillis()));
            }
            mCallBackFunction.onCallBack(gson.toJson(baseMyMode));
        }
    }

    private String mCurrenSrc;

    @Subscribe
    public void onEventModeRefresh(SongInfo listBean) {
        Log.i("TAG","");
        if (mCallBackFunction != null) {
            if (TextUtils.isEmpty(listBean.getUrl())) {
                mCurrenSrc = "";
                mMyMw.callHandler("cancelYinyue", "", new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        l("data = " + data);
                    }
                });
            } else {
                Logger.i("添加 =" + listBean.getUrl());
                mCurrenSrc = listBean.getUrl();
                Log.i("TAG","-----"+listBean.toString());
                mCallBackFunction.onCallBack(mCurrenSrc);
                mMyMw.loadUrl("javascript:(function() { var videos = document.getElementsByTagName('audio'); for(var i=0;i<videos.length;i++){videos[i].play();}})()");
            }
        }
    }

    private void initJavaOpt() {

        mMyMw.registerHandler("loadOver", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                mLoadingAlertDialog.dismiss();
            }
        });


        mMyMw.registerHandler("choiceAd", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                baseStartActivity(MyModeHtmlActivity.class);
                mCallBackFunction = function;
            }
        });


        mMyMw.registerHandler("choiceImg", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                mOptImgPopWindow.showAtLocation(mMyMw, Gravity.BOTTOM, 0, 0);
                mCallBackFunction = function;
            }
        });


        mMyMw.registerHandler("choiceMusic", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                mCallBackFunction = function;
                Intent intent = new Intent(PagerEditActivity.this, MusicActivity.class);
                intent.putExtra(MusicActivity.KEY_SRC, mCurrenSrc);
                startActivity(intent);
                mMyMw.loadUrl("javascript:(function() { var videos = document.getElementsByTagName('audio'); for(var i=0;i<videos.length;i++){videos[i].pause();}})()");
            }
        });


        mMyMw.registerHandler("modifyText", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String text = jsonObject.getString("param");
                    mEtContent.setText(text);
                    mflytFont.setVisibility(View.VISIBLE);
                    mEtContent.setTextColor(getResources().getColor(R.color.black));
                    if (mY < mMyMw.getHeight() / 2) {
                        mLlytFont.removeView(mLlytStyle);
                        mLlytFont.addView(mLlytStyle, 1);
                        mLlytFont.setY(mY - UIUtils.dip2px(20));
                    } else {
                        mLlytFont.removeView(mLlytStyle);
                        mLlytFont.addView(mLlytStyle, 0);
                        mLlytFont.setY(mY - UIUtils.dip2px(129) - UIUtils.dip2px(20));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public int createSuccessView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_pager_edit;
    }


    private void initPpwWindow() {
        //设置contentView
        View contentView = UIUtils.inflate(PagerEditActivity.this, R.layout.ppw_mysetting);
        mOptImgPopWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mOptImgPopWindow.setContentView(contentView);
        mOptImgPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mOptImgPopWindow.setOutsideTouchable(true);
        //设置各个控件的点击响应
        TextView tv1 = (TextView) contentView.findViewById(R.id.tvCamera);
        TextView tv2 = (TextView) contentView.findViewById(R.id.tvPhotos);
        TextView tv3 = (TextView) contentView.findViewById(R.id.tvCancle);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
    }


    private void uploadPager(String content) {

        mLoadingAlertDialog.show();
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().doSave((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),content, mTitle, mWebSite, mImgPath, 0, mPageId, 1)).subscribe(new Subscriber<SavePagerResult>() {
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
            public void onNext(SavePagerResult baseBean) {
                if (baseBean.isState()) {
                    EventBus.getDefault().post(new DraftEvent());
                    finish();
                } else {
                    showTest(mServerEror);
                }

            }
        });
        addSubscription(sbMyAccount);
    }


    public String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String wrapHtml(String htmlContent) {
        htmlContent = htmlContent.replace(SERVERSTYLE, "");
        htmlContent = htmlContent.replace(SERVERMUSIC, "img/app/music.png");
        htmlContent = htmlContent.replace(HEADTRAP, "");
        htmlContent = htmlContent.replace(TAILTRAP, "");
        Logger.i("SERVERMUSIC ================================= " + SERVERMUSIC);
        StringBuffer bufferContent = new StringBuffer(htmlContent);
        String prefix = "<head>";
        int index = bufferContent.indexOf(prefix);
        mStrAdd = new StringBuffer();

        mStrAdd.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/app/style.css\"> ");
        mStrAdd.append("<script src=\"js/jquery-2.1.4.min.js\" type=\"text/javascript\"></script> ");
        mStrAdd.append("<link rel=\"stylesheet\" href=\"css/images/style.css\"> ");
        //    mStrAdd.append("<script src=\"js/localResizeIMG-master/dist/lrz.bundle.js\"></script> ");
        mStrAdd.append(" <script src=\"js/device.js\"></script> ");
        mStrAdd.append("<script src=\"js/editor.js?t=1\"></script> ");
        mStrAdd.append(" <script src=\"js/initJs.js\"></script> ");
/*        mStrAdd.append("<script src=\"js/images/sonic.js\"></script> ");
        mStrAdd.append("<script src=\"js/images/comm.js\"></script> ");
        mStrAdd.append("<script src=\"js/images/hammer.js\"></script> ");
        mStrAdd.append("<script src=\"js/images/iscroll-zoom.js\"></script> ");
        mStrAdd.append("<script src=\"js/images/jquery.photoClip.js\"></script> ");*/
        bufferContent.insert(index + prefix.length(), mStrAdd.toString());
        /*  if (TextUtils.isEmpty(mPageId)) {
          String sufffix = "</body>";
            int sufffixIndex = bufferContent.indexOf(sufffix);
            String musicJs = "<script type=\"text/javascript\">\n" +
                    "\t$(\".zhanting\").click(function(event){\n" +
                    "\t\tvar music=document.getElementById(\"music\");\n" +
                    "\t\tif(music.paused)\n" +
                    "\t\t{\n" +
                    "\t\t\tmusic.play();\n" +
                    "\t\t\t$(this).find(\"img\").addClass(\"bofang\");\n" +
                    "\t\t}\n" +
                    "\t\telse\n" +
                    "\t\t{\n" +
                    "\t\t\tmusic.pause();\n" +
                    "\t\t\t$(this).find(\"img\").removeClass(\"bofang\");\n" +
                    "\t\t}\n" +
                    "\t\tevent.stopImmediatePropagation();\n" +
                    "\t});\n" +
                    "</script>";
            //  bufferContent.insert(bufferContent.length(), musicJs);
        }*/
        return bufferContent.toString();
    }

    public String wrapContent(String data) {
        data = data.replace("img/app/music.png", SERVERMUSIC);
        StringBuffer bufferContent = new StringBuffer(data);
        String prefix = "<head>";
        int index = bufferContent.indexOf(prefix);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(SERVERSTYLE);
        bufferContent.insert(index + prefix.length(), stringBuffer.toString());
        String music = "\n" +
                "<script type=\"text/javascript\">\n" +
                "  //暂停/播放 $(\".zhanting\")\n" +
                "  $(\"#zhanting\").click(function (event) {\n" +
                "    var music = document.getElementById(\"music\");\n" +
                "    if (music.paused) {\n" +
                "      music.play();\n" +
                "      $(this).find(\"img\").addClass(\"bofang\");\n" +
                "    }\n" +
                "    else {\n" +
                "      music.pause();\n" +
                "      $(this).find(\"img\").removeClass(\"bofang\");\n" +
                "    }\n" +
                "    event.stopImmediatePropagation();\n" +
                "  });\n" +
                "</script>\n";
        bufferContent.append(music);
        return bufferContent.toString();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.e("resultCode:"+resultCode+"\t"+ Activity.RESULT_OK+"\t"+data.toString()+"\t"+requestCode);
        if (resultCode == Activity.RESULT_OK
                && data != null
                && (requestCode == REQUEST_CLIP_IMAGE)) {//图片剪切 并且请求码和返回码相对于，当点击取消剪切按钮时，到else 处理条件情况
            String path = PhotoActionHelper.getOutputPath(data);
            if (path != null) {
                final Uri uri = Uri.parse("file:/" + path);

                mPath = path;
                uploadImg();
            }
            return;
        }else{
            String path = PhotoActionHelper.getInputPath(data);
            if (path != null) {
                final Uri uri = Uri.parse("file:/" + path);

                mPath = path;
                uploadImg();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void uploadImg() {
        File file=ImageUtil.scal(mPath);
        mLoadingAlertDialog.show();
        Subscription sbUpload = wrapObserverWithHttp(WorkService.getWorkService().otherImagUpload((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),ImageUtil.getInstance().wrapUploadImgRequest(file))).subscribe(new Subscriber<ImageUpload>() {
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
            public void onNext(ImageUpload imageUpload) {
                if (!TextUtils.isEmpty(imageUpload.getImgUrl())) {
                    mCallBackFunction.onCallBack(imageUpload.getImgUrl());
                } else {
                    showTest(mServerEror);
                }

            }
        });
        addSubscription(sbUpload);
    }

    @Override
    protected void onDestroy() {
        if (mMyMw != null) {
            mMyMw.destroy();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    AlertDialog alertDialog = null;

    @Override
    public void onBackPressed() {

        alertDialog = UIUtils.getAlertDialog(this, "温馨提示", "即将推出，需要保存到草稿箱吗?", "保存", "放弃", 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                mMyMw.callHandler("getHtmlContent", "", new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        data = data.replace(mStrAdd.toString(), "");
                        data = wrapContent(data);
                        uploadPager(data);
                    }
                });

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });
        alertDialog.show();

    }


 /*   @Override
    // 设置回退  跳转浏览器
    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
  *//*      if ((keyCode == KeyEvent.KEYCODE_BACK) && mMyMw.canGoBack()) {
            mMyMw.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);*//*
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCamera:
                ImageUtil.getInstance().openCamera(mOnhanlderResultCallback);
                break;
            case R.id.tvPhotos:
                ImageUtil imageUtil = ImageUtil.getInstance();
                imageUtil.chooseImage("开始制作",mOnhanlderResultCallback, 1);
                break;
            case R.id.tvCancle:
                mOptImgPopWindow.dismiss();
                break;
            case R.id.ivConfirm:
                setText(mEtContent.getText().toString());
                break;
            case R.id.ivCancle:
                setCancle();
                mCbStyle.setChecked(false);
                break;
            case R.id.tvClear:
                mEtContent.setText("");
                break;
        }
    }

    public void setFontFamily(String fontFamily) {
        mFontFamily = fontFamily;
        mMyMw.callHandler("setFontFamily", fontFamily, new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                l("data = " + data);
            }
        });
    }


    public void setFontColor(String fontColor) {
        mFontColor = fontColor;
        mEtContent.setTextColor(Color.parseColor(fontColor));
        mMyMw.callHandler("setFontColor", fontColor, new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                l("data = " + data);
            }
        });
    }

    public void setFontSize(float size) {
        mFontSize = size;
        l("size = " + size);
        mMyMw.callHandler("setFontSize", String.valueOf(size), new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                l("data = " + data);
            }
        });
    }

    public void setFontAnimation(int index) {
        mFontAnimate = index;
        Logger.i("index = " + index);
        mMyMw.callHandler("setFontAnimation", String.valueOf(index), new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                l("data = " + data);
            }
        });

    }

    @Override
    public void setText(String text) {
        mMyMw.callHandler("fontConfirm", text, new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                l("data = " + data);
            }
        });
        mflytFont.setVisibility(View.GONE);
    }

    public void setCancle() {
        mMyMw.callHandler("fontCancle", "", new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                l("data = " + data);
            }
        });
        mflytFont.setVisibility(View.GONE);
    }

    @Override
    public void setAlignStyle(int index) {
        mMyMw.callHandler("addFontAlign", String.valueOf(index), new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                l("data = " + data);
            }
        });
    }

    class MyBridgeWebViewClient extends BridgeWebViewClient {

        public MyBridgeWebViewClient(BridgeWebView webView) {
            super(webView);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                String myUrl = URLDecoder.decode(url, "UTF-8");
                if (!myUrl.startsWith(YY_RETURN_DATA) && !myUrl.startsWith(YY_OVERRIDE_SCHEMA) && !myUrl.startsWith(YY_SCHEMA) && !myUrl.startsWith("file://")) { // 如果是返回数据s
                    Intent intent = new Intent(PagerEditActivity.this, PageLookActivity.class);
                    intent.putExtra(PageLookActivity.KEY_URL, myUrl);
                    PagerEditActivity.this.startActivity(intent);
                    return true;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Logger.i("onPageFinishedonPageFinishedonPageFinishedonPageFinishedonPageFinished");
        }
    }
//
//    // /////////////////////////////////////////
//    // 向webview发出信息
//    private void enableX5FullscreenFunc() {
//
//        if (mMyMw.getX5WebViewExtension() != null) {
//            Toast.makeText(this, "开启X5全屏播放模式", Toast.LENGTH_LONG).show();
//            Bundle data = new Bundle();
//
//            data.putBoolean("standardFullScreen", false);// true表示标准全屏，false表示X5全屏；不设置默认false，
//
//            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，
//
//            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1
//
//            mMyMw.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
//                    data);
//        }
//    }
//
//    private void disableX5FullscreenFunc() {
//        if (mMyMw.getX5WebViewExtension() != null) {
//            Toast.makeText(this, "恢复webkit初始状态", Toast.LENGTH_LONG).show();
//            Bundle data = new Bundle();
//
//            data.putBoolean("standardFullScreen", true);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，
//
//            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，
//
//            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1
//
//            mMyMw.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
//                    data);
//        }
//    }
//
//    private void enableLiteWndFunc() {
//        if (mMyMw.getX5WebViewExtension() != null) {
//            Toast.makeText(this, "开启小窗模式", Toast.LENGTH_LONG).show();
//            Bundle data = new Bundle();
//
//            data.putBoolean("standardFullScreen", false);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，
//
//            data.putBoolean("supportLiteWnd", true);// false：关闭小窗；true：开启小窗；不设置默认true，
//
//            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1
//
//            mMyMw.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
//                    data);
//        }
//    }
//
//    private void enablePageVideoFunc() {
//        if (mMyMw.getX5WebViewExtension() != null) {
//            Toast.makeText(this, "页面内全屏播放模式", Toast.LENGTH_LONG).show();
//            Bundle data = new Bundle();
//
//            data.putBoolean("standardFullScreen", false);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，
//
//            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，
//
//            data.putInt("DefaultVideoScreen", 1);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1
//
//            mMyMw.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
//                    data);
//        }
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        // TODO Auto-generated method stub
//        try {
//            super.onConfigurationChanged(newConfig);
//            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//
//            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
}
