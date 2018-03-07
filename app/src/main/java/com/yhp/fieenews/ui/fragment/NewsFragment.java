package com.yhp.fieenews.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yhp.fieenews.R;
import com.yhp.fieenews.model.Topic;
import com.yhp.fieenews.ui.base.BaseListFragment;
import com.yhp.fieenews.ui.base.BaseListPresenter;
import com.yhp.fieenews.ui.base.BaseViewHolder;
import com.yhp.fieenews.ui.presenter.NewsPresenter;
import com.yhp.fieenews.ui.view.NewsViewHolder;


public class NewsFragment extends BaseListFragment<Topic> {

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }


    @Override
    public BaseViewHolder<Topic> provideViewHolder(ViewGroup parent, int viewType) {
        return new NewsViewHolder(getActivity(), parent);
    }

    @Override
    public BaseListPresenter<Topic> createPresenter() {
        return new NewsPresenter(this);
    }
}
