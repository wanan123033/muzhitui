package base.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nevermore.muzhitui.PageLookActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.WxAddFriendActivity;
import com.nevermore.muzhitui.module.bean.PageGPSBean;
import com.nevermore.muzhitui.module.network.WorkService;

import base.SPUtils;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/1/24.
 */

public class VideoGpsView extends RelativeLayout implements View.OnClickListener{

    private int gpsFlag;
    private String jumpUrl;

    private CompositeSubscription mCompositeSubscription;

    public VideoGpsView(Context context) {
        super(context);
        initView(context,null);
    }

    public VideoGpsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    public VideoGpsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }

    public VideoGpsView(Context context, int gpsflag) {
        super(context);
        this.gpsFlag = gpsflag;
        initView(context,null);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_video_gps,this);
        ImageView iv_video = findViewById(R.id.iv_video);
        RotateAnimation anim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(2000);//设置动画持续周期
        anim.setRepeatCount(Integer.MAX_VALUE);//设置重复次数
        anim.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        iv_video.setAnimation(anim);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.video_gps);
            gpsFlag = typedArray.getInt(R.styleable.video_gps_gpsflag, 0);
            typedArray.recycle();
        }
//        setOnClickListener(this);



        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().getOprationGPSByNumber((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),gpsFlag)).subscribe(new Subscriber<PageGPSBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(PageGPSBean pageGPSBean) {
                if ("1".equals(pageGPSBean.state)){
                    jumpUrl = pageGPSBean.jump_url;
                    if (TextUtils.isEmpty(jumpUrl)){
                        setVisibility(GONE);
                    }else {
                        setVisibility(VISIBLE);
                    }
                }else {
                    setVisibility(GONE);
                }
            }
        }));
    }



    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }
    public Observable wrapObserverWithHttp(Observable observable) {
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void onClick(View v) {
        if (!TextUtils.isEmpty(jumpUrl)) {
            Intent intent = new Intent(getContext(), PageLookActivity.class);
            intent.putExtra(PageLookActivity.KEY_URL, jumpUrl);
            intent.putExtra("isOriginal",true);
            getContext().startActivity(intent);
        }
    }
}
