package com.example.lengary_l.wanandroid.data.source;

import com.example.lengary_l.wanandroid.data.ArticleCatgDetailData;

import java.util.List;

import io.reactivex.Observable;

public interface ArticleCatgDataSource {

    Observable<List<ArticleCatgDetailData>> getArticleCatg();
}
