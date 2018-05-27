package com.example.lengary_l.wanandroid.mvp.login;

import com.example.lengary_l.wanandroid.data.LoginDetailData;
import com.example.lengary_l.wanandroid.mvp.BasePresenter;
import com.example.lengary_l.wanandroid.mvp.BaseView;

public interface LoginContract {
    interface Presenter extends BasePresenter{
        void login(String username, String password);

    }

    interface View extends BaseView<Presenter>{
        void showLoginError();

        void showRegisterError();

        void saveUsername2Preference(LoginDetailData loginDetailData);
    }
}
