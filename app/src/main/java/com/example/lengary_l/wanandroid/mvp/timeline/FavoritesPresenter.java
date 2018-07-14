package com.example.lengary_l.wanandroid.mvp.timeline;

import android.util.Log;

import com.example.lengary_l.wanandroid.data.FavoriteArticleDetailData;
import com.example.lengary_l.wanandroid.data.source.FavoriteArticlesDataRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by CoderLengary
 */


public class FavoritesPresenter implements FavoritesContract.Presenter {

    private FavoriteArticlesDataRepository repository;

    private FavoritesContract.View view;
    private CompositeDisposable compositeDisposable;
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
                        if (view.isActive()) {
                            view.showFavoriteArticles(value);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view.isActive()) {
                            view.showEmptyView(true);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (view.isActive()) {
                            view.setLoadingIndicator(false);
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }




    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
    }



}
