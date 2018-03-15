package com.yhp.fieenews.ui.base;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<T>> {

    private List<T> mItemList = new ArrayList<>();

    @Override
    public void onBindViewHolder(BaseViewHolder<T> holder, int position) {
        if (position < mItemList.size()) {
            holder.bindTo(mItemList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public void clear() {
        mItemList.clear();
        notifyDataSetChanged();
    }

    public void addItem(T value) {
        mItemList.add(value);
        notifyItemInserted(mItemList.size() - 1);
    }

    public void addItems(Collection<T> valueCollection) {
        int oldSize = mItemList.size();
        // 指定插入位置为oldSize
        mItemList.addAll(oldSize, valueCollection);
        notifyItemRangeInserted(oldSize, valueCollection.size());
    }

    public void remove(T value) {
        if (mItemList.contains(value)) {
            int oldPosition = mItemList.indexOf(value);
            mItemList.remove(value);
            notifyItemRemoved(oldPosition);
        }
    }
}
