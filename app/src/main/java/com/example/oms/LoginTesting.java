package com.example.oms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;

import android.widget.TextView;
import android.widget.Toast;

import com.example.oms.Prevalent.Prevalent;
import com.example.oms.admin.DashboardAdmin;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginTesting extends AppCompatActivity {

    TextInputLayout username, password;
    private Button loginB, create;
    CheckBox rememberMe;
    TextView adminLink, forgetPass;

    ProgressDialog loading;

    String parentDbname= "Users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        loginB = (Button) findViewById(R.id.login);
        username = (TextInputLayout) findViewById(R.id.username);
        password = (TextInputLayout) findViewById(R.id.password);
        create = findViewById(R.id.btn_create);
        adminLink=(TextView) findViewById(R.id.admin_panel); //betulkan
        loading= new ProgressDialog(this);
        rememberMe=(CheckBox) findViewById(R.id.active);
        forgetPass=(TextView) findViewById(R.id.forget_pass);
        Paper.init(this);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginTesting.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginB.setOnClickListener(v -> LoginUser());

       /* adminLink.setOnClickListener(v -> {
            Button loginB = (Button) v.findViewById(R.id.login);
            loginB.setText("ADMIN LOGIN");
            adminLink.setVisibility(View.INVISIBLE);
            parentDbname="Admin";

        });*/

        forgetPass.setOnClickListener(v -> {
            Intent intent = new Intent(LoginTesting.this, ForgetPasswordActivity.class);
            intent.putExtra("check", "login");
            startActivity(intent);
        });

    }

    private void LoginUser()
    {
        String i_username = username.getEditText().getText().toString();
        String i_password = password.getEditText().getText().toString();

        if (TextUtils.isEmpty(i_username)) {
            Toast.makeText(this, "Enter your username", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(i_password)) {
            Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loading.setTitle("Login Account");
            loading.setMessage("Please wait...");
            loading.setCanceledOnTouchOutside(false);
            loading.show();

            AllowAccess(i_username, i_password);
        }
    }

    private void AllowAccess(final String i_username, final String i_password)
    {
        if(rememberMe.isChecked())
        {
            Paper.book().write(Prevalent.Username, i_username);
            Paper.book().write(Prevalent.UserPassword, i_password);
        }

        final DatabaseReference Rootref;
        Rootref= FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDbname).child(i_username).exists())
                {
                    UserHelperClass usersData= snapshot.child(parentDbname).child(i_username).getValue(UserHelperClass.class);

                    if(usersData.getUsername().equals(i_username))
                    {
                        if(usersData.getPassword().equals(i_password))
                        {
                            if(usersData.getUsername().equals("admin"))
                            {
                                Toast.makeText(LoginTesting.this, "Admin successfully logged in.", Toast.LENGTH_SHORT).show();
                                loading.dismiss();

                                Intent intent = new Intent(LoginTesting.this, DashboardAdmin.class);
                                startActivity(intent);
                            }
                            else
                            {
                                SharePref.init(getApplicationContext());
                                SharePref.write(SharePref.LoginName, i_username);

                                Toast.makeText(LoginTesting.this, "Successfully logged in.", Toast.LENGTH_SHORT).show();
                                loading.dismiss();

                                Intent intent = new Intent(LoginTesting.this, MainActivity.class);
                                Prevalent.currentUser= usersData;
                                startActivity(intent);

                            }

                        }
                        else
                        {
                            loading.dismiss();
                            Toast.makeText(LoginTesting.this, "incorrect password!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(LoginTesting.this, "This number "+ i_username +" doesn't exist.", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }));
    }

    public void forgetPassCall(View view) {
        startActivity(new Intent(getApplicationContext(),ForgetPasswordActivity.class));
    }
}
