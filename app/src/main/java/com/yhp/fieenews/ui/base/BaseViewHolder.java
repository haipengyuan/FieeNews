package com.yhp.fieenews.ui.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import butterknife.ButterKnife;


public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    public BaseViewHolder(Context context, ViewGroup viewGroup, int layoutRes) {
        super(LayoutInflater.from(context).inflate(layoutRes, viewGroup, false));
        ButterKnife.bind(this, itemView);
    }

    public abstract void bindTo(T value);
}
