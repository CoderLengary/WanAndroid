package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.BannerDetailData;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by CoderLengary
 */


public class BannerDataRepository implements BannerDataSource{

    private final BannerDataSource remoteDataSource;

    @NonNull
    private static BannerDataRepository INSTANCE = null;

    private BannerDataRepository(@NonNull BannerDataSource remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    public static BannerDataRepository getInstance(@NonNull BannerDataSource remoteDataSource){
        if (INSTANCE == null) {
            INSTANCE = new BannerDataRepository(remoteDataSource);
        }
        return INSTANCE;
    }
    @Override
    public Observable<List<BannerDetailData>> getBanner() {
        return remoteDataSource.getBanner();
    }
}
