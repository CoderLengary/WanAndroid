package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.lengary_l.wanandroid.data.ArticleDetailData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class ArticlesDataRepository implements ArticlesDataSource {

    @NonNull
    private ArticlesDataSource remoteDataSource;

    @NonNull
    private ArticlesDataSource localDataSource;

    private Map<Integer, ArticleDetailData> articlesCache;

    private Map<Integer, ArticleDetailData> queryCache;

    private final int INDEX = 0;

    @NonNull
    public static ArticlesDataRepository INSTANCE;

    private static final String TAG = "ArticlesDataRepository";

    private ArticlesDataRepository(@NonNull ArticlesDataSource remoteDataSource , @NonNull ArticlesDataSource localDataSource){
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }


    public static ArticlesDataRepository getInstance(@NonNull ArticlesDataSource remoteDataSource,@NonNull ArticlesDataSource localDataSource){
        if (INSTANCE == null) {
            INSTANCE = new ArticlesDataRepository(remoteDataSource,localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<ArticleDetailData>> getArticles(@NonNull final int page, final boolean forceUpdate, final boolean clearCache) {


        //If we do not force update, and the cache is not null, get the cacheItems.
        //退出，再次进入的逻辑
        if (!forceUpdate && articlesCache != null) {
            Log.e(TAG, "getArticles: No forceUpdate " );
            List<ArticleDetailData> cacheItems = new ArrayList<>(articlesCache.values());
            return Observable.fromIterable(cacheItems).toSortedList(new Comparator<ArticleDetailData>() {
                @Override
                public int compare(ArticleDetailData articleDetailData, ArticleDetailData t1) {
                    if (articleDetailData.getPublishTime() > t1.getPublishTime()){
                        return -1;
                    }else {
                        return 1;
                    }
                }
            }).toObservable();
        }

        //if we slide the recycle view,we force update and do not clear the cache,get the articles from remote and cache.
        //if we force update and do not clear the cache,get the articles from remote and cache.
        //滑动向下的处理逻辑

        if (!clearCache&&articlesCache!=null){
            Log.e(TAG, "getArticles: update not clearcache, page is "+page);

            List<ArticleDetailData> cacheItems = new ArrayList<>(articlesCache.values());
            Observable<List<ArticleDetailData>> ob1 = Observable.just(cacheItems);
            Observable<List<ArticleDetailData>> ob2 = remoteDataSource.getArticles(page, forceUpdate, clearCache)
                    .doOnNext(new Consumer<List<ArticleDetailData>>() {
                        @Override
                        public void accept(List<ArticleDetailData> list) throws Exception {
                            refreshArticlesCache(clearCache, list);
                        }
                    });

            return Observable.merge(ob1, ob2).onErrorResumeNext(new Function<Throwable, ObservableSource<? extends List<ArticleDetailData>>>() {
                @Override
                public ObservableSource<? extends List<ArticleDetailData>> apply(Throwable throwable) throws Exception {
                    return localDataSource.getArticles(INDEX, forceUpdate, clearCache);
                }
            });
        }

        //when we refresh the layout,the page is zero,and we force update and clear all the caches.
        //forceupdate,clearcache,下拉刷新，第一次打开应用逻辑,page都是等于0
        Log.e(TAG, "getArticles: update clearcache" );
        return remoteDataSource.getArticles(INDEX, forceUpdate, clearCache)
                .doOnNext(new Consumer<List<ArticleDetailData>>() {
                    @Override
                    public void accept(List<ArticleDetailData> list) throws Exception {
                        refreshArticlesCache(clearCache, list);
                    }
                });

    }



    private void refreshArticlesCache(boolean clearCache,List<ArticleDetailData> list){
        if (articlesCache == null) {
            articlesCache = new LinkedHashMap<>();
        }
        if (clearCache){
            articlesCache.clear();
        }
        for (ArticleDetailData item : list) {
            articlesCache.put(item.getId(), item);
        }

        Log.e(TAG, "refreshCache: cacheSize "+articlesCache.size() );
    }

    @Override
    public Observable<List<ArticleDetailData>> queryArticles(@NonNull int page, @NonNull String keyWords, boolean forceUpdate, final boolean clearCache) {

        if (!forceUpdate && queryCache != null) {
            return Observable.fromIterable(new ArrayList<>(queryCache.values()))
                    .toSortedList(new Comparator<ArticleDetailData>() {
                        @Override
                        public int compare(ArticleDetailData articleDetailData, ArticleDetailData t1) {
                            if (articleDetailData.getPublishTime() > t1.getPublishTime()) {
                                return -1;
                            } else {
                                return 1;
                            }
                        }
                    }).toObservable();
        }

        if (!clearCache && queryCache != null) {
            List<ArticleDetailData> cacheItems = new ArrayList<>(queryCache.values());
            Observable ob1 = Observable.just(cacheItems);
            Observable ob2 = remoteDataSource.queryArticles(page, keyWords, forceUpdate, clearCache)
                    .doOnNext(new Consumer<List<ArticleDetailData>>() {
                        @Override
                        public void accept(List<ArticleDetailData> list) throws Exception {
                            refreshQueryCache(clearCache, list);
                        }
                    });
            return Observable.merge(ob1, ob2);
        }

        //force update and clear cache
        return remoteDataSource.queryArticles(page, keyWords, forceUpdate,clearCache)
                .doOnNext(new Consumer<List<ArticleDetailData>>() {
                    @Override
                    public void accept(List<ArticleDetailData> list) throws Exception {
                        refreshQueryCache(clearCache, list);
                    }
                });
    }

    private void refreshQueryCache(boolean clearCache,List<ArticleDetailData> list) {
        if (queryCache == null) {
            queryCache = new LinkedHashMap<>();
        }
        if (clearCache) {
            queryCache.clear();
        }
        for (ArticleDetailData item : list) {
            queryCache.put(item.getId(), item);
        }

    }

    /*private List<ArticleDetailData> sortCacheItems(List<ArticleDetailData> list){
        if (list==null||list.isEmpty()){
            return null;
        }
        Collections.sort(list, new Comparator<ArticleDetailData>() {
            @Override
            public int compare(ArticleDetailData articleDetailData, ArticleDetailData t1) {
                if (articleDetailData.getId() > t1.getId()){
                    return -1;
                }else {
                    return 1;
                }
            }
        });
        return list;
    }*/
}
