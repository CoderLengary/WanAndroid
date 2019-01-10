package com.example.lengary_l.wanandroid.data.source.local;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.FavoriteArticleDetailData;
import com.example.lengary_l.wanandroid.data.LoginDetailData;
import com.example.lengary_l.wanandroid.data.source.FavoriteArticlesDataSource;
import com.example.lengary_l.wanandroid.realm.RealmHelper;

import java.util.List;

import io.reactivex.Observable;
import io.realm.Realm;

/**
 * Created by CoderLengary
 */


public class FavoriteArticlesDataLocalSource implements FavoriteArticlesDataSource {


    private static FavoriteArticlesDataLocalSource INSTANCE;

    private FavoriteArticlesDataLocalSource() {

    }

    public static FavoriteArticlesDataLocalSource getInstance() {
        if (INSTANCE == null) {
            synchronized (FavoriteArticlesDataLocalSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavoriteArticlesDataLocalSource();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<FavoriteArticleDetailData>> getFavoriteArticles(int page, boolean forceUpdate, boolean clearCache) {
        //Not required because the {@link FavoriteArticlesDataRemoteSource} has handled it.
        return null;
    }

    @Override
    public boolean isExist(int userId, int id) {
        Realm realm = RealmHelper.newRealmInstance();
        LoginDetailData data = realm.copyFromRealm(
                realm.where(LoginDetailData.class)
                        .equalTo("id", userId)
                        .findFirst()
        );
        List<Integer> collectIds = data.getCollectIds();

        if (collectIds.isEmpty()) {
            return false;
        }

        for (Integer collectId : collectIds) {
            if (id == collectId) {
                return true;
            }
        }
        return false;
    }
}
