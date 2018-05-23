package com.nevermore.muzhitui.activity;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nevermore.muzhitui.MainActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.rongyun.MztRongContext;
import com.nevermore.muzhitui.fragment.TabMyFragment;
import com.nevermore.muzhitui.module.bean.FriendBean;
import com.nevermore.muzhitui.module.network.WorkService;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.UIUtils;
import base.thread.BaseRunnable;
import base.thread.ThreadManager;
import base.view.LoadingAlertDialog;
import base.view.WheelPickerView;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by Administrator on 2018/1/2.
 */

public class WxHDJFActivity extends BaseActivityTwoV{
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.tv_num)
    TextView tv_num;

    private Integer num;
    private LoadingAlertDialog mLoadingAlertDialog;
    private AlertDialog alertDialog;

    @Override
    public void init() {
        showBack();
        setMyTitle("号段加粉");
        mLoadingAlertDialog = new LoadingAlertDialog(this);
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_wx_hdjf;
    }

    @OnClick({R.id.rel_num,R.id.btn_hd})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rel_num:
                setNum();
                break;
            case R.id.btn_hd:
                loadData();
                break;
        }
    }

    private void setNum() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_set_num,null,false);
        TextView tv_10 = view.findViewById(R.id.tv_10);
        LinearLayout tv_50 = view.findViewById(R.id.ll_50);
        LinearLayout tv_100 = view.findViewById(R.id.ll_100);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        tv_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                num = 10;
                tv_num.setText("10个");
            }
        });
        tv_50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                final int memberstate = (int) SPUtils.get(SPUtils.KEY_MEMBER_STATE, 0);//会员状态  1是年费2是终身3不是会员
                final int IsExpire = (int) SPUtils.get(SPUtils.KEY_ISEXPIRE, 0);//是否已过期  1是会员已过期 0是正常
                //判断会员是否可分享
                String message = "您目前还不是会员，无法使用该功能；请购买会员后即可使用该功能";
                String clickmessage = "购买会员";
                if (IsExpire == 1) {
                    message = "您的会员已到期，无法使用该功能；请续费后即可恢复正常使用";
                    clickmessage = "续费";
                }

                if ((memberstate == 3) || (IsExpire == 1)) {
                    alertDialog = UIUtils.getAlertDialog(WxHDJFActivity.this, "提示信息", message, "取消", clickmessage, 0, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();

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

                num = 50;
                tv_num.setText("50个");
            }
        });
        tv_100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                final int memberstate = (int) SPUtils.get(SPUtils.KEY_MEMBER_STATE, 0);//会员状态  1是年费2是终身3不是会员
                final int IsExpire = (int) SPUtils.get(SPUtils.KEY_ISEXPIRE, 0);//是否已过期  1是会员已过期 0是正常
                //判断会员是否可分享
                String message = "您目前还不是终生会员，无法使用该功能；请购买终生会员后即可使用";
                String clickmessage = "购买会员";
                if (IsExpire == 1) {
                    message = "您的会员已到期，无法使用该功能；请续费后即可恢复正常使用";
                    clickmessage = "续费";
                }

                if ((memberstate == 3) || (IsExpire == 1) || (memberstate == 1) ) {
                    alertDialog = UIUtils.getAlertDialog(WxHDJFActivity.this, "提示信息", message, "取消", clickmessage, 0, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();

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


                num = 100;
                tv_num.setText("100个");
            }
        });
    }
    private String phone;
    private void loadData() {
        phone = et_phone.getText().toString().trim();
        if(num == 50) {
            final int memberstate = (int) SPUtils.get(SPUtils.KEY_MEMBER_STATE, 0);//会员状态  1是年费2是终身3不是会员
            final int IsExpire = (int) SPUtils.get(SPUtils.KEY_ISEXPIRE, 0);//是否已过期  1是会员已过期 0是正常
            //判断会员是否可分享
            String message = "您目前还不是终生会员，无法使用该功能；请购买终生会员后即可使用";
            String clickmessage = "购买会员";
            if (IsExpire == 1) {
                message = "您的会员已到期，无法使用该功能；请续费后即可恢复正常使用";
                clickmessage = "续费";
            }

            if ((memberstate == 3) || (IsExpire == 1)) {
                alertDialog = UIUtils.getAlertDialog(WxHDJFActivity.this, "提示信息", message, "取消", clickmessage, 0, new View.OnClickListener() {
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
        }else if(num == 100){
            final int memberstate = (int) SPUtils.get(SPUtils.KEY_MEMBER_STATE, 0);//会员状态  1是年费2是终身3不是会员
            final int IsExpire = (int) SPUtils.get(SPUtils.KEY_ISEXPIRE, 0);//是否已过期  1是会员已过期 0是正常
            //判断会员是否可分享
            String message = "您目前还不是终生会员，无法使用该功能；请购买终生会员后即可使用";
            String clickmessage = "购买会员";
            if (IsExpire == 1) {
                message = "您的会员已到期，无法使用该功能；请续费后即可恢复正常使用";
                clickmessage = "续费";
            }

            if ((memberstate == 3) || (IsExpire == 1) || (memberstate == 1) ) {
                alertDialog = UIUtils.getAlertDialog(WxHDJFActivity.this, "提示信息", message, "取消", clickmessage, 0, new View.OnClickListener() {
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
        }
        mLoadingAlertDialog.show();
        ThreadManager.getInstance().run(new BaseRunnable() {
            @Override
            public void run() {
                int[] randoms = randomCommon(0,9999,num);
                for(int random : randoms){
                    if(random >= 0 && random < 10){
                        phone = phone + "000" + random;
                    }else if (random >= 10 && random < 100){
                        phone = phone + "00" +random;
                    }else if(random >= 100 && random < 1000){
                        phone = phone + "0" + random;
                    }else {
                        phone = phone + random;
                    }
                    addContact("mzt_"+phone,phone);
                    phone = phone.substring(0,7);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLoadingAlertDialog.dismiss();
                        showTest("添加完成！");
                    }
                });
            }

            @Override
            public void run(Object... objs) {
                super.run(objs);
            }
        });

    }

    public static int[] randomCommon(int min, int max, int n){
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        int[] result = new int[n];
        int count = 0;
        while(count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if(num == result[j]){
                    flag = false;
                    break;
                }
            }
            if(flag){
                result[count] = num;
                count++;
            }
        }
        return result;
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
