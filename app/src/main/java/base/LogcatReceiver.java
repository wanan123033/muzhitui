package base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import base.network.RetrofitUtil;
import base.thread.BaseRunnable;
import base.thread.ThreadManager;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/11/13.
 */

public class LogcatReceiver extends BroadcastReceiver {

    private String url = RetrofitUtil.API_URL + "song/appBugMessageApi/collectBugMessage";

    @Override
    public void onReceive(final Context context, Intent intent) {
        MyLogger.kLog().e("正在发送日志信息...");
        final String phoneSystem = android.os.Build.MODEL + ","
                + android.os.Build.VERSION.SDK + ","
                + android.os.Build.VERSION.RELEASE;
        final String versionName = "3.8.1";


            ThreadManager.getInstance().run(new BaseRunnable() {
                @Override
                public void run() {
                    try {
                        StringBuffer sb = new StringBuffer();
                        final File file = new File("/sdcard/crash/muzhitui.log");
                        if(file.exists()) {
                            FileInputStream fis = new FileInputStream(file);
                            int len = 0;
                            byte[] buf = new byte[1024];
                            while ((len = fis.read(buf)) != -1) {
                                sb.append(new String(buf, 0, len));
                            }
                            fis.close();

                            OkHttpUtils.post().url(url)
                                    .addParams("app_version", versionName)
                                    .addParams("source", "1")
                                    .addParams("loginId_mzt", (String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))
                                    .addParams("name", (String) SPUtils.get(SPUtils.KEY_USER_NAME,""))
                                    .addParams("phone_system", phoneSystem)
                                    .addParams("bug_msg", sb.toString()).build().execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    MyLogger.kLog().e(e);
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    MyLogger.kLog().e("服务器消息：" + response);
                                    if(response.contains("\"state\":\"1\"")){
                                        file.delete();
                                    }
                                }
                            });
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });


    }
}
