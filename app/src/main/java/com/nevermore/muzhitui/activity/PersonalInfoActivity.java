package com.nevermore.muzhitui.activity;

import android.app.Activity;
import android.content.Intent;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import android.widget.PopupWindow;
import android.widget.TextView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.event.DataRefreshEvent;
import com.nevermore.muzhitui.module.bean.Code;
import com.nevermore.muzhitui.module.bean.FindUserInfoById;

import com.nevermore.muzhitui.module.network.WorkService;
import com.nevermore.muzhitui.module.sqllite.DBManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import base.BaseActivityTwoV;
import base.ImageUtil;
import base.SPUtils;
import base.UIUtils;
import base.helper.PhotoActionHelper;

import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by gk on 2016/3/8.
 * 我的个人信息查看页面
 */
public class PersonalInfoActivity extends BaseActivityTwoV implements View.OnClickListener {
    public static final int REQUEST_CLIP_IMAGE = 2028;
    public static final int REQUEST_NAME = 2;
    public static final int REQUEST_WXID = 3;
    public static final int REQUEST_SEX = 4;
    public static final int REQUEST_ADDRESS = 5;
    public static final int REQUEST_PHONE = 6;
    public static final int REQUEST_MP_DES = 7;
    @BindView(R.id.ivHead)
    ImageView mIvHead;
    @BindView(R.id.flytHead)
    FrameLayout mFlytHead;
    @BindView(R.id.flytName)
    FrameLayout mFlytName;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvPersonalInfoId)
    TextView mTvPersonalInfoId;
    @BindView(R.id.flPersonalInfoId)
    FrameLayout mFlPersonalInfoId;
    @BindView(R.id.tvPersonalInfoWXid)
    TextView mTvPersonalInfoWXid;
    @BindView(R.id.flPersonalInfoWXid)
    FrameLayout mFlPersonalInfoWXid;
    @BindView(R.id.tvPersonalInfoPhone)
    TextView mTvPersonalInfoPhone;
    @BindView(R.id.flPersonalInfoPhone)
    FrameLayout mFlPersonalInfoPhone;
    @BindView(R.id.flPersonalInfoQR)
    FrameLayout mFlPersonalInfoQR;
    @BindView(R.id.tvPersonalInfoSex)
    TextView mTvPersonalInfoSex;
    @BindView(R.id.flPersonalInfoSex)
    FrameLayout mFlPersonalInfoSex;
    @BindView(R.id.tvPersonalInfoAddress)
    TextView mTvPersonalInfoAddress;
    @BindView(R.id.flPersonalInfoAddress)
    FrameLayout mFlPersonalInfoAddress;
    @BindView(R.id.tvPersonalInfoProduce)
    TextView mTvPersonalInfoProduce;
    @BindView(R.id.flPersonalInfoProduce)
    FrameLayout mFlPersonalInfoProduce;
    private PopupWindow mOptImgPopWindow;
    private LoadingAlertDialog mLoadingAlertDialog;
    private FindUserInfoById.LoginBean mUserinfo;
    private String mOutputPath;
    private String mPath;

    private GalleryFinal.OnHanlderResultCallback mOnhanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null && !resultList.isEmpty()) {
                final Uri uri = Uri.parse("file:/" + resultList.get(0).getPhotoPath());
                mOptImgPopWindow.dismiss();
                mOutputPath = new File(UIUtils.getExternalCacheDir(), System.currentTimeMillis() + ".png").getPath();
                PhotoActionHelper.clipImage(PersonalInfoActivity.this).input(resultList.get(0).getPhotoPath()).output(mOutputPath).maxOutputWidth(49 * 3)
                        .requestCode(REQUEST_CLIP_IMAGE).start();
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {

        }
    };

    @Override
    public void init() {
        setMyTitle("个人信息");
        showBack();

        initPpwWindow();
        mLoadingAlertDialog = new LoadingAlertDialog(this);

        loadData();
    }

    private void loadData() {
        mLoadingAlertDialog.show();
        int id = (int) SPUtils.get(SPUtils.KEY_GET_ID, 0);
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().getUserById((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),id + "")).subscribe(new Subscriber<FindUserInfoById>() {
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
            public void onNext(FindUserInfoById userInfo) {
                mUserinfo = userInfo.getLogin();
                Log.e("TAG",mUserinfo.toString());
                if ("1".equals(userInfo.getState())) {

                    Log.e("myAccount000", userInfo.getLogin().getHeadimg() + "");
                    ImageLoader.getInstance().displayImage(userInfo.getLogin().getHeadimg(), mIvHead, ImageUtil.getInstance().getCircleDisplayOption());
                    tvName.setText(userInfo.getLogin().getUser_name());



                    mTvPersonalInfoId.setText(userInfo.getLogin().getId() + "");
                    if (userInfo.getLogin().getWechat() != null && !userInfo.getLogin().getWechat().equals("")) {
                        mTvPersonalInfoWXid.setText(userInfo.getLogin().getWechat());
                    }
                    if (userInfo.getLogin().getUser_phone() != null) {
                        mTvPersonalInfoPhone.setText(userInfo.getLogin().getUser_phone());

                        //SPUtils.put(SPUtils.KEY_PHONE_NUMBER, userInfo.getLogin().getUser_phone());
                    }
                    if (userInfo.getLogin().getWx_sex() == 1) {
                        mTvPersonalInfoSex.setText("男");
                    } else if (userInfo.getLogin().getWx_sex() == 2) {
                        mTvPersonalInfoSex.setText("女");
                    } else {
                        mTvPersonalInfoSex.setText("暂无");
                    }

                    String country="未知",province="未知",city="未知";
                    if (!TextUtils.isEmpty(userInfo.getLogin().getWx_country())){
                        country=userInfo.getLogin().getWx_country();
                    }
                    if (!TextUtils.isEmpty(userInfo.getLogin().getWx_province())){
                        province=userInfo.getLogin().getWx_province();
                    }
                    if (!TextUtils.isEmpty(userInfo.getLogin().getWx_city())){
                        city=userInfo.getLogin().getWx_city();
                    }

                    mTvPersonalInfoAddress.setText(country + " • " + province + " • " + city);

                    if (userInfo.getLogin().getMp_desc() != null && !userInfo.getLogin().getMp_desc().equals("")) {
                        mTvPersonalInfoProduce.setText(userInfo.getLogin().getMp_desc().toString());
                    }

                    Log.e("image:", userInfo.getLogin().getHeadimg());

                } else {
                    showTest(mServerEror);
                    showEmptyView(0);
                }

            }
        });
        addSubscription(sbMyAccount);
    }

    @Override
    public int createSuccessView() {

        return R.layout.activity_personal_info;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK
                && data != null
                && (requestCode == REQUEST_CLIP_IMAGE)) {
            String path = PhotoActionHelper.getOutputPath(data);
            if (path != null) {
               /* Bitmap bitmap = BitmapFactory.decodeFile(path);
                mIvHead.setImageBitmap(bitmap); // 这里注意 别内存溢出*/
                final Uri uri = Uri.parse("file:/" + path);
                mPath = path;
                l("字符串是否相等s =  " + (mPath == path));

                uploadImg();
                ImageLoader.getInstance().displayImage(uri.toString(), mIvHead, ImageUtil.getInstance().getCircleDisplayOption());

            }
            return;
        } else if (resultCode == Activity.RESULT_OK
                && data != null
                && (requestCode == REQUEST_NAME)) {
            String name = data.getStringExtra(SPUtils.KEY_WXNICKNAME);
            mUserinfo.setUser_name(name);
            tvName.setText(name);
            SPUtils.put(SPUtils.KEY_USER_NAME,name);
            EventBus.getDefault().post(new DataRefreshEvent());
        }else if (resultCode == Activity.RESULT_OK
                && data != null
                && (requestCode == REQUEST_WXID)) {
            String wxid = data.getStringExtra("wxid");
            mUserinfo.setWechat(wxid);
            mTvPersonalInfoWXid.setText(wxid);
        } else if (resultCode == Activity.RESULT_OK
                && data != null
                && (requestCode == REQUEST_SEX)) {
            String sex = data.getStringExtra("sex");
            if (sex.equals("1")) {
                mTvPersonalInfoSex.setText("男");
                mUserinfo.setWx_sex(Integer.parseInt(sex));
            } else if (sex.equals("2")) {
                mTvPersonalInfoSex.setText("女");
                mUserinfo.setWx_sex(Integer.parseInt(sex));
            } else {
                mTvPersonalInfoSex.setText("暂无");
               // mUserinfo.setWx_sex(Integer.parseInt(sex));
            }

        } else if (resultCode == Activity.RESULT_OK
                && data != null
                && (requestCode == REQUEST_ADDRESS)) {
            String address = data.getStringExtra("address");
           String [] add= address.split(" • ");
            mUserinfo.setWx_country(add[0]);
            mUserinfo.setWx_province(add[1]);
            mUserinfo.setWx_city(add[2]);
            Log.e("resultCode address:", address + "");
            mTvPersonalInfoAddress.setText(address);
        } else if (resultCode == Activity.RESULT_OK
                && data != null
                && (requestCode == REQUEST_PHONE)) {
            String phone = data.getStringExtra("phone");
            mUserinfo.setUser_phone(phone);
            mTvPersonalInfoPhone.setText(phone);
        } else if (resultCode == Activity.RESULT_OK
                && data != null
                && (requestCode == REQUEST_MP_DES)) {
            String mp_desc = data.getStringExtra("mp_desc");
            mUserinfo.setMp_desc(mp_desc);
            mTvPersonalInfoProduce.setText(mp_desc);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initPpwWindow() {
        //设置contentView
        View contentView = UIUtils.inflate(PersonalInfoActivity.this, R.layout.ppw_mysetting);
        mOptImgPopWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mOptImgPopWindow.setContentView(contentView);
        mOptImgPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mOptImgPopWindow.setOutsideTouchable(true);
        //设置各个控件的点击响应
        TextView tv1 = (TextView) contentView.findViewById(R.id.tvCamera);
        TextView tv2 = (TextView) contentView.findViewById(R.id.tvPhotos);
        TextView tv3 = (TextView) contentView.findViewById(R.id.tvCancle);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
    }

    private void uploadImg() {
        if (!TextUtils.isEmpty(mPath)) {
            File file = new File(mPath);
            mLoadingAlertDialog.show();
            Log.e("Bimp.drr  imgFile:", mPath);
            Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().updateUserInfo((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),1, ImageUtil.getInstance().wrapUploadImgRequest(file))).subscribe(new Subscriber<Code>() {
                @Override
                public void onCompleted() {
                    mLoadingAlertDialog.dismiss();
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    mLoadingAlertDialog.dismiss();
                    showTest("服务器连接失败");
                }

                @Override
                public void onNext(Code code) {
                    if (code.getState().equals("1")) {
                        l("imgeUploiad = " + code.getState());
                        showTest("修改成功");
//                        finish();
                        EventBus.getDefault().post(new DataRefreshEvent());
                    } else {

                        showTest(code.getMsg());
                    }

                }
            });
            addSubscription(sbMyAccount);
        } else {
            showTest("未修改图片");
        }
    }

    @OnClick({ R.id.flytHead, R.id.flytName,R.id.flPersonalInfoId, R.id.flPersonalInfoWXid, R.id.flPersonalInfoPhone, R.id.flPersonalInfoQR, R.id.flPersonalInfoSex, R.id.flPersonalInfoAddress, R.id.flPersonalInfoProduce})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.flytHead:
                mOptImgPopWindow.showAtLocation(mFlytHead, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.tvCamera:
                ImageUtil.getInstance().openCamera(mOnhanlderResultCallback);
                break;
            case R.id.tvPhotos:
                ImageUtil imageUtil = ImageUtil.getInstance();
                imageUtil.chooseImage("开始制作",mOnhanlderResultCallback, 1);
                break;
            case R.id.tvCancle:
                mOptImgPopWindow.dismiss();
                break;
            case R.id.flytName:
                Intent intentName = new Intent(PersonalInfoActivity.this, EditUserInfoActivity.class);
                intentName.putExtra("flag", 2);
                intentName.putExtra("username", mUserinfo.getUser_name());
                startActivityForResult(intentName, REQUEST_NAME);

                break;

            case R.id.flPersonalInfoWXid:
                Intent intentWXid = new Intent(PersonalInfoActivity.this, EditUserInfoActivity.class);
                intentWXid.putExtra("flag", 3);
                intentWXid.putExtra("wxid", mUserinfo.getWechat());
                startActivityForResult(intentWXid, REQUEST_WXID);
                break;
            case R.id.flPersonalInfoQR:
                baseStartActivity(UserInfoQRCode.class);
                break;
            case R.id.flPersonalInfoSex:
                Intent intentSex = new Intent(PersonalInfoActivity.this, EditUserInfoActivity.class);
                intentSex.putExtra("flag", 4);

                intentSex.putExtra("sex", mUserinfo.getWx_sex() + "");
                startActivityForResult(intentSex, REQUEST_SEX);
                break;
            case R.id.flPersonalInfoAddress:
                Intent intentAddress = new Intent(PersonalInfoActivity.this, CityPickerActivity.class);
                intentAddress.putExtra("flag", 5);
                intentAddress.putExtra("address", mUserinfo.getWx_country() + " • "+mUserinfo.getWx_province() +" • " + mUserinfo.getWx_city());
                startActivityForResult(intentAddress, REQUEST_ADDRESS);

                break;
            case R.id.flPersonalInfoPhone:
                Intent intentPhone = new Intent(PersonalInfoActivity.this, EditUserInfoActivity.class);
                intentPhone.putExtra("flag", 6);
                intentPhone.putExtra("phone", mUserinfo.getUser_phone());
                startActivityForResult(intentPhone, REQUEST_PHONE);
                break;

            case R.id.flPersonalInfoProduce:
                Intent intentProduce = new Intent(PersonalInfoActivity.this, EditUserInfoActivity.class);
                intentProduce.putExtra("flag", 7);
                intentProduce.putExtra("mp_desc", mUserinfo.getMp_desc());
                startActivityForResult(intentProduce, REQUEST_MP_DES);

                break;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

}
