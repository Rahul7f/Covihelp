package com.rsin.covihelp;

import java.io.Serializable;
import java.util.ArrayList;

public class Hospital implements Serializable {
    private  String name;
    private  String address;
    private  String fee_type;
    private  String dose1;
    private  String dose2;
    private  ArrayList<String> Slots;
    private  String vaccine;




    public Hospital(String name, String address, String fee_type, String dose1, String dose2, ArrayList<String> slots, String vaccine) {
        this.name = name;
        this.address = address;
        this.fee_type = fee_type;
        this.dose1 = dose1;
        this.dose2 = dose2;
        Slots = slots;
        this.vaccine = vaccine;
    }

    public Hospital(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getFee_type() {
        return fee_type;
    }

    public String getDose1() {
        return dose1;
    }

    public String getDose2() {
        return dose2;
    }

    public ArrayList<String> getSlots() {
        return Slots;
    }

    public String getVaccine() {
        return vaccine;
    }
}
