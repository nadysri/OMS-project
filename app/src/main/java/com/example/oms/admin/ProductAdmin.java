package com.example.oms.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.oms.R;
import com.example.oms.adapter.ProductAdapterAdmin;
import com.example.oms.admin.listener.CartLoadListener;
import com.example.oms.admin.listener.ProductLoadListener;
import com.example.oms.admin.model.CartModel;
import com.example.oms.admin.model.ProductModel;
import com.example.oms.admin.utils.SpaceItemDecoration;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductAdmin extends AppCompatActivity implements ProductLoadListener, CartLoadListener {
    @BindView(R.id.recycler_prod)
    RecyclerView recyclerProduct;
    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;
    @BindView(R.id.btnadd)
    ImageButton imageButton;
    @BindView(R.id.btnCart)
    FrameLayout btnCart;

    ProductLoadListener productLoadListener;
    CartLoadListener cartLoadListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_admin);

        //initialize and assign variable
        ImageButton imageButton =findViewById(R.id.btnadd);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), addProd.class);
                startActivity(i);

            }
        });




        init();
        loadProductFromFirebase();
    }

    private void loadProductFromFirebase() {
        List<ProductModel> productModels = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Product").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for (DataSnapshot productSnapshot: snapshot.getChildren())
                    {
                        ProductModel productModel = productSnapshot.getValue(ProductModel.class);
                        productModel.setKey(productSnapshot.getKey());
                        productModels.add(productModel);
                    }
                    productLoadListener.onProductLoadSuccess(productModels);
                }
                else
                    productLoadListener.onProductLoadFailed("Can't find product");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                productLoadListener.onProductLoadFailed(error.getMessage());
            }
        });
    }

    private void init() {
        ButterKnife.bind(this);

        productLoadListener=this;
        cartLoadListener=this;


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerProduct.setLayoutManager(gridLayoutManager);
        recyclerProduct.addItemDecoration(new SpaceItemDecoration());
    }

    @Override
    public void onProductLoadSuccess(List<ProductModel> productModelList) {
        ProductAdapterAdmin adapter = new ProductAdapterAdmin(this,productModelList,cartLoadListener);
        recyclerProduct.setAdapter(adapter);
    }

    @Override
    public void onProductLoadFailed(String message) {
        Snackbar.make(mainLayout,message,Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {

    }

    @Override
    public void onCartLoadFailed(String message) {

    }
}