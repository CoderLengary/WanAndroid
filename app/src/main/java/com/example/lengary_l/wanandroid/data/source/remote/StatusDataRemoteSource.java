package com.example.lengary_l.wanandroid.data.source.remote;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.ArticleDetailData;
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
    public Observable<Status> collectArticle(final int id) {
        return RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .collectArticle(id)
                .filter(new Predicate<Status>() {
                    @Override
                    public boolean test(Status status) throws Exception {
                        return status.getErrorCode() != -1;
                    }
                }).doOnNext(new Consumer<Status>() {
                    @Override
                    public void accept(Status status) throws Exception {
                        Realm realm = Realm.getInstance(new RealmConfiguration.Builder()
                                .name(RealmHelper.DATABASE_NAME)
                                .deleteRealmIfMigrationNeeded().build());
                        ArticleDetailData data = realm.copyFromRealm(
                                realm.where(ArticleDetailData.class)
                                        .equalTo("id", id)
                                        .findFirst()
                        );

                    }
                });
    }

    @Override
    public Observable<Status> uncollectArticle(int originId) {
        return RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .uncollectArticle(originId)
                .filter(new Predicate<Status>() {
                    @Override
                    public boolean test(Status status) throws Exception {
                        return status.getErrorCode() != -1;
                    }
                });
    }
}
