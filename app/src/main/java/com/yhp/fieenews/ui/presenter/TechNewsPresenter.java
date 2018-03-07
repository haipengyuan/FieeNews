package com.yhp.fieenews.ui.presenter;


import com.yhp.fieenews.model.ApiData;
import com.yhp.fieenews.model.Topic;
import com.yhp.fieenews.network.ApiService;
import com.yhp.fieenews.network.TechNewsService;
import com.yhp.fieenews.ui.base.BaseListFragment;
import com.yhp.fieenews.ui.base.BaseListPresenter;

import io.reactivex.Observable;

public class TechNewsPresenter extends BaseListPresenter<Topic> {

    TechNewsService mService = ApiService.createTechNewsService();

    public TechNewsPresenter(BaseListFragment<Topic> mFragment) {
        super(mFragment);
    }

    @Override
    public Observable<ApiData> request() {
        return mService.getTechNews();
    }

    @Override
    public Observable<ApiData> requestMore() {
        return mService.getMoreTechNews(getLastCursor(), 10);
    }
}
