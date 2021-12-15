package com.example.oms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import com.example.oms.adapter.OrderStatusAdapter;


import java.util.ArrayList;

public class StatusParcel extends AppCompatActivity {
    RecyclerView recyclerView;
    OrderStatusAdapter orderStatusAdapter;

    String status[]={"Sender preparing your parcel","Ready to ship","Received by the courier", "Out for delivery", "Parcel has been delivered"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_parcel);


        recyclerView = findViewById(R.id.statrecview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderStatusAdapter = new OrderStatusAdapter(this,status);
        recyclerView.setAdapter(orderStatusAdapter);


    }
}