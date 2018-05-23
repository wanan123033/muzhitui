package com.nevermore.muzhitui.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.nevermore.muzhitui.event.PaySuccessEvent;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import base.SPUtils;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    public static int flag1 = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, SPUtils.KEY_WXID);

        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Logger.i("recode = " + resp.errCode + "   " + BaseResp.ErrCode.ERR_OK);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    Logger.i("okokok");
                    EventBus.getDefault().post(new PaySuccessEvent());
                    if (flag1 == 1) {

                    } else {

                    }

                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    //UIUtils.showToastSafe("取消了支付");
                    break;
                default:
                    //UIUtils.showToastSafe("支付失败");
                    break;
            }
        }
        finish();

    }

}