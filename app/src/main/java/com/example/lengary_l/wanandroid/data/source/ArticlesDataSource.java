package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.ArticleDetailData;

import java.util.List;

import io.reactivex.Observable;

public interface ArticlesDataSource {

    Observable<List<ArticleDetailData>> getArticles(@NonNull int page, boolean forceUpdate, boolean clearCache);

    Observable<List<ArticleDetailData>> queryArticles(@NonNull int page, @NonNull String keyWords,boolean forceUpdate , boolean clearCache);

    Observable<ArticleDetailData> getArticleFromId(@NonNull int id);

    void addToReadLater(int currentUserId, int articleId, boolean readerLater);

    Observable<List<ArticleDetailData>> getArticlesFromReadLater(int currentUserId, int articleId);
}
