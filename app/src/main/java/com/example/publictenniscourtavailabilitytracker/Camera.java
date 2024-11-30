package com.example.publictenniscourtavailabilitytracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

import java.io.FileOutputStream;
import java.io.IOException;

import io.github.muddz.styleabletoast.StyleableToast;

public class Camera extends AppCompatActivity {

    String scannedValue;
    private boolean isScannerInstalled = false;
    private GmsBarcodeScanner scanner;
    String ParkName;
    int CourtId;
    EditText QRid;
    Button submitID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_camera);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




        installGoogleScanner();
        //initialize
        ParkName = getIntent().getStringExtra("ParkName");
        CourtId = getIntent().getIntExtra("courtId", -1);
        QRid = findViewById(R.id.QReditText);
        submitID = findViewById(R.id.submit_button);

        // Set OnClickListener for the button
        submitID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text entered by the user
                String userInput = QRid.getText().toString();

                if(userInput.equals("PX2H")){
                    QRid.setText("");
                    Intent intent = new Intent(Camera.this, PlayingGame.class);
                    intent.putExtra("ParkName", ParkName);
                    intent.putExtra("courtId", CourtId);
                    startActivity(intent);
                }else{
                    StyleableToast.makeText(Camera.this, "Incorrect QR code, please try again.", R.style.exampleToast).show();


                }
            }
        });



        // Initialize the scanner
        GmsBarcodeScannerOptions options = initializeGoogleScanner();
        scanner = GmsBarcodeScanning.getClient(this, options);

        // Set up back button functionality
        ImageView backButton = findViewById(R.id.backBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish(); //closes the current activity and navigates back
            }
        });



    }

    private void installGoogleScanner() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        // Check if Google Play services is available
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode == com.google.android.gms.common.ConnectionResult.SUCCESS) {
            // Google Play Services is available
            isScannerInstalled = true;
        } else {
            // Google Play Services is not available
            isScannerInstalled = false;
            StyleableToast.makeText(Camera.this, "Google Play Services not available: "+ resultCode, R.style.exampleToast).show();
        }
    }



    private GmsBarcodeScannerOptions initializeGoogleScanner() {
        return new GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .enableAutoZoom()
                .build();
    }


    private void startScanning() {
        if (isScannerInstalled) {
            // Start scanning immediately
            scanner.startScan()
                    .addOnSuccessListener(barcode -> {
                        scannedValue = barcode.getRawValue();
                        if (scannedValue.equals("1")) {
                            Intent intent = new Intent(Camera.this, PlayingGame.class);
                            intent.putExtra("ParkName", ParkName);
                            intent.putExtra("courtId", CourtId);
                            startActivity(intent);
                        }else{
                            StyleableToast.makeText(Camera.this, "Incorrect QR code, please try again.", R.style.exampleToast).show();

                        }
                    })
                    .addOnCanceledListener(() -> Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            StyleableToast.makeText(Camera.this, "Google Play Services not available. Please install it to scan QR codes.", R.style.exampleToast).show();
        }
    }
    public void scan_qr (View v){
        startScanning();
    }





}