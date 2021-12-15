package com.example.oms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oms.Prevalent.Prevalent;
import com.example.oms.admin.model.CartModel;
import com.google.android.gms.tasks.OnCompleteListener;
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
import java.util.Calendar;
import java.util.HashMap;

public class checkout extends AppCompatActivity {

    TextView name, address, phone,nameprod,desc,prices,qty,tprice;
    ImageView imageView;
    RadioGroup radioGroup;
    Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        nameprod = findViewById(R.id.nameprod);
        desc = findViewById(R.id.desc);
        prices = findViewById(R.id.price);
        qty= findViewById(R.id.qty);
        tprice = findViewById(R.id.tprice);
        imageView = findViewById(R.id.imageprod);
        radioGroup = findViewById(R.id.rdGroup);
        radioGroup.clearCheck();
        btn = findViewById(R.id.paynow);

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
        showOrder();


    }

    private void payNow() {

        String saveTime, saveDate;
        Calendar calForDate =  Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveTime = currentTime.format(calForDate.getTime());

        //new_new_edit
        final DatabaseReference viewRef = FirebaseDatabase.getInstance().getReference().child("ViewOrders").child(Prevalent.currentUser.getUsername()).push();
        String key = viewRef.getKey();
        //String trackNo = viewRef.push().getKey();


        HashMap<String, Object> viewMap = new HashMap<>();

        viewMap.put("totalPrice", tprice.getText().toString());
        viewMap.put("date", saveDate);
        viewMap.put("time", saveTime);
        viewMap.put("name",name.getText().toString());
        viewMap.put("quantity",qty.getText().toString());
        viewMap.put("phone",phone.getText().toString());
        viewMap.put("address",address.getText().toString());
        viewMap.put("pname",nameprod.getText().toString());
        viewMap.put("orderId",key);
        //viewMap.put("trackingNo",trackNo);




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
                                        Intent intent = new Intent(checkout.this, CatalogueUser.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                }
            }
        });


    }


    //retrieve order
    private void showOrder() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Cart").child(Prevalent.currentUser.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    for(DataSnapshot ds : snapshot1.getChildren()) {

                        // String prodImage = snapshot.child("image").getValue(String.class);
                        String userpname = ds.child("pname").getValue().toString();
                        String userpqty = ds.child("quantity").getValue().toString();
                        String userprice = ds.child("price").getValue().toString();
                        String usertotalprice = ds.child("totalPrice").getValue().toString();

                        //   imageView.setImageURI(prodImage); belum jadi
                        nameprod.setText(userpname);
                        prices.setText("RM"+userprice);
                        qty.setText("x" + userpqty);
                        tprice.setText("Total : RM"+usertotalprice);
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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