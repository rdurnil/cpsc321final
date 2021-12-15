package com.durnil.rie.cpsc321final;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * PrevWorkoutViewActivity is responsible for showing the view of the user selected previous workout
 */
public class PrevWorkoutViewActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener {
    private WorkoutOpenHelper helper;
    private TextView nameTV;
    private TextView timeTV;
    private TextView distTV;
    private TextView speedTV;
    private Button deleteBtn;
    private Button backBtn;
    private int workoutID;
    private Workout workout;
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_prev_workout);

        nameTV = findViewById(R.id.prevWorkoutNameTextView);
        timeTV = findViewById(R.id.prevWorkoutTimeValueTextView);
        distTV = findViewById(R.id.prevWorkoutDistDataTextView);
        speedTV = findViewById(R.id.prevWorkoutSpeedDataTextView);
        deleteBtn = findViewById(R.id.prevWorkoutDeleteButton);
        backBtn = findViewById(R.id.prevWorkoutBackButton);
        helper = new WorkoutOpenHelper(this);
        Intent intent = getIntent();
        workoutID = intent.getIntExtra("workout_id", 0);
        workout = helper.getSelectWorkoutById(workoutID);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.prevWorkoutMap);
        mapFragment.getMapAsync(this);

        updateViews();

        //Functionality for the delete button:
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Setting up alert dialog to confirm to use that they want to delete the activity:
                AlertDialog.Builder builder = new AlertDialog.Builder(PrevWorkoutViewActivity.this);
                builder.setTitle("Delete Workout?")
                        .setMessage("Are you sure you want to delete " + workout.getWorkoutName() + "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent deleteIntent = new Intent(PrevWorkoutViewActivity.this, PrevWorkoutsActivity.class);
                                deleteIntent.putExtra("workout_id", workout.getId());

                                helper.deleteWorkoutById(workout.getId());

                                PrevWorkoutViewActivity.this.setResult(Activity.RESULT_OK, deleteIntent);
                                    Toast.makeText(PrevWorkoutViewActivity.this, "Workout Deleted", Toast.LENGTH_SHORT).show();
                                PrevWorkoutViewActivity.this.finish();
                            }
                        }).setNegativeButton("Cancel", null);
                builder.show();
            }
        });

        //Setting up functionality for the back button:
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent(PrevWorkoutViewActivity.this, PrevWorkoutsActivity.class);
                PrevWorkoutViewActivity.this.setResult(Activity.RESULT_OK, backIntent);
                PrevWorkoutViewActivity.this.finish();
            }
        });
    }

    //Helper function to update all of the views with the text from the DB to not take up onCreate()
    private void updateViews() {
        nameTV.setText(workout.getWorkoutName());
        timeTV.setText(workout.getTime());
        distTV.setText(String.format("%.2f", workout.getDistance()) + " miles");
        speedTV.setText(String.format("%.2f", workout.getAvgSpeed()) + " min/mi");
    }

    @Override
    public void onPolylineClick(@NonNull Polyline polyline) {
        //Functionality for clicking on the line...
        //Not sure what would go here in the context of this project...maybe details?
    }

    //Setting up the map view for the previous workout:
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        List<LatLng> latLngList = helper.getLatLangsById(workoutID);
        Polyline route;
        route = googleMap.addPolyline(new PolylineOptions().clickable(false).addAll(latLngList));
        //This next line moves the camera to the first latLng and zooms to level 10
        if (latLngList.size() > 0) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngList.get(0), 15));
            googleMap.setOnPolylineClickListener(this);
        }
    }
}
