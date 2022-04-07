//package com.example.psycounselplatform.data;
//
//import com.example.psycounselplatform.data.model.LoggedInUser;
//import com.example.psycounselplatform.util.GenerateTestUserSig;
//import com.tencent.imsdk.v2.V2TIMCallback;
//import com.tencent.imsdk.v2.V2TIMManager;
//
///**
// * Class that requests authentication and user information from the remote data source and
// * maintains an in-memory cache of login status and user credentials information.
// */
//public class LoginRepository {
//
//    private static volatile LoginRepository instance;
//
//    private LoginDataSource dataSource;
//
//    // If user credentials will be cached in local storage, it is recommended it be encrypted
//    // @see https://developer.android.com/training/articles/keystore
//    private LoggedInUser user = null;
//
//    // private constructor : singleton access
//    private LoginRepository(LoginDataSource dataSource) {
//        this.dataSource = dataSource;
//    }
//
//    public static LoginRepository getInstance(LoginDataSource dataSource) {
//        if (instance == null) {
//            instance = new LoginRepository(dataSource);
//        }
//        return instance;
//    }
//
//    public boolean isLoggedIn() {
//        return user != null;
//    }
//
//    public void logout() {
//        user = null;
//        dataSource.logout();
//    }
//
//    private void setLoggedInUser(LoggedInUser user) {
//        this.user = user;
//        // If user credentials will be cached in local storage, it is recommended it be encrypted
//        // @see https://developer.android.com/training/articles/keystore
//    }
//
//    public Result<LoggedInUser> login(String username, String password) {
//        // handle login
//        Result<LoggedInUser> result = dataSource.login(username, password);
//        V2TIMManager.getInstance().login(username, GenerateTestUserSig.genTestUserSig(username), new V2TIMCallback() {
//            @Override
//            public void onSuccess() {
////                result = dataSource.login(username, password);
//            }
//
//            @Override
//            public void onError(int code, String desc) {
//
//            }
//        });
//        if (result instanceof Result.Success) {
//            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
//        }
//        return result;
//    }
//}