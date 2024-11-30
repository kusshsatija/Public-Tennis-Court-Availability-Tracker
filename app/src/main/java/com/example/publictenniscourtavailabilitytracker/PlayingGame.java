package com.example.publictenniscourtavailabilitytracker;

import android.content.Intent;
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

                // Update the TextView with the formatted time
                timer.setText(String.format("%02d:%02d Remaining", minutes, seconds));
                timer.setBackgroundResource(R.drawable.rounded_timer_green);
            }
            @Override
            public void onFinish() {
                // Update the TextView when the timer finishes
                timer.setText("Time's up!");
                timer.setBackgroundResource(R.drawable.rounded_timer_background); // change colour to red
            }
        };

        // Start the timer
        countDownTimer.start();

        // Set an OnClickListener to handle the back navigation
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}