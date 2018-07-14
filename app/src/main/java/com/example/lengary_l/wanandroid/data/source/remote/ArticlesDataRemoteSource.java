package com.example.lengary_l.wanandroid.data.source.remote;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.ArticleDetailData;
import com.example.lengary_l.wanandroid.data.ArticlesData;
import com.example.lengary_l.wanandroid.data.source.ArticlesDataSource;
import com.example.lengary_l.wanandroid.realm.RealmHelper;
import com.example.lengary_l.wanandroid.retrofit.RetrofitClient;
import com.example.lengary_l.wanandroid.retrofit.RetrofitService;
import com.example.lengary_l.wanandroid.util.SortDescendUtil;

import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by CoderLengary
 */


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
    public Observable<List<ArticleDetailData>> getArticles(@NonNull  int page, @NonNull boolean forceUpdate, @NonNull boolean clearCache) {
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
                                return SortDescendUtil.sortArticleDetailData(articleDetailData, t1);
                            }
                        }).toObservable().doOnNext(new Consumer<List<ArticleDetailData>>() {
                            @Override
                            public void accept(List<ArticleDetailData> list) throws Exception {
                                for (ArticleDetailData item :list){
                                    saveToRealm(item);
                                }
                            }
                        });
                    }
                });
    }

    private void saveToRealm(@NonNull ArticleDetailData article){
        // It is necessary to build a new realm instance
        // in a different thread.
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
    public Observable<List<ArticleDetailData>> queryArticles(@NonNull int page, @NonNull String keyWords, @NonNull boolean forceUpdate, @NonNull  boolean clearCache) {
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
                                return SortDescendUtil.sortArticleDetailData(articleDetailData, t1);
                            }
                        }).toObservable().doOnNext(new Consumer<List<ArticleDetailData>>() {
                            @Override
                            public void accept(List<ArticleDetailData> list) throws Exception {
                                for (ArticleDetailData item :list){
                                    saveToRealm(item);
                                }
                            }
                        });
                    }
                });
    }


    @Override
    public Observable<List<ArticleDetailData>> getArticlesFromCatg(@NonNull int page, @NonNull int categoryId, @NonNull boolean forceUpdate, @NonNull boolean clearCache) {
        return RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .getArticlesFromCatg(page,categoryId)
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
                                return SortDescendUtil.sortArticleDetailData(articleDetailData, t1);
                            }
                        }).toObservable().doOnNext(new Consumer<List<ArticleDetailData>>() {
                            @Override
                            public void accept(List<ArticleDetailData> list) throws Exception {
                                for (ArticleDetailData item :list){
                                    saveToRealm(item);
                                }
                            }
                        });
                    }
                });
    }

}
