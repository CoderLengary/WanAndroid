package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.ArticleDetailData;
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


public class ArticlesDataRepository implements ArticlesDataSource {

    @NonNull
    private final ArticlesDataSource remoteDataSource;

    private Map<Integer, ArticleDetailData> articlesCache;

    private Map<Integer, ArticleDetailData> queryCache;

    private Map<Integer, ArticleDetailData> categoryCache;
    private final int INDEX = 0;


    private static ArticlesDataRepository INSTANCE;

    private ArticlesDataRepository(@NonNull ArticlesDataSource remoteDataSource ){
        this.remoteDataSource = remoteDataSource;
    }


    public static ArticlesDataRepository getInstance(@NonNull ArticlesDataSource remoteDataSource){
        if (INSTANCE == null) {
            synchronized (ArticlesDataRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ArticlesDataRepository(remoteDataSource);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<ArticleDetailData>> getArticles(final int page, final boolean forceUpdate, final boolean clearCache) {

        if (!forceUpdate && articlesCache != null) {
            return Observable.fromIterable(new ArrayList<>(articlesCache.values()))
                    .toSortedList(new Comparator<ArticleDetailData>() {
                        @Override
                        public int compare(ArticleDetailData articleDetailData, ArticleDetailData t1) {
                            return SortDescendUtil.sortArticleDetailData(articleDetailData, t1);
                        }
                    }).toObservable();
        }

        //forceUpdate&&!clearCache: When scrolling to the last item of recycler view, we need to request the data of next page and cache it.
        if (!clearCache&&articlesCache!=null){
            Observable<List<ArticleDetailData>> ob1 = Observable.fromIterable(new ArrayList<>(articlesCache.values()))
                    .toSortedList(new Comparator<ArticleDetailData>() {
                        @Override
                        public int compare(ArticleDetailData articleDetailData, ArticleDetailData t1) {
                            return SortDescendUtil.sortArticleDetailData(articleDetailData, t1);
                        }
                    }).toObservable();

            Observable<List<ArticleDetailData>> ob2 = remoteDataSource.getArticles(page, forceUpdate, clearCache)
                    .doOnNext(new Consumer<List<ArticleDetailData>>() {
                        @Override
                        public void accept(List<ArticleDetailData> list) {
                            refreshArticlesCache(clearCache, list);
                        }
                    });

            //We need to return the data combining network source and cache source
            return Observable.merge(ob1, ob2).collect(new Callable<List<ArticleDetailData>>() {
                @Override
                public List<ArticleDetailData> call() {
                    return new ArrayList<>();
                }
            }, new BiConsumer<List<ArticleDetailData>, List<ArticleDetailData>>() {
                @Override
                public void accept(List<ArticleDetailData> list, List<ArticleDetailData> dataList) {
                    list.addAll(dataList);
                }
            }).toObservable();
        }

        //forceUpdate&&clearCache: Pull-to-refresh.Initial Article Fragment.
        return remoteDataSource.getArticles(INDEX, forceUpdate, clearCache)
                .doOnNext(new Consumer<List<ArticleDetailData>>() {
                    @Override
                    public void accept(List<ArticleDetailData> list) {
                        refreshArticlesCache(clearCache, list);
                    }
                });

    }



    private void refreshArticlesCache(boolean clearCache, @NonNull List<ArticleDetailData> list){
        if (articlesCache == null) {
            articlesCache = new LinkedHashMap<>();
        }
        if (clearCache){
            articlesCache.clear();
        }
        for (ArticleDetailData item : list) {
            articlesCache.put(item.getId(), item);
        }

    }

    @Override
    public Observable<List<ArticleDetailData>> queryArticles(int page, @NonNull String keyWords, boolean forceUpdate, final boolean clearCache) {

        if (!forceUpdate && queryCache != null) {
            return Observable.fromIterable(new ArrayList<>(queryCache.values()))
                    .toSortedList(new Comparator<ArticleDetailData>() {
                        @Override
                        public int compare(ArticleDetailData articleDetailData, ArticleDetailData t1) {
                            return SortDescendUtil.sortArticleDetailData(articleDetailData, t1);
                        }
                    }).toObservable();
        }

        if (!clearCache && queryCache != null) {
            Observable ob1 = Observable.fromIterable(new ArrayList<>(queryCache.values()))
                    .toSortedList(new Comparator<ArticleDetailData>() {
                        @Override
                        public int compare(ArticleDetailData articleDetailData, ArticleDetailData t1) {
                            return SortDescendUtil.sortArticleDetailData(articleDetailData, t1);
                        }
                    }).toObservable();

            Observable ob2 = remoteDataSource.queryArticles(page, keyWords, forceUpdate, clearCache)
                    .doOnNext(new Consumer<List<ArticleDetailData>>() {
                        @Override
                        public void accept(List<ArticleDetailData> list) {
                            refreshQueryCache(clearCache, list);
                        }
                    });

            return Observable.merge(ob1, ob2).collect(new Callable<List<ArticleDetailData>>() {

                @Override
                public List<ArticleDetailData> call() {
                    return new ArrayList<>();
                }

            }, new BiConsumer<List<ArticleDetailData>, List<ArticleDetailData>>() {
                @Override
                public void accept(List<ArticleDetailData> list, List<ArticleDetailData> dataList) {
                    list.addAll(dataList);
                }
            }).toObservable();
        }

        return remoteDataSource.queryArticles(page, keyWords, forceUpdate,clearCache)
                .doOnNext(new Consumer<List<ArticleDetailData>>() {
                    @Override
                    public void accept(List<ArticleDetailData> list) {
                        refreshQueryCache(clearCache, list);
                    }
                });
    }




    private void refreshQueryCache(boolean clearCache, @NonNull List<ArticleDetailData> list) {
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


    @Override
    public Observable<List<ArticleDetailData>> getArticlesFromCatg(int page, int categoryId, final boolean forceUpdate, final boolean clearCache) {
        if (!forceUpdate&&categoryCache!=null){
            return Observable.fromIterable(new ArrayList<>(categoryCache.values()))
                    .toSortedList(new Comparator<ArticleDetailData>() {
                        @Override
                        public int compare(ArticleDetailData articleDetailData, ArticleDetailData t1) {
                            return SortDescendUtil.sortArticleDetailData(articleDetailData, t1);
                        }
                    }).toObservable();
        }

        if (!clearCache&&categoryCache!=null){

            Observable<List<ArticleDetailData>> ob1 = Observable.fromIterable(new ArrayList<>(categoryCache.values()))
                    .toSortedList(new Comparator<ArticleDetailData>() {
                        @Override
                        public int compare(ArticleDetailData articleDetailData, ArticleDetailData t1) {
                            return SortDescendUtil.sortArticleDetailData(articleDetailData, t1);
                        }
                    }).toObservable();

            Observable<List<ArticleDetailData>> ob2 = remoteDataSource.getArticlesFromCatg(page, categoryId, forceUpdate, clearCache)
                    .doOnNext(new Consumer<List<ArticleDetailData>>() {
                        @Override
                        public void accept(List<ArticleDetailData> list) {
                            refreshCategoryCache(clearCache, list);
                        }
                    });

            return Observable.merge(ob1, ob2)
                    .collect(new Callable<List<ArticleDetailData>>() {

                        @Override
                        public List<ArticleDetailData> call() {
                            return new ArrayList<>();
                        }

                    }, new BiConsumer<List<ArticleDetailData>, List<ArticleDetailData>>() {
                        @Override
                        public void accept(List<ArticleDetailData> list, List<ArticleDetailData> dataList) {
                            list.addAll(dataList);
                        }
                    }).toObservable();
        }

        return remoteDataSource.getArticlesFromCatg(page, categoryId, forceUpdate,clearCache)
                .doOnNext(new Consumer<List<ArticleDetailData>>() {
                    @Override
                    public void accept(List<ArticleDetailData> list) {
                        refreshCategoryCache(clearCache, list);
                    }
                });
    }

    private void refreshCategoryCache(boolean clearCache, @NonNull List<ArticleDetailData> list) {
        if (categoryCache == null) {
            categoryCache = new LinkedHashMap<>();
        }
        if (clearCache) {
            categoryCache.clear();
        }
        for (ArticleDetailData item : list) {
            categoryCache.put(item.getId(), item);
        }
    }

}
