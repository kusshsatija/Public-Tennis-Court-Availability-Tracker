package com.example.publictenniscourtavailabilitytracker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.moduleinstall.ModuleInstall;
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

public class Camera extends AppCompatActivity {

    private Button scanQrBtn;
    private TextView scannedValueTv;
    private boolean isScannerInstalled = false;
    private GmsBarcodeScanner scanner;

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
        initVars();
        registerUiListener();
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
            Toast.makeText(this, "Google Play Services not available: " + resultCode, Toast.LENGTH_SHORT).show();
        }
    }

    private void initVars() {
        scanQrBtn = findViewById(R.id.scanQrBtn);
        scannedValueTv = findViewById(R.id.scannedValueTv);

        GmsBarcodeScannerOptions options = initializeGoogleScanner();
        scanner = GmsBarcodeScanning.getClient(this, options);
    }

    private GmsBarcodeScannerOptions initializeGoogleScanner() {
        return new GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .enableAutoZoom()
                .build();
    }

    private void registerUiListener() {
        scanQrBtn.setOnClickListener(v -> {
            if (isScannerInstalled) {
                startScanning();
            } else {
                Toast.makeText(this, "Please try again...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startScanning() {
        scanner.startScan()
                .addOnSuccessListener(barcode -> {
                    String result = barcode.getRawValue();
                    if (result != null) {
                        scannedValueTv.setText("Scanned Value: " + result);
                    }
                })
                .addOnCanceledListener(() -> Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
