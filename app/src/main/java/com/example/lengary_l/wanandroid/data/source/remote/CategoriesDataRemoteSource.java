package com.example.lengary_l.wanandroid.data.source.remote;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.CategoryData;
import com.example.lengary_l.wanandroid.data.CategoryDetailData;
import com.example.lengary_l.wanandroid.data.source.CategoriesDataSource;
import com.example.lengary_l.wanandroid.retrofit.RetrofitClient;
import com.example.lengary_l.wanandroid.retrofit.RetrofitService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

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
    public Observable<List<CategoryDetailData>> getCategories( boolean forceUpdate) {
        return RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .getCategories()
                .flatMap(new Function<CategoryData, ObservableSource<List<CategoryDetailData>>>() {
                    @Override
                    public ObservableSource<List<CategoryDetailData>> apply(CategoryData categoryData) throws Exception {
                        return Observable.just(categoryData.getData());
                    }
                });

    }
}
