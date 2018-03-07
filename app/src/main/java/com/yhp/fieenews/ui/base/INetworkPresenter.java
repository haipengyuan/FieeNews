package com.yhp.fieenews.ui.base;


import io.reactivex.Observable;

public interface INetworkPresenter extends IBasePresenter {

    void start();

    void startRequestMore();

    Observable request();

    Observable requestMore();

    @Override
    INetworkView getView();
}