package com.example.lengary_l.wanandroid.data.source.local;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.LoginData;
import com.example.lengary_l.wanandroid.data.LoginDetailData;
import com.example.lengary_l.wanandroid.data.LoginType;
import com.example.lengary_l.wanandroid.data.source.LoginDataSource;
import com.example.lengary_l.wanandroid.realm.RealmHelper;

import io.reactivex.Observable;
import io.realm.Realm;

/**
 * Created by CoderLengary
 */


public class LoginDataLocalSource implements LoginDataSource{
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
        //Not required because the {@link LoginDataRemoteSource}  has handled it
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

}
