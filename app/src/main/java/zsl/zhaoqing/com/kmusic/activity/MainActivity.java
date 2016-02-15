package zsl.zhaoqing.com.kmusic.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.view.ViewHelper;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import zsl.zhaoqing.com.kmusic.R;
import zsl.zhaoqing.com.kmusic.constants.Contants;
import zsl.zhaoqing.com.kmusic.controller.MusicDownloadController;
import zsl.zhaoqing.com.kmusic.controller.MusicPlayController;
import zsl.zhaoqing.com.kmusic.database.MusicInfoDb;
import zsl.zhaoqing.com.kmusic.fragments.DownLoadListFragment;
import zsl.zhaoqing.com.kmusic.fragments.MusicListFragment;
import zsl.zhaoqing.com.kmusic.fragments.SearchListFragment;
import zsl.zhaoqing.com.kmusic.interfaces.HttpCallback;
import zsl.zhaoqing.com.kmusic.interfaces.MusicCallback;
import zsl.zhaoqing.com.kmusic.model.MusicInfo;
import zsl.zhaoqing.com.kmusic.model.MyButton;
import zsl.zhaoqing.com.kmusic.model.PlayBar;
import zsl.zhaoqing.com.kmusic.utils.ImageDownload;
import zsl.zhaoqing.com.kmusic.utils.MusicDownload;
import zsl.zhaoqing.com.kmusic.utils.MyLog;
import zsl.zhaoqing.com.kmusic.utils.Utils;

/**
 * Created by Administrator on 2016/1/3.
 */
public class MainActivity extends AppCompatActivity implements MusicCallback, View.OnClickListener{

    public static final String Tag = "MainActivity";
    public static final int PROGRESS_STATUS = 0x22;
    public static final int UPDATE_SEARCHFRAGMENT = 0x23;
    public static final int NO_NETWORK = 0x25;
    public static final int SERVER_FIXED = 0X24;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private LinearLayout contentArea;
    private PlayBar playBar;

    private MusicDownloadController downloadController;
    private MusicPlayController playController;
    private Timer timer;

    private int pressBackCount = 0;

    private android.support.v4.app.FragmentManager manager;
    private MusicListFragment musicListFragment;
    private SearchListFragment searchListFragment;
   private DownLoadListFragment downLoadListFragment;

    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            int progress =playController.getProgress();
            Message msg = new Message();
            msg.what = PROGRESS_STATUS;
            msg.obj = progress;
            handler.sendMessage(msg);
        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PROGRESS_STATUS:
                    playBar.getProgressBar().setProgress((Integer) msg.obj);
                    break;
                case UPDATE_SEARCHFRAGMENT:
                    searchListFragment.notifyUpdateListview((List<MusicInfo>) msg.obj);
                    break;
                case SERVER_FIXED:
                    Toast.makeText(MainActivity.this, (String) msg.obj,Toast.LENGTH_SHORT).show();
                    break;
                case NO_NETWORK:
                    Toast.makeText(MainActivity.this,getResources().getString(
                            R.string.no_network),Toast.LENGTH_SHORT).show();
                    break;
                default:break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyLog.d(Tag," onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        initWidget();
        initPlayBar();
        timer = new Timer();
        timer.schedule(task,1000,500);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        pressBackCount = 0;
    }

    @Override
    protected void onStart() {
        MyLog.d(Tag,"onStart");
        super.onStart();
        loadMusicListFromDB();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        MyLog.d(Tag,"onPostCreate");
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        MyLog.d(Tag,"onResume");
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        MyLog.d(Tag,"onPostResume");
        super.onPostResume();
    }

    @Override
    protected void onResumeFragments() {
        MyLog.d(Tag,"onResumeFragments");
        super.onResumeFragments();
    }

    @Override
    protected void onPause() {
        MyLog.d(Tag,"onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        MyLog.d(Tag,"nStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        MyLog.d(Tag,"onDestroy");
        timer.cancel();
        timer = null;
        playController.releases();
        downloadController.recorde();
        downloadController.release();
        super.onDestroy();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        MyLog.d(Tag,"onAttachFragment");
        super.onAttachFragment(fragment);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!drawerLayout.isDrawerOpen(Gravity.LEFT)){
            if (searchListFragment != null){
                if (searchListFragment.isVisible()){
                    searchListFragment.listener(ev);
                }
            }
            if (downLoadListFragment != null){
                if (downLoadListFragment.isVisible()){
                    downLoadListFragment.listener(ev);
                }
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        MyLog.d(Tag,"onbackpressed");
        if (searchListFragment != null){
            if (searchListFragment.isVisible()){
                super.onBackPressed();
                return;
            }
        }
        if (downLoadListFragment != null){
            if (downLoadListFragment.isVisible()){
                super.onBackPressed();
                return;
            }
        }
        pressBackCount ++;
        if (pressBackCount == 1){
            Toast.makeText(this,getResources().getString(R.string.go_home_tips),Toast.LENGTH_SHORT).show();
        }else if (pressBackCount == 2){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }

    @Override
    public void musicSelected(List<MusicInfo> musicInfoList,int position) {
        playController.playMusic(musicInfoList,position);
        setPlayBarStatus(musicInfoList.get(position));
    }

    @Override
    public List<String> getPlayList() {
        List<MusicInfo> list = playController.getPlayMLists();
        List<String> names = new ArrayList<>();
        for (MusicInfo info : list){
            names.add(info.getSongName());
        }
        return names;
    }

    @Override
    public void chooseMusicFromSearch(MusicInfo info) {
        playController.playMusic(info, Contants.MUSIC_FROM_NET, 0);
        setPlayBarStatus(info);
    }

    @Override
    public void notifySearchMusic(String query) {
        searchListFragment = new SearchListFragment();
        FragmentTransaction transaction =manager.beginTransaction();
        transaction.setCustomAnimations(0,0,R.anim.fragment_enter,R.anim.fragment_exit);
        transaction.replace(R.id.fragment_wrapper,searchListFragment)
                .addToBackStack(null)
                .commit();
        searchFromServer(query);
    }

    @Override
    public void openDownFragment() {
        downLoadListFragment= new DownLoadListFragment();
        Bundle arg = new Bundle();
        arg.putSerializable(Contants.DOWN_TASK, (Serializable) downloadController.getDownloads());
        downLoadListFragment.setArguments(arg);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(0,0,R.anim.fragment_enter,R.anim.fragment_exit);
        transaction.replace(R.id.fragment_wrapper,downLoadListFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void deleteMusinInfo(MusicInfo info) {
        if (info.getId() == playController.getCurrentMusicInfo().getId()){
            playController.getPlayer().stop();
            playController.getPlayer().reset();
            playBar.getPlayButton().setVisibility(View.VISIBLE);
            playBar.getPauseButton().setVisibility(View.GONE);
        }
        MusicInfoDb db = MusicInfoDb.getInastance();
        db.deleteMusicInfo(info.getId());
        loadMusicListFromDB();
    }

    @Override
    public void downloadMusic(MusicInfo info) {
        MusicDownload musicDownload = new MusicDownload(this,info.getSongUrl(),info.getSongName(),0);
        musicDownload.execute();
        downloadController.addInDownloadLists(musicDownload);
        Toast.makeText(this,getResources().getString(R.string.add_to_download),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteDownMusic(MusicDownload download) {
        downloadController.removeFromDownloadLists(download);
    }

    public void updateDownloadList() {
        downLoadListFragment.updateList();
    }

    @Override
    public void searchFromServer(String query) {
        Utils.sendHttpRequestForData(query, new HttpCallback() {
            @Override
            public void onSuccess(String result) {
                List<MusicInfo> list = null;
                try {
                    list = Utils.handlerFromResponse(result);
                } catch (JSONException e) {
                    Message msg = handler.obtainMessage(SERVER_FIXED,e.getMessage());
                    handler.sendMessage(msg);
                }
                Message msg = handler.obtainMessage(UPDATE_SEARCHFRAGMENT,list);
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Exception e) {
                handler.sendEmptyMessage(NO_NETWORK);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play_music_button:
                playController.playMusic(playController.getCurrentMusicInfo(),
                        playController.getCurrentMPosition(),0);
                setPlayBarStatus(playController.getCurrentMusicInfo());
                break;
            case R.id.pause_music_button:
                playController.pauseMusic();
                playBar.getPlayButton().setVisibility(View.VISIBLE);
                playBar.getPauseButton().setVisibility(View.GONE);
                break;
            case R.id.next_music_button:
                MusicInfo info = playController.nextMusic();
                setPlayBarStatus(info);
                break;
            case R.id.music_loop_button:
                if (playController.isLoop()){
                    playController.setLoop(false);
                    Toast.makeText(this,getResources().getString(R.string.cancel_recycle),Toast.LENGTH_SHORT).show();
                }else {
                    playController.setLoop(true);
                    Toast.makeText(this,getResources().getString(R.string.set_recycle),Toast.LENGTH_SHORT).show();
                }
                break;
            default:break;
        }
    }

    @Override
    public void supportToolBar(Toolbar toolbar) {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                drawerAnimation(drawerView, contentArea, slideOffset);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    /**
     * 从数据库加载音乐列表
     */
    public void loadMusicListFromDB(){
        MyLog.d(Tag,"---------> loadMusicListFromDB");
        MusicInfoDb db = MusicInfoDb.getInastance();
        List<MusicInfo> musicInfoList = db.queryMusicInfos();
        if (musicInfoList != null){
            musicListFragment.updateListview(musicInfoList);
            playController.updatePlayMLists(musicInfoList);
        }
    }

    private void initWidget() {
        MyLog.d(Tag,"---------> initWidget");
        manager = getSupportFragmentManager();
        musicListFragment = new MusicListFragment();
        manager.beginTransaction().add(R.id.fragment_wrapper,musicListFragment).commit();
        playController = MusicPlayController.getInstance(this);
        downloadController = MusicDownloadController.getInstance(this);
        contentArea = (LinearLayout) findViewById(R.id.content_area);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_container);
        drawerLayout.setScrimColor(getResources().getColor(R.color.colorObscure));

    }

    private void drawerAnimation(View drawerView,View contentView, float slideOffset) {
        float scaleLeft , scaleRight;
        scaleLeft = 0.8f + 0.2f * slideOffset;
        scaleRight = 1.0f - 0.1f * slideOffset;
        ViewHelper.setTranslationX(contentView, slideOffset * scaleRight * drawerView.getWidth());
        ViewHelper.setScaleY(drawerView,scaleLeft);
        ViewHelper.setScaleX(drawerView,scaleLeft);
        ViewHelper.setScaleY(contentView,scaleRight);
        ViewHelper.setScaleX(contentView,scaleRight);
    }

    private void initPlayBar(){
        ImageView songPic = (ImageView) findViewById(R.id.song_pic_view);
        TextView songName = (TextView) findViewById(R.id.song_name_view);
        TextView singerName = (TextView) findViewById(R.id.singer_name_view);
        MyButton playButton = (MyButton) findViewById(R.id.play_music_button);
        MyButton pauseButton = (MyButton) findViewById(R.id.pause_music_button);
        MyButton nextButton = (MyButton) findViewById(R.id.next_music_button);
        MyButton playLoopButton = (MyButton) findViewById(R.id.music_loop_button);
        SeekBar progressBar = (SeekBar) findViewById(R.id.song_seekBar);
        playButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        playLoopButton.setOnClickListener(this);
        progressBar.setMax(1000);
        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                playController.seekToProgress(progress);
            }
        });
        playBar = new PlayBar(songPic,songName,singerName,playButton,pauseButton,nextButton,
                playLoopButton,progressBar);
    }

    private void setPlayBarStatus(MusicInfo info) {
        if (info == null){
            return;
        }
        loadBitmap(info, playBar.getSongPic());
        playBar.getSongName().setText(info.getSongName());
        if (info.getSingerName() != null){
            playBar.getSingerName().setText(info.getSingerName());
        }
        playBar.getPlayButton().setVisibility(View.GONE);
        playBar.getPauseButton().setVisibility(View.VISIBLE);
    }

    private void loadBitmap(MusicInfo info, ImageView imageView) {
        String picUri = info.getAlbumPicLocal();
        if (TextUtils.isEmpty(picUri)){
            imageView.setImageResource(R.drawable.song_pic_default);
            if (!TextUtils.isEmpty(info.getAlbumPic())){
                ImageDownload imageDownload = new ImageDownload(imageView);
                imageDownload.execute(info.getAlbumPic());
            }
        }else {
            imageView.setImageBitmap(BitmapFactory.decodeFile(picUri));
        }
    }

    public void exit(View v){
        this.finish();
    }

}