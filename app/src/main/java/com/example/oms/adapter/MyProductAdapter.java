package com.example.oms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oms.CatalogueUser;
import com.example.oms.Prevalent.Prevalent;
import com.example.oms.R;
import com.example.oms.admin.eventBus.MyUpdateCart;
import com.example.oms.admin.listener.CartLoadListener;
import com.example.oms.admin.listener.RecyclerViewClickListener;
import com.example.oms.admin.model.CartModel;
import com.example.oms.admin.model.ProductModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MyProductAdapter extends RecyclerView.Adapter<MyProductAdapter.MyDrinkViewHolder> {
    private Context context;
    private List<ProductModel> productModelList;
    private CartLoadListener cartLoadListener;

    public MyProductAdapter(Context context, List<ProductModel> productModelList, CartLoadListener cartLoadListener) {
        this.context = context;
        this.productModelList = productModelList;
        this.cartLoadListener = cartLoadListener;
    }

    @NonNull
    @Override
    public MyDrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyDrinkViewHolder(LayoutInflater.from(context).inflate(R.layout.drink_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyDrinkViewHolder holder, int position) {
        Glide.with(context).load(productModelList.get(position).getImage())
                .into(holder.imageView);
        holder.txtpname.setText(new StringBuilder().append(productModelList.get(position).getPname()));
        holder.txtprice.setText(new StringBuilder("RM").append(productModelList.get(position).getPrice()));
        holder.txtstock.setText(new StringBuilder().append(productModelList.get(position).getStock()));

        holder.setListener((view, adapterPosition) -> {
            addToCart(productModelList.get(position));

        });


    }

    private void addToCart(ProductModel productModel) {
        DatabaseReference userCart = FirebaseDatabase
                .getInstance()
                .getReference("Cart")
                .child(Prevalent.currentUser.getUsername())
                .child("UNIQUE_USER_ID");

        userCart.child(productModel.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) //if have item in cart
                        {
                            CartModel cartModel =snapshot.getValue(CartModel.class);
                            cartModel.setQuantity(cartModel.getQuantity());
                            Map<String,Object> updateData = new HashMap<>();
                            updateData.put("quantity",cartModel.getQuantity());
                            updateData.put("totalPrice",cartModel.getQuantity()*Float.parseFloat(cartModel.getPrice()));

                            userCart.child(productModel.getKey())
                                    .updateChildren(updateData)
                                    .addOnSuccessListener(aVoid -> {
                                        cartLoadListener.onCartLoadFailed("Add to Cart Success");

                                    })
                                    .addOnFailureListener(e -> cartLoadListener.onCartLoadFailed(e.getMessage()));

                        }
                        else { //if not in cart
                            CartModel cartModel = new CartModel();
                            cartModel.setPname(productModel.getPname());
                            cartModel.setImage(productModel.getImage());
                            cartModel.setKey(productModel.getKey());
                            cartModel.setPrice(productModel.getPrice());
                            cartModel.setQuantity(1);
                            cartModel.setTotalPrice(Float.parseFloat(productModel.getPrice()));

                            userCart.child(productModel.getKey())
                                    .setValue(cartModel)
                                    .addOnSuccessListener(aVoid -> {
                                        cartLoadListener.onCartLoadFailed("Add to cart Success");

                                    })
                                    .addOnFailureListener(e -> cartLoadListener.onCartLoadFailed(e.getMessage()));

                        }
                        EventBus.getDefault().postSticky(new MyUpdateCart());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cartLoadListener.onCartLoadFailed(error.getMessage());

                    }
                });
    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public class MyDrinkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.txtpname)
        TextView txtpname;
        @BindView(R.id.txtprice)
        TextView txtprice;
        @BindView(R.id.txtstock)
        TextView txtstock;

        RecyclerViewClickListener listener;

        public void setListener(RecyclerViewClickListener listener) {
            this.listener = listener;
        }

        private Unbinder unbinder;
        public MyDrinkViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onRecyclerClick(v,getBindingAdapterPosition());

        }
    }
}
