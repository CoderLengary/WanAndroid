package com.example.lengary_l.wanandroid.mvp.timeline;

import com.example.lengary_l.wanandroid.data.ArticlesData;
import com.example.lengary_l.wanandroid.data.source.ArticlesDataRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ArticlesPresenter implements ArticlesContract.Presenter {

    private ArticlesDataRepository repository;
    private CompositeDisposable compositeDisposable;
    private ArticlesContract.View view;


    public ArticlesPresenter(ArticlesContract.View view,ArticlesDataRepository repository){
        this.repository = repository;
        this.view = view;
        this.view.setPresenter(this);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getArticles(int page, boolean forceUpdate, boolean clearCache) {
        Disposable disposable = repository.getArticles(page, forceUpdate, clearCache)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ArticlesData>() {

                    @Override
                    public void onNext(ArticlesData value) {
                        view.showArticles(value.getData().getDatas());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        view.setLoadingIndicator(false);
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }
}
