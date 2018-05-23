package com.nevermore.muzhitui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.MusicDataBean;
import com.nevermore.muzhitui.module.bean.QuickReply;
import com.nevermore.muzhitui.module.bean.VedioText;
import com.nevermore.muzhitui.module.json.QuickReplyBean;

import java.util.List;


/**
 * Created by Simone on 2016/12/14.
 */

public class LvQuickReplyAdapter extends BaseExpandableListAdapter {
    List<QuickReplyBean> mQuickReplyBean;

    public Context mContext;
    ExpandableListView mListviewMusic;
    private boolean isShow=false;

    public LvQuickReplyAdapter(List<QuickReplyBean> mQuickReplyBean, Context context, ExpandableListView mListviewMusic,boolean isShow) {
        this.mQuickReplyBean = mQuickReplyBean;

        mContext = context;
        this.mListviewMusic = mListviewMusic;
        this.isShow=isShow;
    }
    public LvQuickReplyAdapter(List<QuickReplyBean> mQuickReplyBean, Context context, ExpandableListView mListviewMusic) {
        this.mQuickReplyBean = mQuickReplyBean;

        mContext = context;
        this.mListviewMusic = mListviewMusic;

    }
    @Override
    public void notifyDataSetChanged() {


        super.notifyDataSetChanged();
        //二级列表默认展开
        for (int i = 0; i < mQuickReplyBean.size(); i++) {
            mListviewMusic.expandGroup(i);
        }

    }

    @Override
    public int getGroupCount() {
        return mQuickReplyBean.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mQuickReplyBean.get(groupPosition).getList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mQuickReplyBean.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mQuickReplyBean.get(groupPosition).getList();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.quick_reply_father, null);
        TextView textView = (TextView) view.findViewById(R.id.sectionType);
        textView.setText(mQuickReplyBean.get(groupPosition).getCategory());
        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = null;
        view = LayoutInflater.from(mContext).inflate(
                R.layout.quick_reply_child, null);
        TextView textView = (TextView) view
                .findViewById(R.id.MusicName);
        TextView tvContent = (TextView) view
                .findViewById(R.id.tvContent);
        ImageView ivQuickReply = (ImageView) view
                .findViewById(R.id.ivQuickReply);

        textView.setText(mQuickReplyBean.get(groupPosition).getList().get(childPosition).getName());
        //区分快速回复和视屏区是否显示向下的箭头。和文字布局
        if (isShow){
            ivQuickReply.setVisibility(View.VISIBLE);
            if (mQuickReplyBean.get(groupPosition).getList().get(childPosition).isCheck()) {
                tvContent.setVisibility(View.VISIBLE);//当选中点击时 文字区域显示
                ivQuickReply.setImageResource(R.mipmap.ic_etitartcle_up);

                tvContent.setText(mQuickReplyBean.get(groupPosition).getList().get(childPosition).getUrl());
            } else {
                tvContent.setVisibility(View.GONE);
                ivQuickReply.setImageResource(R.mipmap.ic_etitartcle_down);
            }

        }else{
            ivQuickReply.setVisibility(View.GONE);

        }



        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

		/*Toast.makeText(mContext,
                "第" + mMusicDataBeens.get(groupPosition).getCategory() + "大项，第" + mMusicDataBeens.get(groupPosition).getList().get(childPosition).getName() + "小项被点击了",
				Toast.LENGTH_LONG).show();*/
        return true;
    }
}
