package com.nevermore.muzhitui.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.FindUserInfoById;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

import base.BaseActivityTwoV;
import base.ImageUtil;
import base.SPUtils;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Administrator on 2017/1/16.
 */

public class UserInfoQRCode extends BaseActivityTwoV {

    @BindView(R.id.ivUIQRCodeHead)
    ImageView mIvUIQRCodeHead;
    @BindView(R.id.tvUIQRCodeNameInfo)
    TextView mTvUIQRCodeNameInfo;

    @BindView(R.id.ivUIQRCode)
    ImageView mIvUIQRCode;
    private LoadingAlertDialog mLoadingAlertDialog;
    @Override
    public void init() {
        setMyTitle("我的二维码");
        showBack();
        mLoadingAlertDialog = new LoadingAlertDialog(this);
       int id= getIntent().getIntExtra("id",0);
        int ourid = (int) SPUtils.get(SPUtils.KEY_GET_ID, 0);
        if (id!=0){
            loadData(String.valueOf(id));
        }else{
            loadData(String.valueOf(ourid));
        }

    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_userinfo_qr_code;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void loadData(String id) {

        mLoadingAlertDialog.show();



        Subscription sbGetCode = wrapObserverWithHttp(WorkService.getWorkService().getUserById((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),id )).subscribe(new Subscriber<FindUserInfoById>() {
            @Override
            public void onCompleted() {
                mLoadingAlertDialog.dismiss();
                removeLoadingView();

            }

            @Override
            public void onError(Throwable e) {

                mLoadingAlertDialog.dismiss();
                removeLoadingView();
                showErrorView();
                e.printStackTrace();
            }

            @Override
            public void onNext(final FindUserInfoById userInfo) {


                if ("1".equals(userInfo.getState())) {

                    setCodeText(userInfo.getLogin());


                } else {
                    showTest(userInfo.getMsg());
                }

            }
        });
        addSubscription(sbGetCode);
    }
    private void setCodeText(FindUserInfoById.LoginBean userInfo) {


        ImageLoader.getInstance().displayImage( userInfo.getHeadimg(), mIvUIQRCodeHead, ImageUtil.getInstance().getCircleDisplayOption());
        if (userInfo.getUser_name().length()>8){
            mTvUIQRCodeNameInfo.setText(userInfo.getUser_name() + "\n(ID:" + userInfo.getId() + ")");
        }else{
            mTvUIQRCodeNameInfo.setText(userInfo.getUser_name() + "  (ID:" + userInfo.getId() + ")");
        }


        StringBuffer s = new StringBuffer();
        s.append("add_friend:");


        s.append(userInfo.getId());

        Log.e("UserInfoQRCode s.append", s.toString());
        //add_friend:100001

        //Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.login_qq);
        Bitmap bitmap = EncodingUtils.createQRCode(s.toString(), 350, 350,null);
        mIvUIQRCode.setImageBitmap(bitmap);
    }

}
