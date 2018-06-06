package com.example.lengary_l.wanandroid.mvp.categories;

import com.example.lengary_l.wanandroid.data.CategoryDetailData;
import com.example.lengary_l.wanandroid.data.source.CategoriesDataRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class CategoriesPresenter implements CategoriesContract.Presenter {
    private CategoriesDataRepository repository;
    private CompositeDisposable compositeDisposable;
    private CategoriesContract.View view;

    public CategoriesPresenter(CategoriesDataRepository repository, CategoriesContract.View view) {
        this.repository = repository;
        compositeDisposable = new CompositeDisposable();
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        Disposable disposable = repository.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<CategoryDetailData>>() {
                    @Override
                    public void onNext(List<CategoryDetailData> value) {
                        if (!view.isActive()){
                            return;
                        }
                        view.showCategories(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showEmptyView();
                    }

                    @Override
                    public void onComplete() {
                        if (!view.isActive()){
                            return;
                        }
                        view.setLoadingIndicator(false);
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void unSubscribe() {

    }
}
