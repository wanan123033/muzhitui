package com.nevermore.muzhitui;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.nevermore.muzhitui.module.bean.My;
import com.nevermore.muzhitui.module.network.WorkService;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by hehe on 2016/6/2.
 */
public class ModifyNameActivity extends BaseActivityTwoV {
    @BindView(R.id.etName)
    EditText etName;
    private LoadingAlertDialog mLoadingAlertDialog;

    @Override
    public void init() {
        setMyTitle("修改昵称");
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        showBack();
        TextView textView = showRight();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }

    private void loadData() {
        final String name = etName.getText().toString();
        if (!name.isEmpty()) {
            mLoadingAlertDialog.show();
            Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().updateWechatname((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),name)).subscribe(new Subscriber<My>() {
                @Override
                public void onCompleted() {
                    mLoadingAlertDialog.dismiss();
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    mLoadingAlertDialog.dismiss();
                    showTest(mNetWorkError);
                }

                @Override
                public void onNext(My my) {
                    if ("1".equals(my.getState())) {
                        showTest("修改成功");
                        Intent intent = new Intent();
                        intent.putExtra(SPUtils.KEY_WXNICKNAME, name);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    } else {
                        showTest(mServerEror);
                    }

                }
            });
            addSubscription(sbMyAccount);
        } else {
            showTest("请填写昵称");
        }
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_updatename;
    }
}
