package com.example.publictenniscourtavailabilitytracker;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import java.util.Random;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AvailabilityPage extends AppCompatActivity {
    TextView ParkName;
    ImageView backButton;
    Button startGameButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_availability_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //initialize game button
        startGameButton = findViewById(R.id.start_game_button);
        startGameButton.setEnabled(false); // Initially disabled



        //get court detail values
        String name = getIntent().getStringExtra("park_name");
        int numofCourts = getIntent().getIntExtra("numofCourts", 0);

        //generate appropriate courts
        generateCourtTextViews(numofCourts);

        //set the appropriate park name
        ParkName = findViewById(R.id.park_name);
        ParkName.setText(name);

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish(); //closes the current activity and navigates back
            }
        });





    }
    private void generateCourtTextViews(int count) {
        LinearLayout layout = findViewById(R.id.text_view_container);
        layout.removeAllViews(); // Clear any existing views

        for (int i = 0; i < count; i++) {
            //new code
            LinearLayout courtLayout = new LinearLayout(this);
            courtLayout.setOrientation(LinearLayout.HORIZONTAL);
            courtLayout.setPadding(16, 16, 16, 60);

            //Court number TextView
            TextView textView = new TextView(this);
            textView.setText("Court " + (i + 1));
            textView.setTextSize(22);
            textView.setTypeface(null, Typeface.BOLD);



            //new code
            // Timer TextView with background color
            TextView timerTextView = new TextView(this);
            timerTextView.setTextSize(22);
            //if button is selected
            timerTextView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startGameButton.setEnabled(true);
                }
            });
            timerTextView.setTextColor(Color.WHITE);
            timerTextView.setTypeface(null, Typeface.BOLD);
            timerTextView.setPadding(16, 8, 16, 8); // Add padding inside the background
            if(RandomizerColor()){
                timerTextView.setBackgroundResource(R.drawable.rounded_timer_green);
                timerTextView.setText("Free to Play");
            }else {
                timerTextView.setBackgroundResource(R.drawable.rounded_timer_background);

                Random random = new Random();
                int randomMinutes = random.nextInt(60) + 1; // Random number between 1 and 90
                // Convert the random minutes to milliseconds for the CountDownTimer
                long timeInMillis = randomMinutes * 60 * 1000;

                // Create the CountDownTimer with the randomized duration
                CountDownTimer countDownTimer = new CountDownTimer(timeInMillis, 1000) { // Timer duration in ms
                    public void onTick(long millisUntilFinished) {
                        long minutes = (millisUntilFinished / 1000) / 60;
                        long seconds = (millisUntilFinished / 1000) % 60;
                        timerTextView.setText(String.format("%02d:%02d Remaining", minutes, seconds));
                    }
                    public void onFinish() {
                        timerTextView.setText("Free to Play");
                        timerTextView.setBackgroundResource(R.drawable.rounded_timer_green); // Change background when finished
                    }
                };

                countDownTimer.start(); // Start the countdown timer
            }




            Space space = new Space(this);
            space.setLayoutParams(new LinearLayout.LayoutParams(60, LinearLayout.LayoutParams.WRAP_CONTENT));

            courtLayout.addView(textView);
            courtLayout.addView(space);
            courtLayout.addView(timerTextView);


            layout.addView(courtLayout);
        }
    }


    public boolean RandomizerColor(){
            Random random = new Random();
            boolean courtInUse = false;
            // Generate a random number between 0 and 2 (inclusive)
            int randomValue = random.nextInt(3); // 0, 1, or 2

        Random randomMin = new Random();
        int randomMinutes = randomMin.nextInt(90) + 1; // Generates 1 to 90 minutes

            // Check for 1/3 odds
            if (randomValue == 0) // 1 in 3 chance
                courtInUse = true;

        return courtInUse;
    }
    public void onStartGame(View view) {
        // Here, you can start the game activity or perform other actions
        Intent intent = new Intent(AvailabilityPage.this, Camera.class);
        startActivity(intent); // Start the new activity
    }



}