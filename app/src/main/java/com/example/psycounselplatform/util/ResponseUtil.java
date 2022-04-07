package com.example.psycounselplatform.util;

import com.example.psycounselplatform.data.model.Customer;
import com.example.psycounselplatform.data.model.LoggedInUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResponseUtil {
    public static final String ERROR_CODE = "-1";

    public static LoggedInUser handleUser(String responseData){
        try{
            JSONObject jsonObject = new JSONObject(responseData);
            JSONObject userJson = jsonObject.getJSONObject("data");
            return new Gson().fromJson(userJson.toString(), new TypeToken<LoggedInUser>(){}.getType());
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public static Customer handleCustomer(String responseData){
        try{
            JSONObject jsonObject = new JSONObject(responseData);
            JSONObject userJson = jsonObject.getJSONObject("data");
            return new Gson().fromJson(userJson.toString(), new TypeToken<Customer>(){}.getType());
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String checkString(String response, String string)
    {
        try {
            JSONObject dataObject = new JSONObject(response);
            return dataObject.getString(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ERROR_CODE;
    }
}
