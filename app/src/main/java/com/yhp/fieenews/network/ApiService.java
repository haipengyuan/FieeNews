package com.yhp.fieenews.network;


import android.util.Log;

import com.yhp.fieenews.app.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constant.API_HOST)
            .client(new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor(
                            new HttpLoggingInterceptor.Logger() {
                            @Override
                            public void log(String message) {
                                Log.i("retrofit", message);
                            }
                        }).setLevel(HttpLoggingInterceptor.Level.BODY))
                    .readTimeout(2, TimeUnit.MINUTES)
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .writeTimeout(2, TimeUnit.MINUTES)
                    .build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();


    public static HotTopicService createHotTopicService() {
        return retrofit.create(HotTopicService.class);
    }

    public static NewsService createNewsService() {
        return retrofit.create(NewsService.class);
    }

    public static TechNewsService createTechNewsService() {
        return retrofit.create(TechNewsService.class);
    }
}
