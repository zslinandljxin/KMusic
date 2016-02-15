package zsl.zhaoqing.com.kmusic.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/2/12.
 */
public class MusicDownload extends AsyncTask<Void,Integer,Void> {

    private Context context;
    private String songUrl;
    private String songName;
    private long songSize;
    private long hasRead;
    private long offset;
    private ProgressBar progressBar;

    public MusicDownload(Context context,String songUrl,String songName,long offset){
        this.context = context;
        this.songUrl = songUrl;
        this.songName = songName;
        this.offset = offset;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.d("downloadMusic", "--------->");
        InputStream in = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(songUrl);
            File file1 = new File(songUrl);
            songSize = file1.length();
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            Log.d("downloadMusic", "---------> connect");
            in = connection.getInputStream();
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MUSIC),songName);
            Log.d("downloadMusic", "--------->" + file.getAbsolutePath());
            OutputStream os = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];
            hasRead = offset;
            long actualCount;
            Log.d("downloadMusic", "---------->");
            while (offset > 0) {
                actualCount = in.skip(offset);
                offset = actualCount == -1 ? 0 : actualCount;
            }
            long readCount;
            while ((readCount = in.read(buffer)) != -1) {
                os.write(buffer);
                hasRead += readCount;
                publishProgress(((int)(hasRead * 1.0f/songSize) * 100));
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (progressBar != null){
            progressBar.setProgress(values[0]);
        }
    }

    public void setProgressBar(ProgressBar progressBar){
        if (this.progressBar == null)
            this.progressBar = progressBar;
    }

    public long getHasRead(){
        return hasRead;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public String getSongName() {
        return songName;
    }
}
