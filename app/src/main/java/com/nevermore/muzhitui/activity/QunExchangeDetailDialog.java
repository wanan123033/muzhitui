package com.nevermore.muzhitui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.BaseBean;
import com.nevermore.muzhitui.module.bean.QunChangeBean;
import com.nevermore.muzhitui.module.bean.QunChangeInfo;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import base.BaseActivityTwoV;
import base.ImageUtil;
import base.MyLogger;
import base.SPUtils;
import base.UIUtils;
import base.network.RetrofitUtil;
import base.thread.BaseRunnable;
import base.thread.ThreadManager;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2017/9/26.
 * 群互换详情
 */

public class QunExchangeDetailDialog extends AlertDialog {

    private final String qunName;
    private final int extime;
    @BindView(R.id.iv_qrcode)
    ImageView iv_qrcode;
    @BindView(R.id.tv_wx)
    TextView tv_wx;
    @BindView(R.id.tv_info)
    TextView tv_qunName;
    @BindView(R.id.tv_info2)
    TextView tv_extime;


    private BaseActivityTwoV activity;

    private CompositeSubscription mCompositeSubscription;
    private int qunid;

    private LoadingAlertDialog mLoadingAlertDialog;


    public QunExchangeDetailDialog(Context context,QunChangeBean.QunChange qunChange) {
        super(context);
        View view = View.inflate(context,R.layout.dialog_save_qun,null);
        setView(view);
        this.qunid = qunChange.id;
        this.qunName = qunChange.wx_qun_name;
        this.extime = qunChange.expiration_time;
        setCanceledOnTouchOutside(false);
        ButterKnife.bind(this,view);
        activity = (BaseActivityTwoV) context;
        mLoadingAlertDialog = new LoadingAlertDialog(activity);
        loadData();
    }

    private void loadData() {
        tv_qunName.setText("群名称："+qunName);
        if(extime > 0){
            tv_extime.setText("还有"+extime+"天过期");
        }else {
            tv_extime.setText("已过期");
        }

        mLoadingAlertDialog.show();
        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().getQunChangeOne((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),qunid)).subscribe(new Subscriber<QunChangeInfo>() {
            @Override
            public void onCompleted() {
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onNext(final QunChangeInfo qunChangeInfo) {
                ImageLoader.getInstance().displayImage(qunChangeInfo.pic1,iv_qrcode);

                SpannableStringBuilder string = new SpannableStringBuilder("点击复制微信  " + qunChangeInfo.wx_no + "  ,直接拉你进群");
                string.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        MyLogger.kLog().e("------------------");
                        UIUtils.copy(qunChangeInfo.wx_no);
                        Toast.makeText(getContext(),"已复制",Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        ds.setColor(Color.BLUE);
                    }
                },6,("点击复制微信    " + qunChangeInfo.wx_no).length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                tv_wx.setText(string);
                tv_wx.setMovementMethod(LinkMovementMethod.getInstance());

                addSubscription(wrapObserverWithHttp(WorkService.getWorkService().addQunChangeRead((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),qunChangeInfo.id)).subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BaseBean o) {

                    }
                }));
            }
        }));
    }


    @OnClick({R.id.btn_save,R.id.iv_finish})
    public void onClick(final View view){
        if(view.getId() == R.id.btn_save) {
            mLoadingAlertDialog.show();
            ThreadManager.getInstance().run(new BaseRunnable() {
                public void run() {
                    Bitmap bitmap = UIUtils.loadBitmapFromView(iv_qrcode);
                    if(bitmap != null){
                        try{
                            final String path = ImageUtil.saveImageToGallery(getContext(),bitmap);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    activity.showTest("已保存至"+path);
                                    mLoadingAlertDialog.dismiss();
                                }
                            });
                        }catch(Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void run(Object... objs) {
                    super.run(objs);
                }
            });
        }else if(view.getId() == R.id.iv_finish){
            dismiss();
        }
    }
    public Observable wrapObserverWithHttp(Observable observable) {
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }
}
