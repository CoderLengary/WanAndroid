package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.HotKeyDetailData;
import com.example.lengary_l.wanandroid.util.SortDescendUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by CoderLengary
 */


public class HotKeyDataRepository implements HotKeyDataSource {
    @NonNull
    private static HotKeyDataRepository INSTANCE;
    @NonNull
    private final HotKeyDataSource remoteDataSource;
    private HashMap<Integer, HotKeyDetailData> cache;

    private HotKeyDataRepository(@NonNull HotKeyDataSource remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    public static HotKeyDataRepository getInstance(@NonNull HotKeyDataSource remoteDataSource){
        if (INSTANCE == null) {
            INSTANCE = new HotKeyDataRepository(remoteDataSource);
        }
        return INSTANCE;
    }



    @Override
    public Observable<List<HotKeyDetailData>> getHotKeys(@NonNull final boolean forceUpdate) {

        if (!forceUpdate && cache != null) {
            return Observable.fromIterable(new ArrayList<>(cache.values())).toSortedList(
                    new Comparator<HotKeyDetailData>() {
                        @Override
                        public int compare(HotKeyDetailData hotKeyDetailData, HotKeyDetailData t1) {
                            return SortDescendUtil.sortHotKeyDetailData(hotKeyDetailData, t1);
                        }
                    }
            ).toObservable();
        }


        return remoteDataSource.getHotKeys(forceUpdate).doOnNext(new Consumer<List<HotKeyDetailData>>() {
            @Override
            public void accept(List<HotKeyDetailData> list) {
                refreshCache(forceUpdate, list);
            }
        });
    }

    private void refreshCache(@NonNull boolean forceUpdate, @NonNull List<HotKeyDetailData> list){
        if (cache == null) {
            cache = new LinkedHashMap<>();
        }
        if (forceUpdate){
            cache.clear();
        }
        for (HotKeyDetailData data : list) {
            cache.put(data.getId(), data);
        }
    }


}
