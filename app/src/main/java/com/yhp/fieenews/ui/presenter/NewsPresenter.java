package com.yhp.fieenews.ui.presenter;

import com.yhp.fieenews.model.ApiData;
import com.yhp.fieenews.model.Topic;
import com.yhp.fieenews.network.ApiService;
import com.yhp.fieenews.network.NewsService;
import com.yhp.fieenews.ui.base.BaseListFragment;
import com.yhp.fieenews.ui.base.BaseListPresenter;

import io.reactivex.Observable;


public class NewsPresenter extends BaseListPresenter<Topic> {

    private NewsService mService = ApiService.createNewsService();

    public NewsPresenter(BaseListFragment<Topic> mFragment) {
        super(mFragment);
    }

    @Override
    public Observable<ApiData> request() {
        return mService.getNews();
    }

    @Override
    public Observable<ApiData> requestMore() {
        return mService.getMoreNews(getLastCursor(), 10);
    }
}
