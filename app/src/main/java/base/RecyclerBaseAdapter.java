package base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nevermore.muzhitui.R;

import java.util.List;

import base.helper.ItemTouchHelperAdapter;

/**
 * Created by hehe on 2016/1/7.
 */
public abstract class RecyclerBaseAdapter<T> extends RecyclerView.Adapter<RecyclerBaseAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private List<T> mLt;
    private LayoutInflater mInflate;
    private int mLayoutId;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public RecyclerBaseAdapter(Context context, List<T> list, int layoutId, OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        mInflate = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mLt = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflate.inflate(mLayoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        if (mIsCela) {
            viewHolder.getView(R.id.rlyt).setOnClickListener(mItemClickListener);
            viewHolder.getView(R.id.tvEdit).setOnClickListener(mItemClickListener);
            viewHolder.getView(R.id.tvDelete).setOnClickListener(mItemClickListener);
        } else {
            view.setOnClickListener(mItemClickListener);
        }
        return viewHolder;
    }


    private boolean mIsCela = false;
    private View.OnClickListener mItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null)
                mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    };

    public void setIsCela(boolean flag) {
        mIsCela = flag;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mIsCela) {
            holder.getView(R.id.tvEdit).setTag(position);
            holder.getView(R.id.tvDelete).setTag(position);
            holder.getView(R.id.rlyt).setTag(position);
        } else {
            holder.mItemView.setTag(position);
        }
        fillData(holder, mLt.get(position), position);
    }

    public abstract void fillData(ViewHolder viewHolder, T data, int position);

    @Override
    public int getItemCount() {
        return (mLt != null) ? mLt.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> mSaView;
        public View mItemView;

        public ViewHolder(View itemView) {
            super(itemView);
            mSaView = new SparseArray<View>();
            mItemView = itemView;
        }

        public <T extends View> T getView(int viewId) {
            View view = mSaView.get(viewId);
            if (view == null) {
                view = mItemView.findViewById(viewId);
                mSaView.put(viewId, view);
            }
            return (T) view;
        }

        public void setText(int viewId, String text) {
            TextView tv = getView(viewId);
            tv.setText(text);
        }

        public void setText(int viewId, CharSequence text) {
            TextView tv = getView(viewId);
            tv.setText(text);
        }
    }

    public void setData(List<T> temp) {
        if (temp != null) {
            mLt.clear();
            mLt.addAll(temp);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        mLt.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onItemDismiss(int position) {
        mLt.remove(position);
        notifyItemRemoved(position);
    }


}
