package zsl.zhaoqing.com.kmusic.model;

import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/1/16.
 */
public class PlayBar {
    private ImageView songPic;
    private TextView songName;
    private TextView singerName;
    private MyButton playButton;
    private MyButton pauseButton;
    private MyButton nextButton;
    private MyButton playLoopButton;
    private SeekBar progressBar;

    public PlayBar(ImageView songPic, TextView songName, TextView singerName, MyButton playButton,
                   MyButton pauseButton, MyButton nextButton, MyButton playLoopButton,
                   SeekBar progressBar) {
        this.songPic = songPic;
        this.songName = songName;
        this.singerName = singerName;
        this.playButton = playButton;
        this.pauseButton = pauseButton;
        this.nextButton = nextButton;
        this.playLoopButton = playLoopButton;
        this.progressBar = progressBar;
    }

    public ImageView getSongPic() {
        return songPic;
    }

    public TextView getSongName() {
        return songName;
    }

    public MyButton getPauseButton() {
        return pauseButton;
    }

    public void setPauseButton(MyButton pauseButton) {
        this.pauseButton = pauseButton;
    }

    public TextView getSingerName() {
        return singerName;
    }

    public MyButton getPlayButton() {
        return playButton;
    }

    public MyButton getNextButton() {
        return nextButton;
    }

    public MyButton getPlayLoopButton() {
        return playLoopButton;
    }

    public SeekBar getProgressBar() {
        return progressBar;
    }

    public void setSongPic(ImageView songPic) {
        this.songPic = songPic;
    }

    public void setSongName(TextView songName) {
        this.songName = songName;
    }

    public void setSingerName(TextView singerName) {
        this.singerName = singerName;
    }

    public void setPlayButton(MyButton playButton) {
        this.playButton = playButton;
    }

    public void setNextButton(MyButton nextButton) {
        this.nextButton = nextButton;
    }

    public void SetPlayLoopButton(MyButton playListButton) {
        this.playLoopButton = playListButton;
    }

    public void setProgressBar(SeekBar progressBar) {
        this.progressBar = progressBar;
    }
}
