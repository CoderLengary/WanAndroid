package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.Status;

import io.reactivex.Observable;

/**
 * Created by CoderLengary
 */


public class StatusDataRepository implements StatusDataSource {
    @NonNull
    private StatusDataSource remote;

    @NonNull
    private static StatusDataRepository INSTANCE;

    private StatusDataRepository(@NonNull StatusDataSource remote) {
        this.remote = remote;
    }

    public static StatusDataRepository getInstance(@NonNull StatusDataSource remote) {
        if (INSTANCE == null) {
            INSTANCE = new StatusDataRepository(remote);
        }
        return INSTANCE;
    }

    @Override
    public Observable<Status> collectArticle(@NonNull int userId, @NonNull int id) {
        return remote.collectArticle(userId,id);
    }

    @Override
    public Observable<Status> uncollectArticle(@NonNull int userId, @NonNull int originId) {
        return remote.uncollectArticle(userId, originId);
    }
}
