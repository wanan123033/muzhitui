package com.nevermore.muzhitui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nevermore.muzhitui.MainActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.rongyun.MztRongContext;
import com.nevermore.muzhitui.event.ReadCountEvent;
import com.nevermore.muzhitui.fragment.TabMyFragment;
import com.nevermore.muzhitui.module.bean.DynamicBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.orhanobut.logger.Logger;
import com.tencent.smtt.sdk.WebSettings;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.ImageUtil;
import base.MyLogger;
import base.SPUtils;
import base.UIUtils;
import base.network.RetrofitUtil;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
import base.thread.BaseRunnable;
import base.thread.ThreadManager;
import base.util.ShareUtil;
import base.view.LoadingAlertDialog;
import base.view.RoundImageView;
import base.view.WebViewJavaScriptFunction;
import base.view.X5WebView;
import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/9/13.
 * 生成拇指推宣传图片预览
 */

public class SnapshotActivity extends BaseActivityTwoV{

    public static final String RIGHT_STATE = "right_state";  //转发保存  0转发 1 保存
    public static final String DYNAMIC_CONTENT = "dynamic_content"; //动态内容
    public static final String WEB_URL = "web_url";  //做网页截图时的图片路径
    public static final String IMAGES_PATH = "images_path"; //拼图时多张图片的路径级
    @BindView(R.id.tv_content_top)
    TextView tv_content_top;
    @BindView(R.id.tv_content_bottom)
    TextView tv_content_bottom;
    @BindView(R.id.ll_rootView)
    LinearLayout ll_rootView;
    @BindView(R.id.civ_topic)
    CircleImageView civ_topic;
    @BindView(R.id.iv_qrcode)
    ImageView iv_qrcode;
    @BindView(R.id.tv_username)
    TextView tv_username;
    @BindView(R.id.wv_net_snap)
    X5WebView wv_net_snap;
    @BindView(R.id.sb_padding)
    SeekBar sb_padding;
    @BindView(R.id.sb_margin)
    SeekBar sb_margin;
    @BindView(R.id.sb_contors)
    SeekBar sb_contors;
    @BindView(R.id.rv_img)
    RecyclerView rv_img;
    @BindView(R.id.ll_menu)
    LinearLayout ll_menu;
    @BindView(R.id.ll_tools)
    LinearLayout ll_tools;
    @BindView(R.id.rel_setText)
    RelativeLayout rel_setText;
    @BindView(R.id.tv_topTitle)
    TextView tv_topTitle;
    @BindView(R.id.tv_bottomTitle)
    TextView tv_bottomTitle;
    @BindView(R.id.rel_topic)
    RelativeLayout rel_topic;
    @BindView(R.id.tv_qr)
    TextView tv_qr;
    @BindView(R.id.tv_floor)
    TextView tv_floor;
    @BindView(R.id.iv_check_top)
    ImageView iv_check_top;
    @BindView(R.id.iv_check_bottom)
    ImageView iv_check_bottom;
    @BindView(R.id.ll_imgContent)
    LinearLayout ll_imgContent;
    @BindView(R.id.iv_floor)
    ImageView iv_floor;
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.view)
    View view1;
    @BindView(R.id.view1)
    View view;
    @BindView(R.id.tv_floor1)
    TextView tv_qu;
    @BindView(R.id.iv_floor1)
    ImageView iv_floor1;
    @BindView(R.id.ll_menu1)
    LinearLayout ll_menu1;

    public List<DynamicBean.Pic> urls = new ArrayList<>();

    private LoadingAlertDialog mLoadingAlertDialog;
    private DynamicBean.Dynamic dynamic;
    private String webUrl;
    private List<String> imagesPath;

    private ImgAdapter mAdapter;
    private int isTitle = 1;
    private int floor = 1;
    private AlertDialog alertDialog;

    @Override
    public void init() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        wv_net_snap.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        showBack();
        setMyTitle("快照预览");
        int state = getIntent().getIntExtra(RIGHT_STATE, 0);

        webUrl = getIntent().getStringExtra(WEB_URL);
        
        imagesPath = getIntent().getStringArrayListExtra(IMAGES_PATH);

        dynamic = (DynamicBean.Dynamic) getIntent().getSerializableExtra(DYNAMIC_CONTENT);
        initView();
        if(state == 0) {
            showRight("转发", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(dynamic != null && TextUtils.isEmpty(webUrl) && (imagesPath == null || imagesPath.isEmpty())) {   //动态快照
                        snapshot(dynamic.getId());
                    }else if (!TextUtils.isEmpty(webUrl) && dynamic == null && (imagesPath == null || imagesPath.isEmpty())){  //网页截图

                    }else if((imagesPath != null && !imagesPath.isEmpty()) && TextUtils.isEmpty(webUrl) && dynamic == null){   //拼图

                    }
                }
            });
        }else if(state == 5){

        }else {
            showRight("保存", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(dynamic != null && TextUtils.isEmpty(webUrl) && (imagesPath == null || imagesPath.isEmpty())) {   //动态快照
                        saveSnapshot(dynamic.getId());
                    }else if (!TextUtils.isEmpty(webUrl) && dynamic == null && (imagesPath == null || imagesPath.isEmpty())){  //网页截图
                        saveSnapshot();
                    }else if((imagesPath != null && !imagesPath.isEmpty()) && TextUtils.isEmpty(webUrl) && dynamic == null){   //拼图
                        saveSnapshot();
                    }
                }
            });
        }
        rv_img.setHasFixedSize(true);
        rv_img.setNestedScrollingEnabled(false);
        sb_padding.setOnSeekBarChangeListener(seekBarChangeListener);
        sb_margin.setOnSeekBarChangeListener(seekBarChangeListener);
        sb_contors.setOnSeekBarChangeListener(seekBarChangeListener);

        initWeb();
    }

    private void initWeb() {
        wv_net_snap.getSettings().setPluginState(WebSettings.PluginState.ON);
        wv_net_snap.getSettings().setJavaScriptEnabled(true);
        wv_net_snap.getSettings().setAppCacheEnabled(false);
        wv_net_snap.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wv_net_snap.getSettings().setUseWideViewPort(true);
        wv_net_snap.getSettings().setLoadWithOverviewMode(true);
        wv_net_snap.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        wv_net_snap.addJavascriptInterface(new WebViewJavaScriptFunction() {

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
    }

    private void saveSnapshot() {
        mLoadingAlertDialog.show();
        ThreadManager.getInstance().run(new BaseRunnable() {
            @Override
            public void run() {
                Bitmap bitmap = UIUtils.loadBitmapFromView(ll_rootView);
                if (bitmap != null) {
                    try {
                        final String path = ImageUtil.saveImageToGallery(getApplicationContext(), bitmap);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showTest("已保存至" + path);
                                mLoadingAlertDialog.dismiss();
                            }
                        });
                    }catch (SecurityException e) {
                        e.printStackTrace();
                        alertDialog3 = UIUtils.getAlertDialog(SnapshotActivity.this, "权限设置", "请允许拇指推APP访问您的通讯录", null, "现在去设置", 0, null, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog3.dismiss();
                                if (Build.VERSION.SDK_INT >= 23) {
                                    requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION","android.permission.WRITE_EXTERNAL_STORAGE","android.permission.READ_EXTERNAL_STORAGE","android.permission.INTERNET"},1024);
                                }
                            }
                        });
                        alertDialog3.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void run(Object... objs) {
                super.run(objs);
            }
        });
    }

    private void saveSnapshot(final int id) {
        File file = new File((String) SPUtils.get(id+"",""));
        if(!file.exists()) {
            mLoadingAlertDialog.show();
            ThreadManager.getInstance().run(new BaseRunnable() {
                @Override
                public void run() {
                    Bitmap bitmap = UIUtils.loadBitmapFromView(ll_rootView);
                    if (bitmap != null) {
                        try {
                            final String path = ImageUtil.saveImageToGallery(getApplicationContext(), bitmap);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showTest("已保存至" + path);
                                    SPUtils.put(id + "", path);
                                    mLoadingAlertDialog.dismiss();
                                }
                            });
                        }catch (SecurityException e) {
                            e.printStackTrace();
                            alertDialog3 = UIUtils.getAlertDialog(SnapshotActivity.this, "权限设置", "请允许拇指推APP访问您的通讯录", null, "现在去设置", 0, null, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog3.dismiss();
                                    if (Build.VERSION.SDK_INT >= 23) {
                                        requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION","android.permission.WRITE_EXTERNAL_STORAGE","android.permission.READ_EXTERNAL_STORAGE","android.permission.INTERNET"},1024);
                                    }
                                }
                            });
                            alertDialog3.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void run(Object... objs) {
                    super.run(objs);
                }
            });
        }else {

            showTest("已保存至" + file.getAbsolutePath());
        }
    }
    private void initView() {
        if(dynamic != null && TextUtils.isEmpty(webUrl) && (imagesPath == null || imagesPath.isEmpty())) {   //动态快照
            initDynamicData();
        }else if (!TextUtils.isEmpty(webUrl) && dynamic == null && (imagesPath == null || imagesPath.isEmpty())){  //网页截图
            initWebPage();
        }else if((imagesPath != null && !imagesPath.isEmpty()) && TextUtils.isEmpty(webUrl) && dynamic == null){   //拼图
            initPuzzle();
        }

    }

    /**
     * 初始化拼图
     */
    private List<RoundImageView> puzzleImgViews = new ArrayList<>();
    private void initPuzzle() {
        view1.setVisibility(View.GONE);
        ll_menu1.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
        tv_content_bottom.setVisibility(View.GONE);
        tv_content_top.setVisibility(View.GONE);
        ll_menu.setVisibility(View.VISIBLE);
        rv_img.setVisibility(View.VISIBLE);
//        ll_imgContent.setVisibility(View.GONE);
        if (mAdapter == null){
            mAdapter = new ImgAdapter(this,new ArrayList<String>());
        }
        rv_img.setAdapter(mAdapter);
        mAdapter.addDate(imagesPath);
        initData();
        if(mLoadingAlertDialog != null){
            mLoadingAlertDialog.dismiss();
        }



//        puzzleImgViews.clear();
//        for (int i = 0; i < imagesPath.size(); i++) {
//            RoundImageView iv = new RoundImageView(this);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            params.setMargins(0, 12, 0, 12);
//            iv.setScaleType(ImageView.ScaleType.FIT_XY);
//            iv.setAdjustViewBounds(true);
//            iv.setLayoutParams(params);
//            ll_imgContent.addView(iv);
//            if (i < imagesPath.size() - 1) {
//                View view = new View(this);
//                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
//                view.setLayoutParams(params2);
//                view.setBackgroundColor(Color.parseColor("#EDEDF1"));
//                ll_imgContent.addView(view);
//            }
//            puzzleImgViews.add(iv);
//            MyLogger.kLog().e(imagesPath.get(i));
//            ImageLoader.getInstance().displayImage(Uri.fromFile(new File(imagesPath.get(i))).toString(), iv, ImageUtil.getInstance().getBaseDisplayOption(), listener, progressListener);
//        }
//        initData();
//        if(mLoadingAlertDialog != null){
//            mLoadingAlertDialog.dismiss();
//        }
    }

    /**
     * 初始化网页截图
     */
    private void initWebPage() {
        view1.setVisibility(View.VISIBLE);
        ll_menu1.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
        tv_content_bottom.setVisibility(View.GONE);
        tv_content_top.setVisibility(View.GONE);
        rv_img.setVisibility(View.GONE);
        wv_net_snap.setVisibility(View.VISIBLE);
//        ll_imgContent.setVisibility(View.GONE);
        wv_net_snap.loadUrl(webUrl);
        tv_content_top.setVisibility(View.GONE);
        initData();
        if(mLoadingAlertDialog != null){
            mLoadingAlertDialog.dismiss();
        }
    }

    /**
     * 初始化动态快照
     */
    private void initDynamicData() {
        view1.setVisibility(View.GONE);
        ll_menu1.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        tv_content_bottom.setVisibility(View.GONE);
        tv_content_top.setVisibility(View.VISIBLE);
        wv_net_snap.setVisibility(View.GONE);
        mLoadingAlertDialog.show();
        if (!TextUtils.isEmpty(dynamic.getContent()))
            tv_content_top.setText(dynamic.getContent());
        else {
            tv_content_top.setText("");
        }
        urls.clear();
        if(dynamic != null)
            urls.addAll(dynamic.getPics());
        ll_imgContent.removeAllViews();
        for (int i = 0; i < urls.size(); i++) {
            RoundImageView iv = new RoundImageView(this);
            iv.setBackgroundResource(R.drawable.snap_corners);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 12, 0, 12);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setAdjustViewBounds(true);
            iv.setLayoutParams(params);
            ll_imgContent.addView(iv);
            if (i < urls.size() - 1) {
                View view = new View(this);
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
                view.setLayoutParams(params2);
                view.setBackgroundColor(Color.parseColor("#EDEDF1"));
                ll_imgContent.addView(view);
            }
            MyLogger.kLog().e(urls.get(i).getPage_picpath());
            ImageLoader.getInstance().displayImage(urls.get(i).getPage_picpath().replace("_s", ""), iv, ImageUtil.getInstance().getBaseDisplayOption(), listener, progressListener);
        }
        initData();
    }

    private void initData() {
        String headimg = RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + SPUtils.get(SPUtils.KEY_TOPIC, "");
        ImageLoader.getInstance().displayImage(headimg, civ_topic);
        String path = (String) SPUtils.get(SPUtils.qr_code_img, "");
        if (!TextUtils.isEmpty(path)) {
            ImageLoader.getInstance().displayImage(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + path, iv_qrcode, ImageUtil.getInstance().getBaseDisplayOption(), listener, progressListener);
        }
        String username = (String) SPUtils.get(SPUtils.KEY_USERNAME, "");
        tv_username.setText(username);
    }

    private AlertDialog alertDialog3;
    private void snapshot(final int id) {
        mLoadingAlertDialog.show();
        final File file = new File((String) SPUtils.get(id+"",""));
        if(!file.exists()) {
            ThreadManager.getInstance().run(new BaseRunnable() {
                @Override
                public void run() {
                    Bitmap bitmap = UIUtils.loadBitmapFromView(ll_rootView);
                    if (bitmap != null) {
                        try {
                            File file = new File(ImageUtil.saveImageToGallery(bitmap));
                            //TODO 跳转到微信分享
                            List<File> files = new ArrayList<>();
                            files.add(file);
                            if(dynamic != null)
                            startActivity(ShareUtil.wxzf(dynamic.getContent(), files));
                            SPUtils.put(id+ "", file.getAbsolutePath());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mLoadingAlertDialog.dismiss();
                                }
                            });
                        } catch (SecurityException e) {
                            e.printStackTrace();
                            alertDialog3 = UIUtils.getAlertDialog(SnapshotActivity.this, "权限设置", "请允许拇指推APP访问您的通讯录", null, "现在去设置", 0, null, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog3.dismiss();
                                    if (Build.VERSION.SDK_INT >= 23) {
                                        requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION","android.permission.WRITE_EXTERNAL_STORAGE","android.permission.READ_EXTERNAL_STORAGE","android.permission.INTERNET"},1024);
                                    }
                                }
                            });
                            alertDialog3.show();
                        }catch (Exception e){
                            e.printStackTrace();
                            showTest("跳转微信出现异常，请稍后再试");
                        }

                    }
                }

                @Override
                public void run(Object... objs) {
                    super.run(objs);
                }
            });
        }else {
            ThreadManager.getInstance().run(new BaseRunnable() {
                @Override
                public void run() {
                    try {
                        List<File> files = new ArrayList<>();
                        files.add(file);
                        if(dynamic != null)
                        startActivity(ShareUtil.wxzf(dynamic.getContent(), files));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mLoadingAlertDialog.dismiss();
                            }
                        });
                    }catch (SecurityException e) {
                        e.printStackTrace();
                        alertDialog3 = UIUtils.getAlertDialog(SnapshotActivity.this, "权限设置", "请允许拇指推APP访问您的通讯录", null, "现在去设置", 0, null, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog3.dismiss();
                                if (Build.VERSION.SDK_INT >= 23) {
                                    requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION","android.permission.WRITE_EXTERNAL_STORAGE","android.permission.READ_EXTERNAL_STORAGE","android.permission.INTERNET"},1024);
                                }
                            }
                        });
                        alertDialog3.show();
                    }
                }
            });
        }
    }
    @Override
    public int createSuccessView() {
        return R.layout.activity_snapshot;
    }

    private int index;
    private ImageLoadingListener listener = new ImageLoadingListener() {

        @Override
        public void onLoadingStarted(String imageUri, View view) {
            if(mLoadingAlertDialog != null)
                mLoadingAlertDialog.show();
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            if(mLoadingAlertDialog != null)
                mLoadingAlertDialog.dismiss();
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if(mLoadingAlertDialog != null){
                index++;
                if(dynamic != null)
                if (index >= dynamic.getPics().size()+1){
                    mLoadingAlertDialog.dismiss();
                }
            }

        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {
            if(mLoadingAlertDialog != null)
                mLoadingAlertDialog.dismiss();
        }
    };

    private ImageLoadingProgressListener progressListener = new ImageLoadingProgressListener() {
        @Override
        public void onProgressUpdate(String imageUri, View view, int current, int total) {

        }

    };


    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            switch (seekBar.getId()){
                case R.id.sb_padding:
                    if (mAdapter != null){
                        mAdapter.setPadding(progress);
                    }
                    break;
                case R.id.sb_margin:
                    if (mAdapter != null){
                        mAdapter.setMargin(progress);
                    }
                    break;
                case R.id.sb_contors:
                    if (mAdapter != null){
                        mAdapter.setCorners(progress);
                    }
                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    @OnClick({R.id.ll_border,R.id.tv_jwhy,R.id.ll_topTitle,R.id.ll_bottomTitle,R.id.btn_settingText,R.id.ll_floor,R.id.ll_floor1,R.id.ll_border1})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_border: //调节边框
                rel_setText.setVisibility(View.GONE);
                if(ll_tools.getVisibility() == View.GONE){
                    ll_tools.setVisibility(View.VISIBLE);
                }else {
                    ll_tools.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_jwhy:  //设置文字
                ll_tools.setVisibility(View.GONE);
                if(rel_setText.getVisibility() == View.GONE){
                    rel_setText.setVisibility(View.VISIBLE);
                }else {
                    rel_setText.setVisibility(View.GONE);
                }
                break;
            case R.id.ll_topTitle:
                tv_topTitle.setTextColor(getResources().getColor(R.color.orange));
                tv_bottomTitle.setTextColor(getResources().getColor(R.color.gray));
                iv_check_bottom.setImageResource(R.mipmap.iv_uncheck_title);
                iv_check_top.setImageResource(R.mipmap.iv_check_title);
                isTitle = 1;
                break;
            case R.id.ll_bottomTitle:
                tv_bottomTitle.setTextColor(getResources().getColor(R.color.orange));
                tv_topTitle.setTextColor(getResources().getColor(R.color.gray));
                iv_check_top.setImageResource(R.mipmap.iv_uncheck_title);
                iv_check_bottom.setImageResource(R.mipmap.iv_check_title);
                isTitle = 2;
                break;
            case R.id.btn_settingText:
                String text =et_content.getText().toString().trim();
                if(isTitle == 1){
                    tv_content_top.setVisibility(View.VISIBLE);
                    tv_content_top.setText(text);
                }else if (isTitle == 2){
                    tv_content_bottom.setVisibility(View.VISIBLE);
                    tv_content_bottom.setText(text);
                }else {
                    showTest("请选择是上标题还是下标题");
                }
                break;
            case R.id.ll_floor:
                final int memberstate = (int) SPUtils.get(SPUtils.KEY_MEMBER_STATE, 0);//会员状态  1是年费2是终身3不是会员
                final int IsExpire = (int) SPUtils.get(SPUtils.KEY_ISEXPIRE, 0);//是否已过期  1是会员已过期 0是正常
                String message = "您目前还不是会员，无法去背景；请购买会员后即可去背景";
                String clickmessage = "购买会员";
                if (IsExpire == 1) {
                    message = "您的会员已到期，无法去背景；请续费后即可恢复正常使用";
                    clickmessage = "续费";
                }

                if (((memberstate == 3) || (IsExpire == 1))) {

                    alertDialog = UIUtils.getAlertDialog(SnapshotActivity.this, "提示信息", message, "取消", clickmessage, 0, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            finish();

                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            //finish();
                            baseStartActivity(MainActivity.class);
                            TabMyFragment.mIsBuy = true;
                            MztRongContext.getInstance().popAllActivity(3);
                        }
                    });
                    alertDialog.show();
                }else {
                    if (floor == 1) {
                        alertDialog = UIUtils.getAlertDialog(SnapshotActivity.this, "提示信息", "是否去掉背景？", "取消", "确定", 0, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();

                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                                rel_topic.setVisibility(View.GONE);
                                iv_qrcode.setVisibility(View.GONE);
                                tv_qr.setVisibility(View.GONE);
                                iv_floor.setImageResource(R.mipmap.iv_snap_bg);
                                tv_floor.setText("加背景");
                                floor = 0;

                            }
                        });
                        alertDialog.show();

                    } else {
                        alertDialog = UIUtils.getAlertDialog(SnapshotActivity.this, "提示信息", "是否加上背景？", "取消", "确定", 0, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();

                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                                rel_topic.setVisibility(View.VISIBLE);
                                iv_qrcode.setVisibility(View.VISIBLE);
                                tv_qr.setVisibility(View.VISIBLE);
                                iv_floor.setImageResource(R.mipmap.ic_delete_friends);
                                tv_floor.setText("去背景");
                                floor = 1;

                            }
                        });
                        alertDialog.show();

                    }
                }
                break;
            case R.id.ll_floor1:
                final int memberstate1 = (int) SPUtils.get(SPUtils.KEY_MEMBER_STATE, 0);//会员状态  1是年费2是终身3不是会员
                final int IsExpire1 = (int) SPUtils.get(SPUtils.KEY_ISEXPIRE, 0);//是否已过期  1是会员已过期 0是正常
                String message1 = "您目前还不是会员，无法去背景；请购买会员后即可去背景";
                String clickmessage1 = "购买会员";
                if (IsExpire1 == 1) {
                    message1 = "您的会员已到期，无法去背景；请续费后即可恢复正常使用";
                    clickmessage1 = "续费";
                }

                if (((memberstate1 == 3) || (IsExpire1 == 1))) {

                    alertDialog = UIUtils.getAlertDialog(SnapshotActivity.this, "提示信息", message1, "取消", clickmessage1, 0, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            finish();

                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            //finish();
                            baseStartActivity(MainActivity.class);
                            TabMyFragment.mIsBuy = true;
                            MztRongContext.getInstance().popAllActivity(3);
                        }
                    });
                    alertDialog.show();
                }else {
                    if (floor == 1) {
                        alertDialog = UIUtils.getAlertDialog(SnapshotActivity.this, "提示信息", "是否去掉背景？", "取消", "确定", 0, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();

                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                                rel_topic.setVisibility(View.GONE);
                                iv_qrcode.setVisibility(View.GONE);
                                tv_qr.setVisibility(View.GONE);
                                iv_floor1.setImageResource(R.mipmap.iv_snap_bg);
                                tv_qu.setText("加背景");
                                floor = 0;

                            }
                        });
                        alertDialog.show();

                    } else {
                        alertDialog = UIUtils.getAlertDialog(SnapshotActivity.this, "提示信息", "是否加上背景？", "取消", "确定", 0, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();

                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                                rel_topic.setVisibility(View.VISIBLE);
                                iv_qrcode.setVisibility(View.VISIBLE);
                                tv_qr.setVisibility(View.VISIBLE);
                                iv_floor1.setImageResource(R.mipmap.ic_delete_friends);
                                tv_qu.setText("去背景");
                                floor = 1;

                            }
                        });
                        alertDialog.show();

                    }
                }
                break;
            case R.id.ll_border1:
                saveSnapshot();
                break;
        }
    }

    public static class ImgAdapter extends CommonAdapter<String>{
        private float corners;  //圆角度
        private int padding;  //内边距
        private int margin;   //外边距

        public ImgAdapter(Context context, List<String> datas) {
            super(context, R.layout.item_snap, datas);
        }

        @Override
        public void convert(ViewHolder holder, String url) {
            RoundImageView riv =  holder.getView(R.id.iv_imgContent);
            ImageLoader.getInstance().displayImage(Uri.fromFile(new File(url)).toString(),riv);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMarginStart(margin);
            params.setMarginEnd(margin);
            params.topMargin = padding;
            params.bottomMargin = padding;
            riv.setLayoutParams(params);
            riv.setRound(corners);

        }

        public void setCorners(float corners){
            MyLogger.kLog().e("corners = " + corners);
            this.corners = corners;


            notifyDataSetChanged();
        }
        public void setPadding(int padding) {
            MyLogger.kLog().e("padding = " + padding);
            this.padding = padding;
            notifyDataSetChanged();
        }

        public void setMargin(int margin) {
            MyLogger.kLog().e("margin = " + margin);
            this.margin = margin;
            notifyDataSetChanged();
        }
    }


    // /////////////////////////////////////////
    // 向webview发出信息
    private void enableX5FullscreenFunc() {

        if (wv_net_snap.getX5WebViewExtension() != null) {
            Toast.makeText(this, "开启X5全屏播放模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            wv_net_snap.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void disableX5FullscreenFunc() {
        if (wv_net_snap.getX5WebViewExtension() != null) {
            Toast.makeText(this, "恢复webkit初始状态", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", true);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            wv_net_snap.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void enableLiteWndFunc() {
        if (wv_net_snap.getX5WebViewExtension() != null) {
            Toast.makeText(this, "开启小窗模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", true);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            wv_net_snap.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void enablePageVideoFunc() {
        if (wv_net_snap.getX5WebViewExtension() != null) {
            Toast.makeText(this, "页面内全屏播放模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 1);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            wv_net_snap.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        wv_net_snap.resumeTimers();
    }
    @Override
    protected void onPause() {
        super.onPause();
        wv_net_snap.pauseTimers();
    }
    @Override
    protected void onDestroy() {
        if (wv_net_snap != null) {
            Logger.i("close");
            wv_net_snap.destroy();
        }
        super.onDestroy();
    }

}
