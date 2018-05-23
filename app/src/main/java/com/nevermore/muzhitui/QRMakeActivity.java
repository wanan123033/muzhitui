package com.nevermore.muzhitui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nevermore.muzhitui.event.MyModeRefreshEvent;
import com.nevermore.muzhitui.fragment.MyWorksModeFragment;
import com.nevermore.muzhitui.module.bean.BaseBean;
import com.nevermore.muzhitui.module.bean.ImageUpload;
import com.nevermore.muzhitui.module.bean.MyMode;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

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
import butterknife.BindView;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by hehe on 2016/4/30.
 */
public class QRMakeActivity extends BaseActivityTwoV {
    @BindView(R.id.etTitle1)
    EditText mEtTitle1;
    @BindView(R.id.etTitle2)
    EditText mEtTitle2;
    @BindView(R.id.ivQr)
    ImageView mIvQr;
    @BindView(R.id.tvChangeQR)
    TextView mTvChangeQR;
    private int mId;
    private MyMode.BotArrayBean mBotArrayBean;
    private String mImg;
    private LoadingAlertDialog mLoadingAlertDialog;
    private boolean mIsChoiceImg;
    public static final int REQUEST_CLIP_IMAGE = 2028;

    @Override
    public void init() {
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        setMyTitle("二维码模板制作");
        showBack();
        showRight("完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadQR();
            }
        });

        mBotArrayBean = (MyMode.BotArrayBean) getIntent().getSerializableExtra(MyWorksModeFragment.YEJIAO);
        if (mBotArrayBean != null) {
            mId = mBotArrayBean.getBotId();
            mEtTitle1.setText(mBotArrayBean.getTitle1());
            mEtTitle2.setText(mBotArrayBean.getTitle2());
            mImg = mBotArrayBean.getImg();
            ImageLoader.getInstance().displayImage(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + mBotArrayBean.getImg(), mIvQr, ImageUtil.getInstance().getBaseDisplayOption());
        }


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
                ImageLoader.getInstance().displayImage(uri.toString(), mIvQr);
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public int createSuccessView() {
        return R.layout.activity_qrmake;
    }


    @OnClick({R.id.ivQr, R.id.tvChangeQR})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivQr:
                loadImage();
                break;
            case R.id.tvChangeQR:
                loadImage();
                break;
        }
    }

    private void uploadQR() {

        final String title1 = mEtTitle1.getText().toString();
        final String title2 = mEtTitle2.getText().toString();
        if (TextUtils.isEmpty(title1)) {
            showTest("请填写标题1");
            return;
        }
        if (TextUtils.isEmpty(title2)) {
            showTest("请填写标题2");
            return;
        }
        if (TextUtils.isEmpty(mImg)) {
            showTest("请添加二维码");
            return;
        }
        mLoadingAlertDialog.show();
        Observable observable = null;
        if (mId == 0) {
            observable = wrapObserverWithHttp(WorkService.getWorkService().otherImagUpload((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),ImageUtil.getInstance().wrapUploadImgRequest(new File(mImg))).flatMap(new Func1<ImageUpload, Observable<BaseBean>>() {
                @Override
                public Observable<BaseBean> call(ImageUpload o) {
                    return WorkService.getWorkService().saveBot((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),title1, title2, o.getImgUrl());
                }
            }));
        } else {
            if (mIsChoiceImg) {
                observable = wrapObserverWithHttp(WorkService.getWorkService().otherImagUpload((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),ImageUtil.getInstance().wrapUploadImgRequest(new File(mImg))).flatMap(new Func1<ImageUpload, Observable<BaseBean>>() {
                    @Override
                    public Observable<BaseBean> call(ImageUpload o) {
                        return WorkService.getWorkService().updateBot((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),mId, title1, title2, o.getImgUrl());
                    }
                }));
            } else {
                observable = wrapObserverWithHttp(WorkService.getWorkService().updateBot((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),mId, title1, title2, mImg));
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
                Logger.i("baseBean.getState() =" + baseBean.getState());
                if (baseBean.getState() == 1) {
                    EventBus.getDefault().post(new MyModeRefreshEvent());
                    finish();
                    showTest("添加成功");
                } else {
                    showTest(mServerEror);
                }

            }
        });
        addSubscription(sbMyAccount);
    }
    private String mOutputPath;
    public void loadImage() {
        CacheUtil.getInstance().add(CacheUtil.FORMAT_FRTE_TYPE,1);
        ImageUtil.getInstance().chooseImage("开始制作",new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (resultList != null && !resultList.isEmpty()) {
                    mImg = resultList.get(0).getPhotoPath();
                    mOutputPath = new File(UIUtils.getExternalCacheDir(), System.currentTimeMillis() + ".jpg").getPath();
                    PhotoActionHelper.clipImage(QRMakeActivity.this).input(mImg).output(mOutputPath).maxOutputWidth(640)
                            .requestCode(REQUEST_CLIP_IMAGE).start();
                }
            }
            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {

            }
        }, 1);

    }

}
