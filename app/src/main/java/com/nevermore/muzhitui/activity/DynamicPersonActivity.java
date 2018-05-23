package com.nevermore.muzhitui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.nevermore.muzhitui.EventBusContanct;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.event.refrush;
import com.nevermore.muzhitui.fragment.DynamicPage;
import com.nevermore.muzhitui.fragment.MyOrgielFragment;
import com.nevermore.muzhitui.fragment.MyWorksWorksFragment;
import com.nevermore.muzhitui.module.BaseBean;
import com.nevermore.muzhitui.module.bean.MyInfo;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.BaseFragment;
import base.MyLogger;
import base.SPUtils;
import base.view.LoadingAlertDialog;
import base.view.MyTabLayout;
import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imkit.RongIM;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/9/12.
 * 动态的个人信息界面
 */

public class DynamicPersonActivity extends BaseActivityTwoV{
    @BindView(R.id.vp)
    ViewPager mVp;
    @BindView(R.id.tbl)
    MyTabLayout mTbl;
    @BindView(R.id.iv_g)
    ImageView iv_g;
    @BindView(R.id.tv_attention)
    TextView tv_attention;
    @BindView(R.id.tv_attentioned)
    TextView tv_attentioned;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.ivHead)
    CircleImageView ivHead;
    @BindView(R.id.tv_attention_num)
    TextView tv_attention_num;
    @BindView(R.id.tv_f_num)
    TextView tv_f_num;
    @BindView(R.id.tv_friend_num)
    TextView tv_friend_num;
    @BindView(R.id.tv_jwhy)
    TextView tv_jwhy;
    @BindView(R.id.ll_menu)
    LinearLayout ll_menu;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.sh_shild)
    Switch sh_shild;

    public static final String USERID = "userId";
    private String tabTiles[] = new String[]{"动态","秒变", "原创"};
    MyViewpagerAdapter viewpagerAdapter;
    private List<BaseFragment> frags = new ArrayList<>();

    private String userId;

    private LoadingAlertDialog mLoadingAlertDialog;
    private MyInfo info;


    @Override
    public void init() {
        hideActionBar();
        showBack();
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        mTbl.setTabMode(TabLayout.MODE_FIXED);
        userId = getIntent().getStringExtra(USERID);
        DynamicPage page = new DynamicPage();
        Bundle bundle = new Bundle();
        bundle.putInt(DynamicPage.DYNAMIC_PAGE_STATUS,2);
        bundle.putString(DynamicPage.DYNAMIC_USER_ID,userId);
        page.setArguments(bundle);
        frags.add(page);
        frags.add(MyWorksWorksFragment.newInstance(1,userId));
        frags.add(MyOrgielFragment.newInstance(2,userId));

        viewpagerAdapter = new MyViewpagerAdapter(getSupportFragmentManager());
        mVp.setAdapter(viewpagerAdapter);
        mTbl.setupWithViewPager(mVp);
        loadData();

        if(SPUtils.get(SPUtils.GET_LOGIN_ID,"").equals(userId)){
            ll_menu.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }else {
            ll_menu.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
        }

        sh_shild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyLogger.kLog().i("------"+sh_shild.isChecked());
                if(sh_shild.isChecked()){
                    shild(1);
                }else {
                    shild(0);
                }
            }
        });
    }

    @Override
    public int createSuccessView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_dynamic_person;
    }

    @OnClick({R.id.ll_menu_g,R.id.ll_menu_f,R.id.ll_g,R.id.tv_jwhy,R.id.ivHead,R.id.ll_menu_h})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_menu_g:
            case R.id.ll_menu_f:
                int menuStatus = 0;
                if(view.getId() == R.id.ll_menu_f)
                    menuStatus = 1;

                Intent intent = new Intent(this,GFActivity.class);
                intent.putExtra(GFActivity.MENU_STATE,menuStatus);
                intent.putExtra(GFActivity.USER_ID,userId);
                startActivity(intent);
                break;
            case R.id.ll_g:   //关注此人
                mLoadingAlertDialog.show();
                addSubscription(wrapObserverWithHttp(WorkService.getWorkService().fans((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),userId+"",info.is_fan == 1 ? 0 : 1)).subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {
                        removeLoadingView();
                        mLoadingAlertDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        removeLoadingView();
                        mLoadingAlertDialog.dismiss();
                    }

                    @Override
                    public void onNext(BaseBean o) {
                        if("1".equals(o.state)){
                            EventBus.getDefault().post(DynamicPostedActivity.POSTED_STATE);
                            showTest(o.msg);
                            if(info.is_fan == 0){
                                info.is_fan = 1;
                            }else {
                                info.is_fan = 0;
                            }
                            if(info.is_fan == 1){
                                tv_attention.setVisibility(View.GONE);
                                iv_g.setVisibility(View.GONE);
                                tv_attentioned.setVisibility(View.VISIBLE);
                                EventBus.getDefault().post(EventBusContanct.getInstance(EventBusContanct.TABMYFRAGMENT_REFRSH_FAN_JIA1));
                            }else{
                                tv_attention.setVisibility(View.VISIBLE);
                                iv_g.setVisibility(View.VISIBLE);
                                tv_attentioned.setVisibility(View.GONE);
                                EventBus.getDefault().post(EventBusContanct.getInstance(EventBusContanct.TABMYFRAGMENT_REFRSH_FAN_JIAN1));
                            }
                            loadData();
                        }else{
                            showTest(o.msg);
                        }
                    }
                }));
                break;
            case R.id.tv_jwhy: //加为好友
                if(tv_jwhy.getText().toString().equals("发送消息") || tv_jwhy.getText().toString().equals("验证通过")) {
                    if (RongIM.getInstance() != null)
                        RongIM.getInstance().startPrivateChat(this, userId + "", info.user_name + "");
                }else if(tv_jwhy.getText().toString().equals("加为好友")){

                    Intent intent2 = new Intent(this, FriendVerifcationActivity.class);
                    MyLogger.kLog().e("userId=" +userId);
                    intent2.putExtra("id", Integer.parseInt(userId));
                    startActivity(intent2);
                }else if(tv_jwhy.getText().toString().equals("等待验证")){
                    showTest("已发送好友请求，请等待添加...");
                }
                break;
            case R.id.ivHead:
                if(info.loginId == Integer.parseInt((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))){
                    baseStartActivity(PersonalInfoActivity.class);
                    break;
                }
                Intent intent2 = new Intent(this, SeePersonalInfoActivity.class);
                MyLogger.kLog().e("userId=" +userId);
                intent2.putExtra("id", userId + "");
                intent2.putExtra("friend_state",11+"");
                startActivity(intent2);
                break;
            case R.id.ll_menu_h:
                showTest("好友暂不开放");
                break;

        }

    }

    private void loadData() {
        mLoadingAlertDialog.show();
        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().getMyInfo((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),userId)).subscribe(new Subscriber<MyInfo>() {
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
            public void onNext(MyInfo o) {
                initInfo(o);
            }
        }));
    }

    private void shild(final int type){
        mLoadingAlertDialog.show();
        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().shield((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),type,userId)).subscribe(new Subscriber<BaseBean>() {
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
                if("1".equals(baseBean.state)){
                    if(type == 1){
                        showTest("已屏蔽该人动态提醒");
                    }else {
                        showTest("已开启该人动态提醒");
                    }
                }else if("2".equals(baseBean.state)){
                    showTest("已屏蔽该人动态提醒");
                }else if("3".equals(baseBean.state)){
                    showTest("已开启该人动态提醒");
                }else {
                    if(type == 1){
                        showTest("屏蔽该人动态提醒失败");
                    }else {
                        showTest("开启该人动态提醒失败");
                    }
                }
            }
        }));
    }
    private void initInfo(MyInfo info) {
        MyLogger.kLog().e("info:is_friend="+info.friend_status+",is_fan_together="+info.is_fan_together);
        this.info = info;
        ImageLoader.getInstance().displayImage(info.headimg,ivHead);
        tvName.setText(info.user_name);
        tv_attention_num.setText(info.count_attention+"");
        tv_f_num.setText(info.count_fans+"");
        tv_friend_num.setText(info.count_friend+"");
        sh_shild.setVisibility(View.GONE);
        if(info.friend_status == 1 || info.is_fan_together == 1){
            tv_jwhy.setText("发送消息");
            if(!String.valueOf(info.loginId).equals((String)SPUtils.get(SPUtils.GET_LOGIN_ID,"")) && info.is_fan_together == 1) {
                sh_shild.setVisibility(View.VISIBLE);
            }
        }else if(info.friend_status == 0 || info.friend_status == 5){
            tv_jwhy.setText("加为好友");
        } else if(info.friend_status == 3){
            tv_jwhy.setText("通过验证");
        }else{
            tv_jwhy.setText("等待验证");
        }
        if(info.is_fan == 1){
            tv_attention.setVisibility(View.GONE);
            iv_g.setVisibility(View.GONE);
            tv_attentioned.setVisibility(View.VISIBLE);
            if(!String.valueOf(info.loginId).equals((String)SPUtils.get(SPUtils.GET_LOGIN_ID,""))) {
                sh_shild.setVisibility(View.VISIBLE);
            }
        }else {
            tv_attention.setVisibility(View.VISIBLE);
            iv_g.setVisibility(View.VISIBLE);
            tv_attentioned.setVisibility(View.GONE);
        }

        if(info.is_shield == 1){
            sh_shild.setChecked(true);
        }else {
            sh_shild.setChecked(false);
        }
        if (viewpagerAdapter != null){
            viewpagerAdapter.setInfo(info);
            viewpagerAdapter.notifyDataSetChanged();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventEMMessage(refrush refrush) {
        if(refrush.getState() == 1){
            tv_jwhy.setText("等待验证");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void showBack() {
        iv_back.setColorFilter(Color.parseColor("#ffffff"));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                finish();
                onBackPressed();
            }
        });
    }

    public class MyViewpagerAdapter extends FragmentPagerAdapter{
        private MyInfo info;

        public MyViewpagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return tabTiles.length;
        }

        @Override
        public Fragment getItem(int position) {
            return frags.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = tabTiles[position];
            if(info != null){
                if(position == 0){
                    title = title +  "(" + info.count_dt +")";
                }else if (position == 1){
                    title = title +  "(" + info.count_mb +")";
                }else if(position == 2){
                    title = title + "("+info.count_yc +")";
                }
            }
            return title;
        }

        public void setInfo(MyInfo info){
            this.info = info;
        }
    }
}