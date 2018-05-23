package com.nevermore.muzhitui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import com.nevermore.muzhitui.R;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

import base.BaseActivityTwoV;
import base.ImageUtil;
import base.SPUtils;
import base.UIUtils;
import base.thread.BaseRunnable;
import base.thread.ThreadManager;
import base.util.QrUtil;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/11.
 */

public class StringGenQRActivity extends BaseActivityTwoV{
    public static final String QRStr = "qr_str";

    @BindView(R.id.iv_qrcode)
    ImageView iv_qrcode;

    private LoadingAlertDialog mLoadingAlertDialog;
    private AlertDialog alertDialog3;

    private String qrStr;

    @Override
    public void init() {
        showBack();
        setMyTitle("二维码");
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        qrStr = getIntent().getStringExtra(QRStr);
        iv_qrcode.setImageBitmap(EncodingUtils.createQRCode(qrStr, 350, 350,null));
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_string_gen_qr;
    }

    @OnClick({R.id.btn_saveQr,R.id.btn_genQr})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_saveQr:  //保存二维码
                saveQr();
                break;
            case R.id.btn_genQr:   //美化二维码
                Intent intent = new Intent(this,BeautifyQrActivity.class);
                intent.putExtra(BeautifyQrActivity.QR_STRING,qrStr);
                startActivity(intent);
                break;
        }
    }

    private void saveQr() {
        mLoadingAlertDialog.show();
        ThreadManager.getInstance().run(new BaseRunnable() {
            @Override
            public void run() {
                Bitmap bitmap = UIUtils.loadBitmapFromView(iv_qrcode);
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
                        alertDialog3 = UIUtils.getAlertDialog(StringGenQRActivity.this, "权限设置", "请允许拇指推APP访问您的通讯录", null, "现在去设置", 0, null, new View.OnClickListener() {
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
}
