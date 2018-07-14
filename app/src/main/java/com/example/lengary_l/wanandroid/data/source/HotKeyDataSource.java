package com.example.lengary_l.wanandroid.data.source;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.HotKeyDetailData;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by CoderLengary
 */


public interface HotKeyDataSource {

    Observable<List<HotKeyDetailData>> getHotKeys(@NonNull boolean forceUpdate);

}
