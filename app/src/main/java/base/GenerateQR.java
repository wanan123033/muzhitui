package base;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nevermore.muzhitui.R;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hehe on 2016/4/19.
 */
public class GenerateQR extends BaseActivityTwoV {
    public static final String VALUE = "value";
    @BindView(R.id.ivQr)
    ImageView mIvQR;
    Encoder mEncoder;

    @Override
    public void init() {
        final String value = getIntent().getStringExtra(VALUE);
        mEncoder = new Encoder.Builder()
                .setBackgroundColor(0xFFFFFF)
                .setCodeColor(0xFF000000)
                .setOutputBitmapPadding(0)
                .setOutputBitmapWidth(500)
                .setOutputBitmapHeight(500)
                .build();
        Observable.just(value).map(new Func1<String, Bitmap>() {
            @Override
            public Bitmap call(String s) {
                l(Thread.currentThread().toString());
                return mEncoder.encode(value);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                mIvQR.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_generateqr;
    }


    public void onClick(View v) {

    }
}
