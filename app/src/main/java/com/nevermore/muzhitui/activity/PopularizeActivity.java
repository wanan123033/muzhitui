package com.nevermore.muzhitui.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.nevermore.muzhitui.MainActivity;
import com.nevermore.muzhitui.PageLookActivity;
import com.nevermore.muzhitui.PagerEditActivity;
import com.nevermore.muzhitui.QrActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.Order;
import com.nevermore.muzhitui.module.bean.PageGPSBean;
import com.nevermore.muzhitui.module.bean.VideoPath;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.smtt.sdk.TbsVideo;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.BaseApplication;
import base.MyLogger;
import base.SPUtils;
import base.network.RetrofitUtil;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
import base.recycler.recyclerview.OnItemClickListener;
import base.thread.Handler;
import base.util.ShareUtil;
import base.view.DragView;
import base.view.LoadingAlertDialog;
import base.view.VideoGpsView;
import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/10/31.
 */

public class PopularizeActivity extends BaseActivityTwoV{

    @BindView(R.id.rv_pop_list)
    RecyclerView rv_pop_list;
    @BindView(R.id.sv_rootview)
    ScrollView sv_rootview;
    @BindView(R.id.iv_qrcode)
    ImageView iv_qrcode;
    @BindView(R.id.civ_topic)
    CircleImageView civ_topic;
    @BindView(R.id.dragview)
    DragView dragview;

    private String[] videoUrls = new String[9];
    private List<Order.OrderPar> orders = new ArrayList<Order.OrderPar>();

    private PopupWindow mPpwMenuShare;

    private LoadingAlertDialog dialog;
    final Runnable scrollRunnable = new Runnable() {
        @Override
        public void run() {
            rv_pop_list.scrollBy(0,3);
            Handler.getHandler().postDelayed(scrollRunnable,50);
        }
    };

    @Override
    public void init() {
        setMyTitle("推广");
        showBack();
        dialog = new LoadingAlertDialog(this);
        final CommonAdapter adapter = new CommonAdapter<Order.OrderPar>(this,R.layout.item_popularize,orders) {
            @Override
            public int getItemCount() {
                if(mDatas.size() == 0){
                    return 0;
                }
                return Integer.MAX_VALUE;
            }


            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                Order.OrderPar order = mDatas.get(position % mDatas.size());
                convert(holder,order);
            }

            @Override
            public void convert(ViewHolder holder, final Order.OrderPar o) {
                ImageLoader.getInstance().displayImage(o.headimg, (ImageView) holder.getView(R.id.civ_topic));
                ((TextView)holder.getView(R.id.tv_info)).setText(Html.fromHtml("<u>"+o.user_name+"</u>"));
                ((TextView)holder.getView(R.id.tv_info2)).setText(Html.fromHtml("<u>"+o.username_out+"</u>"));
                ((TextView)holder.getView(R.id.tv_info4)).setText(Html.fromHtml("<u>"+o.price+"</u>"));

                ((TextView)holder.getView(R.id.tv_info)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Order.OrderPar order = o;
                        if(order.friend_status == 1 || order.friend_status == 3){  //是好友
                            Intent intent = new Intent(PopularizeActivity.this, SeePersonalInfoIsFriendActivity.class);
                            intent.putExtra("id", order.loginId + "");
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(PopularizeActivity.this, SeePersonalInfoActivity.class);
                            intent.putExtra("id", order.loginId + "");
                            intent.putExtra("friend_state", (order.friend_status == 0 ? 11:12) + "");//11:加为好友  12 等待验证
                            startActivity(intent);
                        }
                    }
                });

                ((TextView)holder.getView(R.id.tv_info2)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Order.OrderPar order = o;
                        if(order.friend_status == 1 || order.friend_status == 3){  //是好友
                            Intent intent = new Intent(PopularizeActivity.this, SeePersonalInfoIsFriendActivity.class);
                            intent.putExtra("id", order.loginId_out + "");
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(PopularizeActivity.this, SeePersonalInfoActivity.class);
                            intent.putExtra("id", order.loginId_out + "");
                            intent.putExtra("friend_state", (order.friend_status == 0 ? 11:12) + "");//11:加为好友  12 等待验证
                            startActivity(intent);
                        }
                    }
                });
            }
        };
        RecyclerAdapterWithHF adapterWithHF = new RecyclerAdapterWithHF(adapter);
//        adapterWithHF.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
//            @Override
//            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {
//                Order.OrderPar order = orders.get(position);
//                if(order.friend_status == 1 || order.friend_status == 3){  //是好友
//                    Intent intent = new Intent(PopularizeActivity.this, SeePersonalInfoIsFriendActivity.class);
//                    intent.putExtra("id", order.loginId + "");
//                    startActivity(intent);
//                }else {
//                    Intent intent = new Intent(PopularizeActivity.this, SeePersonalInfoActivity.class);
//                    intent.putExtra("id", order.loginId + "");
//                    intent.putExtra("friend_state", (order.friend_status == 0 ? 11:12) + "");//11:加为好友  12 等待验证
//                    startActivity(intent);
//                }
//            }
//        });
//        String s = null;
//        s.length();
        rv_pop_list.setAdapter(adapterWithHF);
        dialog.show();

        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().getOrderList((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))).subscribe(new Subscriber<Order>() {
            @Override
            public void onCompleted() {
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
            }

            @Override
            public void onNext(Order o) {
                if("1".equals(o.state)){
                    adapter.replaceAllDate(o.orderList);
                }else {
                    showTest(o.msg);
                }
            }
        }));
        initMenuPpwWindow();
        showRight(R.drawable.ic_spread, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPpwMenuShare.showAtLocation(sv_rootview, Gravity.BOTTOM, 0, 0);
            }
        });

        String pathimg = RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + (String) SPUtils.get(SPUtils.qr_code_img, "");
        MyLogger.kLog().e(pathimg);
        ImageLoader.getInstance().displayImage(pathimg,iv_qrcode);
        String headimg = RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + (String)SPUtils.get(SPUtils.KEY_TOPIC,"");
        ImageLoader.getInstance().displayImage(headimg,civ_topic);

        loadData();

        final VideoGpsView videoGpsView = new VideoGpsView(this,2);
        dragview.addDragView(videoGpsView, 550, 850, true, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoGpsView.onClick(v);
            }
        });
    }

    private void loadData() {
        dialog.show();
        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().getPopVedio()).subscribe(new Subscriber<VideoPath>() {
            @Override
            public void onCompleted() {
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
            }

            @Override
            public void onNext(VideoPath videoPath) {
                if("1".equals(videoPath.state)){
                    for(int i = 0 ; i < videoPath.arrayList.size() ; i++){
                        videoUrls[videoPath.arrayList.get(i).show_number - 1] = videoPath.arrayList.get(i).jump_url;
                    }

                }
            }
        }));
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_popularize;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Handler.getHandler().postDelayed(scrollRunnable,10);
    }

    private void initMenuPpwWindow() {
        //设置contentView
        mPpwMenuShare = ShareUtil.getInstance().initMenuPpwWindow();
        ShareUtil.getInstance().setShareInfo(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + RetrofitUtil.SHAREQR + MainActivity.loginId, "拇指推—这是一个推广神器！", PagerEditActivity.defaultPath, "这个软件可以把你的产品或项目推广到整个社交生态圈的推广神器！");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Handler.getHandler().removeCallbacks(scrollRunnable);
    }
    @OnClick({R.id.btn_gen_snap,R.id.iv_mbPlay,R.id.iv_ycPlay,R.id.iv_afPlay,R.id.iv_zfPlay,R.id.iv_kzPlay,R.id.iv_cpPlay,R.id.iv_qunPlay,R.id.iv_rmPlay,R.id.ll_text3})
    public void onClick(View view){
        int index = 0;
        switch (view.getId()){
            case R.id.btn_gen_snap:
                baseStartActivity(GenSnapActivity.class);
                return;
            case R.id.iv_mbPlay:
                index = 0;
                break;
            case R.id.iv_ycPlay:
                index = 1;
                break;
            case R.id.iv_afPlay:
                index = 2;
                break;
            case R.id.iv_zfPlay:
                index = 3;
                break;
            case R.id.iv_kzPlay:
                index = 4;
                break;
            case R.id.iv_cpPlay:
                index = 5;
                break;
            case R.id.iv_qunPlay:
                index = 6;
                break;
            case R.id.iv_rmPlay:
                index = 7;
                break;
            case R.id.ll_text3:
                index = 8;
                break;
        }
        Intent intent = new Intent(getApplicationContext(),PageLookActivity.class);
        intent.putExtra(PageLookActivity.KEY_URL,videoUrls[index]);
        startActivity(intent);
    }
}
