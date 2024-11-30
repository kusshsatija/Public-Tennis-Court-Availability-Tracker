package com.example.publictenniscourtavailabilitytracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.github.muddz.styleabletoast.StyleableToast;

public class AvailabilityPage extends AppCompatActivity {
    TextView ParkName;
    ImageView backButton;
    Button startGameButton;
    String name;
    TextView selectedTimerTextView = null; // To store the selected TextView
    int selectedCourtNumber = -1;

    // List to store court data (name + status)
    List<String> courtData = new ArrayList<>();
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



        //get court detail values
        name = getIntent().getStringExtra("park_name");
        int numofCourts = getIntent().getIntExtra("numofCourts", 0);



        //generate appropriate courts + timers
        try {
            generateCourtTextViews(numofCourts);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //set the appropriate park name
        ParkName = findViewById(R.id.park_name);
        ParkName.setText(name);

        //back button
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish(); //closes the current activity and navigates back
            }
        });


    }
    //method for making the court number and timer textviews
    private void generateCourtTextViews(int count) throws IOException {
        LinearLayout layout = findViewById(R.id.text_view_container);
        layout.removeAllViews(); //Clear any existing views

        //availability timers data
        List<String> timers = getCourtsForPark(name);

        //for each court in that park
        for (int i = 0; i < count; i++) {
            LinearLayout courtLayout = new LinearLayout(this);
            courtLayout.setOrientation(LinearLayout.HORIZONTAL);
            courtLayout.setPadding(16, 16, 16, 50);

            //Court number TextView
            TextView courtTextView = new TextView(this);
            courtTextView.setText("Court " + (i + 1));
            courtTextView.setTextSize(20);
            courtTextView.setTypeface(null, Typeface.BOLD);


            //Timer TextView with background color
            TextView timerTextView = new TextView(this);
            timerTextView.setTextSize(20);
            timerTextView.setTypeface(null, Typeface.BOLD);
            String time = timers.get(i); //get timer info from data file

            final int courtIndex = i;
            timerTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (selectedTimerTextView != null) {
                        selectedTimerTextView.setBackgroundResource(R.drawable.title_background); // Reset background
                    }

                    // Mark the new selection
                    selectedTimerTextView = timerTextView;
                    selectedCourtNumber = courtIndex + 1; // Set the court number (1-based index)

                    // Apply the selected background
                    if ("Free to Play".equals(timerTextView.getText())) {
                        timerTextView.setBackgroundResource(R.drawable.when_clicked); // Selected free court
                    } else {
                        timerTextView.setBackgroundResource(R.drawable.when_clicked_red); // Selected occupied court
                    }
                }
            });

            if("00:00".equals(time)){ // if court is available
                timerTextView.setBackgroundResource(R.drawable.title_background);
                timerTextView.setTextColor(Color.parseColor("#4faa79"));
                timerTextView.setText("Free to Play");
            }else {  //if court is not available
                timerTextView.setBackgroundResource(R.drawable.title_background);
                timerTextView.setTextColor(Color.parseColor("#ff3131"));

                //parse the timer value (time in "MM:SS" format)
                String[] timeParts = time.split(":");
                int minutes = Integer.parseInt(timeParts[0]);
                int seconds = Integer.parseInt(timeParts[1]);

                // Convert the time to milliseconds
                long timeInMillis = (minutes * 60 + seconds) * 1000; // Convert to milliseconds

                // Create the CountDownTimer with the parsed time
                CountDownTimer countDownTimer = new CountDownTimer(timeInMillis, 1000) { // Timer duration in ms
                    public void onTick(long millisUntilFinished) {
                        long remainingMinutes = (millisUntilFinished / 1000) / 60;
                        long remainingSeconds = (millisUntilFinished / 1000) % 60;
                        timerTextView.setText(String.format("%02d:%02d Remaining", remainingMinutes, remainingSeconds));
                    }

                    public void onFinish() {
                        timerTextView.setText("Free to Play");
                        timerTextView.setBackgroundResource(R.drawable.rounded_timer_green); // Change background when finished
                    }
                };

                countDownTimer.start(); //start the countdown timer
            }



            //create space inbetween the textviews
            Space space = new Space(this);
            space.setLayoutParams(new LinearLayout.LayoutParams(60, LinearLayout.LayoutParams.WRAP_CONTENT));

            courtLayout.addView(courtTextView);
            courtLayout.addView(space);
            courtLayout.addView(timerTextView);


            layout.addView(courtLayout);
        }
    }



    public void onStartGame(View view) {
        if (selectedTimerTextView == null) {
            // Show Toast if no timerTextView is selected

            StyleableToast.makeText(AvailabilityPage.this, "Please select a free court to play!", R.style.exampleToast).show();
        } else {
            String selectedTime = selectedTimerTextView.getText().toString();

            if ("Free to Play".equals(selectedTime)) {
                // Proceed with game start
                Intent intent = new Intent(AvailabilityPage.this, Camera.class);
                intent.putExtra("ParkName", name);
                intent.putExtra("courtId", selectedCourtNumber);
                startActivity(intent); // Start the new activity
            } else {
                // Show Toast if the selected timer is not free
                StyleableToast.makeText(AvailabilityPage.this, "Selected court is not free. Please choose another.", R.style.exampleToast).show();

            }
        }
    }


    public List<String> getCourtsForPark(String parkName) throws IOException {
        List<String> courts = new ArrayList<>();
        File file = new File(this.getFilesDir(), "courts_availability_data.txt");
        if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data[0].equals(parkName)) { //match park name
                    courts.add(data[2]); //add timer
                }
            }
            reader.close();
        }
        return courts;
    }








}