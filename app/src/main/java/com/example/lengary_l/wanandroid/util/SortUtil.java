package com.example.lengary_l.wanandroid.util;

import com.example.lengary_l.wanandroid.data.ArticleDetailData;

public class SortUtil {

    public static int sortArticleDetailData(ArticleDetailData articleDetailData, ArticleDetailData t1) {
        if (articleDetailData.getPublishTime() > t1.getPublishTime()){
            return -1;
        }else {
            return 1;
        }
    }

}
