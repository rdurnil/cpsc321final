package com.durnil.rie.cpsc321final;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BackgroundService extends Service {
    Runnable runnable;
    Handler handler = null;
    int seconds = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        seconds = intent.getIntExtra("seconds", 0);
        //This method should be starting the timer again and resuming it like normal
        // it isn't doing that because stopTimer() is being called everytime the app is closed.
        //What should be happening is onStartCommand() should "undo" this stopTimer() call
        // and resume the timer anyways. I haven't figured out how to do this yet.

        //This is started by calling startService() which is called in onStop()
        //Same functionality as start button:
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
