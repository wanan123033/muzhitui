package com.nevermore.muzhitui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nevermore.muzhitui.activity.rongyun.MztRongContext;
import com.nevermore.muzhitui.activity.rongyun.broadcast.BroadcastManager;
import com.nevermore.muzhitui.module.sqllite.DBManager;

import java.io.File;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.UIUtils;
import base.util.DataCleanManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hehe on 2016/6/4.
 */
public class SettingActivity extends BaseActivityTwoV {
    @BindView(R.id.flytCache)
    FrameLayout flytCache;
    @BindView(R.id.flytAboutMe)
    FrameLayout flytAboutMe;
    @BindView(R.id.flytService)
    FrameLayout flytService;

    @BindView(R.id.flytQuit)
    FrameLayout flytQuit;
    @BindView(R.id.tvCurrentVersion)
    TextView mTvCurrentVersion;
    private DBManager mgr;

    @Override
    public void init() {
        showBack();
        setMyTitle("设置");
        //初始化DBManager
        mgr = new DBManager(this);

        alertDialog = UIUtils.getAlertDialog(this, "", "确认退出", "取消", "确认", 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.remove(SPUtils.KEY_WXUNIONID);
                SPUtils.remove(SPUtils.KEY_WXOPENID);
              //清除用户信息
                SPUtils.remove(SPUtils.KEY_GET_TOKEN);
                SPUtils.remove(SPUtils.KEY_GET_ID);
                SPUtils.remove(SPUtils.GET_LOGIN_ID);
                SPUtils.remove(SPUtils.KEY_WX_CITY);
                //SPUtils.remove(SPUtils.KEY_PHONE_NUMBER);
                SPUtils.remove(SPUtils.KEY_PASSWORD);
                SPUtils.remove(SPUtils.KEY_USERNAME);
                SPUtils.remove(SPUtils.KEY_GET_ID);
                SPUtils.remove(SPUtils.KEY_GET_JlOGIN_ID);
                SPUtils.remove(SPUtils.NEWFRIEND_NUM);

                mgr.deleteTableByDBNameAll();//删除用户信息表 所有的表删除
                mgr.deleteDBByName();//删除用户信息数据库


                alertDialog.dismiss();//关闭对话框
                finish();
                BroadcastManager.getInstance(SettingActivity.this).sendBroadcast(MztRongContext.EXIT);


            }
        });

        clearAppDialog = UIUtils.getAlertDialog(this, "是否清除？", "清除缓存会清除所有数据并返回到登录界面。", "取消", "确认", 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAppDialog.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAppDialog.dismiss();
                DataCleanManager.cleanApplicationData(getApplicationContext(),new File(Environment.getExternalStorageDirectory(), "Boohee").getAbsolutePath());
                showTest("已清除缓存");
            }
        });
        mTvCurrentVersion.setText("当前版本："+getAppVersion());
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_setting;
    }

    private AlertDialog alertDialog,clearAppDialog;

    @OnClick({R.id.flytCache, R.id.flytAboutMe, R.id.flytService, R.id.flytQuit})
    public void onClick(View view) {
        Intent intent = new Intent(this, PageLookActivity.class);
        switch (view.getId()) {
            case R.id.flytCache:
                clearAppDialog.show();
                break;
            case R.id.flytAboutMe:
                intent.putExtra(PageLookActivity.KEY_URL, "http://www.muzhitui.cn/song/wx/aboutme.html");
                startActivity(intent);
                break;
            case R.id.flytService:
                intent.putExtra(PageLookActivity.KEY_URL, "http://www.muzhitui.cn/song/wx/serverterm.html");
                startActivity(intent);
                break;
            case R.id.flytQuit:
                alertDialog.show();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //应用的最后一个Activity关闭时应释放DB
        mgr.closeDB();
    }
}
