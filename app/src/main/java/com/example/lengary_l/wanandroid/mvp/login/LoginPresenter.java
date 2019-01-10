package com.example.lengary_l.wanandroid.mvp.login;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.LoginData;
import com.example.lengary_l.wanandroid.data.LoginType;
import com.example.lengary_l.wanandroid.data.source.LoginDataRepository;
import com.example.lengary_l.wanandroid.data.source.ReadLaterArticlesDataRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by CoderLengary
 */


public class  LoginPresenter implements LoginContract.Presenter{

    @NonNull
    private final LoginContract.View view;
    @NonNull
    private final LoginDataRepository repository;

    private final CompositeDisposable compositeDisposable;

    private final ReadLaterArticlesDataRepository readLaterArticlesDataRepository;


    public LoginPresenter(@NonNull LoginContract.View view, @NonNull LoginDataRepository loginDataRepository,
                          @NonNull ReadLaterArticlesDataRepository readLaterArticlesDataRepository) {
        this.view = view;
        this.repository = loginDataRepository;
        this.readLaterArticlesDataRepository = readLaterArticlesDataRepository;
        this.view.setPresenter(this);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void login(String userName, String password, @NonNull LoginType loginType) {
        getLoginData(userName, password,loginType);
    }

    @Override
    public void clearReadLaterData() {
        readLaterArticlesDataRepository.clearAll();
    }


    private void getLoginData(final String userName, final String password, @NonNull final LoginType loginType){

        Disposable disposable=repository.getRemoteLoginData(userName, password,loginType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LoginData>() {

                    @Override
                    public void onNext(LoginData value) {
                        if (!view.isActive()) {
                            return;
                        }
                        if (value.getErrorCode() != 0){
                            view.showLoginError(value.getErrorMsg());
                        }else {
                           view.saveUser2Preference(value.getData().getId(), userName, password);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view.isActive()) {
                            view.showNetworkError();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        compositeDisposable.add(disposable);
    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
    }


}
