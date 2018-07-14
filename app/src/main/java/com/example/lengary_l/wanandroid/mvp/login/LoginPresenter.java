package com.example.lengary_l.wanandroid.mvp.login;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.LoginData;
import com.example.lengary_l.wanandroid.data.LoginType;
import com.example.lengary_l.wanandroid.data.source.LoginDataRepository;
import com.example.lengary_l.wanandroid.data.source.ReadLaterArticlesRepository;

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
    private LoginContract.View view;
    @NonNull
    private LoginDataRepository repository;

    private CompositeDisposable compositeDisposable;

    private ReadLaterArticlesRepository readLaterArticlesRepository;


    public LoginPresenter(@NonNull LoginContract.View view, @NonNull LoginDataRepository loginDataRepository,
                          @NonNull ReadLaterArticlesRepository readLaterArticlesRepository) {
        this.view = view;
        this.repository = loginDataRepository;
        this.readLaterArticlesRepository = readLaterArticlesRepository;
        this.view.setPresenter(this);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void login(String username, String password, @NonNull LoginType loginType) {
        getLoginData(username, password,loginType);
    }

    @Override
    public void clearReadLaterData() {
        readLaterArticlesRepository.clearAll();
    }


    private void getLoginData(String username,String password, @NonNull final LoginType loginType){

        Disposable disposable=repository.getRemoteLoginData(username, password,loginType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LoginData>() {

                    @Override
                    public void onNext(LoginData value) {
                        if (!view.isActive()) {
                            return;
                        }
                        if (value.getErrorCode()==-1){
                            view.showLoginError(value.getErrorMsg());
                        }else {
                           view.saveUser2Preference(value.getData());
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
