package com.example.oms;


import android.os.Bundle;
import android.view.View;

public class PopUpActivity extends DialogBaseActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);

        initView();
    }

    private void initView() {
        findViewById(R.id.popup_dialog_exit_btn).setOnClickListener(this);
        findViewById(R.id.popup_bg).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.popup_dialog_exit_btn:
                onBackPressed();
                break;

            case R.id.popup_bg:
                onBackPressed();
                break;
        }
    }
}