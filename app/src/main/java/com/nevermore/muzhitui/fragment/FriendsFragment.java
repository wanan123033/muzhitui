package com.nevermore.muzhitui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.kevin.wraprecyclerview.WrapAdapter;
import com.nevermore.muzhitui.EventBusContanct;
import com.nevermore.muzhitui.MessageActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.CityListActivity;
import com.nevermore.muzhitui.activity.FriendVerifcationActivity;
import com.nevermore.muzhitui.activity.MyFriendsActivity;
import com.nevermore.muzhitui.activity.MySubordinatesActivity;
import com.nevermore.muzhitui.activity.NewFriendsActivity;
import com.nevermore.muzhitui.activity.SeePersonalInfoActivity;
import com.nevermore.muzhitui.activity.SeePersonalInfoIsFriendActivity;
import com.nevermore.muzhitui.activity.rongyun.MztRongContext;
import com.nevermore.muzhitui.activity.rongyun.broadcast.BroadcastManager;
import com.nevermore.muzhitui.event.CityEvent;
import com.nevermore.muzhitui.event.EventRedDot;
import com.nevermore.muzhitui.module.bean.Contacts;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.BaseFragment;
import base.SPUtils;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
import base.recycler.recyclerview.OnItemClickListener;
import base.view.LoadingAlertDialog;
import base.view.refresh.SmartRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Administrator on 2018/1/29.
 */

public class FriendsFragment extends BaseFragment {
    @BindView(R.id.lvContacts)
    RecyclerView lvContacts;
    @BindView(R.id.str_layout)
    SmartRefreshLayout str_layout;
    @BindView(R.id.ivContactsPosition)
    TextView ivContactsPosition;

    private CommonAdapter mAdapter;
    private List<Contacts.LoginListBean> mLtObject;
    private int mCurrenPager;
    private LoadingAlertDialog mLoadingAlertDialog;
    private WrapAdapter adapter;
    private static String currentcity;
    private LocationClient mLocationClient;
    public BDLocationListener myListener = new MyLocationListener();

    @Override
    public int createSuccessView() {
        return R.layout.fragment_friends;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mLocationClient = new LocationClient(getActivity()); // 声明LocationClient类
        mLocationClient.registerLocationListener(myListener); // 注册监听函数
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLtObject = new ArrayList<>();
        mLoadingAlertDialog = new LoadingAlertDialog(getActivity());
        mAdapter = new CommonAdapter<Contacts.LoginListBean>(getActivity(), R.layout.item_lv_contacts, mLtObject) {
            @Override
            public void convert(ViewHolder holder, final Contacts.LoginListBean contacts) {
                ImageLoader.getInstance().displayImage(contacts.getHeadimg(), (ImageView) holder.getView(R.id.ivContactsHead));
                if (!contacts.getAgent().equals("未加入会员")) {
                    if (contacts.getAgent().equals("年费会员")) {
                        holder.setVisible(R.id.tvCntactsnianfei, true);
                        holder.setVisible(R.id.tvCntactszhongshen, false);
                    } else {
                        holder.setVisible(R.id.tvCntactsnianfei, false);
                        holder.setVisible(R.id.tvCntactszhongshen, true);
                    }
                    String username = contacts.getUser_name();
                    if(username.length() > 12){
                        username = username.substring(0,12);
                    }
                    holder.setText(R.id.tvCntactsName, username+ "  •");
                }else{
                    String username = contacts.getUser_name();
                    if(username.length() > 12){
                        username = username.substring(0,12);
                    }
                    holder.setText(R.id.tvCntactsName, username);
                    holder.setVisible(R.id.tvCntactsnianfei, false);
                    holder.setVisible(R.id.tvCntactszhongshen, false);
                }

                if (contacts.getStatus() == 0) {//添加按钮
                    holder.setVisible(R.id.ivContactsAdd, true);
                    holder.setVisible(R.id.ivContactsInAdd, false);
                    holder.setVisible(R.id.ivContactsWaite, false);
                } else if (contacts.getStatus() == 1) {//已添加
                    holder.setVisible(R.id.ivContactsAdd, false);
                    holder.setVisible(R.id.ivContactsInAdd, true);
                    holder.setVisible(R.id.ivContactsWaite, false);
                } else if (contacts.getStatus() == 2) {//等待验证
                    holder.setVisible(R.id.ivContactsAdd, false);
                    holder.setVisible(R.id.ivContactsInAdd, false);
                    holder.setVisible(R.id.ivContactsWaite, true);
                }
                String country="未知",province="未知",city="未知";
                if (!TextUtils.isEmpty(contacts.getWx_country())){
                    country=contacts.getWx_country();
                }
                if (!TextUtils.isEmpty(contacts.getWx_province())){
                    province=contacts.getWx_province();
                }
                if (!TextUtils.isEmpty(contacts.getWx_city())){
                    city=contacts.getWx_city();
                }

                holder.setText(R.id.tvCntactsAddress, country + " • " + province + " • " + city);

                holder.setOnClickListener(R.id.ivContactsAdd, new View.OnClickListener() {      //人脉列表上的添加按钮
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), FriendVerifcationActivity.class);
                        intent.putExtra("id", contacts.getId());
                        startActivity(intent);
                    }
                });
            }
        };


        adapter = new WrapAdapter(mAdapter);
        View headview = LayoutInflater.from(getActivity()).inflate(R.layout.head_fragment_friends,null,false);
        initHeadView(headview);
        adapter.addHeaderView(headview);
        lvContacts.setAdapter(adapter);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                Log.e("LOGIN",mLtObject.get(position).toString());
                Log.e("id:", mLtObject.get(position).getId() + "");
                Log.e("id  status:", mLtObject.get(position).getStatus() + "");
                if (mLtObject.get(position - 1).getStatus() == 1) {
                    Intent intent = new Intent(getActivity(), SeePersonalInfoIsFriendActivity.class);
                    intent.putExtra("id", mLtObject.get(position - 1).getId() + "");
                    startActivity(intent);
                } else if (mLtObject.get(position - 1).getStatus() == 0) {
                    Intent intent = new Intent(getActivity(), SeePersonalInfoActivity.class);
                    intent.putExtra("id", mLtObject.get(position - 1).getId() + "");
                    intent.putExtra("friend_state", 11 + "");//11表示从人脉跳转到查看信息页面
                    startActivity(intent);
                } else if (mLtObject.get(position - 1).getStatus() == 2) {
                    Intent intent = new Intent(getActivity(), SeePersonalInfoActivity.class);
                    intent.putExtra("id", mLtObject.get(position - 1).getId() + "");
                    intent.putExtra("friend_state", 12 + "");//11表示从人脉跳转到查看信息页面
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), SeePersonalInfoActivity.class);
                    intent.putExtra("id", mLtObject.get(position - 1).getId() + "");
                    intent.putExtra("friend_state", 11 + "");//11表示从人脉跳转到查看信息页面
                    startActivity(intent);
                }
                mAdapter.notifyItemChanged(position);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                return false;
            }
        });
        str_layout.setOnRefreshListener(new SmartRefreshLayout.onRefreshListener() {
            @Override
            public void onRefresh() {
                mLtObject.clear();
                mCurrenPager=1;
                loadData(mCurrenPager, (String) SPUtils.get(SPUtils.KEY_WX_CITY, ""));
            }

            @Override
            public void onLoadMore() {
                loadData(++mCurrenPager, (String) SPUtils.get(SPUtils.KEY_WX_CITY, ""));
            }
        });
        mCurrenPager=1;
        loadData(mCurrenPager, (String) SPUtils.get(SPUtils.KEY_WX_CITY, ""));

        initLocation();
        BroadcastManager.getInstance(getActivity()).addAction(MztRongContext.UPDATE_RED_DOT, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String command = intent.getAction();
                if (!TextUtils.isEmpty(command)) {

                }
            }
        });



        if ( !TextUtils.isEmpty((String) SPUtils.get(SPUtils.KEY_WX_CITY, ""))){
            ivContactsPosition.setText((String) SPUtils.get(SPUtils.KEY_WX_CITY, ""));
            // Log.e("城市：","11111");
        }else{
            SPUtils.put(SPUtils.KEY_WX_CITY, "全国");
            ivContactsPosition.setText("全国");
            // Log.e("城市：","2222");
        }
    }

    @OnClick(R.id.ivContactsPosition)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ivContactsPosition:
                Intent intentCity = new Intent(getActivity(), CityListActivity.class);
                intentCity.putExtra("cityName", (String) SPUtils.get(SPUtils.KEY_WX_CITY, ""));
                intentCity.putExtra("currentCity", currentcity);
                getActivity().startActivity(intentCity);
                break;
        }
    }
    /**
     * 设置定位参数
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
        mLocationClient.start();
        mLocationClient.requestLocation();// 开始请求位置
    }

    private void loadData(int mCurrenPager, String wx_city) {
        mLoadingAlertDialog.show();
        if (wx_city.contains("#")||wx_city==null||wx_city.contains("全国")){
            wx_city="";
        }
        Subscription sbGetCode = wrapObserverWithHttp(WorkService.getWorkService().allConnection((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),mCurrenPager, wx_city)).subscribe(new Subscriber<Contacts>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                str_layout.stopRefresh();
                str_layout.stopLoadMore();
//                removeErrorView();
//                mPcContactsFlyt.setLoadMoreEnable(true);
//                mPcContactsFlyt.loadMoreComplete(true);
//                mPcContactsFlyt.refreshComplete();//设置刷新完成隐藏到刷新页面
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {

                mLoadingAlertDialog.dismiss();
                removeLoadingView();
                showErrorView();
                e.printStackTrace();
                showTest(mNetWorkError);
            }

            @Override
            public void onNext(Contacts contacts) {
                if ("1".equals(contacts.getState())) {
                    mAdapter.addDate(contacts.getLoginList());
                    adapter.notifyDataSetChanged();
                } else {
                    showTest("人脉信息请求失败，请重新登录");
                }
            }

        });
        ((BaseActivityTwoV) getActivity()).addSubscription(sbGetCode);
    }

    private void initHeadView(View headview) {
        new ViewIndes((BaseActivityTwoV)getActivity(),headview);
    }

    public static class ViewIndes implements View.OnClickListener{
        private BaseActivityTwoV activity;
        TextView tvContactsNewFriend;
        TextView tvContactsLowerLevel;
        TextView pointView;

        public ViewIndes(BaseActivityTwoV activity,View headviews) {
            this.activity = activity;
            EventBus.getDefault().register(this);
            tvContactsNewFriend = headviews.findViewById(R.id.tvContactsNewFriend);
            tvContactsLowerLevel = headviews.findViewById(R.id.tvContactsLowerLevel);
            pointView = headviews.findViewById(R.id.seal_numgh);

            headviews.findViewById(R.id.flContactsNewfriends).setOnClickListener(this);
            headviews.findViewById(R.id.flContactsMyfriends).setOnClickListener(this);
            headviews.findViewById(R.id.ll_contact).setOnClickListener(this);
            headviews.findViewById(R.id.flContactsLowerLevel).setOnClickListener(this);
        }

        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.flContactsNewfriends:{
                    RongIMClient.getInstance().clearConversations(Conversation.ConversationType.SYSTEM);  //清空好友请求
                    EventBus.getDefault().post(new EventRedDot()); //更新人脉消息
                    tvContactsNewFriend.setVisibility(View.GONE);
                    Intent intentNewF = new Intent(activity, NewFriendsActivity.class);
                    activity.startActivity(intentNewF);
                }
                break;
                case R.id.flContactsMyfriends:{
                    Intent intent = new Intent(activity, MyFriendsActivity.class);
                    intent.putExtra("fragment", "contactsfragment");
                    activity.startActivity(intent);
                }
                break;
                case R.id.flContactsLowerLevel:{
                    SPUtils.remove(SPUtils.XIAJI_NUM);
                    tvContactsLowerLevel.setVisibility(View.GONE);
                    EventBus.getDefault().post(new EventRedDot()); //更新人脉消息
                    activity.baseStartActivity(MySubordinatesActivity.class);
                }
                break;
                case R.id.ll_contact:
                    activity.baseStartActivity(MessageActivity.class);
                    break;
            }
        }



        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onEventEMMessage(EventBusContanct contanct) {
            if (contanct.getFlag() == EventBusContanct.REFRSH_FRIENDS){ //好友请求
                int addFriendNum = RongIMClient.getInstance().getUnreadCount(Conversation.ConversationType.SYSTEM);
                if(addFriendNum > 0){
                    tvContactsNewFriend.setText(addFriendNum+"");
                    tvContactsNewFriend.setVisibility(View.VISIBLE);
                }else {
                    tvContactsNewFriend.setVisibility(View.GONE);
                }
            }else if(contanct.getFlag() == EventBusContanct.REFRSH_CHAT){  //聊天信息
                int messageNum = RongIMClient.getInstance().getUnreadCount(Conversation.ConversationType.PRIVATE);
                if(messageNum > 0){
                    pointView.setVisibility(View.VISIBLE);
                    pointView.setText(String.valueOf(messageNum));
                }else {
                    pointView.setVisibility(View.GONE);
                }
            }else if(contanct.getFlag() == EventBusContanct.REFRESH_XIAJI){  //我的下级
                int xiajiNum = (int) SPUtils.get(SPUtils.XIAJI_NUM,0);
                if(xiajiNum > 0){
                    tvContactsLowerLevel.setText(xiajiNum+"");
                    tvContactsLowerLevel.setVisibility(View.VISIBLE);
                }else {
                    tvContactsLowerLevel.setVisibility(View.GONE);
                }
            }
            EventBus.getDefault().post(new EventRedDot()); //更新人脉消息


        }
    }
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location != null) {
                Log.e("location", location.getAddrStr() + location.getCity());

                String loc = location.getCity().replace("市", "");
                //ivContactsPosition.setText(loc);
                //SPUtils.put(SPUtils.KEY_CURRENT_CITY, loc);
                currentcity = loc;
                mLocationClient.stop();
            } else {
                ivContactsPosition.setText("无法定位");
                return;
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventEMMessage(CityEvent cityEvent) {
        if(cityEvent.getType() == 0) {
            String loc = cityEvent.getCityName().replace("市", "");
            if (loc.contains("#") || loc == null) {
                loc = "全国";
            }
            ivContactsPosition.setText(loc);
            SPUtils.put(SPUtils.KEY_WX_CITY, loc);
            mCurrenPager = 1;
            mLtObject.clear();
            loadData(mCurrenPager, loc);
        }
    }
}
