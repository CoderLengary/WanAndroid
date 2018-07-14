package com.example.lengary_l.wanandroid.data.source.local;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.ArticleDetailData;
import com.example.lengary_l.wanandroid.data.ReadLaterArticleData;
import com.example.lengary_l.wanandroid.data.source.ReadLaterArticlesDataSource;
import com.example.lengary_l.wanandroid.realm.RealmHelper;

import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by CoderLengary
 */


public class ReadLaterArticlesLocalSource implements ReadLaterArticlesDataSource {
    @NonNull
    private static ReadLaterArticlesLocalSource INSTANCE;

    private ReadLaterArticlesLocalSource() {

    }

    public static ReadLaterArticlesDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ReadLaterArticlesLocalSource();
        }
        return INSTANCE;
    }


    @Override
    public Observable<List<ReadLaterArticleData>> getReadLaterArticles(@NonNull int userId) {
        Realm realm = RealmHelper.newRealmInstance();
        List<ReadLaterArticleData> datas=realm.copyFromRealm(realm.where(ReadLaterArticleData.class)
                .equalTo("userId", userId)
                .findAll());
        return Observable.fromIterable(datas).toSortedList(new Comparator<ReadLaterArticleData>() {
            @Override
            public int compare(ReadLaterArticleData data, ReadLaterArticleData t1) {
                if (data.getTimestamp() > t1.getTimestamp()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }).toObservable();

    }

    @Override
    public void insertReadLaterArticle(@NonNull int userId, @NonNull int id, @NonNull long timeStamp) {
        Realm realm = RealmHelper.newRealmInstance();
        ArticleDetailData articleDetailData = realm.copyFromRealm(
                realm.where(ArticleDetailData.class)
                        .equalTo("id", id)
                        .findFirst()
        );
        ReadLaterArticleData data = new ReadLaterArticleData();
        data.setId(id);
        data.setTitle(articleDetailData.getTitle());
        data.setLink(articleDetailData.getLink());
        data.setAuthor(articleDetailData.getAuthor());
        data.setChapterId(articleDetailData.getChapterId());
        data.setChapterName(articleDetailData.getChapterName());
        data.setUserId(userId);
        data.setTimestamp(timeStamp);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(data);
        realm.commitTransaction();
        realm.close();

    }

    @Override
    public void removeReadLaterArticle(@NonNull int userId, @NonNull int id) {
        Realm realm = RealmHelper.newRealmInstance();
        ReadLaterArticleData data = realm.where(ReadLaterArticleData.class)
                .equalTo("userId", userId)
                .and()
                .equalTo("id", id)
                .findFirst();
        realm.beginTransaction();
        data.deleteFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public boolean isExist(@NonNull int userId, @NonNull int id) {
        Realm realm = RealmHelper.newRealmInstance();
        RealmResults<ReadLaterArticleData> list =
                realm.where(ReadLaterArticleData.class)
                        .equalTo("userId", userId)
                        .and()
                        .equalTo("id", id)
                        .findAll();
        return !list.isEmpty();

    }

    @Override
    public void clearAll() {
        Realm realm = RealmHelper.newRealmInstance();
        RealmResults<ReadLaterArticleData> results =
                realm.where(ReadLaterArticleData.class).findAll();
        realm.beginTransaction();
        results.deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
    }
}
