package com.nevermore.muzhitui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nevermore.muzhitui.module.bean.ImageUpload;
import com.nevermore.muzhitui.module.bean.My;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.List;

import base.BaseActivityTwoV;
import base.ImageUtil;
import base.SPUtils;
import base.UIUtils;
import base.helper.PhotoActionHelper;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by gk on 2016/3/8.
 */
public class MySettingActivity extends BaseActivityTwoV implements View.OnClickListener {
    public static final int REQUEST_CLIP_IMAGE = 2028;
    public static final int REQUEST_NAME = 2027;
    @BindView(R.id.ivHead)
    ImageView mIvHead;
    @BindView(R.id.flytHead)
    FrameLayout mFlytHead;
    @BindView(R.id.flytName)
    FrameLayout mFlytName;
    @BindView(R.id.tvName)
    TextView tvName;
    private PopupWindow mOptImgPopWindow;
    private LoadingAlertDialog mLoadingAlertDialog;
    private String mOutputPath;
    private String mPath;
    private GalleryFinal.OnHanlderResultCallback mOnhanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null && !resultList.isEmpty()) {
                final Uri uri = Uri.parse("file:/" + resultList.get(0).getPhotoPath());
                mOptImgPopWindow.dismiss();
                mOutputPath = new File(UIUtils.getExternalCacheDir(), System.currentTimeMillis() + ".png").getPath();
                PhotoActionHelper.clipImage(MySettingActivity.this).input(resultList.get(0).getPhotoPath()).output(mOutputPath).maxOutputWidth(49 * 3)
                        .requestCode(REQUEST_CLIP_IMAGE).start();
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {

        }
    };

    @Override
    public void init() {
        setMyTitle("个人设置");
        showBack();
        TextView textView = showRight();
        textView.setOnClickListener(this);
        initPpwWindow();
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        loadData();
    }

    private void loadData() {
        mLoadingAlertDialog.show();
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().mySet((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))).subscribe(new Subscriber<My>() {
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
            public void onNext(My my) {
                if ("1".equals(my.getState())) {
                    ImageLoader.getInstance().displayImage(my.getHeadimg(), mIvHead, ImageUtil.getInstance().getCircleDisplayOption());
                    tvName.setText(my.getWechatname());
                } else {
                    showTest(mServerEror);
                    showEmptyView(0);
                }

            }
        });
        addSubscription(sbMyAccount);
    }

    @Override
    public int createSuccessView() {

        return R.layout.activity_mysetting;
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
                mPath = path;
                l("字符串是否相等s =  " + (mPath == path));
                ImageLoader.getInstance().displayImage(uri.toString(), mIvHead, ImageUtil.getInstance().getCircleDisplayOption());
            }
            return;
        } else if (resultCode == Activity.RESULT_OK
                && data != null
                && (requestCode == REQUEST_NAME)) {
            String name = data.getStringExtra(SPUtils.KEY_WXNICKNAME);
            tvName.setText(name);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initPpwWindow() {
        //设置contentView
        View contentView = UIUtils.inflate(MySettingActivity.this, R.layout.ppw_mysetting);
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

    private void uploadImg() {
        if (!TextUtils.isEmpty(mPath)) {
            File file = new File(mPath);
            mLoadingAlertDialog.show();
            String descriptionString = "test";
           /* RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);*/
            //  RequestBody requestBody1 = RequestBody.create(MediaType.parse("image/*"), file);
            Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().uploadImg((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),ImageUtil.getInstance().wrapUploadImgRequest(file))).subscribe(new Subscriber<ImageUpload>() {
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
                    if (imageUpload.getState() == 1) {
                        l("imgeUploiad = " + imageUpload.getState());
                        showTest("修改成功");
//                        finish();
                    } else {

                        showTest(mServerEror);
                    }

                }
            });
            addSubscription(sbMyAccount);
        } else {
            showTest("未修改图片");
        }
    }

    @OnClick({R.id.ivHead, R.id.flytHead, R.id.flytName})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.flytHead:
                mOptImgPopWindow.showAtLocation(mFlytHead, Gravity.BOTTOM, 0, 0);
                break;
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
            case R.id.flytName:
                Intent intentUpdateName = new Intent(MySettingActivity.this, ModifyNameActivity.class);
                startActivityForResult(intentUpdateName, REQUEST_NAME);
                break;
            case R.id.tvRight:
                //   finish();
                uploadImg();
                break;
        }
    }

}
