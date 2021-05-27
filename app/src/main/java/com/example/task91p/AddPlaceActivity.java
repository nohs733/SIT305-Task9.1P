package com.example.task91p;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLEngineResult;

public class AddPlaceActivity extends AppCompatActivity {

    Button locationbtn, showonmap, savebtn;
    FusedLocationProviderClient fusedLocationProviderClient;
    EditText placename, placelocation;
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        placename = findViewById(R.id.placename);
        placelocation = findViewById(R.id.placelocation);
        savebtn = findViewById(R.id.savebtn);

        locationbtn = findViewById(R.id.locationbtn);
        showonmap = findViewById(R.id.showonmap);

        Places.initialize(getApplicationContext(),"AIzaSyBvH3Ph9uJ7wUcF6BIo-SmzDmttRXssDD4");

        placelocation.setFocusable(false);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(placename.getText().toString()) &&
                        !TextUtils.isEmpty(placelocation.getText().toString())) {

                    DatabaseClass db = new DatabaseClass(AddPlaceActivity.this);
                    db.savebtn(placename.getText().toString(), placelocation.getText().toString());



                    Intent intent = new Intent(AddPlaceActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }else {
                    Toast.makeText(AddPlaceActivity.this, "Something goes wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        placelocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(AddPlaceActivity.this);
                startActivityForResult(intent, 100);
            }
        });

        locationbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){

                        fusedLocationProviderClient.getLastLocation()
                                .addOnSuccessListener(new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {

                                        if (location != null) {
                                            Double latitude = location.getLatitude();
                                            Double longitude = location.getLongitude();
                                            placelocation.setText(latitude + ", " + longitude);
                                        }
                                    }
                                });
                    }
                }else {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
            }
        });

        showonmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(AddPlaceActivity.this, MapsActivity.class);
                intent.putExtra("place", placename.getText().toString());
                intent.putExtra("lat", latitude);
                intent.putExtra("long", longitude);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);

            latitude = place.getLatLng().latitude;
            longitude = place.getLatLng().longitude;

            placelocation.setText(place.getAddress());
            placename.setText(place.getName());

        } else if (resultCode == AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(),status.getStatusMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
















