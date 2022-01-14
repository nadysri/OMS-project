package com.example.oms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button RegisterButton,cancel;
    TextInputLayout regName, regusername,regEmail,regAddress, regPnumber, regPassword, regCode;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RegisterButton= (Button) findViewById(R.id.signup);
        regName = findViewById(R.id.regname);
        regusername = findViewById(R.id.regUsername);
        regEmail = findViewById(R.id.regemail);
        regAddress = findViewById(R.id.regaddress);
        regPnumber = findViewById(R.id.regnumber);
        regPassword = findViewById(R.id.regpassword);
        regCode = findViewById(R.id.regcode);
        loading= new ProgressDialog(this);
        cancel = findViewById(R.id.cancelS);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginTesting.class);
                startActivity(intent);

            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }
        });
    }

    private void SignIn()
    {
        String i_name= regName.getEditText().getText().toString();
        String i_username= regusername.getEditText().getText().toString();
        String i_email= regEmail.getEditText().getText().toString();
        String i_address= regAddress.getEditText().getText().toString();
        String i_number= regPnumber.getEditText().getText().toString();
        String i_password= regPassword.getEditText().getText().toString();
        String i_code= regCode.getEditText().getText().toString();

        if (TextUtils.isEmpty(i_name))
        {
            Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(i_number))
        {
            Toast.makeText(this, "Enter your number", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(i_address))
        {
            Toast.makeText(this, "Enter your address", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(i_email))
        {
            Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(i_username))
        {
            Toast.makeText(this, "Enter your username", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(i_password))
        {
            Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show();
        }
        if (!i_code.equals("AVSN7"))
        {
            Toast.makeText(this, "Please enter the right code", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loading.setTitle("Sign In");
            loading.setMessage("Please wait...");
            loading.setCanceledOnTouchOutside(false);
            loading.show();

            ExistingNumber(i_name, i_number, i_password, i_email, i_address, i_username, i_code);
        }

    }

    private void ExistingNumber(String i_name, String i_number, String i_password, String i_email, String i_address, String i_username, String i_code) {
        final DatabaseReference Rootref;
        Rootref= FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot)
            {
                Date date = new Date();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-M-yyyy");
                final String saveDate = currentDate.format(date);
                if(!(snapshot.child("Users").child(i_username).exists() ))
                {
                    HashMap<String,Object>userdataMap =new HashMap<>();
                    userdataMap.put("name",i_name);
                    userdataMap.put("address",i_address);
                    userdataMap.put("username",i_username);
                    userdataMap.put("email",i_email);
                    userdataMap.put("phone",i_number);
                    userdataMap.put("password",i_password);
                    userdataMap.put("startDate",saveDate);


                    Rootref.child("Users").child(i_username).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(RegisterActivity.this, "Congratulation, you have successfully sign in",Toast.LENGTH_SHORT).show();
                                loading.dismiss();

                                Intent intent = new Intent(RegisterActivity.this, LoginTesting.class);
                                startActivity(intent);
                            }
                            else
                            {
                                loading.dismiss();
                                Toast.makeText(RegisterActivity.this, "Error!try again.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "this" + i_username + "already exists", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please use another username.",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void tos(View view) {
        Intent intent = new Intent(RegisterActivity.this, TermOfServicesActivity.class);
        startActivity(intent);

    }
}
