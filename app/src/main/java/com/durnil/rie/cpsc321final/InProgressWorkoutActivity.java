package com.durnil.rie.cpsc321final;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class InProgressWorkoutActivity extends AppCompatActivity {
    Handler handler = null;
    int seconds = 0;
    double distance = 0.0;

    Button pauseButton;
    Runnable runnable;
    Intent serviceIntent;

    boolean serviceBound;
    BackgroundService service;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_REQUEST_CODE = 1;
    private List<LatLng> locations;
    private static final String TAG = "WorkoutAppTag";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_progress_workout);

        locations = new ArrayList<>();
        fusedLocationProviderClient = new FusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationCallback locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);

                    Location location = locationResult.getLastLocation();
                    locations.add(new LatLng(location.getLatitude(), location.getLongitude()));
                    Log.d(TAG, "onLocationResult: " + location.getLatitude() + ", " + location.getLongitude());
                }
            };
            fusedLocationProviderClient.requestLocationUpdates(LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(5), locationCallback, handler.getLooper());
        } else {
            // need to request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }

        handler = new Handler(Looper.getMainLooper());
        // start timer immediately since they already pressed start
        handler.postDelayed(runnable, 1000);

        runnable = new Runnable() {
            @Override
            public void run() {
                updateSeconds(seconds + 1); //Updating timer text
                updateDistance();
                updateAvgSpeed();
                handler.postDelayed(this, 1000); //Running runnable every second
            }
        };


        //Pause Button:
        pauseButton = findViewById(R.id.inProgPauseButton);
        if (pauseButton.getText().toString().equals(R.string.pause)) {
            pauseButton.setText(R.string.resume);
            pauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopTimerAndTracking(runnable);
                }
            });
        } else {
            pauseButton.setText(R.string.pause);
            pauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (handler == null) {
                        handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(runnable, 1000);
                    }
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
    * Function to update the seconds text:
    */
    public void updateSeconds (int newSeconds) {
        seconds = newSeconds; //Updating seconds
        TextView tv = findViewById(R.id.timeDisplayTextView);
        tv.setText(formatSeconds());
    }

    private String formatSeconds() {
        int hours = this.seconds / 3600;
        int minutes = (this.seconds % 3600) / 60;
        int seconds = this.seconds % 60;
        return hours + ":" + minutes + ":" + seconds;
        // TODO: figure out how to add zeros if int is less than 10
    }

    private void updateDistance() {
        if (locations.size() < 2) {
            distance = 0.0;
        } else {
            LatLng location1 = locations.get(locations.size() - 2);
            LatLng location2 = locations.get(locations.size() - 1);
            //int latDiff = location2.get
        }
    }

    private void updateAvgSpeed() {

    }

    /**
     * Function to stop the timer:
    */
    public void stopTimerAndTracking(Runnable runnableParam) {
        if (handler != null) {
            handler.removeCallbacks(runnableParam);
            handler = null;
        }
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // Binding to the Background service and getting its instance
            // in order to use public methods
            BackgroundService.LocalBinder binder = (BackgroundService.LocalBinder) service;
            InProgressWorkoutActivity.this.service = binder.getService();
            boolean serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            serviceBound = false;
        }
    };
}
