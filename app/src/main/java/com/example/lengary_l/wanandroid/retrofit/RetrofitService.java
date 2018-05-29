package com.example.lengary_l.wanandroid.retrofit;

import com.example.lengary_l.wanandroid.data.LoginData;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitService {

    @FormUrlEncoded
    @POST(Api.LOGIN)
    Observable<LoginData> login(@Field("username") String username, @Field("password") String password);
    @FormUrlEncoded
    @POST(Api.REGISTER)
    Observable<LoginData> register(@Field("username") String username, @Field("password") String password,@Field("repassword") String repassword);
}
