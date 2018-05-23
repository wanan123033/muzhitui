package base;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class SPUtils {
    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "share_data";
    public static final String KEY_WXUNIONID = "KEY_WXUNIONID";
    public static final String KEY_WXOPENID = "KEY_WXOPENID"; //  country city privilege province language sex
    public static final String KEY_WXHEAD = "KEY_WXHEAD";
    public static final String KEY_WXNICKNAME = "KEY_WXNICKNAME";
    public static final String KEY_ISFIRSTENTER = "KEY_ISFIRSTENTER";
    public static final String KEY_WXID = "wxd1d6178de8a22bf2";
    public static final String KEY_COUNTRY = "COUNTRY";
    public static final String KEY_CITY = "CITY";
    public static final String KEY_PRIVILEGE = "PRIVILEGE";
    public static final String KEY_PROVINCE = "PROVINCE";
    public static final String KEY_LANGUAGE = "LANGUAGE";
    public static final String KEY_SEX = "SEX";
    //用户注册手机号
    public static final String KEY_PHONE_NUMBER = "PHONE_NUMBER";
    //密码
    public static final String KEY_PASSWORD = "PASSWORD";
    public static final String KEY_USERNAME = "USERNAME";

    //获取Token
    public static final String KEY_GET_TOKEN = "GET_TOKEN";

    public static final String KEY_GET_ID = "GET_ID";//用户登录ID 100001
    public static final String GET_LOGIN_ID = "GET_LOGIN_ID";//用户登录ID 100001 string 类型
    public static final String KEY_GET_JlOGIN_ID = "GET_JlOGIN_ID";//邀请人加入的id
    public static final String KEY_WX_CITY = "WX_CITY";
    public static final String KEY_MEMBER_STATE = "MEMBER_STATE";//会员状态 1是年费2是终身3不是会员
     public static final String KEY_ISEXPIRE = "ISEXPIRE";//会员是否过期   1是会员已过期 0是正常
    public static final String NEWFRIEND_NUM = "NEWFRIEND_NUM";//新的好友数字标示
    public static final String QUICK_REPLY_ID = "QUICK_REPLY_ID";//快速回复发送的id
    public static final String IS_PUBLIC = "is_public";//是否现实公告文章   显示 1    审核时改为0，
    public static final String KEY_GET_NAME = "get_name"; //用户名
    public static final String KEY_BUG_MSG="bug_msg";
    public static final String KEY_PHONE_SYSTEM="phone_system";
    public static final String KEY_VERSION_NAME = "versionName";
    public static final String KEY_TOPIC = "key_topic";
    public static final String KEY_USER_NAME = "key_user_name";
    public static final String KEY_PHONE_TIME = "phone_time_contact";
    public static final String KEY_DYNAMIC_DATA = "dynamic_data";
    public static final String CHAT_MESSAGE_NUM = "chat_message_num";
    public static final String KEY_POP_SNAP = "key_pop_snap";
    public static final String XIAJI_NUM = "xiaji_num";
    public static final String DYNAMIC_NUM = "dynamic_num";
    public static String qr_code_img="qr_code_img";

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param
     * @param key
     * @param object
     */
    public static void put(String key, Object object) {

        SharedPreferences sp = UIUtils.getContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(String key, Object defaultObject) {
        SharedPreferences sp = UIUtils.getContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param
     * @param key
     */
    public static void remove(String key) {
        SharedPreferences sp = UIUtils.getContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param
     */
    public static void clear() {
        SharedPreferences sp = UIUtils.getContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param
     * @param key
     * @return
     */
    public static boolean contains(String key) {
        SharedPreferences sp = UIUtils.getContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param
     * @return
     */
    public static Map<String, ?> getAll() {
        SharedPreferences sp = UIUtils.getContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }

}
