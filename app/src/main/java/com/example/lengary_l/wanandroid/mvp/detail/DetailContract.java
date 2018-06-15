package com.example.lengary_l.wanandroid.mvp.detail;

import com.example.lengary_l.wanandroid.mvp.BasePresenter;
import com.example.lengary_l.wanandroid.mvp.BaseView;

public interface DetailContract {
    interface Presenter extends BasePresenter{

        void collectArticle(int id);

        void uncollectArticle(int originId);

        void addToReadLater(int id, int userId);

        
    }

    interface View extends BaseView<Presenter>{

    }
}
