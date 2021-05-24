package com.rsin.covihelp.all_Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rsin.covihelp.Hospital;
import com.rsin.covihelp.R;
import com.rsin.covihelp.adapters.VaccineCenterAdapter;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CenterDetailsActivity extends AppCompatActivity {
    RequestQueue myQueue;
    RecyclerView recyclerView;
    VaccineCenterAdapter adapter;
    ArrayList<Hospital> hospitalArrayList;
    ArrayList<String> hospitalNameList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_details);
        recyclerView = findViewById(R.id.vaccine_recycl);
        myQueue = Volley.newRequestQueue(this);

//        Hospital hospital = new Hospital("hospital name","basai enclace sec 36");
//        hospitalArrayList.add(hospital);
//
//        adapter  = new VaccineCenterAdapter(getApplicationContext(),hospitalArrayList);
        JsonParse();
        recyclerView.setAdapter(adapter);

    }


    private void JsonParse() {
        hospitalNameList = new ArrayList<>();
        String errorMsg = "No Centres Available";
        hospitalNameList.add(errorMsg);

        String url = getIntent().getStringExtra("url");
        hospitalArrayList = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray centers = response.getJSONArray("sessions");
                for (int i = 0; i < centers.length(); i++) {
                    JSONObject data = centers.getJSONObject(i);
                    String name = data.getString("name");
                    Log.d("name_data", name);
                    String address = data.getString("address");
                    Log.d("address_data", address);
                    String fee_type = data.getString("fee_type");
                    Log.d("fee_data", fee_type);
                    String available_capacity_dose1 = data.getString("available_capacity_dose1");
                    Log.d("dose1_data", available_capacity_dose1);
                    String available_capacity_dose2 = data.getString("available_capacity_dose2");
                    Log.d("dose1_data", available_capacity_dose1);
                    String vaccine = data.getString("vaccine");
                    Log.d("vaccine_data", vaccine);
                    JSONArray slots = data.getJSONArray("slots");
                    ArrayList<String> slotsArray = new ArrayList<>();
                    for (int k = 0; k < slots.length(); k++) {
                        String time = slots.getString(k);
                        Log.d("time_data", time);
                        slotsArray.add(time);
                    }
                    if (address.equals("")) {
                        address = "Not Provided";
                    }
//                    if ((Integer.parseInt(available_capacity_dose1) != 0) || (Integer.parseInt(available_capacity_dose2) != 0)) {
//
//                    }
                    Hospital hospital = new Hospital(name, address, fee_type, available_capacity_dose1, available_capacity_dose2, slotsArray, vaccine);
                    hospitalArrayList.add(hospital);
                    hospitalNameList.add(name + " (" + vaccine + ")");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(CenterDetailsActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
            }
            if (hospitalNameList == null) {
                Toast.makeText(CenterDetailsActivity.this, "No Hospital", Toast.LENGTH_SHORT).show();
            }
            if ((hospitalNameList.size() > 1) && hospitalNameList.get(0).equals(errorMsg)) {
                hospitalNameList.remove(0);
            }
            if (hospitalNameList.get(0).equals(errorMsg)) {
                Toast.makeText(this, "No centres available on this date ", Toast.LENGTH_SHORT).show();
            } else{
                VaccineCenterAdapter vaccineCenterAdapter = new VaccineCenterAdapter(getApplicationContext(),hospitalArrayList);
                recyclerView.setAdapter(vaccineCenterAdapter);
            }
        }, error -> Toast.makeText(CenterDetailsActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show());
        myQueue.add(request);
        if (hospitalNameList == null) {
            Toast.makeText(this, "N0 Hospitals Available", Toast.LENGTH_SHORT).show();
        }
    }
//    private void JsonParse() {
//        hospitalNameList = new ArrayList<>();
//        String errorMsg = "No Centres Available";
//        hospitalNameList.add(errorMsg);
//        String url = getIntent().getStringExtra("url");
//        hospitalArrayList = new ArrayList<>();
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
//            try {
//                JSONArray centers = response.getJSONArray("sessions");
//                for (int i = 0; i < centers.length(); i++) {
//                    JSONObject data = centers.getJSONObject(i);
//
//
//                    String name = data.getString("name");
//                    String address = data.getString("address");
//                    String fee_type = data.getString("fee_type");
//                    String available_capacity_dose1 = data.getString("available_capacity_dose1");
//                    String available_capacity_dose2 = data.getString("available_capacity_dose2");
//                    String vaccine = data.getString("vaccine");
//                    JSONArray slots = data.getJSONArray("slots");
//                    ArrayList<String> slotsArray = new ArrayList<>();
//
//                    for (int k = 0; k < slots.length(); k++) {
//                        String time = slots.getString(k);
//                        Log.d("time_data", time);
//                        slotsArray.add(time);
//                    }
//                    if (address.equals("")) {
//                        address = "Not Provided";
//                    }
//                    if ((Integer.parseInt(available_capacity_dose1) != 0) || (Integer.parseInt(available_capacity_dose2) != 0)) {
//                        Hospital hospital = new Hospital(name, address, fee_type, available_capacity_dose1, available_capacity_dose2, slotsArray, vaccine);
//                        hospitalArrayList.add(hospital);
//                        hospitalNameList.add(name + " (" + vaccine + ")");
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Toast.makeText(CenterDetailsActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
//            }
//
//            if (hospitalNameList == null) {
//                Toast.makeText(CenterDetailsActivity.this, "No Hospital", Toast.LENGTH_SHORT).show();
//            }
//            if ((hospitalNameList.size() > 1) && hospitalNameList.get(0).equals(errorMsg)) {
//                hospitalNameList.remove(0);
//            }
//            ArrayAdapter<String> HospitalSpinnerAdapter = new ArrayAdapter<>(CenterDetailsActivity.this, android.R.layout.simple_spinner_dropdown_item, hospitalNameList);
//
//            if (!hospitalNameList.get(0).equals(errorMsg)) {
//
//            } else
//                Toast.makeText(CenterDetailsActivity.this, "No centres available on this date", Toast.LENGTH_SHORT).show();
//        },
//                error -> Toast.makeText(CenterDetailsActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show());
//
//        myQueue.add(request);
//        if (hospitalNameList == null) {
//            Toast.makeText(this, "N0 Hospitals Available", Toast.LENGTH_SHORT).show();
//        }
//    }
}