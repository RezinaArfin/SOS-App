package com.example.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15;

    private EditText inputSearchText;
    private ImageView gps;
    private Spinner spinnerType;
    private Button nearbyBtn;

    private Boolean isLocationPermissionGranted = false;
    private GoogleMap gMap;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "The Map Is Ready!", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "map is ready");
        gMap = googleMap;

        if (isLocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            gMap.setMyLocationEnabled(true);
            gMap.getUiSettings().setMyLocationButtonEnabled(false);

            init();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        inputSearchText = (EditText) findViewById(R.id.search_bar);
        gps = (ImageView)findViewById(R.id.ic_gps);
        spinnerType = findViewById(R.id.spinnerType);
        nearbyBtn = findViewById(R.id.nearbyBtn);
        getLocationPermission();

    }
    private void init(){
        Log.d(TAG, "initializing!");
        inputSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
                    geoLocate();
                    return true;
                }
                return false;
            }
        });

        gps.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d(TAG, "onClick: Clicked gps icon");
                getDeviceLocation();
            }
        });



        hideSoftKeyboard();
    }

    private void geoLocate(){
        Log.d(TAG, "geoLocating");
        String searchString = inputSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: "+ e.getMessage());
        }

        if(list.size()>0)
        {
            Address address = list.get(0);
            Log.d(TAG, "geoLocate: getting the devices current Location"+address.toString());

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()),
                    DEFAULT_ZOOM, address.getAddressLine(0));
        }
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the current device location");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(isLocationPermissionGranted)
            {
                final Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task task) {
                        Location currentLocation = null;
                        if(task.isSuccessful()){
                            Log.d(TAG, "OnComplete: Found User Location!");
                            try {
                                currentLocation = (Location) task.getResult();
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "Users Current Location");
                            }catch (NullPointerException e){
                                e.printStackTrace();
                            }
                            String[] placeTypeList = {"hospitals", "restaurants", "police stations", "atm", "bank"};
                            String[] placeNameList = {"Hospitals", "Restaurants", "Police Stations", "ATM", "Bank"};

                            spinnerType.setAdapter(new ArrayAdapter<>(MapActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item, placeNameList));

                            Location finalCurrentLocation = currentLocation;
                            nearbyBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int i = spinnerType.getSelectedItemPosition();
                                    String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" + "?location="
                                            + finalCurrentLocation.getLatitude() + "," + finalCurrentLocation.getLongitude() +
                                            "&radius=5000" + "&types=" + placeTypeList[i] + "&sensor = true" +
                                            "&key=" + getResources().getString(R.string.google_api_key);

                                    new placeTask().execute(url);
                                }
                            });
                        }
                        else
                        {
                            Log.d(TAG, "OnComplete: Location Null!");
                            Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }catch(SecurityException e){
            Log.d(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "move camera: Move camera to, lat:"+latLng.latitude+", lng:"+latLng.longitude);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if(!title.equals("Users Current Location")){
            MarkerOptions options = new MarkerOptions().position(latLng).title(title);
            gMap.addMarker(options);
        }
        hideSoftKeyboard();
    }

    private void initializeMap(){
        Log.d(TAG, "initializing the map");
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity.this);
    }

    private void getLocationPermission() {
        Log.d(TAG, "Location Permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                isLocationPermissionGranted = true;
                initializeMap();
            }
            else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults){
        Log.d(TAG, "This was Called");
        isLocationPermissionGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length>0){
                    for(int i=0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            isLocationPermissionGranted = false;
                            Log.d(TAG, "Permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "Permission Granted");
                    isLocationPermissionGranted = true;

                    initializeMap();
                }
            }
        }
    }
   private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

   }
   private class placeTask extends AsyncTask<String, Integer, String>{

       @Override
       protected String doInBackground(String... strings) {
           String data = null;
           try {
               data = downloadUrl(strings[0]);
           } catch (IOException e) {
               e.printStackTrace();
           }
           return data;
       }

       @Override
       protected void onPostExecute(String s) {
           new parserTask().execute(s);
       }
   }

   private String downloadUrl(String string) throws IOException{
        URL url = new URL(string);
       HttpURLConnection connection = (HttpURLConnection) url.openConnection();

       connection.connect();
       InputStream stream = connection.getInputStream();
       BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

       StringBuilder builder = new StringBuilder();
       String line = "";
       while((line=reader.readLine()) != null){
           builder.append(line);
       }
       String data = builder.toString();
       reader.close();

       return data;
   }

   private class parserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

       @Override
       protected List<HashMap<String, String>> doInBackground(String... strings) {
           JsonParser jasonParser = new JsonParser();
           List<HashMap<String, String>> mapList = null;
           JSONObject object = null;
           try {
               object = new JSONObject(strings[0]);
               mapList = jasonParser.parseResult(object);
           } catch (JSONException e) {
               e.printStackTrace();
           }
           return mapList;
       }

       @Override
       protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
           gMap.clear();
           for(int i=0; i<hashMaps.size(); i++){
               HashMap<String, String> hashMapList = hashMaps.get(i);
               double lat = Double.parseDouble(hashMapList.get("lat"));
               double lng = Double.parseDouble(hashMapList.get("lng"));
               String name = hashMapList.get("name");

               LatLng latLng = new LatLng(lat, lng);
               MarkerOptions options = new MarkerOptions();
               options.position(latLng);
               options.title(name);

               gMap.addMarker(options);
           }
       }
   }

}