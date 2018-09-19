package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.CategoryDetailData;
import com.example.lengary_l.wanandroid.util.SortDescendUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by CoderLengary
 */


public class CategoriesDataRepository implements CategoriesDataSource {
    @NonNull
    private static CategoriesDataRepository INSTANCE;
    private final CategoriesDataSource remoteDataSource;
    private HashMap<Integer, CategoryDetailData> cache;

    private CategoriesDataRepository(@NonNull CategoriesDataSource remoteDataSource){
        this.remoteDataSource = remoteDataSource;
    }
    public static CategoriesDataRepository getInstance(@NonNull CategoriesDataSource remoteDataSource){
        if (INSTANCE==null){
            INSTANCE = new CategoriesDataRepository(remoteDataSource);
        }
        return INSTANCE;
    }
    @Override
    public Observable<List<CategoryDetailData>> getCategories(@NonNull final boolean forceUpdate) {
        if (!forceUpdate){
            return Observable.just(sortCacheItems(new ArrayList<>(cache.values())));
        }

        return remoteDataSource.getCategories(forceUpdate)
                .doOnNext(new Consumer<List<CategoryDetailData>>() {
                    @Override
                    public void accept(List<CategoryDetailData> list) {
                        refreshCache(forceUpdate,list);
                    }
                });
    }

    private void refreshCache(@NonNull boolean forceUpdate, @NonNull List<CategoryDetailData> list){
        if (cache==null){
            cache = new LinkedHashMap<>();
        }
        if (forceUpdate){
            cache.clear();
        }
        for (CategoryDetailData data:list){
            cache.put(data.getId(), data);
        }
    }

    private List<CategoryDetailData> sortCacheItems(@NonNull List<CategoryDetailData> list){
        Collections.sort(list, new Comparator<CategoryDetailData>() {
            @Override
            public int compare(CategoryDetailData categoryDetailData, CategoryDetailData t1) {
                return SortDescendUtil.sortCategoryDetailData(categoryDetailData, t1);
            }
        });
        return list;
    }
}
