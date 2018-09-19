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
    @NonNull
    private static FavoriteArticlesDataRepository INSTANCE;

    private FavoriteArticlesDataRepository(@NonNull FavoriteArticlesDataSource remoteDataSource, @NonNull FavoriteArticlesDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }
    public static FavoriteArticlesDataRepository getInstance(@NonNull FavoriteArticlesDataSource remoteDataSource,@NonNull FavoriteArticlesDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new FavoriteArticlesDataRepository(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }


    @Override
    public Observable<List<FavoriteArticleDetailData>> getFavoriteArticles(@NonNull int page, @NonNull boolean forceUpdate, @NonNull final boolean clearCache) {
        //!forceUpdate即用户按home键然后再返回我们的APP的情况，这时候直接返回缓存的文章列表
        if (!forceUpdate && favoriteArticlesCache != null) {
            return Observable.fromIterable(new ArrayList<>(favoriteArticlesCache.values()))
                    .toSortedList(new Comparator<FavoriteArticleDetailData>() {
                        @Override
                        public int compare(FavoriteArticleDetailData favoriteArticleDetailData, FavoriteArticleDetailData t1) {
                            return SortDescendUtil.sortFavoriteDetailData(favoriteArticleDetailData, t1);
                        }
                    }).toObservable();
        }

        //forceUpdate&&!clearCache 即用户向下滑动列表的情况，我们需要请求下一页的数据，并保存到缓存里
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

            //获取到缓存的数据加上新请求的下一页的数据，需要结合这两个数据并统一发送
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

        //forceUpdate&&clearCache 即下拉刷新，还有第一次加载的情况
        return remoteDataSource.getFavoriteArticles(page, forceUpdate,clearCache)
                .doOnNext(new Consumer<List<FavoriteArticleDetailData>>() {
                    @Override
                    public void accept(List<FavoriteArticleDetailData> list) {
                        refreshArticlesCache(clearCache,list);
                    }
                });
    }

    @Override
    public boolean isExist(@NonNull int userId, @NonNull int id) {
        return localDataSource.isExist(userId, id);
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
