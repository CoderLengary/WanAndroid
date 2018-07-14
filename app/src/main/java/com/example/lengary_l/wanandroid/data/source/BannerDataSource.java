package com.example.lengary_l.wanandroid.data.source;

import com.example.lengary_l.wanandroid.data.BannerDetailData;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by CoderLengary
 */


public interface BannerDataSource {
    Observable<List<BannerDetailData>> getBanner();
}
