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
import android.widget.Toast;

import com.example.lengary_l.wanandroid.MainActivity;
import com.example.lengary_l.wanandroid.R;
import com.example.lengary_l.wanandroid.data.LoginDetailData;
import com.example.lengary_l.wanandroid.data.LoginType;
import com.example.lengary_l.wanandroid.util.SettingsUtil;
import com.example.lengary_l.wanandroid.util.StringUtil;

/**
 * Created by CoderLengary
 */


public class SignUpFragment extends Fragment implements LoginContract.View {

    private TextInputEditText editUserName;
    private TextInputEditText editPassword;
    private TextInputEditText editRePassword;
    private AppCompatButton btnSignUp;
    private TextView linkLogin;
    private LoginContract.Presenter presenter;


    public SignUpFragment() {

    }

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        initViews(view);

        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity loginActivity = (LoginActivity) getActivity();
                loginActivity.showLoginFragment();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editUserName.getText().toString();
                String password = editPassword.getText().toString();
                String rePassword = editRePassword.getText().toString();
                if (checkValid(username,password,rePassword)){
                    //register
                    presenter.login(username,password,LoginType.TYPE_REGISTER);
                }
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unSubscribe();
    }

    private boolean checkValid(String username,String password,String rePassword){
        boolean isValid = false;
        if (StringUtil.isInvalid(username)|| StringUtil.isInvalid(password)|| StringUtil.isInvalid(rePassword)){
            Snackbar.make(linkLogin,getString(R.string.input_error),Snackbar.LENGTH_SHORT).show();
        }else if (!password.equals(rePassword)){
            Snackbar.make(linkLogin,getString(R.string.repassword_not_match),Snackbar.LENGTH_SHORT).show();
        }else {
            isValid = true;
        }
        return isValid;

    }

    @Override
    public void showLoginError( String errorMsg) {
       Snackbar.make(linkLogin,errorMsg,Snackbar.LENGTH_SHORT).show();
    }



    @Override
    public void saveUser2Preference(LoginDetailData loginDetailData) {
        int userId = loginDetailData.getId();
        String username = loginDetailData.getUsername();
        String password = loginDetailData.getPassword();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        presenter.clearReadLaterData();
        sp.edit().putInt(SettingsUtil.USERID, userId).apply();
        sp.edit().putString(SettingsUtil.USERNAME, username).apply();
        sp.edit().putString(SettingsUtil.PASSWORD, password).apply();
        sp.edit().putBoolean(SettingsUtil.KEY_SKIP_LOGIN_PAGE,true).apply();
        navigateToMain();
    }

    @Override
    public void showNetworkError() {
        Toast.makeText(getContext(),getString(R.string.network_error),Toast.LENGTH_LONG).show();
    }

    @Override
    public void initViews(View view) {
        editUserName = view.findViewById(R.id.edit_username);
        editPassword = view.findViewById(R.id.edit_password);
        editRePassword = view.findViewById(R.id.edit_re_password);
        btnSignUp = view.findViewById(R.id.btn_sign_up);
        linkLogin = view.findViewById(R.id.text_link_login);
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private void navigateToMain() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

    @Override
    public boolean isActive() {
        return isAdded() && isResumed();
    }
}
