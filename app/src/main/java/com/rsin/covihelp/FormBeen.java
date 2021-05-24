package com.rsin.covihelp;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class FormBeen {

    private String title;
    private String description;
    private String category;
    private String state;
    private String city;
    private String pincode;
    private String address;
    private String latitude;
    private String longitude;
    private String name;
    private String uuid;
    private String phone;
    private List<String > all_photos;
    private List<String> aadhar_photo;

    public FormBeen() {
    }

    public FormBeen(String title, String description, String category, String state, String city, String pincode, String address, String latitude, String longitude, String name, String uuid, String phone, List<String> all_photos, List<String> aadhar_photo) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.state = state;
        this.city = city;
        this.pincode = pincode;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.uuid = uuid;
        this.phone = phone;
        this.all_photos = all_photos;
        this.aadhar_photo = aadhar_photo;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getPincode() {
        return pincode;
    }

    public String getAddress() {
        return address;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    public String getPhone() {
        return phone;
    }

    public List<String> getAll_photos() {
        return all_photos;
    }

    public List<String> getAadhar_photo() {
        return aadhar_photo;
    }
}
