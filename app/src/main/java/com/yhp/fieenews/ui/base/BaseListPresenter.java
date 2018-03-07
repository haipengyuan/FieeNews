package com.yhp.fieenews.ui.base;


import com.yhp.fieenews.model.ApiData;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseListPresenter<T> implements INetworkPresenter {
    private BaseListFragment<T> mFragment;
    private String lastCursor;

    public BaseListPresenter(BaseListFragment<T> mFragment) {
        this.mFragment = mFragment;
    }

    @Override
    public void start() {
        request().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<ApiData>() {
                    @Override
                    public void accept(ApiData apiData) throws Exception {
                        if (apiData == null || apiData.getData() == null) {
                            getView().onError(new Throwable("请求失败"));
                            return;
                        }
                        getView().onSuccess(apiData.getData());
                        lastCursor = apiData.getData().get(apiData.getData().size() - 1)
                                .getLastCursor();
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
    public void startRequestMore() {
        requestMore().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<ApiData>() {
                    @Override
                    public void accept(ApiData apiData) throws Exception {
                        if (apiData == null || apiData.getData() == null) {
                            getView().onError(new Throwable("请求失败"));
                            return;
                        }
                        getView().onSuccess(apiData.getData());

                        // 没有更多的数据，设置hasMore为false
                        if (apiData.getData().isEmpty()) {
                            getView().hasMore = false;
                            return;
                        }
                        lastCursor = apiData.getData().get(apiData.getData().size() - 1)
                                .getLastCursor();
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
    public BaseListFragment<T> getView() {
        return mFragment;
    }

    @Override
    public abstract Observable<ApiData> request();

    @Override
    public abstract Observable<ApiData> requestMore();

    public String getLastCursor() {
        return lastCursor;
    }
}
