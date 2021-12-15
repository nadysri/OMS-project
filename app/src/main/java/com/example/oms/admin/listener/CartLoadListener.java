package com.example.oms.admin.listener;

import com.example.oms.admin.model.CartModel;

import java.util.List;

public interface CartLoadListener {
    void onCartLoadSuccess(List<CartModel> cartModelList);
    void onCartLoadFailed(String message);
}
