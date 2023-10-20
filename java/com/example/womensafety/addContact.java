package com.example.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class addContact extends AppCompatActivity {

    private static final int CONTACT_PERMISSION_CODE = 2;
    private FloatingActionButton addContact;
    public final int PICK_CONTACT = 2015;
    Button saveInfo;
    private TextView personname, personnum;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Toolbar toolbar;
    private FirebaseDatabase firebaseDatabase;
    private List<contactInfo>contactInfoList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        addContact = findViewById(R.id.add_person);

        personname = findViewById(R.id.person_name);
        personnum = findViewById(R.id.person_number);
        saveInfo=findViewById(R.id.save);


        toolbar = findViewById(R.id.layout_bar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        setSupportActionBar(toolbar);

        databaseReference = firebaseDatabase.getReference("contactInfo");


        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (checkContactPermission()) {

                    pickContact();

                } else {
                    requestContactPermission();
                }
            }


        });

        saveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String fullname = personname.getText().toString().trim();
                    String phoneNo = personnum.getText().toString().trim();

                    contactInfo(fullname,phoneNo);

                }

        });

    }


    private void contactInfo(String fullname,String phoneNo) {

        contactInfo contactInfo=new contactInfo(fullname,phoneNo);
        String key=databaseReference.push().getKey();
        databaseReference.child(key).setValue(contactInfo);
        Toast.makeText(this, "Person info saved.", Toast.LENGTH_SHORT).show();


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void pickContact() {
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(i, PICK_CONTACT);

    }


    private Boolean checkContactPermission() {

        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) ==
                (PackageManager.PERMISSION_GRANTED);

        return result;
    }


    private void requestContactPermission() {

        String[] permission = {Manifest.permission.READ_CONTACTS};

        ActivityCompat.requestPermissions(this, permission, CONTACT_PERMISSION_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();

            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);

            cursor.moveToFirst();


            String Fullname = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String PhoneNo = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


            personname.setText(Fullname);
            personnum.setText(PhoneNo);



        }
    }

}