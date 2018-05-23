package com.nevermore.muzhitui.activity;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.editPhoto.ImageItem;
import com.nevermore.muzhitui.module.bean.Bimp;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.polites.android.GestureImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoDetailActivity extends BaseActivityTwoV {

    public static final String POSTION = "postion";
    public static final String TYPE = "type";
    public static final String PHOTO_TYPE = "photo_type";
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    private ArrayList<View> listViews = null;

    private MyPageAdapter adapter;
    private int count;

    private String photoType;

    public List<ImageItem> bmp = new ArrayList<>();

    public List<String> imageLoad = new ArrayList<String>();

    private LoadingAlertDialog mLoadingAlertDialog;
    private AlertDialog alertDialog3;

    @Override
    public void init() {
        showBack();
        setMyTitle("图片浏览");
        Intent intent = getIntent();
        photoType = intent.getStringExtra(PHOTO_TYPE);
        for (int i = 0; i < Bimp.bmp.size(); i++) {
            bmp.add(Bimp.bmp.get(i));
            imageLoad.add(Bimp.bmp.get(i).imageLoadPath);

        }
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        mViewpager.setOnPageChangeListener(pageChangeListener);
        for (int i = 0; i < bmp.size(); i++) {

            initListViews(bmp.get(i));//
        }

        adapter = new MyPageAdapter(listViews);// 构造adapter
        mViewpager.setAdapter(adapter);// 设置适配器

        int id = intent.getIntExtra(POSTION, 0);

        mViewpager.setCurrentItem(id);
        int type = getIntent().getIntExtra(TYPE,0);
        if(type == 1){
            showRight("删除", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletePhoto();
                }
            });
        }else {
            showRight("下载图片", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savePhoto();
                }
            });
        }
    }

    private void deletePhoto() {
        int position = mViewpager.getCurrentItem();
        Intent intent = new Intent();
        intent.putExtra("position",position);
        intent.putExtra("path",imageLoad.get(position));
        setResult(RESULT_OK,intent);
        finish();
    }

    private void savePhoto() {
        mLoadingAlertDialog.show();
        ThreadManager.getInstance().run(new BaseRunnable() {
            @Override
            public void run() {
                Bitmap bitmap = UIUtils.loadBitmapFromView(mViewpager);
                if (bitmap != null) {
                    try {
                        final String path = ImageUtil.saveImageToGallery(getApplicationContext(), bitmap);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mLoadingAlertDialog.dismiss();
                                showTest("已保存至" + path);

                            }
                        });
                    }catch (SecurityException e) {
                        e.printStackTrace();
                        alertDialog3 = UIUtils.getAlertDialog(PhotoDetailActivity.this, "权限设置", "请允许拇指推APP访问您的通讯录", null, "现在去设置", 0, null, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog3.dismiss();
                                if (Build.VERSION.SDK_INT >= 23) {
                                    requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION","android.permission.WRITE_EXTERNAL_STORAGE","android.permission.READ_EXTERNAL_STORAGE","android.permission.INTERNET"},1024);
                                }
                            }
                        });
                        alertDialog3.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void run(Object... objs) {
                super.run(objs);
            }
        });
//        ThreadManager.getInstance().run(new BaseRunnable() {
//            @Override
//            public void run() {
//                View view = listViews.get(mViewpager.getCurrentItem());
//                ImageItem ii = (ImageItem) view.getTag();
//                File file2 = null;
//                if (ii.imageLoadPath != null) {
//                    if(ii.imageLoadPath.startsWith("http")){
//                        file2 = ImageLoader.getInstance().getDiskCache().get(ii.imageLoadPath);
//                    }else {
//                        file2 = ImageLoader.getInstance().getDiskCache().get(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + ii.imageLoadPath);
//                    }
//                }
//                if(file2 != null){
//                    try{
//                        File file = new File("/storage/emulated/0/"+System.currentTimeMillis()+".png");
//                        file.createNewFile();
//                        FileOutputStream out = new FileOutputStream(file);
//                        FileInputStream in = new FileInputStream(file2);
//                        byte[] buf = new byte[1024];
//                        int len = 0;
//                        while ((len = in.read(buf)) != -1){
//                            out.write(buf,0,len);
//                        }
//                        out.flush();
//                        in.close();
//                        out.close();
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                showTest("已保存系统相册");
//                            }
//                        });
//                    }catch(Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            @Override
//            public void run(Object... objs) {
//                super.run(objs);
//            }
//        });
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_photo_detail;
    }

    private void initListViews(ImageItem ii) {
        if (listViews == null)
            listViews = new ArrayList<View>();
        final GestureImageView img = new GestureImageView(this);// 构造textView对象
        img.setBackgroundColor(0xff000000);
        MyLogger.kLog().e(ii.imageLoadPath);
        if (ii.imageLoadPath != null) {
            MyLogger.kLog().e(photoType + ii.imageLoadPath);

            if(ii.imageLoadPath.startsWith("http")){

                ImageLoader.getInstance().displayImage(ii.imageLoadPath.replace("_s",""), img);
            }else if(photoType == null){
                ImageLoader.getInstance().displayImage(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + ii.imageLoadPath, img);
            }
            if(photoType != null){
                if(ii.imageLoadPath.startsWith("http")){
                    ImageLoader.getInstance().displayImage(ii.imageLoadPath, img);
                }else {
                    ImageLoader.getInstance().displayImage(photoType + ii.imageLoadPath, img);
                }
            }

        }

        img.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));
        img.setTag(ii);
        listViews.add(img);// 添加view// 添加view
    }

    private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        public void onPageSelected(int arg0) {// 页面选择响应函数
            count = arg0;
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {// 滑动中。。。

        }

        public void onPageScrollStateChanged(int arg0) {// 滑动状态改变

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    class MyPageAdapter extends PagerAdapter {

        private ArrayList<View> listViews;// content

        private int size;// 页数

        public MyPageAdapter(ArrayList<View> listViews) {// 构造函数
            // 初始化viewpager的时候给的一个页面
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public void setListViews(ArrayList<View> listViews) {// 自己写的一个方法用来添加数据
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public int getCount() {// 返回数量
            return size;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {// 销毁view对象
            ((ViewPager) arg0).removeView(listViews.get(arg1 % size));
        }

        public void finishUpdate(View arg0) {
        }

        public Object instantiateItem(View arg0, int arg1) {// 返回view对象
            try {
                ((ViewPager) arg0).addView(listViews.get(arg1 % size), 0);

            } catch (Exception e) {
            }
            return listViews.get(arg1 % size);
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }
}
