package com.nevermore.muzhitui.activity;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.QunFanOne;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import base.BaseActivityTwoV;
import base.ImageUtil;
import base.SPUtils;
import base.UIUtils;
import base.thread.BaseRunnable;
import base.thread.ThreadManager;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by Administrator on 2018/1/10.
 */

public class BusinessDatailActivity extends BaseActivityTwoV{
    public static final String BUSINESS_ID = "business_id";

    @BindView(R.id.iv_headimg)
    ImageView iv_headimg;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_info)
    TextView tv_info;
    @BindView(R.id.tv_wx_no)
    TextView tv_wx_no;
    @BindView(R.id.tv_info2)
    TextView tv_info2;
    @BindView(R.id.iv_qrcode)
    ImageView iv_qrcode;
    @BindView(R.id.iv_sex)
    ImageView iv_sex;

    private LoadingAlertDialog mLoadingAlertDialog;
    private QunFanOne qunFanOne;

    @Override
    public void init() {
        showBack();
        setMyTitle("个人名片");
        int id = getIntent().getIntExtra(BUSINESS_ID,0);
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        loadData(id);
    }

    private void loadData(int id) {
        mLoadingAlertDialog.show();
        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().getQunFansOne((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),id)).subscribe(new Subscriber<QunFanOne>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                removeLoadingView();
                removeErrorView();
                mLoadingAlertDialog.dismiss();
                showTest("网络请求错误"+e.getMessage());
            }

            @Override
            public void onNext(QunFanOne qunFanOne) {
                if("1".equals(qunFanOne.state)){
                    initQunFans(qunFanOne);
                }else {
                    showTest(qunFanOne.msg);
                }
            }
        }));
    }

    private void initQunFans(QunFanOne qunFanOne) {
        this.qunFanOne = qunFanOne;
        if(TextUtils.isEmpty(qunFanOne.wx_name)){
            tv_name.setText("");
        }else {
            tv_name.setText(qunFanOne.wx_name);
        }
        String string = "";
        if(!TextUtils.isEmpty(qunFanOne.province)){
            string = qunFanOne.province;
        }
        if(!TextUtils.isEmpty(qunFanOne.city)){
            string = string + qunFanOne.city;
        }
        if(!TextUtils.isEmpty(qunFanOne.industry_type)){
            string = string + " | " + qunFanOne.industry_type;
        }
        if (qunFanOne.sex == 1){
            iv_sex.setImageResource(R.mipmap.ic_nan);
        }else if (qunFanOne.sex == 2){
            iv_sex.setImageResource(R.mipmap.ic_nv);
        }else {
            iv_sex.setImageBitmap(null);
        }
        tv_info.setText(string);
        if(TextUtils.isEmpty(qunFanOne.wx_no)){
            tv_wx_no.setText("");
        }else {
            tv_wx_no.setText("微信号：" + qunFanOne.wx_no);
        }
        if(TextUtils.isEmpty(qunFanOne.introduce)){
            tv_info2.setText("");
        }else {
            tv_info2.setText(qunFanOne.introduce);
        }
        ImageLoader.getInstance().displayImage(qunFanOne.pic1,iv_qrcode);
        ImageLoader.getInstance().displayImage(qunFanOne.headimg,iv_headimg);
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_business_datail;
    }

    @OnClick({R.id.ll_saveqr,R.id.ll_copywx,R.id.ll_cantact})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_saveqr:  //保存二维码
                saveQr();
                break;
            case R.id.ll_copywx:  //复制微信号
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(qunFanOne.wx_no);
                showTest("已复制");
                break;
            case R.id.ll_cantact: //导入通讯录
                addContact("mzt_"+qunFanOne.phone,qunFanOne.phone);
                break;

        }
    }
    private AlertDialog alertDialog3;
    private void saveQr() {
        mLoadingAlertDialog.show();
        ThreadManager.getInstance().run(new BaseRunnable() {
            @Override
            public void run() {
                Bitmap bitmap = UIUtils.loadBitmapFromView(iv_qrcode);
                if (bitmap != null) {
                    try {
                        final String path = ImageUtil.saveImageToGallery(getApplicationContext(), bitmap);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showTest("已保存至" + path);
                                mLoadingAlertDialog.dismiss();
                            }
                        });
                    }catch (SecurityException e) {
                        e.printStackTrace();
                        alertDialog3 = UIUtils.getAlertDialog(BusinessDatailActivity.this, "权限设置", "请允许拇指推APP访问您的通讯录", null, "现在去设置", 0, null, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog3.dismiss();
                                if (Build.VERSION.SDK_INT >= 23) {
                                    requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION","android.permission.WRITE_EXTERNAL_STORAGE","android.permission.READ_EXTERNAL_STORAGE","android.permission.INTERNET"},1024);
                                }
                            }
                        });
                        alertDialog3.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void run(Object... objs) {
                super.run(objs);
            }
        });
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
            showTest("已导入通讯录");
        }
    }
}
