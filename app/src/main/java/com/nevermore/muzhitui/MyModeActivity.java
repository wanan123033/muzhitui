package com.nevermore.muzhitui;



import android.util.Log;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.event.PublishedModelEvent;

import org.greenrobot.eventbus.EventBus;

import base.BaseActivityTwoV;


public class MyModeActivity extends BaseActivityTwoV {

    @Override
    public void init() {
        showBack();
        setMyTitle("我的模板");
        int type = getIntent().getIntExtra("type", 0);
        if (type==1) {
            Log.e("add =type=","点击添加模板11");
            int position = getIntent().getIntExtra("position", 0);
            EventBus.getDefault().post(new PublishedModelEvent(1,position));
        }else if (type==2){ Log.e("add =type=","点击添加模板2222");

            EventBus.getDefault().post(new PublishedModelEvent(2));
        }
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_mymode;
    }
}
