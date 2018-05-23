package com.nevermore.muzhitui.activity.rongyun.message.module;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;


import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.rongyun.QuickReplyActivity;


import base.SPUtils;
import io.rong.imkit.RongExtension;

import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;


/**
 * Created by Simone on 2017/3/15.
 */

public class MyPlugin implements IPluginModule {

    @Override
    public Drawable obtainDrawable(Context context) {
        //设置插件 Plugin 图标R.drawable.rc_ext_plugin_quick_reply_selector
        return ContextCompat.getDrawable(context, R.mipmap.ic_muzhitui);
    }

    @Override
    public String obtainTitle(Context context) {
        //设置插件 Plugin 展示文字
        return "快速回复";
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        //示例获取 会话类型、targetId、Context,此处可根据产品需求自定义逻辑，如:开启新的 Activity 等。

        String targetId = rongExtension.getTargetId();
        SPUtils.put(SPUtils.QUICK_REPLY_ID,targetId);
        Intent intent = new Intent(fragment.getActivity(), QuickReplyActivity.class);

       // intent.putExtra("targetId", targetId);
        fragment.getActivity().startActivity(intent);

    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
