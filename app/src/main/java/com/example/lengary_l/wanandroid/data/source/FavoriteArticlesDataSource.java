package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import io.reactivex.Observable;

/**
 * Created by CoderLengary
 */


public interface FavoriteArticlesDataSource {
    Observable getFavoriteArticles(@NonNull int page, @NonNull boolean forceUpdate, @NonNull boolean clearCache);

    boolean isExist(@NonNull int userId, @NonNull int id);
}
