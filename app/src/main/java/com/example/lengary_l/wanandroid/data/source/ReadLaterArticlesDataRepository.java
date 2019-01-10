package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.ReadLaterArticleData;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by CoderLengary
 */


public class ReadLaterArticlesDataRepository implements ReadLaterArticlesDataSource {
    @NonNull
    private final ReadLaterArticlesDataSource localDataSource;


    private static ReadLaterArticlesDataRepository INSTANCE;

    private ReadLaterArticlesDataRepository(@NonNull ReadLaterArticlesDataSource local) {
        this.localDataSource = local;
    }

    public static ReadLaterArticlesDataRepository getInstance(@NonNull ReadLaterArticlesDataSource local) {
        if (INSTANCE == null) {
            INSTANCE = new ReadLaterArticlesDataRepository(local);
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<ReadLaterArticleData>> getReadLaterArticles(int userId) {
        return localDataSource.getReadLaterArticles(userId);
    }

    @Override
    public void insertReadLaterArticle(int userId, int id, long timeStamp){
        localDataSource.insertReadLaterArticle(userId, id, timeStamp);
    }

    @Override
    public void removeReadLaterArticle(int userId, int id) {
        localDataSource.removeReadLaterArticle(userId, id);
    }

    @Override
    public boolean isExist(int userId, int id) {
        return localDataSource.isExist(userId, id);
    }

    @Override
    public void clearAll() {
        localDataSource.clearAll();
    }
}
