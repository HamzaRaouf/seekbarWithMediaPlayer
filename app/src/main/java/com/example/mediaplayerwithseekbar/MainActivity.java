package com.example.mediaplayerwithseekbar;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    CountDownTimer timer;
    MediaPlayer mPlayer;
    TextView elapsedTimeLabel,remainingTimeLabel;
    ImageView playbtn;
    SeekBar seekBar;
    int totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar=findViewById(R.id.seekbar);
        elapsedTimeLabel=findViewById(R.id.elaspsed_time);
        remainingTimeLabel=findViewById(R.id.remaining_time);

        mPlayer=MediaPlayer.create(this, R.raw.tone);
        playbtn=findViewById(R.id.textView);
        mPlayer.setLooping(true);
        mPlayer.seekTo(0);
        totalTime=mPlayer.getDuration();
        seekBar.setMax(totalTime);
        remainingTimeLabel.setText(""+mPlayer.getDuration());
        playbtn.setBackgroundResource(R.drawable.ic_play);




        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(!mPlayer.isPlaying())
             {
                 mPlayer.start();
                 playbtn.setBackgroundResource(R.drawable.ic_pause);
             }
             else
             {
                 mPlayer.pause();
                 playbtn.setBackgroundResource(R.drawable.ic_play);


             }
            }
        });

//        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//
//            }
//        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUserInput) {
                if(mPlayer!=null && fromUserInput)
                {
                    mPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
//                    System.out.println("----seekbr postion:"+i);
//                    mPlayer.seekTo(i*1000);

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // Thread (Update positionBar & timeLabel)
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mPlayer != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mPlayer.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {}
                }
            }
        }).start();

//        mPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
//            @Override
//            public void onSeekComplete(MediaPlayer mediaPlayer) {
//
//            }
//        });

    }
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int currentPosition = msg.what;
            // Update positionBar.
            seekBar.setProgress(currentPosition);

            // Update Labels.
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = "- " + createTimeLabel(totalTime - currentPosition);
            remainingTimeLabel.setText(remainingTime);

            return true;
        }
    });

    public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }
}