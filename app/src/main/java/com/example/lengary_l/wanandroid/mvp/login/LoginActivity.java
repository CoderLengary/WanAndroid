package com.example.lengary_l.wanandroid.mvp.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.lengary_l.wanandroid.R;
import com.example.lengary_l.wanandroid.data.source.LoginDataRepository;
import com.example.lengary_l.wanandroid.data.source.local.LoginDataLocalSource;
import com.example.lengary_l.wanandroid.data.source.remote.LoginDataRemoteSource;

public class LoginActivity extends AppCompatActivity {
    private LoginFragment loginFragment;
    private SignUpFragment signUpFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);
        loginFragment = LoginFragment.newInstance();
        signUpFragment = SignUpFragment.newInstance();
        if (!loginFragment.isAdded()){
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.view_pager, loginFragment, "LoginFragment")
                        .commit();
            }
            if (!signUpFragment.isAdded()){
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.view_pager, signUpFragment, "SignUpFragment")
                        .commit();
            }
            new LoginPresenter(loginFragment, LoginDataRepository.getInstance(
                    LoginDataLocalSource.getInstance(),
                    LoginDataRemoteSource.getInstance()
            ));
            new LoginPresenter(signUpFragment, LoginDataRepository.getInstance(
                    LoginDataLocalSource.getInstance(),
                    LoginDataRemoteSource.getInstance()
            ));
            showLoginFragment();

    }

    public void showLoginFragment(){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.right_in,R.anim.right_out)
                .show(loginFragment)
                .hide(signUpFragment)
                .commit();

    }

    public void showSignUpFragment(){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.left_in,R.anim.left_out)
                .show(signUpFragment)
                .hide(loginFragment)
                .commit();

    }




}
