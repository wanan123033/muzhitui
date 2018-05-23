package com.nevermore.muzhitui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.nevermore.muzhitui.MainActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.rongyun.MztRongContext;
import com.nevermore.muzhitui.adapter.ImageSecltorAdapter;
import com.nevermore.muzhitui.fragment.TabMyFragment;
import com.nevermore.muzhitui.module.bean.url;
import com.nevermore.muzhitui.module.network.WorkService;

import org.greenrobot.eventbus.EventBus;
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
import base.UIUtils;
import base.network.RetrofitUtil;
import base.thread.BaseRunnable;
import base.thread.ThreadManager;
import base.util.EmojiFilter;
import base.view.DragView;
import base.view.LoadingAlertDialog;
import base.view.VideoGpsView;
import butterknife.BindView;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Administrator on 2017/9/12.
 * 动态发布
 */
//TODO 小图_s 大图没有
public class DynamicPostedActivity extends BaseActivityTwoV{

    public static final String POSTED_STATE = "动态发表成功！";

    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.grid_photo)
    GridView grid_photo;
    @BindView(R.id.dragview)
    DragView dragview;

    private LoadingAlertDialog mLoadingAlertDialog;

    public List<String> imageLoadPaths = new ArrayList<>();
    public String[] imageUrls =null;

    private boolean flag = true;
    @Override
    public void init() {
        setMyTitle("动态发布");
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        showRight("发布", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postedDynamic();
            }
        });
        showBack();

        grid_photo.setAdapter(new ImageSecltorAdapter(this,9,new ArrayList<String>(), R.layout.item_add_image));

        final VideoGpsView videoGpsView = new VideoGpsView(this,1);
        dragview.addDragView(videoGpsView, 550, 850, true, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoGpsView.onClick(v);
            }
        });
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_dynamic_posted;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1025 && data != null){ //删除图片
            String path = data.getStringExtra("path");
            ((ImageSecltorAdapter)grid_photo.getAdapter()).removeData(path);
        }


    }
    //需要线程控制
    AlertDialog alertDialog;
    private void postedDynamic(){
        mLoadingAlertDialog.show();
        hideSoftInput();

        final int memberstate = (int) SPUtils.get(SPUtils.KEY_MEMBER_STATE, 0);//会员状态  1是年费2是终身3不是会员
        final int IsExpire = (int) SPUtils.get(SPUtils.KEY_ISEXPIRE, 0);//是否已过期  1是会员已过期 0是正常
        int dynamicNum = (int) SPUtils.get(SPUtils.DYNAMIC_NUM,0);
        String message = "您目前还不是会员，无法发布动态；请购买会员后即可发布动态";
        String clickmessage = "购买会员";
        if (IsExpire == 1) {
            message = "您的会员已到期，无法发布动态；请续费后即可恢复正常使用";
            clickmessage = "续费";
        }

        if (((memberstate == 3) || (IsExpire == 1)) && dynamicNum > 10) {
            if(dynamicNum > 10){
                message = "您的动态试用次数已经用完，是否购买会员？";
                clickmessage = "购买会员";
            }
            alertDialog = UIUtils.getAlertDialog(DynamicPostedActivity.this, "提示信息", message, "取消", clickmessage, 0, new View.OnClickListener() {
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
        }else {
            ThreadManager.getInstance().run(new BaseRunnable() {
                @Override
                public void run() {
                    imageLoadPaths = ((ImageSecltorAdapter) grid_photo.getAdapter()).getImageList();
                    DisplayMetrics dm = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                    for (int i = 0; i < imageLoadPaths.size(); i++) {
                        Bitmap bitmap = BitmapFactory.decodeFile(imageLoadPaths.get(i));
                        MyLogger.kLog().e(bitmap.getHeight() + "-----" + dm.heightPixels);
                        if (bitmap.getHeight() > bitmap.getWidth() * 2 || bitmap.getHeight() > dm.heightPixels * 3) {
                            bitmap.recycle();
                            bitmap = null;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mLoadingAlertDialog.dismiss();
                                    Toast.makeText(DynamicPostedActivity.this, "您有一张长图，发布动态失败！", Toast.LENGTH_LONG).show();
                                }
                            });
                            return;
                        } else {
                            bitmap.recycle();
                            bitmap = null;
                        }
                    }

                    if (imageLoadPaths == null || imageLoadPaths.isEmpty()) {
                        posted();
                        return;
                    }
                    imageUrls = new String[imageLoadPaths.size()];
                    for (int i = 0; i < imageLoadPaths.size(); i++) {
                        getImageUrl(i, imageLoadPaths.get(i));
                    }
                }

            });
        }


    }

    private void posted() {
        String content = et_content.getText().toString();
        content = EmojiFilter.filterEmoji(content);
        if(imageLoadPaths.isEmpty() && TextUtils.isEmpty(content)){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showTest("请输入动态内容");
                    mLoadingAlertDialog.dismiss();
                }
            });
            return;
        }

        RequestParams params = new RequestParams(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + "appPageDtApi/save");
        params.setConnectTimeout(100000);
        params.addBodyParameter("loginId_mzt", (String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""));
        params.addBodyParameter("content",content);
        try {
            JSONArray array = new JSONArray();
            for(int i = 0 ; i < imageUrls.length ; i++){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name","content_pic"+i);
                jsonObject.put("content",imageUrls[i]);
                array.put(jsonObject);
            }
            params.addBodyParameter("content_text",array.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        x.http().post(params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String s) {
                try {
                    mLoadingAlertDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(s);
                    String state = jsonObject.getString("state");
                    String msg = jsonObject.getString("msg");
                    if("1".equals(state)){
                        showTest(POSTED_STATE);
                        SPUtils.put(SPUtils.DYNAMIC_NUM,((int)SPUtils.get(SPUtils.DYNAMIC_NUM,0) + 1));
                        EventBus.getDefault().post(POSTED_STATE);
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
                MyLogger.kLog().e(throwable);
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onCancelled(CancelledException e) {
                MyLogger.kLog().e(e);
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onFinished() {
                mLoadingAlertDialog.dismiss();
            }
        });
    }
    private int maxImgNum = 0;
    private synchronized void getImageUrl(final int index, final String imageLoadPath) {
       try{
           Subscription sbMyAccount9 = wrapObserverWithHttp(WorkService.getWorkService().uploadDTImg((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""), ImageUtil.getInstance().wrapUploadImgRequest(ImageUtil.scal(imageLoadPath)))
            ).subscribe(new Subscriber<url>() {
                @Override
                public void onCompleted() {

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
                    MyLogger.kLog().e(url.toString());
                    if(url.imgUrl != null && !url.imgUrl.isEmpty()) {
                        imageUrls[index] = url.imgUrl;
                        maxImgNum ++;
                        flag = true;
                    }else {
                        flag = false;
                        showTest("图片上传失败");
                        mLoadingAlertDialog.dismiss();
                    }
                    if (flag && maxImgNum == imageLoadPaths.size()) {
                        posted();
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
