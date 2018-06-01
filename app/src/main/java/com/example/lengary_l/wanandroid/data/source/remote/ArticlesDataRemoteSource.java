package com.example.lengary_l.wanandroid.data.source.remote;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.ArticlesData;
import com.example.lengary_l.wanandroid.data.source.ArticlesDataSource;
import com.example.lengary_l.wanandroid.retrofit.RetrofitClient;
import com.example.lengary_l.wanandroid.retrofit.RetrofitService;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class ArticlesDataRemoteSource implements ArticlesDataSource {
    @NonNull
    public static ArticlesDataRemoteSource INSTANCE;

    private ArticlesDataRemoteSource(){

    }

    public static ArticlesDataRemoteSource getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new ArticlesDataRemoteSource();
        }
        return INSTANCE;
    }

    @Override
    public Observable<ArticlesData> getArticles(@NonNull int page, boolean forceUpdate, boolean clearCache) {
        return RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .getArticles(page)
                .doOnNext(new Consumer<ArticlesData>() {
                    @Override
                    public void accept(ArticlesData articlesData) throws Exception {
                        //Make sure that we save the valid data
                       /* if (articlesData.getErrorCode()!=-1&&!articlesData.getData().isOver()){
                            Realm realm = Realm.getInstance(new RealmConfiguration.Builder()
                                    .name(RealmHelper.DATABASE_NAME)
                                    .deleteRealmIfMigrationNeeded()
                                    .build());
                            realm.beginTransaction();
                            for (ArticleDetailData article:articlesData.getData().getDatas()) {
                                realm.copyToRealmOrUpdate(article);
                            }
                            realm.commitTransaction();
                            realm.close();
                        }*/
                    }
                });
    }
}
