package com.music;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bean.Music;
import com.service.AudioPlayer;
import com.service.OnPlayerEventListener;
import com.utils.SystemUtils;

import java.util.ArrayList;
import java.util.List;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener, OnPlayerEventListener, SeekBar.OnSeekBarChangeListener {
    private String url = "http://zhangmenshiting.qianqian.com/data2/music/ba88ac878c68c2891a9c117a30ec05b1/540320263/540320263.mp3?xcode=b8932ae4a0aa9d8b39ce60c9b3abe9ef";

    private TextView tv_current_time, tv_total_time;
    private SeekBar sb_progress;
    private ImageView iv_prev, iv_play, iv_next;
    private int mLastProgress;
    private boolean isDraggingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);


        tv_current_time = findViewById(R.id.tv_current_time);
        tv_total_time = findViewById(R.id.tv_total_time);

        sb_progress = findViewById(R.id.sb_progress);

        iv_prev = findViewById(R.id.iv_prev);
        iv_play = findViewById(R.id.iv_play);
        iv_next = findViewById(R.id.iv_next);


        if (AudioPlayer.getInstance().isPlaying()) {
            Log.e("onBufferingUpdate", "OnCreate->" + AudioPlayer.secondaryProgress);
            sb_progress.setSecondaryProgress(AudioPlayer.secondaryProgress);
        } else {
            Music music = new Music();
            music.setTitle("怎么遇见你");
            music.setArtist("庄心妍");
            music.setDuration(293000);
            music.setPath(url);
            List<Music> musicList = new ArrayList<>();
            musicList.add(music);
            AudioPlayer.getInstance().setMusicList(musicList);
        }
        onChangeImpl(AudioPlayer.getInstance().getPlayMusic());
        initListener();
    }

    private void initListener() {
        AudioPlayer.getInstance().addOnPlayEventListener(this);
        sb_progress.setOnSeekBarChangeListener(this);
        iv_prev.setOnClickListener(this);
        iv_play.setOnClickListener(this);
        iv_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_prev:
                prev();
                break;
            case R.id.iv_play:
                play();
                break;
            case R.id.iv_next:
                next();
                break;
        }
    }

    private void play() {
        AudioPlayer.getInstance().playPause();
    }

    private void next() {
        AudioPlayer.getInstance().next();
    }

    private void prev() {
        AudioPlayer.getInstance().prev();
    }

    private String formatTime(long time) {
        return SystemUtils.formatTime("mm:ss", time);
    }

    private void onChangeImpl(Music music) {
        if (music == null) {
            return;
        }
        sb_progress.setProgress((int) AudioPlayer.getInstance().getAudioPosition());
        sb_progress.setMax((int) music.getDuration());
        tv_current_time.setText(R.string.play_time_start);
        tv_total_time.setText(formatTime(music.getDuration()));
        if (AudioPlayer.getInstance().isPlaying() || AudioPlayer.getInstance().isPreparing()) {
            iv_play.setSelected(true);
        } else {
            iv_play.setSelected(false);
        }
    }


    @Override
    public void onChange(Music music) {
        Log.e("tag", "onChange->" + music.toString());
        onChangeImpl(music);

    }

    @Override
    public void onPlayerStart() {
        Log.e("tag", "onPlayerStart->");
        iv_play.setSelected(true);
    }

    @Override
    public void onPlayerPause() {
        Log.e("tag", "onPlayerPause->");
        iv_play.setSelected(false);
    }

    @Override
    public void onPublish(int progress) {
        Log.e("tag", "onPublish->" + progress + "   max-->" + sb_progress.getMax());
        if (!isDraggingProgress) {
            sb_progress.setProgress(progress);
        }
    }

    @Override
    public void onBufferingUpdate(int percent) {
        Log.e("onBufferingUpdate", "onBufferingUpdate->" + percent);
        sb_progress.setSecondaryProgress(sb_progress.getMax() * 100 / percent);
        AudioPlayer.secondaryProgress = percent;
        Log.e("onBufferingUpdate", "onBufferingUpdate->" + AudioPlayer.secondaryProgress);
    }


    //SeekBar
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == sb_progress) {
            if (Math.abs(progress - mLastProgress) >= DateUtils.SECOND_IN_MILLIS) {
                tv_current_time.setText(formatTime(progress));
                mLastProgress = progress;
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (seekBar == sb_progress) {
            isDraggingProgress = true;
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBar == sb_progress) {
            isDraggingProgress = false;
            if (AudioPlayer.getInstance().isPlaying() || AudioPlayer.getInstance().isPausing()) {
                int progress = seekBar.getProgress();
                AudioPlayer.getInstance().seekTo(progress);
            } else {
                seekBar.setProgress(0);
            }
        }
    }


}
