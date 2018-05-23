package com.nevermore.muzhitui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.MusicDataBean;

import java.util.List;

public class MusicAdapter extends BaseExpandableListAdapter
{List<MusicDataBean.MusicArrayBean> mMusicDataBeens;

	public Context mContext;
	ExpandableListView mListviewMusic;
	public MusicAdapter(List<MusicDataBean.MusicArrayBean> mMusicDataBeens, Context context,ExpandableListView mListviewMusic) {
		this.mMusicDataBeens = mMusicDataBeens;

		mContext = context;
		this.mListviewMusic=mListviewMusic;
	}

	@Override
	public void notifyDataSetChanged() {


		super.notifyDataSetChanged();
		for (int i=0; i<mMusicDataBeens.size(); i++) {
			mListviewMusic.expandGroup(i);
		};
	}

	@Override
	public int getGroupCount() {
		return mMusicDataBeens.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mMusicDataBeens.get(groupPosition).getList().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mMusicDataBeens.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mMusicDataBeens.get(groupPosition).getList();
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
				R.layout.music_father, null);
		TextView textView = (TextView) view.findViewById(R.id.sectionType);
		textView.setText(mMusicDataBeens.get(groupPosition).getCategory());
		return view;
	}

	@Override
	public View getChildView(final int groupPosition,final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		View view = null;
		view = LayoutInflater.from(mContext).inflate(
				R.layout.music_child, null);
		TextView textView = (TextView) view
				.findViewById(R.id.MusicName);
		final ImageView imageView=(ImageView) view
				.findViewById(R.id.ivMusicCheck);
		textView.setText(mMusicDataBeens.get(groupPosition).getList().get(childPosition).getName());

		if (mMusicDataBeens.get(groupPosition).getList().get(childPosition).isCheck()){
			imageView.setVisibility(View.VISIBLE);
		}else {
			imageView.setVisibility(View.GONE);
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
