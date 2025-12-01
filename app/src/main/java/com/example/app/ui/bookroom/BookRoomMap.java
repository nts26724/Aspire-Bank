package com.example.app.ui.bookroom;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.List;

public class BookRoomMap extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LatLng selectedLatLng;
    private FusedLocationProviderClient fusedLocationClient;
    private CardView find;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.book_room_map);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.book_room_map), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Places.initialize(getApplicationContext(),
                getString(R.string.google_api_key)
        );


        find.setOnClickListener(v -> {
            if(selectedLatLng == null) {
                Toast.makeText(this,
                        "Vui lòng chọn vị trí trên bản đồ", Toast.LENGTH_SHORT);
            } else {
                Intent intentSource = getIntent();

                Intent intentBookRoomList = new Intent(this, BookRoomList.class);
                intentBookRoomList.putExtra("latitude", selectedLatLng.latitude + "");
                intentBookRoomList.putExtra("longitude", selectedLatLng.longitude + "");

//                intentBookRoomList.putExtra("latitude", 35.6895 + "");
//                intentBookRoomList.putExtra("longitude", 139.6917 + "");


                intentBookRoomList.putExtra("checkInDate",
                        intentSource.getStringExtra("checkInDate"));

                intentBookRoomList.putExtra("checkOutDate",
                        intentSource.getStringExtra("checkOutDate"));

                intentBookRoomList.putExtra("quantityPeople",
                        intentSource.getStringExtra("quantityPeople"));

                intentBookRoomList.putExtra("quantityRoom",
                        intentSource.getStringExtra("quantityRoom"));

                intentBookRoomList.putExtra("radius",
                        intentSource.getStringExtra("radius"));

                startActivity(intentBookRoomList);
            }
        });
    }


    public void init() {
        find = findViewById(R.id.find);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        loadCurrentLocation();
        enablePickLocation();
    }


    private void loadCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        mMap.setMyLocationEnabled(true);

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                LatLng myLoc = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 15));
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadCurrentLocation();
            }
        }
    }



    private void enablePickLocation() {
        mMap.setOnMapClickListener(latLng -> {
            selectedLatLng = latLng;

            Log.d("longitude", latLng.longitude + "");
            Log.d("latitude", latLng.latitude + "");

            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng));
        });
    }
}
