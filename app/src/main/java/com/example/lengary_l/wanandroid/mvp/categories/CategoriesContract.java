package com.example.lengary_l.wanandroid.mvp.categories;

import com.example.lengary_l.wanandroid.data.CategoryDetailData;
import com.example.lengary_l.wanandroid.mvp.BasePresenter;
import com.example.lengary_l.wanandroid.mvp.BaseView;

import java.util.List;

public interface CategoriesContract {
    interface Presenter extends BasePresenter{
        void getCategories(boolean forceUpdate);
    }

    interface View extends BaseView<Presenter>{
        void showCategories(List<CategoryDetailData> list);

        void showEmptyView();

        void setLoadingIndicator(boolean isActive);

        boolean isActive();
    }

}
