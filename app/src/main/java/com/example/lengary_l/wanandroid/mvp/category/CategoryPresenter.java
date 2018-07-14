package com.example.lengary_l.wanandroid.mvp.category;

import com.example.lengary_l.wanandroid.data.ArticleDetailData;
import com.example.lengary_l.wanandroid.data.source.ArticlesDataRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by CoderLengary
 */


public class CategoryPresenter implements CategoryContract.Presenter {
    private CategoryContract.View view;
    private ArticlesDataRepository repository;
    private CompositeDisposable compositeDisposable;

    public CategoryPresenter(CategoryContract.View view, ArticlesDataRepository repository) {
        this.view = view;
        this.repository = repository;
        this.view.setPresenter(this);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
    }

    @Override
    public void getArticlesFromCatg(int page, int categoryId, final boolean forceUpdate, final boolean clearCache) {
        Disposable disposable = repository.getArticlesFromCatg(page, categoryId, forceUpdate, clearCache)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<ArticleDetailData>>() {

                    @Override
                    public void onNext(List<ArticleDetailData> value) {
                        if (view.isActive()) {
                            view.showArticles(value);
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

                    }
                });
        compositeDisposable.add(disposable);
    }





}
