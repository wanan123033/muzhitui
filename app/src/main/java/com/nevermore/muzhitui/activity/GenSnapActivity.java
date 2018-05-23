package com.nevermore.muzhitui.activity;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.MyInfo;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import base.BaseActivityTwoV;
import base.ImageUtil;
import base.SPUtils;
import base.UIUtils;
import base.network.RetrofitUtil;
import base.thread.BaseRunnable;
import base.thread.ThreadManager;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/11/7.
 */

public class GenSnapActivity extends BaseActivityTwoV {

    @BindView(R.id.rel_view)
    RelativeLayout rel_view;
    @BindView(R.id.civ_topic)
    CircleImageView civ_topic;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.iv_head_qr)
    ImageView mIvHeadQr;

    private LoadingAlertDialog mLoadingAlertDialog;
    @Override
    public void init() {
        showBack();
        setMyTitle("海报");
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        showRight("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSnapshot();
            }
        });
        loadData();
    }

    private void loadData() {
        String headimg = RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + SPUtils.get(SPUtils.KEY_TOPIC,"");
        String username = (String) SPUtils.get(SPUtils.KEY_USER_NAME,"");
        ImageLoader.getInstance().displayImage(headimg,civ_topic);
        tv_name.setText(username);
        String path = RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + SPUtils.get(SPUtils.qr_code_img,"");
        ImageLoader.getInstance().displayImage(path, mIvHeadQr, ImageUtil.getInstance().getBaseDisplayOption());
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_gen_snap;
    }
    private void saveSnapshot() {
        mLoadingAlertDialog.show();
        ThreadManager.getInstance().run(new BaseRunnable() {
            @Override
            public void run() {
                Bitmap bitmap = UIUtils.loadBitmapFromView(rel_view);
                if(bitmap != null){
                    try{
                        final String path = ImageUtil.saveImageToGallery(getApplicationContext(),bitmap);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showTest("已保存至"+path);
                                mLoadingAlertDialog.dismiss();
                                SPUtils.put(SPUtils.KEY_POP_SNAP,path);
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
    }
}
