package com.example.oms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oms.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Startup extends AppCompatActivity {

    private static  int SPLASH_TIMER = 5000;

    ImageView backgroundImage;
    TextView poweredByLine;
    Button button;
    ProgressDialog loading;


    //animations
    Animation sideAnim, bottomAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_startup);


        //hooks
        backgroundImage = findViewById(R.id.bg_image);
        poweredByLine = findViewById(R.id.powered);
        button = findViewById(R.id.joinnow);

        Paper.init(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),LoginTesting
                        .class);
                startActivity(i);

            }
        });

        String Username=Paper.book().read(Prevalent.Username);
        String UserPassword=Paper.book().read(Prevalent.UserPassword);

        if (Username!="" && UserPassword!="")
        {
            if (!TextUtils.isEmpty(Username)&& !TextUtils.isEmpty(UserPassword)){
                AllowAccessId(Username,UserPassword);

                loading.setTitle("Already Logged in");
                loading.setMessage("Please wait...");
                loading.setCanceledOnTouchOutside(false);
                loading.show();
            }
        }


        //anim
        sideAnim = AnimationUtils.loadAnimation(this,R.anim.side_anim);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_anim);

        //set animations

        backgroundImage.setAnimation(sideAnim);
        poweredByLine.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(getApplicationContext(), LoginTesting.class);
                startActivity(intent);
                finish();

            }
        },SPLASH_TIMER);
    }

    private void AllowAccessId(String username, String userPassword) {
        final DatabaseReference Rootref;
        Rootref= FirebaseDatabase.getInstance().getReference();

        Rootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Users").child(username).exists()){
                    UserHelperClass userHelperClass = snapshot.child("Users").child(username).getValue(UserHelperClass.class);

                    if(userHelperClass.getUsername().equals(username)){
                        Toast.makeText(Startup.this,"Hello Dropshippers", Toast.LENGTH_SHORT).show();
                        loading.dismiss();

                        Intent intent = new Intent(Startup.this, MainActivity.class);
                        Prevalent.currentUser=userHelperClass;
                        startActivity(intent);
                    }
                    else {
                        loading.dismiss();
                        Toast.makeText(Startup.this, "Incorrect Password",Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}