//package com.example.psycounselplatform.ui.login;
//
//import android.util.Patterns;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.ViewModel;
//
//import com.example.psycounselplatform.R;
//import com.example.psycounselplatform.data.LoginRepository;
//import com.example.psycounselplatform.data.Result;
//import com.example.psycounselplatform.data.model.LoggedInUser;
//
//public class LoginViewModelPre extends ViewModel {
//
//    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
//    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
//    private LoginRepository loginRepository;
//
//    LoginViewModelPre(LoginRepository loginRepository) {
//        this.loginRepository = loginRepository;
//    }
//
//    LiveData<LoginFormState> getLoginFormState() {
//        return loginFormState;
//    }
//
//    LiveData<LoginResult> getLoginResult() {
//        return loginResult;
//    }
//
//    public void login(String username, String password) {
//        // can be launched in a separate asynchronous job
//        Result<LoggedInUser> result = loginRepository.login(username, password);
//
//        if (result instanceof Result.Success) {
//            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
//            loginResult.setValue(new LoginResult(new LoggedInUser(data.getID(), data.getRole(), data.isRegistered())));
//        } else {
//            loginResult.setValue(new LoginResult(R.string.login_failed));
//        }
//    }
//
//    public void loginDataChanged(String username, String password) {
//        if (!isUserNameValid(username)) {
//            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
//        } else if (!isPasswordValid(password)) {
//            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
//        } else {
//            loginFormState.setValue(new LoginFormState(true));
//        }
//    }
//
//    // A placeholder username validation check
//    private boolean isUserNameValid(String username) {
//        if (username == null) {
//            return false;
//        }
//        if (username.contains("@")) {
//            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
//        } else {
////            return !username.trim().isEmpty();
//            return false;
//        }
//    }
//
//    // A placeholder password validation check
//    private boolean isPasswordValid(String password) {
//        return password != null && password.trim().length() > 5;
//    }
//}