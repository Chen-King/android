package com.example.psycounselplatform.ui.login;

import static java.lang.Thread.sleep;

import android.app.Activity;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.psycounselplatform.R;
import com.example.psycounselplatform.data.model.LoggedInUser;
import com.example.psycounselplatform.ui.login.LoginViewModel;
//import com.example.psycounselplatform.ui.login.LoginViewModelFactory;
import com.example.psycounselplatform.databinding.ActivityLoginBinding;
import com.example.psycounselplatform.ui.main.MainActivity;
import com.example.psycounselplatform.ui.personal.PersonalActivity;
import com.example.psycounselplatform.util.LogUtil;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMSDKConfig;
import com.tencent.imsdk.v2.V2TIMSDKListener;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    private Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initIMSDK();

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
//        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
//                    LogUtil.e("LoginActivity", "display name:" + loginResult.getSuccess().getDisplayName());
                    updateUiWithUser(loginResult.getSuccess());
                    saveEmailAndPassword(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                    finish();
                }
//                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful

            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE && loginButton.isEnabled()) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            loginViewModel.login(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());
        });

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String email = pref.getString("Email", "");
        String password = pref.getString("Password", "");
        usernameEditText.setText(email);
        passwordEditText.setText(password);
    }

    private void initIMSDK() {
        V2TIMSDKConfig config = new V2TIMSDKConfig();
        config.setLogLevel(V2TIMSDKConfig.V2TIM_LOG_INFO);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在初始化IMSDK");
//        LogUtil.e("LoginActivity", "init sdk");
        progressDialog.show();
        V2TIMManager.getInstance().initSDK(this, 1400645946, config, new V2TIMSDKListener() {
            @Override
            public void onConnectSuccess() {
                super.onConnectSuccess();
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                    Toast.makeText(context, "初始化IMSDK完毕", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onConnectFailed(int code, String error) {
                super.onConnectFailed(code, error);
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                    Toast.makeText(context, "初始化IMSDK失败", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveEmailAndPassword(String email, String password) {
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("Email", email);
        editor.putString("Password", password);
        editor.apply();
    }

    private void updateUiWithUser(LoggedInUser model) {
//        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
//        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        if(!model.isRegistered()){
            Intent intent = new Intent(getApplicationContext(), PersonalActivity.class);

            startActivity(intent);
        }else{
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    private void showLoginFailed(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}