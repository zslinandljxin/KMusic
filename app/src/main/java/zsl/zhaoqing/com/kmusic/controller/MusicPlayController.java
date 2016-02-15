package zsl.zhaoqing.com.kmusic.controller;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import zsl.zhaoqing.com.kmusic.R;
import zsl.zhaoqing.com.kmusic.constants.Contants;
import zsl.zhaoqing.com.kmusic.model.MusicInfo;

/**
 * Created by Administrator on 2016/1/16.
 */
public class MusicPlayController {

    private static MusicPlayController musicPlayController;
    private Context context;
    private MediaPlayer player;

    //播放列表
    private List<MusicInfo> playMLists = new ArrayList<>();
    //记录当前歌曲的播放进度
    private int progress;
    //记录当前歌曲的所有时间
    private int duration;
    //记录当前播放歌曲在播放列表中的位置
    private int currentPosition = Contants.MUSIC_FROM_NET;
    //暂停标志
    private boolean flagOfPause = false;
    //第一次启动controller标志
    private boolean isFirstStart = true;

    private MusicPlayController(Context context) {
        this.context = context;
        player = new MediaPlayer();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (!isFirstStart){
                    if (isLoop()){
                        return;
                    }
                    nextMusic();
                }
            }
        });
    }

    public synchronized static MusicPlayController getInstance(Context context){
        if (musicPlayController == null){
            musicPlayController = new MusicPlayController(context);
        }
        return musicPlayController;
    }

    public List<MusicInfo> getPlayMLists() {
        return playMLists;
    }

    public void updatePlayMLists(List<MusicInfo> musicInfoList) {
        playMLists.clear();
        playMLists.addAll(musicInfoList);
        currentPosition = 0;
        isFirstStart = true;
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    public int getCurrentMPosition() {
        if (currentPosition >= 0 ){
            return currentPosition;
        }
        return Contants.MUSIC_FROM_NET;
   }

    public MusicInfo getCurrentMusicInfo(){
        if (currentPosition < playMLists.size() && currentPosition >= 0){
            return playMLists.get(currentPosition);
        }else {
            return null;
        }
    }

    public void playMusic(List<MusicInfo> musicInfoList, int position){
        updatePlayMLists(musicInfoList);
        playMusic(playMLists.get(position),position,0);
    }

    public void playMusic(MusicInfo info,int position, int startProgress){
        if (isFirstStart){
            isFirstStart = false;
        }
        if (flagOfPause){
            flagOfPause = false;
            player.start();
            return;
        }
        if (info == null){
            Toast.makeText(context,context.getResources().getString(R.string.no_music),Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if (player.isPlaying()){
                player.stop();
                player.reset();
            }
            if (position >= 0){
                currentPosition = position;
            }
            String path = playMLists.get(currentPosition).getSongUrl();
            if (path.contains("http:")){
                player.setDataSource(context, Uri.parse(path));
                player.prepareAsync();
            }else {
                player.setDataSource(path);
                player.prepare();
            }
            player.seekTo(startProgress);
            player.start();
            duration = player.getDuration();
        } catch (Exception e) {
            Toast.makeText(context,context.getResources().getString(R.string.no_music),Toast.LENGTH_SHORT).show();
        }
    }

    public void pauseMusic(){
        if (player.isPlaying()){
            player.pause();
            flagOfPause = true;
        }
    }

    public MusicInfo nextMusic(){
        player.reset();
        if (playMLists != null || playMLists.size() != 0){
            if (currentPosition + 1 < playMLists.size()) {
                currentPosition = currentPosition + 1;
            }else {
                currentPosition = 0;
            }
            playMusic(getCurrentMusicInfo(),currentPosition,0);
            return getCurrentMusicInfo();
        }else {
            Toast.makeText(context,context.getResources().getString(R.string.play_list_tips),
                    Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public boolean isLoop(){
        return player.isLooping();
    }

    public void setLoop(boolean isLooping){
        player.setLooping(isLooping);
    }

    public int getProgress(){
        progress = (int) (player.getCurrentPosition() * 1000.0f / duration);
        return progress;
    }

    public void seekToProgress(int progress){
        player.seekTo(progress * duration / 1000);
    }

    public void addInfoToPlayList(MusicInfo info){
        if (currentPosition == playMLists.size() - 1){
            playMLists.add(currentPosition ,info);
        }else {
            playMLists.add(currentPosition + 1 ,info);
        }


    }

    public void releases(){
        if (player != null){
            player.stop();
            player.release();
            player = null;
            musicPlayController = null;
        }

    }
}

