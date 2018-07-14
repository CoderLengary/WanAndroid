package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.FavoriteArticleDetailData;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by CoderLengary
 */


public interface FavoriteArticlesDataSource {
    Observable<List<FavoriteArticleDetailData>> getFavoriteArticles(@NonNull int page,@NonNull boolean forceUpdate,@NonNull boolean clearCache);

    boolean isExist(@NonNull int userId, @NonNull int id);
}
