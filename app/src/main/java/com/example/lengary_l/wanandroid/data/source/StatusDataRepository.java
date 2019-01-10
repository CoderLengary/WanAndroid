package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.Status;

import io.reactivex.Observable;

/**
 * Created by CoderLengary
 */


public class StatusDataRepository implements StatusDataSource {
    @NonNull
    private final StatusDataSource remoteDataSource;


    private static StatusDataRepository INSTANCE;

    private StatusDataRepository(@NonNull StatusDataSource remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    public static StatusDataRepository getInstance(@NonNull StatusDataSource remote) {
        if (INSTANCE == null) {
            synchronized (StatusDataRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new StatusDataRepository(remote);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Observable<Status> collectArticle( int userId,  int id) {
        return remoteDataSource.collectArticle(userId,id);
    }

    @Override
    public Observable<Status> uncollectArticle(@NonNull int userId, @NonNull int originId) {
        return remoteDataSource.uncollectArticle(userId, originId);
    }
}
