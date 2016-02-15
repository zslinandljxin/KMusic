package zsl.zhaoqing.com.kmusic.interfaces;

import android.support.v7.widget.Toolbar;

import java.util.List;

import zsl.zhaoqing.com.kmusic.model.MusicInfo;
import zsl.zhaoqing.com.kmusic.utils.MusicDownload;

/**
 * Created by Administrator on 2016/1/20.
 */
public interface MusicCallback {

    void supportToolBar(Toolbar toolbar);
    void musicSelected(List<MusicInfo> musicInfoList, int position);
    void deleteMusinInfo(MusicInfo info);
    List<String> getPlayList();
    void notifySearchMusic(String query);
    void openDownFragment();

    void chooseMusicFromSearch(MusicInfo info);
    void searchFromServer(String query);

    void downloadMusic(MusicInfo info);
    void deleteDownMusic(MusicDownload download);

}
