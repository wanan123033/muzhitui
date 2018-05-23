package base;

import android.util.Log;

public class LogUtil {

	private static int level = 10;

	private static int info = 1;
	
	private final static String TAG = "baseLog";
	public static void i(String tag, String msg) {
		if (level > info)
			Log.i(tag, msg);
	}
	
	public static void i(Object msg) {
		if (level > info)
			Log.i(TAG, String.valueOf(msg));
	}
}
