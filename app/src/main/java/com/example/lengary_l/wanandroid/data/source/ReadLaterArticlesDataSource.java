package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.ReadLaterArticleData;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by CoderLengary
 */


public interface ReadLaterArticlesDataSource {
    Observable<List<ReadLaterArticleData>> getReadLaterArticles(@NonNull int userId);

    void insertReadLaterArticle(int userId, @NonNull int id, @NonNull long timeStamp);

    void removeReadLaterArticle(int userId, @NonNull int id);

    boolean isExist(int userId, @NonNull int id);

    void clearAll();
}
