package com.example.oms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TrackerUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker_user);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigationuser);

        //set hom selected
        bottomNavigationView.setSelectedItemId(R.id.nav_track);

        //perform itemselected
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_catalogue:
                        startActivity(new Intent(getApplicationContext(), CatalogueUser.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_track:
                        return true;

                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(), profileUser.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });



    }


}