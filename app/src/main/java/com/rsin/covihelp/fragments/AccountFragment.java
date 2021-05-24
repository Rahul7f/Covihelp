package com.rsin.covihelp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.rsin.covihelp.R;
import com.rsin.covihelp.all_Activitys.FormActivity;
import com.rsin.covihelp.all_Activitys.GoogleAuthActivity;

public class AccountFragment extends Fragment {
    FirebaseAuth mAuth;
    TextView logoutl;
    FirebaseAuth auth;
    TextView username;
    ImageView userprofile;
    CardView be_warrior,askhelp_btn;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        logoutl = view.findViewById(R.id.logout);
        be_warrior = view.findViewById(R.id.warrior_card);
        askhelp_btn = view.findViewById(R.id.askhelp_btn);
        username = view.findViewById(R.id.username);
        userprofile = view.findViewById(R.id.userimg);
        auth = FirebaseAuth.getInstance();

        be_warrior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),FormActivity.class);
                intent.putExtra("path","warriors_forms");
                startActivity(intent);
            }
        });

        askhelp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),FormActivity.class);
                intent.putExtra("path","helpers_forms");
                startActivity(intent);
            }
        });

        logoutl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                AuthFragment fragmentManager = new AuthFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragmentManager);
                fragmentTransaction.commit();
                Toast.makeText(getContext(), "no user", Toast.LENGTH_SHORT).show();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null)
        {
            username.setText(mAuth.getCurrentUser().getDisplayName());
            Glide.with(this).load(mAuth.getCurrentUser().getPhotoUrl()).into(userprofile);

        }
        else {
            //chnage fragment
            AuthFragment fragmentManager = new AuthFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, fragmentManager);
            fragmentTransaction.commit();
            Toast.makeText(getContext(), "no user", Toast.LENGTH_SHORT).show();

        }
        return view;
    }
}