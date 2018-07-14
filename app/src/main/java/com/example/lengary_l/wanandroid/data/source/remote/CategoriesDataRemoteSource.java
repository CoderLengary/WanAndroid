package com.example.lengary_l.wanandroid.data.source.remote;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.CategoriesData;
import com.example.lengary_l.wanandroid.data.CategoryDetailData;
import com.example.lengary_l.wanandroid.data.source.CategoriesDataSource;
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


public class CategoriesDataRemoteSource implements CategoriesDataSource{

    @NonNull
    private static CategoriesDataRemoteSource INSTANCE;

    private CategoriesDataRemoteSource() {

    }

    public static CategoriesDataRemoteSource getInstance(){
        if (INSTANCE==null){
            INSTANCE = new CategoriesDataRemoteSource();
        }
        return INSTANCE;
    }



    @Override
    public Observable<List<CategoryDetailData>> getCategories(@NonNull boolean forceUpdate) {
        return RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .getCategories()
                .filter(new Predicate<CategoriesData>() {
                    @Override
                    public boolean test(CategoriesData categoriesData) throws Exception {
                        return categoriesData.getErrorCode() != -1;
                    }
                })
                .flatMap(new Function<CategoriesData, ObservableSource<List<CategoryDetailData>>>() {
                    @Override
                    public ObservableSource<List<CategoryDetailData>> apply(CategoriesData categoriesData) throws Exception {
                        return Observable.fromIterable(categoriesData.getData()).toSortedList(new Comparator<CategoryDetailData>() {
                            @Override
                            public int compare(CategoryDetailData categoryDetailData, CategoryDetailData t1) {
                                return SortDescendUtil.sortCategoryDetailData(categoryDetailData, t1);
                            }
                        }).toObservable();
                    }
                });

    }
}
