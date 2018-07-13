package com.example.lengary_l.wanandroid.mvp.timeline;

import android.util.Log;

import com.example.lengary_l.wanandroid.data.FavoriteArticleDetailData;
import com.example.lengary_l.wanandroid.data.source.FavoriteArticlesDataRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class FavoritesPresenter implements FavoritesContract.Presenter {

    private FavoriteArticlesDataRepository repository;

    private FavoritesContract.View view;
    private CompositeDisposable compositeDisposable;
    private Map<Integer, FavoriteArticleDetailData> hashMap;
    private static final String TAG = "FavoritesPresenter";

    public FavoritesPresenter(FavoritesContract.View view,FavoriteArticlesDataRepository repository) {
        this.repository = repository;

        this.view = view;
        compositeDisposable = new CompositeDisposable();
        this.view.setPresenter(this);
    }




    @Override
    public void getFavoriteArticles(int page, final boolean forceUpdate, final boolean clearCache) {
        Log.e(TAG, "getFavoriteArticles: forceUpdate="+forceUpdate+" clearCache="+clearCache );
        Disposable disposable = repository.getFavoriteArticles(page, forceUpdate, clearCache)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<FavoriteArticleDetailData>>() {

                    @Override
                    public void onNext(List<FavoriteArticleDetailData> value) {
                        if (!view.isActive()) {
                            return;
                        }
                        if (forceUpdate && !clearCache) {
                            addToHashMap(value, false);
                        }else {
                            view.showFavoriteArticles(value);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showEmptyView(true);
                    }

                    @Override
                    public void onComplete() {
                        if (!view.isActive()) {
                            return;
                        }
                        if (forceUpdate && !clearCache) {
                            view.showFavoriteArticles(sortHashMap(new ArrayList<>(hashMap.values())));
                        }
                        view.setLoadingIndicator(false);
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void clearHashMap() {
        if (hashMap != null) {
            hashMap.clear();
        }
    }


    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
    }

    private void addToHashMap(List<FavoriteArticleDetailData> list,boolean clearCache) {
        if (hashMap == null) {
            hashMap = new LinkedHashMap<>();
        }
        if (clearCache) {
            hashMap.clear();
        }
        for (FavoriteArticleDetailData data : list) {
            hashMap.put(data.getOriginId(), data);
        }
    }

    private List<FavoriteArticleDetailData> sortHashMap(List<FavoriteArticleDetailData> list) {
        Collections.sort(list, new Comparator<FavoriteArticleDetailData>() {
            @Override
            public int compare(FavoriteArticleDetailData favoriteArticleDetailData, FavoriteArticleDetailData t1) {
                if (favoriteArticleDetailData.getPublishTime() > t1.getPublishTime()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        return list;
    }
}
