package com.example.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EmergencymsgSend extends AppCompatActivity {

    private Toolbar toolbar;
    Button emrcnybtn,getlction;
    EditText latitude,longitude,phonenumber,emrgncymsg;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0;
    private ListView listView;
    private numberAdaptor numberAdaptor;
    List<contactInfo> contactInfoList;
    private DatabaseReference databaseReference;
    String getlatitude;
    String getlongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergencymsg_send);
        latitude=findViewById(R.id.latitude);
        longitude=findViewById(R.id.longitude);
        phonenumber=findViewById(R.id.number);
        getlction=findViewById(R.id.crntlocation);
        emrcnybtn=findViewById(R.id.sendsos);
        listView=findViewById(R.id.listview3);

        toolbar=findViewById(R.id.layout_bar);

        setSupportActionBar(toolbar);

        databaseReference = FirebaseDatabase.getInstance().getReference("contactInfo");
        contactInfoList=new ArrayList<>();

        numberAdaptor=new numberAdaptor(EmergencymsgSend.this,contactInfoList);

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(EmergencymsgSend.this);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {

                contactInfoList.clear();
                for(DataSnapshot dataSnapshot1: snapshot.getChildren()) {

                    contactInfo contactInfo = dataSnapshot1.getValue(contactInfo.class);
                    contactInfoList.add(contactInfo);

                }
                listView.setAdapter(numberAdaptor);
            }

            @Override
            public void onCancelled( DatabaseError error) {

                Toast.makeText(EmergencymsgSend.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        getlction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(EmergencymsgSend.this,Manifest.permission.ACCESS_FINE_LOCATION)
                        ==PackageManager.PERMISSION_GRANTED  && ActivityCompat.checkSelfPermission(EmergencymsgSend.this,Manifest.permission
                        .ACCESS_COARSE_LOCATION) ==PackageManager.PERMISSION_GRANTED){

                    getcurrentLocation();
                }else{
                    ActivityCompat.requestPermissions(EmergencymsgSend.this,new String[]{Manifest.permission
                            .ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSIONS_REQUEST_SEND_SMS);
                }

            }
        });

        emrcnybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSOS();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void sendSOS() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS))
        {
            msgSend();
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
    }

    private void msgSend() {
        getlatitude=latitude.getText().toString();
        getlongitude=longitude.getText().toString();

        String phoneNo = phonenumber.getText().toString();


        SmsManager smsManager = SmsManager.getDefault();
        String msg = "Hey,,,I am in danger,Please help me! My Location is: http://maps.google.com/?q=" + getlatitude + "," + getlongitude;
        smsManager.sendTextMessage(phoneNo, null, msg, null, null);
        Toast.makeText(getApplicationContext(), "SMS Sent", Toast.LENGTH_LONG).show();
    }



    @SuppressLint("MissingPermission")
    private void getcurrentLocation() {
        LocationManager locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Location> task) {

                    Location location=task.getResult();
                    if(location!=null){
                        latitude.setText(String.valueOf(location.getLatitude()));
                        longitude.setText(String.valueOf(location.getLongitude()));
                    }else {
                        LocationRequest locationRequest=new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);
                        LocationCallback locationCallback=new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull @NotNull LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                Location location1=locationResult.getLastLocation();

                            }
                        };
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback
                                , Looper.myLooper());
                    }
                }
            });
        }else{
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getcurrentLocation();
                    msgSend();

                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }
}