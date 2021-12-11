package com.haoke91.baselibrary.recycleview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuliyan on 17/12/09.
 */
public abstract class BaseQuickWithPositionAdapter<T, H extends BaseAdapterHelper>
    extends RecyclerView.Adapter<BaseAdapterHelper> implements View.OnClickListener, View.OnLongClickListener {
    
    protected static final String TAG = BaseQuickWithPositionAdapter.class.getSimpleName();
    
    protected final Context context;
    
    protected int layoutResId;
    
    protected List<T> data;
    
    protected OnItemClickListener mOnItemClickListener = null;
    
    protected OnItemLongClickListener mOnItemLongClickListener = null;
    
    protected MultiItemTypeSupport<T> mMultiItemTypeSupport;
    
    //define interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    
    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }
    
    public Context getContext() {
        return context;
    }
    
    /**
     * Create a QuickAdapter.
     *
     * @param context     The context.
     * @param layoutResId The layout resource id of each item.
     */
    protected BaseQuickWithPositionAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }
    
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param context     The context.
     * @param layoutResId The layout resource id of each item.
     * @param data        A new list is created out of this one to avoid mutable list
     */
    protected BaseQuickWithPositionAdapter(Context context, int layoutResId, List<T> data) {
        this.data = data == null ? new ArrayList<T>() : data;
        this.context = context;
        this.layoutResId = layoutResId;
    }
    
    protected BaseQuickWithPositionAdapter(Context context,
                                           MultiItemTypeSupport<T> multiItemTypeSupport) {
        this(context, multiItemTypeSupport, null);
    }
    
    protected BaseQuickWithPositionAdapter(Context context,
                                           MultiItemTypeSupport<T> multiItemTypeSupport, List<T> data) {
        this.context = context;
        this.data = data == null ? new ArrayList<T>() : data;
        this.mMultiItemTypeSupport = multiItemTypeSupport;
    }
    
    @Override
    public int getItemCount() {
        return data.size();
    }
    
    public T getItem(int position) {
        if (position >= data.size()) {
            return null;
        }
        return data.get(position);
    }
    
    @Override
    public int getItemViewType(int position) {
        if (mMultiItemTypeSupport != null) {
            return mMultiItemTypeSupport.getItemViewType(position, getItem(position));
        }
        return super.getItemViewType(position);
    }
    
    @Override
    public BaseAdapterHelper onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = null;
        if (mMultiItemTypeSupport != null) {
            int layoutId = mMultiItemTypeSupport.getLayoutId(viewType);
            view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
        } else {
            view =
                LayoutInflater.from(viewGroup.getContext()).inflate(layoutResId, viewGroup, false);
        }
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new BaseAdapterHelper(view);
    }
    
    
    @Override
    public boolean onLongClick(View view) {
        if (mOnItemLongClickListener != null) {
            mOnItemLongClickListener.onItemLongClick(view, (int) view.getTag());
            return true;
        }
        return false;
    }
    
    
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }
    
    @Override
    public void onBindViewHolder(BaseAdapterHelper helper, int position) {
        helper.itemView.setTag(position);
        T item = getItem(position);
        convert((H) helper, item, position);
    }
    
    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    protected abstract void convert(H helper, T item, int position);
    
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }
    
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    
    public OnItemClickListener getmOnItemClickListener() {
        return mOnItemClickListener;
    }
    
    public OnItemLongClickListener getOnItemLongClickListener() {
        return mOnItemLongClickListener;
    }
    
    public void add(T elem) {
        data.add(elem);
        notifyDataSetChanged();
    }
    
    public void add(T elem, int location) {
        data.add(location, elem);
        notifyDataSetChanged();
    }
    
    public void addAll(List<T> elem) {
        if (elem == null) {
            return;
        }
        data.addAll(elem);
        notifyDataSetChanged();
    }
    
    public void setData(List<T> elem) {
        //        data.addAll(elem);
        if (elem == null) {
            if (!ObjectUtils.isEmpty(data)) {
                data.clear();
            }
        } else {
            data = elem;
        }
        notifyDataSetChanged();
    }
    
    public void addAll(int index, List<T> elem) {
        data.addAll(index, elem);
        notifyDataSetChanged();
    }
    
    public void set(T oldElem, T newElem) {
        set(data.indexOf(oldElem), newElem);
    }
    
    public void set(int index, T elem) {
        data.set(index, elem);
        notifyDataSetChanged();
    }
    
    public void remove(T elem) {
        data.remove(elem);
        notifyDataSetChanged();
    }
    
    public void remove(int index) {
        data.remove(index);
        notifyDataSetChanged();
    }
    
    public void replaceAll(List<T> elem) {
        data.clear();
        data.addAll(elem);
        notifyDataSetChanged();
    }
    
    public boolean contains(T elem) {
        return data.contains(elem);
    }
    
    /**
     * Clear data list
     */
    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }
    
    public List<T> getAll() {
        return data;
    }
}
