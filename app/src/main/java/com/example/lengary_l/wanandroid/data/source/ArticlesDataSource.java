package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.ArticleDetailData;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by CoderLengary
 */


public interface ArticlesDataSource {

    Observable<List<ArticleDetailData>> getArticles(@NonNull int page, @NonNull boolean forceUpdate, @NonNull boolean clearCache);

    Observable<List<ArticleDetailData>> queryArticles(@NonNull int page, @NonNull String keyWords, @NonNull boolean forceUpdate , @NonNull boolean clearCache);

    Observable<List<ArticleDetailData>> getArticlesFromCatg(@NonNull int page, @NonNull int categoryId , @NonNull boolean forceUpdate, @NonNull boolean clearCache);
}
