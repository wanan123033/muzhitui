package com.nevermore.muzhitui.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.nevermore.muzhitui.R;
import com.xys.libzxing.zxing.decode.DecodeFormatManager;

import java.util.Hashtable;
import java.util.Vector;

import base.BaseActivityTwoV;
import base.MyLogger;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/11.
 */

public class RecognitionQrActivity extends BaseActivityTwoV{
    public static final String IMG_PATH = "img_path";
    public static final String QR_STRING = "qr_string";

    @BindView(R.id.tv_content)
    TextView tv_content;

    @Override
    public void init() {
        showBack();
        setMyTitle("识别二维码");
        String path = getIntent().getStringExtra(IMG_PATH);
        if(!TextUtils.isEmpty(path)) {
            String content = analyticBitmap(BitmapFactory.decodeFile(path));
            tv_content.setText(content);
        }else {
            String content = getIntent().getStringExtra(QR_STRING);
            tv_content.setText(content);
        }


    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_recognition_qr;
    }

    @OnClick({R.id.btn_genQr,R.id.btn_copy})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_genQr:
                Intent intent = new Intent(getApplicationContext(), StringGenQRActivity.class);
                intent.putExtra(StringGenQRActivity.QRStr, tv_content.getText().toString());
                startActivity(intent);
                break;
            case R.id.btn_copy:
                ClipboardManager cmb = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(tv_content.getText().toString().trim());
                showTest("复制成功");
                break;
        }
    }

    public static String analyticBitmap(Bitmap mBitmap) {

        MultiFormatReader multiFormatReader = new MultiFormatReader();

        // 解码的参数
        Hashtable<DecodeHintType, Object> hints = new Hashtable<>();
        // 可以解析的编码类型
        Vector<BarcodeFormat> decodeFormats = new Vector<>();
        if (decodeFormats == null || decodeFormats.isEmpty()) {
            decodeFormats = new Vector<>();
            // 这里设置可扫描的类型
            decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);//二维码
        }
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

        // 设置解析配置参数
        multiFormatReader.setHints(hints);

        // 开始对图像资源解码
        Result rawResult = null;
        try {
            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();
            int[] pixels = new int[width * height];
            mBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
            rawResult = multiFormatReader.decode(new BinaryBitmap(new HybridBinarizer(source)));
            mBitmap.recycle();
            return rawResult.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
