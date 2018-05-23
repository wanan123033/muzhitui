package com.nevermore.muzhitui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.lljjcoder.citypickerview.widget.CityPicker;
import com.nevermore.muzhitui.EventBusContanct;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.BaseBean;
import com.nevermore.muzhitui.module.bean.QunFanOne;
import com.nevermore.muzhitui.module.bean.url;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.List;

import base.BaseActivityTwoV;
import base.ImageUtil;
import base.MyLogger;
import base.SPUtils;
import base.network.RetrofitUtil;
import base.thread.BaseRunnable;
import base.thread.ThreadManager;
import base.util.QrUtil;
import base.view.CateGoryPickerView;
import base.view.LoadingAlertDialog;
import base.view.SexPickerView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Administrator on 2018/1/10.
 */

public class BusinessPostedActivity extends BaseActivityTwoV{

    public static final String BTN_STATE = "btn_state";  //1： 修改名片  0：发布名片
    public static final String BUSINESS_ID = "business_id"; //名片id
    @BindView(R.id.et_wxname)
    EditText et_wxname;
    @BindView(R.id.et_wxnumber)
    EditText et_wxnumber;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_info)
    TextView et_info;
    @BindView(R.id.iv_wxqrcode)
    ImageView iv_wxqrcode;
    @BindView(R.id.tv_posted)
    TextView tv_posted;

    @BindView(R.id.et_hycate)
    TextView et_hycate;
    @BindView(R.id.et_address)
    TextView tvAddress;
    @BindView(R.id.tv_sex)
    TextView tv_sex;

    private int businessId;
    private LoadingAlertDialog mLoadingAlertDialog;

    private String imgUrl;
    private String imgPath;
    private String city;
    private String privder;
    private Integer sex;
    private String imagePath;

    @Override
    public void init() {
        showBack();
        setMyTitle("发布名片");

        int btn_state = getIntent().getIntExtra(BTN_STATE,0);
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        businessId = getIntent().getIntExtra(BUSINESS_ID,0);
        if(btn_state == 0){
            tv_posted.setText("发布名片");
        }else {
            tv_posted.setText("修改/刷新再发布");
        }

        if(businessId != 0){
            loadData();
        }

    }

    private void loadData() {
        mLoadingAlertDialog.show();
        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().getQunFansOne((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),businessId)).subscribe(new Subscriber<QunFanOne>() {
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
            public void onNext(QunFanOne qunFanOne) {
                if ("1".equals(qunFanOne.state)) {
                    initView(qunFanOne);
                }else {
                    showTest(qunFanOne.msg);
                }
            }
        }));
    }

    private void initView(QunFanOne qunFanOne) {
        et_wxname.setText(qunFanOne.wx_name);
        et_wxnumber.setText(qunFanOne.wx_no);
        et_phone.setText(qunFanOne.phone);
        et_info.setText(qunFanOne.introduce);
        et_hycate.setText(qunFanOne.industry_type);
        tvAddress.setText((TextUtils.isEmpty(qunFanOne.province) ? "":qunFanOne.province)  + (TextUtils.isEmpty(qunFanOne.city) ? "":qunFanOne.city));
        tv_sex.setText(qunFanOne.sex == 1 ? "男" : "女");
        privder = qunFanOne.province;
        city = qunFanOne.city;
        sex = qunFanOne.sex;
        ImageLoader.getInstance().displayImage(qunFanOne.pic1,iv_wxqrcode);

        imgUrl = qunFanOne.pic1.replaceAll(RetrofitUtil.API_URL+RetrofitUtil.PROJECT_URL ,"");
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_posted_business;
    }

    @OnClick({R.id.tv_posted,R.id.rel_qrcode,R.id.ll_address,R.id.ll_sex,R.id.ll_incuType,R.id.ll_info})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.tv_posted:
                if (tv_posted.getText().toString().contains("发布名片")) {
                    if(TextUtils.isEmpty(imgPath)){
                        postedBusiness();
                    }else {
                        getImageUrl(imgPath);
                    }
                } else if (tv_posted.getText().toString().contains("修改/刷新再发布")) {
                    if(TextUtils.isEmpty(imgPath)){
                        updateBusiness();
                    }else {
                        getImageUrl(imgPath);
                    }

                }
                break;
            case R.id.rel_qrcode:
                GalleryFinal.openGalleryMuti(169, 1, new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, final List<PhotoInfo> resultList) {
                        if (reqeustCode == 169 && resultList != null && !resultList.isEmpty()) {
                            imgPath = resultList.get(0).getPhotoPath();
                            ImageLoader.getInstance().displayImage(Uri.fromFile(new File(imgPath)).toString(),iv_wxqrcode);
                        }
                    }

                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {

                    }
                });
                break;
            case R.id.ll_address:
                setPickCity();
                break;
            case R.id.ll_sex:
                setSex();
                break;
            case R.id.ll_incuType:
                setCate();
                break;
            case R.id.ll_info:
                Intent intent = new Intent(this,FeedbackActivity.class);
                intent.putExtra(FeedbackActivity.TITLE,"个人介绍");
                intent.putExtra(FeedbackActivity.CONTENT,et_info.getText().toString().trim());
                startActivityForResult(intent,256);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 256 && resultCode == RESULT_OK && data != null){
            String contact = data.getStringExtra(FeedbackActivity.CONTACT);
            et_info.setText(contact);
        }
    }

    private void setPickCity() {
        final CityPicker cityPicker = new CityPicker.Builder(BusinessPostedActivity.this).textSize(20)
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
            }

            @Override
            public void onCancel() {
                Toast.makeText(BusinessPostedActivity.this, "已取消", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setCate() {
        final CateGoryPickerView cityPicker = new CateGoryPickerView.Builder(BusinessPostedActivity.this).textSize(20)
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
                et_hycate.setText(citySelected[0]);
            }

            @Override
            public void onCancel() {
                Toast.makeText(BusinessPostedActivity.this, "已取消", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void setSex() {
        final SexPickerView cityPicker = new SexPickerView.Builder(BusinessPostedActivity.this).textSize(20)
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
                tv_sex.setText(citySelected[0]);
            }

            @Override
            public void onCancel() {
                Toast.makeText(BusinessPostedActivity.this, "已取消", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void getImageUrl(String imageLoadPath) {
        try{
            mLoadingAlertDialog.show();
            Subscription sbMyAccount9 = wrapObserverWithHttp(WorkService.getWorkService().compressUpload((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""), ImageUtil.getInstance().wrapUploadImgRequest(ImageUtil.scal(imageLoadPath)))
            ).subscribe(new Subscriber<url>() {
                @Override
                public void onCompleted() {
                    mLoadingAlertDialog.dismiss();

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    mLoadingAlertDialog.dismiss();
                    showTest("服务器连接失败");
                    MyLogger.kLog().e(e);
                }

                @Override
                public void onNext(url url) {
                    MyLogger.kLog().e(url.imgUrl.toString());
                    imgUrl = url.imgUrl;

                    if (tv_posted.getText().toString().contains("发布名片")) {
                        postedBusiness();
                    } else if (tv_posted.getText().toString().contains("修改/刷新再发布")) {
                        updateBusiness();
                    }

                }
            });

            addSubscription(sbMyAccount9);
        }catch (Exception e){
            e.printStackTrace();
            Log.e("====url:",e.toString());
        }
    }



    private void updateBusiness() {

        String wxname = et_wxname.getText().toString().trim();
        String wxnumber = et_wxnumber.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String info = et_info.getText().toString().trim();
        String hycate = et_hycate.getText().toString().trim();

        if (TextUtils.isEmpty(wxname)){
            showTest("请输入微信昵称");
            return;
        }
        if (TextUtils.isEmpty(wxnumber)){
            showTest("请输入微信号");
            return;
        }
        if (TextUtils.isEmpty(phone)){
            showTest("请输入电话号码");
            return;
        }
        if (TextUtils.isEmpty(info)){
            showTest("请输入个人介绍");
            return;
        }

        if (hycate.equals("全部类型")){
            showTest("请选择行业类型");
            return;
        }

        mLoadingAlertDialog.show();
        RequestParams params = new RequestParams(RetrofitUtil.API_URL + "song/appWxQunFansApi/updateQunFans");
        try {
            JSONArray array = new JSONArray();
            JSONObject json = new JSONObject();
            json.put("name","content_pic1");
            json.put("content",imgUrl);
            array.put(json);
            params.addBodyParameter("content_text",array.toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
        params.addBodyParameter("loginId_mzt", (String) SPUtils.get(SPUtils.GET_LOGIN_ID,""));
        params.addBodyParameter("id",businessId+"");
        params.addBodyParameter("wx_name",wxname);
        params.addBodyParameter("wx_no",wxnumber);
        params.addBodyParameter("phone",phone);
        params.addBodyParameter("industry_type",hycate);
        params.addBodyParameter("introduce",info);
        params.addBodyParameter("province",privder); // 省
        params.addBodyParameter("city",city); // 市
        params.addBodyParameter("sex",sex+"");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String baseBean) {
                MyLogger.kLog().e(baseBean);
                mLoadingAlertDialog.dismiss();
                try {
                    JSONObject json = new JSONObject(baseBean);
                    String state = json.getString("state");
                    if("1".equals(state)){
                        showTest("修改更新成功");
                        finish();
                    }else {
                        showTest(json.getString("msg"));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                mLoadingAlertDialog.dismiss();
                throwable.printStackTrace();
                MyLogger.kLog().e(throwable);
            }

            @Override
            public void onCancelled(CancelledException e) {
                mLoadingAlertDialog.dismiss();
                e.printStackTrace();
                MyLogger.kLog().e(e);
            }

            @Override
            public void onFinished() {
                mLoadingAlertDialog.dismiss();
            }
        });
    }

    private void postedBusiness() {
        mLoadingAlertDialog.show();
        String wxname = et_wxname.getText().toString().trim();
        String wxnumber = et_wxnumber.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String info = et_info.getText().toString().trim();
        String hycate = et_hycate.getText().toString().trim();

        if (TextUtils.isEmpty(wxname)){
            showTest("请输入微信昵称");
            return;
        }
        if (TextUtils.isEmpty(wxnumber)){
            showTest("请输入微信号");
            return;
        }
        if (TextUtils.isEmpty(phone)){
            showTest("请输入电话号码");
            return;
        }
        if (TextUtils.isEmpty(info)){
            showTest("请输入个人介绍");
            return;
        }

        if (hycate.equals("全部类型")){
            showTest("请选择行业类型");
            return;
        }

        RequestParams params = new RequestParams(RetrofitUtil.API_URL + "song/appWxQunFansApi/saveQunFans");
        try {
            JSONArray array = new JSONArray();
            JSONObject json = new JSONObject();
            json.put("name","content_pic1");
            json.put("content",imgUrl);
            array.put(json);
            params.addBodyParameter("content_text",array.toString());
        }catch (JSONException e){
            e.printStackTrace();
        }

        params.addBodyParameter("loginId_mzt", (String) SPUtils.get(SPUtils.GET_LOGIN_ID,""));
        params.addBodyParameter("wx_name",wxname);
        params.addBodyParameter("wx_no",wxnumber);
        params.addBodyParameter("phone",phone);
        params.addBodyParameter("industry_type",hycate);
        params.addBodyParameter("introduce",info);
        params.addBodyParameter("province",privder); // 省
        params.addBodyParameter("city",city); // 市
        params.addBodyParameter("sex",sex+"");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String baseBean) {
                MyLogger.kLog().e(baseBean);
                mLoadingAlertDialog.dismiss();
                try {
                    JSONObject json = new JSONObject(baseBean);
                    String state = json.getString("state");
                    if("1".equals(state)){
                        showTest("上传成功");
                        EventBus.getDefault().post(new EventBusContanct(EventBusContanct.WX_XIU));
                        finish();
                    }else {
                        showTest(json.getString("msg"));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                mLoadingAlertDialog.dismiss();
                MyLogger.kLog().e(throwable);
            }

            @Override
            public void onCancelled(CancelledException e) {
                mLoadingAlertDialog.dismiss();
                MyLogger.kLog().e(e);
            }

            @Override
            public void onFinished() {
                mLoadingAlertDialog.dismiss();
            }
        });
    }
}
