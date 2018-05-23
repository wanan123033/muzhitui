package com.nevermore.muzhitui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.nevermore.muzhitui.PageLookActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.PageGPSBean;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.io.File;
import java.util.Hashtable;
import java.util.List;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.UIUtils;
import base.helper.PhotoActionHelper;
import base.thread.BaseRunnable;
import base.thread.ThreadManager;
import base.view.DragView;
import base.view.LoadingAlertDialog;
import base.view.VideoGpsView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/12/11.
 */

public class QrHomeActivity extends BaseActivityTwoV{
    private static final int QR_CAPTURE = 1255;
    private LoadingAlertDialog mLoadingAlertDialog;


    @BindView(R.id.dragview)
    DragView dragview;
    @Override
    public void init() {
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        showBack();
        setMyTitle("二维码");

        final VideoGpsView videoGpsView = new VideoGpsView(this,6);
        dragview.addDragView(videoGpsView, 550, 850, true, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoGpsView.onClick(v);
            }
        });
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_qr_home;
    }
    @OnClick({R.id.flyt_gen_qr,R.id.flyt_beautify_qr,R.id.flyt_caram_qr,R.id.flyt_qr})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.flyt_gen_qr:  //生成二维码
                baseStartActivity(GenQRCodeActivity.class);
                break;
            case R.id.flyt_beautify_qr:  //美化二维码
                GalleryFinal.openGalleryMuti(133, 1, new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        if (reqeustCode == 133 && resultList != null && !resultList.isEmpty()) {

                            String path = resultList.get(0).getPhotoPath();
                            Result result = scanningImage(path);
                            if(result != null) {
                                Intent intent = new Intent(getApplicationContext(), BeautifyQrActivity.class);
                                intent.putExtra(BeautifyQrActivity.QR_STRING, result.getText());
                                startActivity(intent);
                            }else {
                                showTest("请选择一张未经过美化的二维码图片");
                            }
                        }
                    }

                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {

                    }
                });
                break;
            case R.id.flyt_caram_qr:   //扫一扫
                startActivityForResult(new Intent(getApplicationContext(),CaptureActivity.class),QR_CAPTURE);
                break;
            case R.id.flyt_qr:  //识别二维码
                GalleryFinal.openGalleryMuti(133, 1, new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        if (reqeustCode == 133 && resultList != null && !resultList.isEmpty()) {

                            String path = resultList.get(0).getPhotoPath();
                            Result result = scanningImage(path);
                            if(result != null) {
                                Intent intent = new Intent(getApplicationContext(), RecognitionQrActivity.class);
                                intent.putExtra(RecognitionQrActivity.QR_STRING, result.getText());
                                startActivity(intent);
                            }else {
                                showTest("请选择一张二维码图片");
                            }
                        }
                    }

                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {

                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QR_CAPTURE && data != null){
            String result = data.getStringExtra("result");
            Intent intent = new Intent(getApplicationContext(), RecognitionQrActivity.class);
            intent.putExtra(RecognitionQrActivity.QR_STRING, result);
            startActivity(intent);
        }
    }

    protected Result scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        // DecodeHintType 和EncodeHintType
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 设置二维码内容的编码
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        Bitmap scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小

        int sampleSize = (int) (options.outHeight / (float) 200);

        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);
        int width = scanBitmap.getWidth();
        int height = scanBitmap.getHeight();
        int[] pixels = new int[width * height];
        scanBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        RGBLuminanceSource source = new RGBLuminanceSource(width,height,pixels);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            scanBitmap.recycle();
            return reader.decode(bitmap1, hints);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }

}
