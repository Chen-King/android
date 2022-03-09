package com.example.psycounselplatform.util;

import android.util.Log;

public class LogUtil
{
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;
    public static int level = VERBOSE;

    public static void v(String tag, String msg)
    {
        if(level <= VERBOSE)
        {
            Log.v(tag,msg);
        }
    }

    public static void d(String tag, String msg)
    {
        if(level <= DEBUG)
        {
            Log.d(tag,msg);
        }
    }

    public static void i(String tag, String msg)
    {
        if(level <= INFO)
        {
            Log.i(tag,msg);
        }
    }

    public static void w(String tag, String msg)
    {
        if(level <= WARN)
        {
            Log.w(tag,msg);
        }
    }

    public static void e(String tag, String msg)
    {
        if(level <= ERROR)
        {
            Log.e(tag, msg);
//            int maxLength = 1024;
//            while(msg.length() > 0){
//                String logContent = msg.substring(0, Math.min(msg.length(), maxLength));
//                Log.e(tag,logContent);
//                msg = msg.replace(logContent,"");
//            }
        }
    }

    public static void complete_e(String tag, String msg){
        int maxLength = 1024 * 3;
        if(level <= ERROR)
        {
            while(msg.length() > 0){
                String logContent = msg.substring(0,Math.min(msg.length(), maxLength));
                Log.e(tag,logContent);
                msg = msg.replace(logContent,"");
            }
            Log.e(tag,msg);
        }
    }
}
