package com.nevermore.muzhitui.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.editPhoto.ImageItem;
import com.nevermore.muzhitui.module.BaseBean;
import com.nevermore.muzhitui.module.bean.Bimp;
import com.nevermore.muzhitui.module.bean.DynamicBean;
import com.nevermore.muzhitui.module.bean.QunMeInfo;
import com.nevermore.muzhitui.module.bean.QunWantInfo;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.MyLogger;
import base.SPUtils;
import base.UIUtils;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

import static com.nevermore.muzhitui.module.bean.Bimp.bmp;


/**
 * Created by Administrator on 2017/9/14.
 * 我有群,我要群详情
 */

public class QunDetailActivity extends BaseActivityTwoV {
    public static final String QUN_ID = "qun_id";
    public static final String TYPE = "type";

    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_weizhi)
    TextView tv_weizhi;
    @BindView(R.id.tv_baojia)
    TextView tv_baojia;
    @BindView(R.id.tv_wxno)
    TextView tv_wxno;
    @BindView(R.id.tv_phone_num)
    TextView tv_phone_num;
    @BindView(R.id.tv_owner_admin)
    TextView tv_owner_admin;
    @BindView(R.id.tv_remark)
    TextView tv_remark;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_pic1)
    ImageView iv_pic1;
    @BindView(R.id.iv_pic2)
    ImageView iv_pic2;
    @BindView(R.id.iv_pic3)
    ImageView iv_pic3;
    @BindView(R.id.iv_pic4)
    ImageView iv_pic4;

    private int qunId;
    private int type;  //0我有群 1我要群
    private String wx_no;
    String phone;


    private LoadingAlertDialog mLoadingAlertDialog;
    private QunMeInfo qunMeInfo;
    private QunWantInfo qunWantInfo;

    @Override
    public void init() {
        setMyTitle("群详情");
        showBack();
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        qunId = getIntent().getIntExtra(QUN_ID,0);
        type = getIntent().getIntExtra(TYPE,0);
        loadData();
    }

    private void loadData() {
        mLoadingAlertDialog.show();
        if(type == 0) {
            addSubscription(wrapObserverWithHttp(WorkService.getWorkService().getQunMeOne((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""), qunId)).subscribe(new Subscriber<QunMeInfo>() {
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
                public void onNext(QunMeInfo qunMeInfo) {
                    if ("1".equals(qunMeInfo.state)) {
                        setData(qunMeInfo);
                    } else {
                        showTest(qunMeInfo.msg);
                    }
                }
            }));
        }else if(type == 1){
            addSubscription(wrapObserverWithHttp(WorkService.getWorkService().getQunWantOne((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""), qunId)).subscribe(new Subscriber<QunWantInfo>() {
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
                public void onNext(QunWantInfo qunWantInfo) {
                    if ("1".equals(qunWantInfo.state)) {
                        setData(qunWantInfo);
                    } else {
                        showTest(qunWantInfo.msg);
                    }
                }
            }));
        }
    }

    private void setData(QunWantInfo qunWantInfo) {
        this.qunWantInfo = qunWantInfo;
        wx_no = qunWantInfo.wx_no;
        phone = qunWantInfo.phone;
        tv_title.setText("悬赏(元)");
        tv_name.setText(qunWantInfo.wx_qun_name);
        tv_baojia.setText("￥"+qunWantInfo.offer+"元");
        tv_weizhi.setText(qunWantInfo.wx_city);
        tv_phone_num.setText(qunWantInfo.phone);
        tv_wxno.setText(qunWantInfo.wx_no);
        tv_owner_admin.setText("--");
        ImageLoader.getInstance().displayImage(qunWantInfo.pic1,iv_pic1);
        ImageLoader.getInstance().displayImage(qunWantInfo.pic2,iv_pic2);
        ImageLoader.getInstance().displayImage(qunWantInfo.pic3,iv_pic3);
        ImageLoader.getInstance().displayImage(qunWantInfo.pic4,iv_pic4);

        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().addQunWantRead((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),qunWantInfo.id)).subscribe(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BaseBean baseBean) {

            }
        }));
    }

    private void setData(QunMeInfo qunMeInfo) {
        this.qunMeInfo = qunMeInfo;
        wx_no = qunMeInfo.wx_no;
        phone = qunMeInfo.phone;
        tv_name.setText(qunMeInfo.wx_qun_name);
        tv_baojia.setText("￥"+qunMeInfo.offer+"元");
        tv_weizhi.setText(qunMeInfo.wx_city);
        tv_phone_num.setText(qunMeInfo.phone);
        tv_wxno.setText(qunMeInfo.wx_no);
        if (qunMeInfo.is_owner == 1){
            tv_owner_admin.setText("是");
        }else {
            tv_owner_admin.setText("否");
        }
        tv_remark.setText(qunMeInfo.remark);
        tv_title.setText("报价(元)");
        ImageLoader.getInstance().displayImage(qunMeInfo.pic1,iv_pic1);
        ImageLoader.getInstance().displayImage(qunMeInfo.pic2,iv_pic2);
        ImageLoader.getInstance().displayImage(qunMeInfo.pic3,iv_pic3);
        ImageLoader.getInstance().displayImage(qunMeInfo.pic4,iv_pic4);

        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().addQunMeRead((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),qunMeInfo.id)).subscribe(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BaseBean baseBean) {

            }
        }));
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_qun_detail;
    }
    @OnClick({R.id.tv_fz_wx,R.id.iv_call_phone,R.id.iv_pic1,R.id.iv_pic2,R.id.iv_pic3,R.id.iv_pic4})
    public void onClick(View view){
        int position = 0;
        switch (view.getId()){
            case R.id.tv_fz_wx:
                UIUtils.copy(wx_no);
                showTest("复制成功");
                break;
            case R.id.iv_call_phone:
                MyLogger.kLog().e("tel:"+phone);
                Intent in2 = new Intent();
                in2.setAction(Intent.ACTION_DIAL);
                in2.setData(Uri.parse("tel:"+phone));
                startActivity(in2);
                break;
            case R.id.iv_pic1:
            case R.id.iv_pic2:
            case R.id.iv_pic3:
            case R.id.iv_pic4:
                //TODO 图片查看
                if(view.getId() == R.id.iv_pic1){
                    position = 0;
                }else if(view.getId() == R.id.iv_pic2){
                    position = 1;
                }else if(view.getId() == R.id.iv_pic3){
                    position = 2;
                }else if(view.getId() == R.id.iv_pic4){
                    position = 3;
                }
                if(type == 0){
                    bmp.clear();
                    if(qunMeInfo.pic1 != null)
                        bmp.add(new ImageItem("","",qunMeInfo.pic1,0,"","",0,0));
                    if(qunMeInfo.pic2 != null)
                        bmp.add(new ImageItem("","",qunMeInfo.pic2,0,"","",0,0));
                    if(qunMeInfo.pic3 != null)
                        bmp.add(new ImageItem("","",qunMeInfo.pic3,0,"","",0,0));
                    if(qunMeInfo.pic4 != null)
                        bmp.add(new ImageItem("","",qunMeInfo.pic4,0,"","",0,0));
                }else if(type == 1){
                    bmp.clear();
                    if(qunWantInfo.pic1 != null)
                        bmp.add(new ImageItem("","",qunWantInfo.pic1,0,"","",0,0));
                    if(qunWantInfo.pic2 != null)
                        bmp.add(new ImageItem("","",qunWantInfo.pic2,0,"","",0,0));
                    if(qunWantInfo.pic3 != null)
                        bmp.add(new ImageItem("","",qunWantInfo.pic3,0,"","",0,0));
                    if(qunWantInfo.pic4 != null)
                        bmp.add(new ImageItem("","",qunWantInfo.pic4,0,"","",0,0));
                }
                Intent intent = new Intent(getApplicationContext(),PhotoDetailActivity.class);
                intent.putExtra(PhotoDetailActivity.POSTION,position);
                startActivity(intent);
                break;

        }
    }
}
