package com.yhp.fieenews.network;


import com.yhp.fieenews.model.ApiData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TechNewsService {
    @GET("technews")
    Observable<ApiData> getTechNews();

    @GET("technews")
    Observable<ApiData> getMoreTechNews(@Query("lastCursor") String lastCursor,
                                        @Query("pageSize") int pageSize);
}
