package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.BannerDetailData;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by CoderLengary
 */


public class BannerDataRepository implements BannerDataSource{

    private BannerDataSource remote;

    @NonNull
    private static BannerDataRepository INSTANCE = null;

    private BannerDataRepository(@NonNull BannerDataSource remote) {
        this.remote = remote;
    }

    public static BannerDataRepository getInstance(@NonNull BannerDataSource remote){
        if (INSTANCE == null) {
            INSTANCE = new BannerDataRepository(remote);
        }
        return INSTANCE;
    }
    @Override
    public Observable<List<BannerDetailData>> getBanner() {
        return remote.getBanner();
    }
}
