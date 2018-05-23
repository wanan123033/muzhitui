package com.nevermore.muzhitui.activity.editPhoto;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.Bimp;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.ImageUtil;
import base.network.RetrofitUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoActivity extends BaseActivityTwoV {

    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    private ArrayList<View> listViews = null;

    private MyPageAdapter adapter;
    private int count;

    public List<ImageItem> bmp = new ArrayList<>();

    public List<String> imageLoad = new ArrayList<String>();


    @Override
    public void init() {
        showBack();
        setMyTitle("图片浏览");
        for (int i = 0; i < Bimp.bmp.size(); i++) {
            bmp.add(Bimp.bmp.get(i));
            imageLoad.add(Bimp.bmp.get(i).imageLoadPath);

        }

        mViewpager.setOnPageChangeListener(pageChangeListener);
        for (int i = 0; i < bmp.size(); i++) {

            initListViews(bmp.get(i));//
        }

        adapter = new MyPageAdapter(listViews);// 构造adapter
        mViewpager.setAdapter(adapter);// 设置适配器
        Intent intent = getIntent();
        int id = intent.getIntExtra("ID", 0);
        mViewpager.setCurrentItem(id);
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_photo;
    }

    private void initListViews(ImageItem ii) {
        if (listViews == null)
            listViews = new ArrayList<View>();
        ImageView img = new ImageView(this);// 构造textView对象
        img.setBackgroundColor(0xff000000);
        if (ii.imageLoadPath != null) {
            if(ii.imageLoadPath.startsWith("http")){
                ImageLoader.getInstance().displayImage(ii.imageLoadPath, img, ImageUtil.getInstance().getBaseDisplayOption());
            }else {
                ImageLoader.getInstance().displayImage(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + ii.imageLoadPath, img, ImageUtil.getInstance().getBaseDisplayOption());
            }

        }

        img.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));
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
