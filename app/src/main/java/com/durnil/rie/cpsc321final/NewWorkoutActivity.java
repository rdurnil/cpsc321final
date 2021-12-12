package com.durnil.rie.cpsc321final;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.MapView;

public class NewWorkoutActivity extends AppCompatActivity {
    Button startButton;
    MapView mapView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workout);

        startButton = findViewById(R.id.startWorkoutButton);
        mapView = findViewById(R.id.mapViewNewWorkout);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newWorkoutIntent = new Intent(NewWorkoutActivity.this, InProgressWorkoutActivity.class);

            }
        });
    }


}
