package com.yhp.fieenews.ui.presenter;


import com.yhp.fieenews.model.InstantReadData;
import com.yhp.fieenews.network.ApiService;
import com.yhp.fieenews.network.HotTopicService;
import com.yhp.fieenews.ui.base.INetworkPresenter;
import com.yhp.fieenews.ui.base.INetworkView;
import com.yhp.fieenews.ui.fragment.InstantReadFragment;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class InstantReadPresenter implements INetworkPresenter {

    private HotTopicService mService = ApiService.createHotTopicService();
    private InstantReadFragment mView;
    private String mTopicId;

    public InstantReadPresenter(InstantReadFragment mView) {
        this.mView = mView;
    }

    @Override
    public void start() {
        request().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<InstantReadData>() {

                    @Override
                    public void accept(InstantReadData instantReadData) throws Exception {
                        getView().onSuccess(instantReadData);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        getView().onError(throwable);
                    }
                });
    }

    @Override
    public void startRequestMore() {}

    @Override
    public Observable request() {
        return mService.getInstantRead(mTopicId);
    }

    @Override
    public Observable requestMore() {
        return null;
    }

    @Override
    public InstantReadFragment getView() {
        return mView;
    }

    public void getInstantRead(String topicId) {
        mTopicId = topicId;
        start();
    }
}
