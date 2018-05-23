package com.nevermore.muzhitui.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;


import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.CityModel;
import com.nevermore.muzhitui.module.bean.MSharePreferences;
import com.nevermore.muzhitui.module.bean.ProvinceModel;


import java.util.List;

import base.service.Tools;
import android.widget.ImageView;

public class ProvinceAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
    private int index = -1;
    private int selectedIndex = 0;
    private boolean flag = true;


    private List<ProvinceModel> provinces;

    private List<CityModel> cities;
    private boolean defaultSelectedPosition = true;

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public ProvinceAdapter(Context context, List<ProvinceModel> provinces) {
        this.context = context;
        this.provinces = provinces;

        this.mInflater = LayoutInflater.from(context);

    }

    public ProvinceAdapter(Context context, List<CityModel> cities, boolean flag) {
        this.context = context;
        this.cities = cities;
        this.flag = flag;
        this.mInflater = LayoutInflater.from(context);

    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return flag ? provinces.size() : cities.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return flag ? provinces.get(position) : cities.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        MSharePreferences sharePreferences = MSharePreferences
                .getInstance();
        sharePreferences.getSharedPreferences(context);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_lv_province, null);
            // viewHolder.ProvinceRadioGroup = (RadioGroup)
            // convertView.findViewById(R.id.ProinceParent);
            viewHolder.tvItemProvinceName = (TextView) convertView
                    .findViewById(R.id.tvItemProvinceName);
            viewHolder.ivItemProvinceMore = (ImageView) convertView
                    .findViewById(R.id.ivItemProvinceMore);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (flag) {
            viewHolder.tvItemProvinceName.setText(provinces.get(position).getName());
            if (provinces.get(position).getCityList().size()>0){
                viewHolder.ivItemProvinceMore.setVisibility(View.VISIBLE);
            }else{
                viewHolder.ivItemProvinceMore.setVisibility(View.GONE);
            }
        } else {
            viewHolder.tvItemProvinceName.setText(cities.get(position).getName());

                viewHolder.ivItemProvinceMore.setVisibility(View.GONE);

        }




        return convertView;
    }

    class ViewHolder {
        private TextView tvItemProvinceName;
        private ImageView ivItemProvinceMore;
    }
}
