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
    private Button goBackButton, bookNowButton;
    private List<String> timeSlots;
    private String selectedDate;
    private List<Boolean> availability;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        // Initialize UI components
        parkName = findViewById(R.id.park_name);
        calendarView = findViewById(R.id.calendar_view);
        courtDropdown = findViewById(R.id.court_dropdown);
        startTimeDropdown = findViewById(R.id.startTimeDropdown);
        endTimeDropdown = findViewById(R.id.endTimeDropdown);
        goBackButton = findViewById(R.id.go_back_button);
        bookNowButton = findViewById(R.id.book_now_button);
        // Get data from intent
        Intent intent = getIntent();
        String namePark = intent.getStringExtra("park_name");
        String timings = intent.getStringExtra("timings");
        int numOfCourts = intent.getIntExtra("num_of_courts", 0);
        parkName.setText(namePark);
        // Initialize date
        setCalendarMinDate();
        setInitialSelectedDate();
        // Handle date selection
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);

        });
        // Populate dropdowns
        populateTimeSpinners(timings);
        populateCourtDropdown(numOfCourts);
        // If modifying an existing booking, set the selected court, date, and times
        if (intent.hasExtra("selectedCourt")) {
            String selectedCourt = intent.getStringExtra("selectedCourt");
            String selectedStartTime = intent.getStringExtra("selectedStartTime");
            String selectedEndTime = intent.getStringExtra("selectedEndTime");
            // Update the UI with the passed booking details
            courtDropdown.setSelection(((ArrayAdapter<String>) courtDropdown.getAdapter()).getPosition(selectedCourt));
            startTimeDropdown.setSelection(((ArrayAdapter<String>) startTimeDropdown.getAdapter()).getPosition(selectedStartTime));
            endTimeDropdown.setSelection(((ArrayAdapter<String>) endTimeDropdown.getAdapter()).getPosition(selectedEndTime));
        }
            // Set listeners
            goBackButton.setOnClickListener(v -> finish());
            bookNowButton.setOnClickListener(v -> {
                String selectedCourt = courtDropdown.getSelectedItem().toString();
                String selectedStartTime = startTimeDropdown.getSelectedItem().toString();
                String selectedEndTime = endTimeDropdown.getSelectedItem().toString();
                String selectedParkName = parkName.getText().toString();

                int startMinutes = convertToMinutesSinceMidnight(selectedStartTime);
                int endMinutes = convertToMinutesSinceMidnight(selectedEndTime);

                if (BookingManager.isCourtBooked(selectedParkName, selectedCourt, selectedDate, startMinutes, endMinutes)) {
                    Toast.makeText(this, "Court is already booked for the selected time range", Toast.LENGTH_SHORT).show();
                    return;
                }
                // If modifying an existing booking, delete the old one before adding the new one
                if (intent.hasExtra("selectedCourt")) {
                    String oldCourt = intent.getStringExtra("selectedCourt");
                    String oldStartTime = intent.getStringExtra("selectedStartTime");
                    String oldEndTime = intent.getStringExtra("selectedEndTime");
                    BookingManager.deleteBooking(selectedParkName, oldCourt, selectedDate, convertToMinutesSinceMidnight(oldStartTime), convertToMinutesSinceMidnight(oldEndTime));
                }
                // Add the booking
                BookingManager.addBooking(selectedParkName, selectedCourt, selectedDate, startMinutes, endMinutes);

                updateAvailability(selectedParkName, selectedCourt);
                // Notify user and go back to map
                Toast.makeText(this, "Booking Confirmed!", Toast.LENGTH_SHORT).show();
                Intent mapIntent = new Intent(Reservation.this, Map.class);
                mapIntent.putExtra("selectedCourt", selectedCourt);
                mapIntent.putExtra("selectedStartTime", selectedStartTime);
                mapIntent.putExtra("selectedEndTime", selectedEndTime);
                mapIntent.putExtra("selectedDate", selectedDate);
                mapIntent.putExtra("parkName", parkName.getText().toString());
                mapIntent.putExtra("duration", calculateDuration(selectedStartTime, selectedEndTime));
                startActivity(mapIntent);
            });
        setupStartTimeListener();
    }

    private void setCalendarMinDate() {
        calendarView.setMinDate(Calendar.getInstance().getTimeInMillis());
    }
    private void setInitialSelectedDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        selectedDate = String.format("%04d-%02d-%02d", year, month + 1, day); // YYYY-MM-DD
    }
    private void populateTimeSpinners(String timings) {
        String[] timeRange = timings.split(" - ");
        if (timeRange.length != 2) {
            Toast.makeText(this, "Invalid timing format", Toast.LENGTH_SHORT).show();
            return;
        }
        String startTime = convertTo24HourFormat(timeRange[0]);
        String endTime = convertTo24HourFormat(timeRange[1]);
        if (startTime == null || endTime == null) {
            Toast.makeText(this, "Invalid timing format", Toast.LENGTH_SHORT).show();
            return;
        }
        // Generate time slots between start and end times
        timeSlots = generateTimeSlots(startTime, endTime);
        availability = new ArrayList<>();
        // Check availability for each time slot
        String selectedCourt = courtDropdown.getSelectedItem() != null ? courtDropdown.getSelectedItem().toString() : "";
        String selectedParkName = parkName.getText().toString();
        for (String timeSlot : timeSlots) {
            boolean isAvailable = !BookingManager.isCourtBooked(
                    selectedParkName,
                    selectedCourt,
                    selectedDate,
                    convertToMinutesSinceMidnight(timeSlot),
                    convertToMinutesSinceMidnight(timeSlot) + 30
            );
            availability.add(isAvailable);
        }
        // Set up adapters for both start and end time spinners
        TimeSlotAdapter timeAdapter = new TimeSlotAdapter(this, android.R.layout.simple_spinner_item, timeSlots, availability);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startTimeDropdown.setAdapter(timeAdapter);
        endTimeDropdown.setAdapter(timeAdapter);
    }

    private String convertTo24HourFormat(String time) {
        try {
            time = time.toUpperCase().trim(); // Normalize case
            String[] parts = time.split(" ");
            if (parts.length != 2) return null;
            String[] timeParts = parts[0].split(":");
            int hours = Integer.parseInt(timeParts[0]);
            int minutes = timeParts.length == 2 ? Integer.parseInt(timeParts[1]) : 0;
            String meridian = parts[1];
            if (meridian.equals("PM") && hours != 12) {
                hours += 12;
            } else if (meridian.equals("AM") && hours == 12) {
                hours = 0;
            }
            return String.format("%02d:%02d", hours, minutes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private List<String> generateTimeSlots(String startTime, String endTime) {
        List<String> slots = new ArrayList<>();
        int startMinutes = convertToMinutesSinceMidnight(startTime);
        int endMinutes = convertToMinutesSinceMidnight(endTime);

        for (int minutes = startMinutes; minutes < endMinutes; minutes += 30) {
            slots.add(convertMinutesToTime(minutes));
        }

        return slots;
    }
    private int convertToMinutesSinceMidnight(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }

    private String convertMinutesToTime(int minutes) {
        int hours = minutes / 60;
        int remainingMinutes = minutes % 60;
        return String.format("%02d:%02d", hours, remainingMinutes);
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

//    private void setButtonListeners() {
//        goBackButton.setOnClickListener(v -> finish());
//        bookNowButton.setOnClickListener(v -> {
//            String selectedCourt = courtDropdown.getSelectedItem().toString();
//            String selectedStartTime = startTimeDropdown.getSelectedItem().toString();
//            String selectedEndTime = endTimeDropdown.getSelectedItem().toString();
//            String selectedParkName = parkName.getText().toString();
//
//            int startMinutes = convertToMinutesSinceMidnight(selectedStartTime);
//            int endMinutes = convertToMinutesSinceMidnight(selectedEndTime);
//
//            if (BookingManager.isCourtBooked(selectedParkName, selectedCourt, selectedDate, startMinutes, endMinutes)) {
//                Toast.makeText(this, "Court is already booked for the selected time range", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            // If modifying an existing booking, delete the old one before adding the new one
//            if (intent.hasExtra("selectedCourt")) {
//                String oldCourt = intent.getStringExtra("selectedCourt");
//                String oldStartTime = intent.getStringExtra("selectedStartTime");
//                String oldEndTime = intent.getStringExtra("selectedEndTime");
//                BookingManager.deleteBooking(selectedParkName, oldCourt, selectedDate, convertToMinutesSinceMidnight(oldStartTime), convertToMinutesSinceMidnight(oldEndTime));
//            }
//            // Add the booking
//            BookingManager.addBooking(selectedParkName, selectedCourt, selectedDate, startMinutes, endMinutes);
//
//            updateAvailability(selectedParkName, selectedCourt);
//            // Notify user and go back to map
//            Toast.makeText(this, "Booking Confirmed!", Toast.LENGTH_SHORT).show();
//            Intent mapIntent = new Intent(Reservation.this, Map.class);
//            mapIntent.putExtra("selectedCourt", selectedCourt);
//            mapIntent.putExtra("selectedStartTime", selectedStartTime);
//            mapIntent.putExtra("selectedEndTime", selectedEndTime);
//            mapIntent.putExtra("selectedDate", selectedDate);
//            mapIntent.putExtra("parkName", parkName.getText().toString());
//            startActivity(mapIntent);
//        });
//    }
    private void updateAvailability(String selectedParkName, String selectedCourt) {
        for (int i = 0; i < timeSlots.size(); i++) {
            int slotStart = convertToMinutesSinceMidnight(timeSlots.get(i));
            int slotEnd = (i + 1 < timeSlots.size())
                    ? convertToMinutesSinceMidnight(timeSlots.get(i + 1))
                    : slotStart + 30;

            // Update availability based on whether the court is booked or not
            availability.set(i, !BookingManager.isCourtBooked(selectedParkName, selectedCourt, selectedDate, slotStart, slotEnd));
        }

        // Refresh the spinners with the updated availability
        TimeSlotAdapter adapter = new TimeSlotAdapter(this, android.R.layout.simple_spinner_item, timeSlots, availability);
        startTimeDropdown.setAdapter(adapter);
        endTimeDropdown.setAdapter(adapter);
    }



    private String calculateDuration(String selectedStartTime, String selectedEndTime) {
        int startMinutes = convertToMinutesSinceMidnight(selectedStartTime);
        int endMinutes = convertToMinutesSinceMidnight(selectedEndTime);

        if (endMinutes <= startMinutes) return null;
        return String.format("%d minutes", endMinutes - startMinutes);
    }
    private void setupStartTimeListener() {
        startTimeDropdown.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                // Reset the available end times based on the new selected start time
                updateEndTimeDropdown(position);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // Optional: Handle if no item is selected
            }
        });
    }

    private void updateEndTimeDropdown(int startPosition) {
        // Clear the current end time selections
        List<String> filteredEndTimes = new ArrayList<>();
        List<Boolean> filteredAvailability = new ArrayList<>();

        // Populate the available end times based on the selected start time
        for (int i = startPosition + 1; i < timeSlots.size(); i++) {
            filteredEndTimes.add(timeSlots.get(i));
            filteredAvailability.add(availability.get(i));
        }

        // If no end times are available (which shouldn't normally happen), show a message
        if (filteredEndTimes.isEmpty()) {
            Toast.makeText(this, "No valid end times available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Set the adapter for the end time dropdown
        TimeSlotAdapter endTimeAdapter = new TimeSlotAdapter(this, android.R.layout.simple_spinner_item, filteredEndTimes, filteredAvailability);
        endTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endTimeDropdown.setAdapter(endTimeAdapter);

        // Optional: Automatically select the first valid end time
        endTimeDropdown.setSelection(0);
    }

}