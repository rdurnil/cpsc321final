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
    static final int LOCATION_REQUEST_CODE = 1;
    boolean locationPermissionGranted = false;

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
                if (locationPermissionGranted) {
                    Intent intent = new Intent(MainActivity.this, NewWorkoutActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "You must enable your location first", Toast.LENGTH_LONG).show();
                    enableMyLocation();
                }
            }
        });

        prevWorkoutsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PrevWorkoutsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void enableMyLocation() {
        // get the user's permission to access their fine location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
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
                locationPermissionGranted = false;
            }
        }
    }
}
