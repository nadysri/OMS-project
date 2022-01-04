package com.example.oms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.oms.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class deliveryAddress extends AppCompatActivity {

    TextView address, phone, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);

        address =findViewById(R.id.addr);
        name =findViewById(R.id.fullname);
        phone =findViewById(R.id.phonee);

        showAddress();
    }

    private void showAddress() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").child(Prevalent.currentUser.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                String addresses = snapshot.child("address").getValue(String.class);
                String names = snapshot.child("name").getValue(String.class);
                String phones = snapshot.child("phone").getValue(String.class);


                name.setText(names);
                phone.setText(phones);
                address.setText(addresses);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}