package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.ReadLaterArticleData;

import java.util.List;

import io.reactivex.Observable;

public class ReadLaterArticlesRepository implements ReadLaterArticlesDataSource {
    private ReadLaterArticlesDataSource local;

    @NonNull
    private static ReadLaterArticlesRepository INSTANCE;

    private ReadLaterArticlesRepository(ReadLaterArticlesDataSource local) {
        this.local = local;
    }

    public static ReadLaterArticlesRepository getInstance(ReadLaterArticlesDataSource local) {
        if (INSTANCE == null) {
            INSTANCE = new ReadLaterArticlesRepository(local);
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<ReadLaterArticleData>> getReadLaterArticles(int userId) {
        return local.getReadLaterArticles(userId);
    }

    @Override
    public void insertReadLaterArticle(int userId, int id, long timeStamp){
        local.insertReadLaterArticle(userId, id, timeStamp);
    }

    @Override
    public void removeReadLaterArticle(int userId, int id) {
        local.removeReadLaterArticle(userId, id);
    }

    @Override
    public boolean isExist(int userId, int id) {
        return local.isExist(userId, id);
    }
}
