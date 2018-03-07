package com.yhp.fieenews.ui.presenter;


import com.yhp.fieenews.model.ApiData;
import com.yhp.fieenews.model.Topic;
import com.yhp.fieenews.network.ApiService;
import com.yhp.fieenews.network.HotTopicService;
import com.yhp.fieenews.ui.base.BaseListFragment;
import com.yhp.fieenews.ui.base.BaseListPresenter;

import io.reactivex.Observable;

public class HotTopicPresenter extends BaseListPresenter<Topic> {

    private HotTopicService mService = ApiService.createHotTopicService();

    public HotTopicPresenter(BaseListFragment<Topic> mFragment) {
        super(mFragment);
    }

    @Override
    public Observable<ApiData> request() {
        return mService.getHotTopic();
    }

    @Override
    public Observable<ApiData> requestMore() {
        return mService.getMoreHotTopic(getLastCursor(), 10);
    }
}
