package com.example.oms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.oms.Prevalent.Prevalent;
import com.example.oms.adapter.CheckoutAdapter;
import com.example.oms.adapter.MyDropshipAdapter;
import com.example.oms.admin.model.CartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class checkout extends AppCompatActivity {

    TextView name, address, phone,nameprod,desc,prices,qty,tprice,fee;
    ImageView imageView;
    RadioGroup radioGroup;
    Button btn;
    String Tprice;
    RecyclerView recyclerView;
    DatabaseReference database;
    ArrayList<CartModel> list;
    CheckoutAdapter checkoutAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        nameprod = findViewById(R.id.nameprod);
        tprice = findViewById(R.id.tprice);
        imageView = findViewById(R.id.imageprod);
        fee = findViewById(R.id.deliveryfee);
        radioGroup = findViewById(R.id.rdGroup);
        radioGroup.clearCheck();
        btn = findViewById(R.id.paynow);

        Intent intent = getIntent();
        String Tprice = intent.getStringExtra("totalPrice");
        tprice.setText(Tprice);

        recyclerView = findViewById(R.id.checkoutrecview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        checkoutAdapter = new CheckoutAdapter(this,list);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(checkoutAdapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Cart")
                .child(Prevalent.currentUser.getUsername())   //salahhh
                .child("UNIQUE_USER_ID")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            CartModel cartModel = snapshot1.getValue(CartModel.class);
                            list.add(cartModel);

                        }
                        checkoutAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rdbOnline:
                        Toast.makeText(checkout.this,"Online Banking", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rdbCard:
                        Toast.makeText(checkout.this,"Card", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rdbPaypal:
                        Toast.makeText(checkout.this,"Paypal", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payNow();

            }
        });


        showAddress();


    }

    private void payNow() {

        String saveTime, saveDate;
        Calendar calForDate =  Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveTime = currentTime.format(calForDate.getTime());

        String idImageString = String.valueOf(imageView);

        //new_new_edit
        final DatabaseReference viewRef = FirebaseDatabase.getInstance().getReference().child("ViewOrders").child(Prevalent.currentUser.getUsername()).push();
        String key = viewRef.getKey();

        int point = 50;
        HashMap<String, Object> viewMap = new HashMap<>();

        viewMap.put("totalPrice", tprice.getText().toString());
        viewMap.put("date", saveDate);
        viewMap.put("points", point);
        viewMap.put("time", saveTime);
        viewMap.put("name",name.getText().toString());
        viewMap.put("phone",phone.getText().toString());
        viewMap.put("address",address.getText().toString());
        viewMap.put("orderId",key);




        viewRef.updateChildren(viewMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    //to remove all the ordered products
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart")
                            .child(Prevalent.currentUser.getUsername())
                            .child("UNIQUE_USER_ID")
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(checkout.this, "your order has been placed successfully.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(checkout.this, PaymentSuccess.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                }
            }
        });

        //update status delivery
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference stsRef = database.getReference();
        Calendar calForDate1 =  Calendar.getInstance();
        SimpleDateFormat currentDate1 = new SimpleDateFormat("MMM dd, yyyy");
        String saveDate1 = currentDate1.format(calForDate1.getTime());

        stsRef.child("ViewOrders").child(Prevalent.currentUser.getUsername()).child(key).child("StatusDelivery").push();
        String id = stsRef.child("ViewOrders").child(Prevalent.currentUser.getUsername()).child(key).child("StatusDelivery").push().getKey();
        stsRef.child("ViewOrders").child(Prevalent.currentUser.getUsername()).child(key).child("StatusDelivery").child(id).child("status").setValue("Sender is preparing your parcel");
        stsRef.child("ViewOrders").child(Prevalent.currentUser.getUsername()).child(key).child("StatusDelivery").child(id).child("date").setValue(saveDate);

        //item update
        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference stsRef1 = database1.getReference();

        for(int i=0;i<list.size();i++){
            stsRef1.child("ViewOrders").child(Prevalent.currentUser.getUsername()).child(key);
            String id1 = stsRef.child("ViewOrders").child(Prevalent.currentUser.getUsername()).child(key).child("item").push().getKey();
            stsRef1.child("ViewOrders").child(Prevalent.currentUser.getUsername()).child(key).child("item").child(id1).child("pname").setValue(list.get(i).getPname());
            stsRef1.child("ViewOrders").child(Prevalent.currentUser.getUsername()).child(key).child("item").child(id1).child("quantity").setValue(list.get(i).getQuantity());
            stsRef1.child("ViewOrders").child(Prevalent.currentUser.getUsername()).child(key).child("item").child(id1).child("price").setValue(list.get(i).getPrice());
        }

    }


    //retrieve address

    private void showAddress() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").child(Prevalent.currentUser.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String user_name = snapshot.child("name").getValue(String.class);
                String user_address = snapshot.child("address").getValue(String.class);
                String user_phone = snapshot.child("phone").getValue(String.class);

                name.setText(user_name);
                address.setText(user_address);
                phone.setText(user_phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}