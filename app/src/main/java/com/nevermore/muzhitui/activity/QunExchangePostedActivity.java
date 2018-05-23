package com.nevermore.muzhitui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.CursorJoiner;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.nevermore.muzhitui.EventBusContanct;
import com.nevermore.muzhitui.MainActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.editPhoto.ImageItem;
import com.nevermore.muzhitui.activity.rongyun.MztRongContext;
import com.nevermore.muzhitui.adapter.ImageSecltorAdapter;
import com.nevermore.muzhitui.fragment.TabMyFragment;
import com.nevermore.muzhitui.module.bean.QunChangeBean;
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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import base.BaseActivityTwoV;
import base.ImageUtil;
import base.MyLogger;
import base.SPUtils;
import base.UIUtils;
import base.helper.PhotoActionHelper;
import base.network.RetrofitUtil;
import base.util.EmojiFilter;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Administrator on 2017/9/25.
 */

public class QunExchangePostedActivity extends BaseActivityTwoV{

    public static final String DATA = "data";
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_wxno)
    EditText et_wxno;
    @BindView(R.id.grid_photo)
    GridView grid_photo;
    @BindView(R.id.tv_qunex_upload)
    TextView tv_qunex_upload;

    private QunChangeBean.QunChange change;

    public List<String> imageLoadPaths = new ArrayList<>();
    private LoadingAlertDialog mLoadingAlertDialog;
    @Override
    public void init() {
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        setMyTitle("上传二维码");
        showBack();

        change = (QunChangeBean.QunChange) getIntent().getSerializableExtra(DATA);
        ImageSecltorAdapter adapter = new ImageSecltorAdapter(this, 1, new ArrayList<String>(), R.layout.item_add_image2);
        if(change != null){
            tv_qunex_upload.setText("确认修改");
            et_name.setText(change.wx_qun_name);
            et_wxno.setText(change.wx_no);
            imageLoadPaths.clear();
            imageLoadPaths.add(change.pic1);
            MyLogger.kLog().e(change.pic1);
            List<String> arr = new ArrayList<>();
            if(ImageLoader.getInstance().getDiskCache().get(change.pic1) != null) {
                arr.add(ImageLoader.getInstance().getDiskCache().get(change.pic1).getAbsolutePath());
                adapter.setImageList(arr);
            }
        }
        adapter.setCrop(true);
        grid_photo.setAdapter(adapter);
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 20){
                    et_name.setText(s.subSequence(0,20));
                    et_name.setSelection(20);
                }else {
                    
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1025 && resultCode ==RESULT_OK && data != null){
            ((ImageSecltorAdapter)grid_photo.getAdapter()).getImageList().clear();
            ((ImageSecltorAdapter)grid_photo.getAdapter()).notifyDataSetChanged();
        }

        if(requestCode == ImageSecltorAdapter.CLIP_IMAGEAll && resultCode ==RESULT_OK && data != null) {
            String outputPath = PhotoActionHelper.getOutputPath(data);
            ((ImageSecltorAdapter)grid_photo.getAdapter()).getImageList().clear();
            ((ImageSecltorAdapter)grid_photo.getAdapter()).getImageList().add(outputPath);
            ((ImageSecltorAdapter)grid_photo.getAdapter()).notifyDataSetChanged();
        }
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_qun_exchange_posted;
    }

    private AlertDialog alertDialog;
    @OnClick(R.id.tv_qunex_upload)
    public void onClick(View view){
        final int memberstate = (int) SPUtils.get(SPUtils.KEY_MEMBER_STATE, 0);//会员状态  1是年费2是终身3不是会员
        final int IsExpire = (int) SPUtils.get(SPUtils.KEY_ISEXPIRE, 0);//是否已过期  1是会员已过期 0是正常

        //判断会员是否可分享
        String message = "您目前还不是会员，无法发布群互换；请购买会员后即可发布群互换";
        String clickmessage = "购买会员";
        if (IsExpire == 1) {
            message = "您的会员已到期，无法发布群互换；请续费后即可恢复正常使用";
            clickmessage = "续费";
        }

        if ((memberstate == 3) || (IsExpire == 1)) {
            alertDialog = UIUtils.getAlertDialog(QunExchangePostedActivity.this, "提示信息", message, "取消", clickmessage, 0, new View.OnClickListener() {
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

        if(et_name.getText().toString().isEmpty()) {
            showTest("请输入群名称");
            return;
        }
        if (et_wxno.getText().toString().isEmpty()){
            showTest("请输入您的微信号");
            return;
        }
        List<String> imageList = ((ImageSecltorAdapter) grid_photo.getAdapter()).getImageList();
        if(imageList == null || imageList.isEmpty() || scanningImage(imageList.get(0)) == null){
            showTest("请选择一张二维码");
            return;
        }else {
            getImageUrl(imageList.get(0));
        }

    }



    private void qunexUpload() {
        String name = et_name.getText().toString().trim();
        name = EmojiFilter.filterEmoji(name);
        String wxno = et_wxno.getText().toString().trim();
        wxno = EmojiFilter.filterEmoji(wxno);
        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            jsonObject.put("name","content_pic1");
            jsonObject.put("content",imageLoadPaths.get(0));
            array.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(RetrofitUtil.API_URL + "song/appWxQunApi/saveQunChange");
        if(change != null){
            params = new RequestParams(RetrofitUtil.API_URL + "song/appWxQunApi/updateQunChange");
            params.addBodyParameter("qun_id",change.id+"");
        }
        MyLogger.kLog().e(array.toString());
        params.addBodyParameter("content_text",array.toString());
        params.addBodyParameter("loginId_mzt", (String) SPUtils.get(SPUtils.GET_LOGIN_ID,""));
        params.addBodyParameter("wx_qun_name",name);
        params.addBodyParameter("wx_no",wxno);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                try {
                    JSONObject json = new JSONObject(s);
                    String state = json.getString("state");
                    String msg = json.getString("msg");
                    if("1".equals(state)){
                        showTest("上传成功！");
                        EventBus.getDefault().post(EventBusContanct.getInstance(EventBusContanct.REFRESH_QUN_EXCHANGE));
                        finish();
                    }else{
                        showTest(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

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
                    imageLoadPaths.clear();
                    imageLoadPaths.add(url.imgUrl);
                    qunexUpload();
                }
            });

            addSubscription(sbMyAccount9);
        }catch (Exception e){
            e.printStackTrace();
            Log.e("====url:",e.toString());
        }
    }

    protected Result scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        // DecodeHintType 和EncodeHintType
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 设置二维码内容的编码
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        Bitmap scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小

        int sampleSize = (int) (options.outHeight / (float) 200);

        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);
        int width = scanBitmap.getWidth();
        int height = scanBitmap.getHeight();
        int[] pixels = new int[width * height];
        scanBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        RGBLuminanceSource source = new RGBLuminanceSource(width,height,pixels);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            scanBitmap.recycle();
            return reader.decode(bitmap1, hints);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }
}
