package com.example.lengary_l.wanandroid.mvp.timeline;

import com.example.lengary_l.wanandroid.data.ReadLaterArticleData;
import com.example.lengary_l.wanandroid.data.source.ReadLaterArticlesRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by CoderLengary
 */


public class ReadLaterPresenter implements ReadLaterContract.Presenter {

    private ReadLaterArticlesRepository repository;
    private CompositeDisposable compositeDisposable;
    private ReadLaterContract.View view;

    public ReadLaterPresenter(ReadLaterContract.View view,ReadLaterArticlesRepository repository) {
        this.repository = repository;
        this.view = view;
        this.view.setPresenter(this);

        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getReadLaterArticles(int userId) {
        Disposable disposable=repository.getReadLaterArticles(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<ReadLaterArticleData>>() {
                    @Override
                    public void onNext(List<ReadLaterArticleData> value) {
                        if (view.isActive()) {
                            view.showReadLaterArticles(value);
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



    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();

    }
}
