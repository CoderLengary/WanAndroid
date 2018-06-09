package com.example.lengary_l.wanandroid.data.source;

import com.example.lengary_l.wanandroid.data.HotKeyDetailData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class HotKeyDataRepository implements HotKeyDataSource {
    private static HotKeyDataRepository INSTANCE;
    private HotKeyDataSource remote;
    private HashMap<Integer, HotKeyDetailData> cache;

    private HotKeyDataRepository(HotKeyDataSource remote) {
        this.remote = remote;
    }

    public static HotKeyDataRepository getInstance(HotKeyDataSource remote){
        if (INSTANCE == null) {
            INSTANCE = new HotKeyDataRepository(remote);
        }
        return INSTANCE;
    }



    @Override
    public Observable<List<HotKeyDetailData>> getHotKeys(final boolean forceUpdate) {

        if (!forceUpdate && cache != null) {
            return Observable.fromIterable(new ArrayList<>(cache.values())).toSortedList(
                    new Comparator<HotKeyDetailData>() {
                        @Override
                        public int compare(HotKeyDetailData hotKeyDetailData, HotKeyDetailData t1) {
                            if (hotKeyDetailData.getOrder()>t1.getOrder()){
                                return 1;
                            }else {
                                return -1;
                            }
                        }
                    }
            ).toObservable();
        }


        return remote.getHotKeys(forceUpdate).doOnNext(new Consumer<List<HotKeyDetailData>>() {
            @Override
            public void accept(List<HotKeyDetailData> list) throws Exception {
                refreshCache(forceUpdate, list);
            }
        });
    }

    private void refreshCache(boolean forceUpdate,List<HotKeyDetailData> list){
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
