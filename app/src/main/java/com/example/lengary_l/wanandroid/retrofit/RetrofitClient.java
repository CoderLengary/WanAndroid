package com.example.lengary_l.wanandroid.retrofit;

import com.example.lengary_l.wanandroid.app.App;
import com.example.lengary_l.wanandroid.retrofit.Cookies.CookieManger;

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

        private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieManger(App.getContext()))
                .build();


        private static Retrofit retrofit = new Retrofit.Builder()
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
