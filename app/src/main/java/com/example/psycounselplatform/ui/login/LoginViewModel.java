package com.example.psycounselplatform.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

//import com.example.psycounselplatform.data.LoginRepository;
import com.example.psycounselplatform.data.Result;
import com.example.psycounselplatform.data.model.Customer;
import com.example.psycounselplatform.data.model.LoggedInUser;
import com.example.psycounselplatform.R;
import com.example.psycounselplatform.util.GenerateTestUserSig;
import com.example.psycounselplatform.util.HttpUtil;
import com.example.psycounselplatform.util.LogUtil;
import com.example.psycounselplatform.util.MyApplication;
import com.example.psycounselplatform.util.ResponseUtil;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private boolean registered = true;

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public MutableLiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        String address = HttpUtil.LocalAddress + "/user/login";

        HttpUtil.postLoginRequest(address, username, password, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                loginResult.postValue(new LoginResult("登录失败，请检查网络"));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException{
                final String responseData = response.body().string();

                if(ResponseUtil.checkString(responseData, "code").equals("200")){
                    V2TIMManager.getInstance().login(username, GenerateTestUserSig.genTestUserSig(username), new V2TIMCallback() {
                        @Override
                        public void onSuccess() {
                            LogUtil.e("LoginViewModel", responseData);
                            LoggedInUser loggedInUser = ResponseUtil.handleUser(responseData);

                            if(loggedInUser.getRole().equals("ROLE_CUSTOMER")){
                                String customerInfoAddress = HttpUtil.LocalAddress + "/customer/info";
                                HttpUtil.getCustomerInfo(customerInfoAddress, loggedInUser, new Callback() {
                                    @Override
                                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                        loginResult.postValue(new LoginResult("获取个人信息失败，请检查网络"));
                                    }

                                    @Override
                                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                        final String customerInfoResponseData = response.body().string();
                                        LogUtil.e("LoginViewModel", customerInfoResponseData);
                                        if (ResponseUtil.checkString(customerInfoResponseData, "code").equals("200")){
                                            Customer customer = ResponseUtil.handleCustomer(customerInfoResponseData);
                                            MyApplication.setUser(customer);
                                            loggedInUser.setRegistered(customer.getName() != null && customer.getName().length() > 0);
                                            loginResult.postValue(new LoginResult(loggedInUser));
                                        }else{
                                            loginResult.postValue(new LoginResult(ResponseUtil.checkString(responseData, "message")));
                                        }

                                    }
                                });

                            }else{
                                loginResult.postValue(new LoginResult("该类型用户请使用PC端登录"));
                            }

//                result = dataSource.login(username, password);
                        }

                        @Override
                        public void onError(int code, String desc) {
                            loginResult.postValue(new LoginResult(R.string.login_failed));
                        }
                    });
                }else{
                    registered = false;
                    loginResult.postValue(new LoginResult(ResponseUtil.checkString(responseData, "message")));
                    String registerAddress = HttpUtil.LocalAddress + "/customer/register";

                    HttpUtil.postRegisterRequest(registerAddress, username, password, new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            loginResult.postValue(new LoginResult("注册失败，请检查网络"));
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            final String responseData = response.body().string();
                            if (ResponseUtil.checkString(responseData, "code").equals("200")){
                                login(username, password);
                            }else{
                                loginResult.postValue(new LoginResult(ResponseUtil.checkString(responseData, "message")));
                            }

//                            HttpUtil.postLoginRequest(address, username, password, new Callback() {
//                                @Override
//                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                                    loginResult.postValue(new LoginResult("登录失败，请检查网络"));
//                                }
//
//                                @Override
//                                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                                    final String responseData = response.body().string();
//
//                                    V2TIMManager.getInstance().login(username, GenerateTestUserSig.genTestUserSig(username), new V2TIMCallback() {
//                                        @Override
//                                        public void onSuccess() {
//                                            LoggedInUser user = ResponseUtil.handleUser(responseData);
//                                            loginResult.postValue(new LoginResult(new LoggedInUser(user.getID(), user.getRole(),false)));
////                result = dataSource.login(username, password);
//                                        }
//
//                                        @Override
//                                        public void onError(int code, String desc) {
//                                            loginResult.postValue(new LoginResult(R.string.login_failed));
//                                        }
//                                    });
//                                }
//                            });
                        }
                    });
                }
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
//        if (username == null) {
//            return false;
//        }
//        if (username.contains("@")) {
//            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
//        } else {
////            return !username.trim().isEmpty();
//            return false;
//        }
        String pattern = "^[a-zA-Z0-9_]{6,16}$";
        return username != null && Pattern.matches(pattern, username);
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        String pattern = "^[a-zA-Z0-9_\\.,\\?!@#$%^&\\*\\(\\)]{6,16}$";
        return password != null && Pattern.matches(pattern, password);
    }
}