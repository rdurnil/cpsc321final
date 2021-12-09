package com.durnil.rie.cpsc321final;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;

public class BackgroundService extends Service {
    public static final String TAG = "BackgroundService";
    Runnable runnable;
    Handler handler = null;
    int seconds = 0;
    int initialSeconds = 0;
    private final IBinder binder = new LocalBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initialSeconds = intent.getIntExtra("seconds", 0);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // runs the timer, same as timer in MainActivity
        runnable = new Runnable() {
            @Override
            public void run() {
                seconds++; //Updating timer
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
        // ends the timer
        handler.removeCallbacks(runnable);
        handler = null;
        super.onDestroy();
    }

    public int getSeconds() {
        // returns the total seconds the timer should be set to
        return seconds + initialSeconds;
    }

    public class LocalBinder extends Binder {
        BackgroundService getService() {
            // Returns this instance of the Background service so that the Main Activity
            // can access it's public methods
            return BackgroundService.this;
        }
    }
}
