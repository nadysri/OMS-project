package com.example.oms.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.oms.OrdersHelperClass;
import com.example.oms.Prevalent.Prevalent;
import com.example.oms.R;
import com.example.oms.UserHelperClass;
import com.example.oms.adapter.MyDropshipAdapter;
import com.example.oms.adapter.MyOrderAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DropshipList extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    MyDropshipAdapter myAdapter;
    ArrayList<UserHelperClass> list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dropship_list);

        recyclerView = findViewById(R.id.dropsrecview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new MyDropshipAdapter(this,list);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(myAdapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                           UserHelperClass userHelperClass = snapshot1.getValue(UserHelperClass.class);
                            list.add(userHelperClass);

                           /* if(userHelperClass.getUsername().equals("admin")) {
                                recyclerView.setVisibility(View.GONE);
                            }
                            else {
                                recyclerView.setVisibility(View.VISIBLE);
                            }*/

                        }
                        myAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}