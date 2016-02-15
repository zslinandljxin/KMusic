package zsl.zhaoqing.com.kmusic.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import zsl.zhaoqing.com.kmusic.constants.Contants;
import zsl.zhaoqing.com.kmusic.utils.MyApplication;
import zsl.zhaoqing.com.kmusic.model.MusicInfo;

/**
 * Created by Administrator on 2016/1/14.
 */
public class MusicInfoDb {

    private static MusicInfoDb musicInfoDb;
    private SQLiteDatabase db;

    private MusicInfoDb() {
        DatabaseHelper helper = new DatabaseHelper(MyApplication.getContext(), Contants.MUSIC_DATABASE,
                Contants.VERSION_DATABASE);
        db = helper.getReadableDatabase();
    }

    public synchronized static MusicInfoDb getInastance(){
        if (musicInfoDb == null){
            musicInfoDb = new MusicInfoDb();
        }
        return musicInfoDb;
    }

    public void saveMusicInfo(MusicInfo musicInfo){
        if (musicInfo != null){
            ContentValues values = new ContentValues();
            values.put(Contants.ALBUM_NAME,musicInfo.getAlbumName());
            values.put(Contants.ALBUM_PIC,musicInfo.getAlbumPic());
            values.put(Contants.ALBUM_PIC_LOCAL,musicInfo.getAlbumPicLocal());
            values.put(Contants.SONG_ID,musicInfo.getSongId());
            values.put(Contants.SONG_URL,musicInfo.getSongUrl());
            values.put(Contants.SONG_NAME,musicInfo.getSongName());
            values.put(Contants.SINGER_NAME,musicInfo.getSingerName());
            values.put(Contants.MUSIC_LOVE,musicInfo.getMusicLove());
            db.insert(Contants.MUSIC_INFO,null,values);
        }

    }

    public List<MusicInfo> queryMusicInfos(){
        List<MusicInfo> musicInfos = new ArrayList<>();
        Cursor cursor = db.query(Contants.MUSIC_INFO,null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                int id = cursor.getInt(cursor.getColumnIndex(Contants.ID));
                String albumName = cursor.getString(cursor.getColumnIndex(Contants.ALBUM_NAME));
                String albumPic = cursor.getString(cursor.getColumnIndex(Contants.ALBUM_PIC));
                String albumPicLocal = cursor.getString(cursor.getColumnIndex(Contants.ALBUM_PIC_LOCAL));
                int songId = cursor.getInt(cursor.getColumnIndex(Contants.SONG_ID));
                String songUrl = cursor.getString(cursor.getColumnIndex(Contants.SONG_URL));
                String songName = cursor.getString(cursor.getColumnIndex(Contants.SONG_NAME));
                String singerName = cursor.getString(cursor.getColumnIndex(Contants.SINGER_NAME));
                int musicLove = cursor.getInt(cursor.getColumnIndex(Contants.MUSIC_LOVE));
                MusicInfo info = new MusicInfo(id, songId,albumName,albumPic,albumPicLocal,songName,songUrl,singerName,musicLove);
                musicInfos.add(info);
            }
            while (cursor.moveToNext());
        }
        if (cursor != null){
            cursor.close();
        }
        return musicInfos;
    }

    public void deleteMusicInfo(int id){
        Log.d("deleteid","-------->"+id);
        db.delete(Contants.MUSIC_INFO,"_id = ?",new String[]{String.valueOf(id)});
    }
}
