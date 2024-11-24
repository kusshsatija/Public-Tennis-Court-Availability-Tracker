package com.example.publictenniscourtavailabilitytracker;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.publictenniscourtavailabilitytracker.databinding.ActivityMapBinding;

public class Map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

    }
}