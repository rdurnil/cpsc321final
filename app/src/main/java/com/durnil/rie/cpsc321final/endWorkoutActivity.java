package com.durnil.rie.cpsc321final;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * The endWorkoutActivity class is responsible for taking the user and map data and
 *  entering it as a new entry into the database
 */
public class endWorkoutActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener {
    Spinner typeSpinner;
    TextView workoutName;
    TextView timeTV;
    TextView distanceTV;
    TextView avgSpeedTV;
    Button deleteButton;
    Button saveButton;
    WorkoutOpenHelper helper;
    String time;
    double distance;
    List<LatLng> locations;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_workout);
        helper = new WorkoutOpenHelper(this);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.endMap);
        mapFragment.getMapAsync(this);
        //Setting up the views:
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

        Intent intent = getIntent();
        if (intent != null) {
            time = intent.getStringExtra("time");
            distance = intent.getDoubleExtra("distance", 0.0);
            double avgSpeed = intent.getDoubleExtra("avgSpeed", 0.0);
            locations = intent.getParcelableArrayListExtra("locations");
            timeTV.setText(time);
            distanceTV.setText(String.format("%.2f", distance) + " miles");
            avgSpeedTV.setText(String.format("%.2f", avgSpeed) + " mi/min");
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String type = typeSpinner.getSelectedItem().toString();
                if (workoutName.getText().toString().equals("")) {
                    Toast.makeText(endWorkoutActivity.this, "You must give your workout a name.", Toast.LENGTH_LONG).show();
                } else {
                    Workout workout = new Workout(workoutName.getText().toString(), type,
                            LocalDate.now().toString(), time, distance);
                    helper.insertWorkout(workout);
                    Intent intent = new Intent(endWorkoutActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(endWorkoutActivity.this);
                builder.setTitle("Delete Workout?").setMessage("Do you want to delete the workout " +
                        "that you just completed?")
                        .setPositiveButton("Yes, delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(endWorkoutActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No, don't delete", null);
                builder.show();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Polyline route = googleMap.addPolyline(new PolylineOptions().clickable(false).addAll(locations));

        //Update this to use the first lat lng and closer zoom (10?)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations.get(locations.size() / 2), 10));

        googleMap.setOnPolylineClickListener(this);
    }

    @Override
    public void onPolylineClick(@NonNull Polyline polyline) {

    }
}
