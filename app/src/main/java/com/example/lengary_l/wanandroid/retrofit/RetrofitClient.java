package com.example.lengary_l.wanandroid.retrofit;

import com.example.lengary_l.wanandroid.app.App;
import com.example.lengary_l.wanandroid.retrofit.CookiesInterceptor.CookiesManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by CoderLengary
 */

public class RetrofitClient {

    private RetrofitClient() {
    }

    private static class ClientHolder{
        private static CookiesManager cookiesManager = new CookiesManager.Builder(App.getContext()).urls("user/login","user/register").build();


        private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //Manager the cookies
                .addInterceptor(cookiesManager.getGetCookieInterceptor())
                .addInterceptor(cookiesManager.getAddCookiesInterceptor())
                .build();


        private static final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.API_BASE)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static Retrofit getInstance(){
        return ClientHolder.retrofit;
    }
}
