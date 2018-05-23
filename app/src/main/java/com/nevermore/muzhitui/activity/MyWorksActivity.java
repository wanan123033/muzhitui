package com.nevermore.muzhitui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nevermore.muzhitui.MyCard;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.event.ReadCountEvent;
import com.nevermore.muzhitui.fragment.MaterialFragment;
import com.nevermore.muzhitui.fragment.MyOrgielFragment;
import com.nevermore.muzhitui.fragment.MyWorksModeFragment;
import com.nevermore.muzhitui.fragment.MyWorksWorksFragment;
import com.nevermore.muzhitui.module.bean.MyWorkMyInfo;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import base.BaseActivityTwoV;
import base.ImageUtil;
import base.SPUtils;
import base.view.MyTabLayout;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Simone on 2017/1/18.
 * 我的作品Activity
 */

public class MyWorksActivity extends BaseActivityTwoV {
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
    private String tabTiles[] = new String[]{"秒变作品","原创作品", "草稿", "模板"};
    private MyWorksWorksFragment[] mMyWorksWorksFragment = new MyWorksWorksFragment[2];

    @Override
    public void init() {

        setMyTitle("我的作品");
        showBack();
        mTbl.setTabMode(TabLayout.MODE_FIXED);
        mVp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Log.e("position 我的作品",position+"");
                switch (position) {
                    case 0:
                        Log.e("position 000",position+"");
                        return mMyWorksWorksFragment[0] = MyWorksWorksFragment.newInstance(1,null);

                    case 1:
                        Log.e("position 111",position+"");
                        return  MyOrgielFragment.newInstance(2,null);
                    case 2:
                        Log.e("position 222",position+"");
                        return mMyWorksWorksFragment[1] = MyWorksWorksFragment.newInstance(0,null);
                    default:  Log.e("position default",position+"");
                        return new MyWorksModeFragment();

                   /* case 0:
                        Log.e("position 000",position+"");
                        return mMyWorksWorksFragment[0] = MyWorksWorksFragment.newInstance(1);
                    case 1:
                        Log.e("position 111",position+"");
                        return mMyWorksWorksFragment[1] = MyWorksWorksFragment.newInstance(0);
                    default:  Log.e("position 222",position+"");
                        return new MyWorksModeFragment();*/
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
    public int createSuccessView() {
        EventBus.getDefault().register(this);
        return R.layout.fragment_my_works;
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
       addSubscription(sbMyAccount);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
