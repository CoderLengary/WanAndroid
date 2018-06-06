package com.example.lengary_l.wanandroid.mvp.timeline;

import com.example.lengary_l.wanandroid.data.ArticleDetailData;
import com.example.lengary_l.wanandroid.mvp.BasePresenter;
import com.example.lengary_l.wanandroid.mvp.BaseView;

import java.util.List;

public interface ArticlesContract {

    interface Presenter extends BasePresenter{

        void getArticles(int page, boolean forceUpdate, boolean clearCache);
    }

    interface View extends BaseView<Presenter>{

        boolean isActive();

        void setLoadingIndicator(boolean isActive);

        void showArticles(List<ArticleDetailData> list);

        void showEmptyView();
    }
}
