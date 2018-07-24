package com.example.lengary_l.wanandroid.mvp.timeline;

import com.example.lengary_l.wanandroid.data.ArticleDetailData;
import com.example.lengary_l.wanandroid.data.BannerDetailData;
import com.example.lengary_l.wanandroid.data.LoginData;
import com.example.lengary_l.wanandroid.data.LoginType;
import com.example.lengary_l.wanandroid.data.source.ArticlesDataRepository;
import com.example.lengary_l.wanandroid.data.source.BannerDataRepository;
import com.example.lengary_l.wanandroid.data.source.LoginDataRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by CoderLengary
 */


public class ArticlesPresenter implements ArticlesContract.Presenter {

    private ArticlesDataRepository articleRepository;
    private CompositeDisposable compositeDisposable;
    private BannerDataRepository bannerRepository;
    private LoginDataRepository loginDataRepository;
    private ArticlesContract.View view;

    public ArticlesPresenter(ArticlesContract.View view,
                             ArticlesDataRepository articleRepository,
                             BannerDataRepository bannerRepository,
                             LoginDataRepository loginDataRepository){
        this.articleRepository = articleRepository;
        this.bannerRepository = bannerRepository;
        this.loginDataRepository = loginDataRepository;
        this.view = view;
        this.view.setPresenter(this);
        compositeDisposable = new CompositeDisposable();
    }



    @Override
    public void getArticles(int page, final boolean forceUpdate, final boolean clearCache) {
        Disposable disposable = articleRepository.getArticles(page, forceUpdate, clearCache)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<ArticleDetailData>>() {
                    @Override
                    public void onNext(List<ArticleDetailData> value) {
                        if (view.isActive()){
                            view.showEmptyView(false);
                            view.showArticles(value);
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view.isActive()) {
                            view.showEmptyView(true);
                            view.setLoadingIndicator(false);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (view.isActive()){
                            view.setLoadingIndicator(false);
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void getBanner() {
        Disposable disposable = bannerRepository.getBanner()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<BannerDetailData>>() {
                    @Override
                    public void onNext(List<BannerDetailData> value) {
                        if (view.isActive()) {
                            view.showBanner(value);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view.isActive()) {
                            view.hideBanner();
                            view.setLoadingIndicator(false);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (view.isActive()){
                            view.setLoadingIndicator(false);
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void autoLogin(String userName, String password) {
        Disposable disposable = loginDataRepository.getRemoteLoginData(userName, password, LoginType.TYPE_LOGIN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LoginData>() {

                    @Override
                    public void onNext(LoginData value) {
                        if (view.isActive() && value.getErrorCode() == -1) {
                            view.showAutoLoginFail();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view.isActive()) {
                            view.showAutoLoginFail();
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
