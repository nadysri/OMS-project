package com.example.oms.admin.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class NewOrderFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;

    OrderAdmin2Adapter orderAdmin2Adapter;
    ArrayList<OrdersHelperClass> list;
    Button btn, button;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_order, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.orderrecview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));

        list = new ArrayList<>();
        orderAdmin2Adapter = new OrderAdmin2Adapter(getContext(),list);
        recyclerView.setAdapter(orderAdmin2Adapter);
        btn = (Button)v.findViewById(R.id.arrangebtn);
        btn = (Button)v.findViewById(R.id.submitbtn);


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


        return v;


    }



}