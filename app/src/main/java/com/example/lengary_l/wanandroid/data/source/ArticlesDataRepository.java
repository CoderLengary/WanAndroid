package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.ArticlesData;

import io.reactivex.Observable;

public class ArticlesDataRepository implements ArticlesDataSource {

    @NonNull
    private ArticlesDataSource remoteDataSource;

    @NonNull
    public static ArticlesDataRepository INSTANCE;

    private ArticlesDataRepository(@NonNull ArticlesDataSource remoteDataSource){
        this.remoteDataSource = remoteDataSource;
    }


    public static ArticlesDataRepository getInstance(@NonNull ArticlesDataRepository remoteDataSource){
        if (INSTANCE == null) {
            INSTANCE = new ArticlesDataRepository(remoteDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Observable<ArticlesData> getArticles(@NonNull int page) {
        return remoteDataSource.getArticles(page);
    }
}
