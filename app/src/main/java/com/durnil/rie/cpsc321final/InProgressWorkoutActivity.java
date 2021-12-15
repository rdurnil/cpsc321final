package com.durnil.rie.cpsc321final;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import java.util.Locale;

public class InProgressWorkoutActivity extends AppCompatActivity {
    Button endButton;
    Handler handler = null;
    int seconds = 0;
    double distance = 0.0;
    double avgSpeed = 0.0;

    Button pauseButton;
    Runnable timerRunnable;
    Runnable locationRunnable;
    LocationCallback locationCallback;
    Intent serviceIntent;

    boolean pauseEndWorkout = false;
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

        if (ContextCompat.checkSelfPermission(InProgressWorkoutActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            timerRunnable = new Runnable() {
                @Override
                public void run() {
                    updateSeconds(seconds + 1); //Updating timer text
                    handler.postDelayed(this, 1000); //Running runnable every second
                }
            };

            locations = new ArrayList<>();
            fusedLocationProviderClient = new FusedLocationProviderClient(this);

            locationCallback = new LocationCallback() {
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

            locationRunnable = new Runnable() {
                @SuppressLint("MissingPermission")
                @Override
                public void run() {
                    fusedLocationProviderClient.requestLocationUpdates(LocationRequest.create()
                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(5000), locationCallback, handler.getLooper());
                }
            };

            handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(timerRunnable, 1000);
            handler.postDelayed(locationRunnable, 5000);

            //Pause Button:
            pauseButton = findViewById(R.id.inProgPauseButton);
            pauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(InProgressWorkoutActivity.this, "pause button", Toast.LENGTH_SHORT).show();
                    if (pauseButton.getText().toString().equals("Pause")) {
                        pauseButton.setText(R.string.resume);
                        handler.removeCallbacks(timerRunnable);
                        handler.removeCallbacks(locationRunnable);
                        if (locationCallback != null) {
                            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                        }
                        handler = null;
                        pauseEndWorkout = true;
                    } else {
                        pauseButton.setText(R.string.pause);
                        if (handler == null) {
                            handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(timerRunnable, 1000);
                            handler.postDelayed(locationRunnable, 5000);
                        }
                        pauseEndWorkout = false;
                    }
                }
            });

            endButton = findViewById(R.id.inProgEndButton);
            endButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (handler != null) {
                        handler.removeCallbacks(timerRunnable);
                        handler.removeCallbacks(locationRunnable);
                    }
                    if (locationCallback != null) {
                        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                    }
                    handler = null;
                    pauseEndWorkout = true;

                    Intent intent = new Intent(InProgressWorkoutActivity.this, endWorkoutActivity.class);
                    intent.putExtra("time", formatSeconds());
                    intent.putExtra("distance", distance);
                    intent.putExtra("avgSpeed", avgSpeed);
                    intent.putParcelableArrayListExtra("locations", (ArrayList<? extends Parcelable>) locations);
                    startActivity(intent);
                }
            });
        } else {
            // need to request permission
            ActivityCompat.requestPermissions(InProgressWorkoutActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }
    }

    /**
     * Function to update the seconds text:
     */
    public void updateSeconds(int newSeconds) {
        seconds = newSeconds; //Updating seconds
        TextView tv = findViewById(R.id.timeDisplayTextView);
        tv.setText(formatSeconds());
    }

    private String formatSeconds() {
        int hours = this.seconds / 3600;
        int minutes = (this.seconds % 3600) / 60;
        int seconds = this.seconds % 60;
        String time = String.format(Locale.getDefault(),
                "%02d:%02d:%02d", hours, minutes, seconds);
        return time;
    }

    private void updateDistance() {
        if (locations.size() < 2) {
            distance = 0.0;
        } else {
            LatLng location1 = locations.get(locations.size() - 2);
            LatLng location2 = locations.get(locations.size() - 1);
            double latDiff = location2.latitude - location1.latitude;
            double lngDiff = location2.longitude - location1.longitude;
            distance += ((Math.sqrt(Math.pow(latDiff, 2) + Math.pow(lngDiff, 2))) * 69.2);
        }
        TextView tv = findViewById(R.id.distanceDisplayTextView);
        tv.setText(String.format("%.2f", distance) + " miles");
    }

    private void updateAvgSpeed() {
        avgSpeed = ((double) seconds / 60.0) / distance;
        TextView tv = findViewById(R.id.avgSpeedDisplayTextView);
        tv.setText(String.format("%.2f", avgSpeed) + " min/mi");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!pauseEndWorkout) {
            serviceIntent = new Intent(InProgressWorkoutActivity.this, BackgroundService.class);
            serviceIntent.putExtra("seconds", seconds);
            startService(serviceIntent);
            bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (serviceIntent != null) {
            stopService(serviceIntent);
        }
        if (serviceBound) {
            // get the seconds to set the timer to
            seconds = service.getSeconds();
            locations.addAll(service.getLocations());
            distance = distance + service.getDistance();
            updateSeconds(seconds);
            updateDistance();
            updateAvgSpeed();
            // restart the timer
            if (handler == null) {
                handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(timerRunnable, 1000);
                handler.postDelayed(locationRunnable, 5000);
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
            InProgressWorkoutActivity.this.service = binder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            serviceBound = false;
        }
    };
}
