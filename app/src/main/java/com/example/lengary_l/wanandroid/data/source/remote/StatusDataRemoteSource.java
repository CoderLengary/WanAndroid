package com.example.lengary_l.wanandroid.data.source.remote;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.LoginDetailData;
import com.example.lengary_l.wanandroid.data.Status;
import com.example.lengary_l.wanandroid.data.source.StatusDataSource;
import com.example.lengary_l.wanandroid.realm.RealmHelper;
import com.example.lengary_l.wanandroid.retrofit.RetrofitClient;
import com.example.lengary_l.wanandroid.retrofit.RetrofitService;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

/**
 * Created by CoderLengary
 */


public class StatusDataRemoteSource implements StatusDataSource {

    private static StatusDataRemoteSource INSTANCE;

    private StatusDataRemoteSource() {

    }

    public static StatusDataRemoteSource getInstance() {
        if (INSTANCE == null) {
            synchronized (StatusDataRemoteSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new StatusDataRemoteSource();
                }
            }
        }
        return INSTANCE;
    }


    @Override
    public Observable<Status> collectArticle(final int userId, final int id) {
        return RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .collectArticle(id)
                .filter(new Predicate<Status>() {
                    @Override
                    public boolean test(Status status) {
                        return status.getErrorCode() == 0;
                    }
                })
                .doOnNext(new Consumer<Status>() {
                    @Override
                    public void accept(Status status) {
                        // It is necessary to build a new realm instance
                        // in a different thread.
                        Realm realm = Realm.getInstance(new RealmConfiguration.Builder()
                                .deleteRealmIfMigrationNeeded()
                                .name(RealmHelper.DATABASE_NAME)
                                .build());
                        //When we bookmark this article successfully
                        //, we should update the bookmark state of the article in database.
                        LoginDetailData data = realm.copyFromRealm(
                                realm.where(LoginDetailData.class)
                                        .equalTo("id", userId)
                                        .findFirst()
                        );
                        RealmList<Integer> collectIds = data.getCollectIds();
                        if (!checkIsFavorite(id, collectIds)) {
                            Integer integer = id;
                            collectIds.add(integer);
                            data.setCollectIds(collectIds);
                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(data);
                            realm.commitTransaction();
                            realm.close();
                        }
                    }
                });
    }

    @Override
    public Observable<Status> uncollectArticle(final int userId, final int originId) {
        return RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .uncollectArticle(originId)
                .filter(new Predicate<Status>() {
                    @Override
                    public boolean test(Status status) {
                        return status.getErrorCode() == 0;
                    }
                })
                .doOnNext(new Consumer<Status>() {
                    @Override
                    public void accept(Status status) {
                        // It is necessary to build a new realm instance
                        // in a different thread.
                        Realm realm = Realm.getInstance(new RealmConfiguration.Builder()
                                .deleteRealmIfMigrationNeeded()
                                .name(RealmHelper.DATABASE_NAME)
                                .build());
                        LoginDetailData data = realm.copyFromRealm(
                                realm.where(LoginDetailData.class)
                                        .equalTo("id", userId)
                                        .findFirst()
                        );
                        //When we cancel bookmark this article successfully
                        //, we should update the bookmark state of the article in database.
                        RealmList<Integer> collectIds = data.getCollectIds();
                        if (checkIsFavorite(originId, collectIds)) {
                            Integer integer = originId;
                            collectIds.remove(integer);
                            data.setCollectIds(collectIds);
                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(data);
                            realm.commitTransaction();
                            realm.close();
                        }
                    }
                });
    }

    private boolean checkIsFavorite(int articleId, @NonNull RealmList<Integer> collectIds) {
        if (collectIds.isEmpty()) {
            return false;
        }
        for (Integer integer : collectIds) {
            if (integer == articleId) {
                return true;
            }
        }
        return false;
    }

}
