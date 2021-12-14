package com.durnil.rie.cpsc321final;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class BackgroundService extends Service {
    public static final String TAG = "BackgroundService";
    private Runnable timerRunnable;
    private Runnable locationRunnable;
    private Handler handler = null;
    private int seconds = 0;
    private int initialSeconds = 0;
    private List<LatLng> locations;
    private double distance = 0.0;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
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

        fusedLocationProviderClient = new FusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location location = locationResult.getLastLocation();
                locations.add(new LatLng(location.getLatitude(), location.getLongitude()));
                updateDistance();
                Log.d(TAG, "onLocationResult: " + location.getLatitude() + ", " + location.getLongitude());
            }
        };

        // runs the timer, same as timer in MainActivity
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                seconds++; //Updating timer
                handler.postDelayed(this, 1000); //Running runnable every second
            }
        };

        locationRunnable = new Runnable() {
            @Override
            public void run() {
                if (ActivityCompat.checkSelfPermission(BackgroundService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(BackgroundService.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                fusedLocationProviderClient.requestLocationUpdates(LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(5000), locationCallback, handler.getLooper());
            }
        };

        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(timerRunnable, 1000);
            handler.postDelayed(locationRunnable, 5000);
        }
    }

    @Override
    public void onDestroy() {
        // ends the timer
        handler.removeCallbacks(timerRunnable);
        handler.removeCallbacks(locationRunnable);
        handler = null;
        super.onDestroy();
    }

    public int getSeconds() {
        // returns the total seconds the timer should be set to
        return seconds + initialSeconds;
    }

    public List<LatLng> getLocations() {
        return locations;
    }

    public double getDistance() {
        return distance;
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
    }

    public class LocalBinder extends Binder {
        BackgroundService getService() {
            // Returns this instance of the Background service so that the Main Activity
            // can access it's public methods
            return BackgroundService.this;
        }
    }
}
