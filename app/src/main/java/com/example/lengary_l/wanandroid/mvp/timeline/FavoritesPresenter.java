package com.example.lengary_l.wanandroid.mvp.timeline;

import android.util.Log;

import com.example.lengary_l.wanandroid.data.FavoriteArticleDetailData;
import com.example.lengary_l.wanandroid.data.FavoriteArticlesData;
import com.example.lengary_l.wanandroid.data.LoginData;
import com.example.lengary_l.wanandroid.data.LoginType;
import com.example.lengary_l.wanandroid.data.source.FavoriteArticlesDataRepository;
import com.example.lengary_l.wanandroid.data.source.LoginDataRepository;
import com.example.lengary_l.wanandroid.retrofit.RetrofitClient;
import com.example.lengary_l.wanandroid.retrofit.RetrofitService;

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

public class FavoritesPresenter implements FavoritesContract.Presenter {

    private FavoriteArticlesDataRepository repository;
    private LoginDataRepository loginDataRepository;
    private FavoritesContract.View view;
    private CompositeDisposable compositeDisposable;
    private Map<Integer, FavoriteArticleDetailData> hashMap;
    private static final String TAG = "FavoritesPresenter";

    public FavoritesPresenter(FavoritesContract.View view,FavoriteArticlesDataRepository repository, LoginDataRepository loginDataRepository) {
        this.repository = repository;
        this.loginDataRepository = loginDataRepository;
        this.view = view;
        compositeDisposable = new CompositeDisposable();
        this.view.setPresenter(this);
    }

    @Override
    public void autoLogin(String userName, String userPassword) {
        Log.e(TAG, "autoLogin: ");
        Disposable disposable = loginDataRepository.getRemoteLoginData(userName, userPassword, LoginType.TYPE_LOGIN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LoginData>() {

                    @Override
                    public void onNext(LoginData value) {
                        if (value.getErrorCode() == -1&&view.isActive()) {
                            Log.e(TAG, "auto login failed " );
                        }else {
                            Log.e(TAG, "auto login success: " );
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "autologin Error: "+e.getMessage() );
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "auto login Complete: " );
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void getFavoriteArticles(int page, final boolean forceUpdate, final boolean clearCache) {
        Disposable disposable = repository.getFavoriteArticles(page, forceUpdate, clearCache)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<FavoriteArticleDetailData>>() {

                    @Override
                    public void onNext(List<FavoriteArticleDetailData> value) {
                        Log.e(TAG, "onNext: value is not null" );
                        if (!view.isActive()) {
                            return;
                        }
                        if (forceUpdate && !clearCache) {
                            addToHashMap(value, false);
                        }else {
                            view.showFavoriteArticles(value);
                            view.showEmptyView(false);
                            if (clearCache&&hashMap!=null) {
                                hashMap.clear();
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: "+e.getMessage() );
                        Log.e(TAG, "onError: "+e.getLocalizedMessage() );
                        Log.e(TAG, "onError: "+e.toString() );
                        view.showEmptyView(true);
                    }

                    @Override
                    public void onComplete() {
                        //检测view是不是active是必须的吗？？
                        if (!view.isActive()) {
                            return;
                        }
                        if (forceUpdate && !clearCache) {
                            view.showFavoriteArticles(sortHashMap(new ArrayList<>(hashMap.values())));
                            view.showEmptyView(false);
                        }
                        view.setLoadingIndicator(false);
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void subscribe() {
      RetrofitClient.getInstance().create(RetrofitService.class)
                .getFavoriteArticles(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<FavoriteArticlesData>() {
                    @Override
                    public void onNext(FavoriteArticlesData value) {
                        Log.e(TAG, "TextOnNext: error code is "+value.getErrorCode() );
                        Log.e(TAG, "TextOnNext: error message is" + value.getErrorMsg());
                        Log.e(TAG, "TextOnNext: datas are empty is "+value.getData().getDatas().isEmpty() );
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "TextOnNext: local error message is" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void unSubscribe() {

    }

    private void addToHashMap(List<FavoriteArticleDetailData> list,boolean clearCache) {
        if (hashMap == null) {
            hashMap = new LinkedHashMap<>();
        }
        if (clearCache) {
            hashMap.clear();
        }
        for (FavoriteArticleDetailData data : list) {
            hashMap.put(data.getOriginId(), data);
        }
    }

    private List<FavoriteArticleDetailData> sortHashMap(List<FavoriteArticleDetailData> list) {
        Collections.sort(list, new Comparator<FavoriteArticleDetailData>() {
            @Override
            public int compare(FavoriteArticleDetailData favoriteArticleDetailData, FavoriteArticleDetailData t1) {
                if (favoriteArticleDetailData.getPublishTime() > t1.getPublishTime()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        return list;
    }
}
