package com.example.lengary_l.wanandroid.data.source;

import com.example.lengary_l.wanandroid.data.ArticleDetailData;

import java.util.List;

import io.reactivex.Observable;

public interface CategoryDataSource {
    Observable<List<ArticleDetailData>> getArticlesFromCatg(int page, int categoryId , boolean forceUpdate,boolean clearCache);
}
