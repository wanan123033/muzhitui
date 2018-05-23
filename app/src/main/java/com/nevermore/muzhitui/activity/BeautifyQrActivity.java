package com.nevermore.muzhitui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.Result;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.editPhoto.ImageItem;
import com.nevermore.muzhitui.activity.editPhoto.OriginalArticleActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.ImageUtil;
import base.SPUtils;
import base.UIUtils;
import base.helper.PhotoActionHelper;
import base.thread.BaseRunnable;
import base.thread.ThreadManager;
import base.util.QrUtil;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by Administrator on 2017/12/12.
 */

public class BeautifyQrActivity extends BaseActivityTwoV{
    public static final String QR_STRING = "qr_string";

    @BindView(R.id.rel_sponge_qr)
    RelativeLayout rel_sponge_qr;
    @BindView(R.id.rel_squirrels_qr)
    RelativeLayout rel_squirrels_qr;
    @BindView(R.id.rel_redxin_qr)
    RelativeLayout rel_redxin_qr;
    @BindView(R.id.rel_redji_qr)
    RelativeLayout rel_redji_qr;
    @BindView(R.id.rel_doraemon_qr)
    RelativeLayout rel_doraemon_qr;
    @BindView(R.id.rel_xk_qr)
    RelativeLayout rel_xk_qr;
    @BindView(R.id.rel_save_qr)
    RelativeLayout rel_save_qr;
    @BindView(R.id.rel_11)
    RelativeLayout rel_11;
    @BindView(R.id.rel_xk_12)
    RelativeLayout rel_xk_12;
    @BindView(R.id.rel_xk_cai)
    RelativeLayout rel_xk_cai;
    @BindView(R.id.rel_xk_hong)
    RelativeLayout rel_xk_hong;
    @BindView(R.id.rel_xk_rgb)
    RelativeLayout rel_xk_rgb;
    @BindView(R.id.rel_xk_gift)
    RelativeLayout rel_xk_gift;
    @BindView(R.id.rel_xk_horse)
    RelativeLayout rel_xk_horse;
    @BindView(R.id.rel_xk_mushroom)
    RelativeLayout rel_xk_mushroom;
    @BindView(R.id.rel_xk_wxpay)
    RelativeLayout rel_xk_wxpay;
    @BindView(R.id.rel_xk_alipay)
    RelativeLayout rel_xk_alipay;

    @BindView(R.id.ll_make)
    LinearLayout ll_make;
    @BindView(R.id.ll_icon)
    LinearLayout ll_icon;
    @BindView(R.id.ll_color)
    LinearLayout ll_color;
    @BindView(R.id.iv_color)
    ImageView iv_color;
    @BindView(R.id.iv_icon)
    ImageView iv_icon;
    @BindView(R.id.iv_make)
    ImageView iv_make;
    @BindView(R.id.tvMake)
    TextView tvMake;
    @BindView(R.id.tvColor)
    TextView tvColor;
    @BindView(R.id.tvIcon)
    TextView tvIcon;

    @BindView(R.id.rel_make)
    RelativeLayout rel_make;

    private List<RelativeLayout> makeViews = new ArrayList<>();
    private List<ImageView> titleViews = new ArrayList<>();

    private AlertDialog alertDialog3;
    private LoadingAlertDialog mLoadingAlertDialog;

    private Bitmap bitmap;
    private String content;
    private int color = 0;
    private int iconResId;

    private RelativeLayout makeView;

    @Override
    public void init() {
        showBack();
        setMyTitle("美化二维码");
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        makeViews.clear();
        makeViews.add(rel_sponge_qr);
        makeViews.add(rel_squirrels_qr);
        makeViews.add(rel_redxin_qr);
        makeViews.add(rel_redji_qr);
        makeViews.add(rel_doraemon_qr);
        makeViews.add(rel_xk_qr);
        makeViews.add(rel_save_qr);
        makeViews.add(rel_11);
        makeViews.add(rel_xk_12);
        makeViews.add(rel_xk_cai);
        makeViews.add(rel_xk_hong);
        makeViews.add(rel_xk_rgb);
        makeViews.add(rel_xk_gift);
        makeViews.add(rel_xk_horse);
        makeViews.add(rel_xk_mushroom);
        makeViews.add(rel_xk_wxpay);
        makeViews.add(rel_xk_alipay);

        titleViews.add(iv_color);
        titleViews.add(iv_icon);
        titleViews.add(iv_make);

        makeView = rel_save_qr;
        setTitleView(iv_color);
        showRight("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingAlertDialog.show();
                ThreadManager.getInstance().run(new BaseRunnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = UIUtils.loadBitmapFromView(makeView);
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
                                alertDialog3 = UIUtils.getAlertDialog(BeautifyQrActivity.this, "权限设置", "请允许拇指推APP访问您的通讯录", null, "现在去设置", 0, null, new View.OnClickListener() {
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
        });
        content = getIntent().getStringExtra(QR_STRING);
        if(content == null || content.isEmpty()){
            GalleryFinal.openGalleryMuti(133, 1, new GalleryFinal.OnHanlderResultCallback() {
                @Override
                public void onHanlderSuccess(int reqeustCode, final List<PhotoInfo> resultList) {
                    if (reqeustCode == 133 && resultList != null && !resultList.isEmpty()) {
                        mLoadingAlertDialog.show();
                        ThreadManager.getInstance().run(new BaseRunnable() {
                            @Override
                            public void run() {

                                String path = resultList.get(0).getPhotoPath();
                                final Result result = QrUtil.scanningImage(path);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mLoadingAlertDialog.dismiss();
                                        if(result != null) {
                                            content = result.getText();
                                            initQrView(0x000000,0,rel_save_qr);
//                                            bitmap = QrUtil.makeQRImage(content, 320, 320, 0x000000);
//                                            iv_qrcode.setImageBitmap(bitmap);

                                        }else {
                                            showTest("请选择一张二维码图片");
                                        }
                                    }
                                });

                            }

                            @Override
                            public void run(Object... objs) {
                                super.run(objs);
                            }
                        });

                    }
                }

                @Override
                public void onHanlderFailure(int requestCode, String errorMsg) {

                }
            });
        }else {
            initQrView(0x000000,0,rel_save_qr);
//            bitmap = QrUtil.makeQRImage(content, 320, 320, 0x000000);
//            iv_qrcode.setImageBitmap(bitmap);
        }
    }
    @Override
    public int createSuccessView() {
        return R.layout.activity_beautify_qr;
    }

    @OnClick({R.id.tv_color,R.id.tv_icon,R.id.tv_make,
            R.id.iv_black,R.id.iv_red,R.id.iv_orangered,R.id.iv_orange,R.id.iv_green,R.id.iv_greenyellow,R.id.iv_blue,R.id.iv_c_blue,R.id.iv_blueviolet,R.id.iv_blue_700,R.id.iv_yellow,R.id.iv_dodgerblue,R.id.iv_lightgreen,R.id.iv_steelblue,R.id.iv_firebrick,
            R.id.iv_delete_icon,R.id.iv_custom_photo,R.id.iv_e,R.id.iv_read,R.id.iv_music,R.id.iv_video,R.id.iv_down,R.id.iv_wx,R.id.iv_qq,R.id.iv_taobao,
            R.id.iv_make_sponge,R.id.iv_make_squirrels,R.id.iv_make_redxin,R.id.iv_make_redji,R.id.iv_make_doraemon,R.id.iv_make_xk,R.id.iv_make_11,R.id.iv_make_12,R.id.iv_make_cai,R.id.iv_make_hong,R.id.iv_make_rgb,R.id.iv_make_gift,R.id.iv_make_horse,R.id.iv_make_mushroom,R.id.iv_make_wxpay,R.id.iv_make_alipay})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_color:
                setTitleView(iv_color);
                return;
            case R.id.tv_icon:
                setTitleView(iv_icon);
                return;
            case R.id.tv_make:
                setTitleView(iv_make);
                return;
            case R.id.iv_black:
                color = getResources().getColor(R.color.black);
                selectedColorView(R.id.iv_black);
                break;
            case R.id.iv_firebrick:
                color = getResources().getColor(R.color.firebrick);
                selectedColorView(R.id.iv_firebrick);
                break;
            case R.id.iv_dodgerblue:
                color = getResources().getColor(R.color.dodgerblue);
                selectedColorView(R.id.iv_dodgerblue);
                break;
            case R.id.iv_lightgreen:
                color = getResources().getColor(R.color.lightgreen);
                selectedColorView(R.id.iv_lightgreen);
                break;
            case R.id.iv_steelblue:
                color = getResources().getColor(R.color.steelblue);
                selectedColorView(R.id.iv_steelblue);
                break;
            case R.id.iv_yellow:
                color = getResources().getColor(R.color.yellow);
                selectedColorView(R.id.iv_yellow);
                break;
            case R.id.iv_red:
                color = getResources().getColor(R.color.red);
                selectedColorView(R.id.iv_red);
                break;
            case R.id.iv_blue_700:
                color = getResources().getColor(R.color.liji_material_blue_700);
                selectedColorView(R.id.iv_blue_700);
                break;
            case R.id.iv_orangered:
                color = getResources().getColor(R.color.orangered);
                selectedColorView(R.id.iv_orangered);
                break;
            case R.id.iv_orange:
                color = getResources().getColor(R.color.orange);
                selectedColorView(R.id.iv_orange);
                break;
            case R.id.iv_green:
                color = getResources().getColor(R.color.green);
                selectedColorView(R.id.iv_green);
                break;
            case R.id.iv_greenyellow:
                color = getResources().getColor(R.color.greenyellow);
                selectedColorView(R.id.iv_greenyellow);
                break;
            case R.id.iv_blue:
                color = getResources().getColor(R.color.blue);
                selectedColorView(R.id.iv_blue);
                break;
            case R.id.iv_c_blue:
                color = getResources().getColor(R.color.liji_c_blue);
                selectedColorView(R.id.iv_c_blue);
                break;
            case R.id.iv_blueviolet:
                color = getResources().getColor(R.color.blueviolet);
                selectedColorView(R.id.iv_blueviolet);
                break;

            case R.id.iv_delete_icon:
                iconResId = 0;
                selectedIconView(R.id.iv_delete_icon);
                break;
            case R.id.iv_custom_photo:
                GalleryFinal.openGallerySingle(345, new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        if (reqeustCode == 345 && resultList != null && !resultList.isEmpty()){
                            String inputPath = resultList.get(0).getPhotoPath();
                            String mOutputPath = new File(UIUtils.getExternalCacheDir(), System.currentTimeMillis() + ".jpg").getPath();
                            PhotoActionHelper.clipImage(BeautifyQrActivity.this).input(inputPath).output(mOutputPath).setExtraHeight(UIUtils.dip2px(360)).maxOutputWidth(UIUtils.dip2px(400))
                                    .requestCode(520).start();

                        }
                    }
                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {

                    }
                });
                return;
            case R.id.iv_e:
                iconResId = R.mipmap.iv_e;
                selectedIconView(R.id.iv_e);
                break;
            case R.id.iv_read:
                iconResId = R.mipmap.iv_read;
                selectedIconView(R.id.iv_read);
                break;
            case R.id.iv_music:
                iconResId = R.mipmap.iv_music;
                selectedIconView(R.id.iv_music);
                break;
            case R.id.iv_video:
                iconResId = R.mipmap.iv_video;
                selectedIconView(R.id.iv_video);
                break;
            case R.id.iv_down:
                iconResId = R.mipmap.iv_down;
                selectedIconView(R.id.iv_down);
                break;
            case R.id.iv_wx:
                iconResId = R.mipmap.iv_wx;
                selectedIconView(R.id.iv_wx);
                break;
            case R.id.iv_qq:
                iconResId = R.mipmap.iv_qq;
                selectedIconView(R.id.iv_qq);
                break;
            case R.id.iv_taobao:
                iconResId = R.mipmap.iv_taobao;
                selectedIconView(R.id.iv_taobao);
                break;

            case R.id.iv_make_sponge:
                makeView = rel_sponge_qr;
                selectedMakeView(R.id.iv_make_sponge);
                break;
            case R.id.iv_make_squirrels:
                makeView = rel_squirrels_qr;
                selectedMakeView(R.id.iv_make_squirrels);
                break;
            case R.id.iv_make_redxin:
                makeView = rel_redxin_qr;
                selectedMakeView(R.id.iv_make_redxin);
                break;
            case R.id.iv_make_redji:
                makeView = rel_redji_qr;
                selectedMakeView(R.id.iv_make_redji);
                break;
            case R.id.iv_make_doraemon:
                makeView = rel_doraemon_qr;
                selectedMakeView(R.id.iv_make_doraemon);
                break;
            case R.id.iv_make_xk:
                makeView = rel_xk_qr;
                selectedMakeView(R.id.iv_make_xk);
                break;
            case R.id.iv_make_11:
                makeView = rel_11;
                selectedMakeView(R.id.iv_make_11);
                break;
            case R.id.iv_make_12:
                makeView = rel_xk_12;
                selectedMakeView(R.id.iv_make_12);
                break;
            case R.id.iv_make_cai:
                makeView = rel_xk_cai;
                selectedMakeView(R.id.iv_make_cai);
                break;
            case R.id.iv_make_hong:
                makeView = rel_xk_hong;
                selectedMakeView(R.id.iv_make_hong);
                break;
            case R.id.iv_make_rgb:
                makeView = rel_xk_rgb;
                selectedMakeView(R.id.iv_make_rgb);
                break;
            case R.id.iv_make_gift:
                makeView = rel_xk_gift;
                selectedMakeView(R.id.iv_make_gift);
                break;
            case R.id.iv_make_horse:
                makeView = rel_xk_horse;
                selectedMakeView(R.id.iv_make_horse);
                break;
            case R.id.iv_make_mushroom:
                makeView = rel_xk_mushroom;
                selectedMakeView(R.id.iv_make_mushroom);
                break;
            case R.id.iv_make_wxpay:
                makeView = rel_xk_wxpay;
                selectedMakeView(R.id.iv_make_wxpay);
                break;
            case R.id.iv_make_alipay:
                makeView = rel_xk_alipay;
                selectedMakeView(R.id.iv_make_alipay);
                break;

        }
        initQrView(color,iconResId,makeView);
//        bitmap.recycle();
//        bitmap = null;
//        bitmap = QrUtil.makeQRImage(content,320,320,color);
//        iv_qrcode.setImageBitmap(bitmap);

    }

    private void selectedMakeView(int viewId) {
        for (int i = 0 ; i < ll_make.getChildCount() ; i++){
            ImageView iv = (ImageView) ll_make.getChildAt(i);
            if (iv.getId() == viewId){
                iv.setImageResource(R.mipmap.iv_selected);
            }else {
                iv.setImageBitmap(null);
            }
        }
    }

    private void selectedIconView(int viewId) {
        for (int i = 0 ; i < ll_icon.getChildCount() ; i++){
            ImageView iv = (ImageView) ll_icon.getChildAt(i);
            if (iv.getId() == viewId){
                iv.setImageResource(R.mipmap.iv_selected);
            }else {
                iv.setImageBitmap(null);
            }
        }
    }

    private void selectedColorView(int viewId) {
        for (int i = 0 ; i < ll_color.getChildCount() ; i++){
            ImageView iv = (ImageView) ll_color.getChildAt(i);
            if (iv.getId() == viewId){
                iv.setImageResource(R.mipmap.iv_selected);
            }else {
                iv.setImageBitmap(null);
            }
        }
    }

    private void setTitleView(View view) {
        if (view == iv_color){
            iv_color.setImageResource(R.mipmap.iv_color);
            iv_icon.setImageResource(R.mipmap.iv_iconed);
            iv_make.setImageResource(R.mipmap.iv_makeed);

            ll_color.setVisibility(View.VISIBLE);
            ll_icon.setVisibility(View.GONE);
            ll_make.setVisibility(View.GONE);

            tvColor.setTextColor(Color.GREEN);
            tvIcon.setTextColor(Color.BLACK);
            tvMake.setTextColor(Color.BLACK);
        }else if (view == iv_icon){
            iv_color.setImageResource(R.mipmap.iv_colored);
            iv_icon.setImageResource(R.mipmap.iv_icon);
            iv_make.setImageResource(R.mipmap.iv_makeed);

            ll_color.setVisibility(View.GONE);
            ll_icon.setVisibility(View.VISIBLE);
            ll_make.setVisibility(View.GONE);

            tvColor.setTextColor(Color.BLACK);
            tvIcon.setTextColor(Color.GREEN);
            tvMake.setTextColor(Color.BLACK);
        }else if (view == iv_make){
            iv_color.setImageResource(R.mipmap.iv_colored);
            iv_icon.setImageResource(R.mipmap.iv_iconed);
            iv_make.setImageResource(R.mipmap.iv_make);

            ll_color.setVisibility(View.GONE);
            ll_icon.setVisibility(View.GONE);
            ll_make.setVisibility(View.VISIBLE);

            tvColor.setTextColor(Color.BLACK);
            tvIcon.setTextColor(Color.BLACK);
            tvMake.setTextColor(Color.GREEN);
        }
    }

    private String filePath = null;
    private void initQrView(int color,int iconResId,RelativeLayout makeView){
        ImageView iv = (ImageView) makeView.getChildAt(0);
        try {
            if(TextUtils.isEmpty(filePath))
                filePath = ImageUtil.createImageFile().getAbsolutePath();
            int width = iv.getWidth();
            int height = iv.getHeight() ;
            if (width <= 0){
                width = 320;
            }
            if (height <= 0){
                height = 320;
            }
            boolean isQrImage = QrUtil.createQRImage(content, width, height, color,filePath);
            if (isQrImage) {
                bitmap = BitmapFactory.decodeFile(filePath);
                bitmap = QrUtil.addLogo(bitmap, BitmapFactory.decodeResource(getResources(), iconResId));
                iv.setImageBitmap(bitmap);
            }
            showMakeView(makeView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initQrView(int color,String iconPath,RelativeLayout makeView){
        ImageView iv = (ImageView) makeView.getChildAt(0);
        try {
            if(TextUtils.isEmpty(filePath))
                filePath = ImageUtil.createImageFile().getAbsolutePath();
            int width = iv.getWidth();
            int height = iv.getHeight() ;
            if (width <= 0){
                width = 320;
            }
            if (height <= 0){
                height = 320;
            }
            boolean isQrImage = QrUtil.createQRImage(content, width, height, color,filePath);
            if (isQrImage) {
                bitmap = BitmapFactory.decodeFile(filePath);
                bitmap = QrUtil.addLogo(bitmap, BitmapFactory.decodeFile(iconPath));
                iv.setImageBitmap(bitmap);
            }
            showMakeView(makeView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showMakeView(RelativeLayout makeView) {
        for (int i = 0 ; i < makeViews.size() ; i++){
            if(makeView == makeViews.get(i)){
                makeViews.get(i).setVisibility(View.VISIBLE);
            }else {
                makeViews.get(i).setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null){
            bitmap.recycle();
            bitmap = null;
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 520://标题图片（拍照和从相册获取）
                if(resultCode == Activity.RESULT_OK && data != null){
                    String outputPath = PhotoActionHelper.getOutputPath(data);
                    initQrView(color,outputPath,makeView);
                }
                break;



        }
    }
}
