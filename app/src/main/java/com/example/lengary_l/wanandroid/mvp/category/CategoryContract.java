package com.example.lengary_l.wanandroid.mvp.category;

import com.example.lengary_l.wanandroid.data.ArticleDetailData;
import com.example.lengary_l.wanandroid.mvp.BasePresenter;
import com.example.lengary_l.wanandroid.mvp.BaseView;

import java.util.List;

/**
 * Created by CoderLengary
 */


public interface CategoryContract {
    interface Presenter extends BasePresenter{
        void getArticlesFromCatg(int page, int categoryId, boolean forceUpdate, boolean clearCache);
    }

    interface View extends BaseView<Presenter>{
        void showArticles(List<ArticleDetailData> list);

        void showEmptyView(boolean toShow);

        boolean isActive();

    }
}
