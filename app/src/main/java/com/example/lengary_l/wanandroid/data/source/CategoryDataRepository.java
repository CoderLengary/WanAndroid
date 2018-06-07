package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.ArticleDetailData;

import java.util.List;

import io.reactivex.Observable;

public class CategoryDataRepository implements CategoryDataSource {

    private CategoryDataSource remote;

    @NonNull
    private static CategoryDataRepository INSTANCE;

    private CategoryDataRepository(CategoryDataSource remote) {
        this.remote = remote;
    }

    @Override

    public Observable<List<ArticleDetailData>> getArticlesFromCatg(int page, String categoryId, boolean forceUpdate) {
        return remote.getArticlesFromCatg(page, categoryId, forceUpdate);
    }
}
