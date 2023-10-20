package com.example.womensafety;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;

import java.util.List;

public class numberAdaptor extends ArrayAdapter<contactInfo> {
    private Activity context;
    List<contactInfo> contactInfoList;


    public numberAdaptor(Activity context, List<contactInfo>contactInfoList) {

        super(context, R.layout.numberlayout, contactInfoList);
        this.context=context;
        this.contactInfoList=contactInfoList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater=context.getLayoutInflater();
        View view= layoutInflater.inflate(R.layout.numberlayout,null,true);

        contactInfo contactInfo= contactInfoList.get(position);

        //TextView fname=view.findViewById(R.id.person_name1);

        //TextView phnnum=view.findViewById(R.id.person_number1);

        EditText phnnumber=view.findViewById(R.id.number1);

        //fname.setText(contactInfo.getFullname());
        //phnnum.setText(contactInfo.getPhoneNo());
        phnnumber.setText(contactInfo.getPhoneNo());

        return view;

    }
}
