package com.example.lengary_l.wanandroid.data.source;

import com.example.lengary_l.wanandroid.data.FavoriteArticleDetailData;

import java.util.List;

import io.reactivex.Observable;

public interface FavoriteArticlesDataSource {
    Observable<List<FavoriteArticleDetailData>> getFavoriteArticles(int page, boolean forceUpdate,boolean clearCache);

    boolean isExist(int userId, int id);
}
