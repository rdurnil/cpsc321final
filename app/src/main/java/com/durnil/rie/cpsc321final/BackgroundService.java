package com.durnil.rie.cpsc321final;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

public class BackgroundService extends Service {
    public static final String TAG = "BackgroundService";
    Runnable runnable;
    Handler handler = null;
    int seconds = 0;
    int initialSeconds = 0;
    Intent intent;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent = intent; // attempt to get the intent to pass back values, not working
        initialSeconds = intent.getIntExtra("seconds", 0);
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
        // runs the timer, same as timer in MainActivity
        runnable = new Runnable() {
            @Override
            public void run() {
                seconds++; //Updating timer text
                handler.postDelayed(this, 1000); //Running runnable every second
            }
        };

        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(runnable, 1000);
        }
    }

    @Override
    public void onDestroy() {
        // ends the timer, needs to pass back the seconds somehow
        // TODO: figure out how to pass back the seconds
        handler.removeCallbacks(runnable);
        handler = null;
        intent.putExtra("secondsAfter", seconds + initialSeconds);
        Log.d(TAG, "stopService: secondsAfter = " + (seconds+initialSeconds));
        super.onDestroy();
    }
}
