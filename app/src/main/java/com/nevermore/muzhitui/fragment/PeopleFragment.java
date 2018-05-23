package com.nevermore.muzhitui.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chanven.lib.cptr.PtrClassicDefaultHeader;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.nevermore.muzhitui.EventBusContanct;
import com.nevermore.muzhitui.MessageActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.CityListActivity;
import com.nevermore.muzhitui.activity.FriendVerifcationActivity;
import com.nevermore.muzhitui.activity.MyFriendsActivity;
import com.nevermore.muzhitui.activity.MySubordinatesActivity;
import com.nevermore.muzhitui.activity.NewFriendsActivity;
import com.nevermore.muzhitui.activity.QunExchangeActivity;
import com.nevermore.muzhitui.activity.SeePersonalInfoActivity;
import com.nevermore.muzhitui.activity.SeePersonalInfoIsFriendActivity;
import com.nevermore.muzhitui.activity.WxAddFriendActivity;
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
import base.view.LoadingAlertDialog;
import base.view.ScrollView;
import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Administrator on 2018/1/8.
 */

public class PeopleFragment extends BaseFragment {
    @BindView(R.id.ivContactsPosition)
    TextView ivContactsPosition;
    @BindView(R.id.lvContacts)
    RecyclerView mLvContacts;
    @BindView(R.id.pcContactsFlyt)
    PtrClassicFrameLayout mPcContactsFlyt;
    @BindView(R.id.scrollview)
    ScrollView scrollview;
    @BindView(R.id.tvContactsNewFriend)
    TextView tvContactsNewFriend;
    @BindView(R.id.tvContactsLowerLevel)
    TextView tvContactsLowerLevel;
    @BindView(R.id.seal_numgh)
    TextView pointView;

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private CommonAdapter mAdapter;
    private RecyclerAdapterWithHF recyclerAdapterWithHF;
    private String currentcity;
    private List<Contacts.LoginListBean> mLtObject = new ArrayList<>();

    private Conversation.ConversationType[] mConversationsTypes;

    private LoadingAlertDialog mLoadingAlertDialog;
    private int mCurrenPager = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mLocationClient = new LocationClient(getActivity()); // 声明LocationClient类
        mLocationClient.registerLocationListener(myListener); // 注册监听函数
    }

    @Override
    public int createSuccessView() {
        return R.layout.fragment_people;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLvContacts.setHasFixedSize(true);
        mLvContacts.setNestedScrollingEnabled(false);
        mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP,
                Conversation.ConversationType.PUBLIC_SERVICE,
                Conversation.ConversationType.APP_PUBLIC_SERVICE,
                Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.DISCUSSION};

        if (Build.VERSION.SDK_INT >= 23) {
            RongIM.getInstance().setRequestPermissionListener(new RongIM.RequestPermissionsListener() {
                @Override
                public void onPermissionRequest(String[] permissions, final int requestCode) {
                    for (final String permission : permissions) {
                        if (shouldShowRequestPermissionRationale(permission)) {
                            requestPermissions(new String[]{permission}, requestCode);
                        } else {
                            int isPermissionGranted =getActivity().checkSelfPermission(permission);
                            if (isPermissionGranted != PackageManager.PERMISSION_GRANTED) {
                                new AlertDialog.Builder(getActivity())
                                        .setMessage("你需要在设置里打开以下权限:" + permission)
                                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                requestPermissions(new String[]{permission}, requestCode);
                                            }
                                        })
                                        .setNegativeButton("取消", null)
                                        .create().show();
                            }
                            return;
                        }
                    }
                }
            });

        }
        initLocation();
        BroadcastManager.getInstance(getActivity()).addAction(MztRongContext.UPDATE_RED_DOT, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String command = intent.getAction();
                if (!TextUtils.isEmpty(command)) {

                }
            }
        });
        mLoadingAlertDialog = new LoadingAlertDialog(getActivity());


        if ( !TextUtils.isEmpty((String) SPUtils.get(SPUtils.KEY_WX_CITY, ""))){
            ivContactsPosition.setText((String) SPUtils.get(SPUtils.KEY_WX_CITY, ""));
            // Log.e("城市：","11111");
        }else{
            SPUtils.put(SPUtils.KEY_WX_CITY, "全国");
            ivContactsPosition.setText("全国");
            // Log.e("城市：","2222");
        }

        Log.e("城市：",SPUtils.get(SPUtils.KEY_WX_CITY, "")+"");
        loadData(mCurrenPager, (String) SPUtils.get(SPUtils.KEY_WX_CITY, ""));

        setAdapter();//设置数据


        recyclerAdapterWithHF = new RecyclerAdapterWithHF(mAdapter);
        recyclerAdapterWithHF.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {  //人脉列表的单项点击事件
                Log.e("LOGIN",mLtObject.get(position).toString());
                Log.e("id:", mLtObject.get(position).getId() + "");
                Log.e("id  status:", mLtObject.get(position).getStatus() + "");
                if (mLtObject.get(position).getStatus() == 1) {
                    Intent intent = new Intent(getActivity(), SeePersonalInfoIsFriendActivity.class);
                    intent.putExtra("id", mLtObject.get(position).getId() + "");
                    startActivity(intent);
                } else if (mLtObject.get(position).getStatus() == 0) {
                    Intent intent = new Intent(getActivity(), SeePersonalInfoActivity.class);
                    intent.putExtra("id", mLtObject.get(position).getId() + "");
                    intent.putExtra("friend_state", 11 + "");//11表示从人脉跳转到查看信息页面
                    startActivity(intent);
                } else if (mLtObject.get(position).getStatus() == 2) {
                    Intent intent = new Intent(getActivity(), SeePersonalInfoActivity.class);
                    intent.putExtra("id", mLtObject.get(position).getId() + "");
                    intent.putExtra("friend_state", 12 + "");//11表示从人脉跳转到查看信息页面
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), SeePersonalInfoActivity.class);
                    intent.putExtra("id", mLtObject.get(position).getId() + "");
                    intent.putExtra("friend_state", 11 + "");//11表示从人脉跳转到查看信息页面
                    startActivity(intent);
                }
                recyclerAdapterWithHF.notifyItemChanged(position);

            }
        });
        mLvContacts.setAdapter(recyclerAdapterWithHF);

        mPcContactsFlyt.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mLtObject.clear();
                mCurrenPager=1;

                loadData(mCurrenPager, (String) SPUtils.get(SPUtils.KEY_WX_CITY, ""));
            }

        });




        mPcContactsFlyt.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                loadData(++mCurrenPager, (String) SPUtils.get(SPUtils.KEY_WX_CITY, ""));
            }
        });

//        mPcContactsFlyt.setLastUpdateTimeRelateObject(this);
        scrollview.setOnScrollListener(new ScrollView.OnScrollListener() {
            @Override
            public void scrollTop() {
                mLtObject.clear();
                mCurrenPager=1;
                loadData(mCurrenPager, (String) SPUtils.get(SPUtils.KEY_WX_CITY, ""));
            }

            @Override
            public void scrollDown() {
                loadData(++mCurrenPager, (String) SPUtils.get(SPUtils.KEY_WX_CITY, ""));
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopListener();//停止监听


    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        try {
            BroadcastManager.getInstance(getActivity()).destroy(MztRongContext.UPDATE_RED_DOT);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    public void stopListener() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();// 关闭定位SDK
            mLocationClient = null;
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

    private void setAdapter(){
        mAdapter = new CommonAdapter<Contacts.LoginListBean>(getActivity(), R.layout.item_lv_contacts, mLtObject) {
            @Override
            public void convert(final ViewHolder holder, final Contacts.LoginListBean contacts) {
//                holder.setImageURL(R.id.ivContactsHead, contacts.getHeadimg(), false);
                ImageLoader.getInstance().displayImage(contacts.getHeadimg(), (ImageView) holder.getView(R.id.ivContactsHead));
                Log.e("head:",contacts.getId()+"  "+contacts.getUser_name()+"  "+contacts.getHeadimg());

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
    }
    private void loadData(int currentPager, String wx_city) {

        mLoadingAlertDialog.show();
        if (wx_city.contains("#")||wx_city==null||wx_city.contains("全国")){
            wx_city="";
        }
        Subscription sbGetCode = wrapObserverWithHttp(WorkService.getWorkService().allConnection((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),currentPager, wx_city)).subscribe(new Subscriber<Contacts>() {
            @Override
            public void onCompleted() {


                removeLoadingView();
//                removeErrorView();
//                mPcContactsFlyt.setLoadMoreEnable(true);
//                mPcContactsFlyt.loadMoreComplete(true);
                mPcContactsFlyt.refreshComplete();//设置刷新完成隐藏到刷新页面
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
                } else {
                    showTest("人脉信息请求失败，请重新登录");
                }

            }

        });
        ((BaseActivityTwoV) getActivity()).addSubscription(sbGetCode);
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
    @OnClick({R.id.ivContactsPosition,R.id.flContactsNewfriends,R.id.flContactsMyfriends,R.id.ll_contact,R.id.flContactsLowerLevel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivContactsPosition:{
                Intent intentCity = new Intent(getActivity(), CityListActivity.class);
                intentCity.putExtra("cityName", (String) SPUtils.get(SPUtils.KEY_WX_CITY, ""));
                intentCity.putExtra("currentCity", currentcity);
                startActivity(intentCity);
            }
                break;
            case R.id.flContactsNewfriends:{
                RongIMClient.getInstance().clearConversations(Conversation.ConversationType.SYSTEM);  //清空好友请求
                EventBus.getDefault().post(new EventRedDot()); //更新人脉消息
                tvContactsNewFriend.setVisibility(View.GONE);
                Intent intentNewF = new Intent(getActivity(), NewFriendsActivity.class);
                startActivity(intentNewF);
            }
                break;
            case R.id.flContactsMyfriends:{
                Intent intent = new Intent(getActivity(), MyFriendsActivity.class);
                intent.putExtra("fragment", "contactsfragment");
                startActivity(intent);
            }
                break;
            case R.id.flContactsLowerLevel:{
                SPUtils.remove(SPUtils.XIAJI_NUM);
                tvContactsLowerLevel.setVisibility(View.GONE);
                EventBus.getDefault().post(new EventRedDot()); //更新人脉消息
                baseStartActivity(MySubordinatesActivity.class);
            }
                break;
            case R.id.ll_contact:
                baseStartActivity(MessageActivity.class);
                break;
        }
    }


}
