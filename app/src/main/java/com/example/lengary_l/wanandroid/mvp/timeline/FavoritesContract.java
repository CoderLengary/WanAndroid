package com.example.lengary_l.wanandroid.mvp.timeline;

import com.example.lengary_l.wanandroid.data.FavoriteArticleDetailData;
import com.example.lengary_l.wanandroid.mvp.BasePresenter;
import com.example.lengary_l.wanandroid.mvp.BaseView;

import java.util.List;

/**
 * Created by CoderLengary
 */


public interface FavoritesContract {
    interface Presenter extends BasePresenter {



        void getFavoriteArticles(int page, boolean forceUpdate, boolean clearCache);



    }

    interface View extends BaseView<Presenter> {
        void showFavoriteArticles(List<FavoriteArticleDetailData> list);

        boolean isActive();

        void showEmptyView(boolean toShow);

        void setLoadingIndicator(boolean isActive);


    }

}
