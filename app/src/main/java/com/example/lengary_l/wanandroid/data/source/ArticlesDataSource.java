package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.ArticlesData;

import io.reactivex.Observable;

public interface ArticlesDataSource {

    Observable<ArticlesData> getArticles(@NonNull int page, boolean forceUpdate, boolean clearCache);
}
