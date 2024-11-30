package com.example.publictenniscourtavailabilitytracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Reservation extends AppCompatActivity {
    private TextView parkName;
    private CalendarView calendarView;
    private Spinner courtDropdown, startTimeDropdown, endTimeDropdown;
    private Button goBackButton;
    private Button bookNowButton;

    private List<String> timeSlots;
    private String selectedDate; // To store the selected date

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        // Retrieve the Intent extras
        Intent intent = getIntent();
        String namePark = intent.getStringExtra("park_name");
        String timings = intent.getStringExtra("timings");
        int numOfCourts = intent.getIntExtra("num_of_courts", 0);

        // Initialize the UI components
        parkName = findViewById(R.id.park_name);
        calendarView = findViewById(R.id.calendar_view);
        courtDropdown = findViewById(R.id.court_dropdown);
        startTimeDropdown = findViewById(R.id.startTimeDropdown);
        endTimeDropdown = findViewById(R.id.endTimeDropdown);
        goBackButton = findViewById(R.id.go_back_button);
        bookNowButton = findViewById(R.id.book_now_button);

        // Set Values
        parkName.setText(namePark);

        // Restrict calendar to current day and future dates
        setCalendarMinDate();

        // Initialize the selected date to the current date
        setInitialSelectedDate();

        // Set a listener to get the selected date from the calendar
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Save the selected date in "yyyy-MM-dd" format
            selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
        });

        // Populate spinners
        populateTimeSpinners(timings);
        populateCourtDropdown(numOfCourts);

        // Set up event listeners for the buttons
        setButtonListeners();

        // Add listener for start time dropdown
        setupStartTimeListener();
    }

    private void setCalendarMinDate() {
        // Get the current date in milliseconds
        long currentDateMillis = Calendar.getInstance().getTimeInMillis();
        // Set the minimum date for the CalendarView
        calendarView.setMinDate(currentDateMillis);
    }

    private void setInitialSelectedDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        selectedDate = String.format("%04d-%02d-%02d", year, month + 1, day);
    }

    private void populateTimeSpinners(String timings) {
        // Parse the timings string
        String[] timeRange = timings.split(" - ");
        if (timeRange.length != 2) {
            Toast.makeText(this, "Invalid timing format", Toast.LENGTH_SHORT).show();
            return;
        }
        // Generate time slots
        timeSlots = generateTimeSlots(timeRange[0], timeRange[1]);
        // Create an ArrayAdapter and set it to the spinners
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeSlots);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startTimeDropdown.setAdapter(timeAdapter);
        endTimeDropdown.setAdapter(timeAdapter);
    }

    private List<String> generateTimeSlots(String startTime, String endTime) {
        List<String> timeSlots = new ArrayList<>();
        int startHour = convertTo24Hour(startTime);
        int startMinute = getMinutes(startTime);
        int endHour = convertTo24Hour(endTime);
        int endMinute = getMinutes(endTime);

        while (startHour < endHour || (startHour == endHour && startMinute < endMinute)) {
            timeSlots.add(convertTo12Hour(startHour, startMinute));
            startMinute += 30; // Increment by 30 minutes
            if (startMinute >= 60) {
                startMinute = 0;
                startHour++;
            }
        }
        return timeSlots;
    }

    private int convertTo24Hour(String time) {
        boolean isPM = time.contains("PM");
        time = time.replace(" AM", "").replace(" PM", "");
        int hour = Integer.parseInt(time.split(":")[0]);
        if (isPM && hour != 12) {
            hour += 12;
        } else if (!isPM && hour == 12) {
            hour = 0;
        }
        return hour;
    }

    private String convertTo12Hour(int hour, int minute) {
        String period = (hour >= 12) ? "PM" : "AM";
        int displayHour = (hour % 12 == 0) ? 12 : hour % 12;
        String displayMinute = (minute < 10) ? "0" + minute : String.valueOf(minute);
        return displayHour + ":" + displayMinute + " " + period;
    }

    private int getMinutes(String time) {
        String[] parts = time.split(":");
        if (parts.length > 1) {
            return Integer.parseInt(parts[1].replaceAll("[^\\d]", ""));
        }
        return 0;
    }

    private void populateCourtDropdown(int numOfCourts) {
        List<String> courtOptions = new ArrayList<>();
        for (int i = 1; i <= numOfCourts; i++) {
            courtOptions.add("Court " + i);
        }
        ArrayAdapter<String> courtAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courtOptions);
        courtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courtDropdown.setAdapter(courtAdapter);
    }

    private void setButtonListeners() {
        goBackButton.setOnClickListener(v -> finish());

        bookNowButton.setOnClickListener(v -> {
            String selectedCourt = courtDropdown.getSelectedItem().toString();
            String selectedStartTime = startTimeDropdown.getSelectedItem().toString();
            String selectedEndTime = endTimeDropdown.getSelectedItem().toString();
            // Ensure end time is after start time
            String duration = calculateDuration(selectedStartTime, selectedEndTime);
            // Send booking details to Map.java
            Intent mapIntent = new Intent(Reservation.this, Map.class);
            mapIntent.putExtra("selectedCourt", selectedCourt);
            mapIntent.putExtra("selectedStartTime", selectedStartTime);
            mapIntent.putExtra("selectedEndTime", selectedEndTime);
            mapIntent.putExtra("duration", duration);
            mapIntent.putExtra("selectedDate", selectedDate);
            mapIntent.putExtra("parkName", parkName.getText().toString());

            Log.d("Map", "Sending Intent Data:");
            Log.d("Map", "Selected Court: " + selectedCourt);
            Log.d("Map", "Start Time: " + selectedStartTime);
            Log.d("Map", "End Time: " + selectedEndTime);
            Log.d("Map", "duration: " + duration);
            Log.d("Map", "Selected Date: " + selectedDate);
            Log.d("Map", "Park Name: " + parkName);

            startActivity(mapIntent);
        });
    }

    private String calculateDuration(String selectedStartTime, String selectedEndTime) {
        int startHour = convertTo24Hour(selectedStartTime);
        int startMinute = getMinutes(selectedStartTime);
        int endHour = convertTo24Hour(selectedEndTime);
        int endMinute = getMinutes(selectedEndTime);
        // Convert start and end times to total minutes since midnight
        int startTotalMinutes = startHour * 60 + startMinute;
        int endTotalMinutes = endHour * 60 + endMinute;
        // Calculate the duration in minutes
        int totalMinutes = endTotalMinutes - startTotalMinutes;
        // Ensure the duration is positive
        if (totalMinutes <= 0) {
            return null;
        }
        // Convert total minutes into hours and minutes
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        // Build the duration string
        StringBuilder duration = new StringBuilder();
        if (hours > 0) {
            duration.append(hours).append(" hour");
            if (hours > 1) {
                duration.append("s");
            }
        }
        if (minutes > 0) {
            if (hours > 0) {
                duration.append(" ");
            }
            duration.append(minutes).append(" minute");
            if (minutes > 1) {
                duration.append("s");
            }
        }
        return duration.toString();
    }


    private void setupStartTimeListener() {
        startTimeDropdown.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                // Get the selected start time
                String selectedStartTime = timeSlots.get(position);
                // Filter the end time options to only show times after the selected start time
                List<String> filteredEndTimes = timeSlots.subList(position + 1, timeSlots.size());
                ArrayAdapter<String> endTimeAdapter = new ArrayAdapter<>(Reservation.this, android.R.layout.simple_spinner_item, filteredEndTimes);
                endTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                endTimeDropdown.setAdapter(endTimeAdapter);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // Do nothing
            }
        });
    }
}
