package base.util;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by simone on 2017/6/19.
 */

public class ConnectUtil {
    /**
     * 判断链接是否有效
     * 输入链接
     * 返回true或者false
     */
    public static boolean isValid(String strLink) {
        URL url;
        try {
            url = new URL(strLink);
            HttpURLConnection connt = (HttpURLConnection)url.openConnection();
            connt.setRequestMethod("HEAD");
            String strMessage = connt.getResponseMessage();
            if (strMessage.compareTo("Not Found") == 0) {
                return false;
            }
            connt.disconnect();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
