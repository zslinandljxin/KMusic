package zsl.zhaoqing.com.kmusic.controller;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import zsl.zhaoqing.com.kmusic.activity.MainActivity;
import zsl.zhaoqing.com.kmusic.constants.Contants;
import zsl.zhaoqing.com.kmusic.utils.MusicDownload;

/**
 * Created by Administrator on 2016/2/12.
 */
public class MusicDownloadController {

    public final static String DOWNLOAD_CONFIG_FILE = "download_list";
    public final static String OFFSET = "offset";

    public static MusicDownloadController controller;
    private List<MusicDownload> downloads;
    private Context context;

    private MusicDownloadController(Context context){
        this.context = context;
        downloads = new ArrayList<>();
        init();
    }

    public static MusicDownloadController getInstance(Context context){
        if (controller == null){
            controller = new MusicDownloadController(context);
        }
        return controller;
    }

    public void init(){
        SharedPreferences pre = context.getSharedPreferences(DOWNLOAD_CONFIG_FILE,Context.MODE_PRIVATE);
        Map<String,String> map = (Map<String, String>) pre.getAll();
        if (!map.isEmpty()){
            for (int i = 0; i < map.size()/3; i++){
                String songUrl = map.get(Contants.SONG_URL + i);
                String songName = map.get(Contants.SONG_NAME + i);
                long offset = Long.getLong(map.get(OFFSET + i));
                MusicDownload download = new MusicDownload(context,songUrl,songName,offset);
                downloads.add(download);
            }
        }
    }

    public void recorde(){
        stop();
        SharedPreferences pre = context.getSharedPreferences(DOWNLOAD_CONFIG_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        if (downloads != null && downloads.size() > 0){
            for (int i = 0; i < downloads.size(); i++){
                editor.putString(Contants.SONG_URL + i,downloads.get(i).getSongUrl());
                editor.putString(Contants.SONG_NAME + i,downloads.get(i).getSongName());
                editor.putString(OFFSET + i,String.valueOf(downloads.get(i).getHasRead()));
            }
            editor.commit();
        }
    }

    public void addInDownloadLists(MusicDownload musicDownload){
        downloads.add(musicDownload);
    }

    public void removeFromDownloadLists(MusicDownload musicDownload){
        downloads.remove(musicDownload);
        ((MainActivity)context).updateDownloadList();
    }

    public List<MusicDownload> getDownloads() {
        return downloads;
    }

    private void stop(){
        if (downloads != null && downloads.size() > 0){
            for (MusicDownload mdb : downloads){
                mdb.cancel(true);
            }
        }
    }

    public void release(){
        controller = null;
    }
}
