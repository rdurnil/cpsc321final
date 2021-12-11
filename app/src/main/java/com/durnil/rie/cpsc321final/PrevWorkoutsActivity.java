package com.durnil.rie.cpsc321final;

import android.content.Intent;
import android.os.Bundle;
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

public class PrevWorkoutsActivity extends AppCompatActivity {
    static final String TAG = "PrevWorkoutsActivityTag";
    WorkoutsAdapter adapter = new WorkoutsAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prev_workout_selection);

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
//                intent.putExtra("workout_id", id);
                startActivity(intent);
            }

            public void updateView (Workout passedWorkout) {

            }
        }

        @NonNull
        @Override
        public WorkoutsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull WorkoutsViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
