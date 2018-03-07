package com.yhp.fieenews.network;


import com.yhp.fieenews.model.ApiData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsService {

    @GET("news")
    Observable<ApiData> getNews();

    @GET("news")
    Observable<ApiData> getMoreNews(@Query("lastCursor") String lastCursor,
                                    @Query("pageSize") int pageSize);

}
