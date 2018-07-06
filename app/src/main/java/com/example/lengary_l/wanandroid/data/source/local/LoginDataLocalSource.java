package com.example.lengary_l.wanandroid.data.source.local;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.LoginData;
import com.example.lengary_l.wanandroid.data.LoginDetailData;
import com.example.lengary_l.wanandroid.data.LoginType;
import com.example.lengary_l.wanandroid.data.source.LoginDataSource;
import com.example.lengary_l.wanandroid.realm.RealmHelper;

import java.util.List;

import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmResults;

public class LoginDataLocalSource implements LoginDataSource{
    private static final String TAG = "LoginDataLocalSource";
    @NonNull
    private static LoginDataLocalSource INSTANCE;


    private LoginDataLocalSource() {

    }

    public static LoginDataLocalSource getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new LoginDataLocalSource();
        }
        return INSTANCE;
    }


    @Override
    public Observable<LoginData> getRemoteLoginData(@NonNull String userName, @NonNull String password, @NonNull LoginType loginType) {
        //Not require because the RemoteDataSource has handled it
        return null;
    }

    @Override
    public Observable<LoginDetailData> getLocalLoginData(@NonNull int userId) {
        Realm realm = RealmHelper.newRealmInstance();
        LoginDetailData loginDetailData = realm.copyFromRealm(
                realm.where(LoginDetailData.class)
                        .equalTo("id", userId)
                        .findFirst());
        return Observable.just(loginDetailData);
    }

    @Override
    public boolean isAccountExist(@NonNull int userId) {
        Realm realm = RealmHelper.newRealmInstance();
        RealmResults<LoginDetailData> list =
                realm.where(LoginDetailData.class)
                        .equalTo("id", userId)
                        .findAll();
        return (list != null) && (!list.isEmpty());

    }

    @Override
    public Observable<List<Integer>> getFavoriteArticleIdList(@NonNull int userId) {
        Realm realm = RealmHelper.newRealmInstance();
        LoginDetailData data = realm.copyFromRealm(
                realm.where(LoginDetailData.class)
                        .equalTo("id", userId)
                        .findFirst()
        );
        List<Integer> list = data.getCollectIds();
        return Observable.just(list);
    }


}
