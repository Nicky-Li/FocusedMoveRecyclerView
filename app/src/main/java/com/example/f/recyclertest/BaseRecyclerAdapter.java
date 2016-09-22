package com.example.f.recyclertest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * K为adapter数据，V为ViewHolder
 * Created by xuzhili on 16/7/25.
 */
public abstract class BaseRecyclerAdapter<T0, T1 extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context context;
    protected List<T0> datas;
    private OnItemClickListner onItemClickListner;
    private OnItemFocusChangedListner onItemFocusChangedListner;

    public BaseRecyclerAdapter(Context context, List<T0> datas) {
        this.context = context;
        this.datas = datas;
    }

    protected abstract T1 onCreateHolder(ViewGroup parent, int viewType);

    protected abstract void onBindHolder(T1 holder, int position);

    @Override
    public T1 onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (onItemClickListner != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListner.onItemClick(holder.itemView, position);
                }
            });
        }
        if (onItemFocusChangedListner != null) {
            holder.itemView.setFocusable(true);
            holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    Log.d("BaseRecyclerAdapter", "onFocused");
                    onItemFocusChangedListner.onItemFocusChanged(hasFocus, v);
                }
            });
        }

        onBindHolder((T1) holder, position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (recyclerView instanceof OnItemFocusChangedListner && onItemFocusChangedListner == null) {
            this.onItemFocusChangedListner = (OnItemFocusChangedListner) recyclerView;
        }
    }

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }

    /**
     * 清空数据
     */
    public void removeAll() {

        if (datas == null) {
            return;
        }

        datas.clear();
        this.notifyDataSetChanged();

    }

    /**
     * 尾部添加值
     *
     * @param value
     */
    public void addItem(T0 value) {

        if (datas == null) {
            return;
        }

        int size = datas.size();
        datas.add(value);
        notifyItemInserted(size);
    }


    /**
     * 尾部添加values
     *
     * @param values
     */
    public void addAll(List<T0> values) {

        if (datas == null || values == null) {
            return;
        }

        int size = datas.size();
        int insertSize = values.size();
        datas.addAll(values);
        notifyItemRangeInserted(size, insertSize);
    }

    /**
     * 固定位置添加一项
     *
     * @param position
     * @param value
     */
    public void addItem(int position, T0 value) {

        if (datas == null || value == null) {
            return;
        }

        datas.add(position, value);
        notifyItemInserted(position);
    }

    /**
     * 重置adapter数据
     *
     * @param values
     */
    public void resetData(List<T0> values) {

        if (datas == null || values == null) {
            return;
        }

        datas.clear();
        datas.addAll(values);
        notifyDataSetChanged();
    }

    public List<T0> getDatas() {

        return datas;
    }

    public OnItemClickListner getOnItemClickListner() {
        return onItemClickListner;
    }

    public void setOnItemClickListner(OnItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    public OnItemFocusChangedListner getOnItemFocusChangedListner() {
        return onItemFocusChangedListner;
    }

    public void setOnItemFocusChangedListner(OnItemFocusChangedListner onItemFocusChangedListner) {
        this.onItemFocusChangedListner = onItemFocusChangedListner;
    }

    public interface OnItemClickListner {

        void onItemClick(View itemView, int position);

    }

    public interface OnItemFocusChangedListner {

        void onItemFocusChanged(boolean isFocused, View focusedView);

    }


}
