package com.example.task91p;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.task91p.databinding.ActivityMapsaveBinding;

import java.util.ArrayList;
import java.util.List;

public class MapsaveActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsaveBinding binding;

    double latitude, longitude;

    EditText placelocation;
    String placename;

    DatabaseClass databaseClass;
    List<Model> locationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        placelocation = findViewById(R.id.placelocation);

        binding = ActivityMapsaveBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("lat", 0);
        longitude = intent.getDoubleExtra("long", 0);
        placename = intent.getStringExtra("place");

        databaseClass = new DatabaseClass(this);
        locationList = new ArrayList<>();
        databaseClass = new DatabaseClass(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng deakin = new LatLng(-37.84781762835067, 145.11489996471462);
        LatLng melbourne = new LatLng(-37.812669, 144.962404);
        //mMap.addMarker(new MarkerOptions().position(deakin).title("Deakin"));
        mMap.addMarker(new MarkerOptions().position(melbourne).title("Melbourne"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(deakin));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(melbourne));
    }
}