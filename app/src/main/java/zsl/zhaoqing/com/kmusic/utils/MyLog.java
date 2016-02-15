package zsl.zhaoqing.com.kmusic.utils;

import android.util.Log;

import zsl.zhaoqing.com.kmusic.BuildConfig;

/**
 * Created by Administrator on 2016/2/5.
 */
public class MyLog {

    public static void d(String tag, String msg){
        if (BuildConfig.DEBUG == true){
            Log.d(tag,"--------------> " + msg);
        }
    }
}
