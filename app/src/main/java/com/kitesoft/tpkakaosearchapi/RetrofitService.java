package com.kitesoft.tpkakaosearchapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface RetrofitService {

    @Headers("Authorization: KakaoAK 5d8600b361edf43f0c0501ab9afc8f68")
    @GET("/v2/local/search/keyword.json")
    Call<String> searchPlaceByString(@Query("query") String query, @Query("x") String longitude, @Query("y") String latitude);

    @Headers("Authorization: KakaoAK 5d8600b361edf43f0c0501ab9afc8f68")
    @GET("/v2/local/search/keyword.json")
    Call<SearchLocalApiResponse> searchPlace(@Query("query") String query, @Query("x") String longitude, @Query("y") String latitude);
}
