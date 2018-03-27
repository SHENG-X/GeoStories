package com.example.shengx.geostories;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final int REQ_LOC_CODE=100;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                Log.d("Location",point.latitude+"******"+point.longitude);
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(point));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(point));

            }
        });
        if(checkLocationPermission()){
                mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        latitude=location.getLatitude();
                        longitude=location.getLongitude();
                        Log.d("Location",latitude+"******"+longitude);
                        // Add a marker in Sydney and move the camera
                        LatLng clientLocation = new LatLng(latitude, longitude);
                        mMap.addMarker(new MarkerOptions().position(clientLocation).title("My Location"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(clientLocation));
                    }
                });
        }


    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.postgeostory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent=new Intent(this,MainActivity.class);
        switch (item.getItemId()) {
            case R.id.post:
                startActivity(intent);
                return true;
            default:
                return  super.onOptionsItemSelected(item);
        }
    }


    public boolean checkLocationPermission(){
        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},REQ_LOC_CODE);
        int locationPermissionCode=ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION);
        return locationPermissionCode == PackageManager.PERMISSION_GRANTED;
    }


}
