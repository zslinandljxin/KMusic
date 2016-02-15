package zsl.zhaoqing.com.kmusic.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import zsl.zhaoqing.com.kmusic.R;
import zsl.zhaoqing.com.kmusic.activity.MainActivity;
import zsl.zhaoqing.com.kmusic.database.MusicInfoDb;
import zsl.zhaoqing.com.kmusic.model.MusicInfo;

/**
 * Created by Administrator on 2016/1/15.
 */
public class MSearchController {

    private final static int UPDATE_VIEW = 0X13;

    private MusicInfoDb db;
    private List<String> musicPathLists;
    private List<String> musicNameLists;
    private Context context;
    private AlertDialog dialog;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_VIEW){
                //通知activity去更新fragment视图
                ((MainActivity)context).loadMusicListFromDB();
                if (dialog != null){
                    dialog.show();
                    dialog.setMessage("共搜索到" +  musicNameLists.size() + "首音乐！");
                }

            }
        }
    };

    public MSearchController(Context context) {
        db = MusicInfoDb.getInastance();
        musicPathLists = new ArrayList<>();
        musicNameLists = new ArrayList<>();
        this.context = context;
    }

    /**
     * 获取歌曲搜索结果
     * @return 歌曲路径的集合
     */
    public void getMusicSearchResult() {
        if (!isExternalStorageWrite()){
            //提示没有可用外设，并返回null
            Toast.makeText(context,context.getResources().getString(R.string.no_external_storage),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        showDialog();
        searchMusicFromLocal(new FinishMusicSearchCallback() {
            @Override
            public void finishSearch() {
                updateDataToDatabase(musicPathLists,musicNameLists);
                handler.sendEmptyMessage(UPDATE_VIEW);
            }
        });
    }

    private void searchMusicFromLocal(final FinishMusicSearchCallback callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final File sdCardDir = Environment.getExternalStorageDirectory();
                checkDirectorForMusic(sdCardDir);
                if (callback != null){
                    callback.finishSearch();
                }

            }
        }).start();
    }

    /**
     * 搜索file文件目录下的mp3文件
     * @param file 需要检查的文件
     */
    private void checkDirectorForMusic(File file) {
        File[] currentFiles = file.listFiles();
        for (int i = 0; i < currentFiles.length; i++){
            String path = currentFiles[i].getAbsolutePath();
            if (currentFiles[i].isDirectory()){
                checkDirectorForMusic(currentFiles[i]);
            }else if (currentFiles[i].isFile()){
                if (isMP3Music(currentFiles[i])){
                    musicNameLists.add(currentFiles[i].getName());
                    musicPathLists.add(path);
                }
            }
        }
    }

    /**
     * 判断外部储存是否可用
     * @return
     */
    public static boolean isExternalStorageWrite(){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)){
            return true;
        }
        return false;
    }

    /**
     * 判断文件是否为mp3文件
     * @param file
     * @return
     */
    public boolean isMP3Music(File file){
        String path = file.getAbsolutePath();
        if (path.endsWith(".mp3") || path.endsWith(".MP3")){
            return true;
        }
        return false;
    }

    /**
     * 更新数据库
     * @param musicPathLists  歌曲所在路径集合
     * @param musicNameLists  歌曲名集合
     */
    private void updateDataToDatabase(List<String> musicPathLists, List<String> musicNameLists){
        boolean flag;
        List<MusicInfo> musicInfos = db.queryMusicInfos();
        for (int i = 0; i < musicPathLists.size(); i++){
            flag = true;
            for (int j = 0; j < musicInfos.size(); j++){
                if (musicPathLists.get(i).equals(musicInfos.get(j).getSongUrl())){
                    flag = false;
                    break;
                }
            }
            if (flag){
                MusicInfo info = new MusicInfo();
                info.setSongName(musicNameLists.get(i));
                info.setSongUrl(musicPathLists.get(i));
                db.saveMusicInfo(info);
            }
        }
    }

    public void showDialog(){
        dialog = new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.search_music))
                .setMessage(context.getResources().getString(R.string.searching))
                .create();
        dialog.show();
    }

    private void dismissDialog(){
        dialog.dismiss();
    }

    interface FinishMusicSearchCallback {
        void finishSearch();
    }
}