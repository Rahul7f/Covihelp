package com.rsin.covihelp.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.rsin.covihelp.R;
import com.rsin.covihelp.all_Activitys.CenterDetailsActivity;
import com.rsin.covihelp.all_Activitys.FormActivity;

import java.util.Calendar;


public class VaccineFragment extends Fragment {

    TextView displayDate;
    TextInputLayout getPin;
    ImageView datepicker_btn;
    Button search_btn;
    private String url;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_vaccine, container, false);

         getPin = root.findViewById(R.id.pincode_search);
        displayDate = root.findViewById(R.id.showdate_tv);
        datepicker_btn = root.findViewById(R.id.date_picker_btn);
        search_btn = root.findViewById(R.id.search_btn);

        datepicker_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int date = calendar.get(Calendar.DATE);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, month1, dayOfMonth) -> {
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.set(Calendar.YEAR, year1);
                    calendar1.set(Calendar.MONTH, month1);
                    calendar1.set(Calendar.DATE, dayOfMonth);
                    CharSequence date1 = DateFormat.format("dd-MM-yyyy", calendar1);
                    displayDate.setText(date1);
                }
                , year, month, date);
                datePickerDialog.show();
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                url = null;
                String pin = getPin.getEditText().getText().toString();
                String date = displayDate.getText().toString();
                setUrl("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode=" + pin + "&date=" + date);
                if (!pin.equals("") && Integer.parseInt(pin) >=(100000) && Integer.parseInt(pin) <= (999999)) {
                    if (!date.equals("")) {
                        Intent intent = new Intent(getContext(), CenterDetailsActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(getContext(), "Please Select Date", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(getContext(), "Invalid pin code", Toast.LENGTH_SHORT).show();

            }
        });



        return root;
    }


    public void setUrl(String url) {
        this.url = url;
    }
}