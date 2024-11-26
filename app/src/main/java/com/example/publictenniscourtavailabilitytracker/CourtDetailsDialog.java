package com.example.publictenniscourtavailabilitytracker;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.util.HashMap;

public class CourtDetailsDialog extends DialogFragment {
    // Map to store court details, keyed by the court name
    private static final HashMap<String, String[]> courtDetailsMap = new HashMap<>();

    // Static block to initialize the court details map
    static {
        courtDetailsMap.put("Kinsmen Park", new String[]{"2600 Abbott St, Kelowna, BC V1Y 1G4", "4", "6 AM - 10 PM", "20", "Avaliable"});
        courtDetailsMap.put("Hartwick Park", new String[]{"1468 Lambert Ave, Kelowna, BC V1Y 7H6", "2", "6 AM - 8 PM", "30", "Avaliable"});
        courtDetailsMap.put("Parkinson Rec", new String[]{"1800 Parkinson Way, Kelowna, BC V1Y 4P9", "2", "6 AM - 11 PM", "50", "YeAvaliables"});
        courtDetailsMap.put("Birkdale Park", new String[]{"363 Prestwick St, Kelowna, BC V1P 1R7", "2", "7 AM - 9 PM", "10", "Not Avaliable"});
        courtDetailsMap.put("City Park", new String[]{"1600 Abbott St, Kelowna, BC V1Y 1B7", "6", "6 AM - 10 PM", "30", "Avaliable"});
        courtDetailsMap.put("Blair Pond Park", new String[]{"333 Clifton Rd, Kelowna, BC V1V 1A4", "2", "8 AM - 10 PM", "15", "Avaliable"});
        courtDetailsMap.put("Gerstmar Park", new String[]{"955 Gerstmar Rd, Kelowna, BC V1X 4B8", "1", "8 AM - 8 PM", "35", "Not Avaliable"});
        courtDetailsMap.put("Summerside Park", new String[]{"3858 Summerside Dr, Kelowna, BC V1W 3Z6", "4", "6 AM - 12 PM", "5", "Avaliable"});
    }


    private String courtName;

    // Method to create a new instance of the dialog with the court name
    public static CourtDetailsDialog newInstance(String name) {
        CourtDetailsDialog dialog = new CourtDetailsDialog();
        Bundle args = new Bundle();
        args.putString("court_name", name); // Pass the court name to the dialog
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_court_details, container, false);

        // Retrieve the court name from the passed arguments
        if (getArguments() != null) {
            courtName = getArguments().getString("court_name");
        }

        // Get the court details from the map using the court name
        String[] courtDetails = courtDetailsMap.get(courtName);

        // Populate the UI with the court details
        TextView courtNameText = view.findViewById(R.id.court_name);
        TextView courtAddressText = view.findViewById(R.id.court_address);
        TextView numberOfCourtsText = view.findViewById(R.id.number_of_courts);
        TextView timingText = view.findViewById(R.id.timing);
        TextView parkingText = view.findViewById(R.id.parking_spots);
        TextView washroomText = view.findViewById(R.id.washroom_available);

        if (courtDetails != null) {
            courtNameText.setText(courtName);
            courtAddressText.setText(courtDetails[0]);
            numberOfCourtsText.setText(courtDetails[1]);
            timingText.setText(courtDetails[2]);
            parkingText.setText(courtDetails[3]);
            washroomText.setText(courtDetails[4]);
        }

        Button bookNowButton = view.findViewById(R.id.book_now_button);
        bookNowButton.setOnClickListener(v -> {
            // Handle the "Book Now" button click
            dismiss(); // Dismiss the dialog after action
        });

        Button checkAvailabilityButton = view.findViewById(R.id.check_availability_button);
        checkAvailabilityButton.setOnClickListener(v -> {
            // Create an Intent to start the new activity
            Intent intent = new Intent(requireContext(), AvailabilityPage.class);

            // Optionally, pass the court name or other details to the new activity
            intent.putExtra("park_name", courtName);
            Integer numofCourts = Integer.valueOf(courtDetails[1]);
            intent.putExtra("numofCourts", numofCourts);
            // Start the activity
            startActivity(intent);

            // Handle the "Check Availability" button click
            dismiss(); // Dismiss the dialog after action
        });

        Button commentButton = view.findViewById(R.id.commentsButton);
        commentButton.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), ReadComments.class);
            intent.putExtra("courtName", courtName);
            startActivity(intent);
            dismiss();
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Adjust dialog size (optional)
        if (getDialog() != null) {
            Dialog dialog = getDialog();
            dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.9),
                    (int) (getResources().getDisplayMetrics().heightPixels * 0.67));
        }
    }





}
