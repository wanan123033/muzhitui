package com.nevermore.muzhitui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.nevermore.muzhitui.activity.DynamicPostedActivity;
import com.nevermore.muzhitui.activity.MztProxyProtocolActivity;
import com.nevermore.muzhitui.activity.PopularizeActivity;
import com.nevermore.muzhitui.activity.ThumbChangeActivity;
import com.nevermore.muzhitui.activity.editPhoto.OriginalArticleActivity;

import net.robinx.lib.blur.widget.BlurMaskRelativeLayout;
import net.robinx.lib.blur.widget.BlurMode;

import base.MyLogger;
import base.UIUtils;

/**
 * Created by Administrator on 2018/1/9.
 */

public class MainPopWindow extends PopupWindow {
    private BlurMaskRelativeLayout mBlurMaskRelativeLayout;
    public MainPopWindow(final Context context){
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);
        View view = UIUtils.inflate(context, R.layout.activity_main_icon);
        setContentView(view);
        LinearLayout add_humb_change = view.findViewById(R.id.ll_thumb_change);
        LinearLayout origial_articles =  view.findViewById(R.id.ll_origial_articles);
        LinearLayout add_dynamic = view.findViewById(R.id.ll_add_dynamic);
        LinearLayout ll_sqdl = view.findViewById(R.id.ll_sqdl);
        ImageView tv3 = view.findViewById(R.id.tv_add_cancel);

        mBlurMaskRelativeLayout = getContentView().findViewById(R.id.blur_mask_container);
        MyLogger.kLog().e(mBlurMaskRelativeLayout.getMeasuredWidth() +"---"+mBlurMaskRelativeLayout.getMeasuredHeight());
        mBlurMaskRelativeLayout.blurMode(BlurMode.RENDER_SCRIPT)
                .blurRadius(15);
        add_humb_change.setOnClickListener(new View.OnClickListener() {   //拇指秒变
            @Override
            public void onClick(View v) {
                baseStartActivity(context,ThumbChangeActivity.class);
                dismiss();
            }
        });
        origial_articles.setOnClickListener(new View.OnClickListener() {   //制作文章
            @Override
            public void onClick(View v) {
                baseStartActivity(context,OriginalArticleActivity.class);
                dismiss();
                //  isPowerMe();

            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        add_dynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseStartActivity(context,DynamicPostedActivity.class);
                dismiss();
            }
        });
        ll_sqdl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseStartActivity(context, MztProxyProtocolActivity.class);
                dismiss();
            }
        });

    }

    private void baseStartActivity(Context context, Class activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
    }

    public void addMo() {

        mBlurMaskRelativeLayout.update();
        mBlurMaskRelativeLayout.refresh();
    }
}
