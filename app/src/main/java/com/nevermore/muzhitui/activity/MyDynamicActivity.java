package com.nevermore.muzhitui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nevermore.muzhitui.EventBusContanct;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.event.DataRefreshEvent;
import com.nevermore.muzhitui.fragment.DynamicPage;
import com.nevermore.muzhitui.module.bean.Count;
import com.nevermore.muzhitui.module.network.WorkService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.view.DragPointView;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/9/13.
 */

public class MyDynamicActivity extends BaseActivityTwoV{

    private LoadingAlertDialog mLoadingAlertDialog;

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.seal_num_message)
    TextView seal_num_message;
    @Override
    public void init() {
        hideActionBar();
        showBack();
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        Fragment dynamicPage = new DynamicPage();
        Bundle bundle = new Bundle();
        bundle.putInt(DynamicPage.DYNAMIC_PAGE_STATUS,1);
        bundle.putString(DynamicPage.DYNAMIC_USER_ID, (String) SPUtils.get(SPUtils.GET_LOGIN_ID,""));
        dynamicPage.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,dynamicPage).commit();

        loadData();

    }

    private void loadData() {
        mLoadingAlertDialog.show();
        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().getCountOneDtMesg((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))).subscribe(new Subscriber<Count>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                removeLoadingView();
                removeErrorView();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onNext(Count o) {
                if("1".equals(o.state)){
                    if(o.count == 0){
                        seal_num_message.setVisibility(View.GONE);
                        return;
                    }
                    seal_num_message.setVisibility(View.VISIBLE);
                    seal_num_message.setText(o.count+"");
                }else {
                    showTest(o.msg);
                }
            }
        }));
    }
    @Override
    public void showBack() {
        iv_back.setImageResource(R.mipmap.backicon);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                finish();
                onBackPressed();
            }
        });
    }

    @Override
    public int createSuccessView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_my_dynamic;
    }

    @OnClick(R.id.rel_dynamic_msg)
    public void onClick(View view){
        baseStartActivity(DynamicMsgActivity.class);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventEMMessage(EventBusContanct contanct) {
        if(contanct.getFlag() == EventBusContanct.MYDYNAMICACTIVITY_DYNAMIC_MSG_STATE){
            seal_num_message.setVisibility(View.GONE);
        }
    }
}
