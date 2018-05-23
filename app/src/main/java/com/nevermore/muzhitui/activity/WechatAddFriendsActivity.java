package com.nevermore.muzhitui.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.lljjcoder.citypickerview.widget.CityPicker;
import com.nevermore.muzhitui.MainActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.rongyun.MztRongContext;
import com.nevermore.muzhitui.fragment.TabMyFragment;
import com.nevermore.muzhitui.module.bean.ContactsList;
import com.nevermore.muzhitui.module.bean.RyWxPhone;
import com.nevermore.muzhitui.module.bean.WxFans;
import com.nevermore.muzhitui.module.bean.WxFansCount;
import com.nevermore.muzhitui.module.network.WorkService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.MyLogger;
import base.SPUtils;
import base.UIUtils;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
import base.thread.BaseRunnable;
import base.thread.Handler;
import base.thread.ThreadManager;
import base.util.GetContactsUtil;
import base.util.ProgressUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by simone on 2017/6/5.
 * 微信加粉
 */

public class WechatAddFriendsActivity extends BaseActivityTwoV implements Handler.HandlerListener{
    private static final int HANDLER_SECURITY_MSG = 220;
    private static final int HANDLERMESSAGE_DELETE = 226;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.lvWechatAddFriend)
    RecyclerView lvWechatAddFriend;
    @BindView(R.id.pcWechatAddFriendFlyt)
    PtrClassicFrameLayout pcWechatAddFriendFlyt;
    @BindView(R.id.tvAddFriends)
    TextView tvAddFriends;
    @BindView(R.id.tvDeleteFriends)
    TextView tvDeleteFriends;
    @BindView(R.id.rlAddFriends)
    RelativeLayout rlAddFriends;
    @BindView(R.id.rlDeleteFriends)
    RelativeLayout rlDeleteFriends;
    @BindView(R.id.llTool)
    LinearLayout llTool;

    private int pageNumber;  //当前页
    private String privder;  //省份
    private String city;     //城市



    private ArrayList<RyWxPhone.WxPhone> mLtObject = new ArrayList<>();
    private CommonAdapter mAdapter;
    private RecyclerAdapterWithHF recyclerAdapterWithHF;
    private int countAdd = 0;   //当前需要加粉数量
    private final int sumAdd = 400;   //当天最多加粉数量

    private AlertDialog alertDialog, alertDialog1,alertDialog2;

    private static final int HANDLERMESSAGE = 233;
    private KProgressHUD mLoadingAlertDialog;
    private int count;

    @Override
    public void init() {
        setMyTitle(" 微信加粉");
        showBack();

//        showRight(R.mipmap.contact_info, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        //测试代码
//        for (int i = 0; i < 120; i++) {
//            ContactsList.PhoneListBean p1;
//            if (i < 100) {
//                p1 = new ContactsList.PhoneListBean(1, "muzhitui_156074648" + i, "156074648" + i);
//            } else {
//                p1 = new ContactsList.PhoneListBean(0, "muzhitui_156074648" + i, "156074648" + i);
//            }
//
//            mLtObject.add(p1);
//        }

//        setAdapter();

        loadData(1,null,null);

        alertDialog = UIUtils.getAlertDialog(this, "", "拇指推会员每日最多可添加400个微信好友\n您今日已添加", "继续加粉", "打开微信", 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                loadData(pageNumber,privder,city);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(pageNumber,privder,city);
                JumpWeixin();
                alertDialog.dismiss();
            }
        });
        alertDialog1 = UIUtils.getAlertDialog(this, "", "数据已清空", null, "确定", 0, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });
        alertDialog2 = UIUtils.getAlertDialog(this, "", "您的今日加粉数量已满,请明日继续哦", null, "确定", 0, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog2.dismiss();
            }
        });
        pcWechatAddFriendFlyt.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mLtObject.clear();
                loadData(1,privder,city);
                updateWxCountAdd();

            }

        });
        pcWechatAddFriendFlyt.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                pageNumber++;
                loadData(pageNumber,privder,city);
                updateWxCountAdd();
            }
        });

        showRight("使用说明", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseStartActivity(WxUseDirectionsActivity.class);
            }
        });

        long time = (long) SPUtils.get(SPUtils.KEY_PHONE_TIME,0l);
        if(System.currentTimeMillis() > 7 * 24 * 60 * 60 * 1000 + time){
            setContacts();
            SPUtils.put(SPUtils.KEY_PHONE_TIME,System.currentTimeMillis());
        }

//        if (!checkContacts()) {
//            alertDialog3 = UIUtils.getAlertDialog(this, "权限设置", "请允许拇指推APP访问您的通讯录", null, "现在去设置", 0, null, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    alertDialog3.dismiss();
//                    if (Build.VERSION.SDK_INT >= 23) {
//                        requestPermissions(new String[]{"android.permission.READ_CONTACTS","android.permission.WRITE_CONTACTS"},1024);
//                    }
//                }
//            });
//            alertDialog3.show();
//        }
    }
    //7内只执行一次
    private void setContacts() {
        List<ContactsList.PhoneListBean> phones = null;
        try {
            phones = GetContactsUtil.getAllContacts(getApplicationContext());
        }catch (SecurityException e){
            Handler.getHandler().sendEmptyMessage(HANDLER_SECURITY_MSG,this);
        }

        JSONArray json = new JSONArray();
        try {
            for (int i = 0; i < phones.size(); i++) {
                JSONObject jo = new JSONObject();
                jo.put("name", phones.get(i).getTarget_phone());
                jo.put("phone", phones.get(i).getTarget_phone());
                json.put(jo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("json:", json.toString());
        Subscription sbMyAccount9 = wrapObserverWithHttp(WorkService.getWorkService().getRyPhoneList((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""), json.toString())
        ).subscribe(new Subscriber<ContactsList>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                showTest("服务器连接失败");

            }

            @Override
            public void onNext(ContactsList code) {
                if (code.getState().equals("1")) {
                    if(mLoadingAlertDialog != null)
                        mLoadingAlertDialog.dismiss();

                  /*  for (int i = 0; i < mLtObject.size(); i++) {
                        for (int j = 0; j < code.getPhoneList().size(); j++) {
                            if (mLtObject.get(i).getTarget_phone().equals(code.getPhoneList().get(j).getTarget_phone())) {
                                mLtObject.get(i).setId(code.getPhoneList().get(j).getId());
                                mLtObject.get(i).setFlag(code.getPhoneList().get(j).getFlag());
                            }
                        }
                    }*/

                    //  mLtObjectFilte.addAll(mLtObject);
                    //  setAdapter();

                } else {
                    showTest(code.getState());
                }

            }
        });

        addSubscription(sbMyAccount9);


    }

    private void loadData(final int pageNumber, String privder, String city) {
        this.pageNumber = pageNumber;
        ProgressUtil.showDialog(this,"加载中...");
        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().getRyWxPhoneList((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""), pageNumber, 400-(count+mLtObject.size())-1 > 50 ? 50 : 400-(count+mLtObject.size())-1, privder, city)).subscribe(new Observer<RyWxPhone>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                pcWechatAddFriendFlyt.setLoadMoreEnable(true);
                pcWechatAddFriendFlyt.loadMoreComplete(true);
                pcWechatAddFriendFlyt.refreshComplete();
                ProgressUtil.closeDialog();
            }

            @Override
            public void onError(Throwable e) {
                removeLoadingView();
                showErrorView();
                e.printStackTrace();
            }

            @Override
            public void onNext(RyWxPhone phone) {
                if (phone.getState() == 1){
                    if(pageNumber > phone.getAllPages()){
                        showTest("到底了");
                    }
                     if (pageNumber == 1){
                         mLtObject.clear();
                         mLtObject.addAll(phone.getPhoneList());
                         setAdapter();
                     }else {
                         mAdapter.addDate(phone.getPhoneList());
                     }
                }else {
                    showTest("服务器数据异常");
                }

                updateWxCountAdd();
            }
        }));
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_wechat_add_friends;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);

    }

    private void setAdapter() {
        mAdapter = new CommonAdapter<RyWxPhone.WxPhone>(getApplicationContext(), R.layout.item_lv_wechat_add_friend, mLtObject) {
            @Override
            public void convert(ViewHolder holder, RyWxPhone.WxPhone phoneListBean) {

                //holder.setImageURL(R.id.ivWechatAddFriendHead, phoneListBean.getHeadimg(), true);
                holder.setText(R.id.tvWechatFriendName, phoneListBean.getProvince()+"-"+phoneListBean.getCity());
                holder.setText(R.id.tvWechatphone, phoneListBean.getTarget_phone());
                if (phoneListBean.getFlag() == 1) {
                    holder.setImageResource(R.id.iv_check_friend, R.drawable.selector_check);
                } else {
                    holder.setImageResource(R.id.iv_check_friend, R.mipmap.ic_checked_down);
                }
            }
        };


        recyclerAdapterWithHF = new RecyclerAdapterWithHF(mAdapter);
        recyclerAdapterWithHF.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {
                ViewHolder holder = (ViewHolder) vh;
                if(mLtObject.get(position).getFlag() == 1){
                    mLtObject.get(position).setFlag(0);
                }else{
                    mLtObject.get(position).setFlag(1);
                }
                if (mLtObject.get(position).getFlag() == 1) {
                    holder.setImageResource(R.id.iv_check_friend, R.drawable.selector_check);
                } else {
                    holder.setImageResource(R.id.iv_check_friend, R.mipmap.ic_checked_down);
                }
                updateWxCountAdd();
//                if (mLtObject.get(position).getFlag() == 1) {
//                    mLtObject.get(position).setFlag(0);
//
//                } else {
//                    if (countAdd >= 100) {
//                        showTest("最多只能选择100个！");
//                        return;
//                    }
//                    mLtObject.get(position).setFlag(1);
//
//                }
//                countAdd = 0;
//                for (RyWxPhone.WxPhone p : mLtObject) {
//                    if (p.getFlag() == 1) {
//                        countAdd = countAdd + 1;
//                    }
//                }
//                tvAddFriends.setText("微信加粉(" + countAdd + ")");
//                recyclerAdapterWithHF.notifyItemChanged(position);
            }
        });
        lvWechatAddFriend.setAdapter(recyclerAdapterWithHF);


    }

    @OnClick({R.id.tvAddress, R.id.rlAddFriends, R.id.rlDeleteFriends})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvAddress:
                setPickCity();
                break;
            case R.id.rlAddFriends:
                if (checkContacts()) {
                    final int memberstate = (int) SPUtils.get(SPUtils.KEY_MEMBER_STATE, 0);//会员状态  1是年费2是终身3不是会员
                    final int IsExpire = (int) SPUtils.get(SPUtils.KEY_ISEXPIRE, 0);//是否已过期  1是会员已过期 0是正常

                    //判断会员是否可分享
                    String message = "您还不是拇指推会员，无法使用此功能，请购买会员共享百万好友资源";
                    String clickmessage = "购买会员";
                    if (IsExpire == 1) {
                        message = "您的会员已到期，无法加粉；请续费后即可恢复正常使用";
                        clickmessage = "续费";
                    }

                    if ((memberstate == 3) || (IsExpire == 1)) {
                        alertDialog = UIUtils.getAlertDialog(WechatAddFriendsActivity.this, "你目前没有加粉权限", message, "取消", clickmessage, 0, new View.OnClickListener() {
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
                        break;
                    }
                    int i = (int) SPUtils.get(SPUtils.KEY_MEMBER_STATE,0);
                    SPUtils.get(SPUtils.KEY_ISEXPIRE,2);
                    ProgressUtil.showDialog(this,"加粉中...");
                    addContact();

                } else {
                    alertDialog3 = UIUtils.getAlertDialog(this, "权限设置", "请允许拇指推APP访问您的通讯录", null, "现在去设置", 0, null, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog3.dismiss();
                            if (Build.VERSION.SDK_INT >= 23) {
                                requestPermissions(new String[]{"android.permission.READ_CONTACTS","android.permission.WRITE_CONTACTS"},1024);
                            }
                        }
                    });
                    alertDialog3.show();
                }
                break;
            case R.id.rlDeleteFriends:
                ProgressUtil.showDialog(this,"清除中...");

                testDeleteId("mzt_");

                break;
        }
    }

    private void setPickCity() {
        final CityPicker cityPicker = new CityPicker.Builder(WechatAddFriendsActivity.this).textSize(20)
                .titleTextColor("#000000")
                .backgroundPop(0xa0000000)
                .province("全国")
                .city("不限")
                //.district("不限")
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .build();

        cityPicker.show();
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
              /*  tv_resultWheel.setText("选择结果：\n省：" + citySelected[0] + "\n市：" + citySelected[1] + "\n区："
                        + citySelected[2] + "\n邮编：" + citySelected[3]);*/
                if (citySelected[1].equals("不限") || citySelected[1].equals(citySelected[0])) {
                    tvAddress.setText(citySelected[0]);
                } else {
                    tvAddress.setText(citySelected[0] + " • " + citySelected[1]);
                }
                if(citySelected[1].equals("不限")){
                    city = null;
                }else {
                    city = citySelected[1].substring(0, citySelected[1].length() - 1);
                }
                if(citySelected[0].equals("不限")){
                    privder = null;
                }else {
                    privder = citySelected[0].substring(0, citySelected[0].length() - 1);
                }
                if(citySelected[0].equals("全国")){
                    privder = null;
                    city = null;
                }
                pageNumber = 1;
                MyLogger.kLog().e("pageNumber="+pageNumber+",privder="+citySelected[0]+",city="+citySelected[1]);
                loadData(pageNumber,privder,city);
            }

            @Override
            public void onCancel() {
                Toast.makeText(WechatAddFriendsActivity.this, "已取消", Toast.LENGTH_LONG).show();
            }
        });
    }
    AlertDialog dialog;
    /***
     * 添加联系人到手机通讯录
     * @param
     * @param
     */
    public void addContact() {

        rlAddFriends.setEnabled(false);
        rlAddFriends.setClickable(false);
        Subscription subscription = wrapObserverWithHttp(WorkService.getWorkService().getWxFansCount((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))).subscribe(new Observer<WxFansCount>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(WxFansCount wfc) {
                count = wfc.getNum();
                MyLogger.kLog().e("----=-"+wfc.getNum());
                if(wfc.getState() == 1 && wfc.getNum() + countAdd < sumAdd){
                    addFriend();
                }else{
                    dialog = UIUtils.getAlertDialog(WechatAddFriendsActivity.this, "温馨提示", "您的加粉数量已满，请明日继续哦！", "确定", "打开微信", 0, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JumpWeixin();
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    Handler.getHandler().sendEmptyMessage(1233,WechatAddFriendsActivity.this);
                }
            }
        });
        addSubscription(subscription);
    }

    private void updateWxFuns() {
        Subscription subscription = wrapObserverWithHttp(WorkService.getWorkService().addWxFans((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),countAdd)).subscribe(new Observer<WxFans>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(WxFans o) {
                if(o.getState() == 2){
                    alertDialog2.show();
                    Handler.getHandler().sendEmptyMessage(1233,WechatAddFriendsActivity.this);
                }else {

                }
            }
        });
        addSubscription(subscription);
    }

    /**
     * 联系人模糊查询
     *
     * @param text
     */
    public void testDeleteId(final String text) {
        rlDeleteFriends.setEnabled(false);
        rlDeleteFriends.setClickable(false);
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

                    Handler.getHandler().sendEmptyMessage(HANDLERMESSAGE_DELETE,WechatAddFriendsActivity.this);
                }catch (SecurityException e){
                    Handler.getHandler().sendEmptyMessage(HANDLER_SECURITY_MSG,WechatAddFriendsActivity.this);
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

    /**
     * 跳转到微信
     */
    private void JumpWeixin() {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");

            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // TODO: handle exception
            Toast.makeText(this, "检查到您手机没有安装微信，请安装后使用该功能", Toast.LENGTH_LONG).show();
        }
    }
    AlertDialog alertDialog3 = null;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void handleMessage(Message msg) {
        ProgressUtil.closeDialog();
        rlDeleteFriends.setEnabled(true);
        rlDeleteFriends.setClickable(true);
        rlAddFriends.setEnabled(true);
        rlAddFriends.setClickable(true);
        if(msg.what == HANDLER_SECURITY_MSG){
            if(Integer.parseInt(android.os.Build.VERSION.SDK) >= 23) {
                requestPermissions(new String[]{"android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS"}, 1255);
            }else {
                alertDialog3 = UIUtils.getAlertDialog(WechatAddFriendsActivity.this, "权限设置", "请允许拇指推APP访问您的通讯录", null, "现在去设置", 0,null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog3.dismiss();
                    }
                });
                alertDialog3.show();
            }
        }else if(msg.what == HANDLERMESSAGE_DELETE){
            alertDialog1.show();
        }
    }

    private void updateWxCountAdd(){
        countAdd = 0;
        for (RyWxPhone.WxPhone p : mLtObject) {
            if (p.getFlag() == 1) {
                countAdd ++;
            }
        }
        tvAddFriends.setText("微信加粉(" + countAdd + "/" + sumAdd + ")");
    }

    private void addFriend(){
        ThreadManager.getInstance().run(new BaseRunnable() {
            @Override
            public void run() {
                try {
                    for (RyWxPhone.WxPhone p : mLtObject) {
                        if (p.getFlag() == 1) {
                            ContentValues values = new ContentValues();
                            Uri rawContactUri = getContentResolver().insert(
                                    ContactsContract.RawContacts.CONTENT_URI, values);
                            if(rawContactUri!= null) {
                                long rawContactId = ContentUris.parseId(rawContactUri);
                                // 向data表插入数据
                                if (!TextUtils.isEmpty(p.getProvince()+"-"+p.getCity()+p.getTarget_phone())) {
                                    values.clear();
                                    values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
                                    values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
                                    values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, "mzt_"+p.getTarget_phone());
                                    getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
                                            values);
                                }
                                // 向data表插入电话号码
                                if (!TextUtils.isEmpty(p.getTarget_phone())) {
                                    values.clear();
                                    values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
                                    values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                                    values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, p.getTarget_phone());
                                    values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                                    getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
                                            values);
                                }
                            }
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alertDialog = UIUtils.getAlertDialog(WechatAddFriendsActivity.this, "成功加粉提示", "拇指推会员每日最多可添加400个微信好友\n您今日已添加"+(count+mLtObject.size())+"位手机好友", "继续加粉", "打开微信", 0, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                    loadData(pageNumber,privder,city);
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    loadData(pageNumber,privder,city);
                                    JumpWeixin();
                                    alertDialog.dismiss();
                                }
                            });
                            alertDialog.show();
                        }
                    });

                    //上传当前加粉数
                    updateWxFuns();
                }catch (SecurityException e){
                    sendHandlerMessage(HANDLER_SECURITY_MSG);
                }
                setListener(WechatAddFriendsActivity.this);
                sendHandlerMessage(HANDLERMESSAGE);
            }
        });
    }

    private boolean checkContacts() {
        try {
            Cursor cursor = getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if (cursor != null && cursor.getCount() >= 0 && cursor.moveToNext()) {
                return true;
            } else {
                return false;
            }
        }catch (SecurityException e){
            return false;
        }

    }

}
