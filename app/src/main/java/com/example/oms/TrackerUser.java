package com.example.oms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.oms.adapter.OrderStatusAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TrackerUser extends AppCompatActivity {

    Button btn_search;
    RecyclerView statusrecview;
    ArrayList<OrdersHelperClass> list,listnew;
    String status[]={"Sender is preparing your parcel","Ready to ship","Received by the courier", "Out for delivery", "Parcel has been delivered"};
    RecyclerView recyclerView;
    OrderStatusAdapter orderStatusAdapter;
    com.google.android.material.textfield.TextInputEditText tv_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker_user);
        statusrecview = findViewById(R.id.statusrecview);
        tv_search = findViewById(R.id.tv_search);
        btn_search = findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                SharePref.init(getApplicationContext());
                String username = SharePref.read(SharePref.LoginName, null);
                list = new ArrayList<>();
                listnew = new ArrayList<>();
                String text = tv_search.getText().toString();
                myRef = myRef.child("ViewOrders/"+username);
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String key="";
                        if (dataSnapshot.hasChildren()) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                OrdersHelperClass ordersHelperClass = dataSnapshot1.getValue(OrdersHelperClass.class);
                                if(ordersHelperClass.getTrackNo()!=null){
                                if(ordersHelperClass.getTrackNo().equalsIgnoreCase(tv_search.getText().toString())) {
                                    key=dataSnapshot1.getKey();
                                    list.add(ordersHelperClass);
                                }
                                }
                            }

                            if(list.size()>0) {
                                if(!key.equalsIgnoreCase("")){
                                    FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef1 = database1.getReference();

                                    myRef1 = myRef1.child("ViewOrders").child(username).child(key).child("StatusDelivery");
                                    myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChildren()) {
                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                    OrdersHelperClass ordersHelperClass = dataSnapshot1.getValue(OrdersHelperClass.class);
                                                    listnew.add(ordersHelperClass);
                                                }
                                            }

                                            if(listnew.size()>0) {
                                                recyclerView = findViewById(R.id.statusrecview);
                                                recyclerView.setLayoutManager(new LinearLayoutManager(TrackerUser.this));
                                                orderStatusAdapter = new OrderStatusAdapter(TrackerUser.this, status, listnew);
                                                recyclerView.setAdapter(orderStatusAdapter);
                                            }
                                        }
                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                        Log.w("dd", "Failed to read value.", error.toException());
                                    }

                                });
                            }
                        }


                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.w("dd", "Failed to read value.", error.toException());
                    }
                });
            }
        });

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