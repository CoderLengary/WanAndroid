package com.example.lengary_l.wanandroid.data.source.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.lengary_l.wanandroid.data.Status;
import com.example.lengary_l.wanandroid.data.source.StatusDataSource;
import com.example.lengary_l.wanandroid.retrofit.RetrofitClient;
import com.example.lengary_l.wanandroid.retrofit.RetrofitService;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;

public class StatusDataRemoteSource implements StatusDataSource {
    @NonNull
    private static StatusDataRemoteSource INSTANCE;
    private static final String TAG = "StatusDataRemoteSource";
    private StatusDataRemoteSource() {

    }

    public static StatusDataRemoteSource getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new StatusDataRemoteSource();
        }
        return INSTANCE;
    }


    @Override
    public Observable<Status> collectArticle(final int userId, final int id) {
        return RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .collectArticle(id)
                .filter(new Predicate<Status>() {
                    @Override
                    public boolean test(Status status) throws Exception {
                        return status.getErrorCode() != -1;
                    }
                });
    }

    @Override
    public Observable<Status> uncollectArticle(final int userId, final int originId) {
        Log.e(TAG, "uncollectArticle: id is "+originId );
        return RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .uncollectArticle(originId)
                .filter(new Predicate<Status>() {
                    @Override
                    public boolean test(Status status) throws Exception {
                        return status.getErrorCode() != -1;
                    }
                });
    }


}
