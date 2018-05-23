package com.nevermore.muzhitui.activity;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lljjcoder.citypickerview.widget.CityPicker;
import com.nevermore.muzhitui.MainActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.rongyun.MztRongContext;
import com.nevermore.muzhitui.fragment.TabMyFragment;
import com.nevermore.muzhitui.module.bean.FriendBean;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.MyLogger;
import base.SPUtils;
import base.UIUtils;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
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

public class WxJZJFActivity extends BaseActivityTwoV{
    @BindView(R.id.tv_num)
    TextView tv_num;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tv_sex)
    TextView tv_sex;
    @BindView(R.id.tv_in_type)
    TextView tv_in_type;
    @BindView(R.id.gv_friends)
    GridView gv_friends;

    private String city;
    private String privder;
    private String in_type;
    private Integer sex;
    private Integer num = 0;

    private LoadingAlertDialog mLoadingAlertDialog;
    private GridAdapter mAdapter;
    private AlertDialog alertDialog;

    @Override
    public void init() {
        showBack();
        setMyTitle("精准加粉");
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        mAdapter = new GridAdapter(this,new ArrayList<FriendBean.Friend>());
        gv_friends.setAdapter(mAdapter);
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_wx_jzjf;
    }

    @OnClick({R.id.rel_cate,R.id.rel_sex,R.id.rel_address,R.id.rel_num,R.id.tv_wxjzjf})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rel_cate:
                setCate();
                break;
            case R.id.rel_sex:
                setSex();
                break;
            case R.id.rel_address:
                setPickCity();
                break;
            case R.id.rel_num:
                setNum();
                break;
            case R.id.tv_wxjzjf:
                addContacts();
                break;
        }
    }

    private void addContacts() {
        if (num < 10){
            showTest("请选择加粉数量");
            return;
        }
        if(num == 50) {
            final int memberstate = (int) SPUtils.get(SPUtils.KEY_MEMBER_STATE, 0);//会员状态  1是年费2是终身3不是会员
            final int IsExpire = (int) SPUtils.get(SPUtils.KEY_ISEXPIRE, 0);//是否已过期  1是会员已过期 0是正常
            //判断会员是否可分享
            String message = "您目前还不是年费会员，请购买年费会员后即可使用";
            String clickmessage = "购买会员";
            if (IsExpire == 1) {
                message = "您的会员已到期，请续费后即可恢复正常使用";
                clickmessage = "续费";
            }

            if ((memberstate == 3) || (IsExpire == 1)) {
                alertDialog = UIUtils.getAlertDialog(WxJZJFActivity.this, "提示信息", message, "取消", clickmessage, 0, new View.OnClickListener() {
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
            String message = "您目前还不是终身会员，请购买终生会员后即可使用";
            String clickmessage = "购买会员";
            if (IsExpire == 1) {
                message = "您的会员已到期，请续费后即可恢复正常使用";
                clickmessage = "续费";
            }

            if ((memberstate == 3) || (IsExpire == 1) || (memberstate == 1) ) {
                alertDialog = UIUtils.getAlertDialog(WxJZJFActivity.this, "提示信息", message, "取消", clickmessage, 0, new View.OnClickListener() {
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
                if(mAdapter != null){
                    List<FriendBean.Friend> friends = mAdapter.getData();
                    if(friends != null && !friends.isEmpty()){
                        for (FriendBean.Friend friend : friends){
                            addContact("mzt_"+friend.wx_name,friend.phone);
                        }
                    }
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
                loadData();
            }
        });
        tv_50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                final int memberstate = (int) SPUtils.get(SPUtils.KEY_MEMBER_STATE, 0);//会员状态  1是年费2是终身3不是会员
                final int IsExpire = (int) SPUtils.get(SPUtils.KEY_ISEXPIRE, 0);//是否已过期  1是会员已过期 0是正常
                //判断会员是否可分享
                String message = "您目前还不是年费会员，请购买年费会员后即可使用";
                String clickmessage = "购买会员";
                if (IsExpire == 1) {
                    message = "您的会员已到期，请续费后即可恢复正常使用";
                    clickmessage = "续费";
                }

                if ((memberstate == 3) || (IsExpire == 1)) {
                    alertDialog = UIUtils.getAlertDialog(WxJZJFActivity.this, "提示信息", message, "取消", clickmessage, 0, new View.OnClickListener() {
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
                loadData();
            }
        });
        tv_100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                final int memberstate = (int) SPUtils.get(SPUtils.KEY_MEMBER_STATE, 0);//会员状态  1是年费2是终身3不是会员
                final int IsExpire = (int) SPUtils.get(SPUtils.KEY_ISEXPIRE, 0);//是否已过期  1是会员已过期 0是正常
                //判断会员是否可分享
                String message = "您目前还不是终身会员，请购买终身会员后即可使用";
                String clickmessage = "购买会员";
                if (IsExpire == 1) {
                    message = "您的会员已到期，请续费后即可恢复正常使用";
                    clickmessage = "续费";
                }

                if ((memberstate == 3) || (IsExpire == 1) || (memberstate == 1) ) {
                    alertDialog = UIUtils.getAlertDialog(WxJZJFActivity.this, "提示信息", message, "取消", clickmessage, 0, new View.OnClickListener() {
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
                loadData();
            }
        });
    }

    private void setPickCity() {
        final CityPicker cityPicker = new CityPicker.Builder(WxJZJFActivity.this).textSize(20)
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
                loadData();
            }

            @Override
            public void onCancel() {
                showTest("已取消");
            }
        });
    }

    private void setCate() {
        final WheelPickerView cityPicker = new WheelPickerView.Builder(WxJZJFActivity.this).textSize(20)
                .titleTextColor("#000000")
                .backgroundPop(0xa0000000)
                .title("选择行业")
//                .province("全部类型")
//                .city("不限")
                //.district("不限")
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .fileName("category.xml")
                .build();
        cityPicker.show();
        cityPicker.setOnCityItemClickListener(new WheelPickerView.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                MyLogger.kLog().e(citySelected[0]);
                if ("全部类型".equals(citySelected[0])){
                    in_type = null;
                }else {
                    in_type = citySelected[0];
                }
                tv_in_type.setText(citySelected[0]);
                loadData();
            }

            @Override
            public void onCancel() {
                showTest("已取消");
            }
        });
    }
    private void setSex() {
        final WheelPickerView cityPicker = new WheelPickerView.Builder(WxJZJFActivity.this).textSize(20)
                .titleTextColor("#000000")
                .backgroundPop(0xa0000000)
                .title("选择性别")
//                .province("全部类型")
//                .city("不限")
                //.district("不限")
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .fileName("sex.xml")
                .build();

        cityPicker.show();
        cityPicker.setOnCityItemClickListener(new WheelPickerView.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                MyLogger.kLog().e(citySelected[0]);
                if (citySelected[0].equals("男")){
                    sex = 1;
                }else if (citySelected[0].equals("女")){
                    sex = 2;
                }else {
                    sex = null;
                }
                tv_sex.setText(citySelected[0]);
                loadData();
            }

            @Override
            public void onCancel() {
                showTest("已取消");
            }
        });
    }

    private void loadData() {
        mLoadingAlertDialog.show();
        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().listQunFansAcc((String)SPUtils.get(SPUtils.GET_LOGIN_ID,""),sex,in_type,privder,city,num)).subscribe(new Subscriber<FriendBean>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                removeLoadingView();
                removeErrorView();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onNext(FriendBean friendBean) {
                if ("1".equals(friendBean.state)){
                    mAdapter.addDate(friendBean.list);

                }else {
                    showTest(friendBean.msg);
                }
            }
        }));
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

    private static class GridAdapter extends BaseAdapter{
        private List<FriendBean.Friend> friends;
        private Context context;

        public GridAdapter(Context context, List<FriendBean.Friend> friends){
            this.context = context;
            this.friends = friends;
        }
        @Override
        public int getCount() {
            return friends.size();
        }

        @Override
        public Object getItem(int position) {
            return friends.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.item_batch_fan,null,false);
            }
            FriendBean.Friend friend = (FriendBean.Friend) getItem(position);
            ImageLoader.getInstance().displayImage(friend.headimg, (ImageView) convertView.findViewById(R.id.iv_headimg));
            ((TextView)convertView.findViewById(R.id.tv_name)).setText(friend.wx_name);
            return convertView;
        }

        public void addDate(List<FriendBean.Friend> list) {
            this.friends.clear();
            this.friends.addAll(list);
            notifyDataSetChanged();
        }

        public List<FriendBean.Friend> getData() {
            return friends;
        }
    }


}
