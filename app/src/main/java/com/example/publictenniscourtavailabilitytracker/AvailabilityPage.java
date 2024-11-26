package com.example.publictenniscourtavailabilitytracker;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AvailabilityPage extends AppCompatActivity {
    TextView ParkName;
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
        //get court detail values
        String name = getIntent().getStringExtra("park_name");
        int numofCourts = getIntent().getIntExtra("numofCourts", 0);

        //generate appropriate courts
        generateCourtTextViews(numofCourts);

        //set the appropriate park name
        ParkName = findViewById(R.id.park_name);
        ParkName.setText(name);


    }
    private void generateCourtTextViews(int count) {
        LinearLayout layout = findViewById(R.id.text_view_container);
        layout.removeAllViews(); // Clear any existing views

        for (int i = 0; i < count; i++) {
            TextView textView = new TextView(this);
            textView.setText("Court " + (i + 1));
            textView.setTextSize(16);
            textView.setPadding(10, 10, 10, 10);
            layout.addView(textView);
        }
    }
}