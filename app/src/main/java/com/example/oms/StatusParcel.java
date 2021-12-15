package com.example.oms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;

import com.example.oms.adapter.OrderStatusAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class StatusParcel extends AppCompatActivity {
    RecyclerView recyclerView;
    OrderStatusAdapter orderStatusAdapter;

    String status[]={"Sender is preparing your parcel","Ready to ship","Received by the courier", "Out for delivery", "Parcel has been delivered"};

    ArrayList<OrdersHelperClass> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_parcel);

        Intent intent = getIntent();
        String keyname, key;
        keyname = intent.getStringExtra("keyName");
        key = intent.getStringExtra("key");
        list = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef = myRef.child("ViewOrders/"+keyname).child(key).child("StatusDelivery");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        OrdersHelperClass ordersHelperClass = dataSnapshot1.getValue(OrdersHelperClass.class);
                        list.add(ordersHelperClass);
                    }

                    if(list.size()>0) {
                        recyclerView = findViewById(R.id.statrecview);
                        recyclerView.setLayoutManager(new LinearLayoutManager(StatusParcel.this));
                        orderStatusAdapter = new OrderStatusAdapter(StatusParcel.this, status, list);
                        recyclerView.setAdapter(orderStatusAdapter);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("dd", "Failed to read value.", error.toException());
            }
        });




    }
}