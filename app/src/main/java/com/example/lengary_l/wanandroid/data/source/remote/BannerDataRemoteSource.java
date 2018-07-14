package com.example.lengary_l.wanandroid.data.source.remote;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.BannerData;
import com.example.lengary_l.wanandroid.data.BannerDetailData;
import com.example.lengary_l.wanandroid.data.source.BannerDataSource;
import com.example.lengary_l.wanandroid.retrofit.RetrofitClient;
import com.example.lengary_l.wanandroid.retrofit.RetrofitService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by CoderLengary
 */


public class BannerDataRemoteSource implements BannerDataSource {

    @NonNull
    private static BannerDataRemoteSource INSTANCE;

    private BannerDataRemoteSource() {

    }

    public static BannerDataRemoteSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BannerDataRemoteSource();
        }
        return INSTANCE;
    }


    @Override
    public Observable<List<BannerDetailData>> getBanner() {
        return RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .getBanner()
                .filter(new Predicate<BannerData>() {
                    @Override
                    public boolean test(BannerData bannerData) throws Exception {
                        return bannerData.getErrorCode() != -1;
                    }
                })
                .flatMap(new Function<BannerData, ObservableSource<List<BannerDetailData>>>() {
                    @Override
                    public ObservableSource<List<BannerDetailData>> apply(BannerData bannerData) throws Exception {
                        return Observable.fromIterable(bannerData.getData()).toList().toObservable();
                    }
                });
    }
}
