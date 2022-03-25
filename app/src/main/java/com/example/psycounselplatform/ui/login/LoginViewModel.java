package com.example.psycounselplatform.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.example.psycounselplatform.data.LoginRepository;
import com.example.psycounselplatform.data.Result;
import com.example.psycounselplatform.data.model.LoggedInUser;
import com.example.psycounselplatform.R;
import com.example.psycounselplatform.util.GenerateTestUserSig;
import com.example.psycounselplatform.util.LogUtil;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public MutableLiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        V2TIMManager.getInstance().login(username, GenerateTestUserSig.genTestUserSig(username), new V2TIMCallback() {
            @Override
            public void onSuccess() {
                loginResult.setValue(new LoginResult(new LoggedInUserView("用户名", true)));
//                result = dataSource.login(username, password);
            }

            @Override
            public void onError(int code, String desc) {
                loginResult.setValue(new LoginResult(R.string.login_failed));
            }
        });
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
//            return !username.trim().isEmpty();
            return false;
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}