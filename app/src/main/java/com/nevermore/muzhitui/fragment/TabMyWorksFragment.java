package com.nevermore.muzhitui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nevermore.muzhitui.MyCard;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.event.ReadCountEvent;
import com.nevermore.muzhitui.module.bean.MyWorkMyInfo;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import base.BaseActivityTwoV;
import base.BaseFragment;
import base.ImageUtil;
import base.SPUtils;
import base.view.MyTabLayout;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;

public class TabMyWorksFragment extends BaseFragment {

    @BindView(R.id.ivHead)
    ImageView mIvHead;
    @BindView(R.id.tvName)
    TextView mTvName;
    @BindView(R.id.tvReadSize)
    TextView mTvReadSize;
    @BindView(R.id.tbl)
    MyTabLayout mTbl;
    @BindView(R.id.vp)
    ViewPager mVp;
    private String tabTiles[] = new String[]{"作品", "草稿", "模板"};
  //  private LoadingAlertDialog mLoadingAlertDialog;
    private MyWorksWorksFragment[] mMyWorksWorksFragment = new MyWorksWorksFragment[2];
    private boolean mIsFirst = true;

    @Override
    public int createSuccessView() {
        EventBus.getDefault().register(this);
        return R.layout.fragment_my_works;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // mLoadingAlertDialog = new LoadingAlertDialog(getActivity());
        mTbl.setTabMode(TabLayout.MODE_FIXED);
        mVp.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return mMyWorksWorksFragment[0] = MyWorksWorksFragment.newInstance(1,null);
                    case 1:
                        return mMyWorksWorksFragment[1] = MyWorksWorksFragment.newInstance(0,null);
                    default:
                        return new MyWorksModeFragment();
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
        // mVp.setOffscreenPageLimit(1);
        mTbl.setupWithViewPager(mVp);
        loadData();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (mIsFirst) {
                mIsFirst = false;
            } else {
                loadData();
                mMyWorksWorksFragment[0].refreshData();
                mMyWorksWorksFragment[1].refreshData();
            }

        }
    }

    @Subscribe
    public void refreshReadCount(ReadCountEvent readCountEvent) {
        Logger.i("=============refreshReadCount=============");
        loadData();
    }

    @OnClick(R.id.ivHead)
    public void click(View view) {
        baseStartActivity(MyCard.class);
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void loadData() {
        // mLoadingAlertDialog.show();
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().myWorkMyInfo((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))).subscribe(new Subscriber<MyWorkMyInfo>() {
            @Override
            public void onCompleted() {
                //  mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                //  mLoadingAlertDialog.dismiss();
                showTest(mNetWorkError);
            }

            @Override
            public void onNext(MyWorkMyInfo myWorkMyInfo) {
                if ("1".equals(myWorkMyInfo.getState())) {
                    ImageLoader.getInstance().displayImage(myWorkMyInfo.getHeadimg(), mIvHead, ImageUtil.getInstance().getCircleDisplayOption());
                 //   mTvName.setText(myWorkMyInfo.getWechatname());
                    mTvReadSize.setText("累计阅读：" + myWorkMyInfo.getReadCount() + "次");
                } else {
                    showTest(mServerEror);
                }

            }
        });
        ((BaseActivityTwoV) getActivity()).addSubscription(sbMyAccount);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
