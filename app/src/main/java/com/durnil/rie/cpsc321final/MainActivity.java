package com.durnil.rie.cpsc321final;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Handler handler = null;
    int seconds = 0;

    Button startButton;
    Button pauseButton;
    Button resetButton;
    Button runServiceButton;
    Runnable runnable;

    boolean serviceRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         runnable = new Runnable() {
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

        //Run Service Button:
        serviceRunning = false; //Set to false to start
        runServiceButton = findViewById(R.id.runServiceButton);
        runServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (serviceRunning) { //If the service is already running
                    serviceRunning = false;
                    Toast.makeText(MainActivity.this, "Service Ended, timer will stop on close", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Service Started, timer will continue on close", Toast.LENGTH_SHORT).show();
                    serviceRunning = true;
                }
                Toast.makeText(MainActivity.this, "runServiceButton pressed", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onStop() {
        super.onStop();
        stopTimer(runnable); //Stopping the timer everytime the app is closed
        //If the user pressed the runServiceButton the timer should continue even if it was stopped
        if (serviceRunning) { //***THIS DOESN'T WORK YET
            Intent intent = new Intent(MainActivity.this, BackgroundService.class);
            startService(intent);
        }
    }

    public class BackgroundService extends Service {
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {

            //This method should be starting the timer again and resuming it like normal
            // it isn't doing that because stopTimer() is being called everytime the app is closed.
            //What should be happening is onStartCommand() should "undo" this stopTimer() call
            // and resume the timer anyways. I haven't figured out how to do this yet.

            //This is started by calling startService() which is called in onStop()
            //Same functionality as start button:
            if (handler == null) {
                handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(runnable, 1000);
                startButton.setEnabled(false);
                pauseButton.setEnabled(true);
            }

            Toast.makeText(MainActivity.this, "in onStartCommand", Toast.LENGTH_SHORT).show();

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