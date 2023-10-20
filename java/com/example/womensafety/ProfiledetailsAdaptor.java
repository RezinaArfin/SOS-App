package com.example.womensafety;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ProfiledetailsAdaptor extends ArrayAdapter<UserDetails> {


    private Activity context;
    private List<UserDetails>userDetailsList;


    public ProfiledetailsAdaptor(Activity context, List<UserDetails> userDetailslist) {

        super(context, R.layout.profile_listview, userDetailslist);
        this.context=context;
        this.userDetailsList=userDetailslist;
    }

    @NonNull
    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater=context.getLayoutInflater();
        View view= layoutInflater.inflate(R.layout.profile_listview,null,true);

        UserDetails userDetails=userDetailsList.get(position);

        TextView fname=view.findViewById(R.id.profile_fname);
        TextView usname=view.findViewById(R.id.profile_usname);
        TextView mail=view.findViewById(R.id.profile_mail);
        TextView phnnum=view.findViewById(R.id.profile_phnnum);
        TextView pass=view.findViewById(R.id.profile_password);


        fname.setText("Fullname : "+userDetails.getFullname());
        usname.setText("Username :"+userDetails.getUsername());
        mail.setText("Email Address: "+userDetails.getEmail());
        phnnum.setText("Phone Number: "+userDetails.getPhone_num());
        pass.setText("Password :"+userDetails.getPassword());

        return view;

    }
}
