package com.nevermore.muzhitui;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.IconHintView;
import com.nevermore.muzhitui.fragment.MaterialFragment;
import com.nevermore.muzhitui.module.bean.TopMaterial;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.ImageUtil;
import base.SPUtils;
import base.UIUtils;
import base.network.RetrofitUtil;
import base.view.MyTabLayout;
import butterknife.BindView;
import rx.Subscriber;
import rx.Subscription;

public class MaterialActivity extends BaseActivityTwoV {

    @BindView(R.id.rpv)
    com.jude.rollviewpager.RollPagerView mRpv;
    @BindView(R.id.tbl)
    MyTabLayout mTbl;
    @BindView(R.id.vp)
    ViewPager mVp;
    private String tabTiles[] = new String[]{"最新", "最热"};
    //   private LoadingAlertDialog mLoadingAlertDialog;
    private List<TopMaterial.ScreensBean> mList = new ArrayList<>();
    private TestLoopAdapter mLoopAdapter;

    @Override
    public void init() {
        showBack();
        setMyTitle("素材库");
        //    mLoadingAlertDialog = new LoadingAlertDialog(this);
        mLoopAdapter = new TestLoopAdapter(mRpv);

        mRpv.setAdapter(mLoopAdapter);
        mRpv.setHintView(new IconHintView(this, R.drawable.shape_point_focuse, R.drawable.shape_point_normal, UIUtils.dip2px(16)));

        mTbl.setTabMode(TabLayout.MODE_FIXED);
        mVp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    position = 1;
                } else {
                    position = 0;
                }
                return MaterialFragment.newInstance(position,0);
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
        loadData();
    }


    private void loadData() {
        //   mLoadingAlertDialog.show();
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().toPages((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))).subscribe(new Subscriber<TopMaterial>() {
            @Override
            public void onCompleted() {
                // mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                //  mLoadingAlertDialog.dismiss();
                showTest(mNetWorkError);
            }

            @Override
            public void onNext(TopMaterial topMaterial) {
                if ("1".equals(topMaterial.getState())) {
                    mList.addAll(topMaterial.getScreens());
                    mLoopAdapter.notifyDataSetChanged();
                } else {
                    showTest(mServerEror);
                }

            }
        });
        addSubscription(sbMyAccount);
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_material;
    }


    private class TestLoopAdapter extends LoopPagerAdapter {


        public TestLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, final int position) {
            ImageView view = new ImageView(container.getContext());
            //   view.setImageResource(imgs[position]);
            TopMaterial.ScreensBean screensBean = mList.get(position);
            ImageLoader.getInstance().displayImage(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + screensBean.getImg_url(), view, ImageUtil.getInstance().getBaseDisplayOption());
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Logger.i("onclicklistener");
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MaterialActivity.this, PageLookActivity.class);
                    intent.putExtra(PageLookActivity.KEY_URL, mList.get(position).getJump_url());
                    startActivity(intent);
                }
            });
            return view;
        }

        @Override
        public int getRealCount() {
            return mList.size();
        }
    }

}
