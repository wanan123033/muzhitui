package base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.editPhoto.OriginalArticleActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.FileOutputStream;

import base.helper.PhotoActionHelper;
import base.thread.BaseRunnable;
import base.thread.ThreadManager;
import base.util.CacheUtil;
import base.view.ClipImageView;

/**
 * Created by gk on 2016/3/8.
 */
public class ClipImagActivity extends BaseActivityTwoV implements View.OnClickListener {
    private TextView mTvCancle;
    private TextView mTvOk;
    private ClipImageView mClipImageView;

    private String mOutput;
    private String mInput;
    private int mMaxWidth;


    // 图片被旋转的角度
    private int mDegree;
    // 大图被设置之前的缩放比例
    private int mSampleSize;
    private int mSourceWidth;
    private int mSourceHeight;

    private ProgressDialog mDialog;
    private int mBorderHeight;

    private Integer type = 0;

    @Override
    public void init() {
        hideActionBar();
        View view = getmSuccessView();
        type = (Integer) CacheUtil.getInstance().remove(CacheUtil.FORMAT_FRTE_TYPE);
        mTvCancle = (TextView) view.findViewById(R.id.tvCancle);
        mTvOk = (TextView) view.findViewById(R.id.tvConfirm);
        mClipImageView = (ClipImageView) view.findViewById(R.id.civ);
        mTvCancle.setOnClickListener(this);
        mTvOk.setOnClickListener(this);
        final Intent data = getIntent();
        mOutput = PhotoActionHelper.getOutputPath(data);
        mInput = PhotoActionHelper.getInputPath(data);
        mMaxWidth = PhotoActionHelper.getMaxOutputWidth(data);
        mBorderHeight = PhotoActionHelper.getExtraHeight(data);

        mClipImageView.setBorderHeight(mBorderHeight);
//        setImageAndClipParams();
        final Uri uri = Uri.parse("file:/" + mInput);

        ImageLoader.getInstance().displayImage(uri.toString(), mClipImageView, ImageUtil.getInstance().getBaseDisplayOption());

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("正在截取图片");

        if(type == null){
            return;
        }

        if (type == 1){
            mTvCancle.setText("取消");
            mTvCancle.setCompoundDrawables(null,null,null,null);
        }

    }

    @Override
    public int createSuccessView() {
        //  setTitle("设置");
        return R.layout.activity_clip;
    }

    private static int findBestSample(int origin, int target) {
        int sample = 1;
        for (int out = origin / 2; out > target; out /= 2) {
            sample *= 2;
        }
        return sample;
    }

    private void clipImage() {
        if (mOutput != null) {
            mDialog.show();

            ThreadManager.getInstance().run(new BaseRunnable() {
                @Override
                public void run() {
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(mOutput);
                        Bitmap bitmap = getClippedBitmap();
                        ImageUtil.getInstance().decodeBitmapFromBitmap(bitmap, mMaxWidth, mBorderHeight, fos);
//              /          bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        if (!bitmap.isRecycled()) {
                            bitmap.recycle();
                        }
                        setResult(Activity.RESULT_OK, getIntent());
                    } catch (Exception e) {
                        Toast.makeText(ClipImagActivity.this, "不能保存图片", Toast.LENGTH_SHORT).show();
                    } finally {
                        UIUtils.close(fos);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.dismiss();
                            finish();
                        }
                    });
                }
            });
        } else {
            finish();
        }
    }


    private Bitmap getClippedBitmap() {
        if (mSampleSize <= 1) {
            return mClipImageView.clip();
        }

        // 获取缩放位移后的矩阵值
        final float[] matrixValues = mClipImageView.getClipMatrixValues();
        final float scale = matrixValues[Matrix.MSCALE_X];
        final float transX = matrixValues[Matrix.MTRANS_X];
        final float transY = matrixValues[Matrix.MTRANS_Y];

        // 获取在显示的图片中裁剪的位置
        final Rect border = mClipImageView.getClipBorder();
        final float cropX = ((-transX + border.left) / scale) * mSampleSize;
        final float cropY = ((-transY + border.top) / scale) * mSampleSize;
        final float cropWidth = (border.width() / scale) * mSampleSize;
        final float cropHeight = (border.height() / scale) * mSampleSize;

        // 获取在旋转之前的裁剪位置
        final RectF srcRect = new RectF(cropX, cropY, cropX + cropWidth, cropY + cropHeight);
        final Rect clipRect = getRealRect(srcRect);

        final BitmapFactory.Options ops = new BitmapFactory.Options();
        final Matrix outputMatrix = new Matrix();

        outputMatrix.setRotate(mDegree);
        // 如果裁剪之后的图片宽高仍然太大,则进行缩小
        if (mMaxWidth > 0 && cropWidth > mMaxWidth) {
            ops.inSampleSize = findBestSample((int) cropWidth, mMaxWidth);

            final float outputScale = mMaxWidth / (cropWidth / ops.inSampleSize);
            outputMatrix.postScale(outputScale, outputScale);
        }

        // 裁剪
        BitmapRegionDecoder decoder = null;
        try {
            decoder = BitmapRegionDecoder.newInstance(mInput, false);
            final Bitmap source = decoder.decodeRegion(clipRect, ops);
            recycleImageViewBitmap();
            return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), outputMatrix, false);
        } catch (Exception e) {
            return mClipImageView.clip();
        } finally {
            if (decoder != null && !decoder.isRecycled()) {
                decoder.recycle();
            }
        }
    }

    private Rect getRealRect(RectF srcRect) {
        final int reverseDegree = 360 - mDegree;// 矩形应该旋转的角度与图片需要旋转的角度方向相反
        switch (reverseDegree) {
            case 90:
                return new Rect((int) (mSourceWidth - srcRect.bottom), (int) srcRect.left,
                        (int) (mSourceWidth - srcRect.top), (int) srcRect.right);
            case 180:
                return new Rect((int) (mSourceHeight - srcRect.right), (int) (mSourceWidth - srcRect.bottom),
                        (int) (mSourceHeight - srcRect.left), (int) (mSourceWidth - srcRect.top));
            case 270:
                return new Rect((int) srcRect.top, (int) (mSourceHeight - srcRect.right),
                        (int) srcRect.bottom, (int) (mSourceHeight - srcRect.left));
            default:
                return new Rect((int) srcRect.left, (int) srcRect.top, (int) srcRect.right, (int) srcRect.bottom);
        }
    }

    private void recycleImageViewBitmap() {
        mClipImageView.post(new Runnable() {
            @Override
            public void run() {
                mClipImageView.setImageBitmap(null);
            }
        });
    }

    //此标记判断是否点击了取消。如果点击了取消则是取消剪切
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancle:


                onBackPressed();
                break;
            case R.id.tvConfirm:

                clipImage();
                break;
        }
    }

    @Override
    public void onBackPressed() {

        setResult(Activity.RESULT_CANCELED, getIntent());
        super.onBackPressed();
    }
}
