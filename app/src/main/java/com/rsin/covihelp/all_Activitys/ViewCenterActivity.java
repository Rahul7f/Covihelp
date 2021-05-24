package com.rsin.covihelp.all_Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.rsin.covihelp.Hospital;
import com.rsin.covihelp.R;

public class ViewCenterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_center);
        Intent i = getIntent();
        Hospital hospital = (Hospital) ((Intent) i).getSerializableExtra("sampleObject");

        TextView displayName = findViewById(R.id.displayName);
        TextView displayFeeType = findViewById(R.id.displayFeeType);
        TextView displayVaccineName = findViewById(R.id.displayVaccineName);
        TextView displayDose1 = findViewById(R.id.displayDose1);
        TextView displayDose2 = findViewById(R.id.displayDose2);
        TextView showAddress = findViewById(R.id.showAddress);
        ListView showTime = findViewById(R.id.showTime);

        showAddress.setText(hospital.getAddress());
        displayName.setText(hospital.getName());
        displayFeeType.setText(hospital.getFee_type());
        displayVaccineName.setText(hospital.getVaccine());
        displayDose1.setText(hospital.getDose1());
        displayDose2.setText(hospital.getDose2());

        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(ViewCenterActivity.this, android.R.layout.simple_list_item_1, hospital.getSlots());
        showTime.setAdapter(timeAdapter);



    }
}