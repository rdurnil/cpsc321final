package com.durnil.rie.cpsc321final;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

public class NewWorkoutActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationClickListener {

    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_REQUEST_CODE = 1;
    private List<LatLng> locations;
    private static final String TAG = "WorkoutAppTag";
    private Button startWorkoutButton;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workout);

        locations = new ArrayList<>();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapViewNewWorkout);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        startWorkoutButton = findViewById(R.id.startWorkoutButton);
        startWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewWorkoutActivity.this, InProgressWorkoutActivity.class);
                startActivity(intent);
            }
        });

        handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 5000);
            }
        };

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            LocationCallback locationCallback = new LocationCallback() {
//                @Override
//                public void onLocationResult(@NonNull LocationResult locationResult) {
//                    super.onLocationResult(locationResult);
//                    Location location = locationResult.getLastLocation();
//                    locations.add(new LatLng(location.getLatitude(), location.getLongitude()));
//                    Log.d(TAG, "onLocationResult: " + location.getLatitude() + ", " + location.getLongitude());
//                }
//            };
////            fusedLocationProviderClient.requestLocationUpdates(LocationRequest.create()
////                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(5), locationCallback, handler.getLooper());
//        } else {
//            // need to request permission
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    LOCATION_REQUEST_CODE);
//        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        enableUserLocation();
    }

    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationCallback locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    Location location = locationResult.getLastLocation();
                    locations.add(new LatLng(location.getLatitude(), location.getLongitude()));
                    Log.d(TAG, "onLocationResult: " + location.getLatitude() + ", " + location.getLongitude());
                    setupMarker(new LatLng(locationResult.getLastLocation().getLatitude(),  locationResult.getLastLocation().getLongitude()));
                    }
            };

            map.setMyLocationEnabled(true);
            map.setOnMyLocationClickListener(this);
            fusedLocationProviderClient.requestLocationUpdates(LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(5), locationCallback, handler.getLooper());
        } else {
            // need to request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }
    }

    private void setupMarker(LatLng latLng) {
        MarkerOptions newMarker = new MarkerOptions();
        newMarker.title("User Location");
        newMarker.snippet("You Are Here");
        newMarker.position(latLng);
        map.addMarker(newMarker);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15.0f);
        map.moveCamera(cameraUpdate);
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }
}
