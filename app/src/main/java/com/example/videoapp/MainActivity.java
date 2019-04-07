package com.example.videoapp;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {

    private VideoView myVideoView;
    private Button btnPlayVideo;
    private MediaController mediaController;
    private Button btnPlayMusic, btnPauseMusic;
    private MediaPlayer mediaPlayer;
    private SeekBar mySeekBar;
    private AudioManager audioManager;
    private SeekBar moveSeekBar;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myVideoView = findViewById(R.id.myVideoView);
        btnPlayVideo = findViewById(R.id.btnPlayVideo);

        btnPlayMusic = findViewById(R.id.btnPlay);
        btnPauseMusic = findViewById(R.id.btnPause);
        btnPlayMusic.setOnClickListener(MainActivity.this);
        btnPauseMusic.setOnClickListener(MainActivity.this);

        btnPlayVideo.setOnClickListener(MainActivity.this);
        mySeekBar = findViewById(R.id.seekBarVolume);

        moveSeekBar = findViewById(R.id.seekBarMove);

        mediaController = new MediaController(MainActivity.this);
        mediaPlayer = MediaPlayer.create(this, R.raw.bensound_creativeminds);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        mySeekBar.setMax(maxVolume);
        mySeekBar.setProgress(currentVol);

        mySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                //    Toast.makeText(MainActivity.this, Integer.toString(progress)+"", Toast.LENGTH_SHORT).show();
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        moveSeekBar.setOnSeekBarChangeListener(this);
        moveSeekBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onClick(View buttonView) {


        switch (buttonView.getId()){

            case R.id.btnPlayVideo:
                Uri videoUri = Uri.parse("android.resource://" + getPackageName()+"/"+R.raw.video);

                myVideoView.setVideoURI(videoUri);
                myVideoView.setMediaController(mediaController);
                mediaController.setAnchorView(myVideoView);
                myVideoView.start();
                break;

            case R.id.btnPlay:

                mediaPlayer.start();

                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        moveSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                },0,1000);

                break;

            case R.id.btnPause:
                mediaPlayer.pause();
                timer.cancel();
                break;

        }


    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (fromUser){
           // Toast.makeText(this, Integer.toString(progress)+"", Toast.LENGTH_SHORT).show();

            mediaPlayer.seekTo(progress);
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        timer.cancel();
        Toast.makeText(this, "Musis is ended", Toast.LENGTH_SHORT).show();
    }
}
