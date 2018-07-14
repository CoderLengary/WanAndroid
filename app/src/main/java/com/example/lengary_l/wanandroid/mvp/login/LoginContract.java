package com.example.lengary_l.wanandroid.mvp.login;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.LoginDetailData;
import com.example.lengary_l.wanandroid.data.LoginType;
import com.example.lengary_l.wanandroid.mvp.BasePresenter;
import com.example.lengary_l.wanandroid.mvp.BaseView;

/**
 * Created by CoderLengary
 */


public interface LoginContract {
    interface Presenter extends BasePresenter{
        void login(String username, String password, @NonNull LoginType loginType);

        void clearReadLaterData();

    }

    interface View extends BaseView<Presenter>{
        void showLoginError( String errorMsg);

        boolean isActive();

        void saveUser2Preference(LoginDetailData loginDetailData);

        void showNetworkError();
    }
}
