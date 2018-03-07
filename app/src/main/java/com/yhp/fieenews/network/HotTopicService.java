package com.yhp.fieenews.network;


import com.yhp.fieenews.model.ApiData;
import com.yhp.fieenews.model.InstantReadData;
import com.yhp.fieenews.model.Topic;
import com.yhp.fieenews.ui.fragment.InstantReadFragment;

import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableRange;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HotTopicService {

    @GET("topic")
    Observable<ApiData> getHotTopic();

    @GET("topic")
    Observable<ApiData> getMoreHotTopic(@Query("lastCursor") String lastCursor,
                                        @Query("pageSize") int pageSize);

    @GET("topic/{topic_id}")
    Observable<Topic> getHotTopicDetail(@Path("topic_id") String topicId);

    @GET("/topic/instantview")
    Observable<InstantReadData> getInstantRead(@Query("topicId") String topicId);
}
