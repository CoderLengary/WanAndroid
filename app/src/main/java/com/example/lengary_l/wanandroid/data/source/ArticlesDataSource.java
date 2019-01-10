package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.ArticleDetailData;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by CoderLengary
 */


public interface ArticlesDataSource {

    Observable<List<ArticleDetailData>> getArticles(int page, boolean forceUpdate, boolean clearCache);

    Observable<List<ArticleDetailData>> queryArticles(int page,@NonNull String keyWords, boolean forceUpdate , boolean clearCache);

    Observable<List<ArticleDetailData>> getArticlesFromCatg(int page, int categoryId , boolean forceUpdate, boolean clearCache);
}
