package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.LoginData;
import com.example.lengary_l.wanandroid.data.LoginDetailData;
import com.example.lengary_l.wanandroid.data.LoginType;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;

public class LoginDataRepository implements LoginDataSource{
    @NonNull
    private LoginDataSource localDataSource;
    @NonNull
    private LoginDataSource remoteDataSource;

    private LoginDataRepository(@NonNull LoginDataSource localDataSource,@NonNull LoginDataSource remoteDataSource){
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }
    @NonNull
    private static LoginDataRepository INSTANCE;

    public static LoginDataRepository getInstance(@NonNull LoginDataSource localDataSource,@NonNull LoginDataSource remoteDataSource){
        if (INSTANCE == null) {
            INSTANCE = new LoginDataRepository(localDataSource, remoteDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Observable<LoginData> getRemoteLoginData(@NonNull String userName, @NonNull String password, @NonNull LoginType loginType) {
        return remoteDataSource.getRemoteLoginData(userName, password,loginType);
    }

    @Override
    public Observable<LoginDetailData> getLocalLoginData(@NonNull int userId) {
        return localDataSource.getLocalLoginData(userId);
    }

    @Override
    public boolean isAccountExist(@NonNull int userId) {
        return localDataSource.isAccountExist(userId);
    }

    @Override
    public Observable<List<Integer>> getFavoriteArticleIdList(int userId) {
        return localDataSource.getFavoriteArticleIdList(userId)
                .filter(new Predicate<List<Integer>>() {
                    @Override
                    public boolean test(List<Integer> list) throws Exception {
                        return !list.isEmpty();
                    }
                });
    }


}
