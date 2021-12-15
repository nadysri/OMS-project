package com.example.oms.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.oms.LoginTesting;
import com.example.oms.R;
import com.example.oms.RegisterActivity;
import com.example.oms.profileUser;

public class DashboardAdmin extends AppCompatActivity {

    CardView prod, order, ds, sale, rank, invoice;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);

        prod = findViewById(R.id.productbtn);
        order = findViewById(R.id.orderbtn);
        ds = findViewById(R.id.dsbtn);
        btnLogout = findViewById(R.id.btnlogout);

        btnLogout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(DashboardAdmin.this);
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

        prod.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardAdmin.this, ProductAdmin.class);
            startActivity(intent);
        });

        ds.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardAdmin.this, DropshipList.class);
            startActivity(intent);
        });

        order.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardAdmin.this, OrderAdmin.class);
            startActivity(intent);
        });


    }
}