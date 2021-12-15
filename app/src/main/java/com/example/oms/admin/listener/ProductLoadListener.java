package com.example.oms.admin.listener;

import com.example.oms.admin.model.ProductModel;

import java.util.List;

public interface ProductLoadListener {
    void onProductLoadSuccess(List<ProductModel> productModelList);
    void onProductLoadFailed(String message);


}
