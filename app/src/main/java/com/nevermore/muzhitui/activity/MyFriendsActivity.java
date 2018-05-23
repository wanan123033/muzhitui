package com.nevermore.muzhitui.activity;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicDefaultHeader;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.liucanwen.citylist.model.CityItem;
import com.liucanwen.citylist.widget.ContactItemInterface;
import com.liucanwen.citylist.widget.pinyin.PinYin;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.MyFriends;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nevermore.muzhitui.module.sqllite.DBManager;
import com.nevermore.muzhitui.module.sqllite.UserInfoRong;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
import base.view.LinearLayoutManagerWrapper;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import rx.Subscriber;
import rx.Subscription;


/**
 * Created by Simone on 2016/12/21.
 */

public class MyFriendsActivity extends BaseActivityTwoV implements TextWatcher {


    @BindView(R.id.input_search_query)
    EditText mInputSearchQuery;

    @BindView(R.id.lvMyFriend)
    RecyclerView mLvMyFriend;
    @BindView(R.id.pcMyFriendFlyt)
    PtrClassicFrameLayout mPcMyFriendFlyt;
    @BindView(R.id.ivInputMyfriendsDelete)
    ImageView mIvInputMyfriendsDelete;
    @BindView(R.id.friendBack)
    RelativeLayout mFriendBack;
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.addFriend)
    TextView mAddFriend;

    private CommonAdapter mAdapter;
    private RecyclerAdapterWithHF recyclerAdapterWithHF;

    List<MyFriends.LoginListBean> mLtObject = new ArrayList<>();
    private int mCurrenPager = 1;
    private String fragment;
    private DBManager mgr;
    String name = null;
    private LoadingAlertDialog mLoadingAlertDialog;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //应用的最后一个Activity关闭时应释放DB
        mgr.closeDB();
    }

    @Override
    public void init() {
        hideActionBar();
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        //初始化DBManager
        mgr = new DBManager(this);
        TextView tv = showRight();
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseStartActivity(AddFriendsActivity.class);
            }
        });

        fragment = getIntent().getStringExtra("fragment");
        if (fragment.equals("messagefragment")) {

            mTvTitle.setText("发起聊天");
            tv.setVisibility(View.GONE);
        } else if (fragment.equals("contactsfragment")) {

            mTvTitle.setText("我的好友");
            mAddFriend.setVisibility(View.VISIBLE);
        }
        List<UserInfoRong> userInfoRongs = mgr.queryFriends();
        if (userInfoRongs != null && userInfoRongs.size() > 0) {
            for (UserInfoRong userInfoRong : userInfoRongs) {

                mLtObject.add(new MyFriends.LoginListBean(userInfoRong.id, userInfoRong.user_name,
                        userInfoRong.headimg, userInfoRong.agent));
            }


            setAdapter();
        }

       /* //设置下拉刷新
        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(this);
        mPcMyFriendFlyt.addPtrUIHandler(header);*/
        mPcMyFriendFlyt.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mLtObject.clear();
                mCurrenPager=1;

                loadData(mCurrenPager);
            }

        });
        mInputSearchQuery.addTextChangedListener(this);
        mLvMyFriend.setLayoutManager(new LinearLayoutManagerWrapper(this, LinearLayoutManager.VERTICAL, false));//设置下拉刷新控件抛出异常崩溃捕捉。


    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


    }

    @Override
    public void afterTextChanged(Editable s) {

        name = mInputSearchQuery.getText().toString().trim();


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
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);

    }

    @OnClick({R.id.friendBack, R.id.addFriend,R.id.ivInputMyfriendsDelete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.friendBack:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mInputSearchQuery.getWindowToken(), 0);
                finish();
                break;
            case R.id.addFriend:
                baseStartActivity(AddFriendsActivity.class);
                break;
            case R.id.ivInputMyfriendsDelete:
                mInputSearchQuery.setText("");
                break;
        }
    }


    boolean inSearchMode = false;
    private Object searchLock = new Object();
    List<ContactItemInterface> contactList = new ArrayList<>();
    private SearchListTask curSearchTask = null;
    List<MyFriends.LoginListBean> mFliteLtObject = new ArrayList<>();//选择过滤的好友信息

    private class SearchListTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            contactList.clear();
            mFliteLtObject.clear();
            List<UserInfoRong> userInfoRongs = mgr.queryFriends();
            if (userInfoRongs != null && userInfoRongs.size() > 0) {
                for (UserInfoRong userInfoRong : userInfoRongs) {


                    contactList.add(new CityItem(userInfoRong.id,userInfoRong.user_name, PinYin.getPinYin(userInfoRong.user_name), userInfoRong.headimg,userInfoRong.agent));
                }



            }



            String keyword = params[0];

            inSearchMode = (keyword.length() > 0);

            if (inSearchMode) {

                // get all the items matching this
                for (ContactItemInterface item : contactList) {
                    CityItem contact = (CityItem) item;

                    boolean isPinyin = contact.getFullName().toUpperCase().indexOf(keyword) > -1;
                    boolean isChinese = contact.getNickName().indexOf(keyword) > -1;
                    if (isPinyin || isChinese) {

                        mFliteLtObject.add(new MyFriends.LoginListBean(((CityItem) item).getId(), ((CityItem) item).getNickName(), ((CityItem) item).getHeadimg(), ((CityItem) item).getAgent()));
                    }

                }

            }
            return null;
        }

        protected void onPostExecute(String result) {

            synchronized (searchLock) {

                if (inSearchMode) {
                    Log.e("onPostExecute","55555 长度："+mFliteLtObject.size());
                    mLtObject.clear();
                    mAdapter.addDate(mFliteLtObject);


                } else {
                    mLtObject.clear();
                    List<UserInfoRong> userInfoRongs = mgr.queryFriends();
                    if (userInfoRongs != null && userInfoRongs.size() > 0) {
                        for (UserInfoRong userInfoRong : userInfoRongs) {

                            mLtObject.add(new MyFriends.LoginListBean(userInfoRong.id, userInfoRong.user_name,
                                    userInfoRong.headimg, userInfoRong.agent));
                        }

                        Log.e("onPostExecute","6666 长度："+mLtObject.size());
                        setAdapter();
                    }
                }
            }


        }
    }




    private void setAdapter() {
        mAdapter = new CommonAdapter<MyFriends.LoginListBean>(getApplicationContext(), R.layout.item_lv_myfriend, mLtObject) {
            @Override
            public void convert(ViewHolder holder, MyFriends.LoginListBean myFriends) {

                holder.setImageURL(R.id.ivMyFriendHead, myFriends.getHeadimg(), false);
                holder.setText(R.id.ivMyFriendName, myFriends.getUser_name());
                if (fragment.equals("messagefragment")) {
                    holder.setVisible(R.id.ivMyFriendState1, false);
                    holder.setVisible(R.id.ivMyFriendState2, false);
                } else if (fragment.equals("contactsfragment")) {

                    if (!myFriends.getAgent().equals("未加入会员")) {
                        if (myFriends.getAgent().equals("年费会员")) {
                            holder.setVisible(R.id.ivMyFriendState1, true);
                            holder.setVisible(R.id.ivMyFriendState2, false);
                        } else {
                            holder.setVisible(R.id.ivMyFriendState1, false);
                            holder.setVisible(R.id.ivMyFriendState2, true);
                        }

                    }else{
                        holder.setVisible(R.id.ivMyFriendState1, false);
                        holder.setVisible(R.id.ivMyFriendState2, false);
                    }
                }


            }


        };


        recyclerAdapterWithHF = new RecyclerAdapterWithHF(mAdapter);
        recyclerAdapterWithHF.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {
                if (RongIM.getInstance() != null)
                    RongIM.getInstance().setMessageAttachedUserInfo(true);
                RongIM.getInstance().startPrivateChat(MyFriendsActivity.this, mLtObject.get(position).getId() + "", mLtObject.get(position).getUser_name() + "");

                recyclerAdapterWithHF.notifyItemChanged(position);
                finish();
            }
        });
        mLvMyFriend.setAdapter(recyclerAdapterWithHF);



    }
    private void loadData(int currentPager) {

        mLoadingAlertDialog.show();

        Subscription sbGetCode = wrapObserverWithHttp(WorkService.getWorkService().myFrind((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),currentPager, 1)).subscribe(new Subscriber<MyFriends>() {
            @Override
            public void onCompleted() {

                mLoadingAlertDialog.dismiss();
                removeLoadingView();
                // mPcMyFriendFlyt.setLoadMoreEnable(true);
                // mPcMyFriendFlyt.loadMoreComplete(true);
                mPcMyFriendFlyt.refreshComplete();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.e("error","---");
                mLoadingAlertDialog.dismiss();
                removeLoadingView();
                mPcMyFriendFlyt.refreshComplete();
                e.printStackTrace();
            }

            @Override
            public void onNext(MyFriends myFriends) {
                if ("1".equals(myFriends.getState())) {
                    mgr.deleteTableByDBNameAllAndFriends();//删除表里的数据
                    Log.e("error","- 删除--");
                    ArrayList<UserInfoRong> userInfoAddRongs = new ArrayList<UserInfoRong>();
                    for (MyFriends.LoginListBean friend : myFriends.getLoginList()) {
                        mLtObject.add(new MyFriends.LoginListBean(friend.getId(), friend.getUser_name(),
                                friend.getHeadimg(), friend.getAgent()));

                        setAdapter();
                        userInfoAddRongs.add(new UserInfoRong(friend.getId(), friend.getUser_name(), friend.getHeadimg(), friend.getAgent()));

                    }
                    List<UserInfoRong> levels = mgr.queryLevels();

                    mgr.addFriend(userInfoAddRongs);//添加到我的好友表里

                    mgr.add(levels);//将我的下级数据放入所有好友的表中
                       userInfoAddRongs.add(new UserInfoRong(1000, "拇指推官方账号", "http://www.muzhitui.cn/song/wx/img/logo.jpg", "2"));
                    mgr.add(userInfoAddRongs);//添加到所有好友的表里









                } else {
                    showTest(myFriends.getState());
                }

            }
        });
        addSubscription(sbGetCode);
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_myfriends;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation

        ButterKnife.bind(this);

    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            onKeyDown(KeyEvent.KEYCODE_BACK, event);
            //或者粗鲁一点，直接在调用finish()关闭activity
            //     (推荐第1种方式，由activity的onKeyDown统一处理）:
            finish();
            return true;
        }else {
            return super.dispatchKeyEvent(event);
        }
    }
}
