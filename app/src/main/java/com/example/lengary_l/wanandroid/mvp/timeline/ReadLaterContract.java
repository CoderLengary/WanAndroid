package com.example.lengary_l.wanandroid.mvp.timeline;

import com.example.lengary_l.wanandroid.data.ReadLaterArticleData;
import com.example.lengary_l.wanandroid.mvp.BasePresenter;
import com.example.lengary_l.wanandroid.mvp.BaseView;

import java.util.List;

/**
 * Created by CoderLengary
 */


public interface ReadLaterContract {
    interface Presenter extends BasePresenter{
        void getReadLaterArticles(int userId);

    }

    interface View extends BaseView<Presenter>{
        void showReadLaterArticles(List<ReadLaterArticleData> list);


        boolean isActive();

        void showEmptyView(boolean toShow);

    }
}
