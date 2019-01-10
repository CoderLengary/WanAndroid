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
    public Observable<List<ArticleDetailData>> getArticles(@NonNull final int page, @NonNull final boolean forceUpdate, @NonNull final boolean clearCache) {

        //!forceUpdate即用户按home键然后再返回我们的APP的情况，这时候直接返回缓存的文章列表
        if (!forceUpdate && articlesCache != null) {
            return Observable.fromIterable(new ArrayList<>(articlesCache.values()))
                    .toSortedList(new Comparator<ArticleDetailData>() {
                        @Override
                        public int compare(ArticleDetailData articleDetailData, ArticleDetailData t1) {
                            return SortDescendUtil.sortArticleDetailData(articleDetailData, t1);
                        }
                    }).toObservable();
        }

        //forceUpdate&&!clearCache 即用户向下滑动列表的情况，我们需要请求下一页的数据，并保存到缓存里
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

            //获取到缓存的数据加上新请求的下一页的数据，需要结合这两个数据并统一发送
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

        //forceUpdate&&clearCache 即下拉刷新，还有第一次加载的情况
        return remoteDataSource.getArticles(INDEX, forceUpdate, clearCache)
                .doOnNext(new Consumer<List<ArticleDetailData>>() {
                    @Override
                    public void accept(List<ArticleDetailData> list) {
                        refreshArticlesCache(clearCache, list);
                    }
                });

    }



    private void refreshArticlesCache(@NonNull boolean clearCache, @NonNull List<ArticleDetailData> list){
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
    public Observable<List<ArticleDetailData>> queryArticles(@NonNull int page, @NonNull String keyWords, @NonNull boolean forceUpdate, @NonNull final boolean clearCache) {

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




    private void refreshQueryCache(@NonNull boolean clearCache, @NonNull List<ArticleDetailData> list) {
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
    public Observable<List<ArticleDetailData>> getArticlesFromCatg(@NonNull int page, @NonNull int categoryId, @NonNull final boolean forceUpdate, @NonNull final boolean clearCache) {
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

    private void refreshCategoryCache(@NonNull boolean clearCache, @NonNull List<ArticleDetailData> list) {
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
