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
       /* Realm realm = RealmHelper.newRealmInstance();
        return Observable.just(
                realm.copyFromRealm(realm.where(ArticleDetailData.class).findAll().sort("publishTime", Sort.DESCENDING))
        );*/
       //The remote has handle it
        return null;

    }
}
