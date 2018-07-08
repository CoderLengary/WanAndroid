package com.example.lengary_l.wanandroid.mvp.category;

import com.example.lengary_l.wanandroid.data.ArticleDetailData;
import com.example.lengary_l.wanandroid.data.source.ArticlesDataRepository;
import com.example.lengary_l.wanandroid.util.SortUtil;

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


public class CategoryPresenter implements CategoryContract.Presenter {
    private CategoryContract.View view;
    private ArticlesDataRepository repository;
    private Map<Integer, ArticleDetailData> hashMap;
    private CompositeDisposable compositeDisposable;

    public CategoryPresenter(CategoryContract.View view, ArticlesDataRepository repository) {
        this.view = view;
        this.repository = repository;
        this.view.setPresenter(this);
        hashMap = new LinkedHashMap<>();
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
                        if (!view.isActive()) {
                            return;
                        }
                        if (forceUpdate && !clearCache) {
                            addToHashMap(value);
                        } else {
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
                        if (!view.isActive()) {
                            return;
                        }
                        if (forceUpdate && !clearCache) {
                            view.showArticles(sortHashMap(
                                    new ArrayList<>(hashMap.values())
                            ));
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void addToHashMap(List<ArticleDetailData> value) {
        for (ArticleDetailData d : value) {
            hashMap.put(d.getId(), d);
        }
    }

    private List<ArticleDetailData> sortHashMap(List<ArticleDetailData> list) {
        Collections.sort(list, new Comparator<ArticleDetailData>() {
            @Override
            public int compare(ArticleDetailData articleDetailData, ArticleDetailData t1) {
               return SortUtil.sortArticleDetailData(articleDetailData, t1);
            }
        });
        return list;
    }


}
