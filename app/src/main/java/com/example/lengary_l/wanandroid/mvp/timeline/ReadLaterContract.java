package com.example.lengary_l.wanandroid.mvp.timeline;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.ReadLaterArticleData;
import com.example.lengary_l.wanandroid.mvp.BasePresenter;
import com.example.lengary_l.wanandroid.mvp.BaseView;

import java.util.List;

public interface ReadLaterContract {
    interface Presenter extends BasePresenter{
        void getReadLaterArticles(int userId);

        void refreshCollectIdList(@NonNull int userId);
    }

    interface View extends BaseView<Presenter>{
        void showReadLaterArticles(List<ReadLaterArticleData> list);

        void saveFavoriteArticleIdList(List<Integer> list);

        boolean isActive();

        void showEmptyView(boolean toShow);

    }
}
