package com.example.lengary_l.wanandroid.data.source.remote;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.FavoriteArticleDetailData;
import com.example.lengary_l.wanandroid.data.FavoriteArticlesData;
import com.example.lengary_l.wanandroid.data.source.FavoriteArticlesDataSource;
import com.example.lengary_l.wanandroid.retrofit.RetrofitClient;
import com.example.lengary_l.wanandroid.retrofit.RetrofitService;
import com.example.lengary_l.wanandroid.util.SortDescendUtil;

import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by CoderLengary
 */


public class FavoriteArticlesDataRemoteSource implements FavoriteArticlesDataSource {

    private static FavoriteArticlesDataRemoteSource INSTANCE;

    private FavoriteArticlesDataRemoteSource() {

    }

    public static FavoriteArticlesDataRemoteSource getInstance() {
        if (INSTANCE == null) {
            synchronized (FavoriteArticlesDataRemoteSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavoriteArticlesDataRemoteSource();
                }
            }
        }
        return INSTANCE;
    }


    @Override
    public Observable<List<FavoriteArticleDetailData>> getFavoriteArticles(final int page, final boolean forceUpdate, final boolean clearCache) {
        return RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .getFavoriteArticles(page)
                .filter(new Predicate<FavoriteArticlesData>() {
                    @Override
                    public boolean test(FavoriteArticlesData favoriteArticlesData) {
                        return favoriteArticlesData.getErrorCode() == 0;
                    }
                }).flatMap(new Function<FavoriteArticlesData, ObservableSource<List<FavoriteArticleDetailData>>>() {
                    @Override
                    public ObservableSource<List<FavoriteArticleDetailData>> apply(FavoriteArticlesData favoriteArticlesData) {
                        return Observable.fromIterable(favoriteArticlesData.getData().getDatas()).toSortedList(new Comparator<FavoriteArticleDetailData>() {
                            @Override
                            public int compare(FavoriteArticleDetailData favoriteArticleDetailData, FavoriteArticleDetailData t1) {
                                return SortDescendUtil.sortFavoriteDetailData(favoriteArticleDetailData, t1);
                            }
                        }).toObservable();
                    }
                });
    }

    @Override
    public boolean isExist(int userId, int id) {
        //Not required because the {@link FavoriteArticlesDataLocalSource} has handled it
        return false;
    }
}
