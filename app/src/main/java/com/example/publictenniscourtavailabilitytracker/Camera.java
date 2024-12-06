package com.example.publictenniscourtavailabilitytracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
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
    String ID;

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

        //remove those annoying things
        QRid.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        //make the file and read from it
        createFile();
        readFile();

        //set OnClickListener for the button
        submitID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the text entered by the user
                String userInput = QRid.getText().toString();

                if(userInput.equals(ID)){
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

        //Initialize the scanner
        GmsBarcodeScannerOptions options = initializeGoogleScanner();
        scanner = GmsBarcodeScanning.getClient(this, options);

        //set up back button functionality
        ImageView backButton = findViewById(R.id.backBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish(); //closes the current activity and navigates back
            }
        });



    }

    private void installGoogleScanner() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        //Check if Google Play services is available
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode == com.google.android.gms.common.ConnectionResult.SUCCESS) {
            //Google Play Services is available
            isScannerInstalled = true;
        } else {
            //Google Play Services is not available
            isScannerInstalled = false;
            StyleableToast.makeText(Camera.this, "Something went wrong ! Try again", R.style.exampleToast).show();
        }
    }
    //for barcode
    private GmsBarcodeScannerOptions initializeGoogleScanner() {
        return new GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .enableAutoZoom()
                .build();
    }

    private void startScanning() {
        if (isScannerInstalled) {
            //start scanning immediately
            scanner.startScan()
                    .addOnSuccessListener(barcode -> {
                        scannedValue = barcode.getRawValue();
                        //does barcode equal the id it is supposed to
                        if (scannedValue.equals(ID)) {
                            Intent intent = new Intent(Camera.this, PlayingGame.class);
                            intent.putExtra("ParkName", ParkName);
                            intent.putExtra("courtId", CourtId);
                            startActivity(intent);
                        }else{
                            //if wrong qr code is scanned
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

    //creates the file
    private void createFile() {
        String data = "Kinsmen Park|P1XD\n" +
                "Hartwick Park|G5T3\n" +
                "Parkinson Rec|M7HO\n" +
                "Birkdale Park|1|L9J1\n" +
                "City Park|M8B4\n" +
                "Blair Pond Park|W3K6\n" +
                "Gerstmar Park|V7Y3\n" +
                "Summerside Park|H8Z5\n";

        //write to a file
        FileOutputStream fos = null;
        try {
            //create a file internally
            File file = new File(getFilesDir(), "QRid_data.txt");
            fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void readFile() {
        File file = new File(getFilesDir(), "QRid_data.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("|")) {
                    String[] parts = line.split("\\|", 2);
                    //if its at the park that user selected, get the qr id from it
                    if (ParkName.equals(parts[0])) {
                        ID = parts[1];
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}