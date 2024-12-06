package com.example.publictenniscourtavailabilitytracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PlayingGame extends AppCompatActivity {
    TextView title, timer;
    Button endButton;
    String parkName;
    int courtId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_playing_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //initialize
        title =  findViewById(R.id.park_court_nameTV);
        endButton = findViewById(R.id.end_button);
        timer = findViewById(R.id.timerTV);



        //get the court number and park name
        parkName = getIntent().getStringExtra("ParkName");
        courtId = getIntent().getIntExtra("courtId",-1);

        title.setText("Game started \nCourt Number: " + courtId + "\nLocation: " + parkName);

        // 60 minutes in milliseconds
        long startTimeInMillis = 60 * 60 * 1000;

        // Create the CountDownTimer
        CountDownTimer countDownTimer = new CountDownTimer(startTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Calculate remaining minutes and seconds
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;

                //update the TextView with the formatted time
                timer.setText(String.format("%02d:%02d", minutes, seconds));

            }
            @Override
            public void onFinish() {
                //update the TextView when the timer finishes
                timer.setText("Time's up!");
                timer.setTextColor(Color.parseColor("#ff3131"));
            }
        };

        // Start the timer
        countDownTimer.start();

        //end button functinality
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}