package com.nevermore.muzhitui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.nevermore.muzhitui.PageLookActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.PageGPSBean;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import base.BaseActivityTwoV;
import base.MyLogger;
import base.SPUtils;
import base.util.CacheUtil;
import base.view.DragView;
import base.view.VideoGpsView;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/12/12.
 */

public class VideoDownActivity extends BaseActivityTwoV{

    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.dragview)
    DragView dragview;

    @Override
    public void init() {
        showBack();
        setMyTitle("视频下载");
        showRight("我的下载", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseStartActivity(MyDownVideoActivity.class);
            }
        });
        final ClipboardManager myClipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        //GET贴板是否有内容
        ClipData mClipData = myClipboard.getPrimaryClip();
        //获取到内容
        if(mClipData != null) {
            ClipData.Item item = mClipData.getItemAt(0);
            String text = item.getText().toString();
            if (!TextUtils.isEmpty(text)) {
                if(text.indexOf("http") == -1){
                    et_content.setText(text.substring(text.indexOf("http")));
                    et_content.setSelection(et_content.length());
                }else {
                    et_content.setText("");
                }

//                    mEtCard.setText(myClipboard.getText().toString());

            }
        }

        final VideoGpsView videoGpsView = new VideoGpsView(this,5);
        dragview.addDragView(videoGpsView, 550, 850, true, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoGpsView.onClick(v);
            }
        });
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_video_down;
    }

    @OnClick({R.id.btn_down,R.id.iv_delete})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_delete:
                et_content.setText("");
                break;
            case R.id.btn_down:
                String text = et_content.getText().toString().trim();
                text = text.substring(text.indexOf("http"));
                et_content.setText(text);
                if(text.contains("huoshan.com") || text.contains("gifshow.com") || text.contains("xiaokaxiu.com") || text.contains("inke.cn") || text.contains("douyin.com") || text.contains("miaopai.com") || text.contains("weibo.cn") || text.contains("pearvideo.com") || text.contains("xigua") || text.contains("neihanshequ.com") || text.contains("musemuse.cn") || text.contains("baidu.com")) {
                    Intent intent = new Intent(getApplicationContext(), JSVideoDownActivity.class);
                    intent.putExtra(JSVideoDownActivity.PAGE_URL, text);
                    startActivity(intent);
                }else {
                    showTest("暂时不支持该链接");
                }
                break;
        }
    }
}
