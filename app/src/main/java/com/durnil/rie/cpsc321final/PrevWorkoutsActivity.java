package com.durnil.rie.cpsc321final;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    }

    class WorkoutsAdapter extends RecyclerView.Adapter<WorkoutsAdapter.WorkoutsViewHolder> {
        TextView workoutName;
        TextView workoutDate;

        class WorkoutsViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
            public WorkoutsViewHolder(@NonNull View itemView) {
                super(itemView);
                workoutName = itemView.findViewById(R.id.workoutNameText);
                workoutDate = itemView.findViewById(R.id.workoutDateText);
            }

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrevWorkoutsActivity.this, PrevWorkoutViewActivity.class);
                //The indices of these should be lined up:
                intent.putExtra("workout_id", workouts.get(getAdapterPosition()).getId());
                startActivity(intent);
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
            Workout workout = helper.getSelectAllWorkouts().get(position);
            holder.updateView(workout);
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
