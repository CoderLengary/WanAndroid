package com.example.lengary_l.wanandroid.mvp.detail;

import com.example.lengary_l.wanandroid.data.LoginDetailData;
import com.example.lengary_l.wanandroid.data.Status;
import com.example.lengary_l.wanandroid.data.source.LoginDataRepository;
import com.example.lengary_l.wanandroid.data.source.ReadLaterArticlesRepository;
import com.example.lengary_l.wanandroid.data.source.StatusDataRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class DetailPresenter implements DetailContract.Presenter {
    private DetailContract.View view;
    private StatusDataRepository statusDataRepository;
    private CompositeDisposable compositeDisposable;
    private ReadLaterArticlesRepository readLaterArticlesRepository;
    private LoginDataRepository loginRepository;

    public DetailPresenter(DetailContract.View view,
                           StatusDataRepository statusDataRepository,
                           ReadLaterArticlesRepository readLaterArticlesRepository,
                           LoginDataRepository loginRepository) {
        this.view = view;
        this.statusDataRepository = statusDataRepository;
        this.readLaterArticlesRepository = readLaterArticlesRepository;
        this.loginRepository = loginRepository;
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
    public void collectArticle(int userId, int originId) {
        Disposable disposable = statusDataRepository.collectArticle(userId, originId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Status>() {

                    @Override
                    public void onNext(Status value) {
                        if (view.isActive()) {
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
    public void uncollectArticle(int userId, int originId) {
        Disposable disposable = statusDataRepository.uncollectArticle(userId, originId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Status>() {

                    @Override
                    public void onNext(Status value) {
                        if (view.isActive()) {
                            view.showUnCollectStatus(true);
                            view.changeFavoriteState();
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
    public void insertReadLaterArticle(int userId, int id, long timeStamp) {
        readLaterArticlesRepository.insertReadLaterArticle(userId, id, timeStamp);
    }

    @Override
    public void removeReadLaterArticle(int userId, int id) {
        readLaterArticlesRepository.removeReadLaterArticle(userId, id);
    }

    @Override
    public void checkIsReadLater(int userId, int id) {
        view.saveReadLaterState(readLaterArticlesRepository.isExist(userId, id));
    }

    @Override
    public void refreshCollectIdList(int userId) {
        Disposable disposable = loginRepository.getLocalLoginData(userId)
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


}
