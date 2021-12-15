package com.example.oms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.oms.Prevalent.Prevalent;
import com.example.oms.adapter.MyProductAdapter;
import com.example.oms.admin.eventBus.MyUpdateCart;
import com.example.oms.admin.listener.CartLoadListener;
import com.example.oms.admin.listener.ProductLoadListener;
import com.example.oms.admin.model.CartModel;
import com.example.oms.admin.model.ProductModel;
import com.example.oms.admin.utils.SpaceItemDecoration;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CatalogueUser extends AppCompatActivity implements ProductLoadListener, CartLoadListener {
    @BindView(R.id.recycler_prod)
    RecyclerView recyclerProduct;
    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;
    @BindView(R.id.badge)
    NotificationBadge badge;
    @BindView(R.id.btnCart)
    FrameLayout btnCart;

    ProductLoadListener productLoadListener;
    CartLoadListener cartLoadListener;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        if(EventBus.getDefault().hasSubscriberForEvent(MyUpdateCart.class));
        EventBus.getDefault().removeStickyEvent(MyUpdateCart.class);
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onUpdateCart(MyUpdateCart event)
    {
        countCartItem();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue_user);


        //initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigationuser);

        //set hom selected
        bottomNavigationView.setSelectedItemId(R.id.nav_catalogue);

        //perform itemselected
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_catalogue:
                        return true;

                    case R.id.nav_track:
                        startActivity(new Intent(getApplicationContext(), TrackerUser.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(), profileUser.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });//closing bottom

        init();
        loadDrinkFromFirebase();
        countCartItem();
    }

    private void loadDrinkFromFirebase() {
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

        btnCart.setOnClickListener(v -> startActivity(new Intent(this,CartActivity.class)));

    }

    @Override
    public void onProductLoadSuccess(List<ProductModel> productModelList) {
        MyProductAdapter adapter = new MyProductAdapter(this,productModelList,cartLoadListener);
        recyclerProduct.setAdapter(adapter);

    }

    @Override
    public void onProductLoadFailed(String message) {
        Snackbar.make(mainLayout,message,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {
        int cartSum = 0;
        for(CartModel cartModel: cartModelList)
            cartSum+= cartModel.getQuantity();
        badge.setNumber(cartSum);

    }

    @Override
    public void onCartLoadFailed(String message) {
        Snackbar.make(mainLayout,message,Snackbar.LENGTH_LONG).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        countCartItem();
    }

    private void countCartItem() {
        List<CartModel> cartModels = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Cart")
                .child(Prevalent.currentUser.getUsername())   //salahhh
                .child("UNIQUE_USER_ID")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot cartSnapshot:snapshot.getChildren())
                        {
                            CartModel cartModel = cartSnapshot.getValue(CartModel.class);
                            cartModel.setKey(cartSnapshot.getKey());
                            cartModels.add(cartModel);

                        }
                        cartLoadListener.onCartLoadSuccess(cartModels);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }
}