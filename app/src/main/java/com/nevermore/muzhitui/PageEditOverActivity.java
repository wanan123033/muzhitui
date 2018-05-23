package com.nevermore.muzhitui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nevermore.muzhitui.event.WorkEvent;
import com.nevermore.muzhitui.module.bean.ImageUpload;
import com.nevermore.muzhitui.module.bean.PageOverRequest;
import com.nevermore.muzhitui.module.bean.SavePagerResult;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import base.BaseActivityTwoV;
import base.ImageUtil;
import base.SPUtils;
import base.UIUtils;
import base.helper.PhotoActionHelper;
import base.network.RetrofitUtil;
import base.util.CacheUtil;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

public class PageEditOverActivity extends BaseActivityTwoV implements View.OnClickListener {
    @BindView(R.id.iv)
    ImageView mIv;
    @BindView(R.id.flytPhoto)
    FrameLayout mFlytPhoto;
    @BindView(R.id.etTitle)
    EditText mEtTitle;
    @BindView(R.id.cbIsCard)
    CheckBox mCbIsCard;
    @BindView(R.id.icToast)
    ImageView icToast;
    private String mOutputPath;
    public static final String KEY_TITLE = "TITLE";
    public static final String KEY_CONTENT = "CONTENT";
    public static final String KEY_PAGEID = "PAGEID";
    public static final String KEY_WEBSITE = "WEBSITE";
    public static final String KEY_IMAGE = "IMAGE";
    private LoadingAlertDialog mLoadingAlertDialog;
    private PopupWindow mOptImgPopWindow;
    private String defaultPath;
    private String mPath;
    public static final int REQUEST_CLIP_IMAGE = 2028;
    private String mTitle;
    private String mContent;
    private String mWebSite;
    private String mId = "";
    private int isShowCard = 1;
    private String mOverImgPath;
    private GalleryFinal.OnHanlderResultCallback mOnhanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null && !resultList.isEmpty()) {
                final Uri uri = Uri.parse("file:/" + resultList.get(0).getPhotoPath());
                mOptImgPopWindow.dismiss();
                mOutputPath = new File(UIUtils.getExternalCacheDir(), System.currentTimeMillis() + ".png").getPath();
                PhotoActionHelper.clipImage(PageEditOverActivity.this).input(resultList.get(0).getPhotoPath()).output(mOutputPath).maxOutputWidth(100)
                        .requestCode(REQUEST_CLIP_IMAGE).start();
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {

        }
    };


    @Override
    public void init() {
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        initPpwWindow();
        mTitle = getIntent().getStringExtra(KEY_TITLE);
        Log.i("TAG---","PageEditOverActivity mTitle=" + mTitle);
        mContent = (String) CacheUtil.getInstance().remove(KEY_CONTENT);
        mId = getIntent().getStringExtra(KEY_PAGEID);
        mWebSite = getIntent().getStringExtra(KEY_WEBSITE);
        defaultPath = getIntent().getStringExtra(KEY_IMAGE);

        mPath = defaultPath;
        mOverImgPath = mPath;
        //mContent = mWebSite;
        mEtTitle.setText(mTitle);

        showRight("完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPager();
            }
        });
        showBack();
        mFlytPhoto.setOnClickListener(this);
        mCbIsCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isShowCard = 1;
                } else {
                    isShowCard = 0;
                }
            }
        });
        ImageLoader.getInstance().displayImage(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + mPath, mIv, ImageUtil.getInstance().getBaseDisplayOption());
    }

    private AlertDialog alertDialog;

    private void uploadPager() {

        final String title1 = mEtTitle.getText().toString();
        if (TextUtils.isEmpty(title1)) {
            showTest("请填写标题");
            return;
        }
        mLoadingAlertDialog.show();
        Observable observable = null;
        if (!mPath.equals(defaultPath)) {
            observable = wrapObserverWithHttp(WorkService.getWorkService().otherImagUpload((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""), ImageUtil.getInstance().wrapUploadImgRequest(new File(mPath))).flatMap(new Func1<ImageUpload, Observable<SavePagerResult>>() {
                @Override
                public Observable<SavePagerResult> call(ImageUpload o) {
                    if (!TextUtils.isEmpty(o.getImgUrl())) {
                        PageOverRequest pageOverRequest = new PageOverRequest();
                        pageOverRequest.setPagehtml(mContent);
                        pageOverRequest.setTitle(title1);
                        pageOverRequest.setWebsite(mWebSite);
                        mOverImgPath = o.getImgUrl();
                        pageOverRequest.setImage(o.getImgUrl());
                        pageOverRequest.setState(1);
                        pageOverRequest.setId(mId);
                        pageOverRequest.setInfoShow(1);
                        Logger.i("mid = " + mId);
                        return WorkService.getWorkService().doSave((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""), pageOverRequest.getPagehtml(), pageOverRequest.getTitle(), pageOverRequest.getWebsite(), pageOverRequest.getImage(), pageOverRequest.getState(), pageOverRequest.getId(), isShowCard);

                    } else {
                        return null;
                    }
                }
            }));
        } else {
            PageOverRequest pageOverRequest = new PageOverRequest();
            pageOverRequest.setPagehtml(mContent);
            pageOverRequest.setTitle(title1);
            pageOverRequest.setWebsite(mWebSite);
            pageOverRequest.setImage(mPath);
            pageOverRequest.setState(1);
            pageOverRequest.setId(mId);
            pageOverRequest.setInfoShow(1);
            Logger.i("mid = " + mId);
            observable = wrapObserverWithHttp(WorkService.getWorkService().doSave((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""), pageOverRequest.getPagehtml(), pageOverRequest.getTitle(), pageOverRequest.getWebsite(), pageOverRequest.getImage(), pageOverRequest.getState(), pageOverRequest.getId(), isShowCard));
        }
        Subscription sbMyAccount = observable.subscribe(new Subscriber<SavePagerResult>() {
            @Override
            public void onCompleted() {
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mLoadingAlertDialog.dismiss();
                try {
                    File file = new File("/sdcard/crash/muzhitui.log");
                    if(!file.exists()){
                        file.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    PrintStream ps = new PrintStream(fos);
                    e.printStackTrace(ps);
//                    ps.close();
                }catch(Exception e1){
                    e1.printStackTrace();
                }

                showTest(mNetWorkError);
            }

            @Override
            public void onNext(final SavePagerResult baseBean) {
                if (baseBean.isState()) {
                    //秒变添加审核提示

                    alertDialog = UIUtils.getAlertDialogText(PageEditOverActivity.this, "温馨提示", "您的作品已经提交，后台会在24小时内审核，如果涉及以下内容如（但不限于）政治敏感话题、色情淫秽、虚假宣传、资金返利、违规分销、垃圾内容、不够美观、诱导转发、低俗广告、欺诈行为等;" +
                            "有可能被后台审核人员删除，请知晓并严格遵守！", null, "确定", null, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EventBus.getDefault().post(new WorkEvent());
                            //baseStartActivity(MainActivity.class);
                            Intent intent = new Intent(PageEditOverActivity.this, PageLookActivity.class);
                            intent.putExtra(PageLookActivity.KEY_URL, RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + RetrofitUtil.PAGE_LOOK + "?id=" + baseBean.getPageId() + "&share=0" + "&curLoginId=" + MainActivity.loginId);
                            intent.putExtra(PageLookActivity.KEY_IMG, mOverImgPath);
                            intent.putExtra(PageLookActivity.KEY_ID, baseBean.getPageId() + "");
                            startActivity(intent);
                            alertDialog.dismiss();
                            finish();
                        }
                    });
                    alertDialog.show();
                } else {
                    showTest(mServerEror);
                }

            }
        });
        addSubscription(sbMyAccount);
    }

    /*private void uploadPager(String content) {

        mLoadingAlertDialog.show();
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().doSave(content, mTitle, mWebSite, mPath, 0,mId, 1)).subscribe(new Subscriber<SavePagerResult>() {
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
                    EventBus.getDefault().post(new WorkEvent());
                    baseStartActivity(MainActivity.class);
                } else {
                    showTest(mServerEror);
                }

            }
        });
        addSubscription(sbMyAccount);
    }
*/

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
                mPath = path;
                ImageLoader.getInstance().displayImage(uri.toString(), mIv, ImageUtil.getInstance().getBaseDisplayOption());
                icToast.setVisibility(View.GONE);
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initPpwWindow() {
        //设置contentView
        View contentView = UIUtils.inflate(PageEditOverActivity.this, R.layout.ppw_mysetting);
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

    @Override
    public int createSuccessView() {
        return R.layout.activity_page_edit_over;
    }


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
            case R.id.flytPhoto:
                mOptImgPopWindow.showAtLocation(mFlytPhoto, Gravity.BOTTOM, 0, 0);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
