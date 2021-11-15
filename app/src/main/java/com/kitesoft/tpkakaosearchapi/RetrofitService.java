package com.kitesoft.tpkakaosearchapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface RetrofitService {

    @Headers("Authorization: KakaoAK 5d8600b361edf43f0c0501ab9afc8f68")
    @GET("/v2/local/search/keyword.json")
    Call<String> searchPlaceByString(@Query("query") String query, @Query("x") String longitude, @Query("y") String latitude);

    @Headers("Authorization: KakaoAK 5d8600b361edf43f0c0501ab9afc8f68")
    @GET("/v2/local/search/keyword.json")
    Call<SearchLocalApiResponse> searchPlace(@Query("query") String query, @Query("x") String longitude, @Query("y") String latitude);


    //네이버로그인 사용자프로필 api 요청 추상메소드
    //@Headers()는 정적인 값을 전달할때 유용함
    //@Header()는 동적인 값을 전달할때 유용함.
    @GET("/v1/nid/me")
    Call<UserInfoResponse> getUserInfo(@Header("Authorization") String authorization);

}
