package com.nevermore.muzhitui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.lljjcoder.citypickerview.widget.CityPicker;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.adapter.ImageSecltorAdapter;
import com.nevermore.muzhitui.event.CityEvent;
import com.nevermore.muzhitui.module.BaseBean;
import com.nevermore.muzhitui.module.bean.QunMeBean;
import com.nevermore.muzhitui.module.bean.QunMeInfo;
import com.nevermore.muzhitui.module.bean.QunWantBean;
import com.nevermore.muzhitui.module.bean.QunWantInfo;
import com.nevermore.muzhitui.module.bean.url;
import com.nevermore.muzhitui.module.network.WorkService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.ImageUtil;
import base.MyLogger;
import base.SPUtils;
import base.network.RetrofitUtil;
import base.util.EmojiFilter;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Administrator on 2017/9/15.
 * 我要群 我有群发布
 */

public class QunHavePostedActivity extends BaseActivityTwoV {
    public static final String TYPE = "type";
    public static final String DATA = "data";
    private static final int IMAGE_UPDATE = 122;
    private static final int IMAGE_DELETE = 133;
    private static final int REQUEST_QUN = 45;
    @BindView(R.id.et_dispcrition)
    EditText et_dispcrition;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.et_valuation)
    EditText et_valuation;
    @BindView(R.id.et_phone_number)
    EditText et_phone_number;
    @BindView(R.id.et_wx_no)
    EditText et_wx_no;
    @BindView(R.id.et_remark)
    EditText et_remark;
    @BindView(R.id.tv_valuation)
    TextView tv_valuation;
    @BindView(R.id.ll_snop)
    LinearLayout ll_snop;
    @BindView(R.id.ll_address)
    LinearLayout ll_address;
    @BindView(R.id.et_qun_num)
    EditText et_qun_num;
    @BindView(R.id.ll_qun_admin)
    LinearLayout ll_qun_admin;
    @BindView(R.id.grid_photo)
    GridView grid_photo;

    public List<String> imageLoadPaths = new ArrayList<>();

    private LoadingAlertDialog mLoadingAlertDialog;
    private int type;  //0我有群 1我要群
    private QunMeBean.Qun qun;
    private QunWantBean.QunWant want;

    private int is_owner = 1;
    private QunMeInfo meInfo;
    private int addPostion;

    private List<String> paths;
    private String city = "深圳";
    private String privder = "广东";

    @Override
    public void init() {
        showBack();
        hideSoftInput();
        type = getIntent().getIntExtra(TYPE,0);
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        if(type == 0){
            qun = (QunMeBean.Qun) getIntent().getSerializableExtra(DATA);
            if(qun != null){
                et_dispcrition.setText(qun.wx_qun_name);
                tv_address.setText(qun.wx_city);
                et_valuation.setText(qun.offer+"");
                loadData();
            }
            tv_valuation.setText("报价(元)");
            setMyTitle("我有群发布");
            ll_snop.setVisibility(View.VISIBLE);
            ll_qun_admin.setVisibility(View.VISIBLE);

        }else if(type == 1){
            want = (QunWantBean.QunWant) getIntent().getSerializableExtra(DATA);
            if(want != null){
                et_dispcrition.setText(want.wx_qun_name);
                tv_address.setText(want.wx_city);
                et_valuation.setText(want.offer+"");
                loadData();
            }
            tv_valuation.setText("悬赏(元)");
            setMyTitle("我要群发布");
            ll_snop.setVisibility(View.GONE);
            ll_qun_admin.setVisibility(View.GONE);

        }
        ImageSecltorAdapter adapter = new ImageSecltorAdapter(this,4,new ArrayList<String>(), R.layout.item_add_image);
        grid_photo.setAdapter(adapter);

        et_remark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 60){
                    et_remark.setText(s.subSequence(0,60));
                    et_remark.setSelection(60);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_dispcrition.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 250){
                    et_remark.setText(s.subSequence(0,250));
                    et_remark.setSelection(250);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /**
     * 只有修改时才会用到  用于查询详情填充输入框
     */
    private void loadData() {
        if(type == 1){  //我要群详情
            addSubscription(wrapObserverWithHttp(WorkService.getWorkService().getQunWantOne((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),want.id)).subscribe(new Subscriber<QunWantInfo>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(QunWantInfo o) {
                    if("1".equals(o.state)){
                        updateQun(o);
                    }else {
                        showTest(o.msg);
                    }
                }
            }));
        }else if(type == 0) { //我有群详情
            addSubscription(wrapObserverWithHttp(WorkService.getWorkService().getQunMeOne((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""), qun.id)).subscribe(new Subscriber<QunMeInfo>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(QunMeInfo o) {
                    if("1".equals(o.state)){
                        updateQun(o);
                    }else {
                        showTest(o.msg);
                    }
                }
            }));
        }
    }

    private void updateQun(BaseBean info) {
        if(info instanceof QunWantInfo){
            QunWantInfo wantInfo = (QunWantInfo) info;
            et_phone_number.setText(wantInfo.phone);
            et_wx_no.setText(wantInfo.wx_no);
            et_remark.setText(wantInfo.remark);
            et_valuation.setText(wantInfo.offer+"");
            et_dispcrition.setText(wantInfo.wx_qun_name);
        }else if(info instanceof QunMeInfo){
            QunMeInfo meInfo = (QunMeInfo) info;
            et_phone_number.setText(meInfo.phone);
            et_wx_no.setText(meInfo.wx_no);
            et_remark.setText(meInfo.remark);
            et_valuation.setText(meInfo.offer+"");
            et_dispcrition.setText(meInfo.wx_qun_name);
            et_qun_num.setText(meInfo.wx_qun_num+"");
//            if(meInfo.is_owner == 1){
//                ((RadioButton)rg_qun_admin.getChildAt(0)).setChecked(true);
//            }else {
//                ((RadioButton)rg_qun_admin.getChildAt(1)).setChecked(true);
//            }

            is_owner = meInfo.is_owner;
            //TODO 加载图片数据
            updateImg(meInfo);
        }
    }

    private void updateImg(QunMeInfo meInfo) {
        MyLogger.kLog().e(meInfo.pic1);
        MyLogger.kLog().e(meInfo.pic2);
        MyLogger.kLog().e(meInfo.pic3);
        MyLogger.kLog().e(meInfo.pic4);
//        List<String> paths = new ArrayList<>();
        if(!TextUtils.isEmpty(meInfo.pic1)){
            ((ImageSecltorAdapter)grid_photo.getAdapter()).getImageList().add(meInfo.pic1);
        }
//            imageLoadPaths.add(meInfo.pic1);
//            ImageLoader.getInstance().displayImage(meInfo.pic1,new ImageView(this));
//            paths.add(meInfo.pic1);
//            AndroidImagePicker.getInstance().addSelectedImageItem(0,new com.redare.imagepicker.bean.ImageItem(ImageLoader.getInstance().getDiskCache().get(meInfo.pic1).getAbsolutePath(),"",0));
//            MyLogger.kLog().e(ImageLoader.getInstance().getDiskCache().get(meInfo.pic1).getAbsolutePath());
//        }
        if(!TextUtils.isEmpty(meInfo.pic2)){
            ((ImageSecltorAdapter)grid_photo.getAdapter()).getImageList().add(meInfo.pic2);
        }
//            imageLoadPaths.add(meInfo.pic2);
//            ImageLoader.getInstance().displayImage(meInfo.pic2,new ImageView(this));
//            paths.add(meInfo.pic2);
//            MyLogger.kLog().e(ImageLoader.getInstance().getDiskCache().get(meInfo.pic2).getAbsolutePath());
//            AndroidImagePicker.getInstance().addSelectedImageItem(1,new com.redare.imagepicker.bean.ImageItem(ImageLoader.getInstance().getDiskCache().get(meInfo.pic2).getAbsolutePath(),"",0));
//        }
        if(!TextUtils.isEmpty(meInfo.pic3)){
            ((ImageSecltorAdapter)grid_photo.getAdapter()).getImageList().add(meInfo.pic3);
        }
//            imageLoadPaths.add(meInfo.pic3);
//            ImageLoader.getInstance().displayImage(meInfo.pic3,new ImageView(this));
//            paths.add(meInfo.pic3);
//            MyLogger.kLog().e(ImageLoader.getInstance().getDiskCache().get(meInfo.pic3).getAbsolutePath());
//            AndroidImagePicker.getInstance().addSelectedImageItem(2,new com.redare.imagepicker.bean.ImageItem(ImageLoader.getInstance().getDiskCache().get(meInfo.pic3).getAbsolutePath(),"",0));
//        }
        if(!TextUtils.isEmpty(meInfo.pic4)){
            ((ImageSecltorAdapter)grid_photo.getAdapter()).getImageList().add(meInfo.pic4);
        }
        ((ImageSecltorAdapter)grid_photo.getAdapter()).notifyDataSetChanged();
//            imageLoadPaths.add(meInfo.pic4);
//            ImageLoader.getInstance().displayImage(meInfo.pic4,new ImageView(this));
//            paths.add(meInfo.pic4);
//            MyLogger.kLog().e(ImageLoader.getInstance().getDiskCache().get(meInfo.pic4).getAbsolutePath());
//            AndroidImagePicker.getInstance().addSelectedImageItem(3,new com.redare.imagepicker.bean.ImageItem(ImageLoader.getInstance().getDiskCache().get(meInfo.pic4).getAbsolutePath(),"",0));
//        }
//        imagePicker.onActivityResult(1001, 0,null);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != 133) {
            if (requestCode == 1025 && data != null) { //删除图片
                String path = data.getStringExtra("path");
                ((ImageSecltorAdapter) grid_photo.getAdapter()).removeData(path);
            }

        }else {
            if (requestCode == REQUEST_QUN && data != null){
                is_owner = Integer.parseInt(data.getStringExtra("isnot"));
            }
        }

    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_qun_have_posted;
    }

    @OnClick({R.id.tv_qunex_upload,R.id.ll_address,R.id.ll_qun_admin})
    public void onClick(View view){
        if(view.getId() == R.id.tv_qunex_upload) {
            if (et_dispcrition.getText().toString().isEmpty()) {
                showTest("请输入群描述！");
                return;
            }

            if (et_phone_number.getText().toString().isEmpty()) {
                showTest("请输入电话信息");
                return;
            }
            if (et_phone_number.getText().toString().trim().length() != 11) {
                showTest("请输入正确的电话信息");
                return;
            }
            if (et_wx_no.getText().toString().isEmpty()) {
                showTest("请输入微信号");
                return;
            }

            if(et_qun_num.getText().toString().isEmpty()){
                showTest("请输入群人数");
                return;
            }
            if(Integer.parseInt(et_qun_num.getText().toString()) > 500){
                showTest("群人数不能超过500人");
                return;
            }
            qunexUpload();
        }else if(view.getId() == R.id.ll_address){
            setPickCity();
        }else if (view.getId() == R.id.ll_qun_admin){
            Intent intentSex = new Intent(QunHavePostedActivity.this, EditUserInfoActivity.class);
            intentSex.putExtra("flag", 10);

            intentSex.putExtra("isnot", is_owner+"");   //1 是  0 否
            startActivityForResult(intentSex, REQUEST_QUN);
        }
    }
    private void setPickCity() {
        final CityPicker cityPicker = new CityPicker.Builder(this).textSize(20)
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
                    tv_address.setText(citySelected[0]);
                } else {
                    tv_address.setText(citySelected[0] + "  " + citySelected[1]);
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
                Toast.makeText(QunHavePostedActivity.this, "已取消", Toast.LENGTH_LONG).show();
            }
        });
    }
    //TODO  上传图片不能兼容已经上传的
    private void qunexUpload() {
        mLoadingAlertDialog.show();
        paths = ((ImageSecltorAdapter) grid_photo.getAdapter()).getImageList();
        if(paths != null && !paths.isEmpty()){
            for(int i = 0 ; i < paths.size() ; i++){
                if(paths.get(i).startsWith("http")){
                    imageLoadPaths.add(paths.get(i));
                    if(imageLoadPaths.size() == paths.size()){
                        uploadQunInfo();
                    }
                    continue;
                }
                getImageUrl(paths.get(i));
            }
        }else {
            uploadQunInfo();
        }

    }

    private void uploadQunInfo() {
        String dispcrition = et_dispcrition.getText().toString().trim();
        dispcrition = EmojiFilter.filterEmoji(dispcrition);
        String valuation = et_valuation.getText().toString().trim();
        dispcrition = EmojiFilter.filterEmoji(dispcrition);
        String phone_number = et_phone_number.getText().toString().trim();
        phone_number = EmojiFilter.filterEmoji(phone_number);
        String wx_no = et_wx_no.getText().toString().trim();
        wx_no = EmojiFilter.filterEmoji(wx_no);
        String remark = et_remark.getText().toString().trim();
        remark = EmojiFilter.filterEmoji(remark);
        String qunNum = et_qun_num.getText().toString().trim();
        qunNum = EmojiFilter.filterEmoji(qunNum);
        if(type == 0) {
            RequestParams params = new RequestParams(RetrofitUtil.API_URL + "song/appWxQunApi/saveQunMe");
            if(qun != null){
                params = new RequestParams(RetrofitUtil.API_URL + "song/appWxQunApi/updateQunMe");
                params.addBodyParameter("qun_id",qun.id+"");
            }
            //添加一条我有群时的图片上传参数
            try {
                JSONArray arr = new JSONArray();
                MyLogger.kLog().e("imageLoadPaths.size()="+ imageLoadPaths.size());
                for(int i = 0 ; i < imageLoadPaths.size() ; i++){
                    MyLogger.kLog().e(imageLoadPaths.get(i));
                    JSONObject json = new JSONObject();
                    json.put("name","content_pic"+(i+1));
                    String path = imageLoadPaths.get(i);
                    path = path.replaceAll(RetrofitUtil.API_URL+RetrofitUtil.PROJECT_URL,"");
                    MyLogger.kLog().e("path="+path);
                    json.put("content",path);
                    arr.put(json);
                }
                params.addBodyParameter("content_text",arr.toString());
            }catch (JSONException e){
                e.printStackTrace();
            }

            params.addBodyParameter("loginId_mzt", (String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""));
            params.addBodyParameter("wx_qun_name", dispcrition);
            params.addBodyParameter("wx_city", city);
            params.addBodyParameter("wx_province", privder);
            params.addBodyParameter("offer", valuation);
            params.addBodyParameter("phone", phone_number);
            params.addBodyParameter("wx_no", wx_no);
            params.addBodyParameter("wx_qun_num", qunNum);
            params.addBodyParameter("is_owner", String.valueOf(is_owner));
            params.addBodyParameter("remark", remark);

            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    try {
                        mLoadingAlertDialog.dismiss();
                        JSONObject json = new JSONObject(s);
                        String state = json.getString("state");
                        String msg = json.getString("msg");
                        if ("1".equals(state)) {
                            showTest("上传成功！");
                            finish();
                        } else {
                            showTest(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable throwable, boolean b) {
                    mLoadingAlertDialog.dismiss();
                }

                @Override
                public void onCancelled(CancelledException e) {

                }

                @Override
                public void onFinished() {

                }
            });
        }else if(type == 1){
            RequestParams params = new RequestParams(RetrofitUtil.API_URL + "song/appWxQunApi/saveQunWant");
            if(want != null){
                params = new RequestParams(RetrofitUtil.API_URL + "song/appWxQunApi/updateQunWant");
                params.addBodyParameter("qun_id",want.id+"");
            }
            params.addBodyParameter("loginId_mzt", (String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""));
            params.addBodyParameter("wx_qun_name", dispcrition);
            params.addBodyParameter("wx_city", city);
            params.addBodyParameter("wx_province", privder);
            params.addBodyParameter("offer", valuation);
            params.addBodyParameter("phone", phone_number);
            params.addBodyParameter("wx_no", wx_no);
            params.addBodyParameter("remark", remark);

            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    try {
                        mLoadingAlertDialog.dismiss();
                        JSONObject json = new JSONObject(s);
                        String state = json.getString("state");
                        String msg = json.getString("msg");
                        if ("1".equals(state)) {
                            showTest("上传成功！");
                            finish();
                        } else {
                            showTest(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable throwable, boolean b) {
                    mLoadingAlertDialog.dismiss();
                }

                @Override
                public void onCancelled(CancelledException e) {

                }

                @Override
                public void onFinished() {

                }
            });
        }
    }

    private void getImageUrl(String imageLoadPath) {
        try{

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
                    public void onNext(final url url) {
                        MyLogger.kLog().e(url);
                        imageLoadPaths.add(url.imgUrl);
                        if (imageLoadPaths.size() == paths.size()) {
                            uploadQunInfo();
                        }
                    }
                });

                addSubscription(sbMyAccount9);
        }catch (Exception e){
            e.printStackTrace();
            Log.e("====url:",e.toString());
        }

    }
}
