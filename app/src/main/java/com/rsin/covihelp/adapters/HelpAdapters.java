package com.rsin.covihelp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rsin.covihelp.FormBeen;
import com.rsin.covihelp.R;
import com.rsin.covihelp.all_Activitys.ViewDetailsActivity;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

    public class HelpAdapters extends RecyclerView.Adapter<HelpAdapters.ViewHolder> {
    Context context;
    ArrayList<FormBeen> data_list;

    public HelpAdapters(Context context, ArrayList<FormBeen> data_list) {
        this.context = context;
        this.data_list = data_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.help_people_recycle_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HelpAdapters.ViewHolder holder, int position) {
        holder.address.setText(data_list.get(position).getAddress());
        holder.title.setText(data_list.get(position).getTitle());
        holder.category.setText(data_list.get(position).getCategory());
        Glide.with(context).load(data_list.get(position).getAll_photos().get(0)).into(holder.imageView);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ViewDetailsActivity.class);
                intent.putExtra("doc_id",data_list.get(position).getUuid());
                intent.putExtra("path","helpers_forms");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        TextView title,time,address,category;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.help_card);
            imageView = itemView.findViewById(R.id.image_h);
            title = itemView.findViewById(R.id.title_h);
            time = itemView.findViewById(R.id.time_ago_h);
            address = itemView.findViewById(R.id.address_h);
            category = itemView.findViewById(R.id.category_h);
        }
    }
}
