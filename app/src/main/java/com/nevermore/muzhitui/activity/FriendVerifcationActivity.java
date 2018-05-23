package com.nevermore.muzhitui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.event.refrush;
import com.nevermore.muzhitui.module.bean.Code;
import com.nevermore.muzhitui.module.network.WorkService;

import org.greenrobot.eventbus.EventBus;

import base.BaseActivityTwoV;
import base.MyLogger;
import base.SPUtils;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Simone on 2017/1/9.
 */

public class FriendVerifcationActivity extends BaseActivityTwoV implements View.OnClickListener{

    @BindView(R.id.etFriendVInfo)
    EditText mEtFriendVInfo;
    @BindView(R.id.etFriendVOtherInfo)
    EditText mEtFriendVOtherInfo;
    @BindView(R.id.ivFriendVDeleteName)
    ImageView mIvFriendVDeleteName;
    @BindView(R.id.ivFriendVDeleteInfo)
    ImageView mIvFriendVDeleteInfo;

    private LoadingAlertDialog mLoadingAlertDialog;
    private int id;

    public static final String TARGIN_ID = "id";

    @Override
    public void init() {
        showBack();
        setMyTitle("好友验证");
        TextView tv =showRight();
        tv.setOnClickListener(this);
        tv.setText("发送");
        tv.setTextColor(getResources().getColor(R.color.green));
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        id = getIntent().getIntExtra(TARGIN_ID, 0);
        mEtFriendVInfo.setText("我是 "+ SPUtils.get(SPUtils.KEY_USERNAME, ""));
        mEtFriendVInfo.setSelection(mEtFriendVInfo.getText().length());


    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_friend_verification;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({ R.id.ivFriendVDeleteName, R.id.ivFriendVDeleteInfo})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tvRight:
                if (TextUtils.isEmpty(mEtFriendVInfo.getText().toString().trim())) {
                    showTest("请输入验证信息！");
                    return;
                }



                    loadDatarequest();

                break;
            case R.id.ivFriendVDeleteName:
                mEtFriendVInfo.setText("");
                break;
            case R.id.ivFriendVDeleteInfo:
                mEtFriendVOtherInfo.setText("");
                break;
        }
    }

    private void loadDatarequest() {

        mLoadingAlertDialog.show();
        MyLogger.kLog().e("tagetId="+id);
        Subscription sbGetCode = wrapObserverWithHttp(WorkService.getWorkService().requestFriend((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),id, mEtFriendVInfo.getText().toString().trim(), mEtFriendVOtherInfo.getText().toString().trim())).subscribe(new Subscriber<Code>() {
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
            public void onNext(Code code) {
                MyLogger.kLog().e(code.toString());
                if ("1".equals(code.getState())) {
                    showTest(code.getMsg());
                    showTest("请求添加好友发送成功");
                    EventBus.getDefault().post(new refrush(1));
                    finish();
                } else {
                    if("2".equals(code.getState()) && "再次发送好友请求成功".equals(code.getMsg())){
                        showTest(code.getMsg());
                        EventBus.getDefault().post(new refrush(1));
                        finish();
                        return;
                    }
                    showTest(code.getMsg());
                }
            }
        });
        addSubscription(sbGetCode);
    }



}
