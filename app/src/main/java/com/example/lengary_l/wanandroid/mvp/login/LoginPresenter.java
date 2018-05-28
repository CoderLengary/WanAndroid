package com.example.lengary_l.wanandroid.mvp.login;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.LoginData;
import com.example.lengary_l.wanandroid.data.LoginDetailData;
import com.example.lengary_l.wanandroid.data.LoginType;
import com.example.lengary_l.wanandroid.data.source.LoginDataRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter implements LoginContract.Presenter{

    @NonNull
    private LoginContract.View view;
    @NonNull
    private LoginDataRepository repository;

    private CompositeDisposable compositeDisposable;


    public LoginPresenter(@NonNull LoginContract.View view, @NonNull LoginDataRepository loginDataRepository) {
        this.view = view;
        this.repository = loginDataRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void login(String username, String password, @NonNull LoginType loginType) {
        if (repository.isAccountExist(username)){
            getLoginDetailData(username);
        }else {
            getLoginData(username, password,loginType);
        }
    }

    private void getLoginDetailData(String username){
        repository.getLoginDetailData(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LoginDetailData>() {
                    @Override
                    public void onNext(LoginDetailData value) {
                        view.saveUsername2Preference(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void getLoginData(String username,String password, @NonNull final LoginType loginType){

        repository.getRemoteLoginData(username, password,loginType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LoginData>() {

                    @Override
                    public void onNext(LoginData value) {
                        view.saveUsername2Preference(value.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showLoginError( loginType);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void unSubscribe() {

    }


}
