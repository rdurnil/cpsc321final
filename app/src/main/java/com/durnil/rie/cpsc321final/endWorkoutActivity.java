package com.durnil.rie.cpsc321final;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * The endWorkoutActivity class is responsible for taking the user and map data and
 *  entering it as a new entry into the database
 */
public class endWorkoutActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener {
    MapView mapView;
    Spinner typeSpinner;
    TextView workoutName;
    TextView timeTV;
    TextView distanceTV;
    TextView avgSpeedTV;
    Button deleteButton;
    Button saveButton;
    WorkoutOpenHelper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_workout);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.endMap);
        mapFragment.getMapAsync(this);
        //Setting up the views:
        mapView = findViewById(R.id.endMap);
        workoutName = findViewById(R.id.workoutNameEditText);
        timeTV = findViewById(R.id.newWorkoutTimeValueTextView);
        distanceTV = findViewById(R.id.newWorkoutDistDataTextView);
        avgSpeedTV = findViewById(R.id.newWorkoutSpeedDataTextView);
        saveButton = findViewById(R.id.newWorkoutSaveButton);
        deleteButton = findViewById(R.id.newWorkoutDeleteButton);
        typeSpinner = findViewById(R.id.typeSpinner);
        //Setting up the spinner:
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.types_array, android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Workout temp = new Workout()
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Polyline route = googleMap.addPolyline(new PolylineOptions().clickable(false).add(
                new LatLng(-35.016, 143.321),
                new LatLng(-34.747, 145.592),
                new LatLng(-34.364, 147.891),
                new LatLng(-33.501, 150.217),
                new LatLng(-32.306, 149.248),
                new LatLng(-32.491, 147.309)
        ));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-23.684, 133.903), 4));

        googleMap.setOnPolylineClickListener(this);
    }

    @Override
    public void onPolylineClick(@NonNull Polyline polyline) {

    }
}
