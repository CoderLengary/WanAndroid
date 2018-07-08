package com.example.lengary_l.wanandroid.mvp.timeline;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.LoginDetailData;
import com.example.lengary_l.wanandroid.data.ReadLaterArticleData;
import com.example.lengary_l.wanandroid.data.source.LoginDataRepository;
import com.example.lengary_l.wanandroid.data.source.ReadLaterArticlesRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ReadLaterPresenter implements ReadLaterContract.Presenter {

    private ReadLaterArticlesRepository repository;
    private CompositeDisposable compositeDisposable;
    private LoginDataRepository loginDataRepository;
    private ReadLaterContract.View view;
    private static final String TAG = "ReadLaterPresenter";


    public ReadLaterPresenter(ReadLaterContract.View view,ReadLaterArticlesRepository repository,LoginDataRepository loginDataRepository) {
        this.repository = repository;
        this.view = view;
        this.view.setPresenter(this);
        this.loginDataRepository = loginDataRepository;
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
                        if (!view.isActive()) {
                            return;
                        }
                        view.showReadLaterArticles(value);
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
    public void refreshCollectIdList(@NonNull int userId) {
        Disposable disposable = loginDataRepository.getLocalLoginData(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<LoginDetailData>() {
                    @Override
                    public boolean test(LoginDetailData loginDetailData) throws Exception {
                        return !loginDetailData.getCollectIds().isEmpty();
                    }
                })
                .subscribeWith(new DisposableObserver<LoginDetailData>() {
                    @Override
                    public void onNext(LoginDetailData value) {
                        view.saveFavoriteArticleIdList(value.getCollectIds());
                    }

                    @Override
                    public void onError(Throwable e) {

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
