package com.nevermore.muzhitui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.lljjcoder.citypickerview.widget.CityPicker;
import com.nevermore.muzhitui.EventBusContanct;
import com.nevermore.muzhitui.MainActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.rongyun.MztRongContext;
import com.nevermore.muzhitui.fragment.TabMyFragment;
import com.nevermore.muzhitui.module.BaseBean;
import com.nevermore.muzhitui.module.bean.PublishQunFans;
import com.nevermore.muzhitui.module.bean.QunFans;
import com.nevermore.muzhitui.module.bean.QunFansAcc;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import base.BaseActivityTwoV;
import base.MyLogger;
import base.SPUtils;
import base.UIUtils;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
import base.recycler.recyclerview.OnItemClickListener;
import base.view.CateGoryPickerView;
import base.view.LoadingAlertDialog;
import base.view.RoundImageView;
import base.view.SexPickerView;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by Administrator on 2018/1/2.
 * 微信互粉
 */

public class WxHuFenActivity extends BaseActivityTwoV{
    @BindView(R.id.pcFlyt)
    PtrClassicFrameLayout pcFlyt;
    @BindView(R.id.rv_dynamic)
    RecyclerView rv_dynamic;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tv_industry)
    TextView tv_industry;
    @BindView(R.id.tv_sex)
    TextView tv_sex;
    @BindView(R.id.ll_business_update)
    LinearLayout ll_business_update;
    @BindView(R.id.ll_business_posted)
    LinearLayout ll_business_posted;
    @BindView(R.id.ll_placeTop)
    LinearLayout ll_placeTop;

    private List<QunFans.QunFan> list = new ArrayList<QunFans.QunFan>();

    private int mPageCurrent = 1;
    private CommonAdapter mAdapter;
    private LoadingAlertDialog mLoadingAlertDialog;
    private String city;
    private String privder;
    private int id;
    private Integer sex; //1男  2女  null: 不限
    private String in_type;
    private AlertDialog alertDialog,alertDialog1,alertDialog2;
    private int is_top,is_publish;

    @Override
    public void init() {
        showBack();
        setMyTitle("互粉");
        EventBus.getDefault().register(this);
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        mAdapter = new CommonAdapter<QunFans.QunFan>(this,R.layout.item_wx_hufen,list) {
            @Override
            public void convert(ViewHolder holder, final QunFans.QunFan o) {
                RoundImageView iv = holder.getView(R.id.iv_head);
                iv.setRound(4);
                ImageLoader.getInstance().displayImage(o.headimg, iv);
                holder.setText(R.id.tv_name,o.wx_name);
                holder.setText(R.id.tv_info,o.introduce);
                if (o.is_top == 0){
                    ((TextView)holder.getView(R.id.tv_name)).setTextColor(Color.BLACK);
                    holder.getView(R.id.iv_top).setVisibility(View.GONE);
                }else {
                    holder.getView(R.id.iv_top).setVisibility(View.VISIBLE);
                    ImageView ivv = holder.getView(R.id.iv_top);
                    if (o.is_top == 1){
                        ivv.setImageResource(R.mipmap.iv_place_top);
                        ((TextView)holder.getView(R.id.tv_name)).setTextColor(Color.BLUE);
                    }else {
                        ivv.setImageResource(R.mipmap.place_top_red);
                        ((TextView)holder.getView(R.id.tv_name)).setTextColor(Color.RED);
                    }
                }
//                holder.setOnClickListener(R.id.btn_addFriend, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(getApplicationContext(),BusinessDatailActivity.class);
//                        intent.putExtra(BusinessDatailActivity.BUSINESS_ID,o.id);
//                        startActivity(intent);
//                    }
//                });
            }
        };
        RecyclerAdapterWithHF recyclerAdapterWithHF = new RecyclerAdapterWithHF(mAdapter);
        rv_dynamic.setAdapter(recyclerAdapterWithHF);
//        pcFlyt.setLastUpdateTimeRelateObject(this);
        pcFlyt.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                list.clear();
                mPageCurrent = 1;
                loadData(mPageCurrent);
            }

        });
        pcFlyt.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {

                loadData(++mPageCurrent);
            }
        });

        recyclerAdapterWithHF.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {
                QunFans.QunFan qunFan = list.get(position);
                Intent intent = new Intent(getApplicationContext(),BusinessDatailActivity.class);
                intent.putExtra(BusinessDatailActivity.BUSINESS_ID,qunFan.id);
                startActivity(intent);
            }
        });
        loadData(mPageCurrent);
        isPublishQunFans();


        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().isQunFansAcc()).subscribe(new Subscriber<QunFansAcc>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(QunFansAcc qunFansAcc) {
                if("1".equals(qunFansAcc.state)){
                    if(qunFansAcc.is_qunfans_acc == 0){

                    }else if(qunFansAcc.is_qunfans_acc == 1){
//                        showRight("批量加粉", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                baseStartActivity(WxBatchActivity.class);
//                            }
//                        });
                    }
                }
            }
        }));
    }

    private void isPublishQunFans() {
        mLoadingAlertDialog.show();
        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().isPublishQunFans((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))).subscribe(new Subscriber<PublishQunFans>() {
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
            public void onNext(PublishQunFans baseBean) {

                if ("1".equals(baseBean.state)){
                    is_top = baseBean.is_able_top;
                    is_publish = baseBean.is_publish;
                    if(baseBean.is_publish == 1){
                        ll_business_update.setVisibility(View.VISIBLE);
                        ll_business_posted.setVisibility(View.GONE);
                        id = baseBean.id;
                    }else {
                        ll_business_update.setVisibility(View.GONE);
                        ll_business_posted.setVisibility(View.VISIBLE);
                    }

                }else {
                    showTest(baseBean.msg);
                }
            }
        }));
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_wx_hufen;
    }

    @OnClick({R.id.ll_business_posted,R.id.ll_placeTop,R.id.ll_address,R.id.ll_business_update,R.id.ll_cate,R.id.ll_sex})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_business_posted:  //发布名片
                baseStartActivity(BusinessPostedActivity.class);
                break;
            case R.id.ll_placeTop:  //置顶
                placeTop();
                break;
            case R.id.ll_address:  //全部地区
                setPickCity();
                break;
            case R.id.ll_cate:    //全部类型
                setCate();
                break;
            case R.id.ll_business_update:  //修改刷新名片
                Intent intent = new Intent(getApplicationContext(),BusinessPostedActivity.class);
                intent.putExtra(BusinessPostedActivity.BTN_STATE,1);
                intent.putExtra(BusinessPostedActivity.BUSINESS_ID,id);
                startActivity(intent);
                break;
            case R.id.ll_sex:
                setSex();
                break;
        }
    }

    private void placeTop() {
        if(is_top != 1){
            if (is_publish != 1){
                alertDialog2 = UIUtils.getAlertDialog(WxHuFenActivity.this, "提示信息", "您还未发布过名片，不能置顶！", "确定", "", 0, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog2.dismiss();
                    }
                },null);
                alertDialog2.show();
            }else {
                alertDialog2 = UIUtils.getAlertDialog(WxHuFenActivity.this, "提示信息", "您置顶的名片时间还未过期，请过期后再试！", "确定", "", 0, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog2.dismiss();
                    }
                },null);
                alertDialog2.show();
            }
            return;
        }

        final int memberstate = (int) SPUtils.get(SPUtils.KEY_MEMBER_STATE, 0);//会员状态  1是年费2是终身3不是会员
        final int IsExpire = (int) SPUtils.get(SPUtils.KEY_ISEXPIRE, 0);//是否已过期  1是会员已过期 0是正常
        //判断会员是否可分享
        String message = "您目前还不是会员，无法置顶；请购买会员后即可发布动态";
        String clickmessage = "购买会员";
        if (IsExpire == 1) {
            message = "您的会员已到期，无法置顶；请续费后即可恢复正常使用";
            clickmessage = "续费";
        }

        if ((memberstate == 3) || (IsExpire == 1)) {
            alertDialog = UIUtils.getAlertDialog(WxHuFenActivity.this, "提示信息", message, "取消", clickmessage, 0, new View.OnClickListener() {
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
        if(memberstate == 1 || memberstate == 2){
            alertDialog1 = UIUtils.getAlertDialog(WxHuFenActivity.this, "提示信息", "年费会员可置顶6小时\n终身会员可置顶12小时", "取消", "置顶", 0, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog1.dismiss();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog1.dismiss();
                    palceTop();
                }
            });
            alertDialog1.show();
        }

    }

    private void palceTop() {
        mLoadingAlertDialog.show();
        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().topQunFans((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),id)).subscribe(new Subscriber<BaseBean>() {
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
            public void onNext(BaseBean baseBean) {
                MyLogger.kLog().e(baseBean.toString());
                if ("1".equals(baseBean.state)){
                    mPageCurrent = 1;
                    loadData(mPageCurrent);
                }else {
                    showTest(baseBean.msg);
                }
            }
        }));
    }

    private void loadData(final int mPageCurrent) {
        mLoadingAlertDialog.show();
        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().listQunFans((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),in_type,privder,city,sex,mPageCurrent)).subscribe(new Subscriber<QunFans>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                pcFlyt.setLoadMoreEnable(true);
                pcFlyt.loadMoreComplete(true);
                pcFlyt.refreshComplete();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                removeLoadingView();
                removeErrorView();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onNext(QunFans qunFans) {
                if("1".equals(qunFans.state)){
                    Collections.sort(qunFans.list, new Comparator<QunFans.QunFan>() {
                        @Override
                        public int compare(QunFans.QunFan o1, QunFans.QunFan o2) {
                            if(o1.is_top == o2.is_top) {
                                return 0;
                            }else if(o1.is_top < o2.is_top){
                                return 1;
                            }else {
                                return -1;
                            }
                        }
                    });
                    if(mPageCurrent == 1){
                        mAdapter.replaceAllDate(qunFans.list);
                    }else if(mPageCurrent > 1){
                        mAdapter.addDate(qunFans.list);
                    }
                }else {
                    showTest(qunFans.msg);
                }
            }
        }));
    }

    private void setPickCity() {
        final CityPicker cityPicker = new CityPicker.Builder(WxHuFenActivity.this).textSize(20)
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
                mPageCurrent = 1;
                loadData(mPageCurrent);
            }

            @Override
            public void onCancel() {
                Toast.makeText(WxHuFenActivity.this, "已取消", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setCate() {
        final CateGoryPickerView cityPicker = new CateGoryPickerView.Builder(WxHuFenActivity.this).textSize(20)
                .titleTextColor("#000000")
                .backgroundPop(0xa0000000)
//                .province("全部类型")
//                .city("不限")
                //.district("不限")
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .build();
        cityPicker.show();
        cityPicker.setOnCityItemClickListener(new CateGoryPickerView.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                MyLogger.kLog().e(citySelected[0]);
                if ("全部行业".equals(citySelected[0])){
                    in_type = null;
                }else {
                    in_type = citySelected[0];
                }
                mPageCurrent = 1;
                tv_industry.setText(citySelected[0]);
                loadData(mPageCurrent);
            }

            @Override
            public void onCancel() {
                Toast.makeText(WxHuFenActivity.this, "已取消", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void setSex() {
        final SexPickerView cityPicker = new SexPickerView.Builder(WxHuFenActivity.this).textSize(20)
                .titleTextColor("#000000")
                .backgroundPop(0xa0000000)
//                .province("全部类型")
//                .city("不限")
                //.district("不限")
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .build();

        cityPicker.show();
        cityPicker.setOnCityItemClickListener(new SexPickerView.OnCityItemClickListener() {
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
                mPageCurrent = 1;
                tv_sex.setText(citySelected[0]);
                loadData(mPageCurrent);
            }

            @Override
            public void onCancel() {
                Toast.makeText(WxHuFenActivity.this, "已取消", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventEMMessage(EventBusContanct contanct) {
        if (contanct.getFlag() == EventBusContanct.WX_XIU){
            isPublishQunFans();
        }
    }
}
