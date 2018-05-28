package com.example.lengary_l.wanandroid.mvp.login;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.LoginDetailData;
import com.example.lengary_l.wanandroid.data.LoginType;
import com.example.lengary_l.wanandroid.mvp.BasePresenter;
import com.example.lengary_l.wanandroid.mvp.BaseView;

public interface LoginContract {
    interface Presenter extends BasePresenter{
        void login(String username, String password, @NonNull LoginType loginType);

    }

    interface View extends BaseView<Presenter>{
        void showLoginError( @NonNull LoginType loginType);



        void saveUsername2Preference(LoginDetailData loginDetailData);
    }
}
