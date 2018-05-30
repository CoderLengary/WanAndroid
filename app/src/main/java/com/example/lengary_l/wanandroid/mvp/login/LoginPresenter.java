package com.example.lengary_l.wanandroid.mvp.login;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.lengary_l.wanandroid.data.LoginData;
import com.example.lengary_l.wanandroid.data.LoginDetailData;
import com.example.lengary_l.wanandroid.data.LoginType;
import com.example.lengary_l.wanandroid.data.source.LoginDataRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter implements LoginContract.Presenter{

    @NonNull
    private LoginContract.View view;
    @NonNull
    private LoginDataRepository repository;

    private CompositeDisposable compositeDisposable;

    private static final String TAG = "LoginPresenter";


    public LoginPresenter(@NonNull LoginContract.View view, @NonNull LoginDataRepository loginDataRepository) {
        this.view = view;
        this.repository = loginDataRepository;
        this.view.setPresenter(this);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void login(String username, String password, @NonNull LoginType loginType) {
        if (repository.isAccountExist(username)&&loginType!=LoginType.TYPE_REGISTER){
            getLocalLoginData(username,password,loginType);
        }else {
            getLoginData(username, password,loginType);
        }
    }

    private void getLocalLoginData(String username, final String password, final LoginType loginType){
        Disposable disposable=repository.getLocalLoginData(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LoginDetailData>() {
                    @Override
                    public void onNext(LoginDetailData value) {
                        if (!value.getPassword().equals(password)){
                            view.showLoginError(loginType);
                        }else {
                            //view.saveUsername2Preference(value);
                        }
                        Log.e(TAG, "onNext local: username "+value.getUsername() );
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


    private void getLoginData(String username,String password, @NonNull final LoginType loginType){

        Disposable disposable=repository.getRemoteLoginData(username, password,loginType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LoginData>() {

                    @Override
                    public void onNext(LoginData value) {
                        Log.e(TAG, "onNext: is run" );
                        if (value.getErrorCode()==-1||value.getErrorMsg().isEmpty()){
                            view.showLoginError(loginType);
                        }else {
                            Log.e(TAG, "logindata code: "+value.getErrorCode() );
                            //view.saveUsername2Preference(value.getData());
                        }


                     // Log.e(TAG, "onNext: "+value.getData().getUsername()+" "+value.getData().getPassword()+" "+loginType.name() );
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showNetworkError();
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
