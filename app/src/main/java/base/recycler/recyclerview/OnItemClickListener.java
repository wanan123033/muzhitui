package base.recycler.recyclerview;

import android.view.View;
import android.view.ViewGroup;

import base.recycler.ViewHolder;

public interface OnItemClickListener<T>
{
    void onItemClick(ViewGroup parent, View view, T t, int position,ViewHolder viewHolder);
    boolean onItemLongClick(ViewGroup parent, View view, T t, int position,ViewHolder viewHolder);
}