package com.example.psycounselplatform.util;

import android.app.Application;
import android.content.Context;

import com.example.psycounselplatform.data.model.Customer;
import com.example.psycounselplatform.data.model.LoggedInUser;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMSDKConfig;
import com.tencent.imsdk.v2.V2TIMSDKListener;

public class MyApplication extends Application {
    private static Context context;
    private static Customer user;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

    public static void setUser(Customer user) {
        MyApplication.user = user;
    }

    public static Customer getUser() {
        return user;
    }
}
