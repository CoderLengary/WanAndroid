package com.example.lengary_l.wanandroid.retrofit;

import com.example.lengary_l.wanandroid.data.LoginData;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitService {


    @POST(Api.LOGIN)
    Observable<LoginData> login(@Query("username") String username, @Query("password") String password);

    @POST(Api.REGISTER)
    Observable<LoginData> register(@Query("username") String username, @Query("password") String password,@Query("repassword") String repassword);
}
