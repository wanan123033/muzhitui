package com.nevermore.muzhitui;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nevermore.muzhitui.activity.MztProxyProtocolActivity;
import com.nevermore.muzhitui.event.BatchSellEvent;
import com.nevermore.muzhitui.fragment.MyProxyFragment;
import com.nevermore.muzhitui.fragment.MyRepertoryFragment;
import com.nevermore.muzhitui.module.bean.MyStock;
import com.nevermore.muzhitui.module.network.WorkService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.view.LoadingAlertDialog;
import base.view.MyTabLayout;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;

public class MyProxy extends BaseActivityTwoV {
    @BindView(R.id.tbl)
    MyTabLayout mTbl;
    @BindView(R.id.vp)
    ViewPager mVp;
    @BindView(R.id.llytProxy)
    LinearLayout mLlytProxy;
    @BindView(R.id.tvApply)
    TextView mTvApply;
    @BindView(R.id.tvProxyProtocol)
    TextView mTvProxyProtocol;
    @BindView(R.id.flytProxy)
    FrameLayout mFlytProxy;
    @BindView(R.id.tvMyProxyPhone)
    TextView mTvMyProxyPhone;
    private boolean mIsProxy = false;
    private String tabTiles[] = new String[]{"我的库存", "我的下级代理"};
    private LoadingAlertDialog mLoadingAlertDialog;
    private boolean mIsFirst = true;
    MyRepertoryFragment mMyRepertoryFragment;

    @Override
    public void init() {
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        setMyTitle("我的代理");
        showBack();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    private void loadData() {
        mLoadingAlertDialog.show();
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().myCards((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))).subscribe(new Subscriber<MyStock>() {
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
            public void onNext(MyStock my) {
                if ("1".equals(my.getState())) {
                    if("非代理".equals(my.getTypeName())){
                        if (mMyRepertoryFragment != null) {
                            mMyRepertoryFragment.setmMyStock(my);
                        }
                        return;
                    }
                    mFlytProxy.setVisibility(View.GONE);
                    if (mIsFirst) {
                        mIsFirst = false;
                        initProxy(my);
                    } else {
                        if (mMyRepertoryFragment != null) {
                            mMyRepertoryFragment.setmMyStock(my);
                        }
                    }
                }
            }
        });
        addSubscription(sbMyAccount);

    }


    @Subscribe
    public void onEventModeRefresh(BatchSellEvent batchSellEvent) {
        loadData();
    }


    public void initProxy(final MyStock myStock) {
        mTbl.setTabMode(TabLayout.MODE_FIXED);
        mVp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        mMyRepertoryFragment = new MyRepertoryFragment();
                        mMyRepertoryFragment.setmMyStock(myStock);
                        return mMyRepertoryFragment;
                    default:
                        return new MyProxyFragment();
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
    }

    @Override
    public int createSuccessView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_my_proxy;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @OnClick({R.id.tvApply, R.id.tvMyProxyPhone,R.id.tvProxyProtocol})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvApply:
                baseStartActivity(BuyProxyActivity.class);
                finish();
                break;
            case R.id.tvMyProxyPhone:
                Intent in2 = new Intent();
                in2.setAction(Intent.ACTION_CALL);
                in2.setData(Uri.parse("tel:"+mTvMyProxyPhone.getText().toString()));
                startActivity(in2);
                break;
            case R.id.tvProxyProtocol:
                baseStartActivity(MztProxyProtocolActivity.class);
                break;
        }
    }
}
