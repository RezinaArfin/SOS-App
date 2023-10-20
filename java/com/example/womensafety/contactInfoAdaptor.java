package com.example.womensafety;

import android.app.Activity;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class contactInfoAdaptor extends ArrayAdapter<contactInfo> {


    private Activity context;
    private List<contactInfo>contactInfoList;


    public contactInfoAdaptor(Activity context, List<contactInfo>contactInfoList) {

        super(context, R.layout.listview, contactInfoList);
        this.context=context;
        this.contactInfoList=contactInfoList;
    }

    @NonNull
    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater=context.getLayoutInflater();
        View view= layoutInflater.inflate(R.layout.listview,null,true);

        contactInfo contactInfo=contactInfoList.get(position);

        TextView fname=view.findViewById(R.id.person_name1);

        TextView phnnum=view.findViewById(R.id.person_number1);

        fname.setText(contactInfo.getFullname());
        phnnum.setText(contactInfo.getPhoneNo());

        return view;

    }
}