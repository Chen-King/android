package com.example.psycounselplatform.util;

import com.example.psycounselplatform.data.model.LoggedInUser;
import com.google.gson.Gson;

import java.util.HashMap;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {
    public static final String LocalAddress = "http://101.43.247.72:8080";

    public static void postLoginRequest(String address, String username, String password, Callback callback){
        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HashMap<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String jsonStr = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(jsonStr, JSON);

        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void postRegisterRequest(String address, String username, String password, Callback callback){
        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HashMap<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String jsonStr = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(jsonStr, JSON);

        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void getCustomerInfo(String address, LoggedInUser loggedInUser, Callback callback){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(address + "/" + loggedInUser.getId())
                .addHeader("token", loggedInUser.getToken())
                .build();
        client.newCall(request).enqueue(callback);
    }
}
