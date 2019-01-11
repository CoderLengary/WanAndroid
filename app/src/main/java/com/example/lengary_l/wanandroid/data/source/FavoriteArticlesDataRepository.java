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
    private final FavoriteArticlesDataSource remoteDataSource;
    private final FavoriteArticlesDataSource localDataSource;
    private Map<Integer, FavoriteArticleDetailData> favoriteArticlesCache;

    private static FavoriteArticlesDataRepository INSTANCE;

    private FavoriteArticlesDataRepository(@NonNull FavoriteArticlesDataSource remoteDataSource, @NonNull FavoriteArticlesDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }
    public static FavoriteArticlesDataRepository getInstance(@NonNull FavoriteArticlesDataSource remoteDataSource,@NonNull FavoriteArticlesDataSource localDataSource) {
        if (INSTANCE == null) {
            synchronized (FavoriteArticlesDataRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavoriteArticlesDataRepository(remoteDataSource, localDataSource);
                }
            }
        }
        return INSTANCE;
    }


    @Override
    public Observable<List<FavoriteArticleDetailData>> getFavoriteArticles(int page, boolean forceUpdate, final boolean clearCache) {
        if (!forceUpdate && favoriteArticlesCache != null) {
            return Observable.fromIterable(new ArrayList<>(favoriteArticlesCache.values()))
                    .toSortedList(new Comparator<FavoriteArticleDetailData>() {
                        @Override
                        public int compare(FavoriteArticleDetailData favoriteArticleDetailData, FavoriteArticleDetailData t1) {
                            return SortDescendUtil.sortFavoriteDetailData(favoriteArticleDetailData, t1);
                        }
                    }).toObservable();
        }

        //forceUpdate&&!clearCache: When scrolling to the last item of recycler view, we need to request the data of next page and cache it.
        if (!clearCache&&favoriteArticlesCache!=null) {
            Observable ob1 = Observable.fromIterable(new ArrayList<>(favoriteArticlesCache.values()))
                    .toSortedList(new Comparator<FavoriteArticleDetailData>() {
                        @Override
                        public int compare(FavoriteArticleDetailData favoriteArticleDetailData, FavoriteArticleDetailData t1) {
                            return SortDescendUtil.sortFavoriteDetailData(favoriteArticleDetailData, t1);
                        }
                    }).toObservable();

            Observable ob2=remoteDataSource.getFavoriteArticles(page, forceUpdate, clearCache)
                    .doOnNext(new Consumer<List<FavoriteArticleDetailData>>() {
                        @Override
                        public void accept(List<FavoriteArticleDetailData> list) {
                            refreshArticlesCache(clearCache, list);
                        }
                    });

            //We need to return the data combining network source and cache source
            return Observable.merge(ob1, ob2)
                    .collect(new Callable<List<FavoriteArticleDetailData>>() {

                        @Override
                        public List<FavoriteArticleDetailData> call() {
                            return new ArrayList<>();
                        }
                    }, new BiConsumer<List<FavoriteArticleDetailData>, List<FavoriteArticleDetailData>>() {

                        @Override
                        public void accept(List<FavoriteArticleDetailData> list, List<FavoriteArticleDetailData> dataList) {
                            list.addAll(dataList);
                        }
                    }).toObservable();
        }

        //forceUpdate&&clearCache: Pull-to-refresh.Initial Article Fragment.
        return remoteDataSource.getFavoriteArticles(page, forceUpdate,clearCache)
                .doOnNext(new Consumer<List<FavoriteArticleDetailData>>() {
                    @Override
                    public void accept(List<FavoriteArticleDetailData> list) {
                        refreshArticlesCache(clearCache,list);
                    }
                });
    }

    @Override
    public boolean isExist(int userId, int id) {
        return localDataSource.isExist(userId, id);
    }

    private void refreshArticlesCache(boolean clearCache, @NonNull List<FavoriteArticleDetailData> list) {
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
