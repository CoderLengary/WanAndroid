package com.example.lengary_l.wanandroid.mvp.search;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.ArticleDetailData;
import com.example.lengary_l.wanandroid.data.HotKeyDetailData;
import com.example.lengary_l.wanandroid.mvp.BasePresenter;
import com.example.lengary_l.wanandroid.mvp.BaseView;

import java.util.List;

/**
 * Created by CoderLengary
 */


public interface SearchContract {
    interface Presenter extends BasePresenter{
        void getHotKeys(boolean forceUpdate);
        void searchArticles(@NonNull int page, @NonNull String keyWords, boolean forceUpdate , boolean clearCache);
    }

    interface View extends BaseView<Presenter>{
        void showArticles(List<ArticleDetailData> articlesList);

        void showHotKeys(List<HotKeyDetailData> hotKeyList);

        boolean isActive();

        void hideImn();

        void showEmptyView(boolean toShow);
    }
}
