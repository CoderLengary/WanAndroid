package com.example.lengary_l.wanandroid.data.source;

import com.example.lengary_l.wanandroid.data.CategoryDetailData;

import java.util.List;

import io.reactivex.Observable;

public class CategoriesDataRepository implements CategoriesDataSource {

    private static CategoriesDataRepository INSTANCE;
    private CategoriesDataSource remote;

    private CategoriesDataRepository(CategoriesDataSource remote){
        this.remote = remote;
    }
    public static CategoriesDataRepository getInstance(CategoriesDataSource remote){
        if (INSTANCE==null){
            INSTANCE = new CategoriesDataRepository(remote);
        }
        return INSTANCE;
    }
    @Override
    public Observable<List<CategoryDetailData>> getCategories() {
        return remote.getCategories();
    }
}
