package base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nevermore.muzhitui.R;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class UIUtils {

    /**
     * 获取上下文
     *
     * @return
     */
    public static Context getContext() {
        return BaseApplication.getApplication();
    }


    /**
     * dip转换px
     */
    public static int dip2px(int dip) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * pxz转换dip
     */
    public static int px2dip(int px) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 获取主线程的handler
     */
    public static Handler getHandler() {
        return BaseApplication.getMainThreadHandler();
    }

    /**
     * 延时在主线程执行runnable
     */
    public static boolean postDelayed(Runnable runnable, long delayMillis) {
        return getHandler().postDelayed(runnable, delayMillis);
    }

    /**
     * 在主线程执行runnable
     */
    public static boolean post(Runnable runnable) {
        return getHandler().post(runnable);
    }

    /**
     * 从主线程looper里面移除runnable
     */
    public static void removeCallbacks(Runnable runnable) {
        getHandler().removeCallbacks(runnable);
    }

    /**
     * 获取布局
     *
     * @param resId
     * @return
     */
    public static View inflate(Context context, int resId) {
        return LayoutInflater.from(context).inflate(resId, null);
    }

    /**
     * 获取资源
     */
    public static Resources getResources() {

        return getContext().getResources();
    }

    /**
     * 获取文字
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 获取文字数组
     */
    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 获取dimen
     */
    public static int getDimens(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    /**
     * 获取drawable
     */
    public static Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    /**
     * 获取颜色
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 获取颜色选择器
     */
    public static ColorStateList getColorStateList(int resId) {
        return getResources().getColorStateList(resId);
    }


    public static Drawable getBoundDrawable(int drawableId) {
        Drawable drawable = getContext().getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        return drawable;
    }

    public static String getFormatDate(String format, long now) {
        DateFormat formatter = new SimpleDateFormat(format);// "yyyy-MM-dd"
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        return formatter.format(calendar.getTime());
    }

    public static String getOffsetTime(long end, long start) {
        return (end - start) / 100 / 60 / 60 / 10f + "";
    }

    public static long getMillis(String format, String start) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        /*
         * String start="2011-09-20 12:30:45"; String end ="2011-10-20 6:30:00";
         */
        // 得到毫秒数
        try {
            long timeStart = sdf.parse(start).getTime();
            return timeStart;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean isWXAppInstalledAndSupported() {
        IWXAPI api = WXAPIFactory.createWXAPI(getContext(), SPUtils.KEY_WXID);
        boolean sIsWXAppInstalledAndSupported = api.isWXAppInstalled()
                && api.isWXAppSupportAPI();
        if (!sIsWXAppInstalledAndSupported) {

        }
        return sIsWXAppInstalledAndSupported;
    }

    public static String getSubOneYear(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = null;
        try {
            dt = sdf.parse(date);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.YEAR, 1);//日期减1年
/*            rightNow.add(Calendar.MONTH,-3);//日期加-3个月
            rightNow.add(Calendar.DAY_OF_YEAR,10);//日期加10天*/
            Date dt1 = rightNow.getTime();
            String reStr = sdf.format(dt1);
            return reStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String addOneYear(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = date;
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.YEAR, 1);//日期减1年
/*            rightNow.add(Calendar.MONTH,-3);//日期加-3个月
            rightNow.add(Calendar.DAY_OF_YEAR,10);//日期加10天*/
        Date dt1 = rightNow.getTime();
        String reStr = sdf.format(dt1);
        return reStr;
    }

   /* public static boolean isWXAppInstalledAndSupported() {
        IWXAPI api = WXAPIFactory.createWXAPI(getContext(), Constant.APP_ID);
        boolean sIsWXAppInstalledAndSupported = api.isWXAppInstalled()
                && api.isWXAppSupportAPI();
        if (!sIsWXAppInstalledAndSupported) {
            UIUtils.showToast("微信未安装");
        }
        return sIsWXAppInstalledAndSupported;
    }*/

    public static boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
        }
        return version;
    }

    public static File getExternalCacheDir() {
        File cacheDir = getContext().getExternalCacheDir();
        File file = new File(cacheDir.getAbsoluteFile() + File.separator + "makeimg");
        if (!file.exists()) {
            file.mkdir();
            LogUtil.i("file = " + file.getAbsolutePath() + "   " + file.getPath());
        }
        return file;
    }

    public static void copy(String str) {
        ClipboardManager myClipboard = (ClipboardManager) getContext().getSystemService(getContext().CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", str);

        myClipboard.setPrimaryClip(myClip);
    }


    public static void close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static AlertDialog getBaseAlertDialog(Activity activity) {
        AlertDialog dialog = new AlertDialog.Builder(activity).create();  //内存泄露的危险
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }


    public static AlertDialog getAlertDialog(Activity activity, String title, String content, String left, String right, int drawableResource, View.OnClickListener leftOnClick, View.OnClickListener rightOnClick) {
        AlertDialog dialog = new AlertDialog.Builder(activity).create();  //内存泄露的危险
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        /**设置在加载时是否可点击返回键，点击返回键是否有效**/
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getRepeatCount() == 0) {
                    //  dialog.dismiss();

                }
                return true;
            }
        });
        View view = UIUtils.inflate(getContext(), R.layout.dialog_mypolicy_flowerless);
        dialog.setView(view);

        TextView titleView = (TextView) view.findViewById(R.id.tvTitle);
        TextView contentView = (TextView) view.findViewById(R.id.tvContent);
        TextView cancelView = (TextView) view.findViewById(R.id.tvCancle);
        TextView confirmView = (TextView) view.findViewById(R.id.tvConfirm);
        View view1 = (View) view.findViewById(R.id.dialog_view);

        if (TextUtils.isEmpty(title)) {
            titleView.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
        } else {
            titleView.setText(title);
        }
        contentView.setText(content);
        if (TextUtils.isEmpty(left)) {
            cancelView.setVisibility(View.GONE);
        } else {
            cancelView.setText(left);
        }
        if ("现在去设置".equals(right)) {
            confirmView.setTextColor(Color.parseColor("#3F5699"));
        }
        if (TextUtils.isEmpty(right)) {
            confirmView.setVisibility(View.GONE);
        } else {
            confirmView.setText(right);
        }
        cancelView.setOnClickListener(leftOnClick);
        confirmView.setOnClickListener(rightOnClick);
        dialog.setCancelable(false);//设置点击对话框页面动画不消失，等加载完成后关闭
        return dialog;
    }

    public static AlertDialog getAlertDialogText(Activity activity, String title, String content, String left, String right, View.OnClickListener leftOnClick, View.OnClickListener rightOnClick) {
        AlertDialog dialog = new AlertDialog.Builder(activity).create();  //内存泄露的危险
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getRepeatCount() == 0) {
                    //  dialog.dismiss();

                }
                return true;
            }
        });
        View view = UIUtils.inflate(getContext(), R.layout.dialog_text);
        dialog.setView(view);
        TextView titleView = (TextView) view.findViewById(R.id.tvTitle);
        TextView contentView = (TextView) view.findViewById(R.id.tvContent);
        TextView cancelView = (TextView) view.findViewById(R.id.tvCancle);
        TextView confirmView = (TextView) view.findViewById(R.id.tvConfirm);
        if (TextUtils.isEmpty(title)) {
            titleView.setVisibility(View.GONE);
        } else {
            titleView.setText(title);
        }
        contentView.setText(content);
        if (TextUtils.isEmpty(left)) {
            cancelView.setVisibility(View.GONE);
        } else {
            cancelView.setText(left);
        }
        confirmView.setText(right);
        cancelView.setOnClickListener(leftOnClick);
        confirmView.setOnClickListener(rightOnClick);
        dialog.setCancelable(false);//设置点击对话框页面动画不消失，等加载完成后关闭
        return dialog;
    }

    public static String getImagPath(String str) {
        return getExternalCacheDir() + File.separator + str;
    }

    public static boolean deleteFile(String path) {
        File file = new File(path);
        return file.exists() && file.isFile() && file.delete();
    }

    public static Bitmap loadBitmapFromView(View v) {
        if (v == null) {
            return null;
        }
        Bitmap screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(screenshot);
        c.translate(-v.getScrollX(), -v.getScrollY());
        v.draw(c);
        return screenshot;
    }


}
