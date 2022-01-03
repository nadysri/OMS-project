package com.example.oms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.oms.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Leaderboard extends AppCompatActivity {
    TextView name1, name2, name3, name4, name5, sale1, sale2, sale3, sale4, sale5;
    ImageButton back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        
        name1 = findViewById(R.id.nameds);
        name2 = findViewById(R.id.nameds2);
        name3 = findViewById(R.id.nameds3);
        name4 = findViewById(R.id.nameds4);
        name5 = findViewById(R.id.nameds5);
        sale1 = findViewById(R.id.totalsale);
        sale2 = findViewById(R.id.totalsale2);
        sale3 = findViewById(R.id.totalsale3);
        sale4 = findViewById(R.id.totalsale4);
        sale5 = findViewById(R.id.totalsale5);
        back = findViewById(R.id.backBtn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Leaderboard.this,MainActivity.class);
                startActivity(intent);
            }
        });
        
        showRankDs();
    }

    private void showRankDs() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Rank").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                String ds1 = snapshot.child("rank1").getValue(String.class);
                String ds2 = snapshot.child("rank2").getValue(String.class);
                String ds3 = snapshot.child("rank3").getValue(String.class);
                String ds4 = snapshot.child("rank4").getValue(String.class);
                String ds5 = snapshot.child("rank5").getValue(String.class);

                String s1 = snapshot.child("totalsale1").getValue(String.class);
                String s2 = snapshot.child("totalsale2").getValue(String.class);
                String s3 = snapshot.child("totalsale3").getValue(String.class);
                String s4 = snapshot.child("totalsale4").getValue(String.class);
                String s5 = snapshot.child("totalsale5").getValue(String.class);


                name1.setText(ds1);
                name2.setText(ds2);
                name3.setText(ds3);
                name4.setText(ds4);
                name5.setText(ds5);

                sale1.setText(s1);
                sale2.setText(s2);
                sale3.setText(s3);
                sale4.setText(s4);
                sale5.setText(s5);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}