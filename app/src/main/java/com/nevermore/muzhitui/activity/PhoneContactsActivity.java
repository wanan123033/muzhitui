package com.nevermore.muzhitui.activity;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.chanven.lib.cptr.PtrClassicDefaultHeader;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.liucanwen.citylist.model.CityItem;
import com.liucanwen.citylist.widget.ContactItemInterface;
import com.liucanwen.citylist.widget.pinyin.PinYin;
import com.nevermore.muzhitui.MainActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.Code;
import com.nevermore.muzhitui.module.bean.ContactsList;
import com.nevermore.muzhitui.module.network.WorkService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.network.RetrofitUtil;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
import base.thread.BaseRunnable;
import base.thread.ThreadManager;
import base.util.GetContactsUtil;
import base.view.LinearLayoutManagerWrapper;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import rx.Subscriber;
import rx.Subscription;


/**
 * Created by Simone on 2016/12/21.
 */

public class PhoneContactsActivity extends BaseActivityTwoV {


    @BindView(R.id.etPhoneContactsName)
    EditText mEtPhoneContactsName;
    @BindView(R.id.lvMyConstacts)
    RecyclerView mLvMyConstacts;
    @BindView(R.id.pcMyConstactsFlyt)
    PtrClassicFrameLayout mPcMyConstactsFlyt;

    private CommonAdapter mAdapter;
    private RecyclerAdapterWithHF recyclerAdapterWithHF;

    private ArrayList<ContactsList.PhoneListBean> mLtObject = new ArrayList<>();
    //  private ArrayList<ContactsList.PhoneListBean> mLtObjectFilte = new ArrayList<>();//用于用户信息清空以后存放到这里面
    String name = null;
    private LoadingAlertDialog mLoadingAlertDialog;

    @Override
    @TargetApi(23)
    public void init() {
        setMyTitle("添加手机联系人");
        showBack();
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        if (Build.VERSION.SDK_INT >= 23) {
            RongIM.getInstance().setRequestPermissionListener(new RongIM.RequestPermissionsListener() {
                @Override
                public void onPermissionRequest(String[] permissions, final int requestCode) {
                    for (final String permission : permissions) {
                        if (shouldShowRequestPermissionRationale(permission)) {
                            requestPermissions(new String[]{permission}, requestCode);
                        } else {
                            int isPermissionGranted = checkSelfPermission(permission);
                            if (isPermissionGranted != PackageManager.PERMISSION_GRANTED) {
                                new AlertDialog.Builder(PhoneContactsActivity.this)
                                        .setMessage("你需要在设置里打开以下权限:" + permission)
                                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                requestPermissions(new String[]{permission}, requestCode);
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        })
                                        .create().show();
                            }
                            return;
                        }
                    }
                }
            });
        }
    }
    //7内只执行一次
    private void setContacts() {
        JSONArray json = new JSONArray();
        try {
            for (int i = 0; i < mLtObject.size(); i++) {
                JSONObject jo = new JSONObject();
                jo.put("name", mLtObject.get(i).getTarget_name());
                jo.put("phone", mLtObject.get(i).getTarget_phone());
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
                mLoadingAlertDialog.dismiss();

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mLoadingAlertDialog.dismiss();
                showTest("服务器连接失败");
            }

            @Override
            public void onNext(ContactsList code) {
                if (code.getState().equals("1")) {
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

    /**
     * 点击邀请或者再次邀请 发送短信并且将所发送短信的id上次至服务器
     *
     * @param id
     */
    private void setItem(final int id) {
        mLoadingAlertDialog.show();

        Subscription sbMyAccount9 = wrapObserverWithHttp(WorkService.getWorkService().sendRyInvitation((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""), id)
        ).subscribe(new Subscriber<Code>() {
            @Override
            public void onCompleted() {
                mLoadingAlertDialog.dismiss();

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mLoadingAlertDialog.dismiss();
                showTest("服务器连接失败");

            }

            @Override
            public void onNext(Code code) {
                if (code.getState().equals("1")) {
                    showTest("短信已发送");
                    mLoadingAlertDialog.dismiss();
                    for (int i = 0; i < mLtObject.size(); i++) {
                        if (id == mLtObject.get(i).getId()) {
                            mLtObject.get(i).setFlag(2);
                        }

                    }
                    mAdapter.notifyDataSetChanged();

                } else {
                    showTest(code.getState());
                }

            }
        });

        addSubscription(sbMyAccount9);


    }

    private void setAdapter(List<ContactsList.PhoneListBean> mLtObject) {

        mLvMyConstacts.setLayoutManager(new LinearLayoutManagerWrapper(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new CommonAdapter<ContactsList.PhoneListBean>(PhoneContactsActivity.this, R.layout.item_lv_phone_contacts, mLtObject) {
            @Override
            public void convert(ViewHolder holder, final ContactsList.PhoneListBean myFriends) {
//                holder.setImageBitmap(R.id.ivPhoneContactsHead, myFriends.getPhoto());

                holder.setText(R.id.tvPhoneContactsName, myFriends.getTarget_name());
                holder.setText(R.id.tvPhoneContactsInfo, myFriends.getTarget_phone());
                if (myFriends.getFlag() == 0) {

                    holder.setVisible(R.id.tvInvitation, true);
                    holder.setVisible(R.id.tvInvitationstate, false);
                    holder.setText(R.id.tvInvitation, "邀请");
                } else if (myFriends.getFlag() == 1) {
                    holder.setVisible(R.id.tvInvitationstate, true);
                    holder.setVisible(R.id.tvInvitation, false);
                } else if (myFriends.getFlag() == 2) {
                    holder.setVisible(R.id.tvInvitation, true);
                    holder.setText(R.id.tvInvitation, "再次邀请");
                    holder.setVisible(R.id.tvInvitationstate, false);
                }
                holder.setOnClickListener(R.id.tvInvitation, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String htmlUrl = RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + RetrofitUtil.MYQR + MainActivity.loginId;
                        String message = "我最近在使用一个拇指推APP，,用了之后发现真的非常好,我真心的推荐给你也用一下! 你可以从应用市场下载，也可以打开下面的链接， 然后截屏用微信扫描关注就可以了，" + htmlUrl;

                        /**调用发送短信的界面**/


                      /*  SmsManager smsManager= SmsManager.getDefault();
                        if(message.length() > 70){
                            //拆分短信
                            ArrayList<String> phoneList = smsManager.divideMessage(message);
                            //发送短信
                            smsManager.sendMultipartTextMessage(myFriends.getTarget_phone(), null, phoneList, null, null);
                        }else {
                            //不超过70字时使用sendTextMessage发送
                            smsManager.sendTextMessage(myFriends.getTarget_phone(), null, message, null, null);
                        }*/
                        /** 调用发送短信的界面*/
//

                        //  setItem(myFriends.getId());
                        File file = new File((String) SPUtils.get(SPUtils.KEY_POP_SNAP,""));
                        if(file.exists()) {
                            Intent intent = new Intent(Intent.ACTION_SEND,  Uri.parse("mms://"));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(Intent.EXTRA_STREAM, file.toURI());// uri为你的附件的uri
                            intent.putExtra("subject", "拇指推彩信"); //彩信的主题
                            intent.putExtra("address", myFriends.getTarget_phone()); //彩信发送目的号码
                            intent.putExtra("sms_body", message); //彩信中文字内容
                            intent.putExtra(Intent.EXTRA_TEXT, "");
                            intent.setType("image/*");// 彩信附件类型
                            startActivity(Intent.createChooser(intent, "MMS:"));
                        }else {
                            showTest("请先保存一张推广海报，再来邀请哦！");
                        }
                    }
                });
            }


        };


        recyclerAdapterWithHF = new RecyclerAdapterWithHF(mAdapter);

        mLvMyConstacts.setAdapter(recyclerAdapterWithHF);


    }

    class EditChangedListener implements TextWatcher {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            name = mEtPhoneContactsName.getText().toString().trim();


            if (curSearchTask != null
                    && curSearchTask.getStatus() != AsyncTask.Status.FINISHED) {
                try {
                    curSearchTask.cancel(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            curSearchTask = new SearchListTask();
            curSearchTask.execute(name);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    ;
    boolean inSearchMode = false;
    private Object searchLock = new Object();
    List<ContactItemInterface> contactList = new ArrayList<>();
    private SearchListTask curSearchTask = null;
    private ArrayList<ContactsList.PhoneListBean> mFliteLtObject = new ArrayList<>();

    private class SearchListTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            mFliteLtObject.clear();


            String keyword = params[0];

            inSearchMode = (keyword.length() > 0);

            if (inSearchMode) {
                // get all the items matching this
                for (ContactItemInterface item : contactList) {
                    CityItem contact = (CityItem) item;

                    boolean isPinyin = contact.getFullName().toUpperCase().indexOf(keyword) > -1;
                    boolean isChinese = contact.getNickName().indexOf(keyword) > -1;

                    if (isPinyin || isChinese) {


                        mFliteLtObject.add(new ContactsList.PhoneListBean(contact.getFlag(), contact.getId(), contact.getNickName(), contact.getHeadimg(), contact.getBitmap()));
                    }

                }

            }
            return null;
        }

        protected void onPostExecute(String result) {

            synchronized (searchLock) {

                if (inSearchMode) {
                    mLtObject.clear();

                    setAdapter(mFliteLtObject);

                } else {
                    mLtObject.clear();
                    mLtObject = GetContactsUtil.getAllContacts(getApplicationContext());
                    setAdapter(mLtObject);


                }
            }


        }
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_phone_contacts;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        // 默认软键盘不弹出
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(mAdapter == null || mAdapter.getItemCount() == 0) {
            mLoadingAlertDialog.show();
            ThreadManager.getInstance().run(new BaseRunnable() {
                @Override
                public void run() {
                    mLtObject = GetContactsUtil.getAllContacts(getApplicationContext());
                    for (ContactsList.PhoneListBean contacts : mLtObject) {
                        contactList.add(new CityItem(contacts.getTarget_name(), PinYin.getPinYin(contacts.getTarget_name()), contacts.getTarget_phone(), null));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mEtPhoneContactsName.addTextChangedListener(new EditChangedListener());
                            PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(PhoneContactsActivity.this);
                            mPcMyConstactsFlyt.addPtrUIHandler(header);
                            mPcMyConstactsFlyt.setPtrHandler(new PtrHandler() {
                                @Override
                                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                                    return false;
                                }

                                @Override
                                public void onRefreshBegin(PtrFrameLayout frame) {

                                }
                            });
                            long time = (long) SPUtils.get(SPUtils.KEY_PHONE_TIME, 0l);
                            if (System.currentTimeMillis() > 7 * 24 * 60 * 60 * 1000 + time) {
                                setContacts();
                                SPUtils.put(SPUtils.KEY_PHONE_TIME, System.currentTimeMillis());
                            }

                            setAdapter(mLtObject);
                            mLoadingAlertDialog.dismiss();
                        }
                    });
                }
            });
        }
    }
}
