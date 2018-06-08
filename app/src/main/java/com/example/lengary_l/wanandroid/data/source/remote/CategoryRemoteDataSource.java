package com.example.lengary_l.wanandroid.data.source.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.lengary_l.wanandroid.data.ArticleDetailData;
import com.example.lengary_l.wanandroid.data.ArticlesData;
import com.example.lengary_l.wanandroid.data.source.CategoryDataSource;
import com.example.lengary_l.wanandroid.retrofit.RetrofitClient;
import com.example.lengary_l.wanandroid.retrofit.RetrofitService;

import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class CategoryRemoteDataSource  implements CategoryDataSource{

    @NonNull
    private static CategoryRemoteDataSource INSTANCE;
    private static final String TAG = "CategoryRemoteDataSourc";

    private CategoryRemoteDataSource() {

    }

    public static CategoryRemoteDataSource getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new CategoryRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<ArticleDetailData>> getArticlesFromCatg(int page, int categoryId, boolean forceUpdate,boolean clearCache) {
        Log.e(TAG, "getArticlesFromCatg: " );
        return RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .getArticlesFromCatg(page,categoryId)
                .filter(new Predicate<ArticlesData>() {
                    @Override
                    public boolean test(ArticlesData articlesData) throws Exception {
                        Log.e(TAG, "test: data's error code is "+articlesData.getErrorCode()+" data is over "+articlesData.getData().isOver() );
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
}
