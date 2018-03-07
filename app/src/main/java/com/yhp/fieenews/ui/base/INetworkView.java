package com.yhp.fieenews.ui.base;



public interface INetworkView<T> extends IBaseView {

    void onSuccess(T t);
    void onError(Throwable e);

    @Override
    INetworkPresenter getPresenter();
}
