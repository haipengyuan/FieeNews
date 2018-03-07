package com.yhp.fieenews.ui.fragment;


import android.view.ViewGroup;

import com.yhp.fieenews.model.Topic;
import com.yhp.fieenews.ui.base.BaseListFragment;
import com.yhp.fieenews.ui.base.BaseListPresenter;
import com.yhp.fieenews.ui.base.BaseViewHolder;
import com.yhp.fieenews.ui.presenter.HotTopicPresenter;
import com.yhp.fieenews.ui.view.HotTopicViewHolder;


public class HotTopicFragment extends BaseListFragment<Topic> {

    public static HotTopicFragment newInstance() {
        return new HotTopicFragment();
    }

    @Override
    public BaseViewHolder<Topic> provideViewHolder(ViewGroup parent, int viewType) {
        return new HotTopicViewHolder(getActivity(), parent);
    }

    @Override
    public BaseListPresenter<Topic> createPresenter() {
        return new HotTopicPresenter(this);
    }
}
