package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.CategoryDetailData;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by CoderLengary
 */


public interface CategoriesDataSource {

    Observable<List<CategoryDetailData>> getCategories(@NonNull boolean forceUpdate);
}
