package com.example.lengary_l.wanandroid.mvp.login;

import com.example.lengary_l.wanandroid.mvp.BasePresenter;
import com.example.lengary_l.wanandroid.mvp.BaseView;

public interface LoginContract {
    interface Presenter extends BasePresenter{

    }

    interface View extends BaseView<Presenter>{
        void showLoginError();

        void showRegisterError();
    }
}
