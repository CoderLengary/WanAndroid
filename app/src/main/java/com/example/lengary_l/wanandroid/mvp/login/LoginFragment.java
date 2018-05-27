package com.example.lengary_l.wanandroid.mvp.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lengary_l.wanandroid.MainActivity;
import com.example.lengary_l.wanandroid.R;
import com.example.lengary_l.wanandroid.data.LoginDetailData;
import com.example.lengary_l.wanandroid.util.SettingsUtil;

public class LoginFragment extends Fragment implements LoginContract.View{

    private TextInputEditText editUserName;
    private TextInputEditText editPassword;
    private AppCompatButton btnLogin;
    private TextView linkSignUp;
    private LoginContract.Presenter presenter;


    public LoginFragment() {

    }

    public static LoginFragment newInstance(){
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initViews(view);
        linkSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity loginActivity = (LoginActivity) getActivity();
                loginActivity.showSignUpFragment();
            }
        });

        return view;
    }

    @Override
    public void showLoginError() {
        Snackbar.make(linkSignUp,getString(R.string.login_error),Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showRegisterError() {

    }



    @Override
    public void initViews(View view) {
        editUserName = view.findViewById(R.id.edit_username);
        editPassword = view.findViewById(R.id.edit_password);
        btnLogin = view.findViewById(R.id.btn_login);
        linkSignUp = view.findViewById(R.id.text_link_signup);
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void saveUsername2Preference(LoginDetailData loginDetailData) {
        String username = loginDetailData.getUsername();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        sp.edit().putBoolean(SettingsUtil.ISLOGIN, true).apply();
        sp.edit().putString(SettingsUtil.USERNAME, username).apply();
        navigateToMain();
    }
    private void navigateToMain() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }
}
