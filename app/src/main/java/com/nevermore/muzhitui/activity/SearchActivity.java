package com.nevermore.muzhitui.activity;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;


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
import com.nevermore.muzhitui.module.bean.MyLever;
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

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Simone on 2016/12/21.
 */

public class SearchActivity extends BaseActivityTwoV implements/* View.OnClickListener,*/ TextWatcher {


    @BindView(R.id.input_search_query)
    EditText mInputSearchQuery;
    @BindView(R.id.lvSearch)
    RecyclerView mLvSearch;
    @BindView(R.id.pcSearchFlyt)
    PtrClassicFrameLayout mPcSearchFlyt;
    private CommonAdapter mAdapter;
    private RecyclerAdapterWithHF recyclerAdapterWithHF;

    private int mCurrenPager = 1;
    List<MyFriends.LoginListBean> mLtObject = new ArrayList<>();

    private LoadingAlertDialog mLoadingAlertDialog;
    String name = null;
    private DBManager mgr;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //应用的最后一个Activity关闭时应释放DB
        mgr.closeDB();
    }

    @Override
    public void init() {

        showBack();

      /*  showRight("搜索", this);*/
        //初始化DBManager
        mgr = new DBManager(this);

        mLoadingAlertDialog = new LoadingAlertDialog(this);

        setMyTitle("我的客户搜索");

        List<UserInfoRong> levels = mgr.queryLevels();
        Log.e("=======下级===",levels.size()+"");
        if (levels != null && levels.size() > 0) {
            for (UserInfoRong level : levels) {

                mLtObject.add(new MyFriends.LoginListBean(level.id, level.user_name,
                        level.headimg, level.agent));
            }


            setAdapter();
        }
        mPcSearchFlyt.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mLtObject.clear();
                mCurrenPager = 1;

                loadData();
            }

        });

        mInputSearchQuery.addTextChangedListener(this);
        mLvSearch.setLayoutManager(new LinearLayoutManagerWrapper(this, LinearLayoutManager.VERTICAL, false));//设置下拉刷新控件抛出异常崩溃捕捉。

    }




    private void loadDataSub(int currentPager) {

        mLoadingAlertDialog.show();

        Subscription sbGetCode = wrapObserverWithHttp(WorkService.getWorkService().myLevelMemberName((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),currentPager, 1, name)).subscribe(new Subscriber<MyFriends>() {
            @Override
            public void onCompleted() {

                mLoadingAlertDialog.dismiss();
                removeLoadingView();
                mPcSearchFlyt.setLoadMoreEnable(true);
                mPcSearchFlyt.loadMoreComplete(true);
            }

            @Override
            public void onError(Throwable e) {

                mLoadingAlertDialog.dismiss();
                removeLoadingView();
                showErrorView();
                e.printStackTrace();
            }

            @Override
            public void onNext(MyFriends myFriends) {
                if ("1".equals(myFriends.getState())) {

                    mAdapter.addDate(myFriends.getLoginList());


                } else {
                    showTest(myFriends.getState());
                }

            }
        });
        addSubscription(sbGetCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);

    }

/*
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tvRight:
                name = mInputSearchQuery.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    showTest("请输入需要查询的数据");
                    return;
                }

                loadDataSub(mCurrenPager);


                break;

        }
    }*/
private void setAdapter() {
    mAdapter = new CommonAdapter<MyFriends.LoginListBean>(getApplicationContext(), R.layout.item_lv_myfriend, mLtObject) {
        @Override
        public void convert(ViewHolder holder, MyFriends.LoginListBean myFriends) {

            holder.setImageURL(R.id.ivMyFriendHead, myFriends.getHeadimg(), false);
            holder.setText(R.id.ivMyFriendName, myFriends.getUser_name());


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
    };
    recyclerAdapterWithHF = new RecyclerAdapterWithHF(mAdapter);
    recyclerAdapterWithHF.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
        @Override
        public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {
            Intent intent = new Intent(SearchActivity.this, SeePersonalInfoIsFriendActivity.class);
            intent.putExtra("id", mLtObject.get(position).getId() + "");
            intent.putExtra("isShowDelete", false);
            startActivity(intent);
            finish();
        }
    });
    mLvSearch.setAdapter(recyclerAdapterWithHF);
}

    @Override
    public int createSuccessView() {
        return R.layout.activity_search;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

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
    public void afterTextChanged(Editable s) {



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
            List<UserInfoRong> userInfoRongs = mgr.queryLevels();
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

                    mLtObject.clear();
                    if(mAdapter != null)
                        mAdapter.addDate(mFliteLtObject);


                } else {
                    mLtObject.clear();
                    List<UserInfoRong> userInfoRongs = mgr.queryLevels();
                    if (userInfoRongs != null && userInfoRongs.size() > 0) {
                        for (UserInfoRong userInfoRong : userInfoRongs) {

                            mLtObject.add(new MyFriends.LoginListBean(userInfoRong.id, userInfoRong.user_name,
                                    userInfoRong.headimg, userInfoRong.agent));
                        }


                        setAdapter();
                    }
                }
            }


        }
    }

    private void loadData() {

        mLoadingAlertDialog.show();

        int id = (int) SPUtils.get(SPUtils.KEY_GET_ID, 0);


        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().myLevelMember((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),0, id, 1, 0)).subscribe(new Subscriber<MyLever>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                mLoadingAlertDialog.dismiss();
                mPcSearchFlyt.refreshComplete();
            }

            @Override
            public void onError(Throwable e) {
                showErrorView();
                showTest(mNetWorkError);
                e.printStackTrace();
                mLoadingAlertDialog.dismiss();
                mPcSearchFlyt.refreshComplete();
            }

            @Override
            public void onNext(MyLever levelMembers) {
                if ("1".equals(levelMembers.getState())) {
                    mgr.deleteTableByDBNameAllAndLever();//删除表里的数据
                    Log.e("error", "- 删除--");
                    ArrayList<UserInfoRong> userInfoAddRongs = new ArrayList<UserInfoRong>();
                    for (MyLever.LoginListBean friend : levelMembers.getLoginList()) {
                        mLtObject.add(new MyFriends.LoginListBean(friend.getId(), friend.getUser_name(),
                                friend.getHeadimg(), friend.getAgent()));

                        setAdapter();
                        userInfoAddRongs.add(new UserInfoRong(friend.getId(), friend.getUser_name(), friend.getHeadimg(), friend.getAgent()));

                    }
                    List<UserInfoRong> friens = mgr.queryFriends();

                    mgr.addLevels(userInfoAddRongs);//添加到我的好友表里

                    mgr.add(friens);//将我的下级数据放入所有好友的表中
                    userInfoAddRongs.add(new UserInfoRong(1000, "拇指推官方账号", "http://www.muzhitui.cn/song/wx/img/logo.jpg", "2"));
                    mgr.add(userInfoAddRongs);//添加到所有好友的表里


                } else {
                    showErrorView();
                    showTest(mServerEror);
                }
            }
        });
        addSubscription(sbMyAccount);
    }
}
