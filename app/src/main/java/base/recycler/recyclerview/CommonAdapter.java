package base.recycler.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nevermore.muzhitui.module.bean.DynamicBean;

import java.util.List;

import base.recycler.ViewHolder;

/**
 * Created by zhy on 16/4/9.
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    private OnItemClickListener mOnItemClickListener;

    private ViewHolder viewHolder;
    private Object object;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public CommonAdapter(Context context, int layoutId, List<T> datas) {
        mContext = context;
        Log.i("TAG","context="+context);
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        viewHolder = ViewHolder.get(mContext, null, parent, mLayoutId, -1);
        setListener(parent, viewHolder, viewType);
        return viewHolder;
    }

    protected int getPosition(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.getAdapterPosition();
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }


    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, int viewType) {
        if (!isEnabled(viewType)) return;
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = getPosition(viewHolder);
                    mOnItemClickListener.onItemClick(parent, v, mDatas.get(position), position, viewHolder);
                }
            }
        });


        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = getPosition(viewHolder);
                    return mOnItemClickListener.onItemLongClick(parent, v, mDatas.get(position), position, viewHolder);
                }
                return false;
            }
        });
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.updatePosition(position);
        Log.i("TAGTAG","onBindViewHolder--"+mDatas.get(position).toString());
        convert(holder, mDatas.get(position));
    }

    public abstract void convert(ViewHolder holder, T t);

    @Override
    public int getItemCount() {
        if (mDatas != null)
            return mDatas.size();
        return 0;
    }


    public void addDate(List<T> list) {
        if (mDatas != null){
            mDatas.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void addDate(T bean) {
        if (mDatas != null) {
            mDatas.add(bean);
            notifyDataSetChanged();
        }
    }

    public void removeDate(T bean) {
        if (mDatas != null) {
            mDatas.remove(bean);
            notifyDataSetChanged();
        }
    }
    public void removeAllDate() {
        if (mDatas != null) {
            mDatas.clear();
            notifyDataSetChanged();
        }
    }
    public void replaceAllDate(List<T> list) {
        if (mDatas != null) {
            mDatas.clear();
            mDatas.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void addFirstData(List<T> dtList) {
        if (mDatas != null) {
            mDatas.removeAll(dtList);
            mDatas.addAll(0, dtList);
            notifyDataSetChanged();
        }
    }

    public List<T> getData(){
        return mDatas;
    }


//    public void setObject(Object object){
//        this.object = object;
//        notifyDataSetChanged();
//    }
//
//    public Object getObject() {
//        return object;
//    }
}
