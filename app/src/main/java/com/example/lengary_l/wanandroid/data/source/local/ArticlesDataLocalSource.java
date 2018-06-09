package com.example.lengary_l.wanandroid.data.source.local;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.ArticleDetailData;
import com.example.lengary_l.wanandroid.data.source.ArticlesDataSource;

import java.util.List;

import io.reactivex.Observable;

public class ArticlesDataLocalSource implements ArticlesDataSource {

    @NonNull
    private static ArticlesDataLocalSource INSTANCE;

    private ArticlesDataLocalSource(){

    }

    public static ArticlesDataLocalSource getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new ArticlesDataLocalSource();
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<ArticleDetailData>> getArticles(@NonNull int page, boolean forceUpdate, boolean clearCache) {

       //The remote has handled it
        return null;

    }

    @Override
    public Observable<List<ArticleDetailData>> queryArticles(@NonNull int page, @NonNull String keyWords, boolean forceUpdate, boolean clearCache) {
        //The remote has handled it
        return null;
    }
}
