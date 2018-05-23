package com.nevermore.muzhitui.activity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.nevermore.muzhitui.MainActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.rongyun.MztRongContext;
import com.nevermore.muzhitui.fragment.TabMyFragment;
import com.nevermore.muzhitui.module.bean.QunFansAcc;
import com.nevermore.muzhitui.module.network.WorkService;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.UIUtils;
import base.thread.BaseRunnable;
import base.thread.Handler;
import base.thread.ThreadManager;
import base.util.ProgressUtil;
import base.view.DragView;
import base.view.VideoGpsView;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/12/29.
 */

public class WxAddFriendActivity extends BaseActivityTwoV implements Handler.HandlerListener {
    private static final int HANDLERMESSAGE_DELETE = 1698;
    private static final int HANDLER_SECURITY_MSG = 1536;

    @BindView(R.id.rel_jzjf)
    RelativeLayout rel_jzjf;
    @BindView(R.id.dragview)
    DragView dragview;

    private AlertDialog alertDialog;

    @Override
    public void init() {
        showBack();
        setMyTitle("微信加粉");

        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().isQunFansAcc()).subscribe(new Subscriber<QunFansAcc>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(QunFansAcc qunFansAcc) {
                if("1".equals(qunFansAcc.state)){
                    if(qunFansAcc.is_qunfans_acc == 0){
                        rel_jzjf.setVisibility(View.GONE);
                    }else if(qunFansAcc.is_qunfans_acc == 1){
                        rel_jzjf.setVisibility(View.VISIBLE);
                    }
                }
            }
        }));
        final VideoGpsView videoGpsView = new VideoGpsView(this,3);
        dragview.addDragView(videoGpsView, 550, 850, true, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoGpsView.onClick(v);
            }
        });
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_wx_addfriend;
    }

    @OnClick({R.id.rel_hf,R.id.rel_jzjf,R.id.rel_hdjf,R.id.rel_ydbf,R.id.rel_qltxl,R.id.rel_syjc,R.id.rel_batch_hf})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rel_hf:
                baseStartActivity(WxHuFenActivity.class);
                break;
            case R.id.rel_jzjf:
                baseStartActivity(WxJZJFActivity.class);
                break;
            case R.id.rel_hdjf:
                baseStartActivity(WxHDJFActivity.class);
                break;
            case R.id.rel_ydbf:
                final int memberstate = (int) SPUtils.get(SPUtils.KEY_MEMBER_STATE, 0);//会员状态  1是年费2是终身3不是会员
                final int IsExpire = (int) SPUtils.get(SPUtils.KEY_ISEXPIRE, 0);//是否已过期  1是会员已过期 0是正常
                //判断会员是否可分享
                String message = "您目前还不是会员，无法使用该功能；请购买会员后即可发布动态";
                String clickmessage = "购买会员";
                if (IsExpire == 1) {
                    message = "您的会员已到期，无法使用该功能；请续费后即可恢复正常使用";
                    clickmessage = "续费";
                }

                if ((memberstate == 3) || (IsExpire == 1)) {
                    alertDialog = UIUtils.getAlertDialog(WxAddFriendActivity.this, "提示信息", message, "取消", clickmessage, 0, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            finish();

                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            //finish();
                            baseStartActivity(MainActivity.class);
                            TabMyFragment.mIsBuy = true;
                            MztRongContext.getInstance().popAllActivity(3);
                        }
                    });
                    alertDialog.show();
                    return;
                }
                baseStartActivity(WechatAddFriendsActivity.class);
                break;
            case R.id.rel_qltxl:
                ProgressUtil.showDialog(this,"清除中...");
                testDeleteId("mzt_");
                break;
            case R.id.rel_syjc:
                baseStartActivity(WxUseDirectionsActivity.class);
                break;
            case R.id.rel_batch_hf:
                baseStartActivity(WxBatchActivity.class);
                break;

        }
    }

    /**
     * 联系人模糊查询
     *
     * @param text
     */
    public void testDeleteId(final String text) {
        ThreadManager.getInstance().run(new BaseRunnable() {
            @Override
            public void run() {
                try {

                    ContentResolver cr = getContentResolver();
                    String projection[] = {ContactsContract.Contacts.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                    };
                    Cursor cursor = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            projection, ContactsContract.Contacts.DISPLAY_NAME
                                    + " like " + "'%"
                                    + text + "%'", null,
                            null);

                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor
                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String number = cursor.getString(cursor
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.e("===：", name + "   " + number);
                        try {
                            testDelete(name);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    cursor.close();

                    Handler.getHandler().sendEmptyMessage(HANDLERMESSAGE_DELETE,WxAddFriendActivity.this);
                }catch (SecurityException e){
                    Handler.getHandler().sendEmptyMessage(HANDLER_SECURITY_MSG,WxAddFriendActivity.this);
                }
            }
        });

    }


    /**
     * 删除联系人
     *
     * @param name
     * @throws Exception
     */
    public void testDelete(String name) throws Exception {

        //根据姓名求id
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Contacts.Data._ID}, "display_name=?", new String[]{name}, null);
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            //根据id删除data中的相应数据
            resolver.delete(uri, "display_name=?", new String[]{name});
            uri = Uri.parse("content://com.android.contacts/data");
            resolver.delete(uri, "raw_contact_id=?", new String[]{id + ""});
        }
    }
    private AlertDialog alertDialog3,alertDialog1;
    @Override
    public void handleMessage(Message msg) {
        ProgressUtil.closeDialog();
        if(msg.what == HANDLER_SECURITY_MSG){
            if(Integer.parseInt(android.os.Build.VERSION.SDK) >= 23) {
                requestPermissions(new String[]{"android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS"}, 1255);
            }else {
                alertDialog3 = UIUtils.getAlertDialog(WxAddFriendActivity.this, "权限设置", "请允许拇指推APP访问您的通讯录", null, "现在去设置", 0,null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog3.dismiss();
                    }
                });
                alertDialog3.show();
            }
        }else if(msg.what == HANDLERMESSAGE_DELETE){
            alertDialog1 = UIUtils.getAlertDialog(this, "", "数据已清空", null, "确定", 0, null, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog1.dismiss();
                }
            });
            alertDialog1.show();
        }
    }


}
