package base;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.nevermore.muzhitui.module.bean.ExceInfo;
import com.nevermore.muzhitui.module.network.WorkService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.nevermore.muzhitui.R2.id.sb;

/**
 * Created by Administrator on 2017/11/13.
 */

public class LogcatService extends IntentService {

    private CompositeSubscription mCompositeSubscription;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public LogcatService() {
        super("LogcatService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        MyLogger.kLog().e("==========服务一起动=====");
        String phoneSystem = android.os.Build.MODEL + ","
                + android.os.Build.VERSION.SDK + ","
                + android.os.Build.VERSION.RELEASE;
        String versionName = intent.getStringExtra("versionName");
        StringBuffer sb = new StringBuffer();
        try {
            FileInputStream fis = new FileInputStream("/sdcard/crash/muzhitui.log");
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = fis.read(buf)) != -1){
                sb.append(new String(buf,0,len));
            }
            fis.close();
            new File("/sdcard/crash/muzhitui.log").delete();
        }catch (IOException e){
            e.printStackTrace();
        }

        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().collectBugMessage((String)SPUtils.get(SPUtils.GET_LOGIN_ID,""),
                versionName,1,(String)SPUtils.get(SPUtils.KEY_GET_NAME,""),phoneSystem,sb.toString())).subscribe(new Subscriber<ExceInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                //退出程序
                stopSelf();
            }

            @Override
            public void onNext(ExceInfo exceInfo) {
                Log.i("TAG",exceInfo.toString());
                stopSelf();
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
}
