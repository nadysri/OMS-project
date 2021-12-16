package com.example.oms;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.oms.Prevalent.Prevalent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;


public class editProfileUser extends AppCompatActivity {
    FloatingActionButton fab;
    ImageView imageView;
    TextInputLayout name, address, email, phone;

    String _NAME,_EMAIL,_ADDRESS,_PHONE,_USERNAME;
    CircularImageView profilepic;

    DatabaseReference reference;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database;


    int SELECT_IMAGE_CODE=1;
    public Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_profile_user);

        reference = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();



        name = findViewById(R.id.layout_first_name);
        address = findViewById(R.id.layout_address);
        email = findViewById(R.id.layout_email);
        phone = findViewById(R.id.layout_phone);
        profilepic = findViewById(R.id.pic);
        fab = findViewById(R.id.floatingActionButton);

        showAllUserData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Title"),SELECT_IMAGE_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            imageView.setImageURI((imageUri));
            uploadPicture();

        }
    }

    private void uploadPicture() {

    }

    private void showAllUserData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").child(Prevalent.currentUser.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                _NAME = snapshot.child("name").getValue(String.class);
                _ADDRESS = snapshot.child("address").getValue(String.class);
                _EMAIL = snapshot.child("email").getValue(String.class);
                _PHONE = snapshot.child("phone").getValue(String.class);
                //_USERNAME = intent.getStringExtra("username");

                name.getEditText().setText(_NAME);
                address.getEditText().setText(_ADDRESS );
                email.getEditText().setText(_EMAIL);
                phone.getEditText().setText(_PHONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void update(View view) {
        if(isNameChanged() || isAddressChanged() || isEmailChanged() || isPhoneChanged()){
            Toast.makeText(this,"Profile been updated",Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this,"Data is same and can not be updated",Toast.LENGTH_LONG).show();

    }

    private boolean isPhoneChanged() {
        if(!_PHONE.equals(phone.getEditText().toString())){

            reference.child(Prevalent.currentUser.getUsername()).child("phone").setValue(phone.getEditText().getText().toString());
            _PHONE = phone.getEditText().getText().toString();
            return true;

        }
        else{
            return false;
        }
    }

    private boolean isEmailChanged() {
        if(!_EMAIL.equals(email.getEditText().getText().toString())){

            reference.child(Prevalent.currentUser.getUsername()).child("email").setValue(email.getEditText().getText().toString());
            _EMAIL = email.getEditText().getText().toString();
            return true;

        }
        else{
            return false;
        }
    }

    private boolean isAddressChanged() {
        if(!_ADDRESS.equals(address.getEditText().getText().toString())){

            reference.child(Prevalent.currentUser.getUsername()).child("address").setValue(address.getEditText().getText().toString());
            _ADDRESS = address.getEditText().getText().toString();
            return true;

        }
        else{
            return false;
        }
    }

    private boolean isNameChanged() {
        if(!_NAME.equals(name.getEditText().getText().toString())){

            reference.child(Prevalent.currentUser.getUsername()).child("name").setValue(name.getEditText().getText().toString());
            _NAME = name.getEditText().getText().toString();
            return true;

        }
        else{
            return false;
        }
    }
}