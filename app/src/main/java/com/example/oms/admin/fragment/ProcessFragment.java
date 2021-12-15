package com.example.oms.admin.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oms.OrdersHelperClass;
import com.example.oms.Prevalent.Prevalent;
import com.example.oms.R;
import com.example.oms.adapter.MyOrderAdapter;
import com.example.oms.adapter.OrderAdmin2Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProcessFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    OrderAdmin2Adapter orderAdmin2Adapter;
    ArrayList<OrdersHelperClass>list;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_process, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.orderrecview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));

        list = new ArrayList<>();
        orderAdmin2Adapter = new OrderAdmin2Adapter(getContext(),list);
        recyclerView.setAdapter(orderAdmin2Adapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("ViewOrders").child("username")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            OrdersHelperClass ordersHelperClass = snapshot1.getValue(OrdersHelperClass.class);
                            list.add(ordersHelperClass);
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