package com.durnil.rie.cpsc321final;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;

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
    Button endButton;
    Handler handler = null;
    int seconds = 0;
    double distance = 0.0;
    double avgSpeed = 0.0;

    Button pauseButton;
    Runnable timerRunnable;
    Runnable locationRunnable;
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

        endButton = findViewById(R.id.inProgEndButton);

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InProgressWorkoutActivity.this, endWorkoutActivity.class);
                startActivity(intent);
            }
        });

        locations = new ArrayList<>();
        fusedLocationProviderClient = new FusedLocationProviderClient(this);

        handler = new Handler(Looper.getMainLooper());
        // start timer immediately since they already pressed start
        handler.postDelayed(timerRunnable, 1000);
        FetchCurrentLocationsTask asyncTask = new FetchCurrentLocationsTask();
        asyncTask.execute();

        timerRunnable = new Runnable() {
            @Override
            public void run() {
                updateSeconds(seconds + 1); //Updating timer text
                handler.postDelayed(this, 1000); //Running runnable every second
            }
        };

        locationRunnable = new Runnable() {
            @Override
            public void run() {
                updateDistance();
                updateAvgSpeed();
            }
        };

        //Pause Button:
        pauseButton = findViewById(R.id.inProgPauseButton);
        if (pauseButton.getText().toString().equals(R.string.pause)) {
            pauseButton.setText(R.string.resume);
            pauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.removeCallbacks(timerRunnable);
                    handler.removeCallbacks(locationRunnable);
                    handler = null;
                }
            });
        } else {
            pauseButton.setText(R.string.pause);
            pauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (handler == null) {
                        handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(timerRunnable, 1000);
                        asyncTask.execute();
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
            double latDiff = location2.latitude - location1.latitude;
            double lngDiff = location2.longitude - location1.longitude;
            distance += Math.sqrt(Math.pow(latDiff, 2) + Math.pow(lngDiff, 2));
        }
        TextView tv = findViewById(R.id.distanceDisplayTextView);
        tv.setText(distance + " miles");
    }

    private void updateAvgSpeed() {
        avgSpeed = distance / (seconds % 60);
        TextView tv = findViewById(R.id.avgSpeedDisplayTextView);
        tv.setText(avgSpeed + "mi/min");
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

    class FetchCurrentLocationsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            if (ContextCompat.checkSelfPermission(InProgressWorkoutActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                LocationCallback locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);

                        Location location = locationResult.getLastLocation();
                        locations.add(new LatLng(location.getLatitude(), location.getLongitude()));
                        updateDistance();
                        updateAvgSpeed();
                        Log.d(TAG, "onLocationResult: " + location.getLatitude() + ", " + location.getLongitude());
                    }
                };
                fusedLocationProviderClient.requestLocationUpdates(LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(5000), locationCallback, handler.getLooper());
            } else {
                // need to request permission
                ActivityCompat.requestPermissions(InProgressWorkoutActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
            }
            return null;
        }
    }
}
