package com.nevermore.muzhitui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.Contacts;
import com.nevermore.muzhitui.module.bean.Video;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import base.ImageUtil;
import base.network.RetrofitUtil;


/**
 * Created by Simone on 2016/12/14.
 */

public class GvVidelAdapter extends BaseAdapter {
    private Context mContext;
    private List<Video.VedioArrayBean> mVedioArrayBeen;

    public GvVidelAdapter(Context mContext, List<Video.VedioArrayBean> mVedioArrayBeen) {
        this.mContext = mContext;
        this.mVedioArrayBeen = mVedioArrayBeen;
    }



    @Override
    public int getCount() {
        return mVedioArrayBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return mVedioArrayBeen.get(position);
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
            view = View.inflate(mContext, R.layout.item_lv_vidio, null);
            holder.ivVidioBase = (ImageView) view.findViewById(R.id.ivVidioBase);
            holder.tvVideoText = (TextView) view.findViewById(R.id.tvVideoText);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        ImageLoader.getInstance().displayImage(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + mVedioArrayBeen.get(position).getImg_url(), holder.ivVidioBase, ImageUtil.getInstance().getBaseDisplayOption());

        holder.tvVideoText.setText(mVedioArrayBeen.get(position).getTitle());

        return view;
    }

    static class ViewHolder {

        ImageView ivVidioBase;


        TextView tvVideoText;


    }


}
