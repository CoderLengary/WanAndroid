package com.example.lengary_l.wanandroid.mvp.detail;

import com.example.lengary_l.wanandroid.mvp.BasePresenter;
import com.example.lengary_l.wanandroid.mvp.BaseView;

public interface DetailContract {
    interface Presenter extends BasePresenter{

        void collectArticle(int userId,int originId);

        void uncollectArticle(int userId, int originId);

        void addToReadLater(int id, int userId);


        
    }

    interface View extends BaseView<Presenter>{
        void showCollectStatus(boolean isSuccess);

        void showUnCollectStatus(boolean isSuccess);

        boolean isActive();

        void changeFavoriteState();
    }
}
