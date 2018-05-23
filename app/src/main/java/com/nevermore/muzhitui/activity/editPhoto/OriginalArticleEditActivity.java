package com.nevermore.muzhitui.activity.editPhoto;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.nevermore.muzhitui.MainActivity;
import com.nevermore.muzhitui.MusicActivity;
import com.nevermore.muzhitui.MyModeActivity;
import com.nevermore.muzhitui.PageLookActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.EditInfoActivity;
import com.nevermore.muzhitui.activity.MakeVideoActivity;
import com.nevermore.muzhitui.event.EditInfoEvent;
import com.nevermore.muzhitui.event.ObjectEvent;
import com.nevermore.muzhitui.event.VideoEvent;
import com.nevermore.muzhitui.module.bean.Bimp;
import com.nevermore.muzhitui.module.bean.MyMode;
import com.nevermore.muzhitui.module.bean.Original;
import com.nevermore.muzhitui.module.bean.Published;
import com.nevermore.muzhitui.module.bean.SongInfo;
import com.nevermore.muzhitui.module.bean.url;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
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
import base.view.LoadingAlertDialog;
import base.view.MyGridView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import rx.Subscriber;
import rx.Subscription;

public class OriginalArticleEditActivity extends BaseActivityTwoV implements OnClickListener, CompoundButton.OnCheckedChangeListener {


    @BindView(R.id.ivEidtArticleTitleImage)
    ImageView mIvEidtArticleTitleImage;
    @BindView(R.id.tvEidtArticleTitleName)
    TextView mTvEidtArticleTitleName;
    @BindView(R.id.tvEidtArticleMusic)
    TextView mTvEidtArticleMusic;
    @BindView(R.id.ivEidtArticleCharge)
    ImageView mIvEidtArticleCharge;
    @BindView(R.id.noScrollgridview)
    MyGridView mNoScrollgridview;
    @BindView(R.id.svEditArticle)
    ScrollView mSvEditArticle;
    @BindView(R.id.rlBack)
    RelativeLayout mRlBack;
    @BindView(R.id.tvFinishSend)
    TextView mTvFinishSend;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.llAddAdvertising)
    LinearLayout mLlAddAdvertising;
    @BindView(R.id.ivImageAdv)
    ImageView mIvImageAdv;

    @BindView(R.id.ivDelete)
    ImageView ivDelete;
    @BindView(R.id.llAdvertising)
    FrameLayout mLlAdvertising;
    @BindView(R.id.tbShowCardName)
    TextView tbShowCardName;
    @BindView(R.id.tbShowCardInfo)
    ToggleButton tbShowCardInfo;
    private SongInfo music = new SongInfo();
    private GridAdapter adapter;
    private LoadingAlertDialog mLoadingAlertDialog;


    private Original mOriginal;

    public static final int REQUEST_CLIP_IMAGEAll = 2027;
    public static final int REQUEST_CLIP_IMAGE = 2028;
    public static final String KEY_ID = "ID";
    private int mPosition = 0;//图片集合点击的item
    private String addAdvertisingImage = "";//添加广告图片
    private String addLink = "";//添加广告连接
    private GalleryFinal.OnHanlderResultCallback mOnhanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null && !resultList.isEmpty()) {
             String   inputPath = resultList.get(0).getPhotoPath();
          String      mOutputPath = new File(UIUtils.getExternalCacheDir(), System.currentTimeMillis() + ".jpg").getPath();
                PhotoActionHelper.clipImage(OriginalArticleEditActivity.this).input(inputPath).output(mOutputPath).setExtraHeight(UIUtils.dip2px(160)).maxOutputWidth(800)
                        .requestCode(REQUEST_CLIP_IMAGE).start();
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {

        }
    };
    private GalleryFinal.OnHanlderResultCallback mOnhanlderResultCallbackAll = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null && !resultList.isEmpty()) {
                Bimp.act_bool = false;
                for (int i = 0; i < resultList.size(); i++) {
                    String photoPath = resultList.get(i).getPhotoPath();
                    String mOutputPath = new File(UIUtils.getExternalCacheDir(), System.currentTimeMillis() + ".jpg").getPath();
                    CacheUtil.getInstance().add(CacheUtil.FORMAT_FRTE_TYPE,0);
                    PhotoActionHelper.clipImage(OriginalArticleEditActivity.this).input(photoPath).output(mOutputPath).setPosition(mPosition + i).setExtraHeight(UIUtils.dip2px(400)).maxOutputWidth(400)
                            .requestCode(REQUEST_CLIP_IMAGEAll).start();
                }

            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {

        }
    };
    private String addAdvFont;
    private int addAdvAnim;
    private float addAdvFontSize;
    private String addAdvColor;
    private String addAdvText;
    private int advertid;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        Init();
    }

    @Override
    public void init() {
        hideActionBar();
        mTitle.setText("原创修改");
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        initPpwWindow();


    }

    @Override
    public int createSuccessView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_selectimg;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearImage();
        EventBus.getDefault().unregister(this);

    }


    public void Init() {
        mSvEditArticle.smoothScrollTo(0, 0);
        mNoScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        mNoScrollgridview.setAdapter(adapter);
        String mPageId = getIntent().getStringExtra(KEY_ID);
        if (mPageId != null) {
            toPageUpdateMe(Integer.parseInt(mPageId));
        }
        tbShowCardInfo.setOnCheckedChangeListener(this);
    }

    private void toPageUpdateMe(int id) {
        Subscription sbGetCode = wrapObserverWithHttp(WorkService.getWorkService().toPageUpdateMe((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""), id)).subscribe(new Subscriber<Original>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                removeErrorView();
            }

            @Override
            public void onError(Throwable e) {
                removeLoadingView();
                showErrorView();
                e.printStackTrace();
            }

            @Override
            public void onNext(Original original) {
                if ("1".equals(original.getState())) {
                    mOriginal = original;
                    setEdit();
                    Log.i("TAG----","Bimp.bmp.size()==");
                    for (int i = 0; i < original.getPageMeDetails().size(); i++) {
                        Original.PageMeDetailsBean pageMeDetailsBean = original.getPageMeDetails().get(i);
                        ImageItem ii = new ImageItem();
                        ii.pageContent = pageMeDetailsBean.getPage_content();
                        if(ii.pageContent != null)
                            ii.txtText = ii.pageContent.replaceAll("<(.|\n)*?>","");
                        if(pageMeDetailsBean.getPage_type() == 1){//纯文字类型
                            ii.text = ii.pageContent;

                        }else if(pageMeDetailsBean.getPage_type() == 2){  //图文类型
                            ii.imageLoadPath = pageMeDetailsBean.getPage_picpath();
                            ii.text = ii.pageContent;
                        }else if(pageMeDetailsBean.getPage_type() == 3){  //视频类型
                            ii.url = pageMeDetailsBean.getPage_url();
                            ii.text = pageMeDetailsBean.getPage_content();
                            ii.imageLoadPath = pageMeDetailsBean.getPage_picpath();
                        }else if(pageMeDetailsBean.getPage_type() == 4){ //模板类型
                            ii.text = pageMeDetailsBean.getAdv_adtext();
                            ii.fontSize = pageMeDetailsBean.getAdv_size();
                            ii.fontfamily = pageMeDetailsBean.getAdv_font();
                            ii.imageLoadPath = pageMeDetailsBean.getPage_picpath();
                            ii.animate = pageMeDetailsBean.getAdv_animate();
                            ii.color = pageMeDetailsBean.getAdv_adcolor();
                            ii.url = pageMeDetailsBean.getPage_url();
                            ii.isVisable = false;
                            ii.txtText = ii.text;
                            ii.id = pageMeDetailsBean.getAdv_id();
                        }
                        ii.imageType = pageMeDetailsBean.getPage_type();
                        Log.e("TAG------",ii.toString());
                        Bimp.bmp.add(ii);
                        addLink = original.getAdv_url();
                        addAdvertisingImage = original.getAdv_pic();
                        addAdvFont = original.getAdv_font();
                        addAdvAnim = original.getAdv_animate();
                        addAdvFontSize = original.getAdv_size();
                        addAdvColor = original.getAdv_adcolor();
                        addAdvText = original.getAdv_adtext();
                        advertid = original.getAdv_id();


                    }
                    Log.i("TAG----","Bimp.bmp.size()==" +Bimp.bmp.size());
                    adapter.update();

                } else {
                    showTest("网络异常");
                }

            }
        });
        addSubscription(sbGetCode);
    }

    private void setEdit() {
        mTvEidtArticleTitleName.setText(mOriginal.getTitle());
        titleImageUrl = mOriginal.getTitle_pic();
        isShow=mOriginal.getInfo_show();
        Log.e("isShow：",isShow+"" +
                "\tinfo:"+mOriginal.getInfo_show());
        if (mOriginal.getInfo_show()==1) {
            tbShowCardName.setText("显示名片");
            tbShowCardInfo.setChecked(true);
            isShow=1;
        } else {
            tbShowCardName.setText("隐藏名片");
            tbShowCardInfo.setChecked(false);
            isShow=0;
        }

        if (mOriginal.getMusic_name() == null) {
            mTvEidtArticleMusic.setText("无背景音乐");
        } else {
            mTvEidtArticleMusic.setText(mOriginal.getMusic_name());
        }
        music.setSongName(mOriginal.getMusic_name());
        music.setUrl(mOriginal.getMusic_url());
        ImageLoader.getInstance().displayImage(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + mOriginal.getTitle_pic(), mIvEidtArticleTitleImage, ImageUtil.getInstance().getBaseDisplayOption());

        if (!TextUtils.isEmpty(mOriginal.getAdv_pic())) {
            ImageLoader.getInstance().displayImage(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + mOriginal.getAdv_pic(), mIvImageAdv, ImageUtil.getInstance().getBaseDisplayOption());
            mLlAddAdvertising.setVisibility(View.GONE);
            mLlAdvertising.setVisibility(View.VISIBLE);
            addAdvertisingImage = mOriginal.getAdv_pic();
            addLink = mOriginal.getAdv_url();
        } else {
            mLlAddAdvertising.setVisibility(View.VISIBLE);
            mLlAdvertising.setVisibility(View.GONE);
        }

    }

    /***
     * 视频添加显示3
     *
     * @param mVideoEvent
     */
    @Subscribe
    public void onEventMainThread(VideoEvent mVideoEvent) {
        ImageItem ii = new ImageItem();
        Log.e("position:", mVideoEvent.getPosition() + "");

        ii.imageType = 3;
        ii.drr = mVideoEvent.getPath();
        ii.text = mVideoEvent.getText();
        ii.txtText = ii.text.replaceAll("<(.|\n)*?>","").replaceAll("\\&[a-zA-Z]{1,10};"," ");

        ii.url = mVideoEvent.getUrl();
        Log.i("TAG","videoEvent="+mVideoEvent.toString());
        //TODO 使用图片合成将蒙版跟视频背景合成一张图片
//        ImageUtils.getImageCopy(this,ii.drr);
        getImageUrl(mVideoEvent.getPosition(), ii);
    }

    /***
     * 模板添加显示
     *
     * @param mObjectEvent
     */
    @Subscribe
    public void onEventMainThread(ObjectEvent mObjectEvent) {
        Object o = mObjectEvent.getOj();


        if (mObjectEvent.getState() == 1) {//当为1的时候是添加任意模板 。2的时候只添加视频模板
            ImageItem ii = new ImageItem();

            ii.imageType = 4;
            if (o instanceof MyMode.TopArrayBean) {
                MyMode.TopArrayBean topArrayBean = (MyMode.TopArrayBean) o;
                ii.text = topArrayBean.getPublicNo();
                ii.imageLoadPath = topArrayBean.getImg();
                ii.url = topArrayBean.getLinkUrl();
                ii.txtText = ii.text.replaceAll("<(.|\n)*?>","").replaceAll("\\&[a-zA-Z]{1,10};"," ");
            } else if (o instanceof MyMode.BotArrayBean) {
                MyMode.BotArrayBean botArrayBean = (MyMode.BotArrayBean) o;
                ii.text = botArrayBean.getTitle1();
                ii.imageLoadPath = botArrayBean.getImg();
                ii.url =RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + botArrayBean.getImg();
                ii.txtText = ii.text.replaceAll("<(.|\n)*?>","").replaceAll("\\&[a-zA-Z]{1,10};"," ");
            } else if (o instanceof MyMode.AdvArrayBean) {
                MyMode.AdvArrayBean advArrayBean = (MyMode.AdvArrayBean) o;
                ii.text = advArrayBean.getAdtext();
                ii.imageLoadPath = advArrayBean.getAdimage();
                ii.url = advArrayBean.getAdurl();
                ii.animate = advArrayBean.getAnimate();
                ii.fontfamily = advArrayBean.getFont();
                ii.color = advArrayBean.getAdcolor();
                ii.id = advArrayBean.getAdvertid();
                ii.txtText = ii.text.replaceAll("<(.|\n)*?>","").replaceAll("\\&[a-zA-Z]{1,10};"," ");
                ii.fontSize = advArrayBean.getSize();

                Log.e("TAGTAG",ii.toString());
            }
            Log.e("==模板图片路径", ii.imageLoadPath);
            Bimp.bmp.add(mObjectEvent.getPosition(), ii);

            adapter.update();
        } else {
            if (o instanceof MyMode.AdvArrayBean) {
                MyMode.AdvArrayBean advArrayBean = (MyMode.AdvArrayBean) o;

                mLlAdvertising.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + advArrayBean.getAdimage(), mIvImageAdv, ImageUtil.getInstance().getBaseDisplayOption());

                mLlAddAdvertising.setVisibility(View.GONE);
                addLink = advArrayBean.getAdurl();
                addAdvertisingImage = advArrayBean.getAdimage();
                addAdvFont = advArrayBean.getFont();
                addAdvAnim = advArrayBean.getAnimate();
                addAdvFontSize = advArrayBean.getSize();
                addAdvColor = advArrayBean.getAdcolor();
                addAdvText = advArrayBean.getAdtext();
                advertid = advArrayBean.getAdvertid();
            }
        }


    }

    /**
     * 文字添加显示
     *
     * @param editInfoEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventEMMessage(EditInfoEvent editInfoEvent) {
        Log.e("editInfoEvent", editInfoEvent.getFlag() + "" + editInfoEvent.getInfo());
        if (editInfoEvent.getFlag() == 101) {
            mTvEidtArticleTitleName.setText(editInfoEvent.getText());
        }
        if (editInfoEvent.getFlag() == 102) {
            music = editInfoEvent.getMusic();
            if(!TextUtils.isEmpty(music.getSingerName()))
                mTvEidtArticleMusic.setText(music.getSongName() +"-"+music.getSingerName());
            else
                mTvEidtArticleMusic.setText(music.getSongName());
        }
        if (editInfoEvent.getFlag() >= 0 && editInfoEvent.getFlag() < Bimp.maxLength) {
            Log.i("TAG",Bimp.bmp.get(editInfoEvent.getFlag()).toString()+"");
            if (editInfoEvent.getState().equals("add")) {
                Log.e("TAG","-------"+editInfoEvent.getTextType());
                ImageItem ii = new ImageItem(Bimp.bmp.get(editInfoEvent.getFlag()).text,  Bimp.bmp.get(editInfoEvent.getFlag()).drr, Bimp.bmp.get(editInfoEvent.getFlag()).imageLoadPath, Bimp.bmp.get(editInfoEvent.getFlag()).imageType, Bimp.bmp.get(editInfoEvent.getFlag()).url,null,0,0);
                ii.txtText = editInfoEvent.getText();
                ii.pageContent = editInfoEvent.getInfo();
                if(editInfoEvent.getTextType() == 1 || editInfoEvent.getTextType() == 2){
                    Log.e("TAG","-------+++++++");
                    ii.text = editInfoEvent.getInfo();
                }
                Bimp.bmp.add(editInfoEvent.getFlag(), ii);

            } else if (editInfoEvent.getState().equals("modify")) {
                Log.i("TAG----",Bimp.bmp.get(editInfoEvent.getFlag()).toString());

                ImageItem ii = Bimp.bmp.get(editInfoEvent.getFlag());
                ii.txtText = editInfoEvent.getText();
                ii.pageContent = editInfoEvent.getInfo();
                if(editInfoEvent.getTextType() == 1 || editInfoEvent.getTextType() == 2){
                    ii.text = editInfoEvent.getInfo();
                }
                Bimp.bmp.set(editInfoEvent.getFlag(), ii);
                Log.i("TAG++++++",Bimp.bmp.get(editInfoEvent.getFlag()).toString()+"");
            }


            adapter.update();

        }
    }

    @OnClick({R.id.rlBack, R.id.tvFinishSend, R.id.tvEidtArticleTitleName, R.id.tvEidtArticleMusic, R.id.ivEidtArticleCharge, R.id.llAddAdvertising, R.id.ivDelete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlBack:
                clearImage();
                finish();

                break;
            case R.id.tvFinishSend:
                // 高清的压缩图片全部就在  list 路径里面了
                // 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
                // 完成上传服务器后 .........
                mSvEditArticle.smoothScrollTo(0, 0);
                mNoScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
                String title = mTvEidtArticleTitleName.getText().toString();

                if (TextUtils.isEmpty(titleImageUrl)) {
                    mOptImgPopWindow.showAtLocation(mTvFinishSend, Gravity.BOTTOM, 0, 0);
                    return;
                }

                if (Bimp.bmp.size() > 0) {
                    if (TextUtils.isEmpty(title) || title.equals("点击设置标题")) {
                        title = "我用拇指推发布的文章";
                    }
                    mLoadingAlertDialog.show();
                    mTvFinishSend.setEnabled(false);
                    mTvFinishSend.setClickable(false);
                    String mPageId = getIntent().getStringExtra(KEY_ID);
                    uploadText(mPageId, title);
                } else {
                    showTest("必须选择一张图片");
                }
                break;
            case R.id.tvEidtArticleTitleName:
                String title1 = mTvEidtArticleTitleName.getText().toString();
                if (title1.equals("点击设置标题") || title1.equals("我用拇指推发布的文章")) {
                    title1 = "";
                }
                Intent intentName = new Intent(OriginalArticleEditActivity.this, EditInfoActivity.class);
                intentName.putExtra("EditInfoTitle", 101);
                intentName.putExtra("titleName", title1);
                startActivity(intentName);
                break;
            case R.id.tvEidtArticleMusic:
                Intent intentMusic = new Intent(OriginalArticleEditActivity.this, MusicActivity.class);
                intentMusic.putExtra("musicName", mTvEidtArticleMusic.getText().toString());
                intentMusic.putExtra("intentName","OriginalArticleActivity");
                startActivity(intentMusic);
                break;
            case R.id.ivEidtArticleCharge:
                new PopupWindows(OriginalArticleEditActivity.this, mNoScrollgridview, 2, mPosition);
                break;
            case R.id.llAddAdvertising:
                Intent intent = new Intent(OriginalArticleEditActivity.this, MyModeActivity.class);

                intent.putExtra("type", 2);

                startActivity(intent);
                break;

            case R.id.ivDelete:
                addLink = "";
                addAdvertisingImage = "";
                mLlAddAdvertising.setVisibility(View.VISIBLE);
                mLlAdvertising.setVisibility(View.GONE);

                break;
        }
    }

    private android.app.AlertDialog alertDialog;
    private String titleImageUrl="";
    int isShow=1;//是否显示名片 1 是 0否
    private void uploadText(String id, String title) {
        //原创修改
        RequestParams params = new RequestParams(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + "appPageMeApi/doUpdateIos");


        params.setConnectTimeout(100000);
        params.addBodyParameter("loginId_mzt", (String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""));
        params.addBodyParameter("title_pic", titleImageUrl);
        params.addBodyParameter("adv_pic", addAdvertisingImage);
        params.addBodyParameter("adv_url", addLink);
        if(music.getUrl() != null && !music.getUrl().startsWith("http")){
            music.setUrl(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL+music.getUrl());
        }
        params.addBodyParameter("music_url", music.getUrl());
        if(!TextUtils.isEmpty(music.getSingerName())) {
            params.addBodyParameter("music_name", music.getSongName() + "-" + music.getSingerName());
        }else{
            params.addBodyParameter("music_name", music.getSongName());
        }
        params.addBodyParameter("title", title);
        params.addBodyParameter("id", id);
        params.addBodyParameter("info_show",isShow+"");
        params.addBodyParameter("adv_adtext",addAdvText);
        params.addBodyParameter("adv_size",addAdvFontSize+"");
        params.addBodyParameter("adv_adcolor",addAdvColor);
        params.addBodyParameter("adv_animate",addAdvAnim+"");
        params.addBodyParameter("adv_font",addAdvFont);
        params.addBodyParameter("adv_id",advertid+"");
        JSONArray json = new JSONArray();
        try {
            for (int i = 0; i < Bimp.bmp.size(); i++) {
                ImageItem ii = Bimp.bmp.get(i);
                if (ii.imageType == 1) {
                    Log.e("==text22 ", ii.text + "\t\timageLoadPath：" + ii.imageLoadPath);
                    if (ii.text != null) {
                        JSONObject jo = new JSONObject();
                        jo.put("name", "text_text" + i);
                        jo.put("content", ii.text);
                        json.put(jo);
                    }


                }
                if (ii.imageType == 2) {
                    Log.e("==图片2", ii.text + "\t\timageLoadPath：" + ii.imageLoadPath);
                    if (ii.imageLoadPath != null) {
                        JSONObject jo = new JSONObject();
                        jo.put("name", "content_pic" + i);
                        jo.put("content", ii.imageLoadPath);
                        json.put(jo);
                    }
                    if (ii.pageContent != null) {
                        JSONObject jo = new JSONObject();
                        jo.put("name", "content_text" + i);
                        jo.put("content", ii.pageContent);
                        json.put(jo);
                    }
                } else if (ii.imageType == 3) {
                    Log.e("==视频3 ", ii.text + "\t\timageLoadPath：" + ii.imageLoadPath);
                    if (ii.imageLoadPath != null) {
                        JSONObject jo = new JSONObject();
                        jo.put("name", "video_pic" + i);
                        jo.put("content", ii.imageLoadPath);
                        json.put(jo);
                    }
                    if (ii.text != null) {
                        JSONObject jo = new JSONObject();
                        jo.put("name", "video_text" + i);
                        jo.put("content", ii.text);
                        json.put(jo);
                    }
                    Log.e("TAG","ii.url="+ii.url);
                    if (ii.url != null) {
                        JSONObject jo = new JSONObject();
                        jo.put("name", "video_url" + i);
                        jo.put("content", ii.url);
                        json.put(jo);
                    }
                    if(!TextUtils.isEmpty(ii.pageContent)){
                        JSONObject jo = new JSONObject();
                        jo.put("name", "video_text" + i);
                        jo.put("content", ii.pageContent);
                        json.put(jo);
                    }
                } else if (ii.imageType == 4) {
                    Log.i("TAG====",ii.toString());
                    Log.e("==模板4", ii.text + "\t\timageLoadPath：" + ii.imageLoadPath);
                    if (ii.text != null) {
                        JSONObject jo = new JSONObject();
                        jo.put("name", "adv_adtext" + i);
                        jo.put("content", ii.text);
                        json.put(jo);
                    }
                    if (ii.imageLoadPath != null) {
                        JSONObject jo = new JSONObject();
                        jo.put("name", "adv_pic" + i);
                        jo.put("content", ii.imageLoadPath);
                        json.put(jo);
                    }
                    if (ii.url != null) {
                        JSONObject jo = new JSONObject();
                        jo.put("name", "adv_url" + i);
                        jo.put("content", ii.url);
                        json.put(jo);
                    }
                    if(ii.color != null){
                        JSONObject jo = new JSONObject();
                        jo.put("name", "adv_adcolor" + i);
                        jo.put("content", ii.color);
                        json.put(jo);
//                        params.addBodyParameter("adv_adcolor",ii.color);
                    }
                    if(ii.animate >= 0){
                        JSONObject jo = new JSONObject();
                        jo.put("name", "adv_animate" + i);
                        jo.put("content", ii.animate);
                        json.put(jo);
//                        params.addBodyParameter("adv_animate",ii.animate+"");
                    }
                    if(ii.id > 0){
                        JSONObject jo = new JSONObject();
                        jo.put("name", "adv_id" + i);
                        jo.put("content", ii.id);
                        json.put(jo);
//                        params.addBodyParameter("adv_id",ii.id + "");
                    }
                    if(!TextUtils.isEmpty(ii.fontfamily)){
                        JSONObject jo = new JSONObject();
                        jo.put("name","adv_font" + i);
                        jo.put("content",ii.fontfamily);
                        json.put(jo);
                    }
                    if(ii.fontSize != 0){
                        JSONObject jo = new JSONObject();
                        jo.put("name","adv_size" + i);
                        jo.put("content",ii.fontSize);
                        json.put(jo);
                    }
                    if(!TextUtils.isEmpty(ii.pageContent)){
                        JSONObject jo = new JSONObject();
                        jo.put("name", "adv_text" + i);
                        jo.put("content", ii.pageContent);
                        json.put(jo);
                    }
                }


            }
            params.addBodyParameter("content_text", json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("json数据：", json.toString());


        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onCancelled(CancelledException arg0) {
                Log.e("HttpUtil onCanceled", arg0.toString());
                mLoadingAlertDialog.dismiss();
                showTest("服务器连接取消");
                mTvFinishSend.setEnabled(true);
                mTvFinishSend.setClickable(true);
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Log.e("HttpUtil onError", arg0.toString() + "\n" + arg1);
                mLoadingAlertDialog.dismiss();
                showTest("服务器连接失败");
                mTvFinishSend.setEnabled(true);
                mTvFinishSend.setClickable(true);
            }

            @Override
            public void onFinished() {
                mLoadingAlertDialog.dismiss();

                mTvFinishSend.setEnabled(true);
                mTvFinishSend.setClickable(true);
            }

            @Override
            public void onSuccess(String s) {
                Log.e("HttpUtil onSuccess", s.toString());
                mLoadingAlertDialog.dismiss();
                mTvFinishSend.setEnabled(true);
                mTvFinishSend.setClickable(true);
                try {
                    Published published = new Published();
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.has("state")) {
                        published.setState(jsonObject.getString("state"));
                    }
                    if (jsonObject.has("pageme_id")) {
                        published.setPageme_id(jsonObject.getInt("pageme_id"));
                    }
                    if (jsonObject.has("msg")) {
                        published.setMsg(jsonObject.getString("msg"));
                    }
                    if (jsonObject.has("title_pic")) {
                        published.setTitle_pic(jsonObject.getString("title_pic"));
                    }
                    if (published.getState().equals("1")) {
                        intent(published);
                    } else {
                        showTest(published.getMsg());
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


    }

    private void intent(final Published code) {
        alertDialog = UIUtils.getAlertDialogText(OriginalArticleEditActivity.this, "温馨提示", "您的作品已经提交，后台会在24小时内审核，如果涉及以下内容如（但不限于）政治敏感话题、色情淫秽、虚假宣传、资金返利、违规分销、垃圾内容、不够美观、诱导转发、低俗广告、欺诈行为等;" +
                "有可能被后台审核人员删除，请知晓并严格遵守！", null, "确定",  null, new OnClickListener() {
            @Override
            public void onClick(View v) {
                clearImage();

                Intent intent = new Intent(OriginalArticleEditActivity.this, PageLookActivity.class);
                intent.putExtra(PageLookActivity.KEY_URL, RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + RetrofitUtil.PAGE_LOOK_OWN + "?id=" + code.getPageme_id() + "&share=1"+ "&curLoginId=" + MainActivity.loginId);

                intent.putExtra(PageLookActivity.KEY_IMG, code.getTitle_pic());

                intent.putExtra(PageLookActivity.KEY_ID, code.getPageme_id() + "");
                intent.putExtra("isOriginal", true);

                startActivity(intent);
                alertDialog.dismiss();
                finish();

            }
        });
        alertDialog.show();

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            tbShowCardName.setText("显示名片");
            isShow=1;
        } else {
            tbShowCardName.setText("隐藏名片");
            isShow=0;
        }
    }


    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater; // 视图容器
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            return (Bimp.bmp.size());
        }

        public Object getItem(int arg0) {

            return null;
        }

        public long getItemId(int arg0) {

            return 0;
        }


        /**
         * ListView Item设置
         */
        public View getView(final int position, View convertView, ViewGroup parent) {
            final int coord = position;
            ViewHolder holder = null;
            if (convertView == null) {

                convertView = inflater.inflate(R.layout.item_published_grida,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                holder.llMainItem = (LinearLayout) convertView
                        .findViewById(R.id.llMainItem);
                holder.ivPublishedGridaAdd = (ImageView) convertView
                        .findViewById(R.id.ivPublishedGridaAdd);
                holder.llAdd = (LinearLayout) convertView
                        .findViewById(R.id.llAdd);
                holder.ivPublishedGridaDelete = (ImageView) convertView
                        .findViewById(R.id.ivPublishedGridaDelete);
                holder.llPublishedGridaTool = (LinearLayout) convertView
                        .findViewById(R.id.llPublishedGridaTool);
                holder.tvPublishedGridaText = (TextView) convertView
                        .findViewById(R.id.tvPublishedGridaText);
                holder.ivPublishedGridaUp = (RelativeLayout) convertView
                        .findViewById(R.id.ivPublishedGridaUp);
                holder.ivPublishedGridaDown = (RelativeLayout) convertView
                        .findViewById(R.id.ivPublishedGridaDown);
                holder.tvPublishedGridaFont = (TextView) convertView
                        .findViewById(R.id.tvPublishedGridaFont);
                holder.ivVideo = (ImageView) convertView
                        .findViewById(R.id.ivVideo);
                holder.tvPublishedGridaImage = (TextView) convertView
                        .findViewById(R.id.tvPublishedGridaImage);
                holder.tvPublishedGridaVodie = (TextView) convertView
                        .findViewById(R.id.tvPublishedGridaVodie);
                holder.tvPublishedGridaMode = (TextView) convertView
                        .findViewById(R.id.tvPublishedGridaMode);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            if (Bimp.bmp.size() > 0) {
                if (Bimp.bmp.get(position).imageType==1){
                    holder.image.setImageResource(R.mipmap.ic_text);
                }else{
                    if (Bimp.bmp.get(position).imageLoadPath != null) {
                        ImageLoader.getInstance().displayImage(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + Bimp.bmp.get(position).imageLoadPath, holder.image, ImageUtil.getInstance().getBaseDisplayOption());
                    }
                }
                if (Bimp.bmp.get(position).imageType == 3) {
                    holder.ivVideo.setVisibility(View.VISIBLE);
                } else {
                    holder.ivVideo.setVisibility(View.GONE);
                }
                if(Bimp.bmp.get(position).pageContent != null)
                    holder.tvPublishedGridaText.setText(Bimp.bmp.get(position).pageContent.replaceAll("<(.|\n)*?>","").replaceAll("\\&[a-zA-Z]{1,10};"," "));
                else {
                    holder.tvPublishedGridaText.setHint("点击添加文字");
                    holder.tvPublishedGridaText.setText("");
                }
            }
            if (position == 0) {
                holder.ivPublishedGridaUp.setVisibility(View.GONE);
            } else {
                holder.ivPublishedGridaUp.setVisibility(View.VISIBLE);
            }
            if (position == Bimp.bmp.size() - 1) {
                holder.ivPublishedGridaDown.setVisibility(View.GONE);
            } else {
                holder.ivPublishedGridaDown.setVisibility(View.VISIBLE);
            }

            holder.ivPublishedGridaUp.setOnClickListener(new onClickUp(position, holder.llMainItem, 1));
            holder.ivPublishedGridaDown.setOnClickListener(new onClickUp(position, holder.llMainItem, 2));


            holder.image.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Bimp.bmp.get(position).imageType == 1) {
                        new PopupWindows(OriginalArticleEditActivity.this, mNoScrollgridview, 1, position);
                    } else {
                        Intent intent = new Intent(OriginalArticleEditActivity.this,
                                PhotoActivity.class);
                        intent.putExtra("ID", position);
                        startActivity(intent);
                    }
                }
            });
            if (Bimp.bmp.get(position).isVisable) {
                holder.llPublishedGridaTool.setVisibility(View.VISIBLE);
                holder.ivPublishedGridaAdd.setVisibility(View.GONE);
            } else {
                holder.llPublishedGridaTool.setVisibility(View.GONE);
                holder.ivPublishedGridaAdd.setVisibility(View.VISIBLE);
            }
            //添加的图标
            holder.ivPublishedGridaAdd.setOnClickListener(new onClick(holder.llAdd,
                    holder.llPublishedGridaTool, position));
            //图文
            holder.llPublishedGridaTool.setOnClickListener(new onClick1(holder.llAdd));
            holder.tvPublishedGridaImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    new PopupWindows(OriginalArticleEditActivity.this, mNoScrollgridview, 1, position);
                    Bimp.bmp.get(position).isVisable = false;
                }
            });
            //视频

            holder.tvPublishedGridaVodie.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(OriginalArticleEditActivity.this, MakeVideoActivity.class);
                    intent.putExtra("position", position);
                    startActivity(intent);
                    Bimp.bmp.get(position).isVisable = false;

                }
            });
            //

            holder.tvPublishedGridaMode.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(OriginalArticleEditActivity.this, MyModeActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                    Bimp.bmp.get(position).isVisable = false;
                }
            });
            //文字

            holder.tvPublishedGridaFont.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentName = new Intent(OriginalArticleEditActivity.this, EditInfoActivity.class);
                    intentName.putExtra("EditInfoTitle", position);
                    intentName.putExtra("textState", "add");
                    intentName.putExtra("text", "");//添加文字时，文字信息传递为空
                    startActivity(intentName);
                    Bimp.bmp.get(position).isVisable = false;
                }
            });
            holder.tvPublishedGridaText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intentName = new Intent(OriginalArticleEditActivity.this, EditInfoActivity.class);
                        Log.e("文字position:", position + "");
                        intentName.putExtra("EditInfoTitle", position);
                        intentName.putExtra("textState", "modify");
                        intentName.putExtra("text", Bimp.bmp.get(position).pageContent);
                        startActivity(intentName);
                        Bimp.bmp.get(position).isVisable = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            holder.ivPublishedGridaDelete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Bimp.bmp.size() > 1) {

                        try {
                            Bimp.bmp.remove(Bimp.bmp.get(position));
                            showTest("删除一个列表");
                            adapter.update();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        showTest("至少要留一张图片");
                    }
                }
            });

            return convertView;
        }

        private class onClick implements OnClickListener {
            private LinearLayout add;
            private LinearLayout tool;
            private int position;


            public onClick(LinearLayout add, LinearLayout tool, int position) {
                this.add = add;
                this.tool = tool;
                this.position = position;

            }

            @Override
            public void onClick(View v) {
                if (Bimp.maxLength - Bimp.bmp.size() > 0) {
                    for (int i = 0; i < Bimp.bmp.size(); i++) {
                        if (position == i) {
                            Bimp.bmp.get(i).isVisable = true;
                            tool.startAnimation(AnimationUtils.loadAnimation(OriginalArticleEditActivity.this, R.anim.activity_translate_in));

                        } else {
                            add.startAnimation(AnimationUtils.loadAnimation(OriginalArticleEditActivity.this, R.anim.activity_translate_in));
                            Bimp.bmp.get(i).isVisable = false;
                        }
                        adapter.update();
                    }
                } else {
                    showTest("超出原创选择的最大限度，无法继续添加了");
                }


            }
        }

        private class onClick1 implements OnClickListener {
            private LinearLayout add;


            public onClick1(LinearLayout add) {
                this.add = add;


            }

            @Override
            public void onClick(View v) {
                for (int i = 0; i < Bimp.bmp.size(); i++) {

                    add.startAnimation(AnimationUtils.loadAnimation(OriginalArticleEditActivity.this, R.anim.activity_translate_in));
                    Bimp.bmp.get(i).isVisable = false;

                    adapter.update();
                }


            }
        }

        private class onClickUp implements OnClickListener {

            private int state;
            private int position;
            private LinearLayout llMainItem;

            public onClickUp(int position, LinearLayout llMainItem, int state) {

                this.llMainItem = llMainItem;
                this.state = state;
                this.position = position;

            }

            @Override
            public void onClick(View v) {
                if (state == 1) {
                    llMainItem.startAnimation(AnimationUtils.loadAnimation(OriginalArticleEditActivity.this, R.anim.activity_translate_in));

                    ImageItem ii = Bimp.bmp.get(position); //new ImageItem(Bimp.bmp.get(position).text, Bimp.bmp.get(position).drr, Bimp.bmp.get(position).imageLoadPath, Bimp.bmp.get(position).imageType, Bimp.bmp.get(position ).url);
                    ImageItem ii2 = Bimp.bmp.get(position - 1); //new ImageItem(Bimp.bmp.get(position - 1).text, Bimp.bmp.get(position - 1).drr, Bimp.bmp.get(position - 1).imageLoadPath, Bimp.bmp.get(position - 1).imageType, Bimp.bmp.get(position -1).url);
                    Bimp.bmp.set(position - 1, ii);
                    Bimp.bmp.set(position, ii2);
                    adapter.update();
                } else {
                    llMainItem.startAnimation(AnimationUtils.loadAnimation(OriginalArticleEditActivity.this, R.anim.activity_translate_out));
                    ImageItem ii = Bimp.bmp.get(position); //new ImageItem(Bimp.bmp.get(position).text, Bimp.bmp.get(position).drr, Bimp.bmp.get(position).imageLoadPath, Bimp.bmp.get(position).imageType, Bimp.bmp.get(position ).url);
                    ImageItem ii2 = Bimp.bmp.get(position + 1); //new ImageItem(Bimp.bmp.get(position + 1).text, Bimp.bmp.get(position + 1).drr, Bimp.bmp.get(position + 1).imageLoadPath, Bimp.bmp.get(position + 1).imageType, Bimp.bmp.get(position+1 ).url);
                    Bimp.bmp.set(position + 1, ii);
                    Bimp.bmp.set(position, ii2);
                    adapter.update();
                }


            }
        }

        public class ViewHolder {
            public LinearLayout llMainItem;
            public ImageView image;
            public ImageView ivPublishedGridaAdd;
            public LinearLayout llAdd;
            public ImageView ivPublishedGridaDelete;
            public LinearLayout llPublishedGridaTool;
            public TextView tvPublishedGridaText;
            public RelativeLayout ivPublishedGridaUp;
            public RelativeLayout ivPublishedGridaDown;
            public TextView tvPublishedGridaFont;
            public TextView tvPublishedGridaImage;
            public TextView tvPublishedGridaVodie;
            public TextView tvPublishedGridaMode;
            public ImageView ivVideo;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {

                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        break;


                    }
                }
            }).start();
        }
    }

    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

    protected void onRestart() {
        adapter.update();
        super.onRestart();
    }

    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent, final int state, final int position) {

            View view = View
                    .inflate(mContext, R.layout.item_popupwindows, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));

            setWidth(LayoutParams.FILL_PARENT);
            setHeight(LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            Button bt1 = (Button) view
                    .findViewById(R.id.item_popupwindows_camera);
            Button bt2 = (Button) view
                    .findViewById(R.id.item_popupwindows_Photo);
            Button bt3 = (Button) view
                    .findViewById(R.id.item_popupwindows_cancel);
            bt1.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    CacheUtil.getInstance().add(CacheUtil.FORMAT_FRTE_TYPE,1);
                    if (state == 1) {
                        ImageUtil.getInstance().openCamera(mOnhanlderResultCallbackAll);
                        mPosition = position;
                    } else {
                        ImageUtil.getInstance().openCamera(mOnhanlderResultCallback);
                    }

                    dismiss();
                }
            });
            bt2.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    CacheUtil.getInstance().add(CacheUtil.FORMAT_FRTE_TYPE,1);
                    if (state == 1) {


                        mPosition = position;
                        int num = Bimp.maxLength - Bimp.bmp.size();
                        if (num > 9) {
                            ImageUtil.getInstance().chooseImage("开始制作",mOnhanlderResultCallbackAll, 9);
                        } else {
                            ImageUtil.getInstance().chooseImage("开始制作",mOnhanlderResultCallbackAll, num);
                        }
                    } else {
                        ImageUtil.getInstance().chooseImage("开始制作",mOnhanlderResultCallback, 1);

                    }

                    dismiss();
                }
            });
            bt3.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {


            case REQUEST_CLIP_IMAGE://标题图片（拍照和从相册获取）

                if(resultCode == Activity.RESULT_OK
                        && data != null){
               String     outputPath = PhotoActionHelper.getOutputPath(data);

                    Bitmap bitmap = BitmapFactory.decodeFile(outputPath);
                    mIvEidtArticleTitleImage.setImageBitmap(bitmap);
                    getUrl(new File(outputPath));
                }else{
                    mIvEidtArticleTitleImage.setImageBitmap(null);

                    titleImageUrl="";
                }
                break;
            case REQUEST_CLIP_IMAGEAll:
                String outputPath = PhotoActionHelper.getOutputPath(data);
                String inputPath = PhotoActionHelper.getInputPath(data);
                int position = PhotoActionHelper.getPosition(data);
                Log.e("help position", position + "");
                ImageItem ii = new ImageItem();

                if(resultCode == Activity.RESULT_OK
                        && data != null){



                    ii.drr = outputPath;
                }else{
                    ii.drr = inputPath;
                }
                getImageUrl(mPosition, ii);

                break;


        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {


            clearImage();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    private void clearImage() {
        addLink = "";
        addAdvertisingImage = "";
        Bimp.bmp.clear();
        System.gc();//释放内存
        Bimp.act_bool = true;


    }

    private PopupWindow mOptImgPopWindow;

    private void initPpwWindow() {
        //设置contentView
        View contentView = UIUtils.inflate(OriginalArticleEditActivity.this, R.layout.ppw_toast);
        mOptImgPopWindow = new PopupWindow(contentView,
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
        mOptImgPopWindow.setContentView(contentView);
        mOptImgPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mOptImgPopWindow.setOutsideTouchable(true);
        //设置各个控件的点击响应
        RelativeLayout rlToast = (RelativeLayout) contentView.findViewById(R.id.rlToast);
        rlToast.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptImgPopWindow.dismiss();
            }
        });


    }

    private void getImageUrl(final int mPosition, final ImageItem ii) {
        try{
        mLoadingAlertDialog.show();
        Log.e("file:",new File(ii.drr).length()+"");
        Log.e("file scal:",ImageUtil.scal(ii.drr).length()+"");
        Log.e("file scal:",ii.drr);
        Subscription sbMyAccount9 = wrapObserverWithHttp(WorkService.getWorkService().compressUpload((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""), ImageUtil.getInstance().wrapUploadImgRequest(ImageUtil.scal(ii.drr)))
        ).subscribe(new Subscriber<url>() {
            @Override
            public void onCompleted() {
                mLoadingAlertDialog.dismiss();

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mLoadingAlertDialog.dismiss();
                showTest("服务器连接失败");
                Log.e("urle :", e.toString());
            }

            @Override
            public void onNext(final url url) {
                Log.e("====url:", url.imgUrl);
                Log.e("====mPosition:", mPosition + "");
                ii.imageLoadPath = url.imgUrl;
                Bimp.bmp.add(mPosition, ii);
                //showTest("图片上传成功");
                adapter.update();
            }
        });

        addSubscription(sbMyAccount9);
        }catch (Exception e){
            e.printStackTrace();
            Log.e("====url:",e.toString());
        }

    }

    private void getUrl(File file) {
        mLoadingAlertDialog.show();
        Subscription sbMyAccount9 = wrapObserverWithHttp(WorkService.getWorkService().compressUpload((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""), ImageUtil.getInstance().wrapUploadImgRequest(file))
        ).subscribe(new Subscriber<url>() {
            @Override
            public void onCompleted() {
                mLoadingAlertDialog.dismiss();

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mLoadingAlertDialog.dismiss();
                showTest("服务器连接失败");
                Log.e("urle :", e.toString());
            }

            @Override
            public void onNext(final url url) {
                Log.e("url:", url.imgUrl);
                titleImageUrl = url.imgUrl;
                //  showTest("图片上传成功");
            }
        });

        addSubscription(sbMyAccount9);
    }
}
