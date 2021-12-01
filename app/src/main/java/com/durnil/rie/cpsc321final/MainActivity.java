package com.durnil.rie.cpsc321final;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Handler handler = null;
    int seconds = 0;

    Button startButton;
    Button pauseButton;
    Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                updateSeconds(seconds + 1); //Updating timer text
                handler.postDelayed(this, 1000); //Running runnable every second
            }
        };

        //Start Button:
        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (handler == null) {
                    handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(runnable, 1000);
                    startButton.setEnabled(false);
                    pauseButton.setEnabled(true);
                }
            }
        });

        //Pause Button:
        pauseButton = findViewById(R.id.pauseButton);
        pauseButton.setEnabled(false);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer(runnable);
            }
        });

        //Reset Button:
        resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSeconds(0); //Set seconds to 0
                stopTimer(runnable); //Stopping the timer
            }
        });
    }

    /**
     * Function to update the seconds text:
     */
    public void updateSeconds (int newSeconds) {
        seconds = newSeconds; //Updating seconds
        TextView tv = findViewById(R.id.secondsTextView);
        tv.setText("" + seconds);
    }

    /**
     * Function to stop the timer:
     */
    public void stopTimer (Runnable runnableParam) {
        if (handler != null) {
            handler.removeCallbacks(runnableParam);
            handler = null;
            pauseButton.setEnabled(false); //Toggling the pause button
            startButton.setEnabled(true); //Toggling the start button
        }
    }

    public class BackgroundService extends Service {
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            return super.onStartCommand(intent, flags, startId);
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onCreate() {
            super.onCreate();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }
    }
}