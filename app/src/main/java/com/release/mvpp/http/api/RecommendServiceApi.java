package com.release.mvpp.http.api;


import com.release.mvpp.mvp.model.RecommendPageBean;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author Mr.release
 * @create 2019/3/29
 * @Describe
 */
public interface RecommendServiceApi {

    @GET("it")
    Flowable<RecommendPageBean> getRecommendData(@Query("key") String key, @Query("num") int num);
}
