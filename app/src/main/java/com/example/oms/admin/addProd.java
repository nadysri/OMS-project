package com.example.oms.admin;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.oms.R;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class addProd extends AppCompatActivity {
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;
    ImageButton imageButton;
    TextInputLayout pname, descr, price, stock;
    Button button;
    private static final int Gallery_Code=1;
    Uri imageUrl=null;
    ProgressDialog progressDialog;
    ActivityResultLauncher<String>mGetContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prod);

        imageButton = findViewById(R.id.imagebtn);
        pname = findViewById(R.id.prod_name);
        descr = findViewById(R.id.desc);
        price = findViewById(R.id.price);
        stock = findViewById(R.id.stock);
        button = findViewById(R.id.publishbtn);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("Product");
        mStorage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(this);

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                imageButton.setImageURI(result);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });
    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if(!(requestCode==Gallery_Code && resultCode==RESULT_OK)){
                imageUrl=data.getData();
                imageButton.setImageURI(imageUrl);
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pn=pname.getEditText().getText().toString().trim();
                    String dc=descr.getEditText().getText().toString().trim();
                    String pr=price.getEditText().getText().toString().trim();
                    String st=stock.getEditText().getText().toString().trim();

                    if(!(pn.isEmpty() && dc.isEmpty() && pr.isEmpty() && st.isEmpty() && imageUrl!=null))
                    {
                        progressDialog.setTitle("Uploading...");
                        progressDialog.show();

                        StorageReference filepath= mStorage.getReference().child("imagePost").child(imageUrl.getLastPathSegment());
                        filepath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Task<Uri> downloadUrl=taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        String t=task.getResult().toString();

                                        DatabaseReference newPost=mRef.push();

                                        newPost.child("pname").setValue(pn);
                                        newPost.child("description").setValue(dc);
                                        newPost.child("price").setValue(pr);
                                        newPost.child("stock").setValue(st);
                                        newPost.child("image").setValue(task.getResult().toString());
                                        progressDialog.dismiss();

                                        Intent intent=new Intent(addProd.this, ProductAdmin.class);
                                        startActivity(intent);
                                    }

                                });

                            }
                        });
                    }
                }
            });

        }


    }



