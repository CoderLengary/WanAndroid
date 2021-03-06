package com.example.lengary_l.wanandroid.data.source.remote;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.HotKeysData;
import com.example.lengary_l.wanandroid.data.HotKeyDetailData;
import com.example.lengary_l.wanandroid.data.source.HotKeyDataSource;
import com.example.lengary_l.wanandroid.retrofit.RetrofitClient;
import com.example.lengary_l.wanandroid.retrofit.RetrofitService;
import com.example.lengary_l.wanandroid.util.SortDescendUtil;

import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by CoderLengary
 */


public class HotKeyDataRemoteSource implements HotKeyDataSource {


    private static HotKeyDataRemoteSource INSTANCE;

    public static HotKeyDataRemoteSource getInstance() {
        if (INSTANCE == null) {
            synchronized (HotKeyDataRemoteSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HotKeyDataRemoteSource();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<HotKeyDetailData>> getHotKeys(boolean forceUpdate) {
        return RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .getHotKeys()
                .filter(new Predicate<HotKeysData>() {
                    @Override
                    public boolean test(HotKeysData hotKeysData) {
                        return hotKeysData.getErrorCode() == 0;
                    }
                })
                //Use flatMap function to gain the  HotKeyDetailData of HotKeysData
                .flatMap(new Function<HotKeysData, ObservableSource<List<HotKeyDetailData>>>() {
                    @Override
                    public ObservableSource<List<HotKeyDetailData>> apply(HotKeysData hotKeysData) {
                        return Observable.fromIterable(hotKeysData.getData()).toSortedList(new Comparator<HotKeyDetailData>() {
                            @Override
                            public int compare(HotKeyDetailData hotKeyDetailData, HotKeyDetailData t1) {
                                return SortDescendUtil.sortHotKeyDetailData(hotKeyDetailData, t1);
                            }
                        }).toObservable();
                    }
                });

    }
}
