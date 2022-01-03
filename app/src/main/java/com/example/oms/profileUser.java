package com.example.oms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oms.Prevalent.Prevalent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class profileUser extends AppCompatActivity {
    Button btnLogout;
    ImageButton editProfile;
    ImageView profilepic;
    TextView fullnames, usernames;
    MaterialButton materialButton,delivery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        btnLogout = findViewById(R.id.btnLogout);
        editProfile =findViewById(R.id.editprofile);
        fullnames =findViewById(R.id.fullname);
        usernames =findViewById(R.id.username);
        materialButton = findViewById(R.id.myorderbtn);
        delivery = findViewById(R.id.deliveryaddr);
        profilepic = findViewById(R.id.profileIV);

        showAllUserData();

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(profileUser.this,MyOrders.class);
                startActivity(i);
                finish();
            }
        });

        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(profileUser.this,deliveryAddress.class);
                startActivity(i);
                finish();
            }
        });



        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(profileUser.this, editProfileUser.class));
            }
        });

        //initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigationuser);

        //set hom selected
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

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
                        startActivity(new Intent(getApplicationContext(), TrackerUser.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_profile:
                        return true;

                }
                return false;
            }
        });

        btnLogout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(profileUser.this);
            builder.setTitle("Sign Out").
                    setMessage("Are you sure that you want to sign out?");
            builder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(getApplicationContext(),
                                    LoginTesting.class);
                            startActivity(i);
                            finish();

                        }
                    });
            builder.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder.create();
            alert11.show();
        });



    }

    private void showAllUserData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").child(Prevalent.currentUser.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                String user_username = snapshot.child("username").getValue(String.class);
                String user_name = snapshot.child("name").getValue(String.class);
                String profile = snapshot.child("image").getValue(String.class);


                fullnames.setText(user_name);
                usernames.setText("@"+ user_username);
                try {
                    Picasso.get().load(profile).placeholder(R.drawable.ic_person_gray).into(profilepic);
                }
                catch (Exception e) {
                    profilepic.setImageResource(R.drawable.ic_person_gray);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}