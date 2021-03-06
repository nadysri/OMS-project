package com.example.oms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.oms.Prevalent.Prevalent;
import com.example.oms.adapter.MyOrderAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyOrders extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    MyOrderAdapter myAdapter;
    ImageButton back;
    ArrayList<OrdersHelperClass> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        back = findViewById(R.id.backBtns);

        recyclerView = findViewById(R.id.orderrecview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new MyOrderAdapter(this,list);
        recyclerView.setAdapter(myAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyOrders.this, profileUser.class);
                startActivity(intent);
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("ViewOrders")
                .child(Prevalent.currentUser.getUsername())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            OrdersHelperClass ordersHelperClass = snapshot1.getValue(OrdersHelperClass.class);
                            list.add(ordersHelperClass);
                        }
                        myAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }
}