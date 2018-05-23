package com.nevermore.muzhitui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.event.refrush;
import com.nevermore.muzhitui.module.bean.Code;
import com.nevermore.muzhitui.module.bean.NewFriend;

import com.nevermore.muzhitui.module.network.WorkService;
import com.nevermore.muzhitui.module.sqllite.DBManager;
import com.nevermore.muzhitui.module.sqllite.UserInfoRong;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import base.BaseActivityTwoV;
import base.ImageUtil;
import base.RecyclerBaseAdapter;

import base.SPUtils;
import base.UIUtils;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Simone on 2016/12/21.
 */

public class NewFriendsActivity extends BaseActivityTwoV implements View.OnClickListener {


    @BindView(R.id.flNewfriendsPhone)
    FrameLayout mFlNewfriendsPhone;
    @BindView(R.id.lvNewFriend)
    RecyclerView mLvNewFriend;
    @BindView(R.id.pcNewFriendsFlyt)
    PtrClassicFrameLayout mPcNewFriendsFlyt;

    private RecyclerAdapterWithHF recyclerAdapterWithHF;
    private RecyclerBaseAdapter mAdapter;
    private int mCurrenPager = 1;
    List<NewFriend.LoginListBean> mLtObject = new ArrayList<>();
    private int mAllPages;
    private LoadingAlertDialog mLoadingAlertDialog;
    private DBManager mgr;
    private int mPostiton = 0;
    private android.app.AlertDialog alertDialog;
    @Override
    public void init() {
        showBack();
        TextView tv = showRight();
        tv.setOnClickListener(this);
        tv.setText("添加好友");
        setMyTitle("新的好友");
        try {


            tv.setTextColor(getResources().getColor(R.color.gray_white));
            mLoadingAlertDialog = new LoadingAlertDialog(this);

            //初始化DBManager
            mgr = new DBManager(this);

            mAdapter = new RecyclerBaseAdapter<NewFriend.LoginListBean>(NewFriendsActivity.this, mLtObject, R.layout.item_lv_newfriend, new RecyclerBaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    switch (view.getId()) {

                        case R.id.tvDelete:

                            delete(mLtObject.get(position));
                            break;
                        case R.id.rlyt:
                            mPostiton = position;
                            if (mLtObject.get(position).getStatus() == 1) {
                                Intent intent = new Intent(NewFriendsActivity.this, SeePersonalInfoIsFriendActivity.class);


                                intent.putExtra("id", mLtObject.get(position).getId() + "");
                                intent.putExtra("friend_state", mLtObject.get(position).getStatus() + "");
                                startActivity(intent);

                            } else {
                                Intent intent = new Intent(NewFriendsActivity.this, SeePersonalInfoActivity.class);


                                intent.putExtra("id", mLtObject.get(position).getId() + "");
                                intent.putExtra("friend_state", mLtObject.get(position).getStatus() + "");
                                startActivity(intent);

                            }

                            recyclerAdapterWithHF.notifyItemChanged(position);

                            break;
                        default:
                            break;
                    }
                }
            }) {
                @Override
                public void fillData(ViewHolder viewHolder, final NewFriend.LoginListBean data, final int position) {
                    ImageLoader.getInstance().displayImage(data.getHeadimg(), (ImageView) viewHolder.getView(R.id.ivNewFriendHead), ImageUtil.getInstance().getBaseDisplayOption());


                    viewHolder.setText(R.id.tvNewFriendName, data.getUser_name());
                    if (data.getMessage() != null) {
                        viewHolder.setText(R.id.tvNewFriendInfo, data.getMessage());
                    }

                    viewHolder.setIsRecyclable(false);
                    //1 好友, 2 请求添加, 3 请求被添加, 4 请求被拒绝, 5 我被对方删除
                    TextView tvNewFAdd = viewHolder.getView(R.id.ivNewFAdd);
                    TextView tvNewFRequestAdd = viewHolder.getView(R.id.ivNewFRequestAdd);
                    TextView tvNewFByRequestAdd = viewHolder.getView(R.id.ivNewFByRequestAdd);
                    LinearLayout rlyt=viewHolder.getView(R.id.rlyt);
                   rlyt.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                           alertDialog = UIUtils.getAlertDialog(NewFriendsActivity.this, null,"您是要删除 "+mLtObject.get(position).getUser_name()+" 这个列表吗?", "取消", "确定", 0, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();


                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            delete(mLtObject.get(position));
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                            return true;
                        }
                    });
                    if (data.getStatus() == 1) {

                        tvNewFAdd.setVisibility(View.VISIBLE);
                        tvNewFRequestAdd.setVisibility(View.GONE);
                        tvNewFByRequestAdd.setVisibility(View.GONE);


                    } else if (data.getStatus() == 2) {

                        tvNewFAdd.setVisibility(View.GONE);
                        tvNewFRequestAdd.setVisibility(View.VISIBLE);
                        tvNewFByRequestAdd.setVisibility(View.GONE);


                    } else if (data.getStatus() == 3) {

                        tvNewFAdd.setVisibility(View.GONE);
                        tvNewFRequestAdd.setVisibility(View.GONE);
                        tvNewFByRequestAdd.setVisibility(View.VISIBLE);


                    } else if (data.getStatus() == 4) {

                        tvNewFAdd.setVisibility(View.GONE);
                        tvNewFRequestAdd.setVisibility(View.VISIBLE);
                        tvNewFByRequestAdd.setVisibility(View.GONE);
                        tvNewFRequestAdd.setText("请求被拒绝");
                    } else if (data.getStatus() == 5) {

                        tvNewFAdd.setVisibility(View.GONE);
                        tvNewFRequestAdd.setVisibility(View.VISIBLE);
                        tvNewFByRequestAdd.setVisibility(View.GONE);
                        tvNewFRequestAdd.setText("被对方删除");

                    }
                    tvNewFByRequestAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPostiton = position;
                            loadDataresponse(1, data);//status    1同意   4拒绝  ，
                        }
                    });

                }

                @Override
                public boolean onItemMove(int fromPosition,final int toPosition) {


                    return false;

                }

            };

            mAdapter.setIsCela(true);
            recyclerAdapterWithHF = new RecyclerAdapterWithHF(mAdapter);
            mLvNewFriend.setAdapter(recyclerAdapterWithHF);
            mPcNewFriendsFlyt.setPtrHandler(new PtrHandler() {
                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    return false;
                }

                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {

                }
            });

            mPcNewFriendsFlyt.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void loadMore() {
                    loadData(++mCurrenPager);
                }
            });
            loadData(mCurrenPager);
            showLoadingView();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventEMMessage(refrush refrush) {

        finish();
       /* if (refrush.getState()==2){


            mLtObject.get(mPostiton).setStatus(1);

            mAdapter.notifyDataSetChanged();
            recyclerAdapterWithHF.notifyItemChanged(mPostiton);

        }*/


    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //应用的最后一个Activity关闭时应释放DB
        mgr.closeDB();
        EventBus.getDefault().unregister(this);

    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_newfriends;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    private void loadData(int currentPager) {

        mLoadingAlertDialog.show();

        Subscription sbGetCode = wrapObserverWithHttp(WorkService.getWorkService().myFrindNew((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),currentPager)).subscribe(new Subscriber<NewFriend>() {
            @Override
            public void onCompleted() {
                mLoadingAlertDialog.dismiss();
                removeLoadingView();
                mPcNewFriendsFlyt.setLoadMoreEnable(true);
                mPcNewFriendsFlyt.loadMoreComplete(true);
                // mPcNewFriendsFlyt.refreshComplete();
                if (mCurrenPager == mAllPages) {
                    mPcNewFriendsFlyt.setLoadMoreEnable(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                mLoadingAlertDialog.dismiss();
                removeLoadingView();
                showErrorView();
                e.printStackTrace();
            }

            @Override
            public void onNext(NewFriend newFriend) {
                if ("1".equals(newFriend.getState())) {
                    mLtObject.addAll(newFriend.getLoginList());
                    mAdapter.notifyDataSetChanged();
                    mAllPages = newFriend.getAllPages();

                } else {
                    showTest(newFriend.getMsg());
                }

            }
        });
        addSubscription(sbGetCode);
    }

    @OnClick({R.id.flNewfriendsPhone})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tvRight:
                baseStartActivity(AddFriendsActivity.class);
                break;
            case R.id.flNewfriendsPhone:
                baseStartActivity(PhoneContactsActivity.class);
                break;
        }
    }

    private void delete(final NewFriend.LoginListBean bean) {
        mLoadingAlertDialog.show();
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().deleteFriendList((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),bean.getId())).subscribe(new Subscriber<Code>() {
            @Override
            public void onCompleted() {

                mLoadingAlertDialog.dismiss();

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mLoadingAlertDialog.dismiss();
                showTest(mNetWorkError);
            }

            @Override
            public void onNext(Code code) {
                if (code.getState().equals("1")) {//
                    showTest("删除成功");
                    mLtObject.remove(bean);
                    mAdapter.notifyDataSetChanged();
                } else {
                    showTest(code.getMsg());
                }
            }
        });
        addSubscription(sbMyAccount);
    }

    private void loadDataresponse(int state, final NewFriend.LoginListBean data) {

        mLoadingAlertDialog.show();

        Subscription sbGetCode = wrapObserverWithHttp(WorkService.getWorkService().responsesFriend((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),state, data.getId(), null, null)).subscribe(new Subscriber<Code>() {
            @Override
            public void onCompleted() {

                mLoadingAlertDialog.dismiss();
                removeLoadingView();

            }

            @Override
            public void onError(Throwable e) {

                mLoadingAlertDialog.dismiss();
                removeLoadingView();
                showErrorView();
                e.printStackTrace();
            }

            @Override
            public void onNext(Code code) {
                if ("1".equals(code.getState())) {

                    ArrayList<UserInfoRong> userInfoAddRongs = new ArrayList<UserInfoRong>();
                    userInfoAddRongs.add(new UserInfoRong(data.getId(), data.getUser_name(), data.getHeadimg(), data.getAgent()));

                    mgr.addFriend(userInfoAddRongs);
                    mgr.add(userInfoAddRongs);
                    //刷新刚添加的好友信息
                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(data.getId() + "",
                            data.getUser_name() == null ? null :data.getUser_name(), data.getHeadimg() == null ? null : Uri.parse(data.getHeadimg())));

                    mLtObject.get(mPostiton).setStatus(1);
                    showTest("添加好友成功，你们现在可以聊天啦");
                    sendTextMessage("我们已经是好友啦,可以开始聊天了~",data.getId()+"");
                    mAdapter.notifyDataSetChanged();
                    recyclerAdapterWithHF.notifyItemChanged(mPostiton);
                } else {
                    showTest(code.getMsg());
                }

            }
        });
        addSubscription(sbGetCode);
    }



    // 发送文本消息。
    private void sendTextMessage(String message,String id) {

        TextMessage txtMsg = TextMessage.obtain(message);


        RongIM.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, id, txtMsg, message, new Date(System.currentTimeMillis()).toString(), new RongIMClient.SendMessageCallback() {
            @Override
            public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
              //  showTest("发送失败");
            }

            @Override
            public void onSuccess(Integer integer) {
               // showTest("发送成功");
            }
        });


    }
}