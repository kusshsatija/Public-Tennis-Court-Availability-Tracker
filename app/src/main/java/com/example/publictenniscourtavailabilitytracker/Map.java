package com.example.publictenniscourtavailabilitytracker;

import androidx.fragment.app.FragmentActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.publictenniscourtavailabilitytracker.databinding.ActivityMapBinding;


public class Map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapBinding binding;

    // Variables to store booking details
    private String parkName;
    private String selectedDate;
    private String selectedStartTime;
    private String selectedEndTime;
    private String courtName;
    private String duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the booking details from the Intent
        Intent intent = getIntent();
        if (intent != null) {
            parkName = intent.getStringExtra("parkName");
            selectedDate = intent.getStringExtra("selectedDate");
            selectedStartTime = intent.getStringExtra("selectedStartTime");
            selectedEndTime = intent.getStringExtra("selectedEndTime");
            courtName = intent.getStringExtra("selectedCourt");
            duration = intent.getStringExtra("duration");
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Show the booking confirmation dialog after loading booking details
        if (courtName != null) {
            showBookingConfirmationDialog(courtName);
        }






    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //locations
        //locations found from https://www.okgntennis.com/courts-clubs
        LatLng kelowna = new LatLng(49.8801, -119.4436);
        LatLng KinsmenPark = new LatLng(49.8677, -119.4952);
        LatLng ParkinsonRec = new LatLng(49.8854, -119.4580);
        LatLng Birkdale = new LatLng(49.88587543146492, -119.34766510014263);
        LatLng CityPark = new LatLng(49.88448865902623, -119.50197352873013);
        LatLng BlairPond = new LatLng(49.91734070559104, -119.46629420033803);
        LatLng HartwickPark = new LatLng(49.8964, -119.4661);
        LatLng Gerstmar = new LatLng(49.877187793935605, -119.40902718869114);
        LatLng Summerside = new LatLng(49.84293207503996, -119.38971318308698);


        //different coloured markers
        BitmapDescriptor orangeMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
        BitmapDescriptor greenMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        BitmapDescriptor redMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);

        //adding markers
        //TO-DO add .icon() appropriately depending on availability level
        //example: mMap.addMarker(new MarkerOptions().position(KinsmenPark).title("Kinsmen Park").icon(greenMarker); if courts are available
        // Adding markers with address in snippet
        mMap.addMarker(new MarkerOptions().position(KinsmenPark).title("Kinsmen Park"));
        mMap.addMarker(new MarkerOptions().position(HartwickPark).title("Hartwick Park"));
        mMap.addMarker(new MarkerOptions().position(ParkinsonRec).title("Parkinson Rec"));
        mMap.addMarker(new MarkerOptions().position(Birkdale).title("Birkdale Park"));
        mMap.addMarker(new MarkerOptions().position(CityPark).title("City Park"));
        mMap.addMarker(new MarkerOptions().position(BlairPond).title("Blair Pond Park"));
        mMap.addMarker(new MarkerOptions().position(Gerstmar).title("Gerstmar Park"));
        mMap.addMarker(new MarkerOptions().position(Summerside).title("Summerside Park"));

        //adding zoom buttons
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //setting up camera in kelowna and zoom
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kelowna, 11));

        // Marker click listener to show dialog
        mMap.setOnMarkerClickListener(marker -> {
            String courtName = marker.getTitle();

            // Show Court Details Dialog
            CourtDetailsDialog dialog = CourtDetailsDialog.newInstance(courtName);
            dialog.show(getSupportFragmentManager(), "CourtDetailsDialog");

            return true;
        });

    }
    private void showBookingConfirmationDialog(String courtTitle) {
        // Create a dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_booking_confirmation);
        // Set dialog views
        TextView dialogCourtName = dialog.findViewById(R.id.dialog_court);
        TextView dialogDate = dialog.findViewById(R.id.dialog_date);
        TextView dialogTimeDetails = dialog.findViewById(R.id.dialog_time);
        TextView dialogParkName = dialog.findViewById(R.id.dialog_park_name);
        // Buttons in the dialog
        TextView closeButton = dialog.findViewById(R.id.dialog_close_button);
        TextView modifyBookingButton = dialog.findViewById(R.id.dialog_modify_button);
        TextView cancelBookingButton = dialog.findViewById(R.id.dialog_cancel_button);
        // Combine start time, end time, and duration into one string
        String timeDetails = selectedStartTime + " - " + selectedEndTime + ", " + duration;
        // Populate the dialog with booking details
        if (courtTitle != null && !courtTitle.isEmpty()) {
            dialogCourtName.setText("Court: " + courtTitle);
        } else {
            dialogCourtName.setText("Court: Not provided");
        }
        if (selectedDate != null && !selectedDate.isEmpty()) {
            dialogDate.setText("Date: " + selectedDate);
        } else {
            dialogDate.setText("Date: Not provided");
        }
        if (timeDetails != null && !timeDetails.isEmpty()) {
            dialogTimeDetails.setText(timeDetails);
        } else {
            dialogTimeDetails.setText("Time: Not provided");
        }
        if (parkName != null && !parkName.isEmpty()) {
            dialogParkName.setText("Park: " + parkName);
        } else {
            dialogParkName.setText("Park: Not provided");
        }
        // Handle "Close" button click
        closeButton.setOnClickListener(v -> dialog.dismiss());
        // Handle "Modify Booking" button click
        modifyBookingButton.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(Map.this, Reservation.class);
            intent.putExtra("selectedCourt", courtTitle); // Pass selected court info for editing
            intent.putExtra("selectedStartTime", selectedStartTime); // Pass the selected start time
            intent.putExtra("selectedEndTime", selectedEndTime); // Pass the selected end time
            intent.putExtra("selectedDate", selectedDate); // Pass the selected date
            intent.putExtra("parkName", parkName); // Pass park name
            startActivity(intent);
        });
        // Handle "Cancel Booking" button click
        cancelBookingButton.setOnClickListener(v -> {
            dialog.dismiss();

            // Assuming the selectedCourt, selectedStartTime, selectedEndTime, and selectedDate are passed to this dialog
            String selectedCourt = getIntent().getStringExtra("selectedCourt");
            String selectedStartTime = getIntent().getStringExtra("selectedStartTime");
            String selectedEndTime = getIntent().getStringExtra("selectedEndTime");
            String selectedDate = getIntent().getStringExtra("selectedDate");
            String parkName = getIntent().getStringExtra("parkName");

            // Convert start and end time to minutes since midnight (same as your other time conversions)
            int startMinutes = convertToMinutesSinceMidnight(selectedStartTime);
            int endMinutes = convertToMinutesSinceMidnight(selectedEndTime);

            // Cancel the booking by calling the delete method
            BookingManager.deleteBooking(parkName, selectedCourt, selectedDate, startMinutes, endMinutes);

            // Notify the user
            Toast.makeText(Map.this, "Booking cancelled", Toast.LENGTH_SHORT).show();

            // Optionally, return to Map or any other relevant activity
            Intent intent = new Intent(Map.this, Map.class);
            startActivity(intent);
        });
        // Show the dialog
        dialog.show();
    }
    private int convertToMinutesSinceMidnight(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }



}