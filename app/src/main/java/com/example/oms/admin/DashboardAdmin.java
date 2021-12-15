package com.example.oms.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.oms.LoginTesting;
import com.example.oms.R;
import com.example.oms.RegisterActivity;

public class DashboardAdmin extends AppCompatActivity {

    CardView prod, order, ds, sale, rank, invoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);

        prod = findViewById(R.id.productbtn);
        order = findViewById(R.id.orderbtn);
        ds = findViewById(R.id.dsbtn);

        prod.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardAdmin.this, ProductAdmin.class);
            startActivity(intent);
        });

        ds.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardAdmin.this, DropshipList.class);
            startActivity(intent);
        });

        order.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardAdmin.this, OrderAdmin.class);
            startActivity(intent);
        });


    }
}