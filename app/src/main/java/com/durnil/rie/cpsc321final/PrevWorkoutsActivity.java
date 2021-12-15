package com.durnil.rie.cpsc321final;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

//TODO: Add functionality to update the list and RV after returning to this screen as the user may have deleted an item
public class PrevWorkoutsActivity extends AppCompatActivity {
    static final String TAG = "PrevWorkoutsActivityTag";
    WorkoutsAdapter adapter = new WorkoutsAdapter();
    WorkoutOpenHelper helper;
    List<Workout> workouts;
    ActivityResultLauncher<Intent> viewWorkoutLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prev_workout_selection);
        helper = new WorkoutOpenHelper(this);
        workouts = helper.getSelectAllWorkouts();

        RecyclerView workoutsRV = findViewById(R.id.prevWorkoutsRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        workoutsRV.setLayoutManager(layoutManager);
        workoutsRV.setAdapter(adapter);

        //Setting up launcher:
        viewWorkoutLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            assert data != null;
                            adapter.notifyDataSetChanged(); //Bad practice
                        }
                    }
                });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class WorkoutsAdapter extends RecyclerView.Adapter<WorkoutsAdapter.WorkoutsViewHolder> {
        TextView workoutName;
        TextView workoutDate;

        class WorkoutsViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
            public WorkoutsViewHolder(@NonNull View itemView) {
                super(itemView);
                workoutName = itemView.findViewById(R.id.workoutNameText);
                workoutDate = itemView.findViewById(R.id.workoutDateText);

                workoutName.setOnClickListener(this);
                workoutDate.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrevWorkoutsActivity.this, PrevWorkoutViewActivity.class);
                //The indices of these should be lined up:
                intent.putExtra("workout_id", workouts.get(getItemCount() - getAdapterPosition() - 1).getId());
                viewWorkoutLauncher.launch(intent); //Launching the intent
            }

            public void updateView (Workout passedWorkout) {
                workoutName.setText(passedWorkout.getWorkoutName());
                workoutDate.setText(passedWorkout.getDate());
            }
        }

        @NonNull
        @Override
        public WorkoutsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(PrevWorkoutsActivity.this).inflate(R.layout.list_item_prev_workouts, parent, false);
            return new WorkoutsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull WorkoutsViewHolder holder, int position) {
            if (workouts.size() > 0) {
                Workout workout = helper.getSelectAllWorkouts().get(getItemCount() - position - 1);
                holder.updateView(workout);
            }
        }

        @Override
        public int getItemCount() {
            if (workouts != null) {
                return workouts.size();
            }
            return 0;
        }
    }
}
