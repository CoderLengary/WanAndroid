package com.example.lengary_l.wanandroid.mvp.login;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.lengary_l.wanandroid.R;
import com.example.lengary_l.wanandroid.data.source.LoginDataRepository;
import com.example.lengary_l.wanandroid.data.source.ReadLaterArticlesRepository;
import com.example.lengary_l.wanandroid.data.source.local.LoginDataLocalSource;
import com.example.lengary_l.wanandroid.data.source.local.ReadLaterArticlesLocalSource;
import com.example.lengary_l.wanandroid.data.source.remote.LoginDataRemoteSource;

/**
 * Created by CoderLengary
 */


public class LoginActivity extends AppCompatActivity {
    private LoginFragment loginFragment;
    private SignUpFragment signUpFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("navigation_bar_tint", true)) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        }
        if (savedInstanceState != null) {
            FragmentManager manager = getSupportFragmentManager();
            loginFragment = (LoginFragment) manager.getFragment(savedInstanceState, LoginFragment.class.getSimpleName());
            signUpFragment = (SignUpFragment) manager.getFragment(savedInstanceState, LoginFragment.class.getSimpleName());
        }else {
            loginFragment = LoginFragment.newInstance();
            signUpFragment = SignUpFragment.newInstance();
        }

        if (!loginFragment.isAdded()){
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.view_pager, loginFragment, LoginFragment.class.getSimpleName())
                        .commit();
            }
            if (!signUpFragment.isAdded()){
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.view_pager, signUpFragment, SignUpFragment.class.getSimpleName())
                        .commit();
            }
            new LoginPresenter(loginFragment, LoginDataRepository.getInstance(
                    LoginDataLocalSource.getInstance(),
                    LoginDataRemoteSource.getInstance()
            ), ReadLaterArticlesRepository.getInstance(ReadLaterArticlesLocalSource.getInstance()));
            new LoginPresenter(signUpFragment, LoginDataRepository.getInstance(
                    LoginDataLocalSource.getInstance(),
                    LoginDataRemoteSource.getInstance()
            ), ReadLaterArticlesRepository.getInstance(ReadLaterArticlesLocalSource.getInstance()));
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentManager manager = getSupportFragmentManager();
        if (loginFragment.isAdded()) {
            manager.putFragment(outState, LoginFragment.class.getSimpleName(), loginFragment);
        }
        if (signUpFragment.isAdded()) {
            manager.putFragment(outState, SignUpFragment.class.getSimpleName(), signUpFragment);
        }
    }
}
