package base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.X5Service;
import com.nevermore.muzhitui.activity.rongyun.MztRongContext;
import com.nevermore.muzhitui.activity.rongyun.message.TestMessage;
import com.nevermore.muzhitui.activity.rongyun.message.provider.ContactNotificationMessageProvider;
import com.nevermore.muzhitui.activity.rongyun.message.provider.TestMessageProvider;
import com.nevermore.muzhitui.receiver.ExampleUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.orhanobut.logger.AndroidLogTool;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.xutils.x;

import java.util.Set;

import base.view.refresh.SmartRefreshLayout;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sharesdk.framework.ShareSDK;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.RealTimeLocationMessageProvider;
import io.rong.imlib.ipc.RongExceptionHandler;
import io.rong.push.RongPushClient;

import static android.content.ContentValues.TAG;


/**
 * Created by gk on 2016/1/10.
 */
public class BaseApplication extends Application {


    private static BaseApplication baseApplication;
    private static Handler mHandler = new Handler();
    public static final String YOUR_TAG = "baseLog";
    public static FunctionConfig mFunctionConfig;

    @Override
    public void onCreate() {
        //TODO 用户运行日志采集
        logcat();
        super.onCreate();
        //查询本地异常信息，并发送给服务器。
//        startService(new Intent(this, SystemLogService.class));

        //异常测试
//        String i = null;
//        i.length();
//        环信
        baseApplication = this;

        //建议在application中配置
        //设置主题
        ThemeConfig theme = ThemeConfig.DEFAULT;
        //配置功能
        mFunctionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(false)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true).setMutiSelectMaxSize(8)
                .build();
        CoreConfig coreConfig = new CoreConfig.Builder(this, new UILImageLoader(), theme)
                .setFunctionConfig(mFunctionConfig)
                .setPauseOnScrollListener(new UILPauseOnScrollListener(false, true))
                .build();
        GalleryFinal.init(coreConfig);
        initImageLoader(this);

        Logger
                .init(YOUR_TAG)                 // default PRETTYLOGGER or use just init()
                .methodCount(3)                 // default 2
                //.hideThreadInfo()               // default shown
                // .logLevel(LogLevel.NONE)        // default LogLevel.FULL
                .methodOffset(2)                // default 0
                .logTool(new AndroidLogTool()); // custom log tool, optional

        //shareSDK 登录和分享
        ShareSDK.initSDK(UIUtils.getContext());
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        //支付

        final IWXAPI msgApi = WXAPIFactory.createWXAPI(UIUtils.getContext(), SPUtils.KEY_WXID,false);
        msgApi.registerApp(SPUtils.KEY_WXID);
        x.Ext.init(this);


//        try {
//            RongPushClient.registerGCM(this);
//        } catch (RongException e) {
//            e.printStackTrace();
//        }
        /**
         *
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */

        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(this);
            try {
                MztRongContext.init(this);
                Thread.setDefaultUncaughtExceptionHandler(new RongExceptionHandler(this));
                RongPushClient.checkManifest(this);//检查融云推送消息
                RongIM.registerMessageTemplate(new ContactNotificationMessageProvider());
                RongIM.registerMessageTemplate(new RealTimeLocationMessageProvider());
                RongIM.registerMessageType(TestMessage.class);
                RongIM.registerMessageTemplate(new TestMessageProvider());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent(this, X5Service.class);
        startService(intent);
//        RongIM.getInstance().setSendMessageListener(MztRongContext.getInstance());//设置发出消息接收监听器.

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        if((boolean)SPUtils.get("setAlias",false))
            setAlias();

    }

    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    private void setAlias() {
        String alias = (String) SPUtils.get(SPUtils.GET_LOGIN_ID,"");

        if (TextUtils.isEmpty(alias)) {
//            Toast.makeText(this, R.string.error_alias_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!ExampleUtil.isValidTagAndAlias(alias)) {
//            Toast.makeText(this,R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        // 调用 Handler 来异步设置别名
        mJPushHandler.sendMessage(mJPushHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private final TagAliasCallback mAliasCallback =  new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    SPUtils.put("setAlias",true);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mJPushHandler.sendMessageDelayed(mJPushHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mJPushHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

    private void logcat() {
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }


    /**
     * 获得当前进程的名字
     * @param context
     * @return
     */
    @Nullable
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }


    public static Context getApplication() {
        return baseApplication;
    }

    public static Handler getMainThreadHandler() {
        return mHandler;
    }

    private void initImageLoader(Context context) {
        // This configuration tuning is Custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY)
                .cacheOnDisk(true).build();
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        config.threadPoolSize(3);

        config.memoryCache(new WeakMemoryCache());

        config.memoryCacheSizePercentage(13);

        config.defaultDisplayImageOptions(defaultOptions).writeDebugLogs() ;// Remove
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }
}
