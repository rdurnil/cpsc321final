package com.durnil.rie.cpsc321final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
    /* public static final String TAG = "MainActivity";
    Handler handler = null;
    int seconds = 0;

    Button startButton;
    Button pauseButton;
    Button resetButton;
    Button runServiceButton;
    Runnable runnable;
    Intent serviceIntent;

    boolean serviceRunning;
    boolean serviceBound;
    BackgroundService service; */

    static final int LOCATION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newWorkoutButton = findViewById(R.id.newWorkoutButton);
        Button prevWorkoutsButton = findViewById(R.id.prevWorkoutsButton);
        enableMyLocation();

        newWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewWorkoutActivity.class);
                startActivity(intent);
            }
        });
    }

    private void enableMyLocation() {
        // get the user's permission to access their fine location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
        } else {
            // need to request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }
    }

    /**
     * Gets the result of the location permission request.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            } else {
                Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
         /*runnable = new Runnable() {
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
        });*/
    }

    /**
     * Function to update the seconds text:
     */
   /* public void updateSeconds (int newSeconds) {
        seconds = newSeconds; //Updating seconds
        TextView tv = findViewById(R.id.secondsTextView);
        tv.setText("" + seconds);
    }*/

    /**
     * Function to stop the timer:
     */
    /*public void stopTimer (Runnable runnableParam) {
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
        if (serviceRunning) {
            serviceIntent = new Intent(MainActivity.this, BackgroundService.class);
            serviceIntent.putExtra("seconds", seconds);
            startService(serviceIntent);
            bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // stops the service from running
        // updates the seconds
        if (serviceRunning) {
            if (serviceIntent != null) {
                stopService(serviceIntent);
            }
            if (serviceBound) {
                // get the seconds to set the timer to
                seconds = service.getSeconds();
                updateSeconds(seconds);
                // restart the timer
                if (handler == null) {
                    handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(runnable, 1000);
                    startButton.setEnabled(false);
                    pauseButton.setEnabled(true);
                }
            }
            // unbind from the service
            unbindService(connection);
        }
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // Binding to the Background service and getting its instance
            // in order to use public methods
            BackgroundService.LocalBinder binder = (BackgroundService.LocalBinder) service;
            MainActivity.this.service = binder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            serviceBound = false;
        }
    }; */