package com.example.lengary_l.wanandroid.data.source;

import com.example.lengary_l.wanandroid.data.Status;

import io.reactivex.Observable;

public interface StatusDataSource {

    Observable<Status> collectArticle(int userId, int id);

    Observable<Status> uncollectArticle(int userId, int originId);

}
