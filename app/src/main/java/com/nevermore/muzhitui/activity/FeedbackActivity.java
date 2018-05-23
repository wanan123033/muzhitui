package com.nevermore.muzhitui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.BaseBean;
import com.nevermore.muzhitui.module.network.WorkService;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import rx.Subscriber;

/**
 * Created by Administrator on 2018/1/11.
 */

public class FeedbackActivity extends BaseActivityTwoV{
    public static final String TITLE = "title";
    public static final String CONTACT = "contact";
    public static final String CONTENT = "content";

    @BindView(R.id.et_content)
    EditText et_content;

    private LoadingAlertDialog mLoadingAlertDialog;

    @Override
    public void init() {
        showBack();

        String title = getIntent().getStringExtra(TITLE);
        String content = getIntent().getStringExtra(CONTENT);
        if(TextUtils.isEmpty(title)){
            setMyTitle("功能需求");
            showRight("提交", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLoadingAlertDialog.show();
                    addSubscription(wrapObserverWithHttp(WorkService.getWorkService().collectFeedback((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),et_content.getText().toString())).subscribe(new Subscriber<BaseBean>() {
                        @Override
                        public void onCompleted() {
                            removeLoadingView();
                            mLoadingAlertDialog.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            removeErrorView();
                            removeLoadingView();
                            mLoadingAlertDialog.dismiss();
                        }

                        @Override
                        public void onNext(BaseBean baseBean) {
                            if ("1".equals(baseBean.state)){
                                showTest("反馈成功！");
                            }else {
                                showTest(baseBean.msg);
                            }
                        }
                    }));
                }
            });
        }else {
            setMyTitle(title);
            if (title.equals("个人介绍")) {
                et_content.setHint("此处请输入您的个人介绍...(限500字以内)");
                if (!TextUtils.isEmpty(content)){
                    et_content.setText(content);
                }
            }
            showRight("完成", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra(CONTACT,et_content.getText().toString());
                    setResult(RESULT_OK,intent);
                    finish();
                }
            });
        }

        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 500){
                    et_content.setText(s.subSequence(0,500));
                    et_content.setSelection(500);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_feedback;
    }
}
