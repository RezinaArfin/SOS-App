package com.example.womensafety;

public class contactInfo {
   private String Fullname,PhoneNo;

    public contactInfo() {
    }

    public contactInfo(String fullname, String phoneNo) {
        Fullname = fullname;
        PhoneNo = phoneNo;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }
}
