package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.Status;

import io.reactivex.Observable;

public class StatusDataRepository implements StatusDataSource {
    private StatusDataSource remote;

    @NonNull
    private static StatusDataRepository INSTANCE;

    private StatusDataRepository(StatusDataSource remote) {
        this.remote = remote;
    }

    public static StatusDataRepository getInstance(StatusDataSource remote) {
        if (INSTANCE == null) {
            INSTANCE = new StatusDataRepository(remote);
        }
        return INSTANCE;
    }

    @Override
    public Observable<Status> collectArticle(int userId, int id) {
        return remote.collectArticle(userId,id);
    }

    @Override
    public Observable<Status> uncollectArticle(int userId, int originId) {
        return remote.uncollectArticle(userId, originId);
    }
}
