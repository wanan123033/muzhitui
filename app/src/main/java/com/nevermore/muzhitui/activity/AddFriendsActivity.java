package com.nevermore.muzhitui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.FindUserInfoById;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

import org.xutils.view.annotation.ViewInject;

import base.BaseActivityTwoV;
import base.ImageUtil;
import base.SPUtils;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Simone on 2016/12/21.
 */

public class AddFriendsActivity extends BaseActivityTwoV implements View.OnClickListener {


    @BindView(R.id.tvAddfriendsId)
    TextView mTvAddfriendsId;
    @BindView(R.id.ivAddfriendsHead)
    ImageView mIvAddfriendsHead;
    @BindView(R.id.tvAddfriendsNameInfo)
    TextView mTvAddfriendsNameInfo;
    @BindView(R.id.tvAddFriendInfo)
    TextView mTvAddFriendInfo;
    @BindView(R.id.ivAddfriendsQRcode)
    ImageView mIvAddfriendsQRcode;
    @BindView(R.id.materialBack)
    RelativeLayout mMaterialBack;
    @BindView(R.id.tvScAddF)
    TextView mTvScAddF;
    private LoadingAlertDialog mLoadingAlertDialog;

    @Override
    public void init() {
        hideActionBar();
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        loadData();
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_addfriends;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);


    }


    @OnClick({R.id.materialBack, R.id.tvScAddF,R.id.tvAddfriendsId})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvAddfriendsId:
                baseStartActivity(InputAddFriendActivity.class);
                break;
            case R.id.materialBack:
                finish();
                break;
            case R.id.tvScAddF:
                startActivityForResult(new Intent(AddFriendsActivity.this, CaptureActivity.class), 0);
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            String result = bundle.getString("result");


            if (result.startsWith("add_friend")) {
                //add_friend:100001
                String[] results = result.split(":");
                Intent intent = new Intent(AddFriendsActivity.this, SeePersonalInfoActivity.class);
                intent.putExtra("id", results[1]);
                intent.putExtra("friend_state", 11 + "");


                startActivity(intent);


            } else {
                showTest("请扫描正确的好友信息！");
            }


        }
    }

    private void loadData() {
        mLoadingAlertDialog.show();
        int id = (int) SPUtils.get(SPUtils.KEY_GET_ID, 0);
        Subscription sbGetCode = wrapObserverWithHttp(WorkService.getWorkService().getUserById((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),id + "")).subscribe(new Subscriber<FindUserInfoById>() {
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
        ImageLoader.getInstance().displayImage(userInfo.getHeadimg(), mIvAddfriendsHead, ImageUtil.getInstance().getCircleDisplayOption());
        mTvAddfriendsNameInfo.setText(userInfo.getUser_name() + " (ID:" + userInfo.getId() + ")");

        StringBuffer s = new StringBuffer();
        s.append("add_friend:");


        s.append(userInfo.getId());

        Log.e("AddFriends s.append", s.toString());
        //add_friend:100001

        //Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.login_qq);
        Bitmap bitmap = EncodingUtils.createQRCode(s.toString(), 350, 350, null);
        mIvAddfriendsQRcode.setImageBitmap(bitmap);
    }
}
