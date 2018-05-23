package base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import base.thread.Handler;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 *
 * @author user
 *
 */
public class CrashHandler implements UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";

    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();
    //程序的Context对象
    private static Context mContext;
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    //用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    public static Handler handler = Handler.getHandler();

    /** 保证只有一个CrashHandler实例 */
    private CrashHandler() {
    }

    /** 获取CrashHandler实例 ,单例模式 */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            }
//            //退出程序
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0x1233, new Handler.HandlerListener() {
                    @Override
                    public void handleMessage(Message msg) {
                        Toast.makeText(mContext,"很抱歉，程序将异常退出！",Toast.LENGTH_LONG).show();
                    }
                });
            }
        }.start();
        //收集设备参数信息
        String versionName = collectDeviceInfo(mContext);
        //保存日志文件
        saveCrashInfo2File(ex,versionName);


        return true;
    }

    /**
     * 收集设备参数信息
     * @param ctx
     */
    public String collectDeviceInfo(Context ctx) {
        String versionName = null;
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
        return versionName;
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return    返回文件名称,便于将文件传送到服务器
     */
    public String saveCrashInfo2File(final Throwable ex, String versionName) {
        MyLogger.kLog().e(ex);
        StringBuffer sb = new StringBuffer();
//        for (Map.Entry<String, String> entry : infos.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            sb.append(key + "=" + value + "\n");
//        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        String phoneSystem = android.os.Build.MODEL + ","
                + android.os.Build.VERSION.SDK + ","
                + android.os.Build.VERSION.RELEASE;
        //上报异常信息

        Log.i("TAG","保存日志信息开始..."+sb.toString());
        //       addSubscription(wrapObserverWithHttp(WorkService.getWorkService().collectBugMessage((String)SPUtils.get(SPUtils.KEY_GET_ID,""),
//                versionName,1,(String)SPUtils.get(SPUtils.KEY_GET_NAME,""),phoneSystem,sb.toString())).subscribe(new Subscriber<ExceInfo>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                //退出程序
//                android.os.Process.killProcess(android.os.Process.myPid());
//                System.exit(1);
//            }
//
//            @Override
//            public void onNext(ExceInfo exceInfo) {
//                Log.i("TAG",exceInfo.toString());
//                //退出程序
//                android.os.Process.killProcess(android.os.Process.myPid());
//                System.exit(1);
//            }
//        }));

        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "muzhitui.log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = "/sdcard/crash/"+fileName;
                File file = new File(path);
                if (!file.exists() || !file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(path);
                fos.write(sb.toString().getBytes());
                fos.close();
//                SPUtils.put(SPUtils.KEY_BUG_MSG,dir.getAbsolutePath());
                Log.i("TAG","保存日志信息成功...");
            }
            //启动上传日志的进程
//            Intent intent1 = new Intent("com.muzhitui.logcat.recevier");
//            mContext.sendBroadcast(intent1);
            //退出
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
//            Process.killProcess(Process.myPid());
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
        Log.i("TAG","-----------------");
        Log.i("TAG",sb.toString());
        Log.i("TAG","-----------------");
        return null;
    }
}