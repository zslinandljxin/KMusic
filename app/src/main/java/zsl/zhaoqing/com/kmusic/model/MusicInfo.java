package zsl.zhaoqing.com.kmusic.model;

import zsl.zhaoqing.com.kmusic.constants.Contants;

/**
 * Created by Administrator on 2016/1/14.
 */
public class MusicInfo {

    private int id;
    private int songId;
    private String albumName;
    private String albumPic;
    private String albumPicLocal;
    private String songName;
    private String songUrl;
    private String singerName;
    private int musicLove = Contants.UNLOVE;

    public MusicInfo() {
        super();
    }


    public MusicInfo(int id, int songId, String albumName, String albumPic, String albumPicLocal, String songName,
                     String songUrl, String singerName, int musicLove) {
        this.id = id;
        this.songId = songId;
        this.albumName = albumName;
        this.albumPic = albumPic;
        this.albumPicLocal = albumPicLocal;
        this.songName = songName;
        this.songUrl = songUrl;
        this.singerName = singerName;
        this.musicLove = musicLove;
    }

    public MusicInfo(int songId, String albumName, String albumPic, String albumPicLocal, String songName, String songUrl,
                     String singerName, int musicLove) {
        this.songId = songId;
        this.albumName = albumName;
        this.albumPic = albumPic;
        this.albumPicLocal = albumPicLocal;
        this.songName = songName;
        this.songUrl = songUrl;
        this.singerName = singerName;
        this.musicLove = musicLove;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumPic() {
        return albumPic;
    }

    public void setAlbumPic(String albumPic) {
        this.albumPic = albumPic;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public int getMusicLove() {
        return musicLove;
    }

    public void setMusicLove(int musicLove) {
        this.musicLove = musicLove;
    }

    public String getAlbumPicLocal() {
        return albumPicLocal;
    }

    public void setAlbumPicLocal(String albumPicLocal) {
        this.albumPicLocal = albumPicLocal;
    }

}
