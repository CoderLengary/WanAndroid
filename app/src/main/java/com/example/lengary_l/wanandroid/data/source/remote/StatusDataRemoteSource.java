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
    @NonNull
    private static StatusDataRemoteSource INSTANCE;
    private StatusDataRemoteSource() {

    }

    public static StatusDataRemoteSource getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new StatusDataRemoteSource();
        }
        return INSTANCE;
    }


    @Override
    public Observable<Status> collectArticle(@NonNull final int userId, @NonNull  final int id) {
        return RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .collectArticle(id)
                .filter(new Predicate<Status>() {
                    @Override
                    public boolean test(Status status) throws Exception {
                        return status.getErrorCode() != -1;
                    }
                })
                .doOnNext(new Consumer<Status>() {
                    @Override
                    public void accept(Status status) throws Exception {
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
                        RealmList<Integer> collectIds = data.getCollectIds();
                        if (!checkIsFavorite(id, collectIds)) {
                            Integer integer = new Integer(id);
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
    public Observable<Status> uncollectArticle(@NonNull final int userId,@NonNull  final int originId) {
        return RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .uncollectArticle(originId)
                .filter(new Predicate<Status>() {
                    @Override
                    public boolean test(Status status) throws Exception {
                        return status.getErrorCode() != -1;
                    }
                })
                .doOnNext(new Consumer<Status>() {
                    @Override
                    public void accept(Status status) throws Exception {
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
                        RealmList<Integer> collectIds = data.getCollectIds();
                        if (checkIsFavorite(originId, collectIds)) {
                            Integer integer = new Integer(originId);
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

    private boolean checkIsFavorite(@NonNull int articleId,@NonNull  RealmList<Integer> collectIds) {
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
