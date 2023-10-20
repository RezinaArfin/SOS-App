package com.example.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class profile extends AppCompatActivity {

    private ListView listView;
    private Toolbar toolbar;
    DatabaseReference databaseReference;
    private List<UserDetails> userDetailsList;
    private ProfiledetailsAdaptor profiledetailsAdaptor;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar=findViewById(R.id.layout_bar);
        listView=findViewById(R.id.listview1);


        setSupportActionBar(toolbar);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        userDetailsList =new ArrayList<>();

        profiledetailsAdaptor=new ProfiledetailsAdaptor(profile.this,userDetailsList);
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
                userDetailsList.clear();
                UserDetails userDetails=snapshot.getValue(UserDetails.class);
                if(userDetails!=null){
                    userDetailsList.add(userDetails);
                }
                listView.setAdapter(profiledetailsAdaptor);
            }



            @Override
            public void onCancelled( DatabaseError error) {

                Toast.makeText(profile.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        super.onStart();
    }
}