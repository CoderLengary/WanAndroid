package com.example.lengary_l.wanandroid.data.source.remote;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.LoginData;
import com.example.lengary_l.wanandroid.data.LoginDetailData;
import com.example.lengary_l.wanandroid.data.source.LoginDataSource;
import com.example.lengary_l.wanandroid.realm.RealmHelper;
import com.example.lengary_l.wanandroid.retrofit.RetrofitClient;
import com.example.lengary_l.wanandroid.retrofit.RetrofitService;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class LoginDataRemoteSource implements LoginDataSource{

    @NonNull
    private static LoginDataRemoteSource INSTANCE;

    private LoginDataRemoteSource(){

    }

    public static LoginDataRemoteSource getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new LoginDataRemoteSource();
        }
        return INSTANCE;
    }

    @Override
    public Observable<LoginData> getRemoteLoginData(@NonNull String userName, @NonNull String password) {

        Observable<LoginData> loginDataObservable=RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .register(userName, password, password)
                .filter(new Predicate<LoginData>() {
                    @Override
                    public boolean test(LoginData loginData) throws Exception {
                        return loginData.getErrorCode() != -1 && loginData.getData() != null;
                    }
                });



        return RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .login(userName,password)
                .filter(new Predicate<LoginData>() {
                    @Override
                    public boolean test(LoginData loginData) throws Exception {
                        return loginData.getErrorCode() != -1 && loginData.getData() != null;
                    }
                })
                .doOnNext(new Consumer<LoginData>() {
                    @Override
                    public void accept(LoginData loginData) throws Exception {
                        if (loginData!=null){
                            Realm realm = Realm.getInstance(new RealmConfiguration.Builder()
                                    .name(RealmHelper.DATABASE_NAME)
                                    .deleteRealmIfMigrationNeeded()
                                    .build());
                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(loginData.getData());
                            realm.commitTransaction();
                            realm.close();
                        }
                    }
                });
    }

    @Override
    public Observable<LoginDetailData> getLoginDetailData(@NonNull String userName) {
        //Not require because the LocalDataSource has handled it
        return null;
    }

    @Override
    public boolean isAccountExist(String userName) {
        return false;
    }


}
