package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.ArticlesData;

import io.reactivex.Observable;

public class ArticlesDataRepository implements ArticlesDataSource {
    @Override
    public Observable<ArticlesData> getArticles(@NonNull int page) {
        return null;
    }
}
