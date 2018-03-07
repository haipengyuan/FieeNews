package com.yhp.fieenews.ui.base;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yhp.fieenews.R;

import butterknife.BindView;

public class LoadingViewHolder extends BaseViewHolder {

    @BindView(R.id.text_view_loading_item)
    TextView loadingItemTv;

    @BindView(R.id.progress_bar_loading_item)
    ProgressBar mProgressBar;

    public LoadingViewHolder(ViewGroup parent, boolean hasMore) {
        super(parent.getContext(), parent, R.layout.list_item_loading);
        loadingItemTv.setVisibility(hasMore ? View.GONE : View.VISIBLE);
        mProgressBar.setVisibility(hasMore ? View.VISIBLE : View.GONE);
    }

    @Override
    public void bindTo(Object value) {
    }
}
