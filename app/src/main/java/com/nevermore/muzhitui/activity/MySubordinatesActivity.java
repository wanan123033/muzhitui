package com.nevermore.muzhitui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.RelativeLayout;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.fragment.MySubordinatesFragment;
import com.nevermore.muzhitui.module.bean.MyLevel;
import com.nevermore.muzhitui.module.network.WorkService;
import com.orhanobut.logger.Logger;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.view.LoadingAlertDialog;
import base.view.MyViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;

public class MySubordinatesActivity extends BaseActivityTwoV {

    @BindView(R.id.materialBack)
    RelativeLayout mMaterialBack;
    @BindView(R.id.materialsearch)
    RelativeLayout mMaterialsearch;
//    private String tabTiles[] = new String[]{"全部/会员\n(136/17)", "组一/会员\n(136/17)", "组二/会员\n" +
//            "(136/17)", "组三/会员\n" +
//            "(136/17)"};
    private LoadingAlertDialog mLoadingAlertDialog;
    MySubordinatesFragment mySubordinatesFragments = null;
    private int mSelectedPosition;

    @Override
    public void init() {
        hideActionBar();
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        loadData();
    }

    private void loadData() {
        mLoadingAlertDialog.show();
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().myLevel((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))).subscribe(new Subscriber<MyLevel>() {
            @Override
            public void onCompleted() {
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                showTest(mNetWorkError);
                mLoadingAlertDialog.dismiss();
                showErrorView();
                e.printStackTrace();
            }

            @Override
            public void onNext(MyLevel myLevel) {
                if (myLevel.getState() == 1) {
                    mySubordinatesFragments = MySubordinatesFragment.newInstance(1);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,mySubordinatesFragments).commitAllowingStateLoss();
                } else {
                    showTest(mServerEror);
                    showErrorView();
                }
            }
        });
        addSubscription(sbMyAccount);
    }

    @Override
    public void onBackPressed() {
        Logger.i("myKehuFragments[mSelectedPosition] = " + mSelectedPosition);
        if (mySubordinatesFragments != null) {
            if (!mySubordinatesFragments.backUpLevel()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_mysubordinate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.materialBack, R.id.materialsearch})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.materialBack:
                finish();
                break;
            case R.id.materialsearch:
                baseStartActivity(SearchActivity.class);
                break;
        }
    }
}
