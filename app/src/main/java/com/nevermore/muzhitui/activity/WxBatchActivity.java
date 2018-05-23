package com.nevermore.muzhitui.activity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.adapter.BatchFanAdapter;
import com.nevermore.muzhitui.module.bean.FriendBean;
import com.nevermore.muzhitui.module.network.WorkService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.thread.BaseRunnable;
import base.thread.ThreadManager;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscriber;

/**
 * Created by Administrator on 2018/1/2.
 */

public class WxBatchActivity extends BaseActivityTwoV{
    @BindView(R.id.vp_list)
    ViewPager vp_list;
    @BindView(R.id.ll_vp_arrow)
    LinearLayout ll_vp_arrow;

    private LoadingAlertDialog mLoadingAlertDialog;

    BatchFanAdapter vpAdapter;
    private List<FriendBean.Friend> friends;

    @Override
    public void init() {
        showBack();
        setMyTitle("批量加粉");
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        loadData();

        vp_list.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for(int i = 0 ; i < ll_vp_arrow.getChildCount() ; i++){
                    CircleImageView childAt = (CircleImageView) ll_vp_arrow.getChildAt(i);
                    if (i == position){
                        childAt.setImageResource(R.color.blue);
                    }else {
                        childAt.setImageResource(R.color.gray);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void loadData() {
        mLoadingAlertDialog.show();
        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().listQunFansAcc((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),null,null,null,null,24)).subscribe(new Subscriber<FriendBean>() {
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
            public void onNext(FriendBean friendBean) {
                if ("1".equals(friendBean.state)){
                    friends = friendBean.list;
                    vpAdapter = new BatchFanAdapter(WxBatchActivity.this, friendBean.list);
                    vp_list.setAdapter(vpAdapter);
                }else {
                    showTest(friendBean.msg);
                }
            }
        }));
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_wx_batch;
    }

    @OnClick({R.id.ll_load_fan,R.id.tv_batch_addfriend})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_load_fan:
                loadData();
                break;
            case R.id.tv_batch_addfriend:

                addContacts();
                break;
        }
    }

    private void addContacts() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String format = sdf.format(new Date());
        final int num = (int) SPUtils.get(format,0);
        final int memberstate = (int) SPUtils.get(SPUtils.KEY_MEMBER_STATE, 0);//会员状态  1是年费2是终身3不是会员
        final int IsExpire = (int) SPUtils.get(SPUtils.KEY_ISEXPIRE, 0);//是否已过期  1是会员已过期 0是正常
        if(((memberstate == 3) || (IsExpire == 1)) && num < 1) {
            mLoadingAlertDialog.show();
            ThreadManager.getInstance().run(new BaseRunnable() {
                @Override
                public void run() {
                    if (friends != null && !friends.isEmpty()) {
                        for (FriendBean.Friend friend : friends) {
                            addContact("mzt_" + friend.wx_name, friend.phone);
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int num1 = num;
                            mLoadingAlertDialog.dismiss();
                            showTest("添加完成！");
                            SPUtils.put(format, ++num1);
                        }
                    });
                }

                @Override
                public void run(Object... objs) {
                    super.run(objs);
                }
            });
        }else if ((memberstate == 3) || (IsExpire == 1)){
            showTest("非会员每天只有一次导入机会！");
        }else {
            mLoadingAlertDialog.show();
            ThreadManager.getInstance().run(new BaseRunnable() {
                @Override
                public void run() {
                    if (friends != null && !friends.isEmpty()) {
                        for (FriendBean.Friend friend : friends) {
                            addContact("mzt_" + friend.wx_name, friend.phone);
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int num1 = num;
                            mLoadingAlertDialog.dismiss();
                            showTest("添加完成！");
                            SPUtils.put(format, ++num1);
                        }
                    });
                }

                @Override
                public void run(Object... objs) {
                    super.run(objs);
                }
            });
        }

    }
    public void addContact(String name, String phoneNumber) {
        ContentValues values = new ContentValues();
        Uri rawContactUri = getContentResolver().insert(
                ContactsContract.RawContacts.CONTENT_URI, values);
        if(rawContactUri!= null) {
            long rawContactId = ContentUris.parseId(rawContactUri);
            if (!TextUtils.isEmpty(name)) {
                values.clear();
                values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
                values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
                values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
                getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
                        values);
            }
            // 向data表插入电话号码
            if (!TextUtils.isEmpty(phoneNumber)) {
                values.clear();
                values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
                values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
                values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
                        values);
            }
        }
    }

}
