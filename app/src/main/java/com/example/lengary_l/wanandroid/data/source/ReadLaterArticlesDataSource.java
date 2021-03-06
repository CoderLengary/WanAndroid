package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.ReadLaterArticleData;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by CoderLengary
 */


public interface ReadLaterArticlesDataSource {
    Observable<List<ReadLaterArticleData>> getReadLaterArticles(int userId);

    void insertReadLaterArticle(int userId, int id, long timeStamp);

    void removeReadLaterArticle(int userId, int id);

    boolean isExist(int userId, int id);

    void clearAll();
}
