package com.rsin.covihelp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rsin.covihelp.Hospital;
import com.rsin.covihelp.R;
import com.rsin.covihelp.all_Activitys.ViewCenterActivity;

import java.util.ArrayList;

public class VaccineCenterAdapter extends RecyclerView.Adapter<VaccineCenterAdapter.ViewHolder>{
    Context context;
    ArrayList<Hospital> list;

    public VaccineCenterAdapter(Context context, ArrayList<Hospital> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VaccineCenterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.vaccine_center_layout, parent, false);
        VaccineCenterAdapter.ViewHolder viewHolder = new VaccineCenterAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VaccineCenterAdapter.ViewHolder holder, int position) {
        holder.center_name.setText(list.get(position).getName());
        holder.center_address.setText(list.get(position).getAddress());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hospital hospital = list.get(position);
                Intent i = new Intent(context, ViewCenterActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("sampleObject", hospital);
                context.startActivity(i);
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView center_name,center_address;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            center_name = itemView.findViewById(R.id.center_name);
            center_address = itemView.findViewById(R.id.center_address);
            cardView = itemView.findViewById(R.id.center_card);


        }
    }
}
