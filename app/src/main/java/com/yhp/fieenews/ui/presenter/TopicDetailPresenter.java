package com.yhp.fieenews.ui.presenter;


import android.os.AsyncTask;

import com.yhp.fieenews.app.Constant;
import com.yhp.fieenews.model.Topic;
import com.yhp.fieenews.model.TopicTimeLine;
import com.yhp.fieenews.network.ApiService;
import com.yhp.fieenews.network.HotTopicService;
import com.yhp.fieenews.ui.base.INetworkPresenter;
import com.yhp.fieenews.ui.base.INetworkView;
import com.yhp.fieenews.ui.fragment.TopicDetailFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TopicDetailPresenter implements INetworkPresenter {

    private HotTopicService mService = ApiService.createHotTopicService();
    private TopicDetailFragment mView;
    private String mTopicId;

    public TopicDetailPresenter(TopicDetailFragment view) {
        mView = view;
    }

    @Override
    public void start() {
        request().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Topic>() {
                    @Override
                    public void accept(Topic topic) throws Exception {
                        getView().onSuccess(topic);
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
    }

    @Override
    public Observable<Topic> request() {
        return mService.getHotTopicDetail(mTopicId);
    }

    @Override
    public Observable<Topic> requestMore() {
        return null;
    }

    @Override
    public TopicDetailFragment getView() {
        return mView;
    }

    public void getTopicDetail(String topicId) {
        mTopicId = topicId;
        start();
    }

    public void getTimeLine(final String topicId) {
        new AsyncTask<Void, Void, Document>() {

            @Override
            protected Document doInBackground(Void... voids) {
                Document document = null;
                try {
                    document = Jsoup.connect(Constant.TOPIC_DETAIL_URL + topicId).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return document;
            }

            @Override
            protected void onPostExecute(Document document) {
                super.onPostExecute(document);
                if (document == null) return;
                Elements timelineContainer = document.getElementsByClass(
                        "timeline__container___3jHS8 timeline__container--PC___1D1r7");
                if (timelineContainer == null || timelineContainer.select("li").isEmpty()) return;
                List<TopicTimeLine> timeLines = new ArrayList<>();
                for (Element liElement : timelineContainer.select("li")) {
                    TopicTimeLine timeLine = new TopicTimeLine();
                    timeLine.date = liElement.getElementsByClass("date-item___1io1R").text();
                    Element contentElement = liElement.getElementsByClass("content-item___3KfMq").get(0);
                    timeLine.content = contentElement.getElementsByTag("a").get(0).text();
                    timeLine.url = contentElement.getElementsByTag("a").get(0).attr("href");
                    timeLines.add(timeLine);
                }
                getView().onGetTimeLineSuccess(timeLines);
            }
        }.execute();
    }
}
