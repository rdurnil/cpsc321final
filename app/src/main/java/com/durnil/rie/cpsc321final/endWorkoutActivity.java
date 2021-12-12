package com.durnil.rie.cpsc321final;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.MapView;

/**
 * The endWorkoutActivity class is responsible for taking the user and map data and
 *  entering it as a new entry into the database
 */
public class endWorkoutActivity extends AppCompatActivity {
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
        //Setting up the views:
        mapView = findViewById(R.id.newWorkoutMap);
        workoutName = findViewById(R.id.workoutNameEditText);
        timeTV = findViewById(R.id.newWorkoutTimeValueTextView);
        distanceTV = findViewById(R.id.newWorkoutDistDataTextView);
        avgSpeedTV = findViewById(R.id.newWorkoutSpeedDataTextView);
        saveButton = findViewById(R.id.newWorkoutSaveButton);
        deleteButton = findViewById(R.id.newWorkoutDeleteButton);
        typeSpinner = findViewById(R.id.workoutTypeSpinner);
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
}
