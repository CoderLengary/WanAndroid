package com.example.lengary_l.wanandroid.mvp.detail;

import com.example.lengary_l.wanandroid.mvp.BasePresenter;
import com.example.lengary_l.wanandroid.mvp.BaseView;

public interface DetailContract {
    interface Presenter extends BasePresenter{




        void favorite();

        void addToReadLater();

        
    }

    interface View extends BaseView<Presenter>{

    }
}
