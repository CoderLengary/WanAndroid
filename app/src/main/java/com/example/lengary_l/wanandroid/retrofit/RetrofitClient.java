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

//单例模式
public class RetrofitClient {

    private RetrofitClient() {
    }

    private static class ClientHolder{

        private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //添加对Cookies的管理
                .cookieJar(new CookieManger(App.getContext()))
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
