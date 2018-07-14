package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.FavoriteArticleDetailData;
import com.example.lengary_l.wanandroid.util.SortDescendUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

/**
 * Created by CoderLengary
 */


public class FavoriteArticlesDataRepository implements FavoriteArticlesDataSource {
    private FavoriteArticlesDataSource remote;
    private FavoriteArticlesDataSource local;
    private Map<Integer, FavoriteArticleDetailData> favoriteArticlesCache;
    @NonNull
    private static FavoriteArticlesDataRepository INSTANCE;

    private FavoriteArticlesDataRepository(@NonNull FavoriteArticlesDataSource remote, @NonNull FavoriteArticlesDataSource local) {
        this.remote = remote;
        this.local = local;
    }
    public static FavoriteArticlesDataRepository getInstance(@NonNull FavoriteArticlesDataSource remote,@NonNull FavoriteArticlesDataSource local) {
        if (INSTANCE == null) {
            INSTANCE = new FavoriteArticlesDataRepository(remote, local);
        }
        return INSTANCE;
    }


    @Override
    public Observable<List<FavoriteArticleDetailData>> getFavoriteArticles(@NonNull int page, @NonNull boolean forceUpdate, @NonNull final boolean clearCache) {
        if (!forceUpdate && favoriteArticlesCache != null) {
            return Observable.fromIterable(new ArrayList<>(favoriteArticlesCache.values()))
                    .toSortedList(new Comparator<FavoriteArticleDetailData>() {
                        @Override
                        public int compare(FavoriteArticleDetailData favoriteArticleDetailData, FavoriteArticleDetailData t1) {
                            return SortDescendUtil.sortFavoriteDetailData(favoriteArticleDetailData, t1);
                        }
                    }).toObservable();
        }

        if (!clearCache&&favoriteArticlesCache!=null) {
            Observable ob1 = Observable.fromIterable(new ArrayList<>(favoriteArticlesCache.values()))
                    .toSortedList(new Comparator<FavoriteArticleDetailData>() {
                        @Override
                        public int compare(FavoriteArticleDetailData favoriteArticleDetailData, FavoriteArticleDetailData t1) {
                            return SortDescendUtil.sortFavoriteDetailData(favoriteArticleDetailData, t1);
                        }
                    }).toObservable();

            Observable ob2=remote.getFavoriteArticles(page, forceUpdate, clearCache)
                    .doOnNext(new Consumer<List<FavoriteArticleDetailData>>() {
                        @Override
                        public void accept(List<FavoriteArticleDetailData> list) throws Exception {
                            refreshArticlesCache(clearCache, list);
                        }
                    });

            return Observable.merge(ob1, ob2)
                    .collect(new Callable<List<FavoriteArticleDetailData>>() {

                        @Override
                        public List<FavoriteArticleDetailData> call() throws Exception {
                            return new ArrayList<>();
                        }
                    }, new BiConsumer<List<FavoriteArticleDetailData>, List<FavoriteArticleDetailData>>() {

                        @Override
                        public void accept(List<FavoriteArticleDetailData> list, List<FavoriteArticleDetailData> dataList) throws Exception {
                            list.addAll(dataList);
                        }
                    }).toObservable();
        }

        return remote.getFavoriteArticles(page, forceUpdate,clearCache)
                .doOnNext(new Consumer<List<FavoriteArticleDetailData>>() {
                    @Override
                    public void accept(List<FavoriteArticleDetailData> list) throws Exception {
                        refreshArticlesCache(clearCache,list);
                    }
                });
    }

    @Override
    public boolean isExist(@NonNull int userId, @NonNull int id) {
        return local.isExist(userId, id);
    }

    private void refreshArticlesCache(@NonNull boolean clearCache, @NonNull List<FavoriteArticleDetailData> list) {
        if (favoriteArticlesCache == null) {
            favoriteArticlesCache = new LinkedHashMap<>();
        }
        if (clearCache) {
            favoriteArticlesCache.clear();
        }
        for (FavoriteArticleDetailData data : list) {
            favoriteArticlesCache.put(data.getOriginId(), data);
        }
    }
}
