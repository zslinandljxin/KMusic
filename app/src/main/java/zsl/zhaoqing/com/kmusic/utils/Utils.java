package zsl.zhaoqing.com.kmusic.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import zsl.zhaoqing.com.kmusic.R;
import zsl.zhaoqing.com.kmusic.constants.Contants;
import zsl.zhaoqing.com.kmusic.interfaces.HttpCallback;
import zsl.zhaoqing.com.kmusic.model.MusicInfo;

/**
 * Created by Administrator on 2016/1/14.
 */
public class Utils {

    /**
     * 判断网络是否可用
     *
     * @return
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) MyApplication.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        } else {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info.getState() == NetworkInfo.State.CONNECTED)
                return true;
        }
        return false;
    }

    /**
     * 判断WiFi是否可用
     *
     * @return
     */
    public static boolean isWifiAvailabe() {
        ConnectivityManager manager = (ConnectivityManager) MyApplication.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null) {
                if (info.getType() == ConnectivityManager.TYPE_WIFI &&
                        info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }

        }
        return false;
    }

    public static void sendHttpRequestForData(String songName, final HttpCallback callback) {
        final String httpUrl = Contants.HTTP_URL + "?s=" + songName;
        if (!isNetworkAvailable()) {
            Toast.makeText(MyApplication.getContext(),MyApplication.getContext().getResources().
                    getString(R.string.no_network), Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(httpUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.setRequestProperty("apikey", Contants.MUSIC_KEY);
                    connection.connect();
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    StringBuffer response = new StringBuffer();
                    String s = null;
                    while ((s = reader.readLine()) != null) {
                        response.append(s);
                    }
                    if (reader != null)
                        reader.close();
                    if (callback != null) {
                        callback.onSuccess(response.toString());
                    }
                } catch (Exception e) {
                    if (callback != null) {
                        callback.onFailure(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static List<MusicInfo> handlerFromResponse(String response) throws JSONException{
        List<MusicInfo> list = null;
        if (TextUtils.isEmpty(response)) {
            return null;
        }
            JSONObject object = new JSONObject(response);
            if (object.get("status").equals("failed")) {
                throw new JSONException(MyApplication.getContext().getResources().getString(
                        R.string.server_failed));
            }
            JSONObject data = object.getJSONObject("data");
            JSONObject data2 = data.getJSONObject("data");
            JSONArray array = data2.getJSONArray("list");
            list = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject coach = array.getJSONObject(i);
                MusicInfo info = new MusicInfo();
                info.setAlbumName(coach.getString("albumName"));
                info.setAlbumPic(coach.getString("albumPic"));
                info.setSongId(coach.getInt("songId"));
                info.setSongName(coach.getString("songName"));
                info.setSongUrl(coach.getString("songUrl"));
                info.setSingerName(coach.getString("userName"));
                list.add(info);
            }
        return list;
    }

    public static Bitmap imageHandler(Context context, Integer resId, String picPath, int reqWidth, int reqHeight){
        int inSampleSize = 1;
        int flag = 0;
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        if (resId != null && picPath == null){
            BitmapFactory.decodeResource(context.getResources(),resId,options);
        }else if (resId == null && picPath != null){
            BitmapFactory.decodeFile(picPath,options);
            flag = 1;
        }else {
            return null;
        }
        int width = options.outWidth;
        int height = options.outHeight;
        if (width > reqWidth || height > reqHeight){
            int widthRatio = Math.round((float)width / reqHeight);
            int heightRatio = Math.round((float)height / reqHeight);
            inSampleSize = widthRatio > heightRatio ? widthRatio : heightRatio;
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        if (flag == 1){
            bitmap = BitmapFactory.decodeFile(picPath,options);;
        }else {
            bitmap = BitmapFactory.decodeResource(context.getResources(),resId,options);
        }
        return bitmap;
    }
}
