package com.example.lengary_l.wanandroid.data.source;

import com.example.lengary_l.wanandroid.data.HotKeyDetailData;

import java.util.List;

import io.reactivex.Observable;

public interface HotKeyDataSource {

    Observable<List<HotKeyDetailData>> getHotKeys(boolean forceUpdate);

}
