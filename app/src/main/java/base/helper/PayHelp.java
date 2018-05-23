package base.helper;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import base.SPUtils;

/**
 * Created by hehe on 2016/6/7.
 */
public class PayHelp {
    private static PayHelp mInstance;

    private PayHelp() {
    }

    public static PayHelp getInstance() {
        if (mInstance == null) {
            synchronized (PayHelp.class) {
                if (mInstance == null) {
                    mInstance = new PayHelp();
                }
            }
        }
        return mInstance;
    }


    public void wxPay(Context context, PayReq payReq) {
        Logger.i("payReq =" + payReq.appId+"   " + payReq.prepayId);
        Logger.i("wxPay");
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, SPUtils.KEY_WXID);
        msgApi.sendReq(payReq);
    }
}
