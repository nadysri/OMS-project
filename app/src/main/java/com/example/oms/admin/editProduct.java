package com.example.oms.admin;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.oms.CatalogueUser;
import com.example.oms.Leaderboard;
import com.example.oms.MainActivity;
import com.example.oms.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class editProduct extends AppCompatActivity {

    private String key = "";
    private Uri image_uri;
    FirebaseStorage mStorage;
    DatabaseReference productsRef;
    ImageButton imageButton;
    TextInputLayout pnames, descr, prices, stocks;
    Button button, cancel, delete;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    private String productName, productDescription, productStock, productPrice;
    ActivityResultLauncher<String> mGetContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        imageButton = findViewById(R.id.imagebtn);
        pnames = findViewById(R.id.prod_name);
        descr = findViewById(R.id.desc);
        prices = findViewById(R.id.price);
        stocks = findViewById(R.id.stock);
        button = findViewById(R.id.publishbtn);
        cancel = findViewById(R.id.cancelbtn);
        mStorage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(this);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(editProduct.this, ProductAdmin.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        key = getIntent().getStringExtra("key");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Product").child(key);

        loadProductDetails();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });
    }

    private void inputData() {

        productName = pnames.getEditText().getText().toString().trim();
        productDescription = descr.getEditText().getText().toString().trim();
        productStock = stocks.getEditText().getText().toString().trim();
        productPrice = prices.getEditText().getText().toString().trim();


        if (TextUtils.isEmpty(productName)){
            Toast.makeText(this, "Product Name is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(productDescription)){
            Toast.makeText(this, "Product Description is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(productStock)){
            Toast.makeText(this, "Product Stock is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(productPrice)){
            Toast.makeText(this, "Price is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        
        updateProduct();
    }

    private void updateProduct() {
        progressDialog.setMessage("Update product...");
        progressDialog.show();

        if (image_uri == null){
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("pname", ""+productName);
            hashMap.put("description", ""+productDescription);
            hashMap.put("stock", ""+productStock);
            hashMap.put("price", ""+productPrice);


            productsRef.updateChildren(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Toast.makeText(editProduct.this, "Product successfully update!", Toast.LENGTH_SHORT).show();
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(editProduct.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    });
        }
        else {
            String fileAndPath ="imagePost/"+ ""+ key;
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(fileAndPath);
            storageReference.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask =  taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            Uri downlodeImageUri = uriTask.getResult();

                            if (uriTask.isSuccessful()){
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("pname", ""+productName);
                                hashMap.put("description", ""+productDescription);
                                hashMap.put("stock", ""+productStock);
                                hashMap.put("price", ""+productPrice);


                                productsRef.updateChildren(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                progressDialog.dismiss();
                                                Toast.makeText(editProduct.this, "Product Added...", Toast.LENGTH_SHORT).show();
                                            }

                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(editProduct.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(editProduct.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void loadProductDetails() {

        productsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String prductName = ""+dataSnapshot.child("pname").getValue();
                        String prductDescription = ""+dataSnapshot.child("description").getValue();
                        String prductStock = ""+dataSnapshot.child("stock").getValue();
                        String price = ""+dataSnapshot.child("price").getValue();
                        String prductImage = ""+dataSnapshot.child("image").getValue();

                        pnames.getEditText().setText(prductName);
                        descr.getEditText().setText(prductDescription);
                        stocks.getEditText().setText(prductStock);
                        prices.getEditText().setText(price);

                        try {
                            Picasso.get().load(prductImage).placeholder(R.drawable.ic_baseline_add_photo_alternate_24).into(imageButton);
                        }
                        catch (Exception e) {
                            imageButton.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}