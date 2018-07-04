package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.LoginData;
import com.example.lengary_l.wanandroid.data.LoginDetailData;
import com.example.lengary_l.wanandroid.data.LoginType;

import java.util.List;

import io.reactivex.Observable;

public interface LoginDataSource {

    Observable<LoginData> getRemoteLoginData(@NonNull String userName, @NonNull String password, @NonNull LoginType loginType);

    Observable<LoginDetailData> getLocalLoginData(@NonNull String userName);

    boolean isAccountExist(String userName);

    Observable<List<Integer>> getFavoriteArticleIdList(int userId);

}
