package com.example.oms.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oms.OrdersHelperClass;
import com.example.oms.Prevalent.Prevalent;
import com.example.oms.R;
import com.example.oms.UserHelperClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class RankDs extends AppCompatActivity {

    DatabaseReference dbref;
    EditText ts1, ts2, ts3, ts4, ts5;
    AutoCompleteTextView sn1, sn2, sn3, sn4, sn5;
    Button savebtn;
    TextView monthyear;
    ProgressDialog progressDialog;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Rank");

    ValueEventListener listener;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_ds);

        ts1 = findViewById(R.id.totalsale);
        ts2 = findViewById(R.id.totalsale2);
        ts3 = findViewById(R.id.totalsale3);
        ts4 = findViewById(R.id.totalsale4);
        ts5 = findViewById(R.id.totalsale5);
        sn1 = findViewById(R.id.sd1);
        sn2 = findViewById(R.id.sd2);
        sn3 = findViewById(R.id.sd3);
        sn4 = findViewById(R.id.sd4);
        sn5 = findViewById(R.id.sd5);
        monthyear = findViewById(R.id.month);
        savebtn = findViewById(R.id.ranksavebtn);
        progressDialog = new ProgressDialog(this);
        dbref = FirebaseDatabase.getInstance().getReference("Rank");
        list=new ArrayList<String>();
        adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,list);
        sn1.setAdapter(adapter);
        sn2.setAdapter(adapter);
        sn3.setAdapter(adapter);
        sn4.setAdapter(adapter);
        sn5.setAdapter(adapter);
        fetchdata();
        showRank();

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String saveMonth;
               // Calendar calForDate =  Calendar.getInstance();
                //SimpleDateFormat currentMonth = new SimpleDateFormat("MMMM , yyyy");
                //saveMonth = currentMonth.format(calForDate.getTime());

                String ds1=sn1.getText().toString().trim();
                String ds2=sn2.getText().toString().trim();
                String ds3=sn3.getText().toString().trim();
                String ds4=sn4.getText().toString().trim();
                String ds5=sn5.getText().toString().trim();
                String data1 = ts1.getText().toString().trim();
                String data2 = ts2.getText().toString().trim();
                String data3 = ts3.getText().toString().trim();
                String data4 = ts4.getText().toString().trim();
                String data5 = ts5.getText().toString().trim();

                HashMap<String, String> userMap = new HashMap<>();

                //userMap.put("date", saveMonth);
                userMap.put("rank1", ds1);
                userMap.put("rank2", ds2);
                userMap.put("rank3", ds3);
                userMap.put("rank4", ds4);
                userMap.put("rank5", ds5);
                userMap.put("totalsale1", data1);
                userMap.put("totalsale2", data2);
                userMap.put("totalsale3", data3);
                userMap.put("totalsale4", data4);
                userMap.put("totalsale5", data5);

                root.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(RankDs.this, "Data Saved", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
    }

    private void showRank() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Rank").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                String sale1 = snapshot.child("totalsale1").getValue(String.class);
                String sale2 = snapshot.child("totalsale2").getValue(String.class);
                String sale3 = snapshot.child("totalsale3").getValue(String.class);
                String sale4 = snapshot.child("totalsale4").getValue(String.class);
                String sale5 = snapshot.child("totalsale5").getValue(String.class);
                String ranks1 = snapshot.child("rank1").getValue(String.class);
                String ranks2 = snapshot.child("rank2").getValue(String.class);
                String ranks3 = snapshot.child("rank3").getValue(String.class);
                String ranks4 = snapshot.child("rank4").getValue(String.class);
                String ranks5 = snapshot.child("rank5").getValue(String.class);


                sn1.setText(ranks1);
                sn2.setText(ranks2);
                sn3.setText(ranks3);
                sn4.setText(ranks4);
                sn5.setText(ranks5);
                ts1.setText(sale1);
                ts2.setText(sale2);
                ts3.setText(sale3);
                ts4.setText(sale4);
                ts5.setText(sale5);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void fetchdata()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot mydata : snapshot.getChildren()) {
                    String name = mydata.child("username").getValue(String.class);
                    list.add(name);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}