package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.FavoriteArticleDetailData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class FavoriteArticlesDataRepository implements FavoriteArticlesDataSource {
    private FavoriteArticlesDataSource remote;
    private Map<Integer, FavoriteArticleDetailData> favoriteArticlesCache;
    @NonNull
    private static FavoriteArticlesDataRepository INSTANCE;

    private FavoriteArticlesDataRepository(FavoriteArticlesDataSource remote) {
        this.remote = remote;
    }
    public static FavoriteArticlesDataRepository getInstance(FavoriteArticlesDataSource remote) {
        if (INSTANCE == null) {
            INSTANCE = new FavoriteArticlesDataRepository(remote);
        }
        return INSTANCE;
    }


    @Override
    public Observable<List<FavoriteArticleDetailData>> getFavoriteArticles(int page, boolean forceUpdate,final boolean clearCache) {
        if (!forceUpdate && favoriteArticlesCache != null) {
            List<FavoriteArticleDetailData> cacheItems = new ArrayList<>(favoriteArticlesCache.values());
            return Observable.fromIterable(cacheItems).toSortedList(new Comparator<FavoriteArticleDetailData>() {
                @Override
                public int compare(FavoriteArticleDetailData favoriteArticleDetailData, FavoriteArticleDetailData t1) {
                    if (favoriteArticleDetailData.getPublishTime() > t1.getPublishTime()) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            }).toObservable();
        }

        if (!clearCache&&favoriteArticlesCache!=null) {
            List<FavoriteArticleDetailData> cacheItems = new ArrayList<>(favoriteArticlesCache.values());
            Observable ob1 = Observable.just(cacheItems);
            Observable ob2=remote.getFavoriteArticles(page, forceUpdate, clearCache)
                    .doOnNext(new Consumer<List<FavoriteArticleDetailData>>() {
                        @Override
                        public void accept(List<FavoriteArticleDetailData> list) throws Exception {
                            refreshArticlesCache(clearCache, list);
                        }
                    });
            return Observable.merge(ob1, ob2);
        }

        return remote.getFavoriteArticles(page, forceUpdate,clearCache)
                .doOnNext(new Consumer<List<FavoriteArticleDetailData>>() {
                    @Override
                    public void accept(List<FavoriteArticleDetailData> list) throws Exception {
                        refreshArticlesCache(clearCache,list);
                    }
                });
    }

    private void refreshArticlesCache(boolean clearCache,List<FavoriteArticleDetailData> list) {
        if (favoriteArticlesCache == null) {
            favoriteArticlesCache = new LinkedHashMap<>();
        }
        for (FavoriteArticleDetailData data : list) {
            favoriteArticlesCache.put(data.getOriginId(), data);
        }
    }
}
