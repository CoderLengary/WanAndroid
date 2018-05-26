package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.LoginData;
import com.example.lengary_l.wanandroid.data.LoginDetailData;

import io.reactivex.Observable;

public interface LoginDataSource {

    Observable<LoginData> getRemoteLoginData(@NonNull String userName,@NonNull String password);

    Observable<LoginDetailData> getLoginDetailData(@NonNull String userName);

}
