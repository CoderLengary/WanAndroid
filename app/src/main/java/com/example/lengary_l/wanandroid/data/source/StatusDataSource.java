package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.Status;

import io.reactivex.Observable;

/**
 * Created by CoderLengary
 */


public interface StatusDataSource {

    Observable<Status> collectArticle(int userId, int id);

    Observable<Status> uncollectArticle(int userId, int originId);

}
