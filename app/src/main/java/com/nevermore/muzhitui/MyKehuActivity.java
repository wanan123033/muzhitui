package com.nevermore.muzhitui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.nevermore.muzhitui.fragment.MyKehuFragment;
import com.nevermore.muzhitui.module.bean.MyLevel;
import com.nevermore.muzhitui.module.network.WorkService;
import com.orhanobut.logger.Logger;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.view.LoadingAlertDialog;
import base.view.MyTabLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.Subscription;

public class MyKehuActivity extends BaseActivityTwoV {
    @BindView(R.id.tbl)
    MyTabLayout mTbl;
    @BindView(R.id.vp)
    ViewPager mVp;
    private String tabTiles[] = new String[]{"一级用户/会员\n(136/17)", "二级用户/会员\n" +
            "(136/17)", "三级用户/会员\n" +
            "(136/17)"};
    private LoadingAlertDialog mLoadingAlertDialog;
    MyKehuFragment[] myKehuFragments = new MyKehuFragment[3];
    private int mSelectedPosition;

    @Override
    public void init() {
        showBack();
        setMyTitle("我的客户");
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        mTbl.setTabMode(TabLayout.MODE_FIXED);

        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mSelectedPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
                    tabTiles[0] = String.format("%s级用户/会员\n(%d/%d)", "一", myLevel.getOneCount(), myLevel.getOneMemCount());
                    tabTiles[1] = String.format("%s级用户/会员\n(%d/%d)", "二", myLevel.getTwoCount(), myLevel.getTwoMemCount());
                    tabTiles[2] = String.format("%s级用户/会员\n(%d/%d)", "三", myLevel.getThirdCount(), myLevel.getThirdMemCount());
                    mVp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                        @Override
                        public Fragment getItem(int position) {
                            switch (position) {
                                case 0:
                                    myKehuFragments[0] = MyKehuFragment.newInstance(0);
                                    return myKehuFragments[0];
                                case 1:
                                    myKehuFragments[1] = MyKehuFragment.newInstance(1);
                                    return myKehuFragments[1];
                                default:
                                    myKehuFragments[2] = MyKehuFragment.newInstance(2);
                                    return myKehuFragments[2];
                            }
                        }

                        @Override
                        public int getCount() {
                            return tabTiles.length;
                        }

                        @Override
                        public CharSequence getPageTitle(int position) {
                            return tabTiles[position];
                        }
                    });
                    mTbl.setupWithViewPager(mVp);
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
        if (myKehuFragments[mSelectedPosition] != null) {
            if (!myKehuFragments[mSelectedPosition].backUpLevel()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_my_kehu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
