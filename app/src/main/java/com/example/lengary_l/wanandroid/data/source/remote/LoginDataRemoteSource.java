package com.example.lengary_l.wanandroid.data.source.remote;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.data.LoginData;
import com.example.lengary_l.wanandroid.data.LoginDetailData;
import com.example.lengary_l.wanandroid.data.LoginType;
import com.example.lengary_l.wanandroid.data.source.LoginDataSource;
import com.example.lengary_l.wanandroid.realm.RealmHelper;
import com.example.lengary_l.wanandroid.retrofit.RetrofitClient;
import com.example.lengary_l.wanandroid.retrofit.RetrofitService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class LoginDataRemoteSource implements LoginDataSource{
    private static final String TAG = "LoginDataRemoteSource";
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
    public Observable<LoginData> getRemoteLoginData(@NonNull String userName, @NonNull String password, @NonNull LoginType loginType) {
       Observable<LoginData> loginDataObservable = null;
        if (loginType==LoginType.TYPE_REGISTER){
            loginDataObservable = RetrofitClient.getInstance()
                    .create(RetrofitService.class)
                    .register(userName, password, password);
        }else if (loginType==LoginType.TYPE_LOGIN){
            loginDataObservable=RetrofitClient.getInstance()
                    .create(RetrofitService.class)
                    .login(userName, password);
        }
        return loginDataObservable
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<LoginData>() {
            @Override
            public void accept(LoginData loginData) throws Exception {
                if (loginData.getErrorCode()!=-1||loginData.getData() != null) {
                    //save the remote data to local.Only the data which is not null will be saved
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
    public Observable<LoginDetailData> getLocalLoginData(@NonNull int userId) {
        //Not require because the LocalDataSource has handled it
        return null;
    }

    @Override
    public boolean isAccountExist(@NonNull int userId) {
        //Not require because the LocalDataSource has handled it
        return false;
    }

    @Override
    public Observable<List<Integer>> getFavoriteArticleIdList(@NonNull int userId) {
        //Not require because the LocalDataSource has handled it
        return null;
    }


}
