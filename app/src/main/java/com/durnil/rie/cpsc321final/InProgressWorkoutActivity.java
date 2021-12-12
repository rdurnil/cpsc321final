package com.durnil.rie.cpsc321final;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class InProgressWorkoutActivity extends AppCompatActivity {
    Button endButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_progress_workout);

        endButton = findViewById(R.id.inProgEndButton);

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InProgressWorkoutActivity.this, endWorkoutActivity.class);
                startActivity(intent);
            }
        });
    }
}
