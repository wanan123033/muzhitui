package base.network;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hehe on 2016/4/18.
 */
public class RetrofitUtil {
    private static Retrofit mInstance;

//      public static final String API_URL = "http://192.168.1.143:8099/";
//      public static final String API_URL = "http://renrenxiu99.tunnel.qydev.com/";
//      public static final String API_URL = "http://www.muzhitui.cn/song/appLoginApi/myAccount?loginId_mzt=178442";

    public static final String API_URL_QIAN = "http://"; //公司域名前面一节
    public static final String API_URL_ZHONG = "www"; //公司域名中间

    public static final String API_URL_HOU = ".muzhitui.cn/"; //公司域名后面
    public static final String API_URL = RetrofitUtil.API_URL_QIAN+RetrofitUtil.API_URL_ZHONG+RetrofitUtil.API_URL_HOU; //公司域名全部
    public static final Random RANDOM=new java.util.Random();// 定义随机类
    public static final int RANDOMS=RANDOM.nextInt(2000);// 返回[0,2000)集合中的整数，注意不包括2000
    public static final String API_URL_RANDOM = RetrofitUtil.API_URL_QIAN+"s"+RetrofitUtil.RANDOMS+RetrofitUtil.API_URL_HOU;
    public static final String PROJECT_URL = "song/";
    public static final String PAGE_LOOK = "appPageApi/pageDetail";
    public static final String PAGE_LOOK_OWN = "appPageMeApi/pageDetailMe";
    public static final String MYQR = "appLoginApi/appShare?tjLoginId=";
   public static final String SHAREQR = "newLoginController/getCode?flag=20&tjLoginId=";

    public static Retrofit getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitUtil.class) {
                if (mInstance == null) {
                    OkHttpClient client = new OkHttpClient.Builder().connectTimeout(180, TimeUnit.SECONDS)       //设置连接超时
                            .readTimeout(180, TimeUnit.SECONDS)          //设置读超时
                            .writeTimeout(180, TimeUnit.SECONDS)          //设置写超时
                            .retryOnConnectionFailure(true)
                            .addNetworkInterceptor(new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Request request = chain.request();
                                    //// TODO: 2016/5/26
                                   // Logger.e("body = " + bodyToString(request.body()));
                                    Response response = chain.proceed(request);
                                     // Logger.i("返回的 = " + response.body().string());
                                    return response;
                                }
                            }).cookieJar(new CookiesManager())
                            .build();

                    mInstance = new Retrofit.Builder()
                            .baseUrl(API_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(client)
                            .build();
                }
            }
        }
        return mInstance;
    }
    private static String bodyToString(final RequestBody request) {
        try {
            final Buffer buffer = new Buffer();
            request.writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
