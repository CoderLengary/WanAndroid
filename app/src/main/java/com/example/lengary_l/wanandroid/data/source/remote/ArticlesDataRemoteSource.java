package com.example.lengary_l.wanandroid.data.source.remote;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.ArticleDetailData;
import com.example.lengary_l.wanandroid.data.ArticlesData;
import com.example.lengary_l.wanandroid.data.source.ArticlesDataSource;
import com.example.lengary_l.wanandroid.realm.RealmHelper;
import com.example.lengary_l.wanandroid.retrofit.RetrofitClient;
import com.example.lengary_l.wanandroid.retrofit.RetrofitService;

import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ArticlesDataRemoteSource implements ArticlesDataSource {
    @NonNull
    private static ArticlesDataRemoteSource INSTANCE;




    private ArticlesDataRemoteSource(){

    }

    public static ArticlesDataRemoteSource getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new ArticlesDataRemoteSource();
        }
        return INSTANCE;
    }


    @Override
    public Observable<List<ArticleDetailData>> getArticles(@NonNull final int page, boolean forceUpdate, boolean clearCache) {
        return RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .getArticles(page)
                .filter(new Predicate<ArticlesData>() {
                    @Override
                    public boolean test(ArticlesData articlesData) throws Exception {
                        return articlesData.getErrorCode() != -1;
                    }
                })
                .flatMap(new Function<ArticlesData, ObservableSource<List<ArticleDetailData>>>() {
                    @Override
                    public ObservableSource<List<ArticleDetailData>> apply(ArticlesData articlesData) throws Exception {
                        return Observable.fromIterable(articlesData.getData().getDatas()).toSortedList(new Comparator<ArticleDetailData>() {
                            @Override
                            public int compare(ArticleDetailData articleDetailData, ArticleDetailData t1) {
                                if (articleDetailData.getPublishTime() > t1.getPublishTime()){
                                    return -1;
                                }else {
                                    return 1;
                                }
                            }
                        }).toObservable().doOnNext(new Consumer<List<ArticleDetailData>>() {
                            @Override
                            public void accept(List<ArticleDetailData> list) throws Exception {
                                for (ArticleDetailData item :list){
                                    saveToRealm(item, page);
                                }
                            }
                        });
                    }
                });
    }

    private void saveToRealm(ArticleDetailData article,int page){
        article.setCurrentPage(page);
        Realm realm = Realm.getInstance(new RealmConfiguration.Builder()
                .name(RealmHelper.DATABASE_NAME)
                .deleteRealmIfMigrationNeeded()
                .build());
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(article);
            realm.commitTransaction();
        realm.close();
    }


    @Override
    public Observable<List<ArticleDetailData>> queryArticles(@NonNull int page, @NonNull String keyWords, boolean forceUpdate, boolean clearCache) {
        return RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .queryArticles(page,keyWords)
                .filter(new Predicate<ArticlesData>() {
                    @Override
                    public boolean test(ArticlesData articlesData) throws Exception {
                        return articlesData.getErrorCode() != -1;
                    }
                })
                .flatMap(new Function<ArticlesData, ObservableSource<List<ArticleDetailData>>>() {
                    @Override
                    public ObservableSource<List<ArticleDetailData>> apply(ArticlesData articlesData) throws Exception {
                        return Observable.fromIterable(articlesData.getData().getDatas()).toSortedList(new Comparator<ArticleDetailData>() {
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
                });
    }

    @Override
    public Observable<ArticleDetailData> getArticleFromId(@NonNull int id) {
        //The local has handled it
        return null;
    }

    @Override
    public void addToReadLater(int currentUserId, int articleId, boolean readerLater) {
        //The local has handled it
    }

    @Override
    public Observable<List<ArticleDetailData>> getArticlesFromReadLater(int currentUserId, int articleId) {
        //The local has handled it
        return null;
    }

}
