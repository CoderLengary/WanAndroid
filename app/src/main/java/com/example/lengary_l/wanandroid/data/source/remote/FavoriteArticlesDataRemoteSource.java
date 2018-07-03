package com.example.lengary_l.wanandroid.data.source.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.lengary_l.wanandroid.data.FavoriteArticleDetailData;
import com.example.lengary_l.wanandroid.data.FavoriteArticlesData;
import com.example.lengary_l.wanandroid.data.source.FavoriteArticlesDataSource;
import com.example.lengary_l.wanandroid.retrofit.RetrofitClient;
import com.example.lengary_l.wanandroid.retrofit.RetrofitService;

import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class FavoriteArticlesDataRemoteSource implements FavoriteArticlesDataSource {
    @NonNull
    private static FavoriteArticlesDataRemoteSource INSTANCE;
    private static final String TAG = "FavoriteArticlesDataRem";
    private FavoriteArticlesDataRemoteSource() {

    }

    public static FavoriteArticlesDataRemoteSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FavoriteArticlesDataRemoteSource();
        }
        return INSTANCE;
    }



    @Override
    public Observable<List<FavoriteArticleDetailData>> getFavoriteArticles(final int page, boolean forceUpdate, boolean clearCache) {
        Log.e(TAG, "getFavoriteArticles: " );
        return RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .getFavoriteArticles(page)
                .filter(new Predicate<FavoriteArticlesData>() {
                    @Override
                    public boolean test(FavoriteArticlesData favoriteArticlesData) throws Exception {
                        Log.e(TAG, "test: page is "+page+" is empty is "+!favoriteArticlesData.getData().getDatas().isEmpty() );
                       // return favoriteArticlesData.getErrorCode() != -1 && !favoriteArticlesData.getData().getDatas().isEmpty();
                        return favoriteArticlesData.getErrorCode() != -1;
                    }
                }).flatMap(new Function<FavoriteArticlesData, ObservableSource<List<FavoriteArticleDetailData>>>() {
                    @Override
                    public ObservableSource<List<FavoriteArticleDetailData>> apply(FavoriteArticlesData favoriteArticlesData) throws Exception {
                        if (favoriteArticlesData.getData() == null) {
                            Log.e(TAG, "apply: data is  null" );
                        }else{
                            Log.e(TAG, "apply: data is  not null" );
                        }

                        if (favoriteArticlesData.getData().getDatas() == null) {
                            Log.e(TAG, "apply: data 's datas is  null");
                        }else {
                            Log.e(TAG, "apply: data 's datas is  not null" );
                        }

                        return Observable.fromIterable(favoriteArticlesData.getData().getDatas()).toSortedList(new Comparator<FavoriteArticleDetailData>() {
                            @Override
                            public int compare(FavoriteArticleDetailData favoriteArticleDetailData, FavoriteArticleDetailData t1) {
                                if (favoriteArticleDetailData.getPublishTime() > t1.getPublishTime()) {
                                    return -1;
                                }else {
                                    return 1;
                                }
                            }
                        }).toObservable();
                    }
                });
    }
}
