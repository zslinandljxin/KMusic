package zsl.zhaoqing.com.kmusic.constants;

/**
 * Created by Administrator on 2016/1/13.albumName
 */
public class Contants {

    /**
     * 音乐接口
     */
    public final static String HTTP_URL = "http://apis.baidu.com/geekery/music/query";
    public final static String MUSIC_KEY = "a04c18ba00cbe10a85869af29cb0fabd";

    public final static int MUSIC_FROM_NET = -1;
    public final static String DOWN_TASK = "downTask";


    /**
     * 数据库相关信息
     */
    public final static String MUSIC_DATABASE = "music_database.db3"; //数据库名
    public final static int VERSION_DATABASE = 1;                     //数据库版本
    public final static String MUSIC_INFO = "music_info";             //音乐信息表
    public final static String ID = "_id";                            //行ID
    public final static String ALBUM_NAME = "albumName";       //专辑名称
    public final static String ALBUM_PIC_LOCAL = "albumPicLocal";        //本地图片地址
    public final static String ALBUM_PIC = "albumPic";          //图片地址
    public final static String SONG_ID = "songId";            //歌曲ID
    public final static String SONG_NAME = "songName";        // 歌曲名称
    public final static String SONG_URL = "songUrl";          //歌曲地址
    public final static String SINGER_NAME = "singerName";    //歌手名称
    public final static String MUSIC_LOVE = "musicLove";     //喜欢的歌曲
    public final static int LOVE = 1;
    public final static int UNLOVE = 0;
    public final static String CREATE__MUSIC_INFO = "create table " + MUSIC_INFO + "("  +
            ID + " integer primary key autoincrement, " +
            ALBUM_NAME + " text, " +
            ALBUM_PIC_LOCAL + " text, " +
            ALBUM_PIC + " text, " +
            SONG_ID + " integer, " +
            SONG_NAME + " text, " +
            SONG_URL + " text, " +
            SINGER_NAME + " text, " +
            MUSIC_LOVE + " boolean)";
}

