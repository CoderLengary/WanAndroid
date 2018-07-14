package com.example.lengary_l.wanandroid.util;

import com.example.lengary_l.wanandroid.data.ArticleDetailData;
import com.example.lengary_l.wanandroid.data.CategoryDetailData;
import com.example.lengary_l.wanandroid.data.FavoriteArticleDetailData;
import com.example.lengary_l.wanandroid.data.HotKeyDetailData;

/**
 * Created by CoderLengary
 */


public class SortDescendUtil {

    public static int sortArticleDetailData(ArticleDetailData articleDetailData, ArticleDetailData t1) {
        if (articleDetailData.getPublishTime() > t1.getPublishTime()){
            return -1;
        }else {
            return 1;
        }
    }

    public static int sortFavoriteDetailData(FavoriteArticleDetailData favoriteArticleDetailData, FavoriteArticleDetailData t1) {
        if (favoriteArticleDetailData.getPublishTime() > t1.getPublishTime()) {
            return -1;
        } else {
            return 1;
        }
    }

    public static int sortCategoryDetailData(CategoryDetailData categoryDetailData, CategoryDetailData t1) {
        if (categoryDetailData.getOrder()>t1.getOrder()){
            return 1;
        }else {
            return -1;
        }
    }

    public static int sortHotKeyDetailData(HotKeyDetailData hotKeyDetailData, HotKeyDetailData t1) {
        if (hotKeyDetailData.getOrder()>t1.getOrder()){
            return 1;
        }else {
            return -1;
        }
    }

}
