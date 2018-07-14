package com.example.lengary_l.wanandroid.mvp.search;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.ArticleDetailData;
import com.example.lengary_l.wanandroid.data.HotKeyDetailData;
import com.example.lengary_l.wanandroid.data.source.ArticlesDataRepository;
import com.example.lengary_l.wanandroid.data.source.HotKeyDataRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by CoderLengary
 */


public class SearchPresenter implements SearchContract.Presenter{
    private HotKeyDataRepository hotKeyDataRepository;
    private CompositeDisposable compositeDisposable;
    private ArticlesDataRepository articlesDataRepository;
    private SearchContract.View view;

    public SearchPresenter(HotKeyDataRepository hotKeyDataRepository, ArticlesDataRepository articlesDataRepository, SearchContract.View view) {
        this.hotKeyDataRepository = hotKeyDataRepository;
        this.articlesDataRepository = articlesDataRepository;
        compositeDisposable = new CompositeDisposable();
        this.view = view;
        this.view.setPresenter(this);

    }

    @Override
    public void getHotKeys(boolean forceUpdate) {
        Disposable disposable = hotKeyDataRepository.getHotKeys(forceUpdate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<HotKeyDetailData>>() {

                    @Override
                    public void onNext(List<HotKeyDetailData> value) {
                        if (view.isActive()) {
                            view.showHotKeys(value);
                        }
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
    public void searchArticles(@NonNull int page, @NonNull String keyWords, final boolean forceUpdate, final boolean clearCache) {
        Disposable disposable = articlesDataRepository.queryArticles(page, keyWords, forceUpdate, clearCache)
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
                        view.showEmptyView(true);
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
