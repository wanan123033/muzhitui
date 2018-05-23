package com.nevermore.muzhitui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.VedioText;
import com.nevermore.muzhitui.module.bean.Video;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import base.ImageUtil;
import base.network.RetrofitUtil;


/**
 * Created by Simone on 2016/12/14.
 */

public class GvVidelTextAdapter extends BaseAdapter {
    private Context mContext;
    private List<VedioText.PageListBean> mPageListBeen;

    public GvVidelTextAdapter(Context mContext, List<VedioText.PageListBean> mPageListBeen) {
        this.mContext = mContext;
        this.mPageListBeen = mPageListBeen;
    }



    @Override
    public int getCount() {
        return mPageListBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return mPageListBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder = new ViewHolder();
        if (view == null) {
            view = View.inflate(mContext, R.layout.video_child, null);

            holder.MusicName = (TextView) view.findViewById(R.id.MusicName);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.MusicName.setText(mPageListBeen.get(position).getTitle());

        return view;
    }

    static class ViewHolder {




        TextView MusicName;


    }


}
