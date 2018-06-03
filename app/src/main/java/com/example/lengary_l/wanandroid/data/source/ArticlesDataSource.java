package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.ArticleDetailData;

import java.util.List;

import io.reactivex.Observable;

public interface ArticlesDataSource {

    Observable<List<ArticleDetailData>> getArticles(@NonNull int page, boolean forceUpdate, boolean clearCache);
}
