package com.nevermore.muzhitui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.FindUserInfoById;
import com.nevermore.muzhitui.module.network.WorkService;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Simone on 2017/1/10.
 */

public class InputAddFriendActivity extends BaseActivityTwoV implements View.OnClickListener{

    @BindView(R.id.tvInputAddfriendsId)
    EditText mTvInputAddfriendsId;
    @BindView(R.id.ivInputAddfriendsDelete)
    ImageView mIvInputAddfriendsDelete;

    private LoadingAlertDialog mLoadingAlertDialog;

    @Override
    public void init() {
        showBack();
        setMyTitle("好友查询");
        TextView tv =showRight();
        tv.setOnClickListener(this);
        tv.setText("搜索");
        mLoadingAlertDialog = new LoadingAlertDialog(this);
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_input_addfriends;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ivInputAddfriendsDelete})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tvRight:
                String id = mTvInputAddfriendsId.getText().toString().trim();
                if (TextUtils.isEmpty(id)) {
                    showTest("输入手机号/拇指ID");
                    return;
                }
                loadData(id);

                break;
            case R.id.ivInputAddfriendsDelete:
                mTvInputAddfriendsId.setText("");
                break;
        }
    }

    private void loadData(String id) {

        mLoadingAlertDialog.show();

        Subscription sbGetCode = wrapObserverWithHttp(WorkService.getWorkService().findFriend((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),id)).subscribe(new Subscriber<FindUserInfoById>() {
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
            public void onNext(FindUserInfoById userInfo) {

                if ("1".equals(userInfo.getState())) {

                    Intent intent = new Intent(InputAddFriendActivity.this, SeePersonalInfoActivity.class);
                    intent.putExtra("id",  userInfo.getLogin().getId()+"");
                    intent.putExtra("friend_state",  11+"");
                    startActivity(intent);

                } else {
                    showTest(userInfo.getMsg());

                }

            }
        });
        addSubscription(sbGetCode);
    }


}
