package com.example.womensafety;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class details extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;
    private contactInfoAdaptor contactInfoAdaptor;
    private List<contactInfo>contactInfoList;
    private DatabaseReference databaseReference;
    Button deletebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        toolbar=findViewById(R.id.layout_bar);

        setSupportActionBar(toolbar);

        listView=findViewById(R.id.listview2);
        deletebtn=findViewById(R.id.deletebtn);

        databaseReference = FirebaseDatabase.getInstance().getReference("contactInfo");
        contactInfoList=new ArrayList<>();

        contactInfoAdaptor=new contactInfoAdaptor(details.this,contactInfoList);

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
                Toast.makeText(details.this, "Deleted all information.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void clearData() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("contactInfo").setValue(null);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {

                contactInfoList.clear();
                for(DataSnapshot dataSnapshot1: snapshot.getChildren()){

                    contactInfo contactInfo=dataSnapshot1.getValue(contactInfo.class);
                    contactInfoList.add(contactInfo);
                }
                listView.setAdapter(contactInfoAdaptor);
            }

            @Override
            public void onCancelled( DatabaseError error) {

                Toast.makeText(details.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        super.onStart();
    }
}