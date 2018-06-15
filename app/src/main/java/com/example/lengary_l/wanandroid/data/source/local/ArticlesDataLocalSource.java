package com.example.lengary_l.wanandroid.data.source.local;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.ArticleDetailData;
import com.example.lengary_l.wanandroid.data.source.ArticlesDataSource;
import com.example.lengary_l.wanandroid.realm.RealmHelper;

import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.realm.Realm;

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

    @Override
    public Observable<ArticleDetailData> getArticleFromId(@NonNull int id) {

        return null;
    }

    @Override
    public void addToReadLater(int currentUserId, int articleId, boolean readLater) {
       /* Realm realm = RealmHelper.newRealmInstance();
        ArticleDetailData article = realm.copyFromRealm(realm.where(ArticleDetailData.class)
                .equalTo("id", articleId)
                .findFirst());
        if (article != null) {
            article.setCurrentUserId(currentUserId);
            article.setReadLater(readLater);
        }
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(article);
        realm.commitTransaction();
        realm.close();
        */
    }

    @Override
    public Observable<List<ArticleDetailData>> getArticlesFromReadLater(int currentUserId, int articleId) {
        Realm realm = RealmHelper.newRealmInstance();
        List<ArticleDetailData> data = realm.copyFromRealm(realm.where(ArticleDetailData.class)
                .equalTo("currentUserId", currentUserId)
                .and()
                .equalTo("readLater", true)
                .findAll());
        return Observable.fromIterable(data).toSortedList(new Comparator<ArticleDetailData>() {
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
}
