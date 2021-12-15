package com.example.oms.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import com.example.oms.OrdersHelperClass;
import com.example.oms.R;
import com.example.oms.adapter.OrderAdmin2Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderAdmin extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;

    OrderAdmin2Adapter orderAdmin2Adapter;
    ArrayList<OrdersHelperClass> list;
    Button btn, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_admin);

        recyclerView = (RecyclerView) findViewById(R.id.orderrecview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        orderAdmin2Adapter = new OrderAdmin2Adapter(this,list);
        recyclerView.setAdapter(orderAdmin2Adapter);
        btn = (Button)findViewById(R.id.arrangebtn);
        btn = (Button)findViewById(R.id.submitbtn);
        status = (Button)findViewById(R.id.statusbtn);
        status = (Button)findViewById(R.id.okbtn);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("ViewOrders")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot snapshots : snapshot.getChildren()) {
                            String name = snapshots.getKey();
                            for (DataSnapshot snapshot1 : snapshots.getChildren()) {
                                OrdersHelperClass ordersHelperClass = snapshot1.getValue(OrdersHelperClass.class);
                                ordersHelperClass.setKey(snapshot1.getKey());
                                ordersHelperClass.setKeyName(name);
                                list.add(ordersHelperClass);
                            }
                        }
                        orderAdmin2Adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}