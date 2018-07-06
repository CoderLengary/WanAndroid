package com.example.lengary_l.wanandroid.mvp.timeline;

import android.util.Log;

import com.example.lengary_l.wanandroid.data.ArticleDetailData;
import com.example.lengary_l.wanandroid.data.BannerDetailData;
import com.example.lengary_l.wanandroid.data.LoginDetailData;
import com.example.lengary_l.wanandroid.data.source.ArticlesDataRepository;
import com.example.lengary_l.wanandroid.data.source.BannerDataRepository;
import com.example.lengary_l.wanandroid.data.source.LoginDataRepository;

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

    private ArticlesDataRepository articleRepository;
    private CompositeDisposable compositeDisposable;
    private BannerDataRepository bannerRepository;
    private LoginDataRepository loginDataRepository;
    private ArticlesContract.View view;
    private Map<Integer, ArticleDetailData> hashMap;
    private static final String TAG = "ArticlesPresenter";

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
        hashMap = new HashMap<>();
    }



    @Override
    public void getArticles(int page, final boolean forceUpdate, final boolean clearCache) {
        Disposable disposable = articleRepository.getArticles(page, forceUpdate, clearCache)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<ArticleDetailData>>() {
                    @Override
                    public void onNext(List<ArticleDetailData> value) {
                        if (!view.isActive()){
                            return;
                        }
                        if (forceUpdate&&!clearCache){
                            addToHashMap(value);
                        }else {
                            view.showArticles(value);
                            view.showEmptyView(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: "+e.getMessage() );
                        view.showEmptyView(true);
                    }

                    @Override
                    public void onComplete() {
                        if (!view.isActive()){
                            return;
                        }
                        if (forceUpdate&&!clearCache){
                            view.showArticles(sortHashMap(new ArrayList<>(hashMap.values())));
                            view.showEmptyView(false);
                        }
                        view.setLoadingIndicator(false);
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
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void refreshCollectIdList(int userId) {
        Disposable disposable = loginDataRepository.getLocalLoginData(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LoginDetailData>() {

                    @Override
                    public void onNext(LoginDetailData value) {
                        if (view.isActive()) {
                            view.saveFavoriteArticleIdList(value.getCollectIds());
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

    private void addToHashMap(List<ArticleDetailData> value){
        for (ArticleDetailData d:value){
            hashMap.put(d.getId(), d);
        }
    }

    private List<ArticleDetailData> sortHashMap(List<ArticleDetailData> list){
        Collections.sort(list, new Comparator<ArticleDetailData>() {
            @Override
            public int compare(ArticleDetailData articleDetailData, ArticleDetailData t1) {
                if (articleDetailData.getPublishTime() > t1.getPublishTime()){
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
        compositeDisposable.clear();
    }
}
