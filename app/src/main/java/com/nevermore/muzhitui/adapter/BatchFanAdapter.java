package com.nevermore.muzhitui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.FriendBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;

/**
 * Created by Administrator on 2018/1/19.
 */

public class BatchFanAdapter extends PagerAdapter {
    private List<FriendBean.Friend> friends;
    private Context context;
    private GridAdapter mAdapter;

    private List<View> views = new ArrayList<>();

    public BatchFanAdapter(Context context, List<FriendBean.Friend> friends){
        this.context = context;
        this.friends = friends;
        View view1 = LayoutInflater.from(context).inflate(R.layout.itemvp_batch_fan,null,false);
        View view2 = LayoutInflater.from(context).inflate(R.layout.itemvp_batch_fan,null,false);
        List<Object[]> list = listToArray(friends);
        if(list.size() == 2) {
            initView(view1, list.get(0));
            initView(view2, list.get(1));
            views.clear();
            views.add(view1);
            views.add(view2);
        }else {
            initView(view1, list.get(0));
            views.clear();
            views.add(view1);
        }
    }

    private void initView(View view, Object[] objs) {
        GridView rv_batch = view.findViewById(R.id.rv_batch);
        rv_batch.setAdapter(new GridAdapter(context,(List)Arrays.asList(objs)));
//        mAdapter = new CommonAdapter<Object>(context,R.layout.item_batch_fan, Arrays.asList(objs)) {
//            @Override
//            public void convert(ViewHolder holder, Object o) {
//                FriendBean.Friend friend = (FriendBean.Friend) o;
//                ImageLoader.getInstance().displayImage(friend.headimg, (ImageView) holder.getView(R.id.iv_headimg));
//                holder.setText(R.id.tv_name,friend.wx_name);
//            }
//        };
//        rv_batch.setAdapter(mAdapter);
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    private static class GridAdapter extends BaseAdapter{
        private List<FriendBean.Friend> friends;
        private Context context;

        public GridAdapter(Context context, List<FriendBean.Friend> friends){
            this.context = context;
            this.friends = friends;
        }
        @Override
        public int getCount() {
            return friends.size();
        }

        @Override
        public Object getItem(int position) {
            return friends.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.item_batch_fan,null,false);
            }
            FriendBean.Friend friend = (FriendBean.Friend) getItem(position);
            ImageLoader.getInstance().displayImage(friend.headimg, (ImageView) convertView.findViewById(R.id.iv_headimg));
            ((TextView)convertView.findViewById(R.id.tv_name)).setText(friend.wx_name);
            return convertView;
        }
    }

    private List<Object[]> listToArray(List list){
        if(list.size() < 12){
            List<Object[]> objss = new ArrayList<>();
            objss.add(list.toArray());
            return objss;
        }else {
            Object[] array = list.toArray();
            Object[] array1 = new Object[12];
            Object[] array2 = new Object[array.length - 12];
            for (int i = 0; i < array1.length; i++) {
                array1[i] = array[i];
            }
            for (int i = 0; i < array2.length; i++) {
                array2[i] = array[i + array1.length - 1];
            }
            List objs = new ArrayList();
            objs.add(array1);
            objs.add(array2);
            return objs;
        }
    }
}
