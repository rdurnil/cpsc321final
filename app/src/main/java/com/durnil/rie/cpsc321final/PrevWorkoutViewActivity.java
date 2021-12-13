package com.durnil.rie.cpsc321final;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PrevWorkoutViewActivity extends AppCompatActivity {
    WorkoutOpenHelper helper;
    TextView nameTV;
    TextView timeTV;
    TextView distTV;
    TextView speedTV;
    Button deleteBtn;
    Button backBtn;
    int workoutID;
    Workout workout;

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
                                //TODO: send the user back to the last activity
                                helper.deleteWorkoutById(workout.getId());
                            }
                        }).setNegativeButton("Cancel", null);
                builder.show();
            }
        });
    }


}
