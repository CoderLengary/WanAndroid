package com.example.lengary_l.wanandroid.mvp.timeline;

import android.util.Log;

import com.example.lengary_l.wanandroid.data.ArticleDetailData;
import com.example.lengary_l.wanandroid.data.source.ArticlesDataRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ArticlesPresenter implements ArticlesContract.Presenter {

    private ArticlesDataRepository repository;
    private CompositeDisposable compositeDisposable;
    private ArticlesContract.View view;
    private Map<Integer, ArticleDetailData> hashMap;
    private static final String TAG = "ArticlesPresenter";

    public ArticlesPresenter(ArticlesContract.View view,ArticlesDataRepository repository){
        this.repository = repository;
        this.view = view;
        this.view.setPresenter(this);
        compositeDisposable = new CompositeDisposable();
        hashMap = new HashMap<>();
    }

    @Override
    public void getArticles(int page, final boolean forceUpdate, final boolean clearCache) {
        Disposable disposable = repository.getArticles(page, forceUpdate, clearCache)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<ArticleDetailData>>() {
                    @Override
                    public void onNext(List<ArticleDetailData> value) {
                        if (forceUpdate&&!clearCache){
                            addToHashMap(value);
                            Log.e(TAG, "onNext: NO DONT " );
                        }else {
                            Log.e(TAG, "onNext: " );
                            view.showArticles(value);
                            view.setLoadingIndicator(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        view.showEmptyView();
                    }

                    @Override
                    public void onComplete() {
                        if (forceUpdate&&!clearCache){
                            view.showArticles(sortHashMap(new ArrayList<>(hashMap.values())));
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void addToHashMap(List<ArticleDetailData> value){
        for (ArticleDetailData d:value){
            hashMap.put(d.getId(), d);
        }
    }

    private List<ArticleDetailData> sortHashMap(List<ArticleDetailData> list){
        Collections.sort(list, new Comparator<ArticleDetailData>() {
            @Override
            public int compare(ArticleDetailData articleDetailData, ArticleDetailData t1) {
                if (articleDetailData.getId() > t1.getId()){
                    return -1;
                }else {
                    return 1;
                }
            }
        });
        return list;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }
}
