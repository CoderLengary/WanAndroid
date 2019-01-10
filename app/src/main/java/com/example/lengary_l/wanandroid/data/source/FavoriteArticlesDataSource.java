package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import io.reactivex.Observable;

/**
 * Created by CoderLengary
 */


public interface FavoriteArticlesDataSource {
    Observable getFavoriteArticles(int page, boolean forceUpdate, boolean clearCache);

    boolean isExist(int userId, int id);
}
