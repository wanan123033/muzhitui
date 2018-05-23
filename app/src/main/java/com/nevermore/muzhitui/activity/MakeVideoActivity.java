package com.nevermore.muzhitui.activity;

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
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.nevermore.muzhitui.FontStyleInterface;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.event.MyModeRefreshEvent;
import com.nevermore.muzhitui.event.ObjectEvent;
import com.nevermore.muzhitui.event.VideoEvent;
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

import java.io.File;
import java.util.List;

import base.BaseActivityTwoV;
import base.ImageUtil;
import base.UIUtils;
import base.helper.PhotoActionHelper;
import base.network.RetrofitUtil;
import base.util.ConnectUtil;
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

public class MakeVideoActivity extends BaseActivityTwoV {




    @BindView(R.id.etTitle1)
    EditText mEtTitle1;
    @BindView(R.id.flytHref)
    FrameLayout mFlytHref;
    @BindView(R.id.etText)
    EditText mEtText;

    @BindView(R.id.tvWordNum)
    TextView mTvWordNum;


    @BindView(R.id.tvConfirm)
    TextView mTvConfirm;
    @BindView(R.id.bwv)
    BridgeWebView bwv;
    @BindView(R.id.ivChoicedImg)
    ImageView ivChoicedImg;

    private String mImg;




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
            if (temp.length() > 25) {
                editStart = mEtText.getSelectionStart();
                editEnd = mEtText.getSelectionEnd();
                showTest(mEtText, "你输入的字数已经超过了限制", Snackbar.LENGTH_SHORT, null, null);
                s.delete(editStart - 1, editEnd);
                int tempSelection = editStart;
                mEtText.setText(s);
                mEtText.setSelection(tempSelection - 1);
            }
            mTvWordNum.setText(s.length() + "/25");
        }
    };


    @Override
    public void init() {

        bwv.setBackgroundColor(Color.TRANSPARENT);
        mEtText.addTextChangedListener(mTextWatcher);

        setMyTitle("广告模板制作");
        showBack();
        String htmlUrl = "file:///android_asset/h5/src/adMake.html";
        bwv.getSettings().setDomStorageEnabled(true);
        bwv.getSettings().setUseWideViewPort(true);
        bwv.loadUrl(htmlUrl);

    }


    private void uploadAD() {

        final String title1 = mEtTitle1.getText().toString();
        final String title2 = mEtText.getText().toString();
        if (TextUtils.isEmpty(title1)) {
            showTest("请填写链接地址");
            return;
        }
       /* if (!ConnectUtil.isValid(title1)) {
            showTest("请填写正确的链接地址");
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

        int position = getIntent().getIntExtra("position", 0);

        EventBus.getDefault().post(new VideoEvent(position, mImg, ImageUtil.getimage(mImg), title2, title1));

        finish();
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
    public int createSuccessView() {
        return R.layout.activity_make_video;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK
                && data != null
                && (requestCode == REQUEST_CLIP_IMAGE)) {
            String path = PhotoActionHelper.getOutputPath(data);
            if (path != null) {

                final Uri uri = Uri.parse("file:/" + path);
                mImg = path;

                ImageLoader.getInstance().displayImage(uri.toString(), ivChoicedImg);
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String mOutputPath;
    public static final int REQUEST_CLIP_IMAGE = 2028;

    @OnClick({R.id.ivConfirm, R.id.tvConfirm, R.id.ivChoicedImg})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ivConfirm:
                Logger.i("onclick  onclick onclick ");
                setText(mEtText.getText().toString());
                break;
            case R.id.tvConfirm:
                uploadAD();
                break;
            case R.id.ivChoicedImg:
                ImageUtil.getInstance().chooseImage("开始制作",new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        if (resultList != null && !resultList.isEmpty()) {
                            mImg = resultList.get(0).getPhotoPath();
                            mOutputPath = new File(UIUtils.getExternalCacheDir(), System.currentTimeMillis() + ".jpg").getPath();
                            PhotoActionHelper.clipImage(MakeVideoActivity.this).input(mImg).output(mOutputPath).setExtraHeight(UIUtils.dip2px(165)).maxOutputWidth(640)
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

            bwv.destroy();

        }
        super.onDestroy();
    }
}
