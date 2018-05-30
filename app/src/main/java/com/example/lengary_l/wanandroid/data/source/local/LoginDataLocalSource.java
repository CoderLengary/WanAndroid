package com.example.lengary_l.wanandroid.data.source.local;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.lengary_l.wanandroid.data.LoginData;
import com.example.lengary_l.wanandroid.data.LoginDetailData;
import com.example.lengary_l.wanandroid.data.LoginType;
import com.example.lengary_l.wanandroid.data.source.LoginDataSource;
import com.example.lengary_l.wanandroid.realm.RealmHelper;

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
    public Observable<LoginDetailData> getLocalLoginData(@NonNull String userName) {
        Realm realm = RealmHelper.newRealmInstance();
        LoginDetailData loginDetailData = realm.copyFromRealm(
                realm.where(LoginDetailData.class)
                        .equalTo("username", userName)
                        .findFirst());
        Log.e(TAG, "getLoginDetailData: get data from realm "+loginDetailData.getUsername() );
        return Observable.just(loginDetailData);
    }

    @Override
    public boolean isAccountExist(String userName) {
        Realm realm = RealmHelper.newRealmInstance();
        RealmResults<LoginDetailData> list =
                realm.where(LoginDetailData.class)
                        .equalTo("username", userName)
                        .findAll();
        return (list != null) && (!list.isEmpty());

    }


}
