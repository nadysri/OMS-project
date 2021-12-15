package com.example.oms.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.oms.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivityAdmin extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        tv = findViewById(R.id.name);



        //initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set hom selected
        bottomNavigationView.setSelectedItemId(R.id.nav_dashboard);

        //perform itemselected
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_dashboard:
                        return true;

                    case R.id.nav_product:
                        startActivity(new Intent(getApplicationContext(),ProductAdmin.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_order:
                        startActivity(new Intent(getApplicationContext(), OrderAdmin.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });
    }

}