package com.example.lengary_l.wanandroid.mvp.detail;

import com.example.lengary_l.wanandroid.data.Status;
import com.example.lengary_l.wanandroid.data.source.ArticlesDataRepository;
import com.example.lengary_l.wanandroid.data.source.StatusDataRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class DetailPresenter implements DetailContract.Presenter{
    private DetailContract.View view;
    private StatusDataRepository statusDataRepository;
    private ArticlesDataRepository articlesDataRepository;
    private CompositeDisposable compositeDisposable;


    public DetailPresenter(DetailContract.View view,
                           StatusDataRepository statusDataRepository,
                           ArticlesDataRepository articlesDataRepository) {
        this.view = view;
        this.statusDataRepository = statusDataRepository;
        this.articlesDataRepository = articlesDataRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
    }

    @Override
    public void collectArticle(int id) {
        Disposable disposable=statusDataRepository.collectArticle(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Status>(){

                    @Override
                    public void onNext(Status value) {
                        if (view.isActive() && value.getErrorCode() != -1) {
                            view.showCollectStatus(true);
                            view.changeFavoriteState();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view.isActive()) {
                            view.showCollectStatus(false);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void uncollectArticle(int originId) {
        Disposable disposable = statusDataRepository.uncollectArticle(originId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Status>() {

                    @Override
                    public void onNext(Status value) {
                        if (view.isActive() && value.getErrorCode() != -1) {
                            view.showUnCollectStatus(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view.isActive()) {
                            view.showUnCollectStatus(false);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void addToReadLater(int id, int userId) {

    }
}
