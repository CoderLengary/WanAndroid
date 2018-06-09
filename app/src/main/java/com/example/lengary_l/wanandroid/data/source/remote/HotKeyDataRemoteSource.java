package com.example.lengary_l.wanandroid.data.source.remote;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.HotKeysData;
import com.example.lengary_l.wanandroid.data.HotKeyDetailData;
import com.example.lengary_l.wanandroid.data.source.HotKeyDataSource;
import com.example.lengary_l.wanandroid.retrofit.RetrofitClient;
import com.example.lengary_l.wanandroid.retrofit.RetrofitService;

import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class HotKeyDataRemoteSource implements HotKeyDataSource {

    @NonNull
    private static HotKeyDataRemoteSource INSTANCE;

    public static HotKeyDataRemoteSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HotKeyDataRemoteSource();
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
                    public boolean test(HotKeysData hotKeysData) throws Exception {
                        return hotKeysData.getErrorCode() != -1;
                    }
                })
                .flatMap(new Function<HotKeysData, ObservableSource<List<HotKeyDetailData>>>() {
                    @Override
                    public ObservableSource<List<HotKeyDetailData>> apply(HotKeysData hotKeysData) throws Exception {
                        return Observable.fromIterable(hotKeysData.getData()).toSortedList(new Comparator<HotKeyDetailData>() {
                            @Override
                            public int compare(HotKeyDetailData hotKeyDetailData, HotKeyDetailData t1) {
                               if (hotKeyDetailData.getOrder()>t1.getOrder()){
                                   return 1;
                               }else {
                                   return -1;
                               }
                            }
                        }).toObservable();
                    }
                });

    }
}
