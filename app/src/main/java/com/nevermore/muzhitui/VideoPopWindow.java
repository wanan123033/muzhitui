package com.nevermore.muzhitui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nevermore.muzhitui.activity.DynamicPostedActivity;
import com.nevermore.muzhitui.activity.MyDownVideoActivity;
import com.nevermore.muzhitui.activity.PopularizeActivity;
import com.nevermore.muzhitui.activity.ThumbChangeActivity;
import com.nevermore.muzhitui.activity.editPhoto.OriginalArticleActivity;
import com.nevermore.muzhitui.module.bean.VideoBean;

import base.UIUtils;

/**
 * Created by Administrator on 2018/1/9.
 */

public class VideoPopWindow extends PopupWindow {
    public VideoPopWindow(final Context context, VideoBean bean, View.OnClickListener onClickListener){
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);
        View view = UIUtils.inflate(context, R.layout.pop_video_title);
        setContentView(view);

        TextView tv_videoName = view.findViewById(R.id.tv_videoName);
        tv_videoName.setText(bean.videoTitle);
        tv_videoName.setOnClickListener(onClickListener);
        TextView tv_cannal = view.findViewById(R.id.tv_cannel);
        tv_cannal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
