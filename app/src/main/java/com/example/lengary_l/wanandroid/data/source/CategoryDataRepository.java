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
import io.reactivex.functions.Consumer;

public class CategoryDataRepository implements CategoryDataSource {

    private CategoryDataSource remote;
    private Map<Integer, ArticleDetailData> cache;
    private static final String TAG = "CategoryDataRepository";

    @NonNull
    private static CategoryDataRepository INSTANCE;

    private CategoryDataRepository(CategoryDataSource remote) {
        this.remote = remote;
    }

    public static CategoryDataRepository getInstance(CategoryDataSource remote) {
        if (INSTANCE == null) {
            INSTANCE = new CategoryDataRepository(remote);
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<ArticleDetailData>> getArticlesFromCatg(int page, int categoryId, final boolean forceUpdate, final boolean clearCache) {
        if (!forceUpdate&&cache!=null){
            List<ArticleDetailData> cacheItems = new ArrayList<>(cache.values());
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

        if (!clearCache&&cache!=null){
            List<ArticleDetailData> cacheItems = new ArrayList<>(cache.values());
            Observable<List<ArticleDetailData>> ob1 = Observable.just(cacheItems);
            Observable<List<ArticleDetailData>> ob2 = remote.getArticlesFromCatg(page, categoryId, forceUpdate, clearCache)
                    .doOnNext(new Consumer<List<ArticleDetailData>>() {
                        @Override
                        public void accept(List<ArticleDetailData> list) throws Exception {
                            refreshCache(clearCache, list);
                        }
                    });
            return Observable.merge(ob1, ob2);

        }

        Log.e(TAG, "getArticlesFromCatg: force update and clear cache" );
        Log.e(TAG, "getArticlesFromCatg: getArticlesFromCatg: categoryId is "+categoryId+ " page is "+page  );
        return remote.getArticlesFromCatg(page, categoryId, forceUpdate,clearCache)
                .doOnNext(new Consumer<List<ArticleDetailData>>() {
                    @Override
                    public void accept(List<ArticleDetailData> list) throws Exception {
                        Log.e(TAG, "accept: list size ="+list.size());
                        refreshCache(clearCache, list);
                    }
                });
    }


    private void refreshCache(boolean clearCache,List<ArticleDetailData> list) {
        if (cache == null) {
            cache = new LinkedHashMap<>();
        }
        if (clearCache) {
            cache.clear();
        }
        for (ArticleDetailData item : list) {
            cache.put(item.getId(), item);
        }
    }
}
